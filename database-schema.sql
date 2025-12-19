CREATE DATABASE IF NOT EXISTS `fotball-db`;
USE fotball-db;

-- Table for competition editions
CREATE TABLE editions (
    edition_id INT PRIMARY KEY AUTO_INCREMENT,
    edition_number INT NOT NULL UNIQUE,
    date DATE NOT NULL
);

-- Table for players
CREATE TABLE players (
    player_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    grade DOUBLE NOT NULL,
    UNIQUE KEY unique_player_name (first_name, last_name)
);

-- Table for teams (per edition)
CREATE TABLE teams (
    team_id INT PRIMARY KEY AUTO_INCREMENT,
    edition_id INT NOT NULL,
    team_color VARCHAR(30) NOT NULL,
    FOREIGN KEY (edition_id) REFERENCES editions(edition_id) ON DELETE CASCADE
);

-- Junction table for players in teams (per edition)
CREATE TABLE team_players (
    team_id INT NOT NULL,
    player_id INT NOT NULL,
    PRIMARY KEY (team_id, player_id),
    FOREIGN KEY (team_id) REFERENCES teams(team_id) ON DELETE CASCADE,
    FOREIGN KEY (player_id) REFERENCES players(player_id)
);

-- Table for matches
CREATE TABLE matches (
    match_id INT PRIMARY KEY AUTO_INCREMENT,
    edition_id INT NOT NULL,
    match_type ENUM('group', 'small_final', 'big_final') NOT NULL DEFAULT 'group',
    stage VARCHAR(20), -- "Round 1", "Big Final", etc.
    team1_id INT NOT NULL,
    team2_id INT NOT NULL,
    team1_score INT,
    team2_score INT,
    FOREIGN KEY (edition_id) REFERENCES editions(edition_id) ON DELETE CASCADE,
    FOREIGN KEY (team1_id) REFERENCES teams(team_id) ON DELETE CASCADE,
    FOREIGN KEY (team2_id) REFERENCES teams(team_id) ON DELETE CASCADE
);

-- Table for goals
CREATE TABLE goals (
    goal_id INT PRIMARY KEY AUTO_INCREMENT,
    goal_type ENUM('default', 'penalty', 'own_goal') NOT NULL DEFAULT 'default',
    match_id INT NOT NULL,
    team_id INT NOT NULL,
    player_id INT NOT NULL,
    FOREIGN KEY (match_id) REFERENCES matches(match_id) ON DELETE CASCADE,
    FOREIGN KEY (team_id) REFERENCES teams(team_id) ON DELETE CASCADE,
    FOREIGN KEY (player_id) REFERENCES players(player_id)
);

-- Attendance for players
CREATE TABLE attendance (
    attendance_id INT PRIMARY KEY AUTO_INCREMENT,
    date DATE NOT NULL,
    player_id INT NOT NULL,
    player_first_name VARCHAR(50) NOT NULL,
    player_last_name VARCHAR(50) NOT NULL,
    edition_id INT NOT NULL,
    FOREIGN KEY (player_id) REFERENCES players(player_id),
    FOREIGN KEY (edition_id) REFERENCES editions(edition_id) ON DELETE CASCADE
);