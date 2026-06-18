package dev.hope.vulcaobackend.TestData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Extensive set of INVALID company registration payloads for negative tests.
 *
 * Body shape (creatorUsername and address.state intentionally removed):
 *   name, category, taxIdNumber, description, phoneNumber, faxNumber,
 *   revenue, email, address { island, city, zone, postalCode, street }, status
 *
 * Validation rules under test:
 *   - name        : required, non-empty (after trim), length <= 500, unique.
 *   - category    : required, one of the CATEGORY constants.
 *   - taxIdNumber : OPTIONAL (may be ""); if present must be exactly 9 digits
 *                   (NIF rule, per RegisterBusinessModal). See note below.
 *   - description : required, non-empty (after trim), length <= 5000.
 *   - phoneNumber : required, non-empty.
 *   - faxNumber   : OPTIONAL (may be ""); if present letters/digits/'-' only.
 *   - revenue     : required, numeric.
 *   - email       : required, well-formed, length 5..50 (inclusive).
 *   - address     : required object; island, city and zone are required
 *                   (postalCode and street are optional).
 *   - status      : required, one of OPERATING | INACTIVE | CLOSED.
 *
 * NOTE on taxIdNumber: these cases treat the 9-digit NIF rule as authoritative.
 * If your real rule is instead "letters/digits/'-'", swap the taxIdNumber
 * section for values containing illegal chars (space, '+', '@', ...).
 *
 * Design: each entry violates exactly ONE rule (every other field valid), so a
 * parameterized test can assert which field the API rejected. A final MULTIPLE
 * section breaks several rules at once. "missing" = key absent; "null" = key
 * maps to a JSON null literal.
 */
public class InvalidCompanyTestData {

    /** A single negative case: payload + which field is expected to fail + why. */
    public static final class InvalidCase {
        public final JSONObject payload;
        public final String field;   // name|category|taxIdNumber|description|phoneNumber|faxNumber|revenue|email|status|island|city|zone|address|multiple
        public final String reason;

        public InvalidCase(JSONObject payload, String field, String reason) {
            this.payload = payload;
            this.field = field;
            this.reason = reason;
        }

        @Override
        public String toString() {
            return "[" + field + "] " + reason;
        }
    }

    // ----- category enum values (exact strings, accents preserved) -----------
    private static final String SUPERMERCADO = "SUPERMERCADO";
    private static final String CATEGORIES_OUTROS = "OUTROS";

    // ----- status enum values ------------------------------------------------
    private static final String OPERATING = "OPERATING";

    public InvalidCompanyTestData() {
    }

    public List<JSONObject> invalidCompanies() throws JSONException {
        List<JSONObject> out = new ArrayList<>();
        for (InvalidCase c : invalidCompanyCases()) {
            out.add(c.payload);
        }
        return out;
    }

