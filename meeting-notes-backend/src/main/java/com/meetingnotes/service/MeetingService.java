package com.meetingnotes.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meetingnotes.entity.ActionItem;
import com.meetingnotes.entity.Meeting;
import com.meetingnotes.repository.ActionItemRepository;
import com.meetingnotes.repository.MeetingRepository;
import com.meetingnotes.exception.ResourceNotFoundException;

@Service
public class MeetingService {

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private ActionItemRepository actionItemRepository;

    // ✅ CREATE MEETING
    public Meeting createMeeting(Meeting meeting) {

        String transcript = meeting.getTranscript();
        String[] sentences = transcript.split("\\.");

        // ----------------------
// 🧠 IMPROVED SUMMARY
// ----------------------
        List<String> summaryPoints = new ArrayList<>();

        for (String s : sentences) {

            if (s == null || s.trim().isEmpty()) {
                continue;
            }

            String lower = s.toLowerCase();

            if (lower.contains("update")
                    || lower.contains("issue")
                    || lower.contains("need")
                    || lower.contains("deploy")
                    || lower.contains("discuss")
                    || lower.contains("important")
                    || lower.contains("fix")
                    || lower.contains("review")) {

                String cleaned = s.trim();

                // Remove names (reuse your method)
                String assignee = extractAssignee(cleaned);
                if (cleaned.toLowerCase().startsWith(assignee.toLowerCase())) {
                    cleaned = cleaned.substring(assignee.length()).trim();
                }

                // Remove unnecessary words
                cleaned = cleaned
                        .replaceAll("(?i)please", "")
                        .replaceAll("(?i)can you", "")
                        .replaceAll("(?i)we need to", "")
                        .trim();

                // Make sentence look like summary
                cleaned = Character.toUpperCase(cleaned.charAt(0)) + cleaned.substring(1);

                summaryPoints.add("• " + cleaned);
            }
        }

// fallback
        if (summaryPoints.isEmpty()) {
            for (int i = 0; i < Math.min(3, sentences.length); i++) {
                if (!sentences[i].trim().isEmpty()) {
                    summaryPoints.add("• " + sentences[i].trim());
                }
            }
        }

// final summary
        String summary = "Summary:\n" + String.join("\n", summaryPoints);
        meeting.setSummary(summary);

        // ✅ Save meeting
        Meeting savedMeeting = meetingRepository.save(meeting);

        // ----------------------
        // ✅ ACTION ITEMS
        // ----------------------
        List<ActionItem> actions = new ArrayList<>();

        for (String s : sentences) {

            if (s == null || s.trim().isEmpty()) {
                continue;
            }

            String lower = s.toLowerCase().trim();

            if (lower.contains("update")
                    || lower.contains("issue")
                    || lower.contains("need")
                    || lower.contains("deploy")
                    || lower.contains("discuss")
                    || lower.contains("important")
                    || lower.contains("fix")
                    || lower.contains("review")
                    || lower.contains("documentation")
                    || lower.contains("handle")
                    || lower.contains("create")
                    || lower.contains("complete")) {

                ActionItem action = buildActionItem(s.trim(), savedMeeting);
                actions.add(action);
            }
        }

        // ✅ fallback
        if (actions.isEmpty()) {
            ActionItem a = new ActionItem();
            a.setDescription("Review discussion and take necessary actions");
            a.setStatus("PENDING");
            a.setAssignee("Team");
            a.setDeadline(LocalDate.now().plusDays(2));
            a.setMeeting(savedMeeting);
            actions.add(a);
        }

        actionItemRepository.saveAll(actions);
        savedMeeting.setActionItems(actions);

        return savedMeeting;
    }

    // ----------------------
    // 👤 EXTRACT ASSIGNEE
    // ----------------------
    private String extractAssignee(String sentence) {
        String[] words = sentence.split(" ");

        for (String word : words) {

            // skip common words
            if (word.equalsIgnoreCase("please") || word.equalsIgnoreCase("can")) {
                continue;
            }

            if (word.length() > 0 && Character.isUpperCase(word.charAt(0))) {
                return word;
            }
        }
        return "Team";
    }

    // ----------------------
    // 📅 EXTRACT DEADLINE
    // ----------------------
    private LocalDate extractDeadline(String sentence) {

        sentence = sentence.toLowerCase();
        LocalDate today = LocalDate.now();

        if (sentence.contains("today")) {
            return today;
        }
        if (sentence.contains("tomorrow")) {
            return today.plusDays(1);
        }
        if (sentence.contains("next week")) {
            return today.plusWeeks(1);
        }

        // in X days
        Pattern pattern = Pattern.compile("in (\\d+) day");
        Matcher matcher = pattern.matcher(sentence);
        if (matcher.find()) {
            int days = Integer.parseInt(matcher.group(1));
            return today.plusDays(days);
        }

        // weekdays
        if (sentence.contains("monday")) {
            return today.with(DayOfWeek.MONDAY);
        }
        if (sentence.contains("tuesday")) {
            return today.with(DayOfWeek.TUESDAY);
        }
        if (sentence.contains("wednesday")) {
            return today.with(DayOfWeek.WEDNESDAY);
        }
        if (sentence.contains("thursday")) {
            return today.with(DayOfWeek.THURSDAY);
        }
        if (sentence.contains("friday")) {
            return today.with(DayOfWeek.FRIDAY);
        }

        return today.plusDays(2); // fallback
    }

    // ----------------------
    // 🔥 BUILD ACTION ITEM
    // ----------------------
    private ActionItem buildActionItem(String sentence, Meeting meeting) {

        ActionItem action = new ActionItem();

        // ✅ Assignee
        String assignee = extractAssignee(sentence);
        action.setAssignee(assignee);

        // ✅ Deadline (ONLY from method — no override)
        LocalDate deadline = extractDeadline(sentence);
        action.setDeadline(deadline);

        // ✅ Clean description
        String cleaned = sentence
                .replaceAll("(?i)can you", "")
                .replaceAll("(?i)please", "")
                .replaceAll("(?i)we need to", "")
                .replaceAll("(?i)make sure", "")
                .replaceAll("(?i)handle", "")
                .trim();

        if (cleaned.toLowerCase().startsWith(assignee.toLowerCase())) {
            cleaned = cleaned.substring(assignee.length()).trim();
        }

        action.setDescription(cleaned);
        action.setStatus("PENDING");
        action.setMeeting(meeting);

        return action;
    }

    // ✅ GET ALL
    public List<Meeting> getAllMeetings() {
        return meetingRepository.findAll();
    }

    // ✅ GET BY ID
    public Meeting getMeetingById(Long id) {
        return meetingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meeting not found with id: " + id));
    }

    // ✅ UPDATE
    public Meeting updateMeeting(Long id, Meeting updatedMeeting) {

        Meeting existing = meetingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meeting not found with id: " + id));

        existing.setTitle(updatedMeeting.getTitle());
        existing.setTranscript(updatedMeeting.getTranscript());
        existing.setSummary(updatedMeeting.getSummary());

        return meetingRepository.save(existing);
    }
}
