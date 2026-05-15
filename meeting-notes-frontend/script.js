let currentMeetingId = null;
// ✅ CREATE MEETING
async function generateNotes() {

    const transcript = document.getElementById("transcript").value;

    const response = await fetch("http://localhost:8080/meetings", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            title: "Meeting",
            transcript: transcript
        })
    });

    const data = await response.json();

    currentMeetingId = data.id; // ✅ FIX

    displayMeeting(data);
}


// ✅ DISPLAY MEETING
function displayMeeting(data) {

    document.getElementById("summary").value = data.summary || "No summary available";
    const actionsList = document.getElementById("actions");
    actionsList.innerHTML = "";

    (data.actionItems || []).forEach(action => {

        const li = document.createElement("li");

        li.innerHTML = `
            <b>${action.assignee}</b> → ${action.description} <br>
            📅 ${formatDate(action.deadline)} <br>

            <button class="status-btn"
               onclick="toggleStatus(${action.id}, '${action.status}', this)">
                ${action.status === 'DONE' ? '✅ Done' : '⏳ Pending'}
            </button>
        `;

        actionsList.appendChild(li);
    });
}


// ✅ LOAD ALL MEETINGS
async function loadMeetingById(id) {

    if (!id) return; // ✅ safety

    currentMeetingId = id;

    const meetingRes = await fetch(`http://localhost:8080/meetings/${id}`);
    const meeting = await meetingRes.json();

    const actionsRes = await fetch(`http://localhost:8080/actions/meeting/${id}`);
    const actions = await actionsRes.json();

    meeting.actionItems = actions || [];

    displayMeeting(meeting);
}


// ✅ LOAD SINGLE MEETING
async function loadMeetings() {

    const response = await fetch("http://localhost:8080/meetings");
    const meetings = await response.json();

    const list = document.getElementById("meetingsList");
    list.innerHTML = "";

    meetings.forEach(m => {

        const li = document.createElement("li");

        li.innerHTML = `
            <b>${m.title || "Untitled Meeting"}</b><br>
            <small>${(m.summary || "No summary").substring(0, 60)}...</small>
        `;

        li.style.cursor = "pointer";

        li.onclick = () => loadMeetingById(m.id);

        list.appendChild(li);
    });
}

async function searchMeeting() {

    const id = document.getElementById("searchId").value;

    if (!id) {
        alert("Enter meeting ID");
        return;
    }

    loadMeetingById(parseInt(id)); // ✅ FIX
}


// ✅ TOGGLE STATUS
async function toggleStatus(id, currentStatus) {

    const newStatus = currentStatus === "DONE" ? "PENDING" : "DONE";

     console.log("Current:", currentStatus, "New:", newStatus);
    await fetch(`http://localhost:8080/actions/${id}?status=${newStatus}`, {
        method: "PUT"
    });

    // ✅ MUST reload meeting
    loadMeetingById(currentMeetingId);
}


// ✅ FORMAT DATE
function formatDate(dateStr) {
    if (!dateStr) return "No deadline";

    const date = new Date(dateStr);
    return date.toLocaleDateString("en-GB", {
        day: "2-digit",
        month: "short",
        year: "numeric"
    });
}


// 🌙 THEME
function toggleTheme() {
    document.body.classList.toggle("dark");
}
