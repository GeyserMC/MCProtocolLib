package com.github.steveice10.mc.protocol;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.auth.service.AuthenticationService;
import com.github.steveice10.mc.protocol.data.SubProtocol;
import com.github.steveice10.mc.protocol.packet.handshake.client.HandshakePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientKeepAlivePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientLockDifficultyPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientPluginMessagePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientRequestPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientResourcePackStatusPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientSetDifficultyPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientSettingsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientTabCompletePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerAbilitiesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerActionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerChangeHeldItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerInteractEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerMovementPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPlaceBlockPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerStatePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerSwingArmPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerUseItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientAdvancementTabPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientClickWindowButtonPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientCloseWindowPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientCraftingBookStatePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientCreativeInventoryActionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientDisplayedRecipePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientEditBookPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientMoveItemToHotbarPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientPrepareCraftingGridPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientRenameItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientSelectTradePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientSetBeaconEffectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientUpdateCommandBlockMinecartPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientUpdateCommandBlockPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientUpdateJigsawBlockPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientUpdateStructureBlockPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientBlockNBTRequestPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientEntityNBTRequestPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientGenerateStructuresPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientSpectatePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientSteerBoatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientSteerVehiclePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientTeleportConfirmPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientUpdateSignPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientVehicleMovePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerAdvancementTabPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerAdvancementsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerBossBarPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDeclareCommandsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDeclareRecipesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDeclareTagsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDifficultyPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDisconnectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerEntitySoundEffectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerKeepAlivePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPlayerListDataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPluginMessagePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerResourcePackSendPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerRespawnPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerSetCooldownPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerStatisticsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerStopSoundPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerSwitchCameraPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerTabCompletePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerUnlockRecipesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityAnimationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityAttachPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityCollectItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerRemoveEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityEffectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityEquipmentPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityHeadLookPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityMetadataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPropertiesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityRemoveEffectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntitySetPassengersPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityStatusPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityTeleportPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityVelocityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerVehicleMovePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.*;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnExpOrbPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnLivingEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPaintingPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerDisplayScoreboardPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerScoreboardObjectivePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerTeamPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerUpdateScorePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.title.*;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerCloseWindowPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerOpenBookPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerOpenHorseWindowPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerOpenWindowPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerPreparedCraftingGridPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerTradeListPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerWindowItemsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerWindowPropertyPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.*;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.border.*;
import com.github.steveice10.mc.protocol.packet.login.client.EncryptionResponsePacket;
import com.github.steveice10.mc.protocol.packet.login.client.LoginPluginResponsePacket;
import com.github.steveice10.mc.protocol.packet.login.client.LoginStartPacket;
import com.github.steveice10.mc.protocol.packet.login.server.EncryptionRequestPacket;
import com.github.steveice10.mc.protocol.packet.login.server.LoginDisconnectPacket;
import com.github.steveice10.mc.protocol.packet.login.server.LoginPluginRequestPacket;
import com.github.steveice10.mc.protocol.packet.login.server.LoginSetCompressionPacket;
import com.github.steveice10.mc.protocol.packet.login.server.LoginSuccessPacket;
import com.github.steveice10.mc.protocol.packet.status.client.StatusPingPacket;
import com.github.steveice10.mc.protocol.packet.status.client.StatusQueryPacket;
import com.github.steveice10.mc.protocol.packet.status.server.StatusPongPacket;
import com.github.steveice10.mc.protocol.packet.status.server.StatusResponsePacket;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.Server;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.crypt.AESEncryption;
import com.github.steveice10.packetlib.crypt.PacketEncryption;
import com.github.steveice10.packetlib.packet.DefaultPacketHeader;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.packet.PacketHeader;
import com.github.steveice10.packetlib.packet.PacketProtocol;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * Implements the Minecraft protocol.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MinecraftProtocol extends PacketProtocol {
    private SubProtocol subProtocol = SubProtocol.HANDSHAKE;
    private final PacketHeader packetHeader = new DefaultPacketHeader();
    private AESEncryption encryption = null;

    private SubProtocol targetSubProtocol;

    /**
     * The player's identity.
     */
    @Getter
    private GameProfile profile = null;

    /**
     * Authentication access token.
     */
    @Getter
    private String accessToken = "";

    /**
     * Whether to add the default client and server listeners for performing initial login.
     */
    @Getter
    @Setter
    private boolean useDefaultListeners = true;

    /**
     * Constructs a new MinecraftProtocol instance, starting with a specific {@link SubProtocol}.
     * @param subProtocol {@link SubProtocol} to start with.
     */
    public MinecraftProtocol(SubProtocol subProtocol) {
        if(subProtocol != SubProtocol.LOGIN && subProtocol != SubProtocol.STATUS) {
            throw new IllegalArgumentException("Only login and status modes are permitted.");
        }

        this.targetSubProtocol = subProtocol;
        if(subProtocol == SubProtocol.LOGIN) {
            this.profile = new GameProfile((UUID) null, "Player");
        }
    }

    /**
     * Constructs a new MinecraftProtocol instance, adopting the given username.
     * @param username Username to adopt.
     */
    public MinecraftProtocol(String username) {
        this(SubProtocol.LOGIN);
        this.profile = new GameProfile((UUID) null, username);
    }

    /**
     * Constructs a new MinecraftProtocol instance, logging in with the given password credentials.
     * @param username Username to log in as. Must be the same as when the access token was generated.
     * @param password Password to log in with.
     * @throws RequestException If the log in request fails.
     */
    @Deprecated
    public MinecraftProtocol(String username, String password) throws RequestException {
        this(createAuthServiceForPasswordLogin(username, password));
    }

    /**
     * Constructs a new MinecraftProtocol instance, logging in with the given token credentials.
     * @param username Username to log in as. Must be the same as when the access token was generated.
     * @param clientToken Client token to log in as. Must be the same as when the access token was generated.
     * @param accessToken Access token to log in with.
     * @throws RequestException If the log in request fails.
     */
    @Deprecated
    public MinecraftProtocol(String username, String clientToken, String accessToken) throws RequestException {
        this(createAuthServiceForTokenLogin(username, clientToken, accessToken));
    }

    /**
     * Constructs a new MinecraftProtocol instance, copying authentication information
     * from a logged-in {@link AuthenticationService}.
     * @param authService {@link AuthenticationService} to copy from.
     */
    public MinecraftProtocol(AuthenticationService authService) {
        this(authService.getSelectedProfile(), authService.getAccessToken());
    }

    /**
     * Constructs a new MinecraftProtocol from authentication information.
     * @param profile GameProfile to use.
     * @param clientToken Client token to use.
     * @param accessToken Access token to use.
     */
    @Deprecated
    public MinecraftProtocol(GameProfile profile, String clientToken, String accessToken) {
        this(SubProtocol.LOGIN);
        this.profile = profile;
        this.accessToken = accessToken;
    }

    /**
     * Constructs a new MinecraftProtocol from authentication information.
     * @param profile GameProfile to use.
     * @param accessToken Access token to use.
     */
    public MinecraftProtocol(GameProfile profile, String accessToken) {
        this(SubProtocol.LOGIN);
        this.profile = profile;
        this.accessToken = accessToken;
    }

    private static AuthenticationService createAuthServiceForPasswordLogin(String username, String password) throws RequestException {
        AuthenticationService auth = new AuthenticationService(UUID.randomUUID().toString());
        auth.setUsername(username);
        auth.setPassword(password);
        auth.login();
        return auth;
    }

    private static AuthenticationService createAuthServiceForTokenLogin(String username, String clientToken, String accessToken) throws RequestException {
        AuthenticationService auth = new AuthenticationService(clientToken);
        auth.setUsername(username);
        auth.setAccessToken(accessToken);
        auth.login();
        return auth;
    }

    @Override
    public String getSRVRecordPrefix() {
        return "_minecraft";
    }

    @Override
    public PacketHeader getPacketHeader() {
        return this.packetHeader;
    }

    @Override
    public PacketEncryption getEncryption() {
        return this.encryption;
    }

    @Override
    public void newClientSession(Client client, Session session) {
        if(this.profile != null) {
            session.setFlag(MinecraftConstants.PROFILE_KEY, this.profile);
            session.setFlag(MinecraftConstants.ACCESS_TOKEN_KEY, this.accessToken);
        }

        this.setSubProtocol(SubProtocol.HANDSHAKE, true, session);

        if(this.useDefaultListeners) {
            session.addListener(new ClientListener(this.targetSubProtocol));
        }
    }

    @Override
    public void newServerSession(Server server, Session session) {
        this.setSubProtocol(SubProtocol.HANDSHAKE, false, session);

        if(this.useDefaultListeners) {
            session.addListener(new ServerListener());
        }
    }

    protected void enableEncryption(Key key) {
        try {
            this.encryption = new AESEncryption(key);
        } catch(GeneralSecurityException e) {
            throw new Error("Failed to enable protocol encryption.", e);
        }
    }

    /**
     * Gets the current {@link SubProtocol} the client is in.
     * @return The current {@link SubProtocol}.
     */
    public SubProtocol getSubProtocol() {
        return this.subProtocol;
    }

    protected void setSubProtocol(SubProtocol subProtocol, boolean client, Session session) {
        this.clearPackets();
        switch(subProtocol) {
            case HANDSHAKE:
                if(client) {
                    this.initClientHandshake();
                } else {
                    this.initServerHandshake();
                }

                break;
            case LOGIN:
                if(client) {
                    this.initLogin(this::registerIncoming, this::registerOutgoing);
                } else {
                    this.initLogin(this::registerOutgoing, this::registerIncoming);
                }

                break;
            case GAME:
                if(client) {
                    this.initGame(this::registerIncoming, this::registerOutgoing);
                } else {
                    this.initGame(this::registerOutgoing, this::registerIncoming);
                }

                break;
            case STATUS:
                if(client) {
                    this.initStatus(this::registerIncoming, this::registerOutgoing);
                } else {
                    this.initStatus(this::registerOutgoing, this::registerIncoming);
                }

                break;
        }

        this.subProtocol = subProtocol;
    }

    private void initClientHandshake() {
        this.registerOutgoing(0, HandshakePacket.class);
    }

    private void initServerHandshake() {
        this.registerIncoming(0, HandshakePacket.class);
    }

    private void initLogin(BiConsumer<Integer, Class<? extends Packet>> clientboundPackets, BiConsumer<Integer, Class<? extends Packet>> serverboundPackets) {
        clientboundPackets.accept(0x00, LoginDisconnectPacket.class);
        clientboundPackets.accept(0x01, EncryptionRequestPacket.class);
        clientboundPackets.accept(0x02, LoginSuccessPacket.class);
        clientboundPackets.accept(0x03, LoginSetCompressionPacket.class);
        clientboundPackets.accept(0x04, LoginPluginRequestPacket.class);

        serverboundPackets.accept(0x00, LoginStartPacket.class);
        serverboundPackets.accept(0x01, EncryptionResponsePacket.class);
        serverboundPackets.accept(0x02, LoginPluginResponsePacket.class);
    }

    private void initGame(BiConsumer<Integer, Class<? extends Packet>> clientboundPackets, BiConsumer<Integer, Class<? extends Packet>> serverboundPackets) {
        clientboundPackets.accept(0x00, ServerSpawnEntityPacket.class);
        clientboundPackets.accept(0x01, ServerSpawnExpOrbPacket.class);
        clientboundPackets.accept(0x02, ServerSpawnLivingEntityPacket.class);
        clientboundPackets.accept(0x03, ServerSpawnPaintingPacket.class);
        clientboundPackets.accept(0x04, ServerSpawnPlayerPacket.class);
        clientboundPackets.accept(0x05, ServerAddVibrationSignalPacket.class);
        clientboundPackets.accept(0x06, ServerEntityAnimationPacket.class);
        clientboundPackets.accept(0x07, ServerStatisticsPacket.class);
        clientboundPackets.accept(0x08, ServerPlayerActionAckPacket.class);
        clientboundPackets.accept(0x09, ServerBlockBreakAnimPacket.class);
        clientboundPackets.accept(0x0A, ServerUpdateTileEntityPacket.class);
        clientboundPackets.accept(0x0B, ServerBlockValuePacket.class);
        clientboundPackets.accept(0x0C, ServerBlockChangePacket.class);
        clientboundPackets.accept(0x0D, ServerBossBarPacket.class);
        clientboundPackets.accept(0x0E, ServerDifficultyPacket.class);
        clientboundPackets.accept(0x0F, ServerChatPacket.class);
        clientboundPackets.accept(0x10, ServerClearTitlesPacket.class);
        clientboundPackets.accept(0x11, ServerTabCompletePacket.class);
        clientboundPackets.accept(0x12, ServerDeclareCommandsPacket.class);
        clientboundPackets.accept(0x13, ServerCloseWindowPacket.class);
        clientboundPackets.accept(0x14, ServerWindowItemsPacket.class);
        clientboundPackets.accept(0x15, ServerWindowPropertyPacket.class);
        clientboundPackets.accept(0x16, ServerSetSlotPacket.class);
        clientboundPackets.accept(0x17, ServerSetCooldownPacket.class);
        clientboundPackets.accept(0x18, ServerPluginMessagePacket.class);
        clientboundPackets.accept(0x19, ServerPlaySoundPacket.class);
        clientboundPackets.accept(0x1A, ServerDisconnectPacket.class);
        clientboundPackets.accept(0x1B, ServerEntityStatusPacket.class);
        clientboundPackets.accept(0x1C, ServerExplosionPacket.class);
        clientboundPackets.accept(0x1D, ServerUnloadChunkPacket.class);
        clientboundPackets.accept(0x1E, ServerNotifyClientPacket.class);
        clientboundPackets.accept(0x1F, ServerOpenHorseWindowPacket.class);
        clientboundPackets.accept(0x20, ServerInitializeBorderPacket.class);
        clientboundPackets.accept(0x21, ServerKeepAlivePacket.class);
        clientboundPackets.accept(0x22, ServerChunkDataPacket.class);
        clientboundPackets.accept(0x23, ServerPlayEffectPacket.class);
        clientboundPackets.accept(0x24, ServerSpawnParticlePacket.class);
        clientboundPackets.accept(0x25, ServerUpdateLightPacket.class);
        clientboundPackets.accept(0x26, ServerJoinGamePacket.class);
        clientboundPackets.accept(0x27, ServerMapDataPacket.class);
        clientboundPackets.accept(0x28, ServerTradeListPacket.class);
        clientboundPackets.accept(0x29, ServerEntityPositionPacket.class);
        clientboundPackets.accept(0x2A, ServerEntityPositionRotationPacket.class);
        clientboundPackets.accept(0x2B, ServerEntityRotationPacket.class);
        clientboundPackets.accept(0x2C, ServerVehicleMovePacket.class);
        clientboundPackets.accept(0x2D, ServerOpenBookPacket.class);
        clientboundPackets.accept(0x2E, ServerOpenWindowPacket.class);
        clientboundPackets.accept(0x2F, ServerOpenTileEntityEditorPacket.class);
        clientboundPackets.accept(0x30, ServerPreparedCraftingGridPacket.class);
        clientboundPackets.accept(0x31, ServerPlayerAbilitiesPacket.class);
        clientboundPackets.accept(0x32, ServerPlayerCombatEndPacket.class);
        clientboundPackets.accept(0x33, ServerPlayerCombatEnterPacket.class);
        clientboundPackets.accept(0x34, ServerPlayerCombatKillPacket.class);
        clientboundPackets.accept(0x35, ServerPlayerListEntryPacket.class);
        clientboundPackets.accept(0x36, ServerPlayerFacingPacket.class);
        clientboundPackets.accept(0x37, ServerPlayerPositionRotationPacket.class);
        clientboundPackets.accept(0x38, ServerUnlockRecipesPacket.class);
        clientboundPackets.accept(0x39, ServerRemoveEntityPacket.class);
        clientboundPackets.accept(0x3A, ServerEntityRemoveEffectPacket.class);
        clientboundPackets.accept(0x3B, ServerResourcePackSendPacket.class);
        clientboundPackets.accept(0x3C, ServerRespawnPacket.class);
        clientboundPackets.accept(0x3D, ServerEntityHeadLookPacket.class);
        clientboundPackets.accept(0x3E, ServerMultiBlockChangePacket.class);
        clientboundPackets.accept(0x3F, ServerAdvancementTabPacket.class);
        clientboundPackets.accept(0x40, ServerSetActionBarTextPacket.class);
        clientboundPackets.accept(0x41, ServerSetBorderCenterPacket.class);
        clientboundPackets.accept(0x42, ServerSetBorderLerpSizePacket.class);
        clientboundPackets.accept(0x43, ServerSetBorderSizePacket.class);
        clientboundPackets.accept(0x44, ServerSetBorderWarningDelayPacket.class);
        clientboundPackets.accept(0x45, ServerSetBorderWarningDistancePacket.class);
        clientboundPackets.accept(0x46, ServerSwitchCameraPacket.class);
        clientboundPackets.accept(0x47, ServerPlayerChangeHeldItemPacket.class);
        clientboundPackets.accept(0x48, ServerUpdateViewPositionPacket.class);
        clientboundPackets.accept(0x49, ServerUpdateViewDistancePacket.class);
        clientboundPackets.accept(0x4A, ServerSpawnPositionPacket.class);
        clientboundPackets.accept(0x4B, ServerDisplayScoreboardPacket.class);
        clientboundPackets.accept(0x4C, ServerEntityMetadataPacket.class);
        clientboundPackets.accept(0x4D, ServerEntityAttachPacket.class);
        clientboundPackets.accept(0x4E, ServerEntityVelocityPacket.class);
        clientboundPackets.accept(0x4F, ServerEntityEquipmentPacket.class);
        clientboundPackets.accept(0x50, ServerPlayerSetExperiencePacket.class);
        clientboundPackets.accept(0x51, ServerPlayerHealthPacket.class);
        clientboundPackets.accept(0x52, ServerScoreboardObjectivePacket.class);
        clientboundPackets.accept(0x53, ServerEntitySetPassengersPacket.class);
        clientboundPackets.accept(0x54, ServerTeamPacket.class);
        clientboundPackets.accept(0x55, ServerUpdateScorePacket.class);
        clientboundPackets.accept(0x56, ServerSetSubtitleTextPacket.class);
        clientboundPackets.accept(0x57, ServerUpdateTimePacket.class);
        clientboundPackets.accept(0x58, ServerSetTitleTextPacket.class);
        clientboundPackets.accept(0x59, ServerSetTitlesAnimationPacket.class);
        clientboundPackets.accept(0x5A, ServerEntitySoundEffectPacket.class);
        clientboundPackets.accept(0x5B, ServerPlayBuiltinSoundPacket.class);
        clientboundPackets.accept(0x5C, ServerStopSoundPacket.class);
        clientboundPackets.accept(0x5D, ServerPlayerListDataPacket.class);
        clientboundPackets.accept(0x5E, ServerNBTResponsePacket.class);
        clientboundPackets.accept(0x5F, ServerEntityCollectItemPacket.class);
        clientboundPackets.accept(0x60, ServerEntityTeleportPacket.class);
        clientboundPackets.accept(0x61, ServerAdvancementsPacket.class);
        clientboundPackets.accept(0x62, ServerEntityPropertiesPacket.class);
        clientboundPackets.accept(0x63, ServerEntityEffectPacket.class);
        clientboundPackets.accept(0x64, ServerDeclareRecipesPacket.class);
        clientboundPackets.accept(0x65, ServerDeclareTagsPacket.class);

        serverboundPackets.accept(0x00, ClientTeleportConfirmPacket.class);
        serverboundPackets.accept(0x01, ClientBlockNBTRequestPacket.class);
        serverboundPackets.accept(0x02, ClientSetDifficultyPacket.class);
        serverboundPackets.accept(0x03, ClientChatPacket.class);
        serverboundPackets.accept(0x04, ClientRequestPacket.class);
        serverboundPackets.accept(0x05, ClientSettingsPacket.class);
        serverboundPackets.accept(0x06, ClientTabCompletePacket.class);
        serverboundPackets.accept(0x07, ClientClickWindowButtonPacket.class);
        serverboundPackets.accept(0x08, ClientWindowActionPacket.class);
        serverboundPackets.accept(0x09, ClientCloseWindowPacket.class);
        serverboundPackets.accept(0x0A, ClientPluginMessagePacket.class);
        serverboundPackets.accept(0x0B, ClientEditBookPacket.class);
        serverboundPackets.accept(0x0C, ClientEntityNBTRequestPacket.class);
        serverboundPackets.accept(0x0D, ClientPlayerInteractEntityPacket.class);
        serverboundPackets.accept(0x0E, ClientGenerateStructuresPacket.class);
        serverboundPackets.accept(0x0F, ClientKeepAlivePacket.class);
        serverboundPackets.accept(0x10, ClientLockDifficultyPacket.class);
        serverboundPackets.accept(0x11, ClientPlayerPositionPacket.class);
        serverboundPackets.accept(0x12, ClientPlayerPositionRotationPacket.class);
        serverboundPackets.accept(0x13, ClientPlayerRotationPacket.class);
        serverboundPackets.accept(0x14, ClientPlayerMovementPacket.class);
        serverboundPackets.accept(0x15, ClientVehicleMovePacket.class);
        serverboundPackets.accept(0x16, ClientSteerBoatPacket.class);
        serverboundPackets.accept(0x17, ClientMoveItemToHotbarPacket.class);
        serverboundPackets.accept(0x18, ClientPrepareCraftingGridPacket.class);
        serverboundPackets.accept(0x19, ClientPlayerAbilitiesPacket.class);
        serverboundPackets.accept(0x1A, ClientPlayerActionPacket.class);
        serverboundPackets.accept(0x1B, ClientPlayerStatePacket.class);
        serverboundPackets.accept(0x1C, ClientSteerVehiclePacket.class);
        serverboundPackets.accept(0x1D, ClientDisplayedRecipePacket.class);
        serverboundPackets.accept(0x1E, ClientCraftingBookStatePacket.class);
        serverboundPackets.accept(0x1F, ClientRenameItemPacket.class);
        serverboundPackets.accept(0x20, ClientResourcePackStatusPacket.class);
        serverboundPackets.accept(0x21, ClientAdvancementTabPacket.class);
        serverboundPackets.accept(0x22, ClientSelectTradePacket.class);
        serverboundPackets.accept(0x23, ClientSetBeaconEffectPacket.class);
        serverboundPackets.accept(0x24, ClientPlayerChangeHeldItemPacket.class);
        serverboundPackets.accept(0x25, ClientUpdateCommandBlockPacket.class);
        serverboundPackets.accept(0x26, ClientUpdateCommandBlockMinecartPacket.class);
        serverboundPackets.accept(0x27, ClientCreativeInventoryActionPacket.class);
        serverboundPackets.accept(0x28, ClientUpdateJigsawBlockPacket.class);
        serverboundPackets.accept(0x29, ClientUpdateStructureBlockPacket.class);
        serverboundPackets.accept(0x2A, ClientUpdateSignPacket.class);
        serverboundPackets.accept(0x2B, ClientPlayerSwingArmPacket.class);
        serverboundPackets.accept(0x2C, ClientSpectatePacket.class);
        serverboundPackets.accept(0x2D, ClientPlayerPlaceBlockPacket.class);
        serverboundPackets.accept(0x2E, ClientPlayerUseItemPacket.class);
    }

    private void initStatus(BiConsumer<Integer, Class<? extends Packet>> clientboundPackets, BiConsumer<Integer, Class<? extends Packet>> serverboundPackets) {
        clientboundPackets.accept(0x00, StatusResponsePacket.class);
        clientboundPackets.accept(0x01, StatusPongPacket.class);

        serverboundPackets.accept(0x00, StatusQueryPacket.class);
        serverboundPackets.accept(0x01, StatusPingPacket.class);
    }
}
