1. Run sql/schema.sql in MySQL.
2. Edit PASS in src/com/voting/db/DBConnection.java.
3. Download mysql-connector-j JAR and put it in a lib/ folder.

Compile (from project root, Linux/Mac):
  javac -cp "lib/*" -d out $(find src -name "*.java")
Compile (Windows):
  dir /s /b src\*.java > files.txt
  javac -cp "lib/*" -d out @files.txt

Run (Linux/Mac):  java -cp "out:lib/*" com.voting.main.Main
Run (Windows):    java -cp "out;lib/*" com.voting.main.Main
