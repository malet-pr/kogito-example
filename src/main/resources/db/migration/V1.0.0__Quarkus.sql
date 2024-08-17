CREATE TABLE IF NOT EXISTS test_entity
(
    id   BIGSERIAL,
    name VARCHAR(20),
    description VARCHAR(200)
);

create sequence hibernate_sequence start 1 increment 1;

INSERT INTO test_entity(name, description)
VALUES ('Quarkus', 'A framework to code Java microservices optimized for cloud.');

