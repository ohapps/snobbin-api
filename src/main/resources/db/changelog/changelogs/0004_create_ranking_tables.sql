CREATE TABLE ranking_items (
	id uuid NOT NULL PRIMARY KEY,
	group_id uuid NOT NULL,
	description text NOT NULL,
	ranked boolean NOT NULL,
	average_ranking numeric null,
	CONSTRAINT ranking_items_fk FOREIGN KEY (group_id) REFERENCES snob_groups(id)
);

GRANT ALL ON TABLE ranking_items TO crud;

CREATE TABLE rankings (
	id uuid NOT NULL PRIMARY KEY,
	item_id uuid NOT NULL,
	group_member_id uuid NOT NULL,
	ranking numeric NOT NULL,
	notes text NULL,
	CONSTRAINT rankings_fk FOREIGN KEY (item_id) REFERENCES ranking_items(id)
);

GRANT ALL ON TABLE rankings TO crud;