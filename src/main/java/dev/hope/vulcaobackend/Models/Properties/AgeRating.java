package dev.hope.vulcaobackend.Models.Properties;


import lombok.Getter;

@Getter
public enum AgeRating {

    // ── USA – MPAA (Film) ──────────────────────────────────────────────────────
    MPAA_G("G", "General Audiences – All ages admitted.", RatingSystem.MPAA, 0),
    MPAA_PG("PG", "Parental Guidance Suggested – Some material may not be suitable for children.", RatingSystem.MPAA, 8),
    MPAA_PG13("PG-13", "Parents Strongly Cautioned – Some material may be inappropriate for children under 13.", RatingSystem.MPAA, 13),
    MPAA_R("R", "Restricted – Under 17 requires accompanying parent or adult guardian.", RatingSystem.MPAA, 17),
    MPAA_NC17("NC-17", "Adults Only – No one 17 and under admitted.", RatingSystem.MPAA, 18),
    MPAA_NR("NR", "Not Rated.", RatingSystem.MPAA, -1),
    MPAA_UR("UR", "Unrated.", RatingSystem.MPAA, -1),

    // ── USA – TV Parental Guidelines ──────────────────────────────────────────
    TV_Y("TV-Y", "All Children.", RatingSystem.TV_PG_US, 0),
    TV_Y7("TV-Y7", "Directed to Older Children (7+).", RatingSystem.TV_PG_US, 7),
    TV_Y7_FV("TV-Y7-FV", "Directed to Older Children – Fantasy Violence.", RatingSystem.TV_PG_US, 7),
    TV_G("TV-G", "General Audience.", RatingSystem.TV_PG_US, 0),
    TV_PG("TV-PG", "Parental Guidance Suggested.", RatingSystem.TV_PG_US, 0),
    TV_14("TV-14", "Parents Strongly Cautioned (14+).", RatingSystem.TV_PG_US, 14),
    TV_MA("TV-MA", "Mature Audience Only (17+).", RatingSystem.TV_PG_US, 17),

    // ── USA – ESRB (Games) ────────────────────────────────────────────────────
    ESRB_EC("EC", "Early Childhood.", RatingSystem.ESRB, 0),
    ESRB_E("E", "Everyone.", RatingSystem.ESRB, 0),
    ESRB_E10("E10+", "Everyone 10 and older.", RatingSystem.ESRB, 10),
    ESRB_T("T", "Teen (13+).", RatingSystem.ESRB, 13),
    ESRB_M("M", "Mature (17+).", RatingSystem.ESRB, 17),
    ESRB_AO("AO", "Adults Only (18+).", RatingSystem.ESRB, 18),
    ESRB_RP("RP", "Rating Pending.", RatingSystem.ESRB, -1),

    // ── UK – BBFC ─────────────────────────────────────────────────────────────
    BBFC_U("U", "Universal – Suitable for all.", RatingSystem.BBFC, 0),
    BBFC_PG("PG", "Parental Guidance.", RatingSystem.BBFC, 0),
    BBFC_12("12", "Suitable for 12 years and over.", RatingSystem.BBFC, 12),
    BBFC_12A("12A", "Suitable for 12 years and over (cinema, accompanied under 12 allowed).", RatingSystem.BBFC, 12),
    BBFC_15("15", "Suitable only for 15 years and over.", RatingSystem.BBFC, 15),
    BBFC_18("18", "Suitable only for adults.", RatingSystem.BBFC, 18),
    BBFC_R18("R18", "Restricted to adults (licensed venues only).", RatingSystem.BBFC, 18),

    // ── EU / Pan-European – PEGI (Games) ─────────────────────────────────────
    PEGI_3("PEGI 3", "Suitable for ages 3 and over.", RatingSystem.PEGI, 3),
    PEGI_7("PEGI 7", "Suitable for ages 7 and over.", RatingSystem.PEGI, 7),
    PEGI_12("PEGI 12", "Suitable for ages 12 and over.", RatingSystem.PEGI, 12),
    PEGI_16("PEGI 16", "Suitable for ages 16 and over.", RatingSystem.PEGI, 16),
    PEGI_18("PEGI 18", "Adults only (18+).", RatingSystem.PEGI, 18),

