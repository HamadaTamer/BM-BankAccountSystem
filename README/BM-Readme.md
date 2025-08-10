# BM-BankAccountSystem — README (for testing & Postman screenshots)

## Overview
This repo is a Spring Boot bank account system which supports customers, accounts, deposits, withdrawals, transfers and transactions.  
I parsed the repo you provided and prepared a step-by-step Postman testing plan + an importable Postman Collection and Environment to make taking screenshots easy. I also included guidance about missing pieces and recommended next steps to fully match the project requirements.

---

## Quick status (what's already implemented)
- Spring Boot 3.5.4 project (Java 17, Maven) — `BankApplication` present.
- Entities: `Customer`, `Account`, `Transaction` exist with JPA annotations.
- Repositories: `CustomerRepository`, `AccountRepository`, `TransactionRepository` exist.
- Services: `CustomerService`, `AccountService` with create/read/update/delete, deposit, withdraw and transfer logic, and transaction creation.
- Controllers: `CustomerController` and `AccountController` implemented with endpoints:
  - `POST /api/customers` (create)
  - `GET /api/customers` (paged list)
  - `GET /api/customers/{id}`
  - `PUT /api/customers/{id}`
  - `DELETE /api/customers/{id}`
  - `POST /api/accounts` (create)
  - `GET /api/accounts` (paged list)
  - `GET /api/accounts/{id}`
  - `DELETE /api/accounts/{id}`
  - `POST /api/accounts/{id}/deposit`
  - `POST /api/accounts/{id}/withdraw`
  - `POST /api/accounts/transfer`
  - `GET /api/accounts/type/{type}`
- DTOs, a mapper and basic validation annotations exist.
- H2 in-memory DB configured by default (application.properties). ERD PNGs provided in `/ERD`.

## What's missing (from the original project requirements)
- **TransactionController** (no dedicated controller to list transactions by account/customer).
- **Global exception handler** (`@ControllerAdvice`) — currently exceptions bubble up as default error responses.
- Some project requirements mention `@Modifying` custom update queries and delete transactions — the service currently updates balances using `save()` and does not provide a transaction-delete endpoint.
- The `GET /accounts?minBalance=` style endpoint is not implemented; controller only supports pageable listing and `findByBalanceBetween` exists in repository but not exposed by query param endpoint.
- No README (this file is the README you asked for).
- Tests/screenshots: not present (you will take screenshots).

---

## How to run the project locally (quick)
Requirements:
- Java 17 JDK
- Maven 3.6+
- (optional) IntelliJ or VS Code

Steps:
1. Open a terminal, go to the `demo/` folder inside the repo.
```bash
cd path/to/BM-BankAccountSystem-main/demo
mvn clean spring-boot:run
```
2. The app will start at `http://localhost:8080` (base API prefix is `/api` so full base is `http://localhost:8080/api`).
3. H2 console (if needed): `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:bankdb`, user: `sa`, no password).

---

## Postman: Import and Environment (recommended)
I created a ready-to-import **Postman Collection** and **Environment** in `/mnt/data`:
- `BM-BankAccountSystem.postman_collection.json` — collection with example requests + tests that capture IDs into environment variables.
- `BM-BankAccountSystem.postman_environment.json` — environment with `baseUrl` variable pointing to `http://localhost:8080/api`.

**How to import**:
1. Open Postman → File → Import → Upload the collection JSON and environment JSON.
2. In the top-right select the imported environment (BM-BankAccountSystem).
3. Make sure the Spring Boot app is running locally (`mvn spring-boot:run`).

---

## Step-by-step Postman testing plan (what to run & screenshot)
Follow these steps in order. Each step shows the request details, expected status and what to screenshot.

> **Note:** All requests require header `Content-Type: application/json` (Postman sets this automatically when using raw JSON body).

1. **Create Customer A**
   - Method / URL: `POST {{baseUrl}}/customers`
   - Body (raw JSON):
```json
{
  "name": "Alice Example",
  "email": "alice@example.com",
  "phone": "0123456789"
}
```
   - Expected: `201 Created`, response body contains `id`, `name`, `email`, `phone`.
   - Screenshot: Save the full Postman request + response panel showing the 201 status and the returned JSON.

   Postman test in collection will save the returned id to `customerAId`.

2. **Create Customer B**
   - Same as step 1 but with a different email/phone.
   - Screenshot: response + status.

3. **Create Account for Customer A**
   - Method / URL: `POST {{baseUrl}}/accounts`
   - Body:
```json
{
  "accountNumber": null,
  "accountType": "SAVINGS",
  "initialBalance": 500,
  "customerId": {{customerAId}}
}
```
   - Expected: `201 Created`, response contains `id` (account id), `accountNumber`, `balance`, `customerId`.
   - Screenshot: response + status.
   - Postman test will save `accountAId`.

4. **Create Account for Customer B**
   - Same as step 3 with `customerBId`. Save `accountBId`.

5. **Deposit into Account A**
   - Method / URL: `POST {{baseUrl}}/accounts/{{accountAId}}/deposit`
   - Body:
```json
{ "amount": 200.0 }
```
   - Expected: `200 OK`, response shows updated `balance` increased by 200 and a `Transaction` was created (the service returns AccountDTO — transactions can be checked via DB/H2 console).
   - Screenshot: response + status.

6. **Withdraw from Account A**
   - Method / URL: `POST {{baseUrl}}/accounts/{{accountAId}}/withdraw`
   - Body:
```json
{ "amount": 100.0 }
```
   - Expected: `200 OK`, balance decreased by 100.
   - Screenshot: response + status.

7. **Transfer from Account A to Account B**
   - Method / URL: `POST {{baseUrl}}/accounts/transfer`
   - Body:
```json
{
  "senderAccountId": {{accountAId}},
  "receiverAccountId": {{accountBId}},
  "amount": 150.0
}
```
   - Expected: `204 No Content` (controller returns `noContent()`), account balances reflect the transfer.
   - Screenshot: request + response (status 204). To show concrete balances, also GET both accounts (next step) and screenshot their responses.

8. **Get Account A and Account B (balance check)**
   - Method / URL: `GET {{baseUrl}}/accounts/{{accountAId}}`
   - And `GET {{baseUrl}}/accounts/{{accountBId}}`
   - Expected: `200 OK` and JSON with latest balances.
   - Screenshot: both responses.

9. **List Accounts (paged)**
   - Method / URL: `GET {{baseUrl}}/accounts?page=0&size=10`
   - Expected: `200 OK` with a pageable response.
   - Screenshot: response showing content array.

10. **Find accounts by type**
    - Method / URL: `GET {{baseUrl}}/accounts/type/SAVINGS`
    - Expected: `200 OK` with list of accounts of that type.
    - Screenshot: response.

11. **Customers list, update and delete**
    - `GET {{baseUrl}}/customers` — screenshot.
    - `PUT {{baseUrl}}/customers/{{customerAId}}` — modify name or phone; expected `200 OK`. Screenshot.
    - `DELETE {{baseUrl}}/customers/{{customerBId}}` — expected `204 No Content`. Screenshot.

(If any request fails, screenshot the error body + headers — you'll include these in the PDF for grading.)

---

## How to take screenshots and prepare the PDF (step-by-step)
1. For each tested request above, open the request in Postman and click **Send**.
2. In the response panel:
   - Make sure status code and response body are visible.
   - Click the three dots menu → **Save Response** if you want a `.json` snapshot (optional).
3. Take a screenshot of the Postman window showing: request URL + method + response status + pretty JSON response.
   - Windows: use Snipping Tool or `Win+Shift+S`.
   - macOS: `Cmd+Shift+4`.
   - Linux: your screenshot utility.
4. Name your screenshots in order, e.g.: `01_create_customerA.png`, `02_create_customerB.png`, `03_create_accountA.png`, etc.
5. Put the screenshots into a single PDF:
   - Option A: Insert images into a Word/Google Docs document in order, then Export → Save as PDF.
   - Option B: On Windows, select images → Print → Choose "Microsoft Print to PDF".
   - Option C: Use an online tool to convert images to a single PDF.
6. Save the generated PDF as `postman_endpoints_screenshots.pdf`. This is what you will upload as the screenshot test evidence.

---

## Recommended next code tasks (to fully meet requirements)
1. Add a `TransactionController` with endpoints:
   - `GET /api/transactions` — all transactions (paged)
   - `GET /api/accounts/{accountId}/transactions` — transactions filtered by account
   - `DELETE /api/transactions/{id}` — delete a transaction
2. Add a `@ControllerAdvice` class that maps exceptions (e.g., `NoSuchElementException`, `IllegalArgumentException`, `DataIntegrityViolationException`) to meaningful JSON error responses (message, timestamp, status).
3. Add endpoints for balance filtering (e.g., `GET /api/accounts?minBalance=1000`) and map to repository `findByBalanceBetween` or `findByBalanceGreaterThan`.
4. Optionally add `@Modifying` repository queries if you prefer direct JPQL updates for balances, and use `@Transactional` when updating multiple entities (already used in service for transfer).
5. Add unit/integration tests, and add a `seed.sql` or data loader for reproducible data in tests.

---

## Files I placed in `/mnt/data`
- `BM-BankAccountSystem_README_generated.md` — this README you can download.
- `BM-BankAccountSystem.postman_collection.json` — Postman collection to import.
- `BM-BankAccountSystem.postman_environment.json` — Postman environment with `baseUrl`.

---

If you want, I can (pick one):
- Generate the missing **TransactionController** and **GlobalExceptionHandler** code snippets ready to paste into your repo.
- Add a `/api/accounts` query param endpoint `minBalance` and sample code.
- Create example screenshots for you using a running server (I can't run the Java app here), but I *can* give exact Postman collection and step-by-step test scripts.

Tell me which of the above you'd like me to do next (I recommend adding the ControllerAdvice + TransactionController snippets).