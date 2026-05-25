package dev.hope.vulcaobackend.Models.Properties;

public enum MediaStatus {
    // Production states
    UPCOMING,
    IN_PRODUCTION,
    PRE_PRODUCTION,
    POST_PRODUCTION,
    ANNOUNCED,

    // Active states
    ONGOING,
    AIRING,
    RELEASING,
    RETURNING,
    ON_HIATUS,

    // Completion states
    COMPLETED,
    ENDED,
    FINISHED,
    CONCLUDED,
    CANCELLED,
    DISCONTINUED,
    ABANDONED,

    // Availability states
    AVAILABLE,
    UNAVAILABLE,
    LICENSED,
    UNLICENSED,
    REGION_LOCKED,
    REMOVED,
    DELISTED,

    // Special states
    REMAKE,
    REMASTER,
    REBOOT,
    SEQUEL,
    PREQUEL,
    SPIN_OFF,
    SPECIAL,
    OVA,
    ONA,
    MOVIE,
    PILOT,

    // Rights & release states
    LIMITED_RELEASE,
    WIDE_RELEASE,
    STREAMING_ONLY,
    THEATRICAL_ONLY,
    DIRECT_TO_VIDEO,
    EXCLUSIVE,
    OUT_OF_PRINT,
    PUBLIC_DOMAIN,
    UNKNOWN
}
