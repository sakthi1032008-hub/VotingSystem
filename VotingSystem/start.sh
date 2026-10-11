#!/bin/bash
cd "$(dirname "$(dirname "$(find /workspaces -name schema.sql | head -1)")")" || exit 1

# install Java and MySQL only if missing
which javac >/dev/null 2>&1 || { sudo apt update; sudo apt install -y default-jdk; }
which mysqld >/dev/null 2>&1 || { sudo apt update; sudo apt install -y mysql-server; }

# start MySQL
sudo mkdir -p /var/run/mysqld
sudo chown mysql:mysql /var/run/mysqld
sudo service mysql start
sleep 8

# set root password (works on first run and later runs)
SQL="ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'root123'; FLUSH PRIVILEGES;"
sudo mysql -e "$SQL" 2>/dev/null || sudo mysql -proot123 -e "$SQL" 2>/dev/null

# create database and tables (safe to repeat)
mysql -h 127.0.0.1 -u root -proot123 < sql/schema.sql 2>/dev/null

# put the password in the code
sed -i 's/your_password/root123/' src/com/voting/db/DBConnection.java

# download the driver if missing
mkdir -p lib
ls lib/*.jar >/dev/null 2>&1 || wget -q https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.4.0/mysql-connector-j-8.4.0.jar -P lib

# compile and run
javac -cp "lib/*" -d out $(find src -name "*.java") && java -cp "out:lib/*" com.voting.main.Main
