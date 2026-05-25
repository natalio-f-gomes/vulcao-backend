package dev.hope.vulcaobackend.Models.Properties;

import lombok.Getter;

@Getter
public enum MediaType {

    // ── Film ──────────────────────────────────────────────────────────────────
    MOVIE("Movie", MediaCategory.FILM),
    SHORT_FILM("Short Film", MediaCategory.FILM),
    FEATURE_FILM("Feature Film", MediaCategory.FILM),
    SILENT_FILM("Silent Film", MediaCategory.FILM),
    ANIMATED_FILM("Animated Film", MediaCategory.FILM),
    ANIME_FILM("Anime Film", MediaCategory.FILM),
    DOCUMENTARY_FILM("Documentary Film", MediaCategory.FILM),
    MOCKUMENTARY_FILM("Mockumentary Film", MediaCategory.FILM),
    CONCERT_FILM("Concert Film", MediaCategory.FILM),
    EXPERIMENTAL_FILM("Experimental Film", MediaCategory.FILM),
    FOUND_FOOTAGE_FILM("Found Footage Film", MediaCategory.FILM),
    DIRECT_TO_VIDEO("Direct-to-Video Film", MediaCategory.FILM),
    MADE_FOR_TV_FILM("Made-for-TV Film", MediaCategory.FILM),
    ANTHOLOGY_FILM("Anthology Film", MediaCategory.FILM),
    COMPILATION_FILM("Compilation Film", MediaCategory.FILM),
    FAN_FILM("Fan Film", MediaCategory.FILM),
    FILM_SERIES("Film Series", MediaCategory.FILM),
    FILM_TRILOGY("Film Trilogy", MediaCategory.FILM),

    // ── Television ────────────────────────────────────────────────────────────
    TV_SERIES("TV Series", MediaCategory.TELEVISION),
    TV_MINI_SERIES("TV Mini-Series", MediaCategory.TELEVISION),
    LIMITED_SERIES("Limited Series", MediaCategory.TELEVISION),
    ANTHOLOGY_SERIES("Anthology Series", MediaCategory.TELEVISION),
    DOCUSERIES("Docuseries", MediaCategory.TELEVISION),
    REALITY_SHOW("Reality Show", MediaCategory.TELEVISION),
    GAME_SHOW("Game Show", MediaCategory.TELEVISION),
    TALK_SHOW("Talk Show", MediaCategory.TELEVISION),
    LATE_NIGHT_SHOW("Late Night Show", MediaCategory.TELEVISION),
    NEWS_PROGRAM("News Program", MediaCategory.TELEVISION),
    SOAP_OPERA("Soap Opera", MediaCategory.TELEVISION),
    TELENOVELA("Telenovela", MediaCategory.TELEVISION),
    NOVELA("Novela", MediaCategory.TELEVISION),
    PROCEDURAL("Procedural", MediaCategory.TELEVISION),
    SITCOM("Sitcom", MediaCategory.TELEVISION),
    SKETCH_SHOW("Sketch Show", MediaCategory.TELEVISION),
    VARIETY_SHOW("Variety Show", MediaCategory.TELEVISION),
    INFOMERCIAL("Infomercial", MediaCategory.TELEVISION),
    TELEFILM("Telefilm", MediaCategory.TELEVISION),
    PILOT("Pilot", MediaCategory.TELEVISION),
    SPECIAL("TV Special", MediaCategory.TELEVISION),
    HOLIDAY_SPECIAL("Holiday Special", MediaCategory.TELEVISION),

    // ── Anime ─────────────────────────────────────────────────────────────────
    ANIME_SERIES("Anime Series", MediaCategory.ANIME),
    ANIME_MINI_SERIES("Anime Mini-Series", MediaCategory.ANIME),
    OVA("OVA (Original Video Animation)", MediaCategory.ANIME),
    ONA("ONA (Original Net Animation)", MediaCategory.ANIME),
    OAD("OAD (Original Animated Disc)", MediaCategory.ANIME),
    ANIME_SPECIAL("Anime Special", MediaCategory.ANIME),
    ANIME_SHORT("Anime Short", MediaCategory.ANIME),
    ANIME_MUSIC_VIDEO("Anime Music Video", MediaCategory.ANIME),
    DONGHUA("Donghua (Chinese Animation)", MediaCategory.ANIME),
    MANHWA_ADAPTATION("Manhwa Adaptation", MediaCategory.ANIME),
    MANHUA_ADAPTATION("Manhua Adaptation", MediaCategory.ANIME),

