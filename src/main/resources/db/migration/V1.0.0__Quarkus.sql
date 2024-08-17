CREATE TABLE IF NOT EXISTS test_entity
(
    id   BIGSERIAL,
    name VARCHAR(20),
    description VARCHAR(200)
);


INSERT INTO test_entity(name, description)
VALUES ('Quarkus', 'A framework to code Java microservices optimized for cloud.');