    public List<InvalidCase> invalidCompanyCases() throws JSONException {
        List<InvalidCase> cases = new ArrayList<>();

        // =====================================================================
        // NAME
        // =====================================================================
        cases.add(bad("name", "empty string",
                companyNamed("")));
        cases.add(bad("name", "single space (blank after trim)",
                companyNamed(" ")));
        cases.add(bad("name", "whitespace only",
                companyNamed("  \t  ")));
        cases.add(bad("name", "501 chars (max + 1)",
                companyNamed(repeat("N", 501))));
        cases.add(bad("name", "600 chars",
                companyNamed(repeat("N", 600))));
        cases.add(bad("name", "key missing",
                without("name", base("ignored-name"))));
        cases.add(bad("name", "explicit JSON null",
                nulled("name", base("ignored-name"))));

        // =====================================================================
        // CATEGORY
        // =====================================================================
        cases.add(bad("category", "empty string",
                withCategory("Cat-Empty Co", "")));
        cases.add(bad("category", "value not in enum",
                withCategory("Cat-Unknown Co", "NOT_A_CATEGORY")));
        cases.add(bad("category", "wrong case",
                withCategory("Cat-Case Co", "supermercado")));
        cases.add(bad("category", "near-miss (OUTRO vs OUTROS)",
                withCategory("Cat-NearMiss Co", "OUTRO")));
        cases.add(bad("category", "accents stripped",
                withCategory("Cat-NoAccent Co", "COMERCIO_ELETRONICO")));
        cases.add(bad("category", "key missing",
                without("category", base("Cat-Missing Co"))));
        cases.add(bad("category", "explicit JSON null",
                nulled("category", base("Cat-Null Co"))));

        // =====================================================================
        // DESCRIPTION
        // =====================================================================
        cases.add(bad("description", "empty string",
                withDescription("Desc-Empty Co", "")));
        cases.add(bad("description", "single space (blank after trim)",
                withDescription("Desc-Space Co", " ")));
        cases.add(bad("description", "whitespace only",
                withDescription("Desc-WS Co", "  \t  ")));
        cases.add(bad("description", "5001 chars (max + 1)",
                withDescription("Desc-Long Co", repeat("d", 5001))));
        cases.add(bad("description", "key missing",
                without("description", base("Desc-Missing Co"))));
        cases.add(bad("description", "explicit JSON null",
                nulled("description", base("Desc-Null Co"))));

        // =====================================================================
        // EMAIL  (valid format AND length 5..50)
        // =====================================================================
        cases.add(bad("email", "empty string",
                withEmail("Email-Empty Co", "")));
        cases.add(bad("email", "too short (< 5 chars)",
                withEmail("Email-Short Co", "a@b")));
        cases.add(bad("email", "too long (> 50 chars)",
                withEmail("Email-Long Co", repeat("a", 45) + "@example.com")));
        cases.add(bad("email", "no @ symbol",
                withEmail("Email-NoAt Co", "plainaddress")));
        cases.add(bad("email", "missing domain",
                withEmail("Email-NoDomain Co", "user@")));
        cases.add(bad("email", "missing local part",
                withEmail("Email-NoLocal Co", "@example.com")));
        cases.add(bad("email", "double @",
                withEmail("Email-DoubleAt Co", "user@@example.com")));
        cases.add(bad("email", "space in address",
                withEmail("Email-Space Co", "user name@example.com")));
        cases.add(bad("email", "missing TLD",
                withEmail("Email-NoTld Co", "user@example")));
        cases.add(bad("email", "key missing",
                without("email", base("Email-Missing Co"))));
        cases.add(bad("email", "explicit JSON null",
                nulled("email", base("Email-Null Co"))));

        // =====================================================================
        // TAX ID NUMBER  (optional; if present must be exactly 9 digits)
        // =====================================================================
        cases.add(bad("taxIdNumber", "8 digits (too short)",
                withTaxId("Tax-Short Co", "12345678")));
        cases.add(bad("taxIdNumber", "10 digits (too long)",
                withTaxId("Tax-Long Co", "1234567890")));
        cases.add(bad("taxIdNumber", "letters + dash (TIN-style, not 9 digits)",
                withTaxId("Tax-Letters Co", "TIN-2034567890")));
        cases.add(bad("taxIdNumber", "contains spaces",
                withTaxId("Tax-Space Co", "12 345 678")));
        cases.add(bad("taxIdNumber", "contains letters",
                withTaxId("Tax-Alpha Co", "12345678A")));
        cases.add(bad("taxIdNumber", "9 chars but with dash",
                withTaxId("Tax-Dash Co", "1234-5678")));

        // =====================================================================
        // FAX NUMBER  (optional; if present letters/digits/'-' only)
        // =====================================================================
        cases.add(bad("faxNumber", "contains '+'",
                withFax("Fax-Plus Co", "+1-555-0235")));
        cases.add(bad("faxNumber", "contains spaces",
                withFax("Fax-Space Co", "555 0235")));
        cases.add(bad("faxNumber", "contains '@'",
                withFax("Fax-At Co", "fax@123")));
        cases.add(bad("faxNumber", "contains parentheses",
                withFax("Fax-Paren Co", "(238)5550235")));

        // =====================================================================
        // REVENUE  (required, numeric)
        // =====================================================================
        cases.add(bad("revenue", "non-numeric string",
                base("Rev-String Co").put("revenue", "not-a-number")));
        cases.add(bad("revenue", "key missing",
                without("revenue", base("Rev-Missing Co"))));
        cases.add(bad("revenue", "explicit JSON null",
                nulled("revenue", base("Rev-Null Co"))));

        // =====================================================================
        // STATUS  (required, enum)
        // =====================================================================
        cases.add(bad("status", "empty string",
                withStatus("Status-Empty Co", "")));
        cases.add(bad("status", "value not in enum",
                withStatus("Status-Unknown Co", "ACTIVE")));
        cases.add(bad("status", "wrong case",
                withStatus("Status-Case Co", "operating")));
        cases.add(bad("status", "key missing",
                without("status", base("Status-Missing Co"))));
        cases.add(bad("status", "explicit JSON null",
                nulled("status", base("Status-Null Co"))));

        // =====================================================================
        // ADDRESS.island / city / zone  (required)
        // =====================================================================
        cases.add(bad("island", "empty string",
                withAddress("Addr-IslandEmpty Co", address("", "Praia", "Plateau", "7600", "Rua Central 1"))));
        cases.add(bad("island", "whitespace only",
                withAddress("Addr-IslandWS Co", address("   ", "Praia", "Plateau", "7600", "Rua Central 1"))));
        cases.add(bad("island", "key missing",
                withAddress("Addr-IslandMissing Co", withoutKey("island",
                        address("SANTIAGO", "Praia", "Plateau", "7600", "Rua Central 1")))));
        cases.add(bad("island", "explicit JSON null",
                withAddress("Addr-IslandNull Co", nulledKey("island",
                        address("SANTIAGO", "Praia", "Plateau", "7600", "Rua Central 1")))));

        cases.add(bad("city", "empty string",
                withAddress("Addr-CityEmpty Co", address("SANTIAGO", "", "Plateau", "7600", "Rua Central 1"))));
        cases.add(bad("city", "whitespace only",
                withAddress("Addr-CityWS Co", address("SANTIAGO", "   ", "Plateau", "7600", "Rua Central 1"))));
        cases.add(bad("city", "key missing",
                withAddress("Addr-CityMissing Co", withoutKey("city",
                        address("SANTIAGO", "Praia", "Plateau", "7600", "Rua Central 1")))));
        cases.add(bad("city", "explicit JSON null",
                withAddress("Addr-CityNull Co", nulledKey("city",
                        address("SANTIAGO", "Praia", "Plateau", "7600", "Rua Central 1")))));

        cases.add(bad("zone", "empty string",
                withAddress("Addr-ZoneEmpty Co", address("SANTIAGO", "Praia", "", "7600", "Rua Central 1"))));
        cases.add(bad("zone", "whitespace only",
                withAddress("Addr-ZoneWS Co", address("SANTIAGO", "Praia", "   ", "7600", "Rua Central 1"))));
        cases.add(bad("zone", "key missing",
                withAddress("Addr-ZoneMissing Co", withoutKey("zone",
                        address("SANTIAGO", "Praia", "Plateau", "7600", "Rua Central 1")))));
        cases.add(bad("zone", "explicit JSON null",
                withAddress("Addr-ZoneNull Co", nulledKey("zone",
                        address("SANTIAGO", "Praia", "Plateau", "7600", "Rua Central 1")))));

        // =====================================================================
        // ADDRESS object itself
        // =====================================================================
        cases.add(bad("address", "key missing",
                without("address", base("Addr-Missing Co"))));
        cases.add(bad("address", "explicit JSON null",
                nulled("address", base("Addr-Null Co"))));

        // =====================================================================
        // MULTIPLE violations at once
        // =====================================================================
        cases.add(bad("multiple", "name empty + bad category + bad email + status missing",
                without("status",
                        company("", "NOPE", VALID_TAX, VALID_DESC, VALID_PHONE, VALID_FAX,
                                VALID_REVENUE, "not-an-email", validAddress(), OPERATING))));
        cases.add(bad("multiple", "all required fields empty + invalid address",
                company("", "", "", "", "", VALID_FAX, VALID_REVENUE, "",
                        address("", "", "", "", ""), "")));
        cases.add(bad("multiple", "empty payload",
                new JSONObject()));

        return cases;
    }

