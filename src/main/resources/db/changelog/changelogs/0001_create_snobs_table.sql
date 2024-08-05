CREATE TABLE snobs (
  id TEXT NOT NULL PRIMARY KEY,
  email TEXT NOT NULL,
  first_name TEXT NULL,
  last_name TEXT NULL
);

GRANT ALL ON TABLE snobs TO crud;