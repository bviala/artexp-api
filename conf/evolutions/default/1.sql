# --- First database schema
# --- !Ups schema creation statements
CREATE TYPE ARTWORK_SOURCE AS ENUM ('artic', 'smk');

CREATE TABLE artwork (
    source ARTWORK_SOURCE NOT NULL,
    id VARCHAR(256) NOT NULL,
    image_src VARCHAR(256) NOT NULL,

    PRIMARY KEY (source, id)
);

CREATE TABLE artexp_user (
    id SERIAL PRIMARY KEY,
    name VARCHAR(32) NOT NULL
);

CREATE TABLE collection (
    user_id INTEGER,
    artwork_source ARTWORK_SOURCE,
    artwork_id VARCHAR(256),

    FOREIGN KEY (artwork_source, artwork_id) REFERENCES artwork (source, id),
    FOREIGN KEY (user_id) REFERENCES artexp_user(id)
);

# --- !Downs schema destruction statements
DROP TABLE IF EXISTS artwork;
DROP TABLE IF EXISTS artexp_user;
DROP TABLE IF EXISTS collection;
DROP TYPE IF EXISTS ARTWORK_SOURCE;