    // -------------------------------------------------------------------------
    // Valid defaults (used to fill the non-targeted fields)
    // -------------------------------------------------------------------------
    private static final String VALID_DESC    = "Organic food production and distribution";
    private static final String VALID_PHONE   = "+238-555-0100";
    private static final String VALID_FAX     = "FAX-5550235";
    private static final String VALID_TAX     = "123456789"; // 9 digits
    private static final String VALID_EMAIL   = "info@greenleaf.com";
    private static final double VALID_REVENUE = 1750000.75;

    private static JSONObject validAddress() throws JSONException {
        return address("SANTIAGO", "Praia", "Plateau", "7600", "Rua Central 1");
    }

    /** Fully valid company except the caller overrides one field afterwards. */
    private static JSONObject base(String name) throws JSONException {
        return company(name, SUPERMERCADO, VALID_TAX, VALID_DESC, VALID_PHONE, VALID_FAX,
                VALID_REVENUE, VALID_EMAIL, validAddress(), OPERATING);
    }

    // single-field override builders (everything else valid) ------------------
    private static JSONObject companyNamed(String name) throws JSONException { return base(name); }

    private static JSONObject withCategory(String name, String category) throws JSONException {
        return company(name, category, VALID_TAX, VALID_DESC, VALID_PHONE, VALID_FAX,
                VALID_REVENUE, VALID_EMAIL, validAddress(), OPERATING);
    }
    private static JSONObject withDescription(String name, String description) throws JSONException {
        return company(name, SUPERMERCADO, VALID_TAX, description, VALID_PHONE, VALID_FAX,
                VALID_REVENUE, VALID_EMAIL, validAddress(), OPERATING);
    }
    private static JSONObject withEmail(String name, String email) throws JSONException {
        return company(name, SUPERMERCADO, VALID_TAX, VALID_DESC, VALID_PHONE, VALID_FAX,
                VALID_REVENUE, email, validAddress(), OPERATING);
    }
    private static JSONObject withTaxId(String name, String taxId) throws JSONException {
        return company(name, SUPERMERCADO, taxId, VALID_DESC, VALID_PHONE, VALID_FAX,
                VALID_REVENUE, VALID_EMAIL, validAddress(), OPERATING);
    }
    private static JSONObject withFax(String name, String fax) throws JSONException {
        return company(name, SUPERMERCADO, VALID_TAX, VALID_DESC, VALID_PHONE, fax,
                VALID_REVENUE, VALID_EMAIL, validAddress(), OPERATING);
    }
    private static JSONObject withStatus(String name, String status) throws JSONException {
        return company(name, SUPERMERCADO, VALID_TAX, VALID_DESC, VALID_PHONE, VALID_FAX,
                VALID_REVENUE, VALID_EMAIL, validAddress(), status);
    }
    private static JSONObject withAddress(String name, JSONObject address) throws JSONException {
        return company(name, SUPERMERCADO, VALID_TAX, VALID_DESC, VALID_PHONE, VALID_FAX,
                VALID_REVENUE, VALID_EMAIL, address, OPERATING);
    }

