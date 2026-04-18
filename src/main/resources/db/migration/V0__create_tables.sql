CREATE TABLE base_user (
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    email           VARCHAR(255)    NOT NULL    UNIQUE,
    provider_uid    VARCHAR(255)    NOT NULL    UNIQUE,
    created_at      TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE friendship_request (
    id              UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    requester_id    UUID        NOT NULL,
    receiver_id     UUID        NOT NULL,
    status          VARCHAR(8)  NOT NULL,
    responded_at    TIMESTAMP,
    created_at      TIMESTAMP   NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP   NOT NULL    DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (requester_id) REFERENCES base_user(id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id)  REFERENCES base_user(id) ON DELETE CASCADE,

    UNIQUE(requester_id, receiver_id)
);

CREATE INDEX idx_friendship_request_requester ON friendship_request(requester_id);
CREATE INDEX idx_friendship_request_receiver  ON friendship_request(receiver_id);

CREATE TABLE artist (
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    name            VARCHAR(255)    NOT NULL    UNIQUE,
    type            VARCHAR(50)     NOT NULL,
    country         VARCHAR(2)      NOT NULL,
    genres          VARCHAR(200),
    created_at      TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE venue (
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    type            VARCHAR(50)     NOT NULL,
    name            VARCHAR(255)    NOT NULL,
    address         VARCHAR(255)    NOT NULL,
    city            VARCHAR(100)    NOT NULL,
    country         VARCHAR(2)    NOT NULL,
    created_at      TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE event (
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    venue_id        UUID            NOT NULL,
    artist_id       UUID            NOT NULL,
    name            VARCHAR(255)    NOT NULL,
    start_time      TIMESTAMP       NOT NULL,
    end_time        TIMESTAMP       NOT NULL,
    created_at      TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (venue_id) REFERENCES venue(id) ON DELETE CASCADE,
    FOREIGN KEY (artist_id) REFERENCES artist(id) ON DELETE CASCADE
);