    // ── Animation ─────────────────────────────────────────────────────────────
    ANIMATED_SERIES("Animated Series", MediaCategory.ANIMATION),
    ANIMATED_SHORT("Animated Short", MediaCategory.ANIMATION),
    ANIMATED_SPECIAL("Animated Special", MediaCategory.ANIMATION),
    ANIMATED_MINI_SERIES("Animated Mini-Series", MediaCategory.ANIMATION),
    STOP_MOTION_SERIES("Stop-Motion Series", MediaCategory.ANIMATION),
    CLAYMATION("Claymation", MediaCategory.ANIMATION),
    MOTION_CAPTURE("Motion Capture Animation", MediaCategory.ANIMATION),
    CGI_SERIES("CGI Series", MediaCategory.ANIMATION),
    FLASH_ANIMATION("Flash Animation", MediaCategory.ANIMATION),
    ADULT_ANIMATION("Adult Animation", MediaCategory.ANIMATION),

    // ── Streaming Originals ────────────────────────────────────────────────────
    STREAMING_SERIES("Streaming Series", MediaCategory.STREAMING),
    STREAMING_FILM("Streaming Film", MediaCategory.STREAMING),
    STREAMING_DOCUMENTARY("Streaming Documentary", MediaCategory.STREAMING),
    STREAMING_SPECIAL("Streaming Special", MediaCategory.STREAMING),
    STREAMING_MINI_SERIES("Streaming Mini-Series", MediaCategory.STREAMING),
    STREAMING_SHORT("Streaming Short", MediaCategory.STREAMING),
    STREAMING_STAND_UP("Streaming Stand-Up Special", MediaCategory.STREAMING),
    INTERACTIVE_FILM("Interactive Film", MediaCategory.STREAMING),
    INTERACTIVE_SERIES("Interactive Series", MediaCategory.STREAMING),

    // ── Web / Online ──────────────────────────────────────────────────────────
    WEB_SERIES("Web Series", MediaCategory.WEB),
    WEBSODE("Websode", MediaCategory.WEB),
    WEB_FILM("Web Film", MediaCategory.WEB),
    YOUTUBE_SERIES("YouTube Series", MediaCategory.WEB),
    YOUTUBE_VIDEO("YouTube Video", MediaCategory.WEB),
    TWITCH_STREAM("Twitch Stream / VOD", MediaCategory.WEB),
    VLOG("Vlog", MediaCategory.WEB),
    VIRAL_VIDEO("Viral Video", MediaCategory.WEB),
    SHORT_FORM_VIDEO("Short-Form Video", MediaCategory.WEB),

    // ── Books & Literature ────────────────────────────────────────────────────
    NOVEL("Novel", MediaCategory.LITERATURE),
    NOVELLA("Novella", MediaCategory.LITERATURE),
    SHORT_STORY("Short Story", MediaCategory.LITERATURE),
    ANTHOLOGY_BOOK("Anthology Book", MediaCategory.LITERATURE),
    POETRY_COLLECTION("Poetry Collection", MediaCategory.LITERATURE),
    BIOGRAPHY("Biography", MediaCategory.LITERATURE),
    AUTOBIOGRAPHY("Autobiography", MediaCategory.LITERATURE),
    MEMOIR("Memoir", MediaCategory.LITERATURE),
    SELF_HELP_BOOK("Self-Help Book", MediaCategory.LITERATURE),
    TEXTBOOK("Textbook", MediaCategory.LITERATURE),
    ENCYCLOPEDIA("Encyclopedia", MediaCategory.LITERATURE),
    ESSAY_COLLECTION("Essay Collection", MediaCategory.LITERATURE),
    TRAVEL_WRITING("Travel Writing", MediaCategory.LITERATURE),
    GRAPHIC_NOVEL("Graphic Novel", MediaCategory.LITERATURE),
    LIGHT_NOVEL("Light Novel", MediaCategory.LITERATURE),
    WEB_NOVEL("Web Novel", MediaCategory.LITERATURE),
    FANFICTION("Fanfiction", MediaCategory.LITERATURE),

    // ── Comics & Manga ────────────────────────────────────────────────────────
    COMIC_BOOK("Comic Book", MediaCategory.COMICS),
    COMIC_STRIP("Comic Strip", MediaCategory.COMICS),
    MANGA("Manga", MediaCategory.COMICS),
    MANHWA("Manhwa", MediaCategory.COMICS),
    MANHUA("Manhua", MediaCategory.COMICS),
    WEBTOON("Webtoon", MediaCategory.COMICS),
    WEBCOMIC("Webcomic", MediaCategory.COMICS),
    ONE_SHOT("One-Shot", MediaCategory.COMICS),
    DOUJINSHI("Doujinshi", MediaCategory.COMICS),
    GRAPHIC_MEMOIR("Graphic Memoir", MediaCategory.COMICS),