    // ── Australia – ACB / Classification Board ────────────────────────────────
    ACB_G("G", "General – All audiences.", RatingSystem.ACB, 0),
    ACB_PG("PG", "Parental Guidance recommended.", RatingSystem.ACB, 0),
    ACB_M("M", "Mature – Recommended for 15+.", RatingSystem.ACB, 15),
    ACB_MA15("MA 15+", "Mature Accompanied – Restricted to 15+.", RatingSystem.ACB, 15),
    ACB_R18("R 18+", "Restricted – Adults only (18+).", RatingSystem.ACB, 18),
    ACB_X18("X 18+", "Restricted – Explicit sexual content (18+).", RatingSystem.ACB, 18),
    ACB_RC("RC", "Refused Classification – Banned.", RatingSystem.ACB, -1),

    // ── Canada – CRTC / Provincial ────────────────────────────────────────────
    CANADA_G("G", "General – All ages.", RatingSystem.CANADA, 0),
    CANADA_PG("PG", "Parental Guidance.", RatingSystem.CANADA, 0),
    CANADA_14A("14A", "Suitable for 14 years and over.", RatingSystem.CANADA, 14),
    CANADA_18A("18A", "Suitable for 18 years and over.", RatingSystem.CANADA, 18),
    CANADA_R("R", "Restricted – 18+.", RatingSystem.CANADA, 18),
    CANADA_A("A", "Adult – Explicit content.", RatingSystem.CANADA, 18),

    // ── Germany – FSK ─────────────────────────────────────────────────────────
    FSK_0("FSK 0", "No age restriction.", RatingSystem.FSK, 0),
    FSK_6("FSK 6", "Approved for ages 6 and over.", RatingSystem.FSK, 6),
    FSK_12("FSK 12", "Approved for ages 12 and over.", RatingSystem.FSK, 12),
    FSK_16("FSK 16", "Approved for ages 16 and over.", RatingSystem.FSK, 16),
    FSK_18("FSK 18", "No minors (18+).", RatingSystem.FSK, 18),

    // ── France – CNC ──────────────────────────────────────────────────────────
    CNC_U("U", "Tous publics – All audiences.", RatingSystem.CNC, 0),
    CNC_10("10", "Not recommended for children under 10.", RatingSystem.CNC, 10),
    CNC_12("12", "Not recommended for children under 12.", RatingSystem.CNC, 12),
    CNC_16("16", "Not recommended for children under 16.", RatingSystem.CNC, 16),
    CNC_18("18", "Prohibited for minors (18+).", RatingSystem.CNC, 18),
    CNC_X("X", "Pornographic / prohibited for minors.", RatingSystem.CNC, 18),

    // ── Japan – EIRIN / CERO ──────────────────────────────────────────────────
    EIRIN_G("G", "General – All audiences.", RatingSystem.EIRIN, 0),
    EIRIN_PG12("PG12", "Parental Guidance for under 12.", RatingSystem.EIRIN, 0),
    EIRIN_R15("R15+", "Restricted to 15 and over.", RatingSystem.EIRIN, 15),
    EIRIN_R18("R18+", "Restricted to 18 and over.", RatingSystem.EIRIN, 18),
    CERO_A("CERO A", "All ages.", RatingSystem.CERO, 0),
    CERO_B("CERO B", "Ages 12 and over.", RatingSystem.CERO, 12),
    CERO_C("CERO C", "Ages 15 and over.", RatingSystem.CERO, 15),
    CERO_D("CERO D", "Ages 17 and over.", RatingSystem.CERO, 17),
    CERO_Z("CERO Z", "Ages 18 and over.", RatingSystem.CERO, 18),

    // ── South Korea – KMRB ────────────────────────────────────────────────────
    KMRB_ALL("All", "All audiences.", RatingSystem.KMRB, 0),
    KMRB_12("12+", "Suitable for ages 12 and over.", RatingSystem.KMRB, 12),
    KMRB_15("15+", "Suitable for ages 15 and over.", RatingSystem.KMRB, 15),
    KMRB_18("18+", "Adults only.", RatingSystem.KMRB, 18),
    KMRB_RESTRICTED("Restricted", "Restricted screening only.", RatingSystem.KMRB, 18),

