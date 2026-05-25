package dev.hope.vulcaobackend.Models.Properties;


import lombok.Getter;

@Getter
public enum ElectronicDeviceType {

    // ── Smartphones ───────────────────────────────────────────────────────────
    SMARTPHONE("Smartphone", "A standard touchscreen mobile phone.", DeviceCategory.SMARTPHONE, false),
    IPHONE("iPhone", "Apple's iPhone lineup.", DeviceCategory.SMARTPHONE, true),
    ANDROID_PHONE("Android Phone", "Android-based smartphone.", DeviceCategory.SMARTPHONE, false),
    BUDGET_PHONE("Budget Phone", "Entry-level smartphone.", DeviceCategory.SMARTPHONE, false),
    MID_RANGE_PHONE("Mid-Range Phone", "Mid-tier smartphone.", DeviceCategory.SMARTPHONE, false),
    FLAGSHIP_PHONE("Flagship Phone", "Top-tier premium smartphone.", DeviceCategory.SMARTPHONE, false),
    RUGGED_PHONE("Rugged Phone", "Durability-focused smartphone.", DeviceCategory.SMARTPHONE, false),
    GAMING_PHONE("Gaming Phone", "Performance-optimized gaming smartphone.", DeviceCategory.SMARTPHONE, false),
    SATELLITE_PHONE("Satellite Phone", "Phone using satellite networks.", DeviceCategory.SMARTPHONE, false),
    FEATURE_PHONE("Feature Phone", "Non-smart basic phone.", DeviceCategory.SMARTPHONE, false),
    FLIP_PHONE("Flip Phone", "Clamshell form-factor phone.", DeviceCategory.SMARTPHONE, false),
    BAR_PHONE("Bar Phone", "Traditional candy-bar form phone.", DeviceCategory.SMARTPHONE, false),
    DUAL_SIM_PHONE("Dual SIM Phone", "Phone supporting two SIM cards.", DeviceCategory.SMARTPHONE, false),
    ESIM_PHONE("eSIM Phone", "Phone with embedded SIM support.", DeviceCategory.SMARTPHONE, false),
    PHABLET("Phablet", "Oversized phone / mini-tablet hybrid (6\"–7\").", DeviceCategory.SMARTPHONE, false),

    // ── Foldables ─────────────────────────────────────────────────────────────
    FOLDABLE_PHONE("Foldable Phone", "Phone with a folding display.", DeviceCategory.FOLDABLE, false),
    FLIP_FOLDABLE("Flip Foldable", "Compact vertically-folding phone.", DeviceCategory.FOLDABLE, false),
    BOOK_FOLDABLE("Book-Style Foldable", "Horizontally-folding phone that opens like a book.", DeviceCategory.FOLDABLE, false),
    ROLLABLE_PHONE("Rollable Phone", "Phone with an extendable/rollable display.", DeviceCategory.FOLDABLE, false),
    DUAL_SCREEN_PHONE("Dual-Screen Phone", "Phone with a secondary display attachment.", DeviceCategory.FOLDABLE, false),
    FOLDABLE_TABLET("Foldable Tablet", "Large-format foldable that opens to tablet size.", DeviceCategory.FOLDABLE, false),

    // ── Tablets ───────────────────────────────────────────────────────────────
    TABLET("Tablet", "Generic touchscreen tablet.", DeviceCategory.TABLET, false),
    IPAD("iPad", "Apple iPad (standard).", DeviceCategory.TABLET, true),
    IPAD_MINI("iPad mini", "Apple compact iPad.", DeviceCategory.TABLET, true),
    IPAD_AIR("iPad Air", "Apple mid-tier iPad.", DeviceCategory.TABLET, true),
    IPAD_PRO("iPad Pro", "Apple professional-grade iPad.", DeviceCategory.TABLET, true),
    ANDROID_TABLET("Android Tablet", "Generic Android tablet.", DeviceCategory.TABLET, false),
    WINDOWS_TABLET("Windows Tablet", "Tablet running Windows OS.", DeviceCategory.TABLET, false),
    AMAZON_FIRE_TABLET("Amazon Fire Tablet", "Amazon Fire OS tablet.", DeviceCategory.TABLET, true),
    DRAWING_TABLET("Drawing Tablet", "Stylus-input graphics tablet.", DeviceCategory.TABLET, false),
    E_INK_TABLET("E-Ink Tablet", "Note-taking tablet with e-paper display.", DeviceCategory.TABLET, false),
    RUGGED_TABLET("Rugged Tablet", "Industrial/outdoor hardened tablet.", DeviceCategory.TABLET, false),
    KIDS_TABLET("Kids Tablet", "Child-oriented tablet with parental controls.", DeviceCategory.TABLET, false),
    TWO_IN_ONE_TABLET("2-in-1 Tablet", "Tablet that doubles as a laptop with keyboard.", DeviceCategory.TABLET, false),

