-- Keep users table compatible with modules.identity.Entities.User mapping.
-- This script is idempotent and safe to run on each startup.

CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY
);

ALTER TABLE users ADD COLUMN IF NOT EXISTS email VARCHAR(150);
ALTER TABLE users ADD COLUMN IF NOT EXISTS password_hash VARCHAR(255);
ALTER TABLE users ADD COLUMN IF NOT EXISTS role VARCHAR(32);
ALTER TABLE users ADD COLUMN IF NOT EXISTS first_name VARCHAR(100);
ALTER TABLE users ADD COLUMN IF NOT EXISTS last_name VARCHAR(100);
ALTER TABLE users ADD COLUMN IF NOT EXISTS phone VARCHAR(20);
ALTER TABLE users ADD COLUMN IF NOT EXISTS location VARCHAR(255);
ALTER TABLE users ADD COLUMN IF NOT EXISTS profile_photo VARCHAR(500);
ALTER TABLE users ADD COLUMN IF NOT EXISTS is_email_verified BOOLEAN;
ALTER TABLE users ADD COLUMN IF NOT EXISTS is_phone_verified BOOLEAN;
ALTER TABLE users ADD COLUMN IF NOT EXISTS is_mfa_enabled BOOLEAN;
ALTER TABLE users ADD COLUMN IF NOT EXISTS status VARCHAR(20);
ALTER TABLE users ADD COLUMN IF NOT EXISTS created_at TIMESTAMP;
ALTER TABLE users ADD COLUMN IF NOT EXISTS last_login TIMESTAMP;

-- Legacy compatibility with older schema versions that still have users.name.
ALTER TABLE users ADD COLUMN IF NOT EXISTS name VARCHAR(255);
UPDATE users
SET name = COALESCE(name, NULLIF(CONCAT_WS(' ', first_name, last_name), ''), email)
WHERE name IS NULL;
ALTER TABLE users ALTER COLUMN name DROP NOT NULL;
