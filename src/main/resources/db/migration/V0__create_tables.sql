CREATE TABLE base_user (
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    provider_uid    VARCHAR(255)    NOT NULL    UNIQUE,
    username        VARCHAR(255)    NOT NULL    UNIQUE,
    created_at      TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE friendship (
    id              UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    requester_id    UUID        NOT NULL,
    receiver_id     UUID        NOT NULL,
    created_at      TIMESTAMP   NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP   NOT NULL    DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (requester_id) REFERENCES base_user(id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id)  REFERENCES base_user(id) ON DELETE CASCADE,

    UNIQUE(requester_id, receiver_id)
);

CREATE INDEX idx_friendship_requester ON friendship(requester_id);
CREATE INDEX idx_friendship_receiver  ON friendship(receiver_id);

CREATE TABLE artist (
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    name            TEXT            NOT NULL,
    type            VARCHAR(50)     NOT NULL,
    country         VARCHAR(2)      NOT NULL,
    genres          TEXT,
    created_at      TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE venue (
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    type            VARCHAR(50)     NOT NULL,
    name            VARCHAR(255)    NOT NULL,
    address         VARCHAR(255)    NOT NULL,
    city            VARCHAR(100)    NOT NULL,
    country         VARCHAR(2)      NOT NULL,
    created_at      TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE event (
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    venue_id        UUID            NOT NULL,
    artist_id       UUID            NOT NULL,
    start_time      TIMESTAMP       NOT NULL,
    end_time        TIMESTAMP       NOT NULL,
    created_at      TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (venue_id) REFERENCES venue(id) ON DELETE CASCADE,
    FOREIGN KEY (artist_id) REFERENCES artist(id) ON DELETE CASCADE
);

CREATE TABLE attendance (
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id        UUID            NOT NULL,
    base_user_id    UUID            NOT NULL,
    score           DECIMAL(4, 2)   NOT NULL,
    description     TEXT,
    has_pictures    BOOLEAN         NOT NULL    DEFAULT FALSE,
    created_at      TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE,
    FOREIGN KEY (base_user_id) REFERENCES base_user(id) ON DELETE CASCADE,

    UNIQUE(event_id, base_user_id)
);

CREATE TABLE attendance_tagged_base_user (
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    attendance_id   UUID            NOT NULL,
    base_user_id    UUID            NOT NULL,
    created_at      TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (attendance_id) REFERENCES attendance(id) ON DELETE CASCADE,
    FOREIGN KEY (base_user_id) REFERENCES base_user(id) ON DELETE CASCADE,

    UNIQUE (attendance_id, base_user_id)
);

CREATE TABLE feed_activity (
    id              UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    attendance_id   UUID        NOT NULL,
    base_user_id    UUID        NOT NULL,
    created_at      TIMESTAMP   NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP   NOT NULL    DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (attendance_id) REFERENCES attendance(id) ON DELETE CASCADE,
    FOREIGN KEY (base_user_id) REFERENCES base_user(id) ON DELETE CASCADE,

    UNIQUE (attendance_id, base_user_id)
)