    // ── Laptops ───────────────────────────────────────────────────────────────
    LAPTOP("Laptop", "Generic portable laptop computer.", DeviceCategory.LAPTOP, false),
    MACBOOK_AIR("MacBook Air", "Apple thin-and-light MacBook Air.", DeviceCategory.LAPTOP, true),
    MACBOOK_PRO("MacBook Pro", "Apple professional MacBook Pro.", DeviceCategory.LAPTOP, true),
    ULTRABOOK("Ultrabook", "Ultra-thin premium Intel-certified laptop.", DeviceCategory.LAPTOP, false),
    BUSINESS_LAPTOP("Business Laptop", "Enterprise-grade laptop.", DeviceCategory.LAPTOP, false),
    GAMING_LAPTOP("Gaming Laptop", "High-performance laptop for gaming.", DeviceCategory.LAPTOP, false),
    BUDGET_LAPTOP("Budget Laptop", "Entry-level affordable laptop.", DeviceCategory.LAPTOP, false),
    RUGGED_LAPTOP("Rugged Laptop", "Military/industrial-grade laptop.", DeviceCategory.LAPTOP, false),
    CHROMEBOOK("Chromebook", "Laptop running ChromeOS.", DeviceCategory.LAPTOP, false),
    CHROMEBOOK_PLUS("Chromebook Plus", "Enhanced Chromebook tier.", DeviceCategory.LAPTOP, false),
    TWO_IN_ONE_LAPTOP("2-in-1 Laptop", "Convertible laptop/tablet hybrid.", DeviceCategory.LAPTOP, false),
    THIN_CLIENT_LAPTOP("Thin Client Laptop", "Cloud-dependent minimal laptop.", DeviceCategory.LAPTOP, false),
    WORKSTATION_LAPTOP("Workstation Laptop", "Mobile workstation for professional tasks.", DeviceCategory.LAPTOP, false),
    MINI_LAPTOP("Mini Laptop", "Sub-12\" compact laptop.", DeviceCategory.LAPTOP, false),
    NETBOOK("Netbook", "Older ultra-compact budget laptop.", DeviceCategory.LAPTOP, false),

    // ── Desktop Computers ─────────────────────────────────────────────────────
    DESKTOP_PC("Desktop PC", "Standard tower desktop computer.", DeviceCategory.DESKTOP, false),
    IMAC("iMac", "Apple all-in-one desktop.", DeviceCategory.DESKTOP, true),
    MAC_MINI("Mac mini", "Apple compact desktop.", DeviceCategory.DESKTOP, true),
    MAC_PRO("Mac Pro", "Apple professional tower workstation.", DeviceCategory.DESKTOP, true),
    MAC_STUDIO("Mac Studio", "Apple compact pro desktop.", DeviceCategory.DESKTOP, true),
    ALL_IN_ONE_PC("All-in-One PC", "Desktop with built-in monitor.", DeviceCategory.DESKTOP, false),
    MINI_PC("Mini PC", "Compact small-form-factor desktop.", DeviceCategory.DESKTOP, false),
    GAMING_DESKTOP("Gaming Desktop", "High-performance gaming tower.", DeviceCategory.DESKTOP, false),
    WORKSTATION_DESKTOP("Workstation Desktop", "Professional-grade desktop workstation.", DeviceCategory.DESKTOP, false),
    THIN_CLIENT_DESKTOP("Thin Client Desktop", "Cloud-dependent minimal desktop.", DeviceCategory.DESKTOP, false),
    STICK_PC("Stick PC", "HDMI dongle-sized PC.", DeviceCategory.DESKTOP, false),
    NUC("NUC", "Intel Next Unit of Computing mini PC.", DeviceCategory.DESKTOP, true),
    RACK_SERVER("Rack Server", "Rack-mounted server unit.", DeviceCategory.DESKTOP, false),
    TOWER_SERVER("Tower Server", "Tower-form server computer.", DeviceCategory.DESKTOP, false),
    BLADE_SERVER("Blade Server", "Modular high-density server.", DeviceCategory.DESKTOP, false),

