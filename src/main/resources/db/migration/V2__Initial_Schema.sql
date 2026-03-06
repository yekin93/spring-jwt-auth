ALTER TABLE event_venue ADD COLUMN organizer_id BIGINT AFTER id;

ALTER TABLE event_venue ADD CONSTRAINT FK_event_venue_organizer_id 
FOREIGN KEY (organizer_id) REFERENCES organizer_profile(id) ON UPDATE NO ACTION ON DELETE NO ACTION;