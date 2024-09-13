package org.geysermc.mcprotocollib.protocol;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtUtils;
import org.geysermc.mcprotocollib.auth.GameProfile;
import org.geysermc.mcprotocollib.network.Server;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.crypt.AESEncryption;
import org.geysermc.mcprotocollib.network.crypt.EncryptionConfig;
import org.geysermc.mcprotocollib.network.packet.PacketHeader;
import org.geysermc.mcprotocollib.network.packet.PacketProtocol;
import org.geysermc.mcprotocollib.network.packet.PacketRegistry;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodec;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.PacketCodec;
import org.geysermc.mcprotocollib.protocol.data.ProtocolState;

import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Objects;
import java.util.UUID;

/**
 * Implements the Minecraft protocol.
 */
public class MinecraftProtocol extends PacketProtocol {

    /**
     * The network codec sent from the server to the client during {@link ProtocolState#CONFIGURATION}.
     * Lazily loaded once when {@link #newServerSession(Server, Session)} is invoked,
     * if {@link #isUseDefaultListeners()} is true.
     */
    @Nullable
    private static NbtMap DEFAULT_NETWORK_CODEC;

    /**
     * The codec used for the Minecraft protocol.
     */
    @Getter
    private final PacketCodec codec;

    private ProtocolState state;
    private PacketRegistry stateRegistry;

    private final ProtocolState targetState;

    /**
     * The player's identity.
     */
    @Getter
    private GameProfile profile;

    /**
     * Authentication access token.
     */
    @Getter
    private String accessToken;

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
        this.targetState = ProtocolState.STATUS;

        this.setState(ProtocolState.HANDSHAKE);
    }

    /**
     * Constructs a new MinecraftProtocol instance for logging in using offline mode.
     *
     * @param username Username to use.
     */
    public MinecraftProtocol(@NonNull String username) {
        this(new GameProfile(UUID.randomUUID(), username), null);
    }

    /**
     * Constructs a new MinecraftProtocol instance for logging in using offline mode.
     *
     * @param codec The packet codec to use.
     * @param username Username to use.
     */
    public MinecraftProtocol(@NonNull PacketCodec codec, @NonNull String username) {
        this(codec, new GameProfile(UUID.randomUUID(), username), null);
    }

    /**
     * Constructs a new MinecraftProtocol instance for logging in.
     *
     * @param profile GameProfile to use.
     * @param accessToken Access token to use, or null if using offline mode.
     */
    public MinecraftProtocol(@NonNull GameProfile profile, String accessToken) {
        this(MinecraftCodec.CODEC, profile, accessToken);
    }

    /**
     * Constructs a new MinecraftProtocol instance for logging in.
     *
     * @param codec The packet codec to use.
     * @param profile GameProfile to use.
     * @param accessToken Access token to use, or null if using offline mode.
     */
    public MinecraftProtocol(@NonNull PacketCodec codec, @NonNull GameProfile profile, String accessToken) {
        this.codec = codec;
        this.targetState = ProtocolState.LOGIN;
        this.profile = profile;
        this.accessToken = accessToken;

        this.setState(ProtocolState.HANDSHAKE);
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
    public MinecraftCodecHelper createHelper() {
        return this.codec.getHelperFactory().get();
    }

    @Override
    public void newClientSession(Session session, boolean transferring) {
        session.setFlag(MinecraftConstants.PROFILE_KEY, this.profile);
        session.setFlag(MinecraftConstants.ACCESS_TOKEN_KEY, this.accessToken);

        this.setState(ProtocolState.HANDSHAKE);

        if (this.useDefaultListeners) {
            session.addListener(new ClientListener(this.targetState, transferring));
        }
    }

    @Override
    public void newServerSession(Server server, Session session) {
        this.setState(ProtocolState.HANDSHAKE);

        if (this.useDefaultListeners) {
            if (DEFAULT_NETWORK_CODEC == null) {
                DEFAULT_NETWORK_CODEC = loadNetworkCodec();
            }

            session.addListener(new ServerListener(DEFAULT_NETWORK_CODEC));
        }
    }

    @Override
    public PacketRegistry getPacketRegistry() {
        return this.stateRegistry;
    }

    protected EncryptionConfig createEncryption(Key key) {
        try {
            return new EncryptionConfig(new AESEncryption(key));
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Failed to create protocol encryption.", e);
        }
    }

    /**
     * Gets the current {@link ProtocolState} the client is in.
     *
     * @return The current {@link ProtocolState}.
     */
    public ProtocolState getState() {
        return this.state;
    }

    public void setState(ProtocolState state) {
        this.state = state;
        this.stateRegistry = this.codec.getCodec(state);
    }

    public static NbtMap loadNetworkCodec() {
        try (InputStream inputStream = Objects.requireNonNull(MinecraftProtocol.class.getClassLoader().getResourceAsStream("networkCodec.nbt"))) {
            return (NbtMap) NbtUtils.createGZIPReader(inputStream).readTag(512);
        } catch (Exception e) {
            throw new AssertionError("Unable to load network codec.", e);
        }
    }
}