    // ── Monitors & Displays ───────────────────────────────────────────────────
    MONITOR("Monitor", "Standard external display.", DeviceCategory.DISPLAY, false),
    GAMING_MONITOR("Gaming Monitor", "High refresh-rate gaming display.", DeviceCategory.DISPLAY, false),
    ULTRAWIDE_MONITOR("Ultrawide Monitor", "21:9 or wider aspect ratio display.", DeviceCategory.DISPLAY, false),
    CURVED_MONITOR("Curved Monitor", "Display with curved panel.", DeviceCategory.DISPLAY, false),
    OLED_MONITOR("OLED Monitor", "Organic LED display monitor.", DeviceCategory.DISPLAY, false),
    PORTABLE_MONITOR("Portable Monitor", "USB-powered travel display.", DeviceCategory.DISPLAY, false),
    TOUCHSCREEN_MONITOR("Touchscreen Monitor", "Touch-enabled external display.", DeviceCategory.DISPLAY, false),
    STUDIO_DISPLAY("Studio Display", "Apple Studio Display.", DeviceCategory.DISPLAY, true),
    PRO_DISPLAY_XDR("Pro Display XDR", "Apple 6K reference monitor.", DeviceCategory.DISPLAY, true),
    PROJECTOR("Projector", "Image/video projection device.", DeviceCategory.DISPLAY, false),
    SMART_PROJECTOR("Smart Projector", "Projector with built-in Android/streaming.", DeviceCategory.DISPLAY, false),
    LASER_PROJECTOR("Laser Projector", "Laser light source projector.", DeviceCategory.DISPLAY, false),

    // ── TVs ───────────────────────────────────────────────────────────────────
    SMART_TV("Smart TV", "Internet-connected television.", DeviceCategory.TV, false),
    OLED_TV("OLED TV", "OLED panel smart TV.", DeviceCategory.TV, false),
    QLED_TV("QLED TV", "Quantum dot LED smart TV.", DeviceCategory.TV, false),
    MINILED_TV("Mini-LED TV", "Mini-LED backlit smart TV.", DeviceCategory.TV, false),
    MICROLED_TV("MicroLED TV", "Self-emissive MicroLED TV.", DeviceCategory.TV, false),
    LED_TV("LED TV", "Standard LED-backlit TV.", DeviceCategory.TV, false),
    QNED_TV("QNED TV", "Quantum NanoCell TV.", DeviceCategory.TV, false),
    HDTV("HDTV", "High-definition television.", DeviceCategory.TV, false),
    UHFTV("4K TV", "Ultra-HD 4K television.", DeviceCategory.TV, false),
    TV_8K("8K TV", "8K resolution television.", DeviceCategory.TV, false),
    PORTABLE_TV("Portable TV", "Battery-powered portable television.", DeviceCategory.TV, false),
    ROLLABLE_TV("Rollable TV", "TV with a rollable/scrollable display.", DeviceCategory.TV, false),
    TRANSPARENT_TV("Transparent TV", "TV with a see-through display.", DeviceCategory.TV, false),

