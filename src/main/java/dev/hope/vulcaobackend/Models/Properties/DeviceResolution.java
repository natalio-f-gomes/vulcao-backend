package dev.hope.vulcaobackend.Models.Properties;


import lombok.Getter;

@Getter
public enum DeviceResolution {

    // ── Mobile – Low End ──────────────────────────────────────────────────────
    RES_240x320("240x320", 240, 320, "QVGA", ResolutionTier.LOW, DeviceClass.MOBILE),
    RES_240x400("240x400", 240, 400, "WQVGA", ResolutionTier.LOW, DeviceClass.MOBILE),
    RES_320x480("320x480", 320, 480, "HVGA", ResolutionTier.LOW, DeviceClass.MOBILE),
    RES_360x640("360x640", 360, 640, "nHD", ResolutionTier.LOW, DeviceClass.MOBILE),
    RES_480x800("480x800", 480, 800, "WVGA", ResolutionTier.LOW, DeviceClass.MOBILE),
    RES_480x854("480x854", 480, 854, "FWVGA", ResolutionTier.LOW, DeviceClass.MOBILE),
    RES_540x960("540x960", 540, 960, "qHD", ResolutionTier.LOW, DeviceClass.MOBILE),

    // ── Mobile – HD ───────────────────────────────────────────────────────────
    RES_720x1280("720x1280", 720, 1280, "HD", ResolutionTier.HD, DeviceClass.MOBILE),
    RES_720x1520("720x1520", 720, 1520, "HD+", ResolutionTier.HD, DeviceClass.MOBILE),
    RES_720x1560("720x1560", 720, 1560, "HD+", ResolutionTier.HD, DeviceClass.MOBILE),
    RES_720x1600("720x1600", 720, 1600, "HD+", ResolutionTier.HD, DeviceClass.MOBILE),
    RES_720x1640("720x1640", 720, 1640, "HD+", ResolutionTier.HD, DeviceClass.MOBILE),

    // ── Mobile – Full HD ──────────────────────────────────────────────────────
    RES_1080x1920("1080x1920", 1080, 1920, "FHD", ResolutionTier.FHD, DeviceClass.MOBILE),
    RES_1080x2160("1080x2160", 1080, 2160, "FHD+", ResolutionTier.FHD, DeviceClass.MOBILE),
    RES_1080x2220("1080x2220", 1080, 2220, "FHD+", ResolutionTier.FHD, DeviceClass.MOBILE),
    RES_1080x2244("1080x2244", 1080, 2244, "FHD+", ResolutionTier.FHD, DeviceClass.MOBILE),
    RES_1080x2340("1080x2340", 1080, 2340, "FHD+", ResolutionTier.FHD, DeviceClass.MOBILE),
    RES_1080x2400("1080x2400", 1080, 2400, "FHD+", ResolutionTier.FHD, DeviceClass.MOBILE),
    RES_1080x2408("1080x2408", 1080, 2408, "FHD+", ResolutionTier.FHD, DeviceClass.MOBILE),
    RES_1080x2412("1080x2412", 1080, 2412, "FHD+", ResolutionTier.FHD, DeviceClass.MOBILE),
    RES_1080x2460("1080x2460", 1080, 2460, "FHD+", ResolutionTier.FHD, DeviceClass.MOBILE),

    // ── Mobile – QHD / 2K ─────────────────────────────────────────────────────
    RES_1440x2560("1440x2560", 1440, 2560, "QHD", ResolutionTier.QHD, DeviceClass.MOBILE),
    RES_1440x2960("1440x2960", 1440, 2960, "QHD+", ResolutionTier.QHD, DeviceClass.MOBILE),
    RES_1440x3040("1440x3040", 1440, 3040, "QHD+", ResolutionTier.QHD, DeviceClass.MOBILE),
    RES_1440x3088("1440x3088", 1440, 3088, "QHD+", ResolutionTier.QHD, DeviceClass.MOBILE),
    RES_1440x3120("1440x3120", 1440, 3120, "QHD+", ResolutionTier.QHD, DeviceClass.MOBILE),
    RES_1440x3168("1440x3168", 1440, 3168, "QHD+", ResolutionTier.QHD, DeviceClass.MOBILE),
    RES_1440x3200("1440x3200", 1440, 3200, "QHD+", ResolutionTier.QHD, DeviceClass.MOBILE),
    RES_1344x2992("1344x2992", 1344, 2992, "QHD+", ResolutionTier.QHD, DeviceClass.MOBILE),

