DROP TABLE IF EXISTS users, categories, locations, events, requests, compilations, compilations_events, comments;

    CREATE TABLE IF NOT EXISTS users (
     id                       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
     name	                  VARCHAR(255) NOT NULL,
     email                    VARCHAR(255) UNIQUE NOT NULL
     );

    CREATE TABLE IF NOT EXISTS categories (
     id                       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
     name	                  VARCHAR(50) UNIQUE NOT NULL
     );

     CREATE TABLE IF NOT EXISTS locations (
     location_id              BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY UNIQUE,
     lat                      FLOAT NOT NULL,
     lon                      FLOAT NOT NULL
     );

    CREATE TABLE IF NOT EXISTS events (
     id                       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
     initiator_id	          BIGINT REFERENCES users (id) ON DELETE CASCADE,
     category_id              BIGINT REFERENCES categories (id) ON DELETE RESTRICT,
     confirmed_requests	      INT NOT NULL DEFAULT 0,
     location_id	          BIGINT NOT NULL REFERENCES locations (location_id),
     title                    VARCHAR(120) NOT NULL,
     annotation               VARCHAR(2000) NOT NULL,
     description              VARCHAR(7000) NOT NULL,
     state                    VARCHAR(30) NOT NULL CHECK (state IN ('PENDING', 'PUBLISHED', 'CANCELED')),
     event_date               TIMESTAMP WITHOUT TIME ZONE NOT NULL,
     created_on               TIMESTAMP WITHOUT TIME ZONE NOT NULL,
     published_on             TIMESTAMP WITHOUT TIME ZONE,
     participant_limit        INT NOT NULL DEFAULT 0,
     paid                     BOOLEAN NOT NULL,
     request_moderation       BOOLEAN NOT NULL
     );

     CREATE TABLE IF NOT EXISTS requests(
     id                       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
     requester_id             BIGINT REFERENCES users (id) ON DELETE CASCADE,
     event_id                 BIGINT REFERENCES events (id) ON DELETE CASCADE,
     status                   VARCHAR(30) NOT NULL CHECK (status IN ('CONFIRMED', 'REJECTED', 'PENDING', 'CANCELED')),
     created                  TIMESTAMP WITHOUT TIME ZONE NOT NULL
     );

     CREATE TABLE IF NOT EXISTS compilations (
     id                       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
     title                    VARCHAR(50) NOT NULL,
     pinned                   BOOLEAN NOT NULL
     );

     CREATE TABLE IF NOT EXISTS compilations_events (
     events_id                INTEGER REFERENCES events (id) ON DELETE CASCADE,
     compilation_id           INTEGER REFERENCES compilations (id) ON DELETE CASCADE,
     CONSTRAINT PK_EVENTS_COMPILATIONS PRIMARY KEY(events_id, compilation_id)
     );

     CREATE TABLE IF NOT EXISTS comments (
     id                       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
     text                     VARCHAR(2000) NOT NULL,
     event_id BIGINT          REFERENCES events (id) ON DELETE CASCADE,
     author_id BIGINT         REFERENCES users (id) ON DELETE CASCADE,
     created                  TIMESTAMP WITHOUT TIME ZONE NOT NULL
     );