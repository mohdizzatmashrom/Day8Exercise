# NFS_JAVA_C2_2026 | Full-Stack Development with Java, React & MongoDB



## Programme Description



This 20-day programme is designed to help participants build a complete full-stack web application using Java, Spring Boot, React, and MongoDB.



The programme takes learners from programming and web fundamentals to backend API development, frontend interface design, database modelling, authentication, testing, performance improvement, and final capstone presentation.



Throughout the programme, participants will work on practical exercises and gradually build a small but production-like web application. The final outcome is a working capstone project that demonstrates the use of a React frontend, Spring Boot backend, MongoDB database, secure authentication, API documentation, testing practices, and deployment-readiness basics.



AI tools such as Gemini are used as learning accelerators to help scaffold examples, suggest refactoring ideas, draft tests, generate sample data, and support MongoDB query or aggregation design. However, participants are expected to review, verify, understand, and take ownership of all generated code.



---



## Programme Duration



* Duration: 20 training days

* Daily Duration: 7 hours per day

* Total Training Hours: 140 hours

* Mode: Instructor-led training with guided labs, team build activities, review sessions, quizzes, and capstone development



---



## Programme Objectives



By the end of this programme, participants will be able to:



* Understand web fundamentals, HTTP, REST, and JSON.

* Write basic to intermediate Java and JavaScript code.

* Build REST APIs using Spring Boot.

* Apply validation, authentication, authorisation, and error-handling practices.

* Model data effectively using MongoDB.

* Use MongoDB indexes, queries, pagination, and aggregation pipelines.

* Build accessible React user interfaces with routing, forms, state, and data fetching.

* Apply testing practices for backend and frontend development.

* Use AI coding assistants responsibly for learning, refactoring, testing, and documentation.

* Design, build, document, and present a full-stack capstone project.



---





---



## AI-Assisted Learning Guidelines



Participants may use AI tools to:



* Generate README drafts and documentation sections.

* Create API call examples and JSON payload samples.

* Suggest method signatures and edge cases.

* Propose refactoring options.

* Draft test scenarios for backend and frontend features.

* Suggest MongoDB document structures, queries, indexes, and aggregation pipelines.

* Improve demo scripts and presentation notes.



Participants must always review, verify, test, and understand any AI-generated output. No passwords, API keys, tokens, private keys, or confidential data should be placed into AI prompts.



---



## Day 8 Exercise 04 - Short Notes



### 1. Which query parameters did you implement?



Three filter parameters on `GET /api/tickets`:
- `status` – filter tickets by status (e.g. OPEN, Closed)
- `priority` – filter tickets by priority (e.g. HIGH, Medium, Low)
- `category` – filter tickets by category (e.g. Email, Bug, Network)



Four pagination/sorting parameters on `GET /api/tickets/paged`:
- `page` – the page number (default 0)
- `size` – the number of items per page (default 5)
- `sortBy` – the field to sort by (default `createdAt`)
- `direction` – sort direction, `asc` or `desc` (default `desc`)



### 2. Which fields did you index?



Five fields are indexed in the `Ticket` model using `@Indexed`:
- `category`
- `priority`
- `status`
- `createdBy`
- `createdAt`



These are the fields most commonly used in filter and sort operations, so indexing them improves query performance.



### 3. Why should an API use pagination?



- **Performance** – Returning all records at once can be slow and consume a lot of memory, especially with large datasets. Pagination returns only a small subset per request.

- **Scalability** – Smaller responses reduce bandwidth and server load, allowing the API to handle more concurrent users.

- **User experience** – Clients can display data in manageable chunks (pages) instead of overwhelming the user with thousands of results.

- **Predictable response times** – Each page has a bounded number of items, so response times stay consistent as the dataset grows.



### 4. What log messages appear when you call the filtering endpoint?



When calling `GET /api/tickets?status=OPEN`, the following log message appears:



```
INFO  c.e.s.service.TicketService - Fetching tickets with filters - status: OPEN, priority: null, category: null
```



When calling the paged endpoint `GET /api/tickets/paged?page=0&size=5`, this log appears:



```
INFO  c.e.s.service.TicketService - Fetching paginated tickets - page: 0, size: 5, sort: createdAt: DESC
```



### 5. What endpoint proves your sorting works?



The endpoint that proves sorting works:



```
GET /api/tickets/paged?page=0&size=5&sortBy=createdAt&direction=desc
```



This returns tickets sorted by `createdAt` in descending order (newest first). You can compare the `createdAt` values in the response to confirm they are ordered from newest to oldest. Changing `direction=asc` reverses the order, further proving sorting is functional.