    // ── Music ─────────────────────────────────────────────────────────────────
    ALBUM("Album", MediaCategory.MUSIC),
    SINGLE("Single", MediaCategory.MUSIC),
    EP("EP (Extended Play)", MediaCategory.MUSIC),
    MIXTAPE("Mixtape", MediaCategory.MUSIC),
    SOUNDTRACK("Soundtrack", MediaCategory.MUSIC),
    COMPILATION_ALBUM("Compilation Album", MediaCategory.MUSIC),
    LIVE_ALBUM("Live Album", MediaCategory.MUSIC),
    DEMO("Demo", MediaCategory.MUSIC),
    MUSIC_VIDEO("Music Video", MediaCategory.MUSIC),
    CONCERT_RECORDING("Concert Recording", MediaCategory.MUSIC),
    PODCAST_MUSIC("Music Podcast", MediaCategory.MUSIC),
    RADIO_SHOW("Radio Show", MediaCategory.MUSIC),

    // ── Podcast & Audio ───────────────────────────────────────────────────────
    PODCAST("Podcast", MediaCategory.AUDIO),
    AUDIOBOOK("Audiobook", MediaCategory.AUDIO),
    AUDIO_DRAMA("Audio Drama", MediaCategory.AUDIO),
    RADIO_DRAMA("Radio Drama", MediaCategory.AUDIO),
    RADIO_DOCUMENTARY("Radio Documentary", MediaCategory.AUDIO),
    ASMR("ASMR", MediaCategory.AUDIO),
    NARRATED_SHORT_STORY("Narrated Short Story", MediaCategory.AUDIO),

    // ── Games ─────────────────────────────────────────────────────────────────
    VIDEO_GAME("Video Game", MediaCategory.GAME),
    MOBILE_GAME("Mobile Game", MediaCategory.GAME),
    BOARD_GAME("Board Game", MediaCategory.GAME),
    CARD_GAME("Card Game", MediaCategory.GAME),
    TABLETOP_RPG("Tabletop RPG", MediaCategory.GAME),
    VISUAL_NOVEL("Visual Novel", MediaCategory.GAME),
    INTERACTIVE_FICTION("Interactive Fiction", MediaCategory.GAME),
    ARCADE_GAME("Arcade Game", MediaCategory.GAME),
    VR_EXPERIENCE("VR Experience", MediaCategory.GAME),

    // ── Stage & Live Performance ───────────────────────────────────────────────
    STAGE_PLAY("Stage Play", MediaCategory.STAGE),
    MUSICAL_THEATRE("Musical Theatre", MediaCategory.STAGE),
    OPERA("Opera", MediaCategory.STAGE),
    BALLET("Ballet", MediaCategory.STAGE),
    STAND_UP_COMEDY("Stand-Up Comedy", MediaCategory.STAGE),
    COMEDY_SPECIAL("Comedy Special", MediaCategory.STAGE),
    IMPROV_SHOW("Improv Show", MediaCategory.STAGE),
    SPOKEN_WORD("Spoken Word", MediaCategory.STAGE),
    CIRCUS("Circus", MediaCategory.STAGE),
    MAGIC_SHOW("Magic Show", MediaCategory.STAGE),

    // ── Other ─────────────────────────────────────────────────────────────────
    MAGAZINE("Magazine", MediaCategory.OTHER),
    NEWSPAPER("Newspaper", MediaCategory.OTHER),
    NEWSLETTER("Newsletter", MediaCategory.OTHER),
    ZINE("Zine", MediaCategory.OTHER),
    PHOTO_BOOK("Photo Book", MediaCategory.OTHER),
    ART_BOOK("Art Book", MediaCategory.OTHER),
    EXHIBITION("Exhibition / Installation", MediaCategory.OTHER),
    MUSEUM_EXHIBIT("Museum Exhibit", MediaCategory.OTHER),
    MIXED_MEDIA("Mixed Media", MediaCategory.OTHER),
    UNKNOWN("Unknown", MediaCategory.OTHER);

    private final String displayName;
    private final MediaCategory category;

    MediaType(String displayName, MediaCategory category) {
        this.displayName = displayName;
        this.category = category;
    }

    public String getDisplayName() { return displayName; }
    public MediaCategory getCategory() { return category; }

    public enum MediaCategory {
        FILM,
        TELEVISION,
        ANIME,
        ANIMATION,
        STREAMING,
        WEB,
        LITERATURE,
        COMICS,
        MUSIC,
        AUDIO,
        GAME,
        STAGE,
        OTHER
    }
}