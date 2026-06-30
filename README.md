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

