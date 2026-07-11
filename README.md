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



---



## Day 8 Exercise 05 - Query Behaviour and Troubleshooting



### Test 1: Invalid Status Value



**Request:** `GET /api/tickets?status=INVALID`



**Result:** The API returned an empty list `[]` with HTTP 200. No error was thrown.



**Log message:**
```
INFO c.e.supportdesk.service.TicketService - Fetching tickets with filters - status: INVALID, priority: null, category: null
```



**Observation:** The API does not validate status values. It simply queries MongoDB for tickets matching `status = "INVALID"`, finds none, and returns an empty array. A future improvement would be to validate the status value against a known list (e.g. OPEN, Closed, IN_PROGRESS) and return a 400 Bad Request for invalid values.



---



### Test 2: Invalid Priority Value



**Request:** `GET /api/tickets?priority=URGENT`



**Result:** The API returned an empty list `[]` with HTTP 200. No error was thrown.



**Log message:**
```
INFO c.e.supportdesk.service.TicketService - Fetching tickets with filters - status: null, priority: URGENT, category: null
```



**Observation:** The API does not validate priority values. "URGENT" is not a supported priority in the seeded data (which uses HIGH, Medium, Low), so the query returns nothing. The API should ideally validate priority against an enum and return a 400 error for unsupported values.



---



### Test 3: Page Number with No Records



**Request:** `GET /api/tickets/paged?page=99&size=5`



**Result:** The API did not crash. It returned an empty page with metadata:
- `content: []` (empty)
- `totalElements: 7`
- `totalPages: 2`
- `number: 99`
- `first: false`, `last: true`



**Log message:**
```
INFO c.e.supportdesk.service.TicketService - Fetching paginated tickets - page: 99, size: 5, sort: createdAt: DESC
WARN ... Serializing PageImpl instances as-is is not supported, meaning that there is no guarantee about the stability of the resulting JSON structure!
```



**Observation:** Spring Data handles out-of-range pages gracefully by returning an empty page. The metadata correctly shows the total count and total pages. The WARN log about PageImpl serialization suggests we should use `@EnableSpringDataWebSupport` or a custom DTO wrapper for stable JSON output.



---



### Test 4: Very Large Page Size



**Request:** `GET /api/tickets/paged?page=0&size=100`



**Result:** The API allowed this request and returned all 7 tickets in a single page with `totalPages: 1`. No error.



**Log message:**
```
INFO c.e.supportdesk.service.TicketService - Fetching paginated tickets - page: 0, size: 100, sort: createdAt: DESC
```



**Observation:** The API does not limit the maximum page size. With only 7 seeded tickets this is harmless, but in production with thousands of records, a page size of 100 (or 1000+) could cause slow queries, high memory usage, and large response payloads. A future improvement would be to cap the page size (e.g. maximum 50) and return a 400 error if exceeded.



---



### Test 5: Unknown Sort Field



**Request:** `GET /api/tickets/paged?page=0&size=5&sortBy=unknownField&direction=asc`



**Result:** The API returned 5 tickets successfully. The sort order appeared arbitrary (not meaningful). No error was thrown.



**Log message:**
```
INFO c.e.supportdesk.service.TicketService - Fetching paginated tickets - page: 0, size: 5, sort: unknownField: ASC
```



**Observation:** MongoDB does not throw an error when sorting by a non-existent field. It simply treats the missing field as null for all documents, resulting in an undefined order. The backend should validate allowed sort fields (e.g. createdAt, priority, status, category) and reject unknown fields with a 400 error.



---



### Test 6: Combined Filters



**Request:** `GET /api/tickets?status=OPEN&priority=HIGH`



**Result:** The API returned 6 tickets — all tickets with status "OPEN", including tickets with priority Medium and Low. Only the `status` filter was applied; `priority=HIGH` was ignored.



**Log message:**
```
INFO c.e.supportdesk.service.TicketService - Fetching tickets with filters - status: OPEN, priority: HIGH, category: null
```



