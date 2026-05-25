package dev.hope.vulcaobackend.Models.Properties;


import lombok.Getter;

@Getter
public enum OperatingSystems {

    // ── Android ───────────────────────────────────────────────────────────────
    ANDROID("Android", "Google", OSFamily.ANDROID, DeviceType.MOBILE),
    ANDROID_1_0("Android 1.0", "Google", OSFamily.ANDROID, DeviceType.MOBILE),
    ANDROID_1_5_CUPCAKE("Android 1.5 Cupcake", "Google", OSFamily.ANDROID, DeviceType.MOBILE),
    ANDROID_1_6_DONUT("Android 1.6 Donut", "Google", OSFamily.ANDROID, DeviceType.MOBILE),
    ANDROID_2_0_ECLAIR("Android 2.0 Eclair", "Google", OSFamily.ANDROID, DeviceType.MOBILE),
    ANDROID_2_2_FROYO("Android 2.2 Froyo", "Google", OSFamily.ANDROID, DeviceType.MOBILE),
    ANDROID_2_3_GINGERBREAD("Android 2.3 Gingerbread", "Google", OSFamily.ANDROID, DeviceType.MOBILE),
    ANDROID_3_0_HONEYCOMB("Android 3.0 Honeycomb", "Google", OSFamily.ANDROID, DeviceType.TABLET),
    ANDROID_4_0_ICE_CREAM_SANDWICH("Android 4.0 Ice Cream Sandwich", "Google", OSFamily.ANDROID, DeviceType.MOBILE),
    ANDROID_4_1_JELLY_BEAN("Android 4.1 Jelly Bean", "Google", OSFamily.ANDROID, DeviceType.MOBILE),
    ANDROID_4_4_KITKAT("Android 4.4 KitKat", "Google", OSFamily.ANDROID, DeviceType.MOBILE),
    ANDROID_5_0_LOLLIPOP("Android 5.0 Lollipop", "Google", OSFamily.ANDROID, DeviceType.MOBILE),
    ANDROID_6_0_MARSHMALLOW("Android 6.0 Marshmallow", "Google", OSFamily.ANDROID, DeviceType.MOBILE),
    ANDROID_7_0_NOUGAT("Android 7.0 Nougat", "Google", OSFamily.ANDROID, DeviceType.MOBILE),
    ANDROID_8_0_OREO("Android 8.0 Oreo", "Google", OSFamily.ANDROID, DeviceType.MOBILE),
    ANDROID_9_PIE("Android 9 Pie", "Google", OSFamily.ANDROID, DeviceType.MOBILE),
    ANDROID_10("Android 10", "Google", OSFamily.ANDROID, DeviceType.MOBILE),
    ANDROID_11("Android 11", "Google", OSFamily.ANDROID, DeviceType.MOBILE),
    ANDROID_12("Android 12", "Google", OSFamily.ANDROID, DeviceType.MOBILE),
    ANDROID_13("Android 13", "Google", OSFamily.ANDROID, DeviceType.MOBILE),
    ANDROID_14("Android 14", "Google", OSFamily.ANDROID, DeviceType.MOBILE),
    ANDROID_15("Android 15", "Google", OSFamily.ANDROID, DeviceType.MOBILE),

