CREATE TABLE IF NOT EXISTS Books (
    id INTEGER PRIMARY KEY,
    link TEXT,
    title TEXT,
    markedRead INTEGER,
    description TEXT,
    time TEXT
);

CREATE TABLE IF NOT EXISTS Tags (
    id INTEGER PRIMARY KEY,
    book_id INTEGER,
    tag TEXT
);
