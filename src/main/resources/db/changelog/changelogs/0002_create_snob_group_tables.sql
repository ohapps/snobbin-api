CREATE TABLE snob_groups (
  id UUID NOT NULL PRIMARY KEY,
  name TEXT NOT NULL,
  description TEXT NOT NULL,
  min_ranking NUMERIC NOT NULL,
  max_ranking NUMERIC NOT NULL,
  increments NUMERIC NOT NULL,
  rank_icon TEXT NOT NULL,
  rankings_required NUMERIC NOT NULL
);

GRANT ALL ON TABLE snob_groups TO crud;

CREATE TABLE snob_group_members (
  id UUID NOT NULL PRIMARY KEY,
  group_id UUID NOT NULL,
  snob_id TEXT NOT NULL,
  role TEXT NOT NULL
);

GRANT ALL ON TABLE snob_group_members TO crud;