    // ── Android Forks / Skins ─────────────────────────────────────────────────
    MIUI("MIUI (Xiaomi)", "Xiaomi", OSFamily.ANDROID, DeviceType.MOBILE),
    ONE_UI("One UI (Samsung)", "Samsung", OSFamily.ANDROID, DeviceType.MOBILE),
    OXY_OS("OxygenOS (OnePlus)", "OnePlus", OSFamily.ANDROID, DeviceType.MOBILE),
    COLOR_OS("ColorOS (OPPO)", "OPPO", OSFamily.ANDROID, DeviceType.MOBILE),
    REALME_UI("Realme UI", "Realme", OSFamily.ANDROID, DeviceType.MOBILE),
    VIVO_FUNTOUCH("Funtouch OS (Vivo)", "Vivo", OSFamily.ANDROID, DeviceType.MOBILE),
    MAGIC_UI("Magic UI (Honor)", "Honor", OSFamily.ANDROID, DeviceType.MOBILE),
    EMUI("EMUI (Huawei)", "Huawei", OSFamily.ANDROID, DeviceType.MOBILE),
    FLYME_OS("Flyme OS (Meizu)", "Meizu", OSFamily.ANDROID, DeviceType.MOBILE),
    LINEAGE_OS("LineageOS", "Community", OSFamily.ANDROID, DeviceType.MOBILE),
    GRAPHENE_OS("GrapheneOS", "Community", OSFamily.ANDROID, DeviceType.MOBILE),
    CALYX_OS("CalyxOS", "Community", OSFamily.ANDROID, DeviceType.MOBILE),
    E_OS("/e/OS", "e Foundation", OSFamily.ANDROID, DeviceType.MOBILE),
    COPPERHEAD_OS("CopperheadOS", "Copperhead", OSFamily.ANDROID, DeviceType.MOBILE),
    PARANOID_ANDROID("Paranoid Android", "Community", OSFamily.ANDROID, DeviceType.MOBILE),

    // ── iOS ───────────────────────────────────────────────────────────────────
    IOS("iOS", "Apple", OSFamily.IOS, DeviceType.MOBILE),
    IOS_1("iPhone OS 1", "Apple", OSFamily.IOS, DeviceType.MOBILE),
    IOS_2("iPhone OS 2", "Apple", OSFamily.IOS, DeviceType.MOBILE),
    IOS_3("iPhone OS 3", "Apple", OSFamily.IOS, DeviceType.MOBILE),
    IOS_4("iOS 4", "Apple", OSFamily.IOS, DeviceType.MOBILE),
    IOS_5("iOS 5", "Apple", OSFamily.IOS, DeviceType.MOBILE),
    IOS_6("iOS 6", "Apple", OSFamily.IOS, DeviceType.MOBILE),
    IOS_7("iOS 7", "Apple", OSFamily.IOS, DeviceType.MOBILE),
    IOS_8("iOS 8", "Apple", OSFamily.IOS, DeviceType.MOBILE),
    IOS_9("iOS 9", "Apple", OSFamily.IOS, DeviceType.MOBILE),
    IOS_10("iOS 10", "Apple", OSFamily.IOS, DeviceType.MOBILE),
    IOS_11("iOS 11", "Apple", OSFamily.IOS, DeviceType.MOBILE),
    IOS_12("iOS 12", "Apple", OSFamily.IOS, DeviceType.MOBILE),
    IOS_13("iOS 13", "Apple", OSFamily.IOS, DeviceType.MOBILE),
    IOS_14("iOS 14", "Apple", OSFamily.IOS, DeviceType.MOBILE),
    IOS_15("iOS 15", "Apple", OSFamily.IOS, DeviceType.MOBILE),
    IOS_16("iOS 16", "Apple", OSFamily.IOS, DeviceType.MOBILE),
    IOS_17("iOS 17", "Apple", OSFamily.IOS, DeviceType.MOBILE),
    IOS_18("iOS 18", "Apple", OSFamily.IOS, DeviceType.MOBILE),

    // ── iPadOS ────────────────────────────────────────────────────────────────
    IPADOS_13("iPadOS 13", "Apple", OSFamily.IPADOS, DeviceType.TABLET),
    IPADOS_14("iPadOS 14", "Apple", OSFamily.IPADOS, DeviceType.TABLET),
    IPADOS_15("iPadOS 15", "Apple", OSFamily.IPADOS, DeviceType.TABLET),
    IPADOS_16("iPadOS 16", "Apple", OSFamily.IPADOS, DeviceType.TABLET),
    IPADOS_17("iPadOS 17", "Apple", OSFamily.IPADOS, DeviceType.TABLET),
    IPADOS_18("iPadOS 18", "Apple", OSFamily.IPADOS, DeviceType.TABLET),

