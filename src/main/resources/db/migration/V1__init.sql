CREATE SCHEMA IF NOT EXISTS util_sch;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS util_sch.asset_data
(
    id                  uuid DEFAULT uuid_generate_v4(),
    name                      VARCHAR NOT NULL UNIQUE,
    type                      VARCHAR NOT NULL,
    cdn_path                  VARCHAR NOT NULL,
    created_date              DATE NOT NULL,
    version                   INT NOT NULL,
    PRIMARY KEY (id)
);