    // ── Streaming & Media Devices ─────────────────────────────────────────────
    STREAMING_STICK("Streaming Stick", "HDMI streaming dongle (Chromecast, Fire Stick, Roku).", DeviceCategory.MEDIA_DEVICE, false),
    STREAMING_BOX("Streaming Box", "Set-top streaming media player (Apple TV, Nvidia Shield).", DeviceCategory.MEDIA_DEVICE, false),
    APPLE_TV("Apple TV", "Apple TV streaming box.", DeviceCategory.MEDIA_DEVICE, true),
    BLU_RAY_PLAYER("Blu-ray Player", "Physical disc media player.", DeviceCategory.MEDIA_DEVICE, false),
    DVD_PLAYER("DVD Player", "DVD disc media player.", DeviceCategory.MEDIA_DEVICE, false),
    MEDIA_SERVER("Media Server", "Local network media server (Plex, Kodi).", DeviceCategory.MEDIA_DEVICE, false),
    HTPC("HTPC", "Home Theater PC.", DeviceCategory.MEDIA_DEVICE, false),

    // ── Game Consoles ─────────────────────────────────────────────────────────
    HOME_CONSOLE("Home Console", "Living room game console.", DeviceCategory.GAMING, false),
    PORTABLE_CONSOLE("Portable Console", "Handheld game console.", DeviceCategory.GAMING, false),
    HYBRID_CONSOLE("Hybrid Console", "Home + portable console (e.g. Nintendo Switch).", DeviceCategory.GAMING, false),
    RETRO_CONSOLE("Retro Console", "Classic/retro game console.", DeviceCategory.GAMING, false),
    ARCADE_MACHINE("Arcade Machine", "Standalone arcade cabinet.", DeviceCategory.GAMING, false),
    CLOUD_GAMING_DEVICE("Cloud Gaming Device", "Streaming-only gaming hardware.", DeviceCategory.GAMING, false),
    HANDHELD_PC("Handheld Gaming PC", "PC in portable form factor (Steam Deck).", DeviceCategory.GAMING, false),

    // ── Wearables ─────────────────────────────────────────────────────────────
    SMARTWATCH("Smartwatch", "Wrist-worn smart device.", DeviceCategory.WEARABLE, false),
    APPLE_WATCH("Apple Watch", "Apple smartwatch.", DeviceCategory.WEARABLE, true),
    FITNESS_TRACKER("Fitness Tracker", "Wearable activity/health tracker band.", DeviceCategory.WEARABLE, false),
    SMART_GLASSES("Smart Glasses", "AR/smart eyewear.", DeviceCategory.WEARABLE, false),
    SMART_RING("Smart Ring", "Ring-form wearable health tracker.", DeviceCategory.WEARABLE, false),
    SPORTS_WATCH("Sports Watch", "GPS-enabled fitness/outdoor watch.", DeviceCategory.WEARABLE, false),
    HEARING_DEVICE("Smart Hearing Device", "AI-enhanced hearing aids or smart earwear.", DeviceCategory.WEARABLE, false),
    ECG_WEARABLE("ECG Wearable", "Medical-grade wearable heart monitor.", DeviceCategory.WEARABLE, false),
    CONTINUOUS_GLUCOSE_MONITOR("CGM Wearable", "Continuous glucose monitor wearable.", DeviceCategory.WEARABLE, false),
    BODY_CAM("Body Camera", "Wearable body-mounted camera.", DeviceCategory.WEARABLE, false),

    // ── Audio ─────────────────────────────────────────────────────────────────
    WIRED_EARPHONES("Wired Earphones", "Cable-connected in-ear headphones.", DeviceCategory.AUDIO, false),
    WIRELESS_EARBUDS("Wireless Earbuds", "True wireless in-ear earbuds.", DeviceCategory.AUDIO, false),
    AIRPODS("AirPods", "Apple AirPods.", DeviceCategory.AUDIO, true),
    AIRPODS_PRO("AirPods Pro", "Apple AirPods Pro with ANC.", DeviceCategory.AUDIO, true),
    AIRPODS_MAX("AirPods Max", "Apple over-ear headphones.", DeviceCategory.AUDIO, true),
    OVER_EAR_HEADPHONES("Over-Ear Headphones", "Full-sized over-ear headphones.", DeviceCategory.AUDIO, false),
    ON_EAR_HEADPHONES("On-Ear Headphones", "On-ear portable headphones.", DeviceCategory.AUDIO, false),
    NOISE_CANCELLING_HEADPHONES("Noise-Cancelling Headphones", "ANC-enabled headphones.", DeviceCategory.AUDIO, false),
    GAMING_HEADSET("Gaming Headset", "Headset with mic for gaming.", DeviceCategory.AUDIO, false),
    BLUETOOTH_SPEAKER("Bluetooth Speaker", "Portable wireless speaker.", DeviceCategory.AUDIO, false),
    SMART_SPEAKER("Smart Speaker", "Voice assistant-enabled speaker.", DeviceCategory.AUDIO, false),
    SOUNDBAR("Soundbar", "TV audio soundbar.", DeviceCategory.AUDIO, false),
    HOME_THEATER_SYSTEM("Home Theater System", "Multi-speaker surround sound system.", DeviceCategory.AUDIO, false),
    RECORD_PLAYER("Record Player", "Vinyl turntable.", DeviceCategory.AUDIO, false),
    DAC("DAC", "Digital-to-analogue audio converter.", DeviceCategory.AUDIO, false),
    AMP("Amplifier", "Audio signal amplifier.", DeviceCategory.AUDIO, false),

