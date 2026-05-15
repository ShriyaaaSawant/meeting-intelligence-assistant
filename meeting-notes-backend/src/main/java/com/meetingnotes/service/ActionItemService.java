package com.meetingnotes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meetingnotes.entity.ActionItem;
import com.meetingnotes.entity.Meeting;
import com.meetingnotes.exception.ResourceNotFoundException;
import com.meetingnotes.repository.ActionItemRepository;
import com.meetingnotes.repository.MeetingRepository;

@Service
public class ActionItemService {

    @Autowired
    private ActionItemRepository actionRepo;

    @Autowired
    private MeetingRepository meetingRepo;

    // Add action
    public ActionItem addActionItem(Long meetingId, ActionItem actionItem) {

        Meeting meeting = meetingRepo.findById(meetingId)
                .orElseThrow(() -> new ResourceNotFoundException("Meeting not found with id: " + meetingId));

        actionItem.setMeeting(meeting);
        actionItem.setStatus("PENDING");

        return actionRepo.save(actionItem);
    }

    // Get actions by meeting
    public List<ActionItem> getActionsByMeeting(Long meetingId) {
        return actionRepo.findByMeetingId(meetingId);
    }

    // Update status
    public ActionItem updateStatus(Long actionId, String status) {

        ActionItem action = actionRepo.findById(actionId)
                .orElseThrow(() -> new ResourceNotFoundException("Action item not found with id: " + actionId));

        action.setStatus(status);

        return actionRepo.save(action);
    }
}
