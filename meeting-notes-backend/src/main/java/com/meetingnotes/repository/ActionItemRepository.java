package com.meetingnotes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meetingnotes.entity.ActionItem;

public interface ActionItemRepository extends JpaRepository<ActionItem, Long> {

    List<ActionItem> findByMeetingId(Long meetingId);
}
