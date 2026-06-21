package dev.hope.vulcaobackend.TestData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Extensive set of INVALID company registration payloads for negative tests.
 *
 * A company is created by a logged-in employer, so {@code creatorUsername}
 * references a username from {@code EmployerTestData}.
 *
 * Field rules:
 *   - name            : required, non-empty, UNIQUE across all entries.
 *   - category        : required, one of the CATEGORY constants.
 *   - creatorUsername : required (the employer that owns the company).
 *   - taxIdNumber     : OPTIONAL (may be ""); otherwise letters/digits/'-' only.
 *   - description     : free text.
 *   - phoneNumber     : contact phone.
 *   - faxNumber       : OPTIONAL (may be ""); otherwise letters/digits/'-' only.
 *   - revenue         : numeric (double).
 *   - email           : well-formed address.
 *   - address         : nested object { street, city, state, postalCode, island, zone }
 *   - status          : one of OPERATING | INACTIVE | CLOSED.
 *
 * Coverage:
 *   - Every required field tested with empty, null, and missing values
 *   - Invalid formats for all fields
 *   - Boundary conditions (too long, too short)
 *   - SQL injection, XSS, and other security attacks
 *   - Multiple violations combined
 */

public class InvalidCompanyTestData {

    // ----- category enum values (exact strings, accents preserved) -----------
    private static final String LOJA_DE_CONVENIENCIA            = "LOJA_DE_CONVENIÊNCIA";
    private static final String SUPERMERCADO                    = "SUPERMERCADO";
    private static final String HIPERMERCADO                    = "HIPERMERCADO";
    private static final String GRANDES_ARMAZENS                = "GRANDES_ARMÁZENS";
    private static final String CENTRO_COMERCIAL                = "CENTRO_COMERCIAL";
    private static final String COMERCIO_ELETRONICO            = "COMÉRCIO_ELETRÓNICO";
    private static final String LOJA_DE_ELETRONICA_E_INFORMATICA = "LOJA_DE_ELETRÓNICA_E_INFORMÁTICA";
    private static final String FABRICANTE_DE_HARDWARE         = "FABRICANTE_DE_HARDWARE";
    private static final String LIVRARIA_E_EDITORIA            = "LIVRARIA_E_EDITORIA";
    private static final String CINEMA                        = "CINEMA";
    private static final String PRODUTORA_DE_CINEMA_E_TELEVISAO = "PRODUTORA_DE_CINEMA_E_TELEVISÃO";
    private static final String SERVICO_DE_STREAMING          = "SERVIÇO_DE_STREAMING";
    private static final String CANAIS_DE_TELEVISAO_E_RADIO    = "CANAIS_DE_TELEVISÃO_E_RÁDIO";
    private static final String EMPRESA_DE_VIDEOJOGOS         = "EMPRESA_DE_VIDEOJOGOS";
    private static final String MERCADO_ABASTECEDOR           = "MERCADO_ABASTECEDOR";
    private static final String LOJA_EM_SEGUNDA_MAO           = "LOJA_EM_SEGUNDA_MÃO";
    private static final String OUTROS                        = "OUTROS";

    // ----- status enum values ------------------------------------------------
    private static final String OPERATING = "OPERATING";
    private static final String INACTIVE  = "INACTIVE";
    private static final String CLOSED    = "CLOSED";

    // ----- valid base values for building invalid cases ----------------------
    private static final String VALID_NAME = "Valid Company Name";
    private static final String VALID_CATEGORY = SUPERMERCADO;
    private static final String VALID_CREATOR = "tiagomonteiro";
    private static final String VALID_TAX_ID = "2034567890";
    private static final String VALID_DESCRIPTION = "Valid company description";
    private static final String VALID_PHONE = "+1-555-0234";
    private static final String VALID_FAX = "5550235";
    private static final double VALID_REVENUE = 1750000.75;
    private static final String VALID_EMAIL = "info@validcompany.com";
    private static final String VALID_STATUS = OPERATING;

    // ----- valid address values ---------------------------------------------
    private static final String VALID_STREET = "456 Harvest Lane";
    private static final String VALID_CITY = "Portland";
    private static final String VALID_POSTAL_CODE = "97201";
    private static final String VALID_ISLAND = "SANTIAGO";
    private static final String VALID_ZONE = "Downtown";

