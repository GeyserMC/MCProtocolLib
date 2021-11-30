package com.github.steveice10.mc.protocol;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.auth.service.SessionService;
import com.github.steveice10.packetlib.packet.DefaultPacketHeader;
import com.github.steveice10.packetlib.packet.PacketHeader;

/**
 * Class containing various constants for Minecraft sessions.
 */
public final class MinecraftConstants {
    // General Key Constants

    /**
     * Session flag where the user {@link GameProfile} is stored.
     */
    public static final String PROFILE_KEY = "profile";

    /**
     * Session flag for providing a custom {@link SessionService} instance.
     */
    public static final String SESSION_SERVICE_KEY = "session-service";

    /**
     * Session flag for whether to automatically send a keep alive packet. <br>
     * For servers: determines if the keep alive task is run. <br>
     * For clients: determines if MCProtocolLib automatically sends back a packet
     * when the server sends their keep alive packet.
     */
    public static final String AUTOMATIC_KEEP_ALIVE_MANAGEMENT = "manage-keep-alive";

    // Client Key Constants

    /**
     * Session flag where the user's access token is stored. Client only.
     */
    public static final String ACCESS_TOKEN_KEY = "access-token";

    /**
     * Session flag for providing a custom server info response handler. Client only.
     */
    public static final String SERVER_INFO_HANDLER_KEY = "server-info-handler";

    /**
     * Session flag for providing a custom ping time response handler. Client only.
     */
    public static final String SERVER_PING_TIME_HANDLER_KEY = "server-ping-time-handler";

    // Server Key Constants

    /**
     * Session flag for determining whether to verify users. Server only.
     */
    public static final String VERIFY_USERS_KEY = "verify-users";

    /**
     * Session flag for providing a custom server info response builder. Server only.
     */
    public static final String SERVER_INFO_BUILDER_KEY = "info-builder";

    /**
     * Session flag for providing a custom server login handler. Server only.
     */
    public static final String SERVER_LOGIN_HANDLER_KEY = "login-handler";

    /**
     * Session flag for storing the current ping time. Server only.
     */
    public static final String PING_KEY = "ping";

    /**
     * Session flag for determining the packet compression threshold. Server only.
     */
    public static final String SERVER_COMPRESSION_THRESHOLD = "compression-threshold";

    /**
     * The packet header used by Minecraft.
     */
    public static final PacketHeader PACKET_HEADER = new DefaultPacketHeader();

    /**
     * The SRV Record prefix used by Minecraft.
     */
    public static final String SRV_RECORD_PREFIX = "_minecraft";

    private MinecraftConstants() {
    }
}
