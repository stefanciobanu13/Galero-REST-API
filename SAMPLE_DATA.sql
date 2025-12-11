-- Sample Data for Football Competition API Testing
-- This file contains example data to populate your database for testing

-- Insert Editions
INSERT INTO editions (edition_number, date) VALUES 
(1, '2025-01-15'),
(2, '2025-06-20'),
(3, '2025-12-10');

-- Insert Players
INSERT INTO players (first_name, last_name, grade) VALUES 
('John', 'Doe', 8.5),
('Jane', 'Smith', 9.0),
('Michael', 'Johnson', 7.8),
('Sarah', 'Williams', 8.7),
('David', 'Brown', 8.2),
('Emma', 'Jones', 9.2),
('Robert', 'Miller', 7.9),
('Lisa', 'Davis', 8.9);

-- Insert Teams for Edition 1
INSERT INTO teams (edition_id, team_color) VALUES 
(1, 'Red'),
(1, 'Blue'),
(1, 'Green'),
(1, 'Yellow');

-- Insert Teams for Edition 2
INSERT INTO teams (edition_id, team_color) VALUES 
(2, 'Red'),
(2, 'Blue'),
(2, 'Green'),
(2, 'Yellow');

-- Insert Teams for Edition 3
INSERT INTO teams (edition_id, team_color) VALUES 
(3, 'Red'),
(3, 'Blue');

-- Assign Players to Teams (Edition 1)
INSERT INTO team_players (team_id, player_id) VALUES 
(1, 1), (1, 2), (1, 3), (1, 4),
(2, 5), (2, 6), (2, 7), (2, 8),
(3, 1), (3, 5), (3, 3), (3, 7),
(4, 2), (4, 6), (4, 4), (4, 8);

-- Assign Players to Teams (Edition 2)
INSERT INTO team_players (team_id, player_id) VALUES 
(5, 1), (5, 3), (5, 5), (5, 7),
(6, 2), (6, 4), (6, 6), (6, 8),
(7, 1), (7, 2), (7, 3), (7, 4),
(8, 5), (8, 6), (8, 7), (8, 8);

-- Assign Players to Teams (Edition 3)
INSERT INTO team_players (team_id, player_id) VALUES 
(9, 1), (9, 2), (9, 3), (9, 4),
(10, 5), (10, 6), (10, 7), (10, 8);

-- Insert Matches for Edition 1
INSERT INTO matches (edition_id, match_type, stage, team1_id, team2_id, team1_score, team2_score) VALUES 
(1, 'group', 'Round 1', 1, 2, 3, 2),
(1, 'group', 'Round 1', 3, 4, 1, 1),
(1, 'group', 'Round 2', 1, 3, 2, 0),
(1, 'group', 'Round 2', 2, 4, 4, 3),
(1, 'small_final', 'Playoff', 2, 3, 2, 1),
(1, 'big_final', 'Final', 1, 2, 3, 1);

-- Insert Matches for Edition 2
INSERT INTO matches (edition_id, match_type, stage, team1_id, team2_id, team1_score, team2_score) VALUES 
(2, 'group', 'Round 1', 5, 6, 2, 2),
(2, 'group', 'Round 1', 7, 8, 1, 0),
(2, 'group', 'Round 2', 5, 7, 3, 1),
(2, 'group', 'Round 2', 6, 8, 2, 2),
(2, 'small_final', 'Playoff', 6, 7, 1, 0),
(2, 'big_final', 'Final', 5, 6, 2, 1);

-- Insert Matches for Edition 3
INSERT INTO matches (edition_id, match_type, stage, team1_id, team2_id, team1_score, team2_score) VALUES 
(3, 'big_final', 'Final', 9, 10, 2, 2);

-- Record Goals for Edition 1, Match 1 (Team Red 3 - 2 Blue)
INSERT INTO goals (goal_type, match_id, team_id, player_id) VALUES 
('default', 1, 1, 1),
('default', 1, 1, 2),
('penalty', 1, 1, 3),
('default', 1, 2, 5),
('own_goal', 1, 2, 6);

-- Record Goals for Edition 1, Match 2 (Green 1 - 1 Yellow)
INSERT INTO goals (goal_type, match_id, team_id, player_id) VALUES 
('default', 2, 3, 1),
('default', 2, 4, 2);

-- Record Goals for Edition 1, Match 3 (Red 2 - 0 Green)
INSERT INTO goals (goal_type, match_id, team_id, player_id) VALUES 
('default', 3, 1, 2),
('penalty', 3, 1, 3);

