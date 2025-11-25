-- 1. Création de la table des types de billets (Référence)
CREATE TABLE ticket_type (
    ticket_type_id UUID PRIMARY KEY,
    label VARCHAR(255) NOT NULL UNIQUE
);

-- 2. Création de la table des billets
CREATE TABLE ticket (
    ticket_id UUID PRIMARY KEY,
    
    -- Relations externes (IDs provenant d'autres microservices)
    event_id UUID NOT NULL,
    vendor_id UUID NOT NULL,
    
    -- Relation interne
    ticket_type_id UUID NOT NULL,
    
    -- Informations financières (Double en Java -> DOUBLE PRECISION en Postgres)
    original_price DOUBLE PRECISION NOT NULL,
    sale_price DOUBLE PRECISION,
    
    -- Localisation (Section/Rang/Siège)
    section VARCHAR(255),
    "row" INTEGER, -- "row" est un mot réservé en SQL, il faut souvent des guillemets
    seat VARCHAR(255),
    
    -- Codes et Statut
    barcode VARCHAR(255),
    qr_code VARCHAR(255),

    sale_date TIMESTAMP,
    
    -- Statut basé sur l'Enum TicketStatus (AVAILABLE, RESERVED, SOLD, CANCELED)
    status VARCHAR(50) NOT NULL CHECK (status IN ('AVAILABLE', 'RESERVED', 'SOLD', 'CANCELED')),
    
    -- Timestamp
    creation_date TIMESTAMP,

    -- Contraintes
    CONSTRAINT fk_ticket_type FOREIGN KEY (ticket_type_id) REFERENCES ticket_type(ticket_type_id)
);

-- 3. Index pour optimiser les recherches fréquentes
CREATE INDEX idx_ticket_event ON ticket(event_id);
CREATE INDEX idx_ticket_vendor ON ticket(vendor_id);
CREATE INDEX idx_ticket_status ON ticket(status);