    // ── Cameras & Photography ─────────────────────────────────────────────────
    DSLR_CAMERA("DSLR Camera", "Digital single-lens reflex camera.", DeviceCategory.CAMERA, false),
    MIRRORLESS_CAMERA("Mirrorless Camera", "Mirrorless interchangeable-lens camera.", DeviceCategory.CAMERA, false),
    POINT_AND_SHOOT("Point-and-Shoot Camera", "Compact fixed-lens camera.", DeviceCategory.CAMERA, false),
    ACTION_CAMERA("Action Camera", "Rugged compact action/sports camera.", DeviceCategory.CAMERA, false),
    INSTANT_CAMERA("Instant Camera", "Camera with built-in photo printing.", DeviceCategory.CAMERA, false),
    FILM_CAMERA("Film Camera", "Analog film camera.", DeviceCategory.CAMERA, false),
    DRONE_CAMERA("Drone Camera", "UAV-mounted aerial camera.", DeviceCategory.CAMERA, false),
    WEBCAM("Webcam", "USB/wireless video camera for calls.", DeviceCategory.CAMERA, false),
    SECURITY_CAMERA("Security Camera", "Indoor/outdoor surveillance camera.", DeviceCategory.CAMERA, false),
    DASHCAM("Dashcam", "Vehicle-mounted recording camera.", DeviceCategory.CAMERA, false),
    THERMAL_CAMERA("Thermal Camera", "Infrared/thermal imaging camera.", DeviceCategory.CAMERA, false),
    THREE_SIXTY_CAMERA("360° Camera", "Omnidirectional spherical camera.", DeviceCategory.CAMERA, false),
    MEDIUM_FORMAT_CAMERA("Medium Format Camera", "Large sensor professional camera.", DeviceCategory.CAMERA, false),

    // ── VR / AR / XR ──────────────────────────────────────────────────────────
    VR_HEADSET("VR Headset", "Virtual reality headset.", DeviceCategory.XR, false),
    AR_HEADSET("AR Headset", "Augmented reality headset.", DeviceCategory.XR, false),
    MR_HEADSET("MR Headset", "Mixed reality headset.", DeviceCategory.XR, false),
    SPATIAL_COMPUTER("Spatial Computer", "Spatial computing device (Apple Vision Pro).", DeviceCategory.XR, false),
    VR_CONTROLLER("VR Controller", "6DoF motion VR controller.", DeviceCategory.XR, false),
    VR_GLOVES("VR Haptic Gloves", "Haptic feedback VR gloves.", DeviceCategory.XR, false),
    VR_TREADMILL("VR Treadmill", "Omni-directional VR movement platform.", DeviceCategory.XR, false),

    // ── E-Readers ─────────────────────────────────────────────────────────────
    E_READER("E-Reader", "E-ink electronic reading device.", DeviceCategory.E_READER, false),
    KINDLE("Kindle", "Amazon Kindle e-reader.", DeviceCategory.E_READER, true),
    KOBO("Kobo", "Kobo e-reader.", DeviceCategory.E_READER, true),
    BOOX("BOOX", "Onyx BOOX e-ink Android device.", DeviceCategory.E_READER, true),
    REMARKABLE("reMarkable", "reMarkable e-ink writing tablet.", DeviceCategory.E_READER, true),
    SUPERNOTE("Supernote", "Ratta Supernote e-ink note-taker.", DeviceCategory.E_READER, true),
    DAYLIGHT_COMPUTER("Daylight Computer", "Reflective LivePaper tablet.", DeviceCategory.E_READER, true),