-- Record Goals for Edition 1, Match 4 (Blue 4 - 3 Yellow)
INSERT INTO goals (goal_type, match_id, team_id, player_id) VALUES 
('default', 4, 2, 5),
('default', 4, 2, 6),
('default', 4, 2, 7),
('default', 4, 2, 8),
('default', 4, 4, 2),
('default', 4, 4, 4),
('default', 4, 4, 8);

-- Record Goals for Edition 1, Match 5 (Small Final: Blue 2 - 1 Green)
INSERT INTO goals (goal_type, match_id, team_id, player_id) VALUES 
('default', 5, 2, 5),
('default', 5, 2, 6),
('default', 5, 3, 1);

-- Record Goals for Edition 1, Match 6 (Final: Red 3 - 1 Blue)
INSERT INTO goals (goal_type, match_id, team_id, player_id) VALUES 
('default', 6, 1, 1),
('default', 6, 1, 2),
('penalty', 6, 1, 3),
('default', 6, 2, 7);

-- Record Attendance for Edition 1
INSERT INTO attendance (date, player_id, player_first_name, player_last_name, edition_id) VALUES 
('2025-01-15', 1, 'John', 'Doe', 1),
('2025-01-15', 2, 'Jane', 'Smith', 1),
('2025-01-15', 3, 'Michael', 'Johnson', 1),
('2025-01-15', 4, 'Sarah', 'Williams', 1),
('2025-01-15', 5, 'David', 'Brown', 1),
('2025-01-15', 6, 'Emma', 'Jones', 1),
('2025-01-15', 7, 'Robert', 'Miller', 1),
('2025-01-15', 8, 'Lisa', 'Davis', 1);

-- Record Attendance for Edition 2
INSERT INTO attendance (date, player_id, player_first_name, player_last_name, edition_id) VALUES 
('2025-06-20', 1, 'John', 'Doe', 2),
('2025-06-20', 2, 'Jane', 'Smith', 2),
('2025-06-20', 3, 'Michael', 'Johnson', 2),
('2025-06-20', 5, 'David', 'Brown', 2),
('2025-06-20', 6, 'Emma', 'Jones', 2),
('2025-06-20', 7, 'Robert', 'Miller', 2),
('2025-06-20', 8, 'Lisa', 'Davis', 2);

-- Record Attendance for Edition 3
INSERT INTO attendance (date, player_id, player_first_name, player_last_name, edition_id) VALUES 
('2025-12-10', 1, 'John', 'Doe', 3),
('2025-12-10', 2, 'Jane', 'Smith', 3),
('2025-12-10', 3, 'Michael', 'Johnson', 3),
('2025-12-10', 4, 'Sarah', 'Williams', 3),
('2025-12-10', 5, 'David', 'Brown', 3),
('2025-12-10', 6, 'Emma', 'Jones', 3),
('2025-12-10', 7, 'Robert', 'Miller', 3),
('2025-12-10', 8, 'Lisa', 'Davis', 3);

-- View the inserted data (for verification)
-- SELECT * FROM editions;
-- SELECT * FROM players;
-- SELECT * FROM teams;
-- SELECT COUNT(*) as total_team_players FROM team_players;
-- SELECT COUNT(*) as total_matches FROM matches;
-- SELECT COUNT(*) as total_goals FROM goals;
-- SELECT COUNT(*) as total_attendance FROM attendance;

-- Useful queries for testing:

-- Query 1: Total goals scored in Edition 1
-- SELECT COUNT(*) as total_goals FROM goals g
-- JOIN matches m ON g.match_id = m.match_id
-- WHERE m.edition_id = 1;

-- Query 2: Top scorers in Edition 1
-- SELECT p.first_name, p.last_name, COUNT(*) as goals_scored
-- FROM goals g
-- JOIN players p ON g.player_id = p.player_id
-- JOIN matches m ON g.match_id = m.match_id
-- WHERE m.edition_id = 1
-- GROUP BY g.player_id, p.first_name, p.last_name
-- ORDER BY goals_scored DESC;

-- Query 3: Team performance (matches, wins, draws, losses)
-- SELECT t.team_id, t.team_color, COUNT(*) as matches_played
-- FROM matches m
-- JOIN teams t ON m.team1_id = t.team_id OR m.team2_id = t.team_id
-- WHERE m.edition_id = 1
-- GROUP BY t.team_id, t.team_color;

-- Query 4: Player attendance by edition
-- SELECT p.first_name, p.last_name, COUNT(*) as attended
-- FROM attendance a
-- JOIN players p ON a.player_id = p.player_id
-- WHERE a.edition_id = 1
-- GROUP BY a.player_id, p.first_name, p.last_name;
