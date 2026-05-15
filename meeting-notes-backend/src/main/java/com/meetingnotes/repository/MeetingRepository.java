package com.meetingnotes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meetingnotes.entity.Meeting;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
}
