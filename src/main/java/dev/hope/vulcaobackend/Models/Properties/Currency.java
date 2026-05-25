package dev.hope.vulcaobackend.Models.Properties;

import lombok.Getter;

@Getter
public enum Currency {
    // A
    AED("United Arab Emirates Dirham"),
    AFN("Afghan Afghani"),
    ALL("Albanian Lek"),
    AMD("Armenian Dram"),
    ANG("Netherlands Antillean Guilder"),
    AOA("Angolan Kwanza"),
    ARS("Argentine Peso"),
    AUD("Australian Dollar"),
    AWG("Aruban Florin"),
    AZN("Azerbaijani Manat"),

    // B
    BAM("Bosnia-Herzegovina Convertible Mark"),
    BBD("Barbadian Dollar"),
    BDT("Bangladeshi Taka"),
    BGN("Bulgarian Lev"),
    BHD("Bahraini Dinar"),
    BIF("Burundian Franc"),
    BMD("Bermudian Dollar"),
    BND("Brunei Dollar"),
    BOB("Bolivian Boliviano"),
    BRL("Brazilian Real"),
    BSD("Bahamian Dollar"),
    BTN("Bhutanese Ngultrum"),
    BWP("Botswanan Pula"),
    BYN("Belarusian Ruble"),
    BZD("Belize Dollar"),

    // C
    CAD("Canadian Dollar"),
    CDF("Congolese Franc"),
    CHF("Swiss Franc"),
    CLP("Chilean Peso"),
    CNY("Chinese Yuan"),
    COP("Colombian Peso"),
    CRC("Costa Rican Colón"),
    CUP("Cuban Peso"),
    CVE("Cape Verdean Escudo"),
    CZK("Czech Koruna"),

    // D
    DJF("Djiboutian Franc"),
    DKK("Danish Krone"),
    DOP("Dominican Peso"),
    DZD("Algerian Dinar"),

    // E
    EGP("Egyptian Pound"),
    ERN("Eritrean Nakfa"),
    ETB("Ethiopian Birr"),
    EUR("Euro"),

    // F
    FJD("Fijian Dollar"),
    FKP("Falkland Islands Pound"),

    // G
    GBP("British Pound Sterling"),
    GEL("Georgian Lari"),
    GHS("Ghanaian Cedi"),
    GIP("Gibraltar Pound"),
    GMD("Gambian Dalasi"),
    GNF("Guinean Franc"),
    GTQ("Guatemalan Quetzal"),
    GYD("Guyanese Dollar"),

    // H
    HKD("Hong Kong Dollar"),
    HNL("Honduran Lempira"),
    HRK("Croatian Kuna"),
    HTG("Haitian Gourde"),
    HUF("Hungarian Forint"),

    // I
    IDR("Indonesian Rupiah"),
    ILS("Israeli New Shekel"),
    INR("Indian Rupee"),
    IQD("Iraqi Dinar"),
    IRR("Iranian Rial"),
    ISK("Icelandic Króna"),

    // J
    JMD("Jamaican Dollar"),
    JOD("Jordanian Dinar"),
    JPY("Japanese Yen"),

    // K
    KES("Kenyan Shilling"),
    KGS("Kyrgyzstani Som"),
    KHR("Cambodian Riel"),
    KMF("Comorian Franc"),
    KPW("North Korean Won"),
    KRW("South Korean Won"),
    KWD("Kuwaiti Dinar"),
    KYD("Cayman Islands Dollar"),
    KZT("Kazakhstani Tenge"),

    // L
    LAK("Laotian Kip"),
    LBP("Lebanese Pound"),
    LKR("Sri Lankan Rupee"),
    LRD("Liberian Dollar"),
    LSL("Lesotho Loti"),
    LYD("Libyan Dinar"),

    // M
    MAD("Moroccan Dirham"),
    MDL("Moldovan Leu"),
    MGA("Malagasy Ariary"),
    MKD("Macedonian Denar"),
    MMK("Myanmar Kyat"),
    MNT("Mongolian Tögrög"),
    MOP("Macanese Pataca"),
    MRU("Mauritanian Ouguiya"),
    MUR("Mauritian Rupee"),
    MVR("Maldivian Rufiyaa"),
    MWK("Malawian Kwacha"),
    MXN("Mexican Peso"),
    MYR("Malaysian Ringgit"),
    MZN("Mozambican Metical"),

    // N
    NAD("Namibian Dollar"),
    NGN("Nigerian Naira"),
    NIO("Nicaraguan Córdoba"),
    NOK("Norwegian Krone"),
    NPR("Nepalese Rupee"),
    NZD("New Zealand Dollar"),

    // O
    OMR("Omani Rial"),

    // P
    PAB("Panamanian Balboa"),
    PEN("Peruvian Sol"),
    PGK("Papua New Guinean Kina"),
    PHP("Philippine Peso"),
    PKR("Pakistani Rupee"),
    PLN("Polish Złoty"),
    PYG("Paraguayan Guaraní"),

    // Q
    QAR("Qatari Riyal"),

    // R
    RON("Romanian Leu"),
    RSD("Serbian Dinar"),
    RUB("Russian Ruble"),
    RWF("Rwandan Franc"),

    // S
    SAR("Saudi Riyal"),
    SBD("Solomon Islands Dollar"),
    SCR("Seychellois Rupee"),
    SDG("Sudanese Pound"),
    SEK("Swedish Krona"),
    SGD("Singapore Dollar"),
    SHP("Saint Helena Pound"),
    SLL("Sierra Leonean Leone"),
    SOS("Somali Shilling"),
    SRD("Surinamese Dollar"),
    STN("São Tomé and Príncipe Dobra"),
    SYP("Syrian Pound"),
    SZL("Swazi Lilangeni"),

    // T
    THB("Thai Baht"),
    TJS("Tajikistani Somoni"),
    TMT("Turkmenistani Manat"),
    TND("Tunisian Dinar"),
    TOP("Tongan Paʻanga"),
    TRY("Turkish Lira"),
    TTD("Trinidad and Tobago Dollar"),
    TWD("New Taiwan Dollar"),
    TZS("Tanzanian Shilling"),

    // U
    UAH("Ukrainian Hryvnia"),
    UGX("Ugandan Shilling"),
    USD("United States Dollar"),
    UYU("Uruguayan Peso"),
    UZS("Uzbekistani Som"),

    // V
    VES("Venezuelan Bolívar"),
    VND("Vietnamese Đồng"),
    VUV("Vanuatu Vatu"),

    // W
    WST("Samoan Tala"),

    // X
    XAF("Central African CFA Franc"),
    XCD("East Caribbean Dollar"),
    XOF("West African CFA Franc"),
    XPF("CFP Franc"),

    // Y
    YER("Yemeni Rial"),

    // Z
    ZAR("South African Rand"),
    ZMW("Zambian Kwacha"),
    ZWL("Zimbabwean Dollar"),

    // Cryptocurrencies
    BTC("Bitcoin"),
    ETH("Ethereum"),
    USDT("Tether"),
    USDC("USD Coin"),
    BNB("Binance Coin"),
    XRP("Ripple"),
    ADA("Cardano"),
    SOL("Solana"),
    DOT("Polkadot"),
    DOGE("Dogecoin"),
    LTC("Litecoin"),
    MATIC("Polygon");

    private final String description;

    Currency(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
