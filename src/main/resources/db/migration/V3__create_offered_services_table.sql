CREATE TABLE offered_services
(
    id               UUID PRIMARY KEY        DEFAULT gen_random_uuid(),
    professional_id  UUID           NOT NULL REFERENCES professional_profiles (id),
    name             VARCHAR(100)   NOT NULL,
    description      VARCHAR(300)   NOT NULL,
    price            NUMERIC(12, 2) NOT NULL CHECK ( price >= 0 ),
    duration_minutes INT            NOT NULL,
    created_at       TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP
);