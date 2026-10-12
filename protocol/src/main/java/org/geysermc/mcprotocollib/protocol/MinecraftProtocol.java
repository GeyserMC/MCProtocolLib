package org.geysermc.mcprotocollib.protocol;

import lombok.Getter;
import lombok.Setter;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtUtils;
import org.geysermc.mcprotocollib.auth.GameProfile;
import org.geysermc.mcprotocollib.network.BuiltinFlags;
import org.geysermc.mcprotocollib.network.Server;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.crypt.AESEncryption;
import org.geysermc.mcprotocollib.network.crypt.EncryptionConfig;
import org.geysermc.mcprotocollib.network.packet.PacketHeader;
import org.geysermc.mcprotocollib.network.packet.PacketProtocol;
import org.geysermc.mcprotocollib.network.packet.PacketRegistry;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodec;
import org.geysermc.mcprotocollib.protocol.codec.PacketCodec;
import org.geysermc.mcprotocollib.protocol.data.ProtocolState;
import org.geysermc.mcprotocollib.protocol.data.handshake.HandshakeIntent;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Objects;
import java.util.UUID;

/**
 * Implements the Minecraft protocol.
 */
public class MinecraftProtocol extends PacketProtocol {
    private static final Logger log = LoggerFactory.getLogger(MinecraftProtocol.class);

    /**
     * The network codec sent from the server to the client during {@link ProtocolState#CONFIGURATION}.
     * Lazily loaded once when {@link #newServerSession(Server, Session)} is invoked,
     * if {@link #isUseDefaultListeners()} is true.
     */
    @Nullable
    private static NbtMap DEFAULT_NETWORK_CODEC;
    /**
     * The tags sent from the server to the client during {@link ProtocolState#CONFIGURATION}.
     * Lazily loaded once when {@link #newServerSession(Server, Session)} is invoked,
     * if {@link #isUseDefaultListeners()} is true.
     */
    @Nullable
    private static NbtMap DEFAULT_NETWORK_TAGS;

    /**
     * The codec used for the Minecraft protocol.
     */
    @Getter
    private final PacketCodec codec;

    private @Nullable ProtocolState inboundState;
    private @Nullable PacketRegistry inboundStateRegistry;

    private @Nullable ProtocolState outboundState;
    private @Nullable PacketRegistry outboundStateRegistry;

    private final HandshakeIntent handshakeIntent;

    /**
     * The player's identity.
     */
    @Getter
    private @Nullable GameProfile profile;

    /**
     * Authentication access token.
     */
    @Getter
    private @Nullable String accessToken;

    /**
     * Whether to add the default client and server listeners for performing initial login.
     */
    @Getter
    @Setter
    private boolean useDefaultListeners = true;

    /**
     * Constructs a new MinecraftProtocol instance for making status queries.
     */
    public MinecraftProtocol() {
        this(MinecraftCodec.CODEC);
    }

    /**
     * Constructs a new MinecraftProtocol instance for making status queries.
     *
     * @param codec The packet codec to use.
     */
    public MinecraftProtocol(PacketCodec codec) {
        this.codec = codec;
        this.handshakeIntent = HandshakeIntent.STATUS;

        resetStates();
    }

    /**
     * Constructs a new MinecraftProtocol instance for logging in using offline mode.
     *
     * @param username Username to use.
     */
    public MinecraftProtocol(String username) {
        this(new GameProfile(UUID.randomUUID(), username), null);
    }

    /**
     * Constructs a new MinecraftProtocol instance for logging in using offline mode.
     *
     * @param codec The packet codec to use.
     * @param username Username to use.
     */
    public MinecraftProtocol(PacketCodec codec, String username) {
        this(codec, new GameProfile(UUID.randomUUID(), username), null);
    }

    /**
     * Constructs a new MinecraftProtocol instance for logging in.
     *
     * @param profile GameProfile to use.
     * @param accessToken Access token to use, or null if using offline mode.
     */
    public MinecraftProtocol(GameProfile profile, @Nullable String accessToken) {
        this(MinecraftCodec.CODEC, profile, accessToken);
    }

