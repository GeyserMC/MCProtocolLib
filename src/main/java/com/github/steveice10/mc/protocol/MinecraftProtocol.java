package com.github.steveice10.mc.protocol;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.data.SubProtocol;
import com.github.steveice10.mc.protocol.packet.handshake.serverbound.ClientIntentionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundAwardStatsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundBossEventPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundChangeDifficultyPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundCommandSuggestionsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundCommandsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundCooldownPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundCustomPayloadPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundDisconnectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundKeepAlivePacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundLoginPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundPingPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundPlayerInfoPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundRecipePacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundResourcePackPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundRespawnPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundSelectAdvancementsTabPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundSetCameraPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundSoundEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundStopSoundPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundTabListPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundUpdateAdvancementsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundUpdateRecipesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundUpdateTagsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.ClientboundAnimatePacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.ClientboundEntityEventPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.ClientboundMoveEntityPosPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.ClientboundMoveEntityPosRotPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.ClientboundMoveEntityRotPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.ClientboundMoveVehiclePacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.ClientboundRemoveEntitiesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.ClientboundRemoveMobEffectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.ClientboundRotateHeadPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.ClientboundSetEntityDataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.ClientboundSetEntityLinkPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.ClientboundSetEntityMotionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.ClientboundSetEquipmentPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.ClientboundSetPassengersPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.ClientboundTakeItemEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.ClientboundTeleportEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.ClientboundUpdateAttributesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.ClientboundUpdateMobEffectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.player.ClientboundBlockBreakAckPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.player.ClientboundPlayerAbilitiesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.player.ClientboundPlayerCombatEndPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.player.ClientboundPlayerCombatEnterPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.player.ClientboundPlayerCombatKillPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.player.ClientboundPlayerLookAtPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.player.ClientboundPlayerPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.player.ClientboundSetCarriedItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.player.ClientboundSetExperiencePacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.player.ClientboundSetHealthPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.spawn.ClientboundAddEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.spawn.ClientboundAddExperienceOrbPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.spawn.ClientboundAddMobPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.spawn.ClientboundAddPaintingPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.spawn.ClientboundAddPlayerPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.ClientboundAddVibrationSignalPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.ClientboundBlockDestructionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.ClientboundBlockEntityDataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.ClientboundBlockEventPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.ClientboundBlockUpdatePacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.ClientboundCustomSoundPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.ClientboundExplodePacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.ClientboundForgetLevelChunkPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.ClientboundGameEventPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.ClientboundLevelChunkPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.ClientboundLevelEventPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.ClientboundLevelParticlesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.ClientboundLightUpdatePacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.ClientboundMapItemDataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.ClientboundOpenSignEditorPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.ClientboundSectionBlocksUpdatePacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.ClientboundSetChunkCacheCenterPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.ClientboundSetChunkCacheRadiusPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.ClientboundSetDefaultSpawnPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.ClientboundSetTimePacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.ClientboundSoundPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.ClientboundTagQueryPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.border.ClientboundInitializeBorderPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.border.ClientboundSetBorderCenterPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.border.ClientboundSetBorderLerpSizePacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.border.ClientboundSetBorderSizePacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.border.ClientboundSetBorderWarningDelayPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.border.ClientboundSetBorderWarningDistancePacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.scoreboard.ClientboundSetDisplayObjectivePacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.scoreboard.ClientboundSetObjectivePacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.scoreboard.ClientboundSetPlayerTeamPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.scoreboard.ClientboundSetScorePacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.title.ClientboundClearTitlesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.title.ClientboundSetActionBarTextPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.title.ClientboundSetSubtitleTextPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.title.ClientboundSetTitleTextPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.title.ClientboundSetTitlesAnimationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.window.ClientboundContainerClosePacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.window.ClientboundContainerSetContentPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.window.ClientboundContainerSetDataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.window.ClientboundContainerSetSlotPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.window.ClientboundHorseScreenOpenPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.window.ClientboundMerchantOffersPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.window.ClientboundOpenBookPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.window.ClientboundOpenScreenPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.window.ClientboundPlaceGhostRecipePacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundChangeDifficultyPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundClientCommandPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundClientInformationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundCommandSuggestionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundCustomPayloadPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundKeepAlivePacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundLockDifficultyPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundPongPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.ServerboundResourcePackPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.level.ServerboundAcceptTeleportationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.level.ServerboundBlockEntityTagQuery;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.level.ServerboundEntityTagQuery;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.level.ServerboundJigsawGeneratePacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.level.ServerboundMoveVehiclePacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.level.ServerboundPaddleBoatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.level.ServerboundPlayerInputPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.level.ServerboundSignUpdatePacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.level.ServerboundTeleportToEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.player.ServerboundInteractPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.player.ServerboundMovePlayerPosPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.player.ServerboundMovePlayerPosRotPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.player.ServerboundMovePlayerRotPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.player.ServerboundMovePlayerStatusOnlyPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.player.ServerboundPlayerAbilitiesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.player.ServerboundPlayerActionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.player.ServerboundPlayerCommandPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.player.ServerboundSetCarriedItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.player.ServerboundSwingPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.player.ServerboundUseItemOnPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.player.ServerboundUseItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.window.ServerboundContainerButtonClickPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.window.ServerboundContainerClickPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.window.ServerboundContainerClosePacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.window.ServerboundEditBookPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.window.ServerboundPickItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.window.ServerboundPlaceRecipePacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.window.ServerboundRecipeBookChangeSettingsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.window.ServerboundRecipeBookSeenRecipePacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.window.ServerboundRenameItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.window.ServerboundSeenAdvancementsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.window.ServerboundSelectTradePacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.window.ServerboundSetBeaconPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.window.ServerboundSetCommandBlockPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.window.ServerboundSetCommandMinecartPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.window.ServerboundSetCreativeModeSlotPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.window.ServerboundSetJigsawBlockPacket;
import com.github.steveice10.mc.protocol.packet.ingame.serverbound.window.ServerboundSetStructureBlockPacket;
import com.github.steveice10.mc.protocol.packet.login.clientbound.ClientboundCustomQueryPacket;
import com.github.steveice10.mc.protocol.packet.login.clientbound.ClientboundGameProfilePacket;
import com.github.steveice10.mc.protocol.packet.login.clientbound.ClientboundHelloPacket;
import com.github.steveice10.mc.protocol.packet.login.clientbound.ClientboundLoginCompressionPacket;
import com.github.steveice10.mc.protocol.packet.login.clientbound.ClientboundLoginDisconnectPacket;
import com.github.steveice10.mc.protocol.packet.login.serverbound.ServerboundCustomQueryPacket;
import com.github.steveice10.mc.protocol.packet.login.serverbound.ServerboundHelloPacket;
import com.github.steveice10.mc.protocol.packet.login.serverbound.ServerboundKeyPacket;
import com.github.steveice10.mc.protocol.packet.status.clientbound.ClientboundPongResponsePacket;
import com.github.steveice10.mc.protocol.packet.status.clientbound.ClientboundStatusResponsePacket;
import com.github.steveice10.mc.protocol.packet.status.serverbound.ServerboundPingRequestPacket;
import com.github.steveice10.mc.protocol.packet.status.serverbound.ServerboundStatusRequestPacket;
import com.github.steveice10.packetlib.Server;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.crypt.AESEncryption;
import com.github.steveice10.packetlib.crypt.PacketEncryption;
import com.github.steveice10.packetlib.packet.DefaultPacketHeader;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.packet.PacketHeader;
import com.github.steveice10.packetlib.packet.PacketProtocol;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * Implements the Minecraft protocol.
 */
