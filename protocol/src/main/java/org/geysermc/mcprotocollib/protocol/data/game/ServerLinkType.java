package org.geysermc.mcprotocollib.protocol.data.game;

public enum ServerLinkType {
    BUG_REPORT,
    COMMUNITY_GUIDELINES,
    SUPPORT,
    STATUS,
    FEEDBACK,
    COMMUNITY,
    WEBSITE,
    FORUMS,
    NEWS,
    ANNOUNCEMENTS;

    private static final ServerLinkType[] VALUES = values();

    public static ServerLinkType from(int id) {
        return VALUES[id];
    }
}