    // ── Brazil – CLASSIND ─────────────────────────────────────────────────────
    CLASSIND_L("L", "Livre – All ages.", RatingSystem.CLASSIND, 0),
    CLASSIND_10("10", "Not recommended for under 10.", RatingSystem.CLASSIND, 10),
    CLASSIND_12("12", "Not recommended for under 12.", RatingSystem.CLASSIND, 12),
    CLASSIND_14("14", "Not recommended for under 14.", RatingSystem.CLASSIND, 14),
    CLASSIND_16("16", "Not recommended for under 16.", RatingSystem.CLASSIND, 16),
    CLASSIND_18("18", "Not recommended for under 18.", RatingSystem.CLASSIND, 18),

    // ── India – CBFC ──────────────────────────────────────────────────────────
    CBFC_U("U", "Unrestricted Public Exhibition.", RatingSystem.CBFC, 0),
    CBFC_UA("U/A", "Unrestricted, with parental guidance for under 12.", RatingSystem.CBFC, 0),
    CBFC_A("A", "Restricted to adults (18+).", RatingSystem.CBFC, 18),
    CBFC_S("S", "Restricted to specialised audiences.", RatingSystem.CBFC, -1),

    // ── Russia – RARS ─────────────────────────────────────────────────────────
    RARS_0("0+", "No age restriction.", RatingSystem.RARS, 0),
    RARS_6("6+", "For ages 6 and over.", RatingSystem.RARS, 6),
    RARS_12("12+", "For ages 12 and over.", RatingSystem.RARS, 12),
    RARS_16("16+", "For ages 16 and over.", RatingSystem.RARS, 16),
    RARS_18("18+", "Adults only.", RatingSystem.RARS, 18),

    // ── China – CPB ───────────────────────────────────────────────────────────
    CPB_ALL("全年齢", "All audiences.", RatingSystem.CPB, 0),
    CPB_PG("PG", "Parental guidance recommended.", RatingSystem.CPB, 0),
    CPB_RESTRICTED("限制级", "Restricted.", RatingSystem.CPB, 18),

    // ── Streaming Platform Ratings ────────────────────────────────────────────
    NETFLIX_AGES_UNKNOWN("Netflix: Ages Unknown", "Not yet rated.", RatingSystem.STREAMING_PLATFORM, -1),
    AMAZON_ALL_AGES("Amazon: All Ages", "Suitable for all ages.", RatingSystem.STREAMING_PLATFORM, 0),
    DISNEY_PLUS_ALL("Disney+: General", "Family friendly content.", RatingSystem.STREAMING_PLATFORM, 0),

    // ── Generic / Universal ───────────────────────────────────────────────────
    NOT_RATED("NR", "Not Rated.", RatingSystem.GENERIC, -1),
    UNRATED("UR", "Unrated.", RatingSystem.GENERIC, -1),
    ADULTS_ONLY("AO", "Adults only (18+).", RatingSystem.GENERIC, 18),
    ALL_AGES("ALL", "Suitable for all ages.", RatingSystem.GENERIC, 0),
    UNKNOWN("Unknown", "Rating unknown.", RatingSystem.GENERIC, -1);

    private final String label;
    private final String description;
    private final RatingSystem system;
    /** Minimum recommended age; -1 means not applicable or not rated. */
    private final int minimumAge;

    AgeRating(String label, String description, RatingSystem system, int minimumAge) {
        this.label = label;
        this.description = description;
        this.system = system;
        this.minimumAge = minimumAge;
    }

    public enum RatingSystem {
        // Film
        MPAA,         // USA – Motion Picture Association
        BBFC,         // UK – British Board of Film Classification
        ACB,          // Australia – Australian Classification Board
        EIRIN,        // Japan – Film Classification and Rating Committee
        KMRB,         // South Korea – Korea Media Rating Board
        CLASSIND,     // Brazil – Classificação Indicativa
        CBFC,         // India – Central Board of Film Certification
        CNC,          // France – Centre National du Cinéma
        FSK,          // Germany – Freiwillige Selbstkontrolle der Filmwirtschaft
        CPB,          // China – Content Policy Bureau
        RARS,         // Russia – Russian Age Rating System
        CANADA,       // Canada – Provincial ratings

        // Television
        TV_PG_US,     // USA – TV Parental Guidelines

        // Games
        ESRB,         // USA – Entertainment Software Rating Board
        PEGI,         // Europe – Pan European Game Information
        CERO,         // Japan – Computer Entertainment Rating Organization

        // Platforms
        STREAMING_PLATFORM,

        // Other
        GENERIC
    }
}