    // ── macOS ─────────────────────────────────────────────────────────────────
    MACOS("macOS", "Apple", OSFamily.MACOS, DeviceType.DESKTOP_LAPTOP),
    MAC_OS_X_CHEETAH("Mac OS X 10.0 Cheetah", "Apple", OSFamily.MACOS, DeviceType.DESKTOP_LAPTOP),
    MAC_OS_X_PUMA("Mac OS X 10.1 Puma", "Apple", OSFamily.MACOS, DeviceType.DESKTOP_LAPTOP),
    MAC_OS_X_JAGUAR("Mac OS X 10.2 Jaguar", "Apple", OSFamily.MACOS, DeviceType.DESKTOP_LAPTOP),
    MAC_OS_X_PANTHER("Mac OS X 10.3 Panther", "Apple", OSFamily.MACOS, DeviceType.DESKTOP_LAPTOP),
    MAC_OS_X_TIGER("Mac OS X 10.4 Tiger", "Apple", OSFamily.MACOS, DeviceType.DESKTOP_LAPTOP),
    MAC_OS_X_LEOPARD("Mac OS X 10.5 Leopard", "Apple", OSFamily.MACOS, DeviceType.DESKTOP_LAPTOP),
    MAC_OS_X_SNOW_LEOPARD("Mac OS X 10.6 Snow Leopard", "Apple", OSFamily.MACOS, DeviceType.DESKTOP_LAPTOP),
    MAC_OS_X_LION("OS X 10.7 Lion", "Apple", OSFamily.MACOS, DeviceType.DESKTOP_LAPTOP),
    MAC_OS_X_MOUNTAIN_LION("OS X 10.8 Mountain Lion", "Apple", OSFamily.MACOS, DeviceType.DESKTOP_LAPTOP),
    MAC_OS_X_MAVERICKS("OS X 10.9 Mavericks", "Apple", OSFamily.MACOS, DeviceType.DESKTOP_LAPTOP),
    MAC_OS_X_YOSEMITE("OS X 10.10 Yosemite", "Apple", OSFamily.MACOS, DeviceType.DESKTOP_LAPTOP),
    MAC_OS_X_EL_CAPITAN("OS X 10.11 El Capitan", "Apple", OSFamily.MACOS, DeviceType.DESKTOP_LAPTOP),
    MACOS_SIERRA("macOS 10.12 Sierra", "Apple", OSFamily.MACOS, DeviceType.DESKTOP_LAPTOP),
    MACOS_HIGH_SIERRA("macOS 10.13 High Sierra", "Apple", OSFamily.MACOS, DeviceType.DESKTOP_LAPTOP),
    MACOS_MOJAVE("macOS 10.14 Mojave", "Apple", OSFamily.MACOS, DeviceType.DESKTOP_LAPTOP),
    MACOS_CATALINA("macOS 10.15 Catalina", "Apple", OSFamily.MACOS, DeviceType.DESKTOP_LAPTOP),
    MACOS_BIG_SUR("macOS 11 Big Sur", "Apple", OSFamily.MACOS, DeviceType.DESKTOP_LAPTOP),
    MACOS_MONTEREY("macOS 12 Monterey", "Apple", OSFamily.MACOS, DeviceType.DESKTOP_LAPTOP),
    MACOS_VENTURA("macOS 13 Ventura", "Apple", OSFamily.MACOS, DeviceType.DESKTOP_LAPTOP),
    MACOS_SONOMA("macOS 14 Sonoma", "Apple", OSFamily.MACOS, DeviceType.DESKTOP_LAPTOP),
    MACOS_SEQUOIA("macOS 15 Sequoia", "Apple", OSFamily.MACOS, DeviceType.DESKTOP_LAPTOP),

