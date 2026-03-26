CREATE TABLE IF NOT EXISTS songs (
    id SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    artist VARCHAR(100) NOT NULL,
    genre VARCHAR(50),
    mood VARCHAR(50) NOT NULL,
    notes VARCHAR(255)
);

INSERT INTO songs (title, artist, genre, mood, notes) VALUES
('505', 'Arctic Monkeys', 'Indie Rock', 'NOSTALGIC', 'Late night vibes'),
('Blinding Lights', 'The Weeknd', 'Pop', 'ENERGETIC', 'Perfect for driving'),
('Someone Like You', 'Adele', 'Pop', 'SAD', 'Heartbreak song'),
('Happy', 'Pharrell Williams', 'Pop', 'HAPPY', 'Feel good song'),
('Weightless', 'Marconi Union', 'Ambient', 'RELAXED', 'Chill music');