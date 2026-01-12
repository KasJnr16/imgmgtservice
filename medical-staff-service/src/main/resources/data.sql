
-- =========================
-- MEDICAL STAFF SERVICE SEED SCRIPT
-- =========================

INSERT INTO medical_staff (id, name, email, address, date_of_birth, registered_date)
VALUES
('22222222-2222-2222-2222-222222222001'::uuid, 'Dr. John Doe', 'doctor1@test.com', 'Accra', '1980-02-14', CURRENT_DATE),
('22222222-2222-2222-2222-222222222002'::uuid, 'Dr. Jane Smith', 'doctor2@test.com', 'Kumasi', '1983-06-21', CURRENT_DATE),
('33333333-3333-3333-3333-333333333001'::uuid, 'Dr. Alex Ray', 'radiologist1@test.com', 'Tema', '1979-09-09', CURRENT_DATE),
('33333333-3333-3333-3333-333333333002'::uuid, 'Dr. Sarah Scan', 'radiologist2@test.com', 'Takoradi', '1985-11-18', CURRENT_DATE)
ON CONFLICT (email) DO NOTHING;