    // ── Smart Home ────────────────────────────────────────────────────────────
    SMART_DISPLAY("Smart Display", "Voice assistant with touchscreen (Echo Show, Nest Hub).", DeviceCategory.SMART_HOME, false),
    SMART_THERMOSTAT("Smart Thermostat", "Wi-Fi connected temperature controller.", DeviceCategory.SMART_HOME, false),
    SMART_DOORBELL("Smart Doorbell", "Video-enabled smart doorbell.", DeviceCategory.SMART_HOME, false),
    SMART_LOCK("Smart Lock", "App/voice controlled door lock.", DeviceCategory.SMART_HOME, false),
    SMART_LIGHT("Smart Light / Hub", "Connected lighting system.", DeviceCategory.SMART_HOME, false),
    SMART_PLUG("Smart Plug", "Wi-Fi controlled power outlet.", DeviceCategory.SMART_HOME, false),
    SMART_SENSOR("Smart Sensor", "Motion/temp/humidity IoT sensor.", DeviceCategory.SMART_HOME, false),
    HOME_HUB("Home Hub", "Central smart home controller.", DeviceCategory.SMART_HOME, false),
    ROBOT_VACUUM("Robot Vacuum", "Autonomous robotic vacuum cleaner.", DeviceCategory.SMART_HOME, false),
    ROBOT_LAWNMOWER("Robot Lawnmower", "Autonomous robot lawn mower.", DeviceCategory.SMART_HOME, false),
    SMART_REFRIGERATOR("Smart Refrigerator", "Internet-connected smart fridge.", DeviceCategory.SMART_HOME, false),
    SMART_OVEN("Smart Oven", "Wi-Fi controlled smart oven.", DeviceCategory.SMART_HOME, false),
    AIR_PURIFIER_SMART("Smart Air Purifier", "Connected air purifier.", DeviceCategory.SMART_HOME, false),

    // ── Networking ────────────────────────────────────────────────────────────
    ROUTER("Router", "Home/office network router.", DeviceCategory.NETWORKING, false),
    MESH_ROUTER("Mesh Router", "Whole-home mesh Wi-Fi system.", DeviceCategory.NETWORKING, false),
    MODEM("Modem", "Internet modem.", DeviceCategory.NETWORKING, false),
    MODEM_ROUTER_COMBO("Modem + Router Combo", "Combined modem/router gateway.", DeviceCategory.NETWORKING, false),
    NETWORK_SWITCH("Network Switch", "Ethernet network switch.", DeviceCategory.NETWORKING, false),
    NETWORK_ATTACHED_STORAGE("NAS", "Network-attached storage device.", DeviceCategory.NETWORKING, false),
    ACCESS_POINT("Access Point", "Wi-Fi access point extender.", DeviceCategory.NETWORKING, false),
    POWERLINE_ADAPTER("Powerline Adapter", "Ethernet-over-powerline adapter.", DeviceCategory.NETWORKING, false),
    WIFI_EXTENDER("Wi-Fi Extender", "Wireless signal range extender.", DeviceCategory.NETWORKING, false),
    CELLULAR_ROUTER("Cellular/4G Router", "SIM-based LTE/5G router.", DeviceCategory.NETWORKING, false),
    FIREWALL_APPLIANCE("Firewall Appliance", "Hardware network firewall.", DeviceCategory.NETWORKING, false),

