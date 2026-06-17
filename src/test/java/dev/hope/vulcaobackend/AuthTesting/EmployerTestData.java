package dev.hope.vulcaobackend.AuthTesting;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Extensive set of valid employer registration payloads for tests.
 *
 * Guarantees:
 *  - username >= 8 characters, unique across all entries
 *  - password >= 8 characters (contains upper, lower and digit), unique
 *  - email is well-formed and unique
 *
 * Fields per object: username, lastName, firstName, email, password.
 */
public  class EmployerTestData {

    public EmployerTestData() {
    }

    public List<JSONObject> validEmployers() throws JSONException {
        List<JSONObject> employers = new ArrayList<>();

        // Portugal (continued)
        employers.add(employer("tiagomonteiro",      "Tiago",      "Monteiro",      "tiago.monteiro31@example.com",      "DemoPass3101"));
        employers.add(employer("ritafigueiredo",     "Rita",       "Figueiredo",    "rita.figueiredo32@example.com",     "DemoPass3202"));
        employers.add(employer("davidsilveira",      "David",      "Silveira",      "david.silveira33@example.com",      "DemoPass3303"));
        employers.add(employer("saracunha",          "Sara",       "Cunha",         "sara.cunha34@example.com",          "DemoPass3404"));
        employers.add(employer("joaopaiva",          "Joao",       "Paiva",         "joao.paiva35@example.com",          "DemoPass3505"));
        employers.add(employer("danielamatos",       "Daniela",    "Matos",         "daniela.matos36@example.com",       "DemoPass3606"));
        employers.add(employer("rodrigoneves",       "Rodrigo",    "Neves",         "rodrigo.neves37@example.com",       "DemoPass3707"));
        employers.add(employer("patriciaguerreiro",  "Patricia",   "Guerreiro",     "patricia.guerreiro38@example.com",  "DemoPass3808"));
        employers.add(employer("sergiopacheco",      "Sergio",     "Pacheco",       "sergio.pacheco39@example.com",      "DemoPass3909"));
        employers.add(employer("claudiaalves",       "Claudia",    "Alves",         "claudia.alves40@example.com",       "DemoPass4010"));

// Cape Verde (continued)
        employers.add(employer("nelsonandrade",      "Nelson",     "Andrade",       "nelson.andrade41@example.com",      "DemoPass4111"));
        employers.add(employer("lucianagomes",       "Luciana",    "Gomes",         "luciana.gomes42@example.com",       "DemoPass4212"));
        employers.add(employer("jorgefortes",        "Jorge",      "Fortes",        "jorge.fortes43@example.com",        "DemoPass4313"));
        employers.add(employer("elenabarbosa",       "Elena",      "Barbosa",       "elena.barbosa44@example.com",       "DemoPass4414"));
        employers.add(employer("adilsonpires",       "Adilson",    "Pires",         "adilson.pires45@example.com",       "DemoPass4515"));

// Guinea-Bissau
        employers.add(employer("mariocande",         "Mario",      "Cande",         "mario.cande46@example.com",         "DemoPass4616"));
        employers.add(employer("augustodjalo",       "Augusto",    "Djalo",         "augusto.djalo47@example.com",       "DemoPass4717"));
        employers.add(employer("fatimatajalo",       "Fatimata",   "Jalo",          "fatimata.jalo48@example.com",       "DemoPass4818"));
        employers.add(employer("bacarsane",          "Bacar",      "Sane",          "bacar.sane49@example.com",          "DemoPass4919"));

// Senegal
        employers.add(employer("ousmanendiaye",      "Ousmane",    "Ndiaye",        "ousmane.ndiaye50@example.com",      "DemoPass5020"));
        employers.add(employer("aissatousow",        "Aissatou",   "Sow",           "aissatou.sow51@example.com",        "DemoPass5121"));
        employers.add(employer("ibrahimaseck",       "Ibrahima",   "Seck",          "ibrahima.seck52@example.com",       "DemoPass5222"));
        employers.add(employer("khadijandour",       "Khadija",    "Ndour",         "khadija.ndour53@example.com",       "DemoPass5323"));

// Nigeria
        employers.add(employer("oluwaseyiadebayo",   "Oluwaseyi",  "Adebayo",       "oluwaseyi.adebayo54@example.com",   "DemoPass5424"));
        employers.add(employer("chinenyeeze",        "Chinenye",   "Eze",           "chinenye.eze55@example.com",        "DemoPass5525"));
        employers.add(employer("ifeanyinwosu",       "Ifeanyi",    "Nwosu",         "ifeanyi.nwosu56@example.com",       "DemoPass5626"));
        employers.add(employer("ngoziokafor",        "Ngozi",      "Okafor",        "ngozi.okafor57@example.com",        "DemoPass5727"));

// Ghana
        employers.add(employer("yawmensah",          "Yaw",        "Mensah",        "yaw.mensah58@example.com",          "DemoPass5828"));
        employers.add(employer("efuaowusu",          "Efua",       "Owusu",         "efua.owusu59@example.com",          "DemoPass5929"));
        employers.add(employer("kofiboateng",        "Kofi",       "Boateng",       "kofi.boateng60@example.com",        "DemoPass6030"));
        employers.add(employer("abenaagyeman",       "Abena",      "Agyeman",       "abena.agyeman61@example.com",       "DemoPass6131"));

// Ivory Coast
        employers.add(employer("yaokouassi",         "Yao",        "Kouassi",       "yao.kouassi62@example.com",         "DemoPass6232"));
        employers.add(employer("aminatatraore",      "Aminata",    "Traore",        "aminata.traore63@example.com",      "DemoPass6333"));
        employers.add(employer("jeanmarckone",       "JeanMarc",   "Kone",          "jeanmarc.kone64@example.com",       "DemoPass6434"));

// Mali
        employers.add(employer("moussakonate",       "Moussa",     "Konate",        "moussa.konate65@example.com",       "DemoPass6535"));
        employers.add(employer("hawadiallo",         "Hawa",       "Diallo",        "hawa.diallo66@example.com",         "DemoPass6636"));
        employers.add(employer("bakarysangare",      "Bakary",     "Sangare",       "bakary.sangare67@example.com",      "DemoPass6737"));

// Guinea
        employers.add(employer("aboubacarbah",       "Aboubacar",  "Bah",           "aboubacar.bah68@example.com",       "DemoPass6838"));
        employers.add(employer("mariatouconte",      "Mariatou",   "Conte",         "mariatou.conte69@example.com",      "DemoPass6939"));
        employers.add(employer("aliousow",           "Aliou",      "Sow",           "aliou.sow70@example.com",           "DemoPass7040"));

// Benin
        employers.add(employer("romualddossou",      "Romuald",    "Dossou",        "romuald.dossou71@example.com",      "DemoPass7141"));
        employers.add(employer("clarissehounkpe",    "Clarisse",   "Hounkpe",       "clarisse.hounkpe72@example.com",    "DemoPass7242"));
        employers.add(employer("patricksossa",       "Patrick",    "Sossa",         "patrick.sossa73@example.com",       "DemoPass7343"));

// Togo
        employers.add(employer("komlanagbodan",      "Komlan",     "Agbodan",       "komlan.agbodan74@example.com",      "DemoPass7444"));
        employers.add(employer("adjosavi",           "Adjo",       "Savi",          "adjo.savi75@example.com",           "DemoPass7545"));
        employers.add(employer("kodjotsi",           "Kodjo",      "Tsi",           "kodjo.tsi76@example.com",           "DemoPass7646"));

        // Portugal
        employers.add(employer("nataliogomes",      "Natalio",   "Gomes",     "natalio.gomes01@proton.me",       "Password123"));
        employers.add(employer("sofiamartins",      "Sofia",     "Martins",   "sofia.martins02@gmail.com",       "Secure4562"));
        employers.add(employer("miguelferreira",    "Miguel",    "Ferreira",  "miguel.ferreira03@outlook.com",   "Vulcao2026"));
        employers.add(employer("beatrizsantos",     "Beatriz",   "Santos",    "beatriz.santos04@yahoo.com",      "MyPass9090"));
        employers.add(employer("tomasoliveira",     "Tomas",     "Oliveira",  "tomas.oliveira05@icloud.com",     "Strong7788"));
        employers.add(employer("inesrodrigues",     "Ines",      "Rodrigues", "ines.rodrigues06@hotmail.com",    "Hello1234A"));
        employers.add(employer("diogocosta",        "Diogo",     "Costa",     "diogo.costa07@fastmail.com",      "Backend321"));
        employers.add(employer("marianapereira",    "Mariana",   "Pereira",   "mariana.pereira08@proton.me",     "Coffee2025"));
        employers.add(employer("andrecarvalho",     "Andre",     "Carvalho",  "andre.carvalho09@gmail.com",      "Dragon8899"));
        employers.add(employer("catarinasousa",     "Catarina",  "Sousa",     "catarina.sousa10@outlook.com",    "Sunshine12"));
        employers.add(employer("ricardoalmeida",    "Ricardo",   "Almeida",   "ricardo.almeida11@yahoo.com",     "Mountain77"));
        employers.add(employer("joanalopes",        "Joana",     "Lopes",     "joana.lopes12@icloud.com",        "River4455A"));
        employers.add(employer("pedromarques",      "Pedro",     "Marques",   "pedro.marques13@hotmail.com",     "Forest9090"));
        employers.add(employer("carolinaribeiro",   "Carolina",  "Ribeiro",   "carolina.ribeiro14@fastmail.com", "Ocean5566B"));
        employers.add(employer("goncalofernandes",  "Goncalo",   "Fernandes", "goncalo.fernandes15@proton.me",   "Thunder123"));
        employers.add(employer("matildegoncalves",  "Matilde",   "Goncalves", "matilde.goncalves16@gmail.com",   "Galaxy7788"));
        employers.add(employer("rafaelpinto",       "Rafael",    "Pinto",     "rafael.pinto17@outlook.com",      "Falcon2024"));
        employers.add(employer("leonorteixeira",    "Leonor",    "Teixeira",  "leonor.teixeira18@yahoo.com",     "Maple3344C"));
        employers.add(employer("brunomoreira",      "Bruno",     "Moreira",   "bruno.moreira19@icloud.com",      "Cedar5567D"));
        employers.add(employer("danielacorreia",    "Daniela",   "Correia",   "daniela.correia20@hotmail.com",   "Willow8890"));
        employers.add(employer("hugonunes",         "Hugo",      "Nunes",     "hugo.nunes21@fastmail.com",       "Aspen1122E"));
        employers.add(employer("carlotacardoso",    "Carlota",   "Cardoso",   "carlota.cardoso22@proton.me",     "Birch3345F"));
        employers.add(employer("franciscomendes",   "Francisco", "Mendes",    "francisco.mendes23@gmail.com",    "Spruce6678"));
        employers.add(employer("margaridacruz",     "Margarida", "Cruz",      "margarida.cruz24@outlook.com",    "Poplar9901"));
        employers.add(employer("vascocoelho",       "Vasco",     "Coelho",    "vasco.coelho25@yahoo.com",        "Juniper234"));
        employers.add(employer("madalenarocha",     "Madalena",  "Rocha",     "madalena.rocha26@icloud.com",     "Cypress567"));
        employers.add(employer("henriquedias",      "Henrique",  "Dias",      "henrique.dias27@hotmail.com",     "Sequoia890"));
        employers.add(employer("laraantunes",       "Lara",      "Antunes",   "lara.antunes28@fastmail.com",     "Magnolia12"));
        employers.add(employer("afonsovicente",     "Afonso",    "Vicente",   "afonso.vicente29@proton.me",      "Hazel4455G"));
        employers.add(employer("evabarros",         "Eva",       "Barros",    "eva.barros30@gmail.com",          "Olive6678H"));

// Cape Verde
        employers.add(employer("carlosvaz",         "Carlos",    "Vaz",       "carlos.vaz31@proton.me",          "CaboVerde1"));
        employers.add(employer("anacorreia",        "Ana",       "Correia",   "ana.correia32@gmail.com",         "Mindelo234"));
        employers.add(employer("joaosilva",         "João",      "Silva",     "joao.silva33@outlook.com",        "Praia5678"));
        employers.add(employer("marialopes",        "Maria",     "Lopes",     "maria.lopes34@yahoo.com",         "Assomada99"));
        employers.add(employer("manuelsoares",      "Manuel",    "Soares",    "manuel.soares35@icloud.com",      "SaoVicente1"));
        employers.add(employer("isabelrodrigues",   "Isabel",    "Rodrigues", "isabel.rodrigues36@hotmail.com",  "FogoIsland3"));
        employers.add(employer("antoniofernandes",  "Antonio",   "Fernandes", "antonio.fernandes37@fastmail.com","SalCapeVer"));
        employers.add(employer("paulomartins",      "Paulo",     "Martins",   "paulo.martins38@proton.me",       "BoaVista12"));
        employers.add(employer("teresasantos",      "Teresa",    "Santos",    "teresa.santos39@gmail.com",       "Tarrafal56"));
        employers.add(employer("josepires",         "José",      "Pires",     "jose.pires40@outlook.com",        "Calheta77"));

// West African Countries
// Guinea-Bissau
        employers.add(employer("baldemarpereira",   "Baldemar",  "Pereira",   "baldemar.pereira41@proton.me",    "Bissau2024"));
        employers.add(employer("filomenacosta",     "Filomena",  "Costa",     "filomena.costa42@gmail.com",      "Cacheu3344"));
        employers.add(employer("domingosdias",      "Domingos",  "Dias",      "domingos.dias43@outlook.com",     "Bolama5566"));

// Senegal
        employers.add(employer("mamadoudiagne",     "Mamadou",   "Diagne",    "mamadou.diagne44@yahoo.com",      "DakarSenegal"));
        employers.add(employer("fatoumbaye",        "Fatou",     "Mbaye",     "fatou.mbaye45@icloud.com",        "Thies7788"));
        employers.add(employer("amadoufall",        "Amadou",    "Fall",      "amadou.fall46@hotmail.com",       "StLouis9900"));

// Nigeria
        employers.add(employer("emekaokonkwo",      "Emeka",     "Okonkwo",   "emeka.okonkwo47@fastmail.com",    "LagosNigeria"));
        employers.add(employer("amaraobi",          "Amara",     "Obi",       "amara.obi48@proton.me",           "AbujaCity12"));
        employers.add(employer("chineduike",        "Chinedu",   "Ike",       "chinedu.ike49@gmail.com",         "PortHarcourt"));

// Ghana
        employers.add(employer("kwameasare",        "Kwame",     "Asare",     "kwame.asare50@outlook.com",       "AccraGhana1"));
        employers.add(employer("akosuaaddo",        "Akosua",    "Addo",      "akosua.addo51@yahoo.com",         "KumasiCity2"));
        employers.add(employer("kojoamponsah",      "Kojo",      "Amponsah",  "kojo.amponsah52@icloud.com",      "Tamale3344"));

// Ivory Coast
        employers.add(employer("koffikouame",       "Koffi",     "Kouame",    "koffi.kouame53@hotmail.com",      "AbidjanCote"));
        employers.add(employer("aminkabia",         "Amin",      "Kabia",     "amin.kabia54@fastmail.com",       "Yamoussoukro"));

// Mali
        employers.add(employer("soumailacisse",     "Soumaila",  "Cisse",     "soumaila.cisse55@proton.me",      "BamakoMali1"));
        employers.add(employer("amitoudiallo",      "Amitou",    "Diallo",    "amitou.diallo56@gmail.com",       "Timbuktu567"));

// Guinea
        employers.add(employer("mohamedcamara",     "Mohamed",   "Camara",    "mohamed.camara57@outlook.com",    "ConakryKing"));
        employers.add(employer("fatoumatax",        "Fatoumata", "X",         "fatoumata.x58@yahoo.com",         "KankanCity"));

// Benin
        employers.add(employer("melchiordossou",    "Melchior",  "Dossou",    "melchior.dossou59@icloud.com",    "Cotonou3344"));

// Togo
        employers.add(employer("esawadjo",          "Esa",       "Wadjo",     "esa.wadjo60@hotmail.com",         "LomeTogo12"));

// Burkina Faso
        employers.add(employer("salifououedraogo",  "Salifou",   "Ouedraogo", "salifou.ouedraogo61@fastmail.com","Ouagadougou"));

// Niger
        employers.add(employer("zahirouyacouba",    "Zahirou",   "Yacouba",   "zahirou.yacouba62@proton.me",     "NiameyNiger"));

// Liberia
        employers.add(employer("barnestubman",      "Barnes",    "Tubman",    "barnes.tubman63@gmail.com",       "Monrovia123"));

// Sierra Leone
        employers.add(employer("kadiatusesay",      "Kadiatu",   "Sesay",     "kadiatu.sesay64@outlook.com",     "Freetown345"));

        return employers;
    }

    private static JSONObject employer(String username, String firstName, String lastName,
                                       String email, String password) throws JSONException {
        return new JSONObject()
                .put("username", username)
                .put("lastName", lastName)
                .put("firstName", firstName)
                .put("email", email)
                .put("password", password);
    }
}