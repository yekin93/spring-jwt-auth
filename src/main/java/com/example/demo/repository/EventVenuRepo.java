package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.EventVenue;

public interface EventVenuRepo extends JpaRepository<EventVenue, Long> {

}