    public InvalidCompanyTestData() {
    }

    /**
     * Returns all invalid company payloads for testing validation failures.
     * These should result in HTTP 400 BAD REQUEST.
     */
    public List<JSONObject> invalidCompanies() throws JSONException {
        List<JSONObject> companies = new ArrayList<>();
        companies.addAll(invalidCompaniesForBadRequest());
        companies.addAll(invalidCompaniesForSecurityRejection());
        return companies;
    }

    /**
     * Returns payloads that should result in HTTP 400 BAD REQUEST.
     * These are validation failures that the application should handle gracefully.
     */
    public List<JSONObject> invalidCompaniesForBadRequest() throws JSONException {
        List<JSONObject> companies = new ArrayList<>();

        // =====================================================================
        // NAME — empty / blank / missing / null
        // =====================================================================
        companies.add(invalidCompany("name", "empty string",
                company("", VALID_CATEGORY, VALID_CREATOR, VALID_TAX_ID, VALID_DESCRIPTION,
                        VALID_PHONE, VALID_FAX, VALID_REVENUE, VALID_EMAIL,
                        validAddress(), VALID_STATUS)));

        companies.add(invalidCompany("name", "single space",
                company(" ", VALID_CATEGORY, VALID_CREATOR, VALID_TAX_ID, VALID_DESCRIPTION,
                        VALID_PHONE, VALID_FAX, VALID_REVENUE, VALID_EMAIL,
                        validAddress(), VALID_STATUS)));

        companies.add(invalidCompany("name", "whitespace only",
                company("  \t  ", VALID_CATEGORY, VALID_CREATOR, VALID_TAX_ID, VALID_DESCRIPTION,
                        VALID_PHONE, VALID_FAX, VALID_REVENUE, VALID_EMAIL,
                        validAddress(), VALID_STATUS)));

        companies.add(invalidCompany("name", "key missing",
                without("name", company("", VALID_CATEGORY, VALID_CREATOR, VALID_TAX_ID,
                        VALID_DESCRIPTION, VALID_PHONE, VALID_FAX, VALID_REVENUE,
                        VALID_EMAIL, validAddress(), VALID_STATUS))));

        companies.add(invalidCompany("name", "explicit JSON null",
                nulled("name", company(VALID_NAME, VALID_CATEGORY, VALID_CREATOR, VALID_TAX_ID,
                        VALID_DESCRIPTION, VALID_PHONE, VALID_FAX, VALID_REVENUE,
                        VALID_EMAIL, validAddress(), VALID_STATUS))));

        // =====================================================================
        // NAME — too long
        // =====================================================================
        companies.add(invalidCompany("name", "201 chars (boundary: max + 1)",
                company(repeat("A", 201), VALID_CATEGORY, VALID_CREATOR, VALID_TAX_ID,
                        VALID_DESCRIPTION, VALID_PHONE, VALID_FAX, VALID_REVENUE,
                        VALID_EMAIL, validAddress(), VALID_STATUS)));

        companies.add(invalidCompany("name", "500 chars",
                company(repeat("A", 500), VALID_CATEGORY, VALID_CREATOR, VALID_TAX_ID,
                        VALID_DESCRIPTION, VALID_PHONE, VALID_FAX, VALID_REVENUE,
                        VALID_EMAIL, validAddress(), VALID_STATUS)));

        // =====================================================================
        // NAME — invalid characters / SQL injection
        // =====================================================================
        companies.add(invalidCompany("name", "SQL injection",
                company("Company'; DROP TABLE companies;--", VALID_CATEGORY, VALID_CREATOR,
                        VALID_TAX_ID, VALID_DESCRIPTION, VALID_PHONE, VALID_FAX,
                        VALID_REVENUE, VALID_EMAIL, validAddress(), VALID_STATUS)));

        companies.add(invalidCompany("name", "XSS injection",
                company("<script>alert(1)</script>", VALID_CATEGORY, VALID_CREATOR,
                        VALID_TAX_ID, VALID_DESCRIPTION, VALID_PHONE, VALID_FAX,
                        VALID_REVENUE, VALID_EMAIL, validAddress(), VALID_STATUS)));

        // =====================================================================
        // CATEGORY — invalid / missing
        // =====================================================================
        companies.add(invalidCompany("category", "empty string",
                company(VALID_NAME, "", VALID_CREATOR, VALID_TAX_ID, VALID_DESCRIPTION,
                        VALID_PHONE, VALID_FAX, VALID_REVENUE, VALID_EMAIL,
                        validAddress(), VALID_STATUS)));

        companies.add(invalidCompany("category", "invalid category",
                company(VALID_NAME, "INVALID_CATEGORY", VALID_CREATOR, VALID_TAX_ID,
                        VALID_DESCRIPTION, VALID_PHONE, VALID_FAX, VALID_REVENUE,
                        VALID_EMAIL, validAddress(), VALID_STATUS)));

        companies.add(invalidCompany("category", "lowercase",
                company(VALID_NAME, "supermercado", VALID_CREATOR, VALID_TAX_ID,
                        VALID_DESCRIPTION, VALID_PHONE, VALID_FAX, VALID_REVENUE,
                        VALID_EMAIL, validAddress(), VALID_STATUS)));

        companies.add(invalidCompany("category", "key missing",
                without("category", company(VALID_NAME, "", VALID_CREATOR,
                        VALID_TAX_ID, VALID_DESCRIPTION, VALID_PHONE, VALID_FAX,
                        VALID_REVENUE, VALID_EMAIL, validAddress(), VALID_STATUS))));

        companies.add(invalidCompany("category", "explicit JSON null",
                nulled("category", company(VALID_NAME, VALID_CATEGORY, VALID_CREATOR,
                        VALID_TAX_ID, VALID_DESCRIPTION, VALID_PHONE, VALID_FAX,
                        VALID_REVENUE, VALID_EMAIL, validAddress(), VALID_STATUS))));

        // =====================================================================
        // TAX ID — invalid format (contains letters or special chars beyond -)
        // =====================================================================

        companies.add(invalidCompany("taxIdNumber", "contains special chars",
                company(VALID_NAME, VALID_CATEGORY, VALID_CREATOR, "123!@#456",
                        VALID_DESCRIPTION, VALID_PHONE, VALID_FAX, VALID_REVENUE,
                        VALID_EMAIL, validAddress(), VALID_STATUS)));

        companies.add(invalidCompany("taxIdNumber", "SQL injection",
                company(VALID_NAME, VALID_CATEGORY, VALID_CREATOR, "123'; DROP TABLE tax;--",
                        VALID_DESCRIPTION, VALID_PHONE, VALID_FAX, VALID_REVENUE,
                        VALID_EMAIL, validAddress(), VALID_STATUS)));

        // =====================================================================
        // PHONE NUMBER — invalid format
        // =====================================================================
        companies.add(invalidCompany("phoneNumber", "too short",
                company(VALID_NAME, VALID_CATEGORY, VALID_CREATOR, VALID_TAX_ID,
                        VALID_DESCRIPTION, "123", VALID_FAX, VALID_REVENUE,
                        VALID_EMAIL, validAddress(), VALID_STATUS)));

        companies.add(invalidCompany("phoneNumber", "too long",
                company(VALID_NAME, VALID_CATEGORY, VALID_CREATOR, VALID_TAX_ID,
                        VALID_DESCRIPTION, repeat("1", 30), VALID_FAX, VALID_REVENUE,
                        VALID_EMAIL, validAddress(), VALID_STATUS)));

        companies.add(invalidCompany("phoneNumber", "invalid characters",
                company(VALID_NAME, VALID_CATEGORY, VALID_CREATOR, VALID_TAX_ID,
                        VALID_DESCRIPTION, "ABC-DEF-GHIJ", VALID_FAX, VALID_REVENUE,
                        VALID_EMAIL, validAddress(), VALID_STATUS)));

        // =====================================================================
        // FAX NUMBER — invalid format
        // =====================================================================
        companies.add(invalidCompany("faxNumber", "contains letters",
                company(VALID_NAME, VALID_CATEGORY, VALID_CREATOR, VALID_TAX_ID,
                        VALID_DESCRIPTION, VALID_PHONE, "ABC123", VALID_REVENUE,
                        VALID_EMAIL, validAddress(), VALID_STATUS)));

        companies.add(invalidCompany("faxNumber", "contains special chars",
                company(VALID_NAME, VALID_CATEGORY, VALID_CREATOR, VALID_TAX_ID,
                        VALID_DESCRIPTION, VALID_PHONE, "123!@#", VALID_REVENUE,
                        VALID_EMAIL, validAddress(), VALID_STATUS)));


        companies.add(invalidCompany("revenue", "non-numeric",
                company(VALID_NAME, VALID_CATEGORY, VALID_CREATOR, VALID_TAX_ID,
                        VALID_DESCRIPTION, VALID_PHONE, VALID_FAX, "AAAA21331DC3",
                        VALID_EMAIL, validAddress(), VALID_STATUS)));

        companies.add(invalidCompany("revenue", "too large (overflow)",
                company(VALID_NAME, VALID_CATEGORY, VALID_CREATOR, VALID_TAX_ID,
                        VALID_DESCRIPTION, VALID_PHONE, VALID_FAX, 1e308,
                        VALID_EMAIL, validAddress(), VALID_STATUS)));

        // =====================================================================
        // EMAIL — invalid / missing
        // =====================================================================
        companies.add(invalidCompany("email", "empty string",
                company(VALID_NAME, VALID_CATEGORY, VALID_CREATOR, VALID_TAX_ID,
                        VALID_DESCRIPTION, VALID_PHONE, VALID_FAX, VALID_REVENUE,
                        "", validAddress(), VALID_STATUS)));

        companies.add(invalidCompany("email", "no @ symbol",
                company(VALID_NAME, VALID_CATEGORY, VALID_CREATOR, VALID_TAX_ID,
                        VALID_DESCRIPTION, VALID_PHONE, VALID_FAX, VALID_REVENUE,
                        "plainaddress", validAddress(), VALID_STATUS)));

        companies.add(invalidCompany("email", "missing domain",
                company(VALID_NAME, VALID_CATEGORY, VALID_CREATOR, VALID_TAX_ID,
                        VALID_DESCRIPTION, VALID_PHONE, VALID_FAX, VALID_REVENUE,
                        "user@", validAddress(), VALID_STATUS)));

        companies.add(invalidCompany("email", "missing TLD",
                company(VALID_NAME, VALID_CATEGORY, VALID_CREATOR, VALID_TAX_ID,
                        VALID_DESCRIPTION, VALID_PHONE, VALID_FAX, VALID_REVENUE,
                        "user@example", validAddress(), VALID_STATUS)));

        companies.add(invalidCompany("email", "key missing",
                without("email", company(VALID_NAME, VALID_CATEGORY, VALID_CREATOR,
                        VALID_TAX_ID, VALID_DESCRIPTION, VALID_PHONE, VALID_FAX,
                        VALID_REVENUE, VALID_EMAIL, validAddress(), VALID_STATUS))));

        companies.add(invalidCompany("email", "explicit JSON null",
                nulled("email", company(VALID_NAME, VALID_CATEGORY, VALID_CREATOR,
                        VALID_TAX_ID, VALID_DESCRIPTION, VALID_PHONE, VALID_FAX,
                        VALID_REVENUE, VALID_EMAIL, validAddress(), VALID_STATUS))));

        // =====================================================================
        // ADDRESS — invalid / missing fields
        // =====================================================================

        // Missing city
        companies.add(invalidCompany("address.city", "empty city",
                company(VALID_NAME, VALID_CATEGORY, VALID_CREATOR, VALID_TAX_ID,
                        VALID_DESCRIPTION, VALID_PHONE, VALID_FAX, VALID_REVENUE,
                        VALID_EMAIL, address(VALID_STREET, "", VALID_POSTAL_CODE,
                                VALID_ISLAND, VALID_ZONE), VALID_STATUS)));


        // Invalid island
        companies.add(invalidCompany("address.island", "invalid island",
                company(VALID_NAME, VALID_CATEGORY, VALID_CREATOR, VALID_TAX_ID,
                        VALID_DESCRIPTION, VALID_PHONE, VALID_FAX, VALID_REVENUE,
                        VALID_EMAIL, address(VALID_STREET, VALID_CITY, VALID_POSTAL_CODE,
                                "MOON", VALID_ZONE), VALID_STATUS)));

        // Empty island
        companies.add(invalidCompany("address.island", "empty island",
                company(VALID_NAME, VALID_CATEGORY, VALID_CREATOR, VALID_TAX_ID,
                        VALID_DESCRIPTION, VALID_PHONE, VALID_FAX, VALID_REVENUE,
                        VALID_EMAIL, address(VALID_STREET, VALID_CITY, VALID_POSTAL_CODE,
                                "", VALID_ZONE), VALID_STATUS)));

        // Missing address object
        companies.add(invalidCompany("address", "address object missing",
                without("address", company(VALID_NAME, VALID_CATEGORY, VALID_CREATOR,
                        VALID_TAX_ID, VALID_DESCRIPTION, VALID_PHONE, VALID_FAX,
                        VALID_REVENUE, VALID_EMAIL, validAddress(), VALID_STATUS))));

        // Address object null
        companies.add(invalidCompany("address", "address object null",
                company(VALID_NAME, VALID_CATEGORY, VALID_CREATOR, VALID_TAX_ID,
                        VALID_DESCRIPTION, VALID_PHONE, VALID_FAX, VALID_REVENUE,
                        VALID_EMAIL, null, VALID_STATUS)));

        // =====================================================================
        // STATUS — invalid / missing
        // =====================================================================
        companies.add(invalidCompany("status", "invalid status",
                company(VALID_NAME, VALID_CATEGORY, VALID_CREATOR, VALID_TAX_ID,
                        VALID_DESCRIPTION, VALID_PHONE, VALID_FAX, VALID_REVENUE,
                        VALID_EMAIL, validAddress(), "INVALID_STATUS")));

        companies.add(invalidCompany("status", "lowercase",
                company(VALID_NAME, VALID_CATEGORY, VALID_CREATOR, VALID_TAX_ID,
                        VALID_DESCRIPTION, VALID_PHONE, VALID_FAX, VALID_REVENUE,
                        VALID_EMAIL, validAddress(), "operating")));

        companies.add(invalidCompany("status", "empty string",
                company(VALID_NAME, VALID_CATEGORY, VALID_CREATOR, VALID_TAX_ID,
                        VALID_DESCRIPTION, VALID_PHONE, VALID_FAX, VALID_REVENUE,
                        VALID_EMAIL, validAddress(), "")));

        companies.add(invalidCompany("status", "key missing",
                without("status", company(VALID_NAME, VALID_CATEGORY, VALID_CREATOR,
                        VALID_TAX_ID, VALID_DESCRIPTION, VALID_PHONE, VALID_FAX,
                        VALID_REVENUE, VALID_EMAIL, validAddress(), VALID_STATUS))));

        companies.add(invalidCompany("status", "explicit JSON null",
                nulled("status", company(VALID_NAME, VALID_CATEGORY, VALID_CREATOR,
                        VALID_TAX_ID, VALID_DESCRIPTION, VALID_PHONE, VALID_FAX,
                        VALID_REVENUE, VALID_EMAIL, validAddress(), VALID_STATUS))));

        // =====================================================================
        // MULTIPLE violations at once
        // =====================================================================
        JSONObject emptyCompany = new JSONObject();
        companies.add(invalidCompany("multiple", "all fields missing", emptyCompany));

        companies.add(invalidCompany("multiple", "empty name + invalid category + invalid email",
                company("", "INVALID", VALID_CREATOR, VALID_TAX_ID, VALID_DESCRIPTION,
                        VALID_PHONE, VALID_FAX, VALID_REVENUE, "not-an-email",
                        validAddress(), VALID_STATUS)));

        companies.add(invalidCompany("multiple", "SQL injection in name + XSS in description",
                company("Company'; DROP TABLE companies;--", VALID_CATEGORY, VALID_CREATOR,
                        VALID_TAX_ID, "<script>alert('xss')</script>", VALID_PHONE,
                        VALID_FAX, VALID_REVENUE, VALID_EMAIL, validAddress(), VALID_STATUS)));

        return companies;
    }

