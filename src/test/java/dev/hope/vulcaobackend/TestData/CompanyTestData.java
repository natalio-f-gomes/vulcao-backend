package dev.hope.vulcaobackend.TestData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Extensive set of VALID company registration payloads for tests.
 *
 * A company is created by a logged-in employer, so {@code creatorUsername}
 * references a username from {@code EmployerTestData} (so the same value can be
 * passed through once the employer authenticates).
 *
 * Field rules:
 *   - name            : required, non-empty, UNIQUE across all entries.
 *   - category        : required, one of the CATEGORY constants below.
 *   - creatorUsername : required (the employer that owns the company).
 *   - taxIdNumber     : OPTIONAL (may be ""); otherwise letters/digits/'-' only.
 *   - description     : free text.
 *   - phoneNumber     : contact phone.
 *   - faxNumber       : OPTIONAL (may be ""); otherwise letters/digits/'-' only.
 *   - revenue         : numeric (double).
 *   - email           : well-formed address.
 *   - address         : nested object { street, city, state, postalCode,
 *                       island, zone } where zone may be "".
 *   - status          : one of OPERATING | INACTIVE | CLOSED.
 *
 * Coverage:
 *   - every category appears at least once,
 *   - all three statuses are represented,
 *   - the optional fields (taxIdNumber, faxNumber, zone) are exercised both
 *     populated and empty.
 */
public class CompanyTestData {

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


    public CompanyTestData() {
    }

