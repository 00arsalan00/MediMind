# MediMind - Healthcare Appointment & Follow-up Manager
​
MediMind is an AI-powered healthcare platform designed to streamline doctor-patient interactions. It features intelligent symptom summaries, automated scheduling with double-booking prevention, and seamless Google Calendar integration.
​

## Features
- **Smart Booking:** Real-time slot management with concurrency control.
- **AI Symptom Summary:** Pre-visit analysis using Google Gemini LLM.
- **Post-Visit Summary:** Automated patient-friendly notes and medication schedules.
- **Role-Based Access:** Separate portals for Patients, Doctors, and Admins.
- **Integrations:** Google Calendar API for events and SMTP for email reminders.

## Tech Stack
- **Backend:** Java 21, Spring Boot 3.4.2, Spring AI, Spring Security (JWT)
- **Database:** PostgreSQL
- **AI:** Google Gemini Pro
- **Notifications:** SendGrid/SMTP & Google Calendar API

## Setup Guide
1. Clone the repository.
2. Create a `.env` file based on `.env.example`.
3. Configure your Google Cloud Console for Calendar API and AI Studio for Gemini API.
4. Run `mvn clean install`.
5. Start the application.


## Module 1:  Foundation & Data Schema Design
In a Healthcare system, the schema must handle three distinct personas (Admin, Doctor, Patient) while ensuring high data integrity for appointments.

• **Identity Management:** A central User entity handles authentication (email, password, roles).

• **Profile Separation:** Since Doctors have unique attributes (specialization, bio) and Patients have others (medical history), we use a One-to-One relationship between User and their respective Profile entities.

• **Scheduling Logic:** Appointment is the central "transactional" entity. It links a Patient, a Doctor, and a specific time slot.

• **AI Metadata:** Instead of cluttering the Appointment table, summaries (Pre-visit and Post-visit) can be stored as JSON types or separate linked tables to keep the main table "lean."


## Module 2: Security & Authentication (JWT + OAuth2)
In a healthcare application, security is not just a feature; it is a legal requirement (like HIPAA compliance). Module 2 handles Identity and Access.

• **Authentication:** "Who are you?" (Handled by JWT Login or Google OAuth2).

• **Authorization:** "What are you allowed to do?" (Handled by Role-Based Access Control - RBAC). 
- Patients can book appointments but cannot see other patients' records.
- Doctors can see their own appointments and write notes.
- Admins can manage doctor profiles.

• **Statelessness:** We use JWT (JSON Web Tokens) so the server doesn't have to remember every logged-in user in its memory (RAM). This makes the app faster and easier to scale.



## Module 3: Google Calendar Integration

**Google Cloud Console Setup (The "Google System"):**
1. Create a Project: Go to the Google Cloud Console, and create a new project named "Medimind".
2. Enable APIs: Search for "Google Calendar API" and click Enable.
3. Configure OAuth Consent Screen:
- Choose External (unless you have a Google Workspace organization).
- Add Scopes: You must add https://www.googleapis.com/auth/calendar and https://www.googleapis.com/auth/calendar.events.
- Add Test Users: Add your own Gmail address so you can test it before the app is verified.
4. Create Credentials:
- Click Create Credentials -> OAuth Client ID.
- Select Web Application.
- Authorized Redirect URIs: Add http://localhost:8080/login/oauth2/code/google.
5. Get Keys: Copy your Client ID and Client Secret into your .env or application.yaml file.


Since app creates calendar events even when the doctor or patient is offline, we cannot just log them in once. We must perform an "Incremental Authorization."
- Offline Access: We must request access_type=offline.
- Refresh Token: Google will give us a Refresh Token (which lasts forever) and an Access Token (which lasts 1 hour).
- Storage: We will create a GoogleToken entity linked to our User to store these.



## Module 4: Booking Engine & Concurrency Control

This is the "Brain" of Medimind. It transforms a doctor's raw working hours (e.g., 9:00 AM – 5:00 PM) into bookable 30-minute intervals while ensuring no two patients can take the same slot.

- Slot Generation: The system takes workingHoursStart and workingHoursEnd, subtracts existing Appointments and DoctorLeave, and presents the remaining "Free" slots.
- Concurrency Challenge: If two patients click "Book" at 10:00:01 AM for the same 2:00 PM slot, the system must ensure only one succeeds and the other gets a "Slot already taken" error.
- Leave Integration: If a doctor marks a day as "Leave," the engine must immediately invalidate all slots for that day and flag existing bookings for cancellation.

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/4.1.0/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/4.1.0/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/4.1.0/reference/web/servlet.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/4.1.0/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Spring Security](https://docs.spring.io/spring-boot/4.1.0/reference/web/spring-security.html)
* [Google GenAI](https://docs.spring.io/spring-ai/reference/api/chat/google-genai-chat.html)
* [Validation](https://docs.spring.io/spring-boot/4.1.0/reference/io/validation.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)