**Observation:** The current `getFilteredTickets` method uses an `if-else-if-else` chain, so only the first non-null filter is applied. When both `status` and `priority` are provided, only `status` is used. This behaviour is not clear to API users. A future improvement would be to support combined filters using a MongoDB query that applies all provided parameters with AND logic.



---



## Reflection Questions



### 1. What happened when you used an invalid status?



The API returned an empty list `[]` with HTTP 200. No error was thrown. MongoDB simply found no documents matching `status = "INVALID"`. The log message showed the filter was received but no validation was performed.



### 2. What happened when you used an invalid priority?



The API returned an empty list `[]` with HTTP 200. The value "URGENT" does not match any seeded priority (HIGH, Medium, Low), so no results were found. The API does not reject unknown values.



### 3. What happened when you requested page 99?



The API did not crash. It returned an empty page with correct metadata showing `totalElements: 7`, `totalPages: 2`, `number: 99`, and `last: true`. Spring Data MongoDB handles out-of-range pages gracefully.



### 4. What happened when you used an unknown sort field?



The API returned data successfully but the sort order was arbitrary and not meaningful. MongoDB does not error on unknown sort fields; it treats missing fields as null, resulting in undefined ordering.



### 5. Why should an API limit page size?



An API should limit page size to prevent excessive memory consumption, slow database queries, and large response payloads. Without a limit, a malicious or careless user could request millions of records in a single request, potentially crashing the server or degrading performance for other users. A reasonable maximum (e.g. 50 or 100) protects server resources.



### 6. Why should an API validate sort fields?



An API should validate sort fields to prevent unexpected behaviour and potential security issues. Sorting by unknown fields produces meaningless results and confuses API consumers. In some databases, certain sort operations on unindexed fields can cause full collection scans and poor performance. Validation ensures only supported, indexed fields are used for sorting.



### 7. Does your current API support combined filters?



No. The current API uses an `if-else-if-else` chain in the `getFilteredTickets` method, so only the first non-null filter parameter is applied. If both `status` and `priority` are provided, only `status` is used and `priority` is silently ignored. True combined filtering would require a MongoDB query that applies all provided parameters together.



### 8. What log messages helped you understand what happened?



The log messages in `TicketService` were the most helpful:
- `Fetching tickets with filters - status: INVALID, priority: null, category: null` showed that the filter value was passed directly to the repository without validation.
- `Fetching paginated tickets - page: 99, size: 5, sort: createdAt: DESC` confirmed the pagination parameters were received correctly.
- `Fetching paginated tickets - page: 0, size: 5, sort: unknownField: ASC` showed that unknown sort fields were accepted without validation.
- The WARN about `PageImpl` serialization highlighted that the current Page response JSON structure is not guaranteed to be stable.



### 9. Which behaviour would you improve in a future version?



### Improvement Plan



In a future version, I would make the following improvements to the API:



**1. Input validation for filter values:** Validate `status`, `priority`, and `category` against known allowed values (e.g. enums). Return a 400 Bad Request with a clear error message when an invalid value is provided, instead of silently returning an empty list.



**2. Support combined filters:** Replace the `if-else-if-else` chain with a dynamic MongoDB query that applies all provided filter parameters using AND logic. This would allow users to filter by status, priority, and category simultaneously.



**3. Cap maximum page size:** Add a maximum page size limit (e.g. 50) to prevent abuse and protect server resources. Return a 400 error if the requested size exceeds the limit.



**4. Validate sort fields:** Add a whitelist of allowed sort fields (e.g. `createdAt`, `priority`, `status`, `category`, `createdBy`). Reject unknown fields with a 400 error instead of returning arbitrarily ordered results.



**5. Use a stable Page DTO:** Replace the direct `Page<TicketResponse>` return type with a custom DTO wrapper (or use `@EnableSpringDataWebSupport`) to ensure stable and predictable JSON structure for paginated responses, as recommended by the Spring Data warning.



**6. Add error messages for edge cases:** Provide meaningful error responses for common mistakes, such as invalid page numbers (negative values) or missing required parameters, so that API consumers can debug their requests easily.

