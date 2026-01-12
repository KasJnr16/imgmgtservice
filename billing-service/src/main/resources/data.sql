-- =========================
-- BILLING ACCOUNT SEED DATA
-- =========================

INSERT INTO billing_account (id, patient_id, account_name, created_at)
VALUES
('22222222-2222-2222-2222-222222222001'::uuid, '11111111-1111-1111-1111-111111111001'::uuid, 'Billing Account - Patient One', NOW()),
('22222222-2222-2222-2222-222222222002'::uuid, '11111111-1111-1111-1111-111111111002'::uuid, 'Billing Account - Patient Two', NOW()),
('22222222-2222-2222-2222-222222222003'::uuid, '11111111-1111-1111-1111-111111111003'::uuid, 'Billing Account - Patient Three', NOW()),
('22222222-2222-2222-2222-222222222004'::uuid, '11111111-1111-1111-1111-111111111004'::uuid, 'Billing Account - Patient Four', NOW()),
('22222222-2222-2222-2222-222222222005'::uuid, '11111111-1111-1111-1111-111111111005'::uuid, 'Billing Account - Patient Five', NOW()),
('22222222-2222-2222-2222-222222222006'::uuid, '11111111-1111-1111-1111-111111111006'::uuid, 'Billing Account - Patient Six', NOW()),
('22222222-2222-2222-2222-222222222007'::uuid, '11111111-1111-1111-1111-111111111007'::uuid, 'Billing Account - Patient Seven', NOW()),
('22222222-2222-2222-2222-222222222008'::uuid, '11111111-1111-1111-1111-111111111008'::uuid, 'Billing Account - Patient Eight', NOW()),
('22222222-2222-2222-2222-222222222009'::uuid, '11111111-1111-1111-1111-111111111009'::uuid, 'Billing Account - Patient Nine', NOW()),
('22222222-2222-2222-2222-222222222010'::uuid, '11111111-1111-1111-1111-111111111010'::uuid, 'Billing Account - Patient Ten', NOW())
ON CONFLICT (patient_id) DO NOTHING;
