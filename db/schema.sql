
CREATE TABLE IF NOT EXISTS books (
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title        VARCHAR(255) NOT NULL,
    author       VARCHAR(255) NOT NULL,
    year         INTEGER      NOT NULL,
    is_available BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS members (
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS borrowings (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    book_id     BIGINT    NOT NULL REFERENCES books (id),
    member_id   BIGINT    NOT NULL REFERENCES members (id),
    borrowed_at TIMESTAMP NOT NULL DEFAULT NOW(),
    returned_at TIMESTAMP
);

-- Each book can have at most one active (not yet returned) borrowing
CREATE UNIQUE INDEX IF NOT EXISTS one_active_borrowing_per_book
    ON borrowings (book_id)
    WHERE returned_at IS NULL;