    // ── Mobile – 4K ───────────────────────────────────────────────────────────
    RES_2160x3840("2160x3840", 2160, 3840, "4K UHD", ResolutionTier.UHD_4K, DeviceClass.MOBILE),

    // ── Foldable / Large Phone ────────────────────────────────────────────────
    RES_1812x2176("1812x2176", 1812, 2176, "Foldable Inner", ResolutionTier.QHD, DeviceClass.FOLDABLE),
    RES_1768x2208("1768x2208", 1768, 2208, "Foldable Inner", ResolutionTier.QHD, DeviceClass.FOLDABLE),
    RES_2208x1768("2208x1768", 2208, 1768, "Foldable Landscape", ResolutionTier.QHD, DeviceClass.FOLDABLE),
    RES_2176x1812("2176x1812", 2176, 1812, "Foldable Landscape", ResolutionTier.QHD, DeviceClass.FOLDABLE),
    RES_2268x1840("2268x1840", 2268, 1840, "Foldable Inner", ResolutionTier.QHD, DeviceClass.FOLDABLE),
    RES_2152x2076("2152x2076", 2152, 2076, "Foldable Square", ResolutionTier.QHD, DeviceClass.FOLDABLE),
    RES_904x2092("904x2092", 904, 2092, "Foldable Outer", ResolutionTier.FHD, DeviceClass.FOLDABLE),
    RES_720x1978("720x1978", 720, 1978, "Foldable Outer", ResolutionTier.HD, DeviceClass.FOLDABLE),

    // ── Tablet ────────────────────────────────────────────────────────────────
    RES_800x1280("800x1280", 800, 1280, "WXGA", ResolutionTier.HD, DeviceClass.TABLET),
    RES_1200x1920("1200x1920", 1200, 1920, "WUXGA", ResolutionTier.FHD, DeviceClass.TABLET),
    RES_1600x2560("1600x2560", 1600, 2560, "WQXGA", ResolutionTier.QHD, DeviceClass.TABLET),
    RES_2048x1536("2048x1536", 2048, 1536, "Retina (iPad)", ResolutionTier.QHD, DeviceClass.TABLET),
    RES_2048x2732("2048x2732", 2048, 2732, "iPad Pro 12.9\"", ResolutionTier.QHD, DeviceClass.TABLET),
    RES_1668x2388("1668x2388", 1668, 2388, "iPad Pro 11\"", ResolutionTier.QHD, DeviceClass.TABLET),
    RES_1640x2360("1640x2360", 1640, 2360, "iPad Air", ResolutionTier.QHD, DeviceClass.TABLET),
    RES_1620x2160("1620x2160", 1620, 2160, "iPad 10th Gen", ResolutionTier.QHD, DeviceClass.TABLET),
    RES_1536x2048("1536x2048", 1536, 2048, "iPad Retina", ResolutionTier.QHD, DeviceClass.TABLET),
    RES_1488x2266("1488x2266", 1488, 2266, "iPad mini", ResolutionTier.QHD, DeviceClass.TABLET),
    RES_2560x1600("2560x1600", 2560, 1600, "Tablet WQXGA", ResolutionTier.QHD, DeviceClass.TABLET),
    RES_2800x1752("2800x1752", 2800, 1752, "Surface Pro 9", ResolutionTier.QHD, DeviceClass.TABLET),
    RES_2880x1920("2880x1920", 2880, 1920, "Surface Pro 10", ResolutionTier.QHD, DeviceClass.TABLET),

    // ── Laptop – Low / Mid ────────────────────────────────────────────────────
    RES_1024x600("1024x600", 1024, 600, "WSVGA", ResolutionTier.LOW, DeviceClass.LAPTOP),
    RES_1024x768("1024x768", 1024, 768, "XGA", ResolutionTier.LOW, DeviceClass.LAPTOP),
    RES_1280x720("1280x720", 1280, 720, "HD", ResolutionTier.HD, DeviceClass.LAPTOP),
    RES_1280x800("1280x800", 1280, 800, "WXGA", ResolutionTier.HD, DeviceClass.LAPTOP),
    RES_1280x1024("1280x1024", 1280, 1024, "SXGA", ResolutionTier.HD, DeviceClass.LAPTOP),
    RES_1366x768("1366x768", 1366, 768, "HD", ResolutionTier.HD, DeviceClass.LAPTOP),
    RES_1440x900("1440x900", 1440, 900, "WXGA+", ResolutionTier.HD, DeviceClass.LAPTOP),
    RES_1600x900("1600x900", 1600, 900, "HD+", ResolutionTier.HD, DeviceClass.LAPTOP),