    // ── Storage ───────────────────────────────────────────────────────────────
    EXTERNAL_HDD("External HDD", "Portable external hard disk drive.", DeviceCategory.STORAGE, false),
    EXTERNAL_SSD("External SSD", "Portable external solid-state drive.", DeviceCategory.STORAGE, false),
    USB_FLASH_DRIVE("USB Flash Drive", "USB thumb drive.", DeviceCategory.STORAGE, false),
    MEMORY_CARD("Memory Card", "SD / microSD memory card.", DeviceCategory.STORAGE, false),
    OPTICAL_DISC("Optical Disc Drive", "CD/DVD/Blu-ray optical drive.", DeviceCategory.STORAGE, false),
    RAID_ENCLOSURE("RAID Enclosure", "Multi-drive RAID storage enclosure.", DeviceCategory.STORAGE, false),
    TAPE_DRIVE("Tape Drive", "Magnetic tape archival storage.", DeviceCategory.STORAGE, false),
    DOCKING_STATION("Docking Station", "Multi-port hub/dock for laptops.", DeviceCategory.STORAGE, false),

    // ── Input Devices ─────────────────────────────────────────────────────────
    KEYBOARD("Keyboard", "Standard input keyboard.", DeviceCategory.INPUT, false),
    MECHANICAL_KEYBOARD("Mechanical Keyboard", "Mechanical switch keyboard.", DeviceCategory.INPUT, false),
    GAMING_KEYBOARD("Gaming Keyboard", "RGB gaming keyboard.", DeviceCategory.INPUT, false),
    MOUSE("Mouse", "Standard computer mouse.", DeviceCategory.INPUT, false),
    GAMING_MOUSE("Gaming Mouse", "High-DPI gaming mouse.", DeviceCategory.INPUT, false),
    TRACKPAD("Trackpad", "External trackpad / Magic Trackpad.", DeviceCategory.INPUT, false),
    TRACKBALL("Trackball", "Trackball pointing device.", DeviceCategory.INPUT, false),
    STYLUS("Stylus / Apple Pencil", "Pressure-sensitive digital stylus.", DeviceCategory.INPUT, false),
    GRAPHICS_TABLET("Graphics Tablet", "Pen input graphics tablet.", DeviceCategory.INPUT, false),
    CONTROLLER("Game Controller", "Gamepad controller.", DeviceCategory.INPUT, false),
    FLIGHT_STICK("Flight Stick / HOTAS", "Flight simulator joystick.", DeviceCategory.INPUT, false),
    STEERING_WHEEL("Racing Wheel", "Force-feedback racing wheel.", DeviceCategory.INPUT, false),
    NUMPAD("Numpad", "External numeric keypad.", DeviceCategory.INPUT, false),
    STREAM_DECK("Stream Deck", "Macro/shortcut LCD button pad.", DeviceCategory.INPUT, false),
    BARCODE_SCANNER("Barcode Scanner", "Optical barcode reader.", DeviceCategory.INPUT, false),
    BIOMETRIC_SCANNER("Biometric Scanner", "Fingerprint/iris scanner.", DeviceCategory.INPUT, false),

    // ── Power ─────────────────────────────────────────────────────────────────
    POWER_BANK("Power Bank", "Portable battery charger.", DeviceCategory.POWER, false),
    WIRELESS_CHARGER("Wireless Charger", "Qi/MagSafe wireless charging pad.", DeviceCategory.POWER, false),
    MAGSAFE_CHARGER("MagSafe Charger", "Apple MagSafe magnetic charger.", DeviceCategory.POWER, true),
    UPS("UPS", "Uninterruptible power supply.", DeviceCategory.POWER, false),
    SOLAR_CHARGER("Solar Charger", "Solar panel USB charger.", DeviceCategory.POWER, false),
    MULTI_PORT_CHARGER("Multi-Port Charger", "GaN multi-port fast charger.", DeviceCategory.POWER, false),
    CHARGING_DOCK("Charging Dock", "Multi-device charging station.", DeviceCategory.POWER, false),

    // ── Printers & Scanners ───────────────────────────────────────────────────
    INKJET_PRINTER("Inkjet Printer", "Consumer inkjet printer.", DeviceCategory.PRINTER, false),
    LASER_PRINTER("Laser Printer", "Laser toner printer.", DeviceCategory.PRINTER, false),
    PHOTO_PRINTER("Photo Printer", "High-quality photo printer.", DeviceCategory.PRINTER, false),
    LABEL_PRINTER("Label Printer", "Thermal label printer.", DeviceCategory.PRINTER, false),
    THREE_D_PRINTER("3D Printer", "Additive manufacturing printer.", DeviceCategory.PRINTER, false),
    SCANNER("Scanner", "Flatbed document/photo scanner.", DeviceCategory.PRINTER, false),
    ALL_IN_ONE_PRINTER("All-in-One Printer", "Printer, scanner, and copier combo.", DeviceCategory.PRINTER, false),
    PLOTTER("Plotter", "Large-format vector plotter.", DeviceCategory.PRINTER, false),

