CREATE TABLE IF NOT EXISTS members
(
    id    INTEGER PRIMARY KEY AUTOINCREMENT,
    name  TEXT NOT NULL CHECK (length(trim(name)) > 0),
    email TEXT NOT NULL UNIQUE COLLATE NOCASE
);

CREATE TABLE IF NOT EXISTS books
(
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    title       TEXT    NOT NULL CHECK (length(trim(title)) > 0),
    author      TEXT    NOT NULL CHECK (length(trim(author)) > 0),
    year        INTEGER NOT NULL CHECK (year >= 1),
    borrowed_by INTEGER REFERENCES members (id) ON DELETE RESTRICT
);