    // ── Laptop – Full HD ──────────────────────────────────────────────────────
    RES_1920x1080("1920x1080", 1920, 1080, "FHD 1080p", ResolutionTier.FHD, DeviceClass.LAPTOP),
    RES_1920x1200("1920x1200", 1920, 1200, "WUXGA", ResolutionTier.FHD, DeviceClass.LAPTOP),

    // ── Laptop – 2K / QHD ─────────────────────────────────────────────────────
    RES_2048x1152("2048x1152", 2048, 1152, "QWXGA", ResolutionTier.QHD, DeviceClass.LAPTOP),
    RES_2160x1350("2160x1350", 2160, 1350, "QHD+", ResolutionTier.QHD, DeviceClass.LAPTOP),
    RES_2240x1400("2240x1400", 2240, 1400, "2.5K WQXGA+", ResolutionTier.QHD, DeviceClass.LAPTOP),
    RES_2256x1504("2256x1504", 2256, 1504, "Surface Laptop", ResolutionTier.QHD, DeviceClass.LAPTOP),
    RES_2304x1440("2304x1440", 2304, 1440, "2.5K", ResolutionTier.QHD, DeviceClass.LAPTOP),
    RES_2560x1440("2560x1440", 2560, 1440, "QHD / 2K", ResolutionTier.QHD, DeviceClass.LAPTOP),
    RES_2560x1664("2560x1664", 2560, 1664, "MacBook Air M2", ResolutionTier.QHD, DeviceClass.LAPTOP),
    RES_2880x1800("2880x1800", 2880, 1800, "Retina / QWXGA+", ResolutionTier.QHD, DeviceClass.LAPTOP),
    RES_2880x1864("2880x1864", 2880, 1864, "MacBook Pro 14\"", ResolutionTier.QHD, DeviceClass.LAPTOP),
    RES_2952x1848("2952x1848", 2952, 1848, "MacBook Pro 16\"", ResolutionTier.QHD, DeviceClass.LAPTOP),
    RES_3000x2000("3000x2000", 3000, 2000, "Surface Laptop Studio", ResolutionTier.QHD, DeviceClass.LAPTOP),
    RES_3024x1890("3024x1890", 3024, 1890, "MacBook Pro M3", ResolutionTier.QHD, DeviceClass.LAPTOP),
    RES_3456x2160("3456x2160", 3456, 2160, "MacBook Pro 16\" M3", ResolutionTier.UHD_4K, DeviceClass.LAPTOP),

    // ── Laptop – 4K ───────────────────────────────────────────────────────────
    RES_3840x2160_LAPTOP("3840x2160", 3840, 2160, "4K UHD Laptop", ResolutionTier.UHD_4K, DeviceClass.LAPTOP),
    RES_3840x2400("3840x2400", 3840, 2400, "4K+ WQUXGA", ResolutionTier.UHD_4K, DeviceClass.LAPTOP),

    // ── Desktop Monitor – SD / HD ─────────────────────────────────────────────
    RES_640x480("640x480", 640, 480, "VGA", ResolutionTier.LOW, DeviceClass.DESKTOP),
    RES_800x600("800x600", 800, 600, "SVGA", ResolutionTier.LOW, DeviceClass.DESKTOP),
    RES_1024x768_DESKTOP("1024x768", 1024, 768, "XGA", ResolutionTier.LOW, DeviceClass.DESKTOP),
    RES_1280x1024_DESKTOP("1280x1024", 1280, 1024, "SXGA", ResolutionTier.HD, DeviceClass.DESKTOP),
    RES_1600x1200("1600x1200", 1600, 1200, "UXGA", ResolutionTier.HD, DeviceClass.DESKTOP),
    RES_1680x1050("1680x1050", 1680, 1050, "WSXGA+", ResolutionTier.HD, DeviceClass.DESKTOP),
    RES_1920x1080_DESKTOP("1920x1080", 1920, 1080, "FHD 1080p", ResolutionTier.FHD, DeviceClass.DESKTOP),
    RES_1920x1200_DESKTOP("1920x1200", 1920, 1200, "WUXGA", ResolutionTier.FHD, DeviceClass.DESKTOP),
    RES_2560x1080("2560x1080", 2560, 1080, "UW-FHD", ResolutionTier.FHD, DeviceClass.DESKTOP),