    // -------------------------------------------------------------------------
    // Core helpers
    // -------------------------------------------------------------------------

    /** Company body WITHOUT creatorUsername and WITHOUT address.state. */
    private static JSONObject company(String name, String category, String taxIdNumber,
                                      String description, String phoneNumber, String faxNumber,
                                      double revenue, String email, JSONObject address,
                                      String status) throws JSONException {
        return new JSONObject()
                .put("name", name)
                .put("category", category)
                .put("taxIdNumber", taxIdNumber)
                .put("description", description)
                .put("phoneNumber", phoneNumber)
                .put("faxNumber", faxNumber)
                .put("revenue", revenue)
                .put("email", email)
                .put("address", address)
                .put("status", status);
    }

    /** Address WITHOUT state. */
    private static JSONObject address(String island, String city, String zone,
                                      String postalCode, String street) throws JSONException {
        return new JSONObject()
                .put("island", island)
                .put("city", city)
                .put("zone", zone)
                .put("postalCode", postalCode)
                .put("street", street);
    }

    private static InvalidCase bad(String field, String reason, JSONObject payload) {
        return new InvalidCase(payload, field, reason);
    }

    private static JSONObject without(String key, JSONObject base) {
        base.remove(key);
        return base;
    }
    private static JSONObject nulled(String key, JSONObject base) throws JSONException {
        return base.put(key, JSONObject.NULL);
    }
    // same two, named distinctly for clarity when mutating the nested address
    private static JSONObject withoutKey(String key, JSONObject obj) {
        obj.remove(key);
        return obj;
    }
    private static JSONObject nulledKey(String key, JSONObject obj) throws JSONException {
        return obj.put(key, JSONObject.NULL);
    }

    private static String repeat(String unit, int count) {
        StringBuilder sb = new StringBuilder(unit.length() * count);
        for (int i = 0; i < count; i++) {
            sb.append(unit);
        }
        return sb.toString();
    }
}