    // ── Windows ───────────────────────────────────────────────────────────────
    WINDOWS("Windows", "Microsoft", OSFamily.WINDOWS, DeviceType.DESKTOP_LAPTOP),
    WINDOWS_1_0("Windows 1.0", "Microsoft", OSFamily.WINDOWS, DeviceType.DESKTOP_LAPTOP),
    WINDOWS_2_0("Windows 2.0", "Microsoft", OSFamily.WINDOWS, DeviceType.DESKTOP_LAPTOP),
    WINDOWS_3_1("Windows 3.1", "Microsoft", OSFamily.WINDOWS, DeviceType.DESKTOP_LAPTOP),
    WINDOWS_95("Windows 95", "Microsoft", OSFamily.WINDOWS, DeviceType.DESKTOP_LAPTOP),
    WINDOWS_98("Windows 98", "Microsoft", OSFamily.WINDOWS, DeviceType.DESKTOP_LAPTOP),
    WINDOWS_ME("Windows ME", "Microsoft", OSFamily.WINDOWS, DeviceType.DESKTOP_LAPTOP),
    WINDOWS_NT_3_1("Windows NT 3.1", "Microsoft", OSFamily.WINDOWS, DeviceType.DESKTOP_LAPTOP),
    WINDOWS_NT_4_0("Windows NT 4.0", "Microsoft", OSFamily.WINDOWS, DeviceType.DESKTOP_LAPTOP),
    WINDOWS_2000("Windows 2000", "Microsoft", OSFamily.WINDOWS, DeviceType.DESKTOP_LAPTOP),
    WINDOWS_XP("Windows XP", "Microsoft", OSFamily.WINDOWS, DeviceType.DESKTOP_LAPTOP),
    WINDOWS_VISTA("Windows Vista", "Microsoft", OSFamily.WINDOWS, DeviceType.DESKTOP_LAPTOP),
    WINDOWS_7("Windows 7", "Microsoft", OSFamily.WINDOWS, DeviceType.DESKTOP_LAPTOP),
    WINDOWS_8("Windows 8", "Microsoft", OSFamily.WINDOWS, DeviceType.DESKTOP_LAPTOP),
    WINDOWS_8_1("Windows 8.1", "Microsoft", OSFamily.WINDOWS, DeviceType.DESKTOP_LAPTOP),
    WINDOWS_10("Windows 10", "Microsoft", OSFamily.WINDOWS, DeviceType.DESKTOP_LAPTOP),
    WINDOWS_11("Windows 11", "Microsoft", OSFamily.WINDOWS, DeviceType.DESKTOP_LAPTOP),
    WINDOWS_PHONE_7("Windows Phone 7", "Microsoft", OSFamily.WINDOWS, DeviceType.MOBILE),
    WINDOWS_PHONE_8("Windows Phone 8", "Microsoft", OSFamily.WINDOWS, DeviceType.MOBILE),
    WINDOWS_PHONE_8_1("Windows Phone 8.1", "Microsoft", OSFamily.WINDOWS, DeviceType.MOBILE),
    WINDOWS_10_MOBILE("Windows 10 Mobile", "Microsoft", OSFamily.WINDOWS, DeviceType.MOBILE),
    WINDOWS_SERVER_2003("Windows Server 2003", "Microsoft", OSFamily.WINDOWS, DeviceType.SERVER),
    WINDOWS_SERVER_2008("Windows Server 2008", "Microsoft", OSFamily.WINDOWS, DeviceType.SERVER),
    WINDOWS_SERVER_2012("Windows Server 2012", "Microsoft", OSFamily.WINDOWS, DeviceType.SERVER),
    WINDOWS_SERVER_2016("Windows Server 2016", "Microsoft", OSFamily.WINDOWS, DeviceType.SERVER),
    WINDOWS_SERVER_2019("Windows Server 2019", "Microsoft", OSFamily.WINDOWS, DeviceType.SERVER),
    WINDOWS_SERVER_2022("Windows Server 2022", "Microsoft", OSFamily.WINDOWS, DeviceType.SERVER),

