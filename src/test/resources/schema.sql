CREATE TABLE IF NOT EXISTS family (
  id int NOT NULL,
  last_name varchar(255) DEFAULT NULL,
  street varchar(255) DEFAULT NULL,
  postal_code varchar(255) DEFAULT NULL,
  city varchar(255) DEFAULT NULL,
  contact1 varchar(255) DEFAULT NULL,
  contact2 varchar(255) DEFAULT NULL,
  contact3 varchar(255) DEFAULT NULL,
  remarks varchar(255) DEFAULT NULL,
  last_update timestamp DEFAULT NULL
);

ALTER TABLE family ADD PRIMARY KEY (id);

CREATE TABLE IF NOT EXISTS person (
  id int NOT NULL,
  first_name varchar(255) DEFAULT NULL,
  last_name varchar(255) DEFAULT NULL,
  birthday varchar(255) DEFAULT NULL,
  contact1 varchar(255) DEFAULT NULL,
  contact2 varchar(255) DEFAULT NULL,
  contact3 varchar(255) DEFAULT NULL,
  remarks varchar(255) DEFAULT NULL,
  family_id int DEFAULT NULL,
  last_update timestamp DEFAULT NULL,
  ordering int DEFAULT NULL
);

ALTER TABLE person ADD PRIMARY KEY (id);
ALTER TABLE person ADD FOREIGN KEY (family_id) REFERENCES FAMILY;

CREATE TABLE id_gen (
    gen_key VARCHAR(255) NOT NULL,
    gen_value BIGINT NOT NULL,
    PRIMARY KEY (gen_key)
);
