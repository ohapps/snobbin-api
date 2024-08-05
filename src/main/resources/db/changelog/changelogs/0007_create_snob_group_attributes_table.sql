CREATE TABLE snob_group_attributes (
	id uuid NOT NULL PRIMARY KEY,
	group_id uuid NOT NULL,
	name text NOT NULL
);

GRANT ALL ON TABLE snob_group_attributes TO crud;