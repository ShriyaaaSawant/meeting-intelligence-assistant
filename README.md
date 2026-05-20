🚀 Meeting Intelligence Assistant

> A smart meeting productivity application that transforms unstructured meeting transcripts into actionable insights.

## 📖 Overview
## 💡 Problem Statement

Teams conducting online meetings often face challenges such as:

- Losing track of important decisions
- Missing follow-up tasks
- Unclear ownership of action items
- Difficulty reviewing lengthy discussions

This project addresses these issues by automating meeting intelligence and task organization.

**Meeting Intelligence Assistant** solves this by automating meeting documentation. The application processes meeting transcripts, generates concise summaries, extracts actionable tasks, identifies responsible owners, detects deadlines, and enables task tracking through a clean interface.

This project was built to demonstrate practical full-stack engineering with a real-world productivity use case instead of a generic CRUD application.

✨ Key Features

- 📝 Automated meeting summary generation
- ✅ Action item extraction from transcripts
- 👤 Task owner identification
- 📅 Deadline detection
- 📂 Meeting history management
- 🔄 Task status tracking (Pending / Done)
- 🛡 Input validation and exception handling
- 💾 Persistent MySQL database storage
- 🔌 RESTful API architecture
- 🏗 Layered backend architecture

## 📸 Application Preview

### Home Page
<img src="screenshots/home.png" alt="Home Page" width="850"/>

### Action Item Tracking
<img src="screenshots/action-items.png" alt="Action Items" width="850"/>


## 🛠 Tech Stack

### Backend
- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- REST APIs
- Bean Validation

### Database
- MySQL

### Frontend
- HTML
- CSS
- JavaScript

### Development Tools
- Git
- GitHub
- VS Code
- Postman


## 🏗 System Architecture

input text
User Meeting Transcript
        │
        ▼
Frontend Interface
(HTML / CSS / JavaScript)
        │
        ▼
Spring Boot REST APIs
        │
        ▼
Service Layer
(Business Logic Processing)
        │
        ├── Summary Generation
        ├── Action Item Extraction
        ├── Assignee Detection
        └── Deadline Parsing
        │
        ▼
Repository Layer
(JPA / Hibernate)
        │
        ▼
MySQL Database
        │
        ▼
Structured Meeting Insights


## ⚙ Backend Concepts Demonstrated

This project showcases:

- Layered Architecture (Controller → Service → Repository)
- REST API Development
- Business Logic Design
- Entity Relationships (`@OneToMany`, `@ManyToOne`)
- JPA/Hibernate ORM Mapping
- Global Exception Handling
- Validation using annotations
- JSON serialization handling
- Database persistence
- Rule-based transcript processing


## 🔍 Processing Workflow

1. User submits a meeting transcript.
2. Backend receives the transcript through REST APIs.
3. Service layer analyzes transcript content.
4. Summary is generated from key discussion points.
5. Action items are extracted using text processing logic.
6. Assignees and deadlines are identified.
7. Processed meeting data is stored in MySQL.
8. Frontend displays structured meeting insights and task progress.

---

## 🚀 Future Enhancements

Potential production-scale improvements:

- AI/LLM-powered summarization
- NLP-based entity recognition
- Real-time speech-to-text integration
- User authentication & authorization
- Email reminders and notifications
- Team collaboration support
- Cloud deployment

---

## 🎯 Why This Project?

Instead of building a basic CRUD system, this project focuses on solving a practical productivity challenge using backend engineering and full-stack integration.

It demonstrates:

✔ Real-world problem solving  
✔ Practical backend architecture  
✔ Database design & persistence  
✔ REST API development  
✔ Business logic implementation  
✔ Full-stack integration  

---
## 👩‍💻 Author

Shriya Sawant 
Computer Engineering Student | Backend / Full Stack Developer  

GitHub: https://github.com/ShriyaaaSawant