    /**
     * Constructs a new MinecraftProtocol instance for logging in.
     *
     * @param codec The packet codec to use.
     * @param profile GameProfile to use.
     * @param accessToken Access token to use, or null if using offline mode.
     */
    public MinecraftProtocol(PacketCodec codec, GameProfile profile, @Nullable String accessToken) {
        this.codec = codec;
        this.handshakeIntent = HandshakeIntent.LOGIN;
        this.profile = profile;
        this.accessToken = accessToken;

        resetStates();
    }

    @Override
    public String getSRVRecordPrefix() {
        return MinecraftConstants.SRV_RECORD_PREFIX;
    }

    @Override
    public PacketHeader getPacketHeader() {
        return MinecraftConstants.PACKET_HEADER;
    }

    @Override
    public void newClientSession(Session session) {
        session.setFlag(MinecraftConstants.PROFILE_KEY, this.profile);
        session.setFlag(MinecraftConstants.ACCESS_TOKEN_KEY, this.accessToken);

        resetStates();

        if (this.useDefaultListeners) {
            session.addListener(new ClientListener(
                this.handshakeIntent == HandshakeIntent.LOGIN
                    && session.getFlag(BuiltinFlags.CLIENT_TRANSFERRING, false) ? HandshakeIntent.TRANSFER : this.handshakeIntent)
            );
        }
    }

    @Override
    public void newServerSession(Server server, Session session) {
        resetStates();

        if (this.useDefaultListeners) {
            if (DEFAULT_NETWORK_CODEC == null) {
                DEFAULT_NETWORK_CODEC = loadNetworkCodec();
            }

            if (DEFAULT_NETWORK_TAGS == null) {
                DEFAULT_NETWORK_TAGS = loadNetworkTags();
            }

            session.addListener(new ServerListener(DEFAULT_NETWORK_CODEC, DEFAULT_NETWORK_TAGS));
        }
    }

    @Override
    public PacketRegistry getInboundPacketRegistry() {
        return Objects.requireNonNull(this.inboundStateRegistry, "packet registries not yet initialised");
    }

    @Override
    public PacketRegistry getOutboundPacketRegistry() {
        return Objects.requireNonNull(this.outboundStateRegistry, "packet registries not yet initialised");
    }

    protected EncryptionConfig createEncryption(Key key) {
        try {
            return new EncryptionConfig(new AESEncryption(key));
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Failed to create protocol encryption.", e);
        }
    }

    /**
     * Resets the protocol states to {@link ProtocolState#HANDSHAKE}.
     */
    public void resetStates() {
        this.setInboundState(ProtocolState.HANDSHAKE);
        this.setOutboundState(ProtocolState.HANDSHAKE);
    }

    /**
     * Gets the current inbound {@link ProtocolState} we're in.
     *
     * @return The current inbound {@link ProtocolState}.
     */
    public @Nullable ProtocolState getInboundState() {
        return this.inboundState;
    }

    /**
     * Gets the current outbound {@link ProtocolState} we're in.
     *
     * @return The current outbound {@link ProtocolState}.
     */
    public @Nullable ProtocolState getOutboundState() {
        return this.outboundState;
    }

    public void setInboundState(ProtocolState state) {
        log.debug("Setting inbound protocol state to: {}", state);

        this.inboundState = state;
        this.inboundStateRegistry = this.codec.getCodec(state);
    }

    public void setOutboundState(ProtocolState state) {
        log.debug("Setting outbound protocol state to: {}", state);

        this.outboundState = state;
        this.outboundStateRegistry = this.codec.getCodec(state);
    }

    public static NbtMap loadNetworkCodec() {
        try (InputStream inputStream = Objects.requireNonNull(MinecraftProtocol.class.getClassLoader().getResourceAsStream("networkCodec.nbt"))) {
            return (NbtMap) NbtUtils.createGZIPReader(inputStream).readTag(512);
        } catch (Exception e) {
            throw new AssertionError("Unable to load network codec.", e);
        }
    }

    public static NbtMap loadNetworkTags() {
        try (InputStream inputStream = Objects.requireNonNull(MinecraftProtocol.class.getClassLoader().getResourceAsStream("networkTags.nbt"))) {
            return (NbtMap) NbtUtils.createGZIPReader(inputStream).readTag(512);
        } catch (Exception e) {
            throw new AssertionError("Unable to load network tags.", e);
        }
    }
}