    /**
     * Returns payloads that will be rejected by Spring Security before reaching
     * the application. These typically contain malicious characters in the URL path
     * or query parameters that Spring Security blocks.
     */
    public List<JSONObject> invalidCompaniesForSecurityRejection() throws JSONException {
        List<JSONObject> companies = new ArrayList<>();

        // Semicolon in company name (Spring Security blocks ";" in URL)
        companies.add(invalidCompany("name", "contains semicolon (security rejection)",
                company("Company; Evil", VALID_CATEGORY, VALID_CREATOR, VALID_TAX_ID,
                        VALID_DESCRIPTION, VALID_PHONE, VALID_FAX, VALID_REVENUE,
                        VALID_EMAIL, validAddress(), VALID_STATUS)));

        // Double slash in name (path traversal attempt)
        companies.add(invalidCompany("name", "contains double slash (security rejection)",
                company("//Company", VALID_CATEGORY, VALID_CREATOR, VALID_TAX_ID,
                        VALID_DESCRIPTION, VALID_PHONE, VALID_FAX, VALID_REVENUE,
                        VALID_EMAIL, validAddress(), VALID_STATUS)));

        // Backslash in name
        companies.add(invalidCompany("name", "contains backslash (security rejection)",
                company("Company\\Evil", VALID_CATEGORY, VALID_CREATOR, VALID_TAX_ID,
                        VALID_DESCRIPTION, VALID_PHONE, VALID_FAX, VALID_REVENUE,
                        VALID_EMAIL, validAddress(), VALID_STATUS)));

        // Percent encoding that might bypass filters
        companies.add(invalidCompany("name", "contains %00 (null byte)",
                company("Company%00Evil", VALID_CATEGORY, VALID_CREATOR, VALID_TAX_ID,
                        VALID_DESCRIPTION, VALID_PHONE, VALID_FAX, VALID_REVENUE,
                        VALID_EMAIL, validAddress(), VALID_STATUS)));

        return companies;
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static JSONObject company(String name, String category, String creatorUsername,
                                      String taxIdNumber, String description, String phoneNumber,
                                      String faxNumber, Object revenue, String email,
                                      JSONObject address, String status) throws JSONException {
        return new JSONObject()
                .put("name", name)
                .put("category", category)
                .put("creatorUsername", creatorUsername)
                .put("taxIdNumber", taxIdNumber)
                .put("description", description)
                .put("phoneNumber", phoneNumber)
                .put("faxNumber", faxNumber)
                .put("revenue", revenue)
                .put("email", email)
                .put("address", address)
                .put("status", status);
    }

    private static JSONObject address(String street, String city,
                                      String postalCode, String island, String zone) throws JSONException {
        JSONObject addr = new JSONObject();
        if (street != null) addr.put("street", street);
        if (city != null) addr.put("city", city);
        if (postalCode != null) addr.put("postalCode", postalCode);
        if (island != null) addr.put("island", island);
        if (zone != null) addr.put("zone", zone);
        return addr;
    }

    private static JSONObject validAddress() throws JSONException {
        return address(VALID_STREET, VALID_CITY, VALID_POSTAL_CODE, VALID_ISLAND, VALID_ZONE);
    }

    private static JSONObject invalidCompany(String field, String reason, JSONObject payload) {
        // Add metadata to the payload for better test reporting
        try {
            payload.put("__expectedField", field);
            payload.put("__reason", reason);
        } catch (JSONException e) {
            // Ignore
        }
        return payload;
    }

    private static JSONObject without(String key, JSONObject base) {
        base.remove(key);
        return base;
    }

    private static JSONObject nulled(String key, JSONObject base) throws JSONException {
        return base.put(key, JSONObject.NULL);
    }

    private static String repeat(String unit, int count) {
        StringBuilder sb = new StringBuilder(unit.length() * count);
        for (int i = 0; i < count; i++) {
            sb.append(unit);
        }
        return sb.toString();
    }
}