    // ── Linux – General Purpose ───────────────────────────────────────────────
    LINUX("Linux (Generic)", "Community", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    UBUNTU("Ubuntu", "Canonical", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    UBUNTU_LTS("Ubuntu LTS", "Canonical", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    DEBIAN("Debian", "Community", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    FEDORA("Fedora", "Red Hat", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    ARCH_LINUX("Arch Linux", "Community", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    MANJARO("Manjaro", "Community", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    MINT("Linux Mint", "Community", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    POP_OS("Pop!_OS", "System76", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    ELEMENTARY_OS("elementary OS", "elementary", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    ZORIN_OS("Zorin OS", "Zorin Group", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    OPENSUSE("openSUSE", "SUSE", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    OPENSUSE_LEAP("openSUSE Leap", "SUSE", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    OPENSUSE_TUMBLEWEED("openSUSE Tumbleweed", "SUSE", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    CENTOS("CentOS", "Community", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    CENTOS_STREAM("CentOS Stream", "Red Hat", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    RHEL("Red Hat Enterprise Linux", "Red Hat", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    KALI_LINUX("Kali Linux", "Offensive Security", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    PARROT_OS("Parrot OS", "Parrot Security", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    TAILS("Tails", "Community", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    WHONIX("Whonix", "Community", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    QUBES_OS("Qubes OS", "Invisible Things Lab", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    GENTOO("Gentoo", "Community", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    VOID_LINUX("Void Linux", "Community", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    NIXOS("NixOS", "Community", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    SOLUS("Solus", "Community", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    MX_LINUX("MX Linux", "Community", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    GARUDA_LINUX("Garuda Linux", "Community", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    ENDEAVOUROS("EndeavourOS", "Community", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    ROCKY_LINUX("Rocky Linux", "Community", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    ALMA_LINUX("AlmaLinux", "Community", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    SLACKWARE("Slackware", "Slackware Linux", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    LUBUNTU("Lubuntu", "Community", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    XUBUNTU("Xubuntu", "Community", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    KUBUNTU("Kubuntu", "Community", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),
    UBUNTU_MATE("Ubuntu MATE", "Community", OSFamily.LINUX, DeviceType.DESKTOP_LAPTOP),

    // ── Linux – Server ────────────────────────────────────────────────────────
    UBUNTU_SERVER("Ubuntu Server", "Canonical", OSFamily.LINUX, DeviceType.SERVER),
    DEBIAN_SERVER("Debian Server", "Community", OSFamily.LINUX, DeviceType.SERVER),
    FEDORA_SERVER("Fedora Server", "Red Hat", OSFamily.LINUX, DeviceType.SERVER),
    RHEL_SERVER("RHEL Server", "Red Hat", OSFamily.LINUX, DeviceType.SERVER),
    ROCKY_LINUX_SERVER("Rocky Linux Server", "Community", OSFamily.LINUX, DeviceType.SERVER),

    // ── Linux – Mobile ────────────────────────────────────────────────────────
    UBUNTU_TOUCH("Ubuntu Touch", "UBports", OSFamily.LINUX, DeviceType.MOBILE),
    POSTMARKET_OS("postmarketOS", "Community", OSFamily.LINUX, DeviceType.MOBILE),
    MOBIAN("Mobian", "Community", OSFamily.LINUX, DeviceType.MOBILE),
    SAILFISH_OS("Sailfish OS", "Jolla", OSFamily.LINUX, DeviceType.MOBILE),
    PLASMA_MOBILE("Plasma Mobile", "KDE", OSFamily.LINUX, DeviceType.MOBILE),
    PUREOS("PureOS", "Purism", OSFamily.LINUX, DeviceType.MOBILE),

    // ── Chrome OS / Google ────────────────────────────────────────────────────
    CHROME_OS("Chrome OS", "Google", OSFamily.CHROME_OS, DeviceType.DESKTOP_LAPTOP),
    CHROMIUM_OS("Chromium OS", "Google", OSFamily.CHROME_OS, DeviceType.DESKTOP_LAPTOP),
    CHROME_OS_FLEX("ChromeOS Flex", "Google", OSFamily.CHROME_OS, DeviceType.DESKTOP_LAPTOP),
    ANDROID_X86("Android-x86", "Community", OSFamily.ANDROID, DeviceType.DESKTOP_LAPTOP),

    // ── HarmonyOS / Huawei ────────────────────────────────────────────────────
    HARMONY_OS("HarmonyOS", "Huawei", OSFamily.HARMONY_OS, DeviceType.MOBILE),
    HARMONY_OS_2("HarmonyOS 2", "Huawei", OSFamily.HARMONY_OS, DeviceType.MOBILE),
    HARMONY_OS_3("HarmonyOS 3", "Huawei", OSFamily.HARMONY_OS, DeviceType.MOBILE),
    HARMONY_OS_4("HarmonyOS 4", "Huawei", OSFamily.HARMONY_OS, DeviceType.MOBILE),
    HARMONY_OS_NEXT("HarmonyOS NEXT", "Huawei", OSFamily.HARMONY_OS, DeviceType.MOBILE),
    LITE_OS("LiteOS", "Huawei", OSFamily.HARMONY_OS, DeviceType.MOBILE),

    // ── KaiOS ─────────────────────────────────────────────────────────────────
    KAI_OS("KaiOS", "KaiOS Technologies", OSFamily.KAIOS, DeviceType.MOBILE),
    KAI_OS_2("KaiOS 2.x", "KaiOS Technologies", OSFamily.KAIOS, DeviceType.MOBILE),
    KAI_OS_3("KaiOS 3.x", "KaiOS Technologies", OSFamily.KAIOS, DeviceType.MOBILE),

    // ── BSD Family ────────────────────────────────────────────────────────────
    FREEBSD("FreeBSD", "FreeBSD Project", OSFamily.BSD, DeviceType.DESKTOP_LAPTOP),
    OPENBSD("OpenBSD", "OpenBSD Project", OSFamily.BSD, DeviceType.DESKTOP_LAPTOP),
    NETBSD("NetBSD", "NetBSD Foundation", OSFamily.BSD, DeviceType.DESKTOP_LAPTOP),
    DRAGONFLY_BSD("DragonFly BSD", "Community", OSFamily.BSD, DeviceType.DESKTOP_LAPTOP),
    GHOSTBSD("GhostBSD", "Community", OSFamily.BSD, DeviceType.DESKTOP_LAPTOP),
    TRUENAS("TrueNAS (FreeNAS)", "iXsystems", OSFamily.BSD, DeviceType.SERVER),

    // ── Unix ──────────────────────────────────────────────────────────────────
    UNIX("Unix (Generic)", "AT&T Bell Labs", OSFamily.UNIX, DeviceType.DESKTOP_LAPTOP),
    SOLARIS("Solaris", "Oracle", OSFamily.UNIX, DeviceType.DESKTOP_LAPTOP),
    OPENSOLARIS("OpenSolaris", "Sun Microsystems", OSFamily.UNIX, DeviceType.DESKTOP_LAPTOP),
    ILLUMOS("illumos", "Community", OSFamily.UNIX, DeviceType.DESKTOP_LAPTOP),
    OMNIOS("OmniOS", "OmniOS Community", OSFamily.UNIX, DeviceType.SERVER),
    HP_UX("HP-UX", "HP", OSFamily.UNIX, DeviceType.SERVER),
    IBM_AIX("IBM AIX", "IBM", OSFamily.UNIX, DeviceType.SERVER),
    IRIX("IRIX", "SGI", OSFamily.UNIX, DeviceType.DESKTOP_LAPTOP),

    // ── Legacy / Historical ───────────────────────────────────────────────────
    MS_DOS("MS-DOS", "Microsoft", OSFamily.DOS, DeviceType.DESKTOP_LAPTOP),
    PC_DOS("PC DOS", "IBM", OSFamily.DOS, DeviceType.DESKTOP_LAPTOP),
    FREEDOS("FreeDOS", "Community", OSFamily.DOS, DeviceType.DESKTOP_LAPTOP),
    OS2("OS/2", "IBM", OSFamily.OTHER, DeviceType.DESKTOP_LAPTOP),
    BEOS("BeOS", "Be Inc.", OSFamily.OTHER, DeviceType.DESKTOP_LAPTOP),
    HAIKU("Haiku", "Community", OSFamily.OTHER, DeviceType.DESKTOP_LAPTOP),
    AMIGA_OS("AmigaOS", "Amiga", OSFamily.OTHER, DeviceType.DESKTOP_LAPTOP),
    RISC_OS("RISC OS", "RISC OS Open", OSFamily.OTHER, DeviceType.DESKTOP_LAPTOP),
    SYMBIAN("Symbian", "Nokia / Symbian Foundation", OSFamily.SYMBIAN, DeviceType.MOBILE),
    SYMBIAN_S60("Symbian S60", "Nokia", OSFamily.SYMBIAN, DeviceType.MOBILE),
    SYMBIAN_S40("Symbian S40", "Nokia", OSFamily.SYMBIAN, DeviceType.MOBILE),
    BLACKBERRY_OS("BlackBerry OS", "BlackBerry", OSFamily.BLACKBERRY, DeviceType.MOBILE),
    BLACKBERRY_10("BlackBerry 10", "BlackBerry", OSFamily.BLACKBERRY, DeviceType.MOBILE),
    PALM_OS("Palm OS", "Palm", OSFamily.OTHER, DeviceType.MOBILE),
    WEBOS("webOS", "Palm / LG", OSFamily.OTHER, DeviceType.MOBILE),
    BADA("Bada", "Samsung", OSFamily.OTHER, DeviceType.MOBILE),
    TIZEN("Tizen", "Samsung / Linux Foundation", OSFamily.OTHER, DeviceType.MOBILE),
    WINDOWS_CE("Windows CE", "Microsoft", OSFamily.WINDOWS, DeviceType.MOBILE),
    WINDOWS_MOBILE("Windows Mobile", "Microsoft", OSFamily.WINDOWS, DeviceType.MOBILE),
    FIREFOX_OS("Firefox OS", "Mozilla", OSFamily.OTHER, DeviceType.MOBILE),
    MEEGO("MeeGo", "Nokia / Intel", OSFamily.LINUX, DeviceType.MOBILE),
    MAEMO("Maemo", "Nokia", OSFamily.LINUX, DeviceType.MOBILE),

    // ── Other / Embedded ──────────────────────────────────────────────────────
    FUCHSIA("Fuchsia OS", "Google", OSFamily.OTHER, DeviceType.MOBILE),
    WEAROS("Wear OS", "Google", OSFamily.ANDROID, DeviceType.WEARABLE),
    WATCHOS("watchOS", "Apple", OSFamily.IOS, DeviceType.WEARABLE),
    TVOS("tvOS", "Apple", OSFamily.IOS, DeviceType.TV),
    ANDROID_TV("Android TV", "Google", OSFamily.ANDROID, DeviceType.TV),
    GOOGLE_TV("Google TV", "Google", OSFamily.ANDROID, DeviceType.TV),
    FIRE_OS("Fire OS", "Amazon", OSFamily.ANDROID, DeviceType.TABLET),
    VISIONOS("visionOS", "Apple", OSFamily.IOS, DeviceType.OTHER),

    UNKNOWN("Unknown", "Unknown", OSFamily.OTHER, DeviceType.OTHER);

    private final String displayName;
    private final String developer;
    private final OSFamily family;
    private final DeviceType deviceType;

    OperatingSystems(String displayName, String developer, OSFamily family, DeviceType deviceType) {
        this.displayName = displayName;
        this.developer = developer;
        this.family = family;
        this.deviceType = deviceType;
    }

    public String getDisplayName() { return displayName; }
    public String getDeveloper() { return developer; }
    public OSFamily getFamily() { return family; }
    public DeviceType getDeviceType() { return deviceType; }

    // ── Nested Enums ──────────────────────────────────────────────────────────

    public enum OSFamily {
        ANDROID,
        IOS,
        IPADOS,
        MACOS,
        WINDOWS,
        LINUX,
        CHROME_OS,
        HARMONY_OS,
        KAIOS,
        BSD,
        UNIX,
        DOS,
        SYMBIAN,
        BLACKBERRY,
        OTHER
    }

    public enum DeviceType {
        MOBILE,
        TABLET,
        DESKTOP_LAPTOP,
        SERVER,
        WEARABLE,
        TV,
        OTHER
    }
}