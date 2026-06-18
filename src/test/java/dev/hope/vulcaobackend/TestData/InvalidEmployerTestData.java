package dev.hope.vulcaobackend.TestData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Extensive set of INVALID employer registration payloads for negative tests.
 *
 * Validation rules under test:
 *   - username : required, length 8..50 (inclusive)
 *   - password : required, length >= 8
 *   - email    : required, must be a well-formed address
 *   - firstName: required, non-empty (after trim), length <= 200
 *   - lastName : required, non-empty (after trim), length <= 200
 *
 * Design:
 *   - Each entry violates exactly ONE rule. Every other field is valid, so the
 *     failing field is unambiguous and a parameterized test can assert on it.
 *   - The "expected" violated field is recorded next to each payload via
 *     {@link #invalidEmployerCases()} (payload + field name + short reason),
 *     while {@link #invalidEmployers()} returns just the payloads to stay
 *     drop-in compatible with the positive {@code EmployerTestData} fixture.
 *   - "missing" means the JSON key is absent; "null" means the key maps to a
 *     JSON null literal. Both are exercised separately for every field.
 *   - A final section covers payloads that violate MULTIPLE rules at once.
 *
 * Boundary notes (one-off-the-edge values that MUST be rejected):
 *   - username length 7  -> too short  | username length 51 -> too long
 *   - password length 7  -> too short
 *   - firstName/lastName length 201 -> too long
 * The matching just-valid edges (8, 50, 8, 200, 200) belong in the POSITIVE set.
 */
public class InvalidEmployerTestData {

    /** A single negative case: the payload, the field expected to fail, and why. */
    public static final class InvalidCase {
        public final JSONObject payload;
        public final String field;   // "username" | "password" | "email" | "firstName" | "lastName" | "multiple"
        public final String reason;  // short human-readable description

        public InvalidCase(JSONObject payload, String field, String reason) {
            this.payload = payload;
            this.field = field;
            this.reason = reason;
        }

        @Override
        public String toString() {
            return field + " -> " + reason;
        }
    }

    public InvalidEmployerTestData() {
    }

    /** Just the bad payloads (drop-in parallel to EmployerTestData#validEmployers). */
    public List<JSONObject> invalidEmployers() throws JSONException {
        List<JSONObject> out = new ArrayList<>();
        for (InvalidCase c : invalidEmployerCases()) {
            out.add(c.payload);
        }
        return out;
    }

    /** Full negative dataset with the expected violated field for each payload. */
    public List<InvalidCase> invalidEmployerCases() throws JSONException {
        List<InvalidCase> cases = new ArrayList<>();

        // =====================================================================
        // USERNAME — too short (length < 8)
        // =====================================================================
        cases.add(bad("username", "empty string (length 0)",
                employer(" ", "Tiago", "Monteiro", "uname.short01@example.com", "DemoPass3101")));
        cases.add(bad("username", "1 char",
                employer("a", "Rita", "Figueiredo", "uname.short02@example.com", "DemoPass3202")));
        cases.add(bad("username", "2 chars",
                employer("ab", "David", "Silveira", "uname.short03@example.com", "DemoPass3303")));
        cases.add(bad("username", "3 chars",
                employer("abc", "Sara", "Cunha", "uname.short04@example.com", "DemoPass3404")));
        cases.add(bad("username", "4 chars",
                employer("abcd", "Joao", "Paiva", "uname.short05@example.com", "DemoPass3505")));
        cases.add(bad("username", "5 chars",
                employer("abcde", "Daniela", "Matos", "uname.short06@example.com", "DemoPass3606")));
        cases.add(bad("username", "6 chars",
                employer("abcdef", "Rodrigo", "Neves", "uname.short07@example.com", "DemoPass3707")));
        cases.add(bad("username", "7 chars (boundary: min - 1)",
                employer("abcdefg", "Patricia", "Guerreiro", "uname.short08@example.com", "DemoPass3808")));

        // =====================================================================
        // USERNAME — too long (length > 50)
        // =====================================================================
        cases.add(bad("username", "51 chars (boundary: max + 1)",
                employer(repeat("u", 51), "Sergio", "Pacheco", "uname.long01@example.com", "DemoPass3909")));
        cases.add(bad("username", "64 chars",
                employer(repeat("u", 64), "Claudia", "Alves", "uname.long02@example.com", "DemoPass4010")));
        cases.add(bad("username", "100 chars",
                employer(repeat("u", 100), "Nelson", "Andrade", "uname.long03@example.com", "DemoPass4111")));
        cases.add(bad("username", "256 chars",
                employer(repeat("u", 256), "Luciana", "Gomes", "uname.long04@example.com", "DemoPass4212")));

        // =====================================================================
        // USERNAME — whitespace-only (collapses to empty after trim)
        // =====================================================================
        cases.add(bad("username", "8 spaces (whitespace only, empty after trim)",
                employer("     ", "Jorge", "Fortes", "uname.ws01.@example.com", "DemoPass4313")));

        // =====================================================================
        // USERNAME — missing / null
        // =====================================================================
        cases.add(bad("username", "key missing",
                without("username",
                        employer("placeholder", "Adilson", "Pires", "uname.missing@example.com", "DemoPass4515"))));
        cases.add(bad("username", "explicit JSON null",
                nulled("username",
                        employer("placeholder", "Mario", "Cande", "uname.null@example.com", "DemoPass4616"))));

        // =====================================================================
        // PASSWORD — too short (length < 8)
        // =====================================================================
        cases.add(bad("password", "empty string (length 0)",
                employer("pwduser0001", "Augusto", "Djalo", "pwd.short01@example.com", "")));
        cases.add(bad("password", "1 char",
                employer("pwduser0002", "Fatimata", "Jalo", "pwd.short02@example.com", "A")));
        cases.add(bad("password", "5 chars",
                employer("pwduser0003", "Bacar", "Sane", "pwd.short03@example.com", "Pass1")));
        cases.add(bad("password", "6 chars",
                employer("pwduser0004", "Ousmane", "Ndiaye", "pwd.short04@example.com", "Pass12")));
        cases.add(bad("password", "7 chars (boundary: min - 1)",
                employer("pwduser0005", "Aissatou", "Sow", "pwd.short05@example.com", "Passwd1")));

        // =====================================================================
        // PASSWORD — whitespace-only (empty after trim, if the validator trims)
        // =====================================================================
        cases.add(bad("password", "8 spaces (whitespace only)",
                employer("pwduser0006", "Ibrahima", "Seck", "pwd.ws01@example.com", "        ")));

        // =====================================================================
        // PASSWORD — missing / null
        // =====================================================================
        cases.add(bad("password", "key missing",
                without("password",
                        employer("pwduser0007", "Khadija", "Ndour", "pwd.missing@example.com", "placeholder"))));
        cases.add(bad("password", "explicit JSON null",
                nulled("password",
                        employer("pwduser0008", "Oluwaseyi", "Adebayo", "pwd.null@example.com", "placeholder"))));

        // =====================================================================
        // EMAIL — malformed
        // =====================================================================
        cases.add(bad("email", "empty string",
                employer("mailuser001", "Chinenye", "Eze", "", "DemoPass5525")));
        cases.add(bad("email", "no @ symbol",
                employer("mailuser002", "Ifeanyi", "Nwosu", "plainaddress", "DemoPass5626")));
        cases.add(bad("email", "missing local part",
                employer("mailuser003", "Ngozi", "Okafor", "@example.com", "DemoPass5727")));
        cases.add(bad("email", "missing domain",
                employer("mailuser004", "Yaw", "Mensah", "user@", "DemoPass5828")));
        cases.add(bad("email", "missing TLD",
                employer("mailuser005", "Efua", "Owusu", "user@example", "DemoPass5929")));
        cases.add(bad("email", "double @",
                employer("mailuser006", "Kofi", "Boateng", "user@@example.com", "DemoPass6030")));
        cases.add(bad("email", "two @ across parts",
                employer("mailuser007", "Abena", "Agyeman", "user@host@example.com", "DemoPass6131")));
        cases.add(bad("email", "space in local part",
                employer("mailuser008", "Yao", "Kouassi", "user name@example.com", "DemoPass6232")));
        cases.add(bad("email", "space in domain",
                employer("mailuser009", "Aminata", "Traore", "user@exam ple.com", "DemoPass6333")));
        cases.add(bad("email", "leading space",
                employer("mailuser010", "JeanMarc", "Kone", " user@example.com", "DemoPass6434")));
        cases.add(bad("email", "trailing space",
                employer("mailuser011", "Moussa", "Konate", "user@example.com ", "DemoPass6535")));
        cases.add(bad("email", "leading dot in local part",
                employer("mailuser012", "Hawa", "Diallo", ".user@example.com", "DemoPass6636")));
        cases.add(bad("email", "trailing dot in local part",
                employer("mailuser013", "Bakary", "Sangare", "user.@example.com", "DemoPass6737")));
        cases.add(bad("email", "consecutive dots in local part",
                employer("mailuser014", "Aboubacar", "Bah", "us..er@example.com", "DemoPass6838")));
        cases.add(bad("email", "consecutive dots in domain",
                employer("mailuser015", "Mariatou", "Conte", "user@example..com", "DemoPass6939")));
        cases.add(bad("email", "domain starts with dot",
                employer("mailuser016", "Aliou", "Sow", "user@.example.com", "DemoPass7040")));
        cases.add(bad("email", "domain ends with dot",
                employer("mailuser017", "Romuald", "Dossou", "user@example.com.", "DemoPass7141")));
        cases.add(bad("email", "domain starts with hyphen",
                employer("mailuser018", "Clarisse", "Hounkpe", "user@-example.com", "DemoPass7242")));
        cases.add(bad("email", "comma instead of dot",
                employer("mailuser019", "Patrick", "Sossa", "user@example,com", "DemoPass7343")));
        cases.add(bad("email", "illegal characters / angle brackets",
                employer("mailuser020", "Komlan", "Agbodan", "us<er>@example.com", "DemoPass7444")));
        cases.add(bad("email", "unicode/illegal char in domain",
                employer("mailuser021", "Adjo", "Savi", "user@exämple.com", "DemoPass7545")));
        cases.add(bad("email", "just an @ symbol",
                employer("mailuser022", "Kodjo", "Tsi", "@", "DemoPass7646")));
        cases.add(bad("email", "missing @ but has dot",
                employer("mailuser023", "Natalio", "Gomes", "user.example.com", "DemoPass7747")));
        cases.add(bad("email", "whitespace only",
                employer("mailuser024", "Sofia", "Martins", "   ", "DemoPass7848")));
        cases.add(bad("email", "key missing",
                without("email",
                        employer("mailuser025", "Miguel", "Ferreira", "ignored@example.com", "DemoPass7949"))));
        cases.add(bad("email", "explicit JSON null",
                nulled("email",
                        employer("mailuser026", "Beatriz", "Santos", "ignored@example.com", "DemoPass8050"))));

        // =====================================================================
        // FIRST NAME — empty / blank / missing / null
        // =====================================================================
        cases.add(bad("firstName", "empty string",
                employer("fnameuser01", "", "Oliveira", "fname.empty01@example.com", "DemoPass8151")));
        cases.add(bad("firstName", "single space (blank after trim)",
                employer("fnameuser02", " ", "Rodrigues", "fname.empty02@example.com", "DemoPass8252")));
        cases.add(bad("firstName", "whitespace only (spaces/tabs)",
                employer("fnameuser03", "  \t  ", "Costa", "fname.empty03@example.com", "DemoPass8353")));
        cases.add(bad("firstName", "key missing",
                without("firstName",
                        employer("fnameuser04", "placeholder", "Pereira", "fname.missing@example.com", "DemoPass8454"))));
        cases.add(bad("firstName", "explicit JSON null",
                nulled("firstName",
                        employer("fnameuser05", "placeholder", "Carvalho", "fname.null@example.com", "DemoPass8555"))));

        // =====================================================================
        // FIRST NAME — too long (length > 200)
        // =====================================================================
        cases.add(bad("firstName", "201 chars (boundary: max + 1)",
                employer("fnameuser06", repeat("F", 201), "Sousa", "fname.long01@example.com", "DemoPass8656")));
        cases.add(bad("firstName", "500 chars",
                employer("fnameuser07", repeat("F", 500), "Almeida", "fname.long02@example.com", "DemoPass8757")));

        // =====================================================================
        // LAST NAME — empty / blank / missing / null
        // =====================================================================
        cases.add(bad("lastName", "empty string",
                employer("lnameuser01", "Joana", "", "lname.empty01@example.com", "DemoPass8858")));
        cases.add(bad("lastName", "single space (blank after trim)",
                employer("lnameuser02", "Pedro", " ", "lname.empty02@example.com", "DemoPass8959")));
        cases.add(bad("lastName", "whitespace only (spaces/tabs)",
                employer("lnameuser03", "Carolina", "  \t  ", "lname.empty03@example.com", "DemoPass9060")));
        cases.add(bad("lastName", "key missing",
                without("lastName",
                        employer("lnameuser04", "Goncalo", "placeholder", "lname.missing@example.com", "DemoPass9161"))));
        cases.add(bad("lastName", "explicit JSON null",
                nulled("lastName",
                        employer("lnameuser05", "Matilde", "placeholder", "lname.null@example.com", "DemoPass9262"))));

        // =====================================================================
        // LAST NAME — too long (length > 200)
        // =====================================================================
        cases.add(bad("lastName", "201 chars (boundary: max + 1)",
                employer("lnameuser06", "Rafael", repeat("L", 201), "lname.long01@example.com", "DemoPass9363")));
        cases.add(bad("lastName", "500 chars",
                employer("lnameuser07", "Leonor", repeat("L", 500), "lname.long02@example.com", "DemoPass9464")));

        // =====================================================================
        // MULTIPLE violations at once (every field invalid)
        // =====================================================================
        cases.add(bad("multiple", "short username + short password + bad email + empty names",
                employer("u", "", "", "not-an-email", "x")));
        cases.add(bad("multiple", "long username + missing-TLD email + over-long first name",
                employer(repeat("u", 60), repeat("F", 201), "Antunes", "user@host", "Pass1")));
        cases.add(bad("multiple", "all fields missing",
                new JSONObject()));
        cases.add(bad("multiple", "all fields explicit JSON null",
                new JSONObject()
                        .put("username", JSONObject.NULL)
                        .put("lastName", JSONObject.NULL)
                        .put("firstName", JSONObject.NULL)
                        .put("email", JSONObject.NULL)
                        .put("password", JSONObject.NULL)));
        cases.add(bad("multiple", "empty payload values all whitespace",
                employer("   ", "   ", "   ", "   ", "   ")));

        return cases;
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    /** Same field shape/order as the positive EmployerTestData fixture. */
    private static JSONObject employer(String username, String firstName, String lastName,
                                       String email, String password) throws JSONException {
        return new JSONObject()
                .put("username", username)
                .put("lastName", lastName)
                .put("firstName", firstName)
                .put("email", email)
                .put("password", password);
    }

    private static InvalidCase bad(String field, String reason, JSONObject payload) {
        return new InvalidCase(payload, field, reason);
    }

    /** Returns a copy of {@code base} with {@code key} removed entirely. */
    private static JSONObject without(String key, JSONObject base) {
        base.remove(key);
        return base;
    }

    /** Returns {@code base} with {@code key} mapped to an explicit JSON null. */
    private static JSONObject nulled(String key, JSONObject base) throws JSONException {
        return base.put(key, JSONObject.NULL);
    }

    /** Builds a string of {@code count} copies of {@code unit} (no Java-version assumptions). */
    private static String repeat(String unit, int count) {
        StringBuilder sb = new StringBuilder(unit.length() * count);
        for (int i = 0; i < count; i++) {
            sb.append(unit);
        }
        return sb.toString();
    }
}