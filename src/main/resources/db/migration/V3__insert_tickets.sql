-- V2__insert_tickets.sql
-- Insertion de données de test pour la table 'ticket'
-- Vendeur utilisé : 'vendeur@eventy.com' (ID: e8f04b6c-2d60-4a96-862e-0687d3fa2564)

INSERT INTO ticket (
    ticket_id,
    event_id,
    vendor_id,
    ticket_type_id,
    original_price,
    sale_price,
    section,
    "row",
    seat,
    barcode,
    qr_code,
    sale_date,
    status,
    creation_date
) VALUES 
    -- 1. CONCERT DE NOËL (e4e4...) - Billet Standard - EN VENTE
    (
        'f1f1f1f1-0001-0000-0000-000000000001',           -- Ticket ID
        'e4e4e4e4-e4e4-e4e4-e4e4-e4e4e4e4e4e4',           -- Event: Concert Noël
        'e8f04b6c-2d60-4a96-862e-0687d3fa2564',           -- Vendor: Vendeur
        '11111111-1111-1111-1111-111111111111',           -- Type: Standard
        45.00,                                            -- Prix Original
        50.00,                                            -- Prix de revente (sale_price)
        'Fosse', NULL, NULL,                              -- Localisation
        'BARCODE_NOEL_001', 'QR_NOEL_001',                -- Codes
        NULL,                                             -- Date vente (NULL car disponible)
        'AVAILABLE',                                      -- Statut
        NOW()
    ),

    -- 2. CONCERT DE NOËL (e4e4...) - Billet VIP - EN VENTE
    (
        'f1f1f1f1-0001-0000-0000-000000000002',
        'e4e4e4e4-e4e4-e4e4-e4e4-e4e4e4e4e4e4',
        'e8f04b6c-2d60-4a96-862e-0687d3fa2564',
        '22222222-2222-2222-2222-222222222222',           -- Type: VIP
        120.00,
        140.00,
        'Carré Or', 1, '12',
        'BARCODE_NOEL_VIP_002', 'QR_NOEL_VIP_002',
        NULL,
        'AVAILABLE',
        NOW()
    ),

    -- 3. OL vs ASSE (e6e6...) - Billet Standard - DÉJÀ VENDU (Historique)
    (
        'f1f1f1f1-0001-0000-0000-000000000003',
        'e6e6e6e6-e6e6-e6e6-e6e6-e6e6e6e6e6e6',           -- Event: Derby Foot
        'e8f04b6c-2d60-4a96-862e-0687d3fa2564',
        '11111111-1111-1111-1111-111111111111',           -- Type: Standard
        30.00,
        35.00,
        'Virage Nord', 42, '105',
        'BARCODE_OL_003', 'QR_OL_003',
        NOW() - INTERVAL '2 days',                        -- Vendu il y a 2 jours
        'SOLD',
        NOW() - INTERVAL '5 days'
    ),

    -- 4. AI SUMMIT (e8e8...) - Billet Early Bird - EN VENTE
    (
        'f1f1f1f1-0001-0000-0000-000000000004',
        'e8e8e8e8-e8e8-e8e8-e8e8-e8e8e8e8e8e8',           -- Event: AI Summit
        'e8f04b6c-2d60-4a96-862e-0687d3fa2564',
        '33333333-3333-3333-3333-333333333333',           -- Type: Early Bird
        200.00,
        180.00,                                           -- Revendu moins cher (bonne affaire)
        'Main Hall', NULL, NULL,
        'BARCODE_AI_004', 'QR_AI_004',
        NULL,
        'AVAILABLE',
        NOW()
    ),

    -- 5. THÉÂTRE (e7e7...) - Loge - EN VENTE
    (
        'f1f1f1f1-0001-0000-0000-000000000005',
        'e7e7e7e7-e7e7-e7e7-e7e7-e7e7e7e7e7e7',           -- Event: Théâtre
        'e8f04b6c-2d60-4a96-862e-0687d3fa2564',
        '44444444-4444-4444-4444-444444444444',           -- Type: Loge
        80.00,
        90.00,
        'Balcon', 1, 'Box 4',
        'BARCODE_THEATRE_005', 'QR_THEATRE_005',
        NULL,
        'AVAILABLE',
        NOW()
    )

ON CONFLICT (ticket_id) DO NOTHING;