    // ── Development & Single-Board Computers ───────────────────────────────────
    SINGLE_BOARD_COMPUTER("Single-Board Computer", "SBC like Raspberry Pi.", DeviceCategory.DEV_BOARD, false),
    RASPBERRY_PI("Raspberry Pi", "Popular Linux SBC.", DeviceCategory.DEV_BOARD, true),
    ARDUINO("Arduino", "Microcontroller development board.", DeviceCategory.DEV_BOARD, true),
    FPGA_BOARD("FPGA Board", "Field-programmable gate array board.", DeviceCategory.DEV_BOARD, false),
    NVIDIA_JETSON("NVIDIA Jetson", "AI edge computing module.", DeviceCategory.DEV_BOARD, true),
    MICROCONTROLLER("Microcontroller", "Embedded microcontroller unit.", DeviceCategory.DEV_BOARD, false),
    DEVELOPMENT_KIT("Development Kit", "Manufacturer SDK hardware kit.", DeviceCategory.DEV_BOARD, false),

    // ── Other ─────────────────────────────────────────────────────────────────
    CALCULATORS("Calculator", "Scientific or graphing calculator.", DeviceCategory.OTHER, false),
    GPS_DEVICE("GPS Device", "Standalone GPS navigation unit.", DeviceCategory.OTHER, false),
    DIGITAL_FRAME("Digital Photo Frame", "Wi-Fi connected digital picture frame.", DeviceCategory.OTHER, false),
    PORTABLE_TRANSLATOR("Portable Translator", "AI-powered language translation device.", DeviceCategory.OTHER, false),
    FAX_MACHINE("Fax Machine", "Document facsimile machine.", DeviceCategory.OTHER, false),
    CASH_REGISTER("Cash Register / POS", "Point-of-sale terminal.", DeviceCategory.OTHER, false),
    ELECTRONIC_READER_PEN("Reader Pen", "Scanning pen that reads text aloud.", DeviceCategory.OTHER, false),
    DIGITAL_PIANO("Digital Piano / MIDI", "Electronic keyboard / MIDI controller.", DeviceCategory.OTHER, false),
    WALKIE_TALKIE("Walkie-Talkie", "Two-way radio communicator.", DeviceCategory.OTHER, false),
    SATELLITE_COMMUNICATOR("Satellite Communicator", "Two-way satellite messaging device.", DeviceCategory.OTHER, false),

    UNKNOWN("Unknown", "Unknown device type.", DeviceCategory.OTHER, false);

    // ── Fields ────────────────────────────────────────────────────────────────
    private final String displayName;
    private final String description;
    private final DeviceCategory category;
    /** True when this entry is brand-specific (e.g. iPhone, iPad, AirPods). */
    private final boolean brandSpecific;

    ElectronicDeviceType(String displayName, String description, DeviceCategory category, boolean brandSpecific) {
        this.displayName = displayName;
        this.description = description;
        this.category = category;
        this.brandSpecific = brandSpecific;
    }

    public String getDisplayName()   { return displayName;   }
    public String getDescription()   { return description;   }
    public DeviceCategory getCategory() { return category;   }
    public boolean isBrandSpecific() { return brandSpecific; }

    // ── Nested Enum ───────────────────────────────────────────────────────────
    public enum DeviceCategory {
        SMARTPHONE,
        FOLDABLE,
        TABLET,
        LAPTOP,
        DESKTOP,
        DISPLAY,
        TV,
        MEDIA_DEVICE,
        GAMING,
        WEARABLE,
        AUDIO,
        CAMERA,
        XR,
        E_READER,
        SMART_HOME,
        NETWORKING,
        STORAGE,
        INPUT,
        POWER,
        PRINTER,
        DEV_BOARD,
        OTHER
    }
}