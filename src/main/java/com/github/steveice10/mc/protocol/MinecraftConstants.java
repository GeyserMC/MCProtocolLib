package com.github.steveice10.mc.protocol;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.auth.service.SessionService;
import com.github.steveice10.mc.protocol.data.status.handler.ServerInfoBuilder;
import com.github.steveice10.mc.protocol.data.status.handler.ServerInfoHandler;
import com.github.steveice10.mc.protocol.data.status.handler.ServerPingTimeHandler;
import com.github.steveice10.packetlib.Flag;
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
    public static final Flag<GameProfile> PROFILE_KEY = new Flag<>("profile");

    /**
     * Session flag for providing a custom {@link SessionService} instance.
     */
    public static final Flag<SessionService> SESSION_SERVICE_KEY = new Flag<>("session-service");

    /**
     * Session flag for whether to automatically send a keep alive packet. <br>
     * For servers: determines if the keep alive task is run. <br>
     * For clients: determines if MCProtocolLib automatically sends back a packet
     * when the server sends their keep alive packet.
     */
    public static final Flag<Boolean> AUTOMATIC_KEEP_ALIVE_MANAGEMENT = new Flag<>("manage-keep-alive");

    // Client Key Constants

    /**
     * Session flag where the user's access token is stored. Client only.
     */
    public static final Flag<String> ACCESS_TOKEN_KEY = new Flag<>("access-token");

    /**
     * Session flag for providing a custom server info response handler. Client only.
     */
    public static final Flag<ServerInfoHandler> SERVER_INFO_HANDLER_KEY = new Flag<>("server-info-handler");

    /**
     * Session flag for providing a custom ping time response handler. Client only.
     */
    public static final Flag<ServerPingTimeHandler> SERVER_PING_TIME_HANDLER_KEY = new Flag<>("server-ping-time-handler");

    // Server Key Constants

    /**
     * Session flag for determining whether to verify users. Server only.
     */
    public static final Flag<Boolean> VERIFY_USERS_KEY = new Flag<>("verify-users");

    /**
     * Session flag for providing a custom server info response builder. Server only.
     */
    public static final Flag<ServerInfoBuilder> SERVER_INFO_BUILDER_KEY = new Flag<>("info-builder");

    /**
     * Session flag for providing a custom server login handler. Server only.
     */
    public static final Flag<ServerLoginHandler> SERVER_LOGIN_HANDLER_KEY = new Flag<>("login-handler");

    /**
     * Session flag for storing the current ping time. Server only.
     */
    public static final Flag<Long> PING_KEY = new Flag<>("ping");

    /**
     * Session flag for determining the packet compression threshold. Server only.
     */
    public static final Flag<Integer> SERVER_COMPRESSION_THRESHOLD = new Flag<>("compression-threshold");

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
