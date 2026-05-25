package dev.hope.vulcaobackend.Models.Properties;

public enum BookFormat {
    // Physical formats
    PAPERBACK,
    HARDCOVER,
    MASS_MARKET_PAPERBACK,
    TRADE_PAPERBACK,
    LARGE_PRINT,
    POCKET_BOOK,
    MINIATURE_BOOK,
    FLIP_BOOK,

    // Bound styles
    PERFECT_BOUND,
    CASE_BOUND,
    LIBRARY_BINDING,
    LEATHERBOUND,
    SPIRAL_BOUND,
    COIL_BOUND,
    SADDLE_STITCH,
    LOOSE_LEAF,

    // Children / special formats
    BOARD_BOOK,
    PICTURE_BOOK,
    ACTIVITY_BOOK,
    LIFT_THE_FLAP,
    POP_UP_BOOK,

    // Collections & compilations
    BOXED_SET,
    OMNIBUS,
    ANTHOLOGY,
    CHAPBOOK,
    ZINE,
    COMIC_BOOK,
    GRAPHIC_NOVEL,
    MANGA,

    // Digital formats
    EBOOK,
    PDF,
    EPUB,
    EPUB3,
    MOBI,
    AZW,
    AZW3,
    CBZ,
    CBR,
    LIT,
    FB2,
    DJVU,
    IBA,
    ODF,
    HTML_EBOOK,

    // Audio formats
    AUDIOBOOK,
    AUDIOBOOK_MP3,
    AUDIOBOOK_AAC,
    AUDIOBOOK_FLAC,
    AUDIOBOOK_WMA,
    AUDIO_CD,

    // Accessibility formats
    BRAILLE,
    DAISY,
    LARGE_CELL_BRAILLE,

    // Other
    NEWSPAPER,
    MAGAZINE,
    JOURNAL,
    NEWSLETTER,
    MANUSCRIPT
}
