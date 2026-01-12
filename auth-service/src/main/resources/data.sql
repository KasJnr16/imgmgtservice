-- =====================================
-- AUTH SERVICE LOGIN SEED (PASSWORD FIXED)
-- =====================================

CREATE TABLE IF NOT EXISTS "users" (
    id UUID PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- Shared password hash (used for ALL users)
-- $2b$12$7hoRZfJrRKD2nIm2vHLs7OBETy.LWenXXMLKf99W8M4PUwO6KB7fu

-- =========================
-- ADMIN
-- =========================
INSERT INTO "users" (id, email, password, role)
SELECT
    '223e4567-e89b-12d3-a456-426614174006'::uuid,
    'admin@test.com',
    '$2b$12$7hoRZfJrRKD2nIm2vHLs7OBETy.LWenXXMLKf99W8M4PUwO6KB7fu',
    'ADMIN'
WHERE NOT EXISTS (
    SELECT 1 FROM "users" WHERE email = 'admin@test.com'
);

-- =========================
-- PATIENTS (10)
-- =========================
INSERT INTO "users" (id, email, password, role)
SELECT
    p.id::uuid,
    p.email,
    '$2b$12$7hoRZfJrRKD2nIm2vHLs7OBETy.LWenXXMLKf99W8M4PUwO6KB7fu',
    'PATIENT'
FROM (
    VALUES
    ('11111111-1111-1111-1111-111111111001', 'patient1@test.com'),
    ('11111111-1111-1111-1111-111111111002', 'patient2@test.com'),
    ('11111111-1111-1111-1111-111111111003', 'patient3@test.com'),
    ('11111111-1111-1111-1111-111111111004', 'patient4@test.com'),
    ('11111111-1111-1111-1111-111111111005', 'patient5@test.com'),
    ('11111111-1111-1111-1111-111111111006', 'patient6@test.com'),
    ('11111111-1111-1111-1111-111111111007', 'patient7@test.com'),
    ('11111111-1111-1111-1111-111111111008', 'patient8@test.com'),
    ('11111111-1111-1111-1111-111111111009', 'patient9@test.com'),
    ('11111111-1111-1111-1111-111111111010', 'patient10@test.com')
) AS p(id, email)
WHERE NOT EXISTS (
    SELECT 1 FROM "users" u WHERE u.email = p.email
);

-- =========================
-- DOCTORS (2)
-- =========================
INSERT INTO "users" (id, email, password, role)
SELECT
    d.id::uuid,
    d.email,
    '$2b$12$7hoRZfJrRKD2nIm2vHLs7OBETy.LWenXXMLKf99W8M4PUwO6KB7fu',
    'DOCTOR'
FROM (
    VALUES
    ('22222222-2222-2222-2222-222222222001', 'doctor1@test.com'),
    ('22222222-2222-2222-2222-222222222002', 'doctor2@test.com')
) AS d(id, email)
WHERE NOT EXISTS (
    SELECT 1 FROM "users" u WHERE u.email = d.email
);

-- =========================
-- RADIOLOGISTS (2)
-- =========================
INSERT INTO "users" (id, email, password, role)
SELECT
    r.id::uuid,
    r.email,
    '$2b$12$7hoRZfJrRKD2nIm2vHLs7OBETy.LWenXXMLKf99W8M4PUwO6KB7fu',
    'RADIOLOGIST'
FROM (
    VALUES
    ('33333333-3333-3333-3333-333333333001', 'radiologist1@test.com'),
    ('33333333-3333-3333-3333-333333333002', 'radiologist2@test.com')
) AS r(id, email)
WHERE NOT EXISTS (
    SELECT 1 FROM "users" u WHERE u.email = r.email
);
