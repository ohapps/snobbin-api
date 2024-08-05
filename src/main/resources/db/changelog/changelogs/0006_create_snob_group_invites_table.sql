CREATE TABLE snob_group_invites (
	id uuid NOT NULL PRIMARY KEY,
	group_id uuid NOT NULL,
	email text NOT NULL,
	status text NOT NULL
);

GRANT ALL ON TABLE snob_group_invites TO crud;