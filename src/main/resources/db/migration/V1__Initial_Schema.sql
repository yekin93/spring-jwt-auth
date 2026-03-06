CREATE TABLE event_venue (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
`name` VARCHAR(200) NOT NULL,
address_line1 VARCHAR(200) NOT NULL,
address_line2 VARCHAR(200),
city VARCHAR(120) NOT NULL,
postal_code VARCHAR(20) NOT NULL,
lat DECIMAL(10,7),
lng DECIMAL(10,7),
modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
INDEX IX_event_venue_id (id),
INDEX IX_event_venue_name (`name`),
INDEX IX_event_venue_addres1 (address_line1),
INDEX IX_event_venu_city (city),
INDEX IX_event_venue_postal_code (postal_code)
);

CREATE TABLE `events` (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
organizer_id BIGINT NOT NULL,
venue_id BIGINT,
title VARCHAR(255),
`description` TEXT,
start_at TIMESTAMP,
end_at TIMESTAMP,
timezone VARCHAR(120),
`status` VARCHAR(50) NOT NULL,
visibility VARCHAR(50) NOT NULL,
event_type VARCHAR(30) NOT NULL,
slug VARCHAR(500) UNIQUE NOT NULL,
pricing_type VARCHAR(50) NOT NULL,
price_amount DECIMAL(15, 2) NOT NULL,
currency VARCHAR(5) NOT NULL,
modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
published_by_user BIGINT,
published_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT FK_events_organizer_id
	FOREIGN KEY (organizer_id)
    REFERENCES organizer_profile (id)
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
CONSTRAINT FK_events_venue_id
	FOREIGN KEY (venue_id)
    REFERENCES event_venue (id)
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
CONSTRAINT FK_events_published_by_user
	FOREIGN KEY (published_by_user)
    REFERENCES users (id)
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
INDEX IX_events_id (id),
INDEX IX_events_organizer_id (organizer_id),
INDEX IX_events_venue_id (venue_id),
INDEX IX_events_title (title),
INDEX IX_events_slug (slug),
INDEX IX_events_event_type (event_type),
INDEX IX_events_published_by_user (published_by_user)
);

CREATE TABLE event_media (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
event_id BIGINT NOT NULL,
file_name VARCHAR(255) NOT NULL,
file_ext VARCHAR(10) NOT NULL,
media_type VARCHAR(20) NOT NULL,
is_deleted BOOLEAN DEFAULT FALSE NOT NULL,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT FK_event_media_event_id
	FOREIGN KEY (event_id) 
    REFERENCES `events`(id)
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
INDEX IX_event_media_id (id),
INDEX IX_event_media_file_name (file_name)
);