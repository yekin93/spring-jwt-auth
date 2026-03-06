ALTER TABLE event_venue ADD COLUMN house_number VARCHAR(50) NOT NULL AFTER street;
ALTER TABLE event_venue ADD COLUMN unit VARCHAR(50) AFTER house_number;
ALTER TABLE event_venue ADD COLUMN country_code VARCHAR(3) NOT NULL AFTER unit