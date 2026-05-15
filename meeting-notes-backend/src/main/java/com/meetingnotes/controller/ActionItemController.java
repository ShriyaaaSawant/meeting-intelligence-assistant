package com.meetingnotes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.meetingnotes.entity.ActionItem;
import com.meetingnotes.service.ActionItemService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/actions")
public class ActionItemController {

    @Autowired
    private ActionItemService actionService;

    // ADD ACTION
    @PostMapping("/{meetingId}")
    public ResponseEntity<ActionItem> addAction(
            @PathVariable Long meetingId,
            @Valid @RequestBody ActionItem actionItem) {

        ActionItem createdAction = actionService.addActionItem(meetingId, actionItem);
        return new ResponseEntity<>(createdAction, HttpStatus.CREATED);
    }

    // GET ACTIONS BY MEETING
    @GetMapping("/meeting/{meetingId}")
    public ResponseEntity<List<ActionItem>> getActions(@PathVariable Long meetingId) {

        List<ActionItem> actions = actionService.getActionsByMeeting(meetingId);
        return ResponseEntity.ok(actions);
    }

    // UPDATE STATUS
    @PutMapping("/{id}")
    public ResponseEntity<ActionItem> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        ActionItem updatedAction = actionService.updateStatus(id, status);
        return ResponseEntity.ok(updatedAction);
    }
}

