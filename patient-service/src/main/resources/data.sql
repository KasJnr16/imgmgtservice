
-- =========================
-- PATIENT SERVICE SEED SCRIPT
-- =========================

INSERT INTO patient (id, name, email, address, date_of_birth, registered_date)
VALUES
('11111111-1111-1111-1111-111111111001'::uuid, 'Patient One', 'patient1@test.com', 'Accra', '1990-01-01', CURRENT_DATE),
('11111111-1111-1111-1111-111111111002'::uuid, 'Patient Two', 'patient2@test.com', 'Kumasi', '1988-05-12', CURRENT_DATE),
('11111111-1111-1111-1111-111111111003'::uuid, 'Patient Three', 'patient3@test.com', 'Tamale', '1992-03-20', CURRENT_DATE),
('11111111-1111-1111-1111-111111111004'::uuid, 'Patient Four', 'patient4@test.com', 'Cape Coast', '1985-07-15', CURRENT_DATE),
('11111111-1111-1111-1111-111111111005'::uuid, 'Patient Five', 'patient5@test.com', 'Takoradi', '1994-11-02', CURRENT_DATE),
('11111111-1111-1111-1111-111111111006'::uuid, 'Patient Six', 'patient6@test.com', 'Ho', '1989-08-19', CURRENT_DATE),
('11111111-1111-1111-1111-111111111007'::uuid, 'Patient Seven', 'patient7@test.com', 'Sunyani', '1991-04-05', CURRENT_DATE),
('11111111-1111-1111-1111-111111111008'::uuid, 'Patient Eight', 'patient8@test.com', 'Bolgatanga', '1993-06-10', CURRENT_DATE),
('11111111-1111-1111-1111-111111111009'::uuid, 'Patient Nine', 'patient9@test.com', 'Wa', '1987-09-30', CURRENT_DATE),
('11111111-1111-1111-1111-111111111010'::uuid, 'Patient Ten', 'patient10@test.com', 'Techiman', '1995-12-25', CURRENT_DATE)
ON CONFLICT (email) DO NOTHING;