    // ── Desktop Monitor – 2K / QHD ────────────────────────────────────────────
    RES_2560x1440_DESKTOP("2560x1440", 2560, 1440, "QHD / 2K", ResolutionTier.QHD, DeviceClass.DESKTOP),
    RES_3440x1440("3440x1440", 3440, 1440, "UW-QHD", ResolutionTier.QHD, DeviceClass.DESKTOP),
    RES_3840x1080("3840x1080", 3840, 1080, "Dual FHD / UW-FHD", ResolutionTier.FHD, DeviceClass.DESKTOP),
    RES_3840x1600("3840x1600", 3840, 1600, "UW-QHD+", ResolutionTier.QHD, DeviceClass.DESKTOP),

    // ── Desktop Monitor – 4K ──────────────────────────────────────────────────
    RES_3840x2160_DESKTOP("3840x2160", 3840, 2160, "4K UHD", ResolutionTier.UHD_4K, DeviceClass.DESKTOP),
    RES_4096x2160("4096x2160", 4096, 2160, "DCI 4K", ResolutionTier.UHD_4K, DeviceClass.DESKTOP),
    RES_5120x1440("5120x1440", 5120, 1440, "UW-5K (Super Wide)", ResolutionTier.UHD_4K, DeviceClass.DESKTOP),
    RES_5120x2160("5120x2160", 5120, 2160, "UW-5K", ResolutionTier.UHD_5K, DeviceClass.DESKTOP),

    // ── Desktop Monitor – 5K ──────────────────────────────────────────────────
    RES_5120x2880("5120x2880", 5120, 2880, "5K", ResolutionTier.UHD_5K, DeviceClass.DESKTOP),
    RES_5120x2880_APPLE("5120x2880 (Studio Display)", 5120, 2880, "Apple 5K Studio Display", ResolutionTier.UHD_5K, DeviceClass.DESKTOP),
    RES_5K_IMAC("5120x2880 (iMac)", 5120, 2880, "iMac 5K Retina", ResolutionTier.UHD_5K, DeviceClass.DESKTOP),

    // ── Desktop Monitor – 6K / 8K ─────────────────────────────────────────────
    RES_6016x3384("6016x3384", 6016, 3384, "6K (Pro Display XDR)", ResolutionTier.UHD_6K, DeviceClass.DESKTOP),
    RES_7680x4320("7680x4320", 7680, 4320, "8K UHD", ResolutionTier.UHD_8K, DeviceClass.DESKTOP),
    RES_10240x4320("10240x4320", 10240, 4320, "10K (8K wide)", ResolutionTier.UHD_8K, DeviceClass.DESKTOP),

    // ── TV / Home Theater ─────────────────────────────────────────────────────
    RES_720x576("720x576", 720, 576, "SD PAL", ResolutionTier.LOW, DeviceClass.TV),
    RES_720x480("720x480", 720, 480, "SD NTSC", ResolutionTier.LOW, DeviceClass.TV),
    RES_1280x720_TV("1280x720", 1280, 720, "HD 720p", ResolutionTier.HD, DeviceClass.TV),
    RES_1920x1080_TV("1920x1080", 1920, 1080, "Full HD 1080p", ResolutionTier.FHD, DeviceClass.TV),
    RES_3840x2160_TV("3840x2160", 3840, 2160, "4K UHD TV", ResolutionTier.UHD_4K, DeviceClass.TV),
    RES_4096x2160_TV("4096x2160", 4096, 2160, "DCI 4K TV", ResolutionTier.UHD_4K, DeviceClass.TV),
    RES_7680x4320_TV("7680x4320", 7680, 4320, "8K UHD TV", ResolutionTier.UHD_8K, DeviceClass.TV),

    // ── Smartwatch ────────────────────────────────────────────────────────────
    RES_272x340("272x340", 272, 340, "Apple Watch 40mm", ResolutionTier.LOW, DeviceClass.WEARABLE),
    RES_312x390("312x390", 312, 390, "Apple Watch 44mm", ResolutionTier.LOW, DeviceClass.WEARABLE),
    RES_352x430("352x430", 352, 430, "Apple Watch Ultra 49mm", ResolutionTier.LOW, DeviceClass.WEARABLE),
    RES_396x484("396x484", 396, 484, "Apple Watch Series 9", ResolutionTier.LOW, DeviceClass.WEARABLE),
    RES_320x360("320x360", 320, 360, "Samsung Galaxy Watch", ResolutionTier.LOW, DeviceClass.WEARABLE),
    RES_450x450("450x450", 450, 450, "Wear OS Round", ResolutionTier.LOW, DeviceClass.WEARABLE),
    RES_454x454("454x454", 454, 454, "Galaxy Watch 6", ResolutionTier.LOW, DeviceClass.WEARABLE),
    RES_466x466("466x466", 466, 466, "Pixel Watch", ResolutionTier.LOW, DeviceClass.WEARABLE),

