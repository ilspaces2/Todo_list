create table users
(
    id       SERIAL PRIMARY KEY,
    userName VARCHAR NOT NULL,
    pass     VARCHAR NOT NULL,
    unique (userName, pass)
);
create table items
(
    id          SERIAL PRIMARY KEY,
    itemName    VARCHAR   NOT NULL,
    description VARCHAR   NOT NULL,
    created     TIMESTAMP NOT NULL,
    finished    TIMESTAMP,
    done        boolean   NOT NULL,
    userId      INT       NOT NULL REFERENCES users (id)
);
