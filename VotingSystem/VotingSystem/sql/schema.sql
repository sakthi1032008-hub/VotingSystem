CREATE DATABASE IF NOT EXISTS voting_db;
USE voting_db;

CREATE TABLE IF NOT EXISTS voters (
    voter_id  INT PRIMARY KEY,
    name      VARCHAR(100) NOT NULL,
    age       INT NOT NULL,
    city      VARCHAR(100),
    has_voted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS candidates (
    candidate_id INT PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    age          INT NOT NULL,
    party        VARCHAR(100) NOT NULL,
    votes        INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS votes (
    vote_id      INT AUTO_INCREMENT PRIMARY KEY,
    voter_id     INT NOT NULL UNIQUE,          -- one vote per voter
    candidate_id INT NOT NULL,
    voted_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (voter_id)     REFERENCES voters(voter_id),
    FOREIGN KEY (candidate_id) REFERENCES candidates(candidate_id)
);
