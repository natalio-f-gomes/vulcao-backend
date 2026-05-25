package dev.hope.vulcaobackend.TestingData;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginTestingData {
    public String[] getInvalidLoginData() throws JSONException {
        String[] loginDataArray = {
                new JSONObject().put("username", "admin'--").put("password", "Password123").toString(),
                new JSONObject().put("username", "admin' #").put("password", "Password123").toString(),
                new JSONObject().put("username", "' OR '1'='1' --").put("password", "Password123").toString(),
                new JSONObject().put("username", "' OR 1=1--").put("password", "Password123").toString(),
                new JSONObject().put("username", "' OR 1=1 LIMIT 1--").put("password", "Password123").toString(),
                new JSONObject().put("username", "admin') OR ('1'='1").put("password", "Password123").toString(),
                new JSONObject().put("username", "' UNION SELECT null,null--").put("password", "Password123").toString(),
                new JSONObject().put("username", "' OR EXISTS(SELECT * FROM users)--").put("password", "Password123").toString(),
                new JSONObject().put("username", "x' OR 'a'='a").put("password", "Password123").toString(),
                new JSONObject().put("username", "admin\\'--").put("password", "Password123").toString(),
                new JSONObject().put("username", "admin' OR '1'='1").put("password", "Password123").toString(),
                new JSONObject().put("username", "' OR '1'='1' --").put("password", "Password123").toString()
        };
        return loginDataArray;
    }

    public String[] getLoginWithNoSqlInjectionData() throws JSONException {
        return new String[] {
                // Step 1: Both username and password as $ne null - matches any user with any password
                new JSONObject()
                        .put("username", new JSONObject().put("$ne", JSONObject.NULL))
                        .put("password", new JSONObject().put("$ne", JSONObject.NULL))
                        .toString(),

                // Step 2: Targeted account "admin" with operator-smuggled password
                new JSONObject()
                        .put("username", "admin")
                        .put("password", new JSONObject().put("$ne", JSONObject.NULL))
                        .toString(),

                // Step 3: $gt empty string in password - matches any non-empty password
                new JSONObject()
                        .put("username", "admin")
                        .put("password", new JSONObject().put("$gt", ""))
                        .toString(),

                // Step 4: $regex match-all in password
                new JSONObject()
                        .put("username", "admin")
                        .put("password", new JSONObject().put("$regex", ".*"))
                        .toString(),
        };
    }

    public String[] getLoginWithDefaultCredentialsData() throws JSONException {
        return new String[] {
                // Step 1: admin / admin
                new JSONObject().put("username", "admin").put("password", "admin").toString(),

                // Step 2: admin / password
                new JSONObject().put("username", "admin").put("password", "password").toString(),

                // Step 3: root / root
                new JSONObject().put("username", "root").put("password", "root").toString(),

                // Step 4: test / test
                new JSONObject().put("username", "test").put("password", "test").toString(),

                // Step 5: guest / guest
                new JSONObject().put("username", "guest").put("password", "guest").toString(),

                // Step 6: sa / (empty) - classic MSSQL default
                new JSONObject().put("username", "sa").put("password", "").toString(),
        };
    }
    public String[] getLoginWithOversizedFieldsData() throws JSONException {
        return new String[] {
                // Step 1: Username with 10,000 characters
                new JSONObject()
                        .put("username", repeat('a', 10_000))
                        .put("password", "grgrbtrhrebwhnrwtn")
                        .toString(),

                // Step 2: Password with 1,000 characters
                new JSONObject()
                        .put("username", "admin")
                        .put("password", repeat('a', 1_000))
                        .toString(),

                // Step 3: Both fields with 1MB of content each
                new JSONObject()
                        .put("username", repeat('a', 1_048_576))
                        .put("password", repeat('a', 1_048_576))
                        .toString(),
        };
    }


    public String[] getLoginWithUnicodeNormalizationData() throws JSONException {
        return new String[] {
                // Step 1: NFC-form "café" (single codepoint U+00E9) — assumes account stored in NFD
                new JSONObject()
                        .put("username", "caf\u00E9")
                        .put("password", "grgrbtrhrebwhnrwtn")
                        .toString(),

                // Step 2: NFD-form "café" (e + combining acute U+0301) — assumes account stored in NFC
                new JSONObject()
                        .put("username", "cafe\u0301")
                        .put("password", "grgrbtrhrebwhnrwtn")
                        .toString(),

                // Step 3: Both forms in one suite for direct comparison —
                // first the precomposed (NFC) form, second the decomposed (NFD) form.
                // Test asserts both succeed or both fail; never one-or-the-other.
                new JSONObject()
                        .put("username", "caf\u00E9user")
                        .put("password", "grgrbtrhrebwhnrwtn")
                        .toString(),
                new JSONObject()
                        .put("username", "cafe\u0301user")
                        .put("password", "grgrbtrhrebwhnrwtn")
                        .toString(),

                // Step 4: Case variant — "Admin" vs stored "admin"
                new JSONObject()
                        .put("username", "Admin")
                        .put("password", "grgrbtrhrebwhnrwtn")
                        .toString(),

                // Step 5: Appended zero-width space — "admin" + U+200B
                new JSONObject()
                        .put("username", "admin\u200B")
                        .put("password", "grgrbtrhrebwhnrwtn")
                        .toString(),
                new JSONObject()
                        .put("username", " admin\u0000")
                        .put("password", "passwordStrong")
                        .toString(),
        };
    }

    public String[] getLoginWithCrlfInjectionData() throws JSONException {
        return new String[] {
                // Step 1: Literal CRLF then X-Forwarded-For header
                new JSONObject()
                        .put("username", "admin\r\nX-Forwarded-For: 127.0.0.1")
                        .put("password", "grgrbtrhrebwhnrwtn")
                        .toString(),

                // Step 2 (extra): LF-only injection — some sanitizers strip \r\n but miss bare \n
                new JSONObject()
                        .put("username", "admin\nX-Forwarded-For: 127.0.0.1")
                        .put("password", "grgrbtrhrebwhnrwtn")
                        .toString(),

                // Step 3 (extra): Percent-encoded CRLF — tests double-decoding bugs
                new JSONObject()
                        .put("username", "admin%0d%0aX-Forwarded-For:%20127.0.0.1")
                        .put("password", "grgrbtrhrebwhnrwtn")
                        .toString(),

                // Step 4 (extra): CRLF + Set-Cookie injection — session fixation vector
                new JSONObject()
                        .put("username", "admin\r\nSet-Cookie: session=attacker-controlled")
                        .put("password", "grgrbtrhrebwhnrwtn")
                        .toString(),

                // Step 5 (extra): CRLF + Location redirect — open-redirect via response splitting
                new JSONObject()
                        .put("username", "admin\r\nLocation: http://evil.com")
                        .put("password", "grgrbtrhrebwhnrwtn")
                        .toString(),

                // Step 6 (extra): CRLF in password field — ensures both fields get the same treatment
                new JSONObject()
                        .put("username", "admin")
                        .put("password", "grgrbtr\r\nX-Admin: true")
                        .toString(),

                // Step 7 (extra): Forged log line in username — log-injection vector
                new JSONObject()
                        .put("username", "alice\n[INFO] User admin logged in successfully")
                        .put("password", "grgrbtrhrebwhnrwtn")
                        .toString(),

                // Step 8 (extra): Unicode line separator U+2028 — bypasses naive \r\n filters
                new JSONObject()
                        .put("username", "admin\u2028X-Forwarded-For: 127.0.0.1")
                        .put("password", "grgrbtrhrebwhnrwtn")
                        .toString(),
        };
    }

    public String[] getLoginWithRedosBaitData() throws JSONException {
        return new String[] {
                // Step 1: 50 'a's followed by '!' — classic catastrophic-backtracking trigger
                new JSONObject()
                        .put("username", repeat('a', 51) + "!")
                        .put("password", "grgrbtrhrebwhnrwtn")
                        .toString(),

                // Step 2: 63 'a's followed by '!' — escalation to confirm linear-time behavior
                new JSONObject()
                        .put("username", repeat('a', 63) + "!")
                        .put("password", "grgrbtrhrebwhnrwtn")
                        .toString(),

                // Step 3 (extra): 100 'a's + '!' — confirms still linear at higher input
                new JSONObject()
                        .put("username", repeat('a', 100) + "!")
                        .put("password", "grgrbtrhrebwhnrwtn")
                        .toString(),

                // Step 4 (extra): Nested-quantifier bait against email regex — "(a+)+" pattern
                new JSONObject()
                        .put("username", "user")
                        .put("password", "grgrbtrhrebwhnrwtn")
                        .put("email", repeat('e', 51) + "@" + repeat('a', 30) + "!.com")
                        .toString(),

                // Step 5 (extra): Alternation-overlap bait — "(a|a)*" pattern trigger
                new JSONObject()
                        .put("username", repeat('b', 51))
                        .put("password", "grgrbtrhrebwhnrwtn")
                        .toString(),

                // Step 6 (extra): Long input ending in mismatch — exposes greedy-backtracking validators
                new JSONObject()
                        .put("username", repeat('c', 200) + "X")
                        .put("password", "grgrbtrhrebwhnrwtn")
                        .toString(),

                // Step 7 (extra): Polynomial blowup on repeated optional groups
                new JSONObject()
                        .put("username", repeat("dd", 30))
                        .put("password", "grgrbtrhrebwhnrwtn")
                        .toString(),
        };
    }

    public String[] getLoginWithBruteForceData() throws JSONException {
        java.util.List<String> p = new java.util.ArrayList<>();

        // Step 1: 5 wrong passwords against the same account in quick succession
        for (int i = 1; i <= 5; i++) {
            p.add(new JSONObject()
                    .put("username", "victim")
                    .put("password", "wrong-pw-" + i)
                    .toString());
        }

        // Step 2: 10 wrong passwords — should cross threshold mid-sequence
        for (int i = 1; i <= 10; i++) {
            p.add(new JSONObject()
                    .put("username", "victim")
                    .put("password", "burst10-" + i)
                    .toString());
        }

        // Step 3: 100 wrong passwords — exercises progressive backoff
        for (int i = 1; i <= 100; i++) {
            p.add(new JSONObject()
                    .put("username", "victim")
                    .put("password", "burst100-" + i)
                    .toString());
        }

        // Step 4: Valid credentials submitted while account is locked out
        p.add(new JSONObject()
                .put("username", "victim")
                .put("password", "correct-password-during-lockout")
                .toString());

        // Step 5: Valid credentials submitted after cooldown elapses
        p.add(new JSONObject()
                .put("username", "victim")
                .put("password", "correct-password-after-cooldown")
                .toString());

        // Step 6: Per-username vs per-IP isolation — wrong password against "victim2" from same IP
        //         (test harness controls IP; this payload just provides a second target account)
        p.add(new JSONObject()
                .put("username", "victim2")
                .put("password", "wrong-different-account")
                .toString());

        // Step 7 (extra): Password spraying — one wrong password against many usernames
        //                 should NOT trigger per-username lockout but SHOULD trigger per-IP rate limit
        String[] sprayTargets = {"alice", "bob", "carol", "dave", "eve", "frank", "grace", "heidi"};
        for (String target : sprayTargets) {
            p.add(new JSONObject()
                    .put("username", target)
                    .put("password", "Summer2024!")
                    .toString());
        }

        // Step 8 (extra): Credential-stuffing pattern — varied user/pass pairs from a leaked-list shape
        String[][] stuffingPairs = {
                {"alice", "P@ssw0rd1"},
                {"bob", "qwerty123"},
                {"carol", "letmein"},
                {"dave", "monkey"},
                {"eve", "dragon"},
        };
        for (String[] pair : stuffingPairs) {
            p.add(new JSONObject()
                    .put("username", pair[0])
                    .put("password", pair[1])
                    .toString());
        }

        // Step 9 (extra): Username-enumeration probe — confirms timing & response are identical
        //                 for nonexistent vs existing-but-wrong-password accounts
        p.add(new JSONObject()
                .put("username", "definitely-does-not-exist-" + System.nanoTime())
                .put("password", "anything")
                .toString());
        p.add(new JSONObject()
                .put("username", "victim")
                .put("password", "definitely-wrong")
                .toString());

        // Step 10 (extra): Case-variant bypass — does "Victim" share the same lockout bucket as "victim"?
        p.add(new JSONObject()
                .put("username", "Victim")
                .put("password", "wrong-case-variant")
                .toString());

        // Step 11 (extra): Whitespace-padded username — does " victim" share the bucket?
        p.add(new JSONObject()
                .put("username", " victim")
                .put("password", "wrong-with-leading-space")
                .toString());

        return p.toArray(new String[0]);
    }

    private static String repeat(char c, int count) {
        char[] buf = new char[count];
        java.util.Arrays.fill(buf, c);
        return new String(buf);
    }

    private static String repeat(String s, int count) {
        StringBuilder sb = new StringBuilder(s.length() * count);
        for (int i = 0; i < count; i++) sb.append(s);
        return sb.toString();
    }

}