    // ── VR / XR Headsets ──────────────────────────────────────────────────────
    RES_1832x1920_PER_EYE("1832x1920 (per eye)", 1832, 1920, "Meta Quest 2", ResolutionTier.QHD, DeviceClass.VR),
    RES_2064x2208_PER_EYE("2064x2208 (per eye)", 2064, 2208, "Meta Quest 3", ResolutionTier.QHD, DeviceClass.VR),
    RES_1440x1600_PER_EYE("1440x1600 (per eye)", 1440, 1600, "Valve Index", ResolutionTier.QHD, DeviceClass.VR),
    RES_2000x2040_PER_EYE("2000x2040 (per eye)", 2000, 2040, "HTC Vive Pro 2", ResolutionTier.QHD, DeviceClass.VR),
    RES_1920x1080_PER_EYE("1920x1080 (per eye)", 1920, 1080, "HTC Vive", ResolutionTier.FHD, DeviceClass.VR),
    RES_3660x3200_PER_EYE("3660x3200 (per eye)", 3660, 3200, "Apple Vision Pro", ResolutionTier.UHD_4K, DeviceClass.VR),
    RES_2880x2720_PER_EYE("2880x2720 (per eye)", 2880, 2720, "PlayStation VR2", ResolutionTier.QHD, DeviceClass.VR),

    // ── Game Consoles ─────────────────────────────────────────────────────────
    RES_1280x720_CONSOLE("1280x720", 1280, 720, "HD 720p (Console)", ResolutionTier.HD, DeviceClass.CONSOLE),
    RES_1920x1080_CONSOLE("1920x1080", 1920, 1080, "FHD 1080p (Console)", ResolutionTier.FHD, DeviceClass.CONSOLE),
    RES_2560x1440_CONSOLE("2560x1440", 2560, 1440, "QHD (Console)", ResolutionTier.QHD, DeviceClass.CONSOLE),
    RES_3840x2160_CONSOLE("3840x2160", 3840, 2160, "4K UHD (Console)", ResolutionTier.UHD_4K, DeviceClass.CONSOLE),
    RES_7680x4320_CONSOLE("7680x4320", 7680, 4320, "8K (Xbox Series X)", ResolutionTier.UHD_8K, DeviceClass.CONSOLE),

    UNKNOWN("Unknown", 0, 0, "Unknown", ResolutionTier.UNKNOWN, DeviceClass.UNKNOWN);

    // ── Fields ────────────────────────────────────────────────────────────────
    private final String displayName;
    private final int width;
    private final int height;
    private final String marketingName;
    private final ResolutionTier tier;
    private final DeviceClass deviceClass;

    DeviceResolution(String displayName, int width, int height, String marketingName,
                     ResolutionTier tier, DeviceClass deviceClass) {
        this.displayName = displayName;
        this.width = width;
        this.height = height;
        this.marketingName = marketingName;
        this.tier = tier;
        this.deviceClass = deviceClass;
    }

    public String getDisplayName()  { return displayName;  }
    public int getWidth()           { return width;         }
    public int getHeight()          { return height;        }
    public String getMarketingName(){ return marketingName; }
    public ResolutionTier getTier() { return tier;          }
    public DeviceClass getDeviceClass() { return deviceClass; }

    /** Total pixel count. */
    public long getTotalPixels() { return (long) width * height; }

    /** Aspect ratio as a human-readable string, e.g. "16:9". */
    public String getAspectRatio() {
        if (width == 0 || height == 0) return "N/A";
        int gcd = gcd(width, height);
        return (width / gcd) + ":" + (height / gcd);
    }

    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    // ── Nested Enums ──────────────────────────────────────────────────────────

    public enum ResolutionTier {
        LOW,
        HD,
        FHD,
        QHD,
        UHD_4K,
        UHD_5K,
        UHD_6K,
        UHD_8K,
        UNKNOWN
    }

    public enum DeviceClass {
        MOBILE,
        FOLDABLE,
        TABLET,
        LAPTOP,
        DESKTOP,
        TV,
        WEARABLE,
        VR,
        CONSOLE,
        UNKNOWN
    }
}