public class MinecraftProtocol extends PacketProtocol {
    private SubProtocol subProtocol = SubProtocol.HANDSHAKE;
    private final PacketHeader packetHeader = new DefaultPacketHeader();
    private AESEncryption encryption;

    private SubProtocol targetSubProtocol;

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
        this.targetSubProtocol = SubProtocol.STATUS;
    }

    /**
     * Constructs a new MinecraftProtocol instance for logging in using offline mode.
     *
     * @param username Username to use.
     */
    public MinecraftProtocol(@NonNull String username) {
        this(new GameProfile((UUID) null, username), null);
    }

    /**
     * Constructs a new MinecraftProtocol instance for logging in.
     *
     * @param profile     GameProfile to use.
     * @param accessToken Access token to use, or null if using offline mode.
     */
    public MinecraftProtocol(@NonNull GameProfile profile, String accessToken) {
        this.targetSubProtocol = SubProtocol.LOGIN;
        this.profile = profile;
        this.accessToken = accessToken;
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
    public void newClientSession(Session session) {
        session.setFlag(MinecraftConstants.PROFILE_KEY, this.profile);
        session.setFlag(MinecraftConstants.ACCESS_TOKEN_KEY, this.accessToken);

        this.setSubProtocol(SubProtocol.HANDSHAKE, true, session);

        if (this.useDefaultListeners) {
            session.addListener(new ClientListener(this.targetSubProtocol));
        }
    }

    @Override
    public void newServerSession(Server server, Session session) {
        this.setSubProtocol(SubProtocol.HANDSHAKE, false, session);

        if (this.useDefaultListeners) {
            session.addListener(new ServerListener());
        }
    }

    protected void enableEncryption(Key key) {
        try {
            this.encryption = new AESEncryption(key);
        } catch (GeneralSecurityException e) {
            throw new Error("Failed to enable protocol encryption.", e);
        }
    }

    /**
     * Gets the current {@link SubProtocol} the client is in.
     *
     * @return The current {@link SubProtocol}.
     */
    public SubProtocol getSubProtocol() {
        return this.subProtocol;
    }

    protected void setSubProtocol(SubProtocol subProtocol, boolean client, Session session) {
        this.clearPackets();
        switch (subProtocol) {
            case HANDSHAKE:
                if (client) {
                    this.initClientHandshake();
                } else {
                    this.initServerHandshake();
                }

                break;
            case LOGIN:
                if (client) {
                    this.initLogin(this::registerIncoming, this::registerOutgoing);
                } else {
                    this.initLogin(this::registerOutgoing, this::registerIncoming);
                }

                break;
            case GAME:
                if (client) {
                    this.initGame(this::registerIncoming, this::registerOutgoing);
                } else {
                    this.initGame(this::registerOutgoing, this::registerIncoming);
                }

                break;
            case STATUS:
                if (client) {
                    this.initStatus(this::registerIncoming, this::registerOutgoing);
                } else {
                    this.initStatus(this::registerOutgoing, this::registerIncoming);
                }

                break;
        }

        this.subProtocol = subProtocol;
    }

    private void initClientHandshake() {
        this.registerOutgoing(0, ClientIntentionPacket.class);
    }

    private void initServerHandshake() {
        this.registerIncoming(0, ClientIntentionPacket.class);
    }

    private void initLogin(BiConsumer<Integer, Class<? extends Packet>> clientboundPackets, BiConsumer<Integer, Class<? extends Packet>> serverboundPackets) {
        clientboundPackets.accept(0x00, ClientboundLoginDisconnectPacket.class);
        clientboundPackets.accept(0x01, ClientboundHelloPacket.class);
        clientboundPackets.accept(0x02, ClientboundGameProfilePacket.class);
        clientboundPackets.accept(0x03, ClientboundLoginCompressionPacket.class);
        clientboundPackets.accept(0x04, ClientboundCustomQueryPacket.class);

        serverboundPackets.accept(0x00, ServerboundHelloPacket.class);
        serverboundPackets.accept(0x01, ServerboundKeyPacket.class);
        serverboundPackets.accept(0x02, ServerboundCustomQueryPacket.class);
    }

    private void initGame(BiConsumer<Integer, Class<? extends Packet>> clientboundPackets, BiConsumer<Integer, Class<? extends Packet>> serverboundPackets) {
        clientboundPackets.accept(0x00, ClientboundAddEntityPacket.class);
        clientboundPackets.accept(0x01, ClientboundAddExperienceOrbPacket.class);
        clientboundPackets.accept(0x02, ClientboundAddMobPacket.class);
        clientboundPackets.accept(0x03, ClientboundAddPaintingPacket.class);
        clientboundPackets.accept(0x04, ClientboundAddPlayerPacket.class);
        clientboundPackets.accept(0x05, ClientboundAddVibrationSignalPacket.class);
        clientboundPackets.accept(0x06, ClientboundAnimatePacket.class);
        clientboundPackets.accept(0x07, ClientboundAwardStatsPacket.class);
        clientboundPackets.accept(0x08, ClientboundBlockBreakAckPacket.class);
        clientboundPackets.accept(0x09, ClientboundBlockDestructionPacket.class);
        clientboundPackets.accept(0x0A, ClientboundBlockEntityDataPacket.class);
        clientboundPackets.accept(0x0B, ClientboundBlockEventPacket.class);
        clientboundPackets.accept(0x0C, ClientboundBlockUpdatePacket.class);
        clientboundPackets.accept(0x0D, ClientboundBossEventPacket.class);
        clientboundPackets.accept(0x0E, ClientboundChangeDifficultyPacket.class);
        clientboundPackets.accept(0x0F, ClientboundChatPacket.class);
        clientboundPackets.accept(0x10, ClientboundClearTitlesPacket.class);
        clientboundPackets.accept(0x11, ClientboundCommandSuggestionsPacket.class);
        clientboundPackets.accept(0x12, ClientboundCommandsPacket.class);
        clientboundPackets.accept(0x13, ClientboundContainerClosePacket.class);
        clientboundPackets.accept(0x14, ClientboundContainerSetContentPacket.class);
        clientboundPackets.accept(0x15, ClientboundContainerSetDataPacket.class);
        clientboundPackets.accept(0x16, ClientboundContainerSetSlotPacket.class);
        clientboundPackets.accept(0x17, ClientboundCooldownPacket.class);
        clientboundPackets.accept(0x18, ClientboundCustomPayloadPacket.class);
        clientboundPackets.accept(0x19, ClientboundCustomSoundPacket.class);
        clientboundPackets.accept(0x1A, ClientboundDisconnectPacket.class);
        clientboundPackets.accept(0x1B, ClientboundEntityEventPacket.class);
        clientboundPackets.accept(0x1C, ClientboundExplodePacket.class);
        clientboundPackets.accept(0x1D, ClientboundForgetLevelChunkPacket.class);
        clientboundPackets.accept(0x1E, ClientboundGameEventPacket.class);
        clientboundPackets.accept(0x1F, ClientboundHorseScreenOpenPacket.class);
        clientboundPackets.accept(0x20, ClientboundInitializeBorderPacket.class);
        clientboundPackets.accept(0x21, ClientboundKeepAlivePacket.class);
        clientboundPackets.accept(0x22, ClientboundLevelChunkPacket.class);
        clientboundPackets.accept(0x23, ClientboundLevelEventPacket.class);
        clientboundPackets.accept(0x24, ClientboundLevelParticlesPacket.class);
        clientboundPackets.accept(0x25, ClientboundLightUpdatePacket.class);
        clientboundPackets.accept(0x26, ClientboundLoginPacket.class);
        clientboundPackets.accept(0x27, ClientboundMapItemDataPacket.class);
        clientboundPackets.accept(0x28, ClientboundMerchantOffersPacket.class);
        clientboundPackets.accept(0x29, ClientboundMoveEntityPosPacket.class);
        clientboundPackets.accept(0x2A, ClientboundMoveEntityPosRotPacket.class);
        clientboundPackets.accept(0x2B, ClientboundMoveEntityRotPacket.class);
        clientboundPackets.accept(0x2C, ClientboundMoveVehiclePacket.class);
        clientboundPackets.accept(0x2D, ClientboundOpenBookPacket.class);
        clientboundPackets.accept(0x2E, ClientboundOpenScreenPacket.class);
        clientboundPackets.accept(0x2F, ClientboundOpenSignEditorPacket.class);
        clientboundPackets.accept(0x30, ClientboundPingPacket.class);
        clientboundPackets.accept(0x31, ClientboundPlaceGhostRecipePacket.class);
        clientboundPackets.accept(0x32, ClientboundPlayerAbilitiesPacket.class);
        clientboundPackets.accept(0x33, ClientboundPlayerCombatEndPacket.class);
        clientboundPackets.accept(0x34, ClientboundPlayerCombatEnterPacket.class);
        clientboundPackets.accept(0x35, ClientboundPlayerCombatKillPacket.class);
        clientboundPackets.accept(0x36, ClientboundPlayerInfoPacket.class);
        clientboundPackets.accept(0x37, ClientboundPlayerLookAtPacket.class);
        clientboundPackets.accept(0x38, ClientboundPlayerPositionPacket.class);
        clientboundPackets.accept(0x39, ClientboundRecipePacket.class);
        clientboundPackets.accept(0x3A, ClientboundRemoveEntitiesPacket.class);
        clientboundPackets.accept(0x3B, ClientboundRemoveMobEffectPacket.class);
        clientboundPackets.accept(0x3C, ClientboundResourcePackPacket.class);
        clientboundPackets.accept(0x3D, ClientboundRespawnPacket.class);
        clientboundPackets.accept(0x3E, ClientboundRotateHeadPacket.class);
        clientboundPackets.accept(0x3F, ClientboundSectionBlocksUpdatePacket.class);
        clientboundPackets.accept(0x40, ClientboundSelectAdvancementsTabPacket.class);
        clientboundPackets.accept(0x41, ClientboundSetActionBarTextPacket.class);
        clientboundPackets.accept(0x42, ClientboundSetBorderCenterPacket.class);
        clientboundPackets.accept(0x43, ClientboundSetBorderLerpSizePacket.class);
        clientboundPackets.accept(0x44, ClientboundSetBorderSizePacket.class);
        clientboundPackets.accept(0x45, ClientboundSetBorderWarningDelayPacket.class);
        clientboundPackets.accept(0x46, ClientboundSetBorderWarningDistancePacket.class);
        clientboundPackets.accept(0x47, ClientboundSetCameraPacket.class);
        clientboundPackets.accept(0x48, ClientboundSetCarriedItemPacket.class);
        clientboundPackets.accept(0x49, ClientboundSetChunkCacheCenterPacket.class);
        clientboundPackets.accept(0x4A, ClientboundSetChunkCacheRadiusPacket.class);
        clientboundPackets.accept(0x4B, ClientboundSetDefaultSpawnPositionPacket.class);
        clientboundPackets.accept(0x4C, ClientboundSetDisplayObjectivePacket.class);
        clientboundPackets.accept(0x4D, ClientboundSetEntityDataPacket.class);
        clientboundPackets.accept(0x4E, ClientboundSetEntityLinkPacket.class);
        clientboundPackets.accept(0x4F, ClientboundSetEntityMotionPacket.class);
        clientboundPackets.accept(0x50, ClientboundSetEquipmentPacket.class);
        clientboundPackets.accept(0x51, ClientboundSetExperiencePacket.class);
        clientboundPackets.accept(0x52, ClientboundSetHealthPacket.class);
        clientboundPackets.accept(0x53, ClientboundSetObjectivePacket.class);
        clientboundPackets.accept(0x54, ClientboundSetPassengersPacket.class);
        clientboundPackets.accept(0x55, ClientboundSetPlayerTeamPacket.class);
        clientboundPackets.accept(0x56, ClientboundSetScorePacket.class);
        clientboundPackets.accept(0x57, ClientboundSetSubtitleTextPacket.class);
        clientboundPackets.accept(0x58, ClientboundSetTimePacket.class);
        clientboundPackets.accept(0x59, ClientboundSetTitleTextPacket.class);
        clientboundPackets.accept(0x5A, ClientboundSetTitlesAnimationPacket.class);
        clientboundPackets.accept(0x5B, ClientboundSoundEntityPacket.class);
        clientboundPackets.accept(0x5C, ClientboundSoundPacket.class);
        clientboundPackets.accept(0x5D, ClientboundStopSoundPacket.class);
        clientboundPackets.accept(0x5E, ClientboundTabListPacket.class);
        clientboundPackets.accept(0x5F, ClientboundTagQueryPacket.class);
        clientboundPackets.accept(0x60, ClientboundTakeItemEntityPacket.class);
        clientboundPackets.accept(0x61, ClientboundTeleportEntityPacket.class);
        clientboundPackets.accept(0x62, ClientboundUpdateAdvancementsPacket.class);
        clientboundPackets.accept(0x63, ClientboundUpdateAttributesPacket.class);
        clientboundPackets.accept(0x64, ClientboundUpdateMobEffectPacket.class);
        clientboundPackets.accept(0x65, ClientboundUpdateRecipesPacket.class);
        clientboundPackets.accept(0x66, ClientboundUpdateTagsPacket.class);

        serverboundPackets.accept(0x00, ServerboundAcceptTeleportationPacket.class);
        serverboundPackets.accept(0x01, ServerboundBlockEntityTagQuery.class);
        serverboundPackets.accept(0x02, ServerboundChangeDifficultyPacket.class);
        serverboundPackets.accept(0x03, ServerboundChatPacket.class);
        serverboundPackets.accept(0x04, ServerboundClientCommandPacket.class);
        serverboundPackets.accept(0x05, ServerboundClientInformationPacket.class);
        serverboundPackets.accept(0x06, ServerboundCommandSuggestionPacket.class);
        serverboundPackets.accept(0x07, ServerboundContainerButtonClickPacket.class);
        serverboundPackets.accept(0x08, ServerboundContainerClickPacket.class);
        serverboundPackets.accept(0x09, ServerboundContainerClosePacket.class);
        serverboundPackets.accept(0x0A, ServerboundCustomPayloadPacket.class);
        serverboundPackets.accept(0x0B, ServerboundEditBookPacket.class);
        serverboundPackets.accept(0x0C, ServerboundEntityTagQuery.class);
        serverboundPackets.accept(0x0D, ServerboundInteractPacket.class);
        serverboundPackets.accept(0x0E, ServerboundJigsawGeneratePacket.class);
        serverboundPackets.accept(0x0F, ServerboundKeepAlivePacket.class);
        serverboundPackets.accept(0x10, ServerboundLockDifficultyPacket.class);
        serverboundPackets.accept(0x11, ServerboundMovePlayerPosPacket.class);
        serverboundPackets.accept(0x12, ServerboundMovePlayerPosRotPacket.class);
        serverboundPackets.accept(0x13, ServerboundMovePlayerRotPacket.class);
        serverboundPackets.accept(0x14, ServerboundMovePlayerStatusOnlyPacket.class);
        serverboundPackets.accept(0x15, ServerboundMoveVehiclePacket.class);
        serverboundPackets.accept(0x16, ServerboundPaddleBoatPacket.class);
        serverboundPackets.accept(0x17, ServerboundPickItemPacket.class);
        serverboundPackets.accept(0x18, ServerboundPlaceRecipePacket.class);
        serverboundPackets.accept(0x19, ServerboundPlayerAbilitiesPacket.class);
        serverboundPackets.accept(0x1A, ServerboundPlayerActionPacket.class);
        serverboundPackets.accept(0x1B, ServerboundPlayerCommandPacket.class);
        serverboundPackets.accept(0x1C, ServerboundPlayerInputPacket.class);
        serverboundPackets.accept(0x1D, ServerboundPongPacket.class);
        serverboundPackets.accept(0x1E, ServerboundRecipeBookChangeSettingsPacket.class);
        serverboundPackets.accept(0x1F, ServerboundRecipeBookSeenRecipePacket.class);
        serverboundPackets.accept(0x20, ServerboundRenameItemPacket.class);
        serverboundPackets.accept(0x21, ServerboundResourcePackPacket.class);
        serverboundPackets.accept(0x22, ServerboundSeenAdvancementsPacket.class);
        serverboundPackets.accept(0x23, ServerboundSelectTradePacket.class);
        serverboundPackets.accept(0x24, ServerboundSetBeaconPacket.class);
        serverboundPackets.accept(0x25, ServerboundSetCarriedItemPacket.class);
        serverboundPackets.accept(0x26, ServerboundSetCommandBlockPacket.class);
        serverboundPackets.accept(0x27, ServerboundSetCommandMinecartPacket.class);
        serverboundPackets.accept(0x28, ServerboundSetCreativeModeSlotPacket.class);
        serverboundPackets.accept(0x29, ServerboundSetJigsawBlockPacket.class);
        serverboundPackets.accept(0x2A, ServerboundSetStructureBlockPacket.class);
        serverboundPackets.accept(0x2B, ServerboundSignUpdatePacket.class);
        serverboundPackets.accept(0x2C, ServerboundSwingPacket.class);
        serverboundPackets.accept(0x2D, ServerboundTeleportToEntityPacket.class);
        serverboundPackets.accept(0x2E, ServerboundUseItemOnPacket.class);
        serverboundPackets.accept(0x2F, ServerboundUseItemPacket.class);
    }

    private void initStatus(BiConsumer<Integer, Class<? extends Packet>> clientboundPackets, BiConsumer<Integer, Class<? extends Packet>> serverboundPackets) {
        clientboundPackets.accept(0x00, ClientboundStatusResponsePacket.class);
        clientboundPackets.accept(0x01, ClientboundPongResponsePacket.class);

        serverboundPackets.accept(0x00, ServerboundStatusRequestPacket.class);
        serverboundPackets.accept(0x01, ServerboundPingRequestPacket.class);
    }
}
