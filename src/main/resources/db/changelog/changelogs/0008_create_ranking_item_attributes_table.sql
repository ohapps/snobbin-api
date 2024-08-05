CREATE TABLE ranking_item_attributes (
	id uuid NOT NULL PRIMARY KEY,
	item_id uuid NOT NULL,
	attribute_id uuid NOT NULL,
	attribute_value text NOT NULL
);

GRANT ALL ON TABLE ranking_item_attributes TO crud;