    public List<JSONObject> validCompanies() throws JSONException {
        List<JSONObject> companies = new ArrayList<>();

        // ===== One per category, mostly OPERATING ============================
        companies.add(company("GreenLeaf Organics", SUPERMERCADO, "tiagomonteiro",
                "TIN-2034567890", "Organic food production and distribution",
                "+1-555-0234", "FAX-5550235", 1750000.75, "info@greenleaf.com",
                address("456 Harvest Lane", "Portland", "97201", "SANTIAGO", "Downtown"),
                OPERATING));

        companies.add(company("QuickStop Mart", LOJA_DE_CONVENIENCIA, "ritafigueiredo",
                "TIN-1102938475", "Neighbourhood convenience store open 24/7",
                "+238-555-0101", "", 98000.50, "contact@quickstop.cv",
                address("12 Rua do Plateau", "Praia",  "7600", "SANTIAGO", "Plateau"),
                OPERATING));

        companies.add(company("MegaSaver Supermarket", SUPERMERCADO, "davidsilveira",
                "TIN-5566778899", "Full-range supermarket with fresh produce",
                "+238-555-0102", "FAX-2384410", 2450000.00, "support@megasaver.cv",
                address("88 Avenida Cidade Velha", "Praia", "7601", "SANTIAGO", "Achada"),
                OPERATING));

        companies.add(company("HiperMundo", HIPERMERCADO, "saracunha",
                "TIN-4040404040", "Large-format hypermarket and homeware",
                "+238-555-0103", "FAX-2384411", 6120000.90, "geral@hipermundo.cv",
                address("Zona Industrial Tira-Chapeu", "Praia", "7602", "SANTIAGO", ""),
                OPERATING));

        companies.add(company("Grande Armazém Atlântico", GRANDES_ARMAZENS, "joaopaiva",
                "CV-99887766", "Department store across multiple floors",
                "+238-555-0104", "FAX-2384412", 3875000.25, "info@armazematlantico.cv",
                address("Rua de Lisboa 23", "Mindelo", "2110", "SANTIAGO", "Centro"),
                OPERATING));

        companies.add(company("Plateau Shopping Center", CENTRO_COMERCIAL, "danielamatos",
                "TIN-7001234567", "Mixed-use shopping centre with 60 stores",
                "+238-555-0105", "", 8900000.00, "manage@plateaushopping.cv",
                address("Praça Alexandre Albuquerque", "Praia", "7603", "SANTIAGO", "Plateau"),
                OPERATING));

        companies.add(company("CompraJá Online", COMERCIO_ELETRONICO, "rodrigoneves",
                "", "E-commerce marketplace for local goods",
                "+238-555-0106", "FAX-2384413", 1320000.40, "hello@compraja.cv",
                address("Servidor Central s/n", "Praia", "7604", "SANTIAGO", "Palmarejo"),
                OPERATING));

        companies.add(company("TechPoint Eletrónica", LOJA_DE_ELETRONICA_E_INFORMATICA, "patriciaguerreiro",
                "TIN-3091827364", "Consumer electronics and computer retailer",
                "+238-555-0107", "FAX-2384414", 980000.10, "sales@techpoint.cv",
                address("Rua Banana 5", "Mindelo", "2111", "SANTIAGO", "Fonte Inês"),
                OPERATING));

        companies.add(company("SiliconForge Hardware", FABRICANTE_DE_HARDWARE, "sergiopacheco",
                "TIN-8123456701", "Designs and assembles networking hardware",
                "+1-555-0420", "FAX-5550421", 14250000.00, "info@siliconforge.com",
                address("900 Circuit Drive", "Austin", "78701", "SANTIAGO", "TechPark"),
                OPERATING));

        companies.add(company("Livraria Horizonte", LIVRARIA_E_EDITORIA, "claudiaalves",
                "TIN-6655443322", "Bookstore and small publishing house",
                "+351-555-0108", "", 410000.65, "geral@livrariahorizonte.pt",
                address("Rua Garrett 100", "Lisboa", "1200-145", "FOGO", "Chiado"),
                OPERATING));

        companies.add(company("CineStar Multiplex", CINEMA, "nelsonandrade",
                "TIN-2233445566", "Eight-screen cinema with IMAX",
                "+238-555-0109", "FAX-2384415", 2150000.00, "tickets@cinestar.cv",
                address("Avenida Marginal 14", "Mindelo", "2112", "FOGO", "Laginha"),
                OPERATING));

        companies.add(company("Atlantic Films Studios", PRODUTORA_DE_CINEMA_E_TELEVISAO, "lucianagomes",
                "TIN-9988776655", "Film and television production company",
                "+1-555-0500", "FAX-5550501", 23400000.00, "studio@atlanticfilms.com",
                address("12 Backlot Avenue", "Burbank", "91502", "FOGO", "Studio Row"),
                OPERATING));

        companies.add(company("StreamWave", SERVICO_DE_STREAMING, "jorgefortes",
                "", "Subscription video streaming platform",
                "+1-555-0600", "", 41750000.50, "care@streamwave.com",
                address("1 Cloud Way", "Seattle", "98101", "FOGO", ""),
                OPERATING));

        companies.add(company("RTV Crioula", CANAIS_DE_TELEVISAO_E_RADIO, "elenabarbosa",
                "TIN-1029384756", "Regional television and radio broadcaster",
                "+238-555-0110", "FAX-2384416", 5600000.00, "contacto@rtvcrioula.cv",
                address("Estrada da Várzea 2", "Praia", "7605", "FOGO", "Várzea"),
                OPERATING));

        companies.add(company("PixelPlay Games", EMPRESA_DE_VIDEOJOGOS, "adilsonpires",
                "TIN-5647382910", "Independent video game developer and publisher",
                "+1-555-0700", "FAX-5550701", 7850000.20, "team@pixelplay.com",
                address("44 Arcade Street", "Montréal", "H2X1Y4", "FOGO", "Mile End"),
                OPERATING));

        companies.add(company("Mercado Abastecedor Central", MERCADO_ABASTECEDOR, "mariocande",
                "TIN-3344556677", "Wholesale supply market for fresh goods",
                "+245-555-0111", "FAX-2455510", 9200000.00, "info@mercadocentral.gw",
                address("Bairro Militar s/n", "Bissau", "1000", "MAIO", "Militar"),
                OPERATING));

        companies.add(company("ReUse Brechó", LOJA_EM_SEGUNDA_MAO, "ousmanendiaye",
                "TIN-7766554433", "Second-hand clothing and furniture store",
                "+221-555-0112", "FAX-2215510", 156000.30, "ola@reusebrecho.sn",
                address("Avenue Bourguiba 18", "Dakar", "11000", "MAIO", ""),
                OPERATING));

        companies.add(company("Diversos & Cia", OUTROS, "aissatousow",
                "TIN-1212121212", "General trade and miscellaneous services",
                "+221-555-0113", "", 305000.00, "geral@diversoscia.sn",
                address("Rue 10 Medina", "Dakar",  "11001", "MAIO", "Medina"),
                OPERATING));

        // ===== INACTIVE companies ===========================================
        companies.add(company("NightOwl Convenience", LOJA_DE_CONVENIENCIA, "ibrahimaseck",
                "TIN-8080808080", "Late-night convenience store, temporarily paused",
                "+221-555-0114", "FAX-2215511", 64000.00, "info@nightowl.sn",
                address("Rue 22 Plateau", "Dakar", "11002", "MAIO", "Plateau"),
                INACTIVE));

        companies.add(company("ValueFresh Super", SUPERMERCADO, "khadijandour",
                "", "Supermarket awaiting reopening after renovation",
                "+221-555-0115", "", 0.00, "contact@valuefresh.sn",
                address("Route de Ouakam 7", "Dakar", "11003", "MAIO", "Ouakam"),
                INACTIVE));

        companies.add(company("BookHaven Editora", LIVRARIA_E_EDITORIA, "yawmensah",
                "TIN-9090909090", "Publishing house on hiatus between seasons",
                "+233-555-0116", "", 220000.45, "info@bookhaven.gh",
                address("Independence Ave 31", "Accra", "GA-145", "MAIO", "Ridge"),
                INACTIVE));

        companies.add(company("Cineteca Ilha", CINEMA, "moussakonate",
                "TIN-1357913579", "Art-house cinema closed for the off-season",
                "+223-555-0117", "FAX-2235510", 134000.00, "info@cinetecailha.ml",
                address("Rue 305 Hamdallaye", "Bamako",  "BP-110", "MAIO", "Hamdallaye"),
                INACTIVE));

        // ===== CLOSED companies =============================================
        companies.add(company("CityHyper", HIPERMERCADO, "oluwaseyiadebayo",
                "TIN-2468024680", "Hypermarket permanently closed in 2025",
                "+234-555-0118", "FAX-2345510", 0.00, "records@cityhyper.ng",
                address("15 Awolowo Road", "Lagos", "101233", "MAIO", "Ikoyi"),
                CLOSED));

        companies.add(company("RetroGames Loja", EMPRESA_DE_VIDEOJOGOS, "kofiboateng",
                "TIN-1928374650", "Retro game shop, business wound down",
                "+233-555-0119", "", 0.00, "hello@retrogames.gh",
                address("Oxford Street 9", "Accra", "GA-200", "SAL", "Osu"),
                CLOSED));

        companies.add(company("Armazéns do Sul", GRANDES_ARMAZENS, "nataliogomes",
                "TIN-5050505050", "Former department store, now closed",
                "+351-555-0120", "FAX-3515510", 0.00, "arquivo@armazenssul.pt",
                address("Rua do Carmo 40", "Porto", "4000-100", "SAL", ""),
                CLOSED));

        // ===== Extra OPERATING entries exercising empty optionals ===========
        companies.add(company("EcoMercado Online", COMERCIO_ELETRONICO, "yaokouassi",
                "", "Eco-friendly e-commerce store",
                "+225-555-0121", "", 540000.00, "ola@ecomercado.ci",
                address("Boulevard Lagunaire 3", "Abidjan",  "01-BP-450", "SAL", ""),
                OPERATING));

        companies.add(company("HardData Manufacturing", FABRICANTE_DE_HARDWARE, "aboubacarbah",
                "TIN-6161616161", "Contract manufacturer of storage devices",
                "+224-555-0122", "FAX-2245510", 18900000.00, "sales@harddata.gn",
                address("Route Le Prince 5", "Conakry", "BP-330", "SAL", "Ratoma"),
                OPERATING));

        companies.add(company("SegundaMão Express", LOJA_EM_SEGUNDA_MAO, "romualddossou",
                "TIN-7373737373", "Fast-turnaround second-hand electronics",
                "+229-555-0123", "FAX-2295510", 87000.80, "info@segundamao.bj",
                address("Carré 122 Cotonou", "Cotonou", "BP-220", "SAL", "Cadjehoun"),
                OPERATING));

        companies.add(company("StreamPlus Media", SERVICO_DE_STREAMING, "komlanagbodan",
                "TIN-8484848484", "Audio and video streaming for West Africa",
                "+228-555-0124", "FAX-2285510", 2650000.00, "support@streamplus.tg",
                address("Boulevard du Mono 2", "Lomé", "BP-115", "SAL", "Bè"),
                OPERATING));

        companies.add(company("Outros Negócios Lda", OUTROS, "sofiamartins",
                "TIN-9595959595", "Holding company for assorted ventures",
                "+351-555-0125", "", 4300000.00, "geral@outrosnegocios.pt",
                address("Avenida da Liberdade 200", "Lisboa",  "1250-147", "SAL", "Avenidas Novas"),
                OPERATING));

        return companies;
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static JSONObject company(String name, String category, String creatorUsername,
                                      String taxIdNumber, String description, String phoneNumber,
                                      String faxNumber, double revenue, String email,
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
        return new JSONObject()
                .put("street", street)
                .put("city", city)
                .put("postalCode", postalCode)
                .put("island", island)
                .put("zone", zone);
    }
}