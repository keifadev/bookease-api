CREATE TABLE professional_profiles
(
    id                        UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    user_id                   UUID         NOT NULL REFERENCES users (id) UNIQUE,
    bio                       VARCHAR(500) NOT NULL,
    specialty                 VARCHAR(50)  NOT NULL,
    cancellation_policy_hours INT          NOT NULL DEFAULT 2,
    created_at                TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);