package com.github.steveice10.mc.protocol;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.data.SubProtocol;
import com.github.steveice10.mc.protocol.packet.handshake.serverbound.ClientIntentionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.*;
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
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.level.*;
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
import com.github.steveice10.packetlib.packet.PacketFactory;
import com.github.steveice10.packetlib.packet.PacketHeader;
import com.github.steveice10.packetlib.packet.PacketProtocol;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.UUID;

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
        this.registerOutgoing(0, ClientIntentionPacket.class, ClientIntentionPacket::new);
    }

    private void initServerHandshake() {
        this.registerIncoming(0, ClientIntentionPacket.class, ClientIntentionPacket::new);
    }

    private void initLogin(PacketRegisterFactory clientboundPackets, PacketRegisterFactory serverboundPackets) {
        clientboundPackets.register(0x00, ClientboundLoginDisconnectPacket.class, ClientboundLoginDisconnectPacket::new);
        clientboundPackets.register(0x01, ClientboundHelloPacket.class, ClientboundHelloPacket::new);
        clientboundPackets.register(0x02, ClientboundGameProfilePacket.class, ClientboundGameProfilePacket::new);
        clientboundPackets.register(0x03, ClientboundLoginCompressionPacket.class, ClientboundLoginCompressionPacket::new);
        clientboundPackets.register(0x04, ClientboundCustomQueryPacket.class, ClientboundCustomQueryPacket::new);

        serverboundPackets.register(0x00, ServerboundHelloPacket.class, ServerboundHelloPacket::new);
        serverboundPackets.register(0x01, ServerboundKeyPacket.class, ServerboundKeyPacket::new);
        serverboundPackets.register(0x02, ServerboundCustomQueryPacket.class, ServerboundCustomQueryPacket::new);
    }

    private void initGame(PacketRegisterFactory clientboundPackets, PacketRegisterFactory serverboundPackets) {
        clientboundPackets.register(0x00, ClientboundAddEntityPacket.class, ClientboundAddEntityPacket::new);
        clientboundPackets.register(0x01, ClientboundAddExperienceOrbPacket.class, ClientboundAddExperienceOrbPacket::new);
        clientboundPackets.register(0x02, ClientboundAddMobPacket.class, ClientboundAddMobPacket::new);
        clientboundPackets.register(0x03, ClientboundAddPaintingPacket.class, ClientboundAddPaintingPacket::new);
        clientboundPackets.register(0x04, ClientboundAddPlayerPacket.class, ClientboundAddPlayerPacket::new);
        clientboundPackets.register(0x05, ClientboundAddVibrationSignalPacket.class, ClientboundAddVibrationSignalPacket::new);
        clientboundPackets.register(0x06, ClientboundAnimatePacket.class, ClientboundAnimatePacket::new);
        clientboundPackets.register(0x07, ClientboundAwardStatsPacket.class, ClientboundAwardStatsPacket::new);
        clientboundPackets.register(0x08, ClientboundBlockBreakAckPacket.class, ClientboundBlockBreakAckPacket::new);
        clientboundPackets.register(0x09, ClientboundBlockDestructionPacket.class, ClientboundBlockDestructionPacket::new);
        clientboundPackets.register(0x0A, ClientboundBlockEntityDataPacket.class, ClientboundBlockEntityDataPacket::new);
        clientboundPackets.register(0x0B, ClientboundBlockEventPacket.class, ClientboundBlockEventPacket::new);
        clientboundPackets.register(0x0C, ClientboundBlockUpdatePacket.class, ClientboundBlockUpdatePacket::new);
        clientboundPackets.register(0x0D, ClientboundBossEventPacket.class, ClientboundBossEventPacket::new);
        clientboundPackets.register(0x0E, ClientboundChangeDifficultyPacket.class, ClientboundChangeDifficultyPacket::new);
        clientboundPackets.register(0x0F, ClientboundChatPacket.class, ClientboundChatPacket::new);
        clientboundPackets.register(0x10, ClientboundClearTitlesPacket.class, ClientboundClearTitlesPacket::new);
        clientboundPackets.register(0x11, ClientboundCommandSuggestionsPacket.class, ClientboundCommandSuggestionsPacket::new);
        clientboundPackets.register(0x12, ClientboundCommandsPacket.class, ClientboundCommandsPacket::new);
        clientboundPackets.register(0x13, ClientboundContainerClosePacket.class, ClientboundContainerClosePacket::new);
        clientboundPackets.register(0x14, ClientboundContainerSetContentPacket.class, ClientboundContainerSetContentPacket::new);
        clientboundPackets.register(0x15, ClientboundContainerSetDataPacket.class, ClientboundContainerSetDataPacket::new);
        clientboundPackets.register(0x16, ClientboundContainerSetSlotPacket.class, ClientboundContainerSetSlotPacket::new);
        clientboundPackets.register(0x17, ClientboundCooldownPacket.class, ClientboundCooldownPacket::new);
        clientboundPackets.register(0x18, ClientboundCustomPayloadPacket.class, ClientboundCustomPayloadPacket::new);
        clientboundPackets.register(0x19, ClientboundCustomSoundPacket.class, ClientboundCustomSoundPacket::new);
        clientboundPackets.register(0x1A, ClientboundDisconnectPacket.class, ClientboundDisconnectPacket::new);
        clientboundPackets.register(0x1B, ClientboundEntityEventPacket.class, ClientboundEntityEventPacket::new);
        clientboundPackets.register(0x1C, ClientboundExplodePacket.class, ClientboundExplodePacket::new);
        clientboundPackets.register(0x1D, ClientboundForgetLevelChunkPacket.class, ClientboundForgetLevelChunkPacket::new);
        clientboundPackets.register(0x1E, ClientboundGameEventPacket.class, ClientboundGameEventPacket::new);
        clientboundPackets.register(0x1F, ClientboundHorseScreenOpenPacket.class, ClientboundHorseScreenOpenPacket::new);
        clientboundPackets.register(0x20, ClientboundInitializeBorderPacket.class, ClientboundInitializeBorderPacket::new);
        clientboundPackets.register(0x21, ClientboundKeepAlivePacket.class, ClientboundKeepAlivePacket::new);
        clientboundPackets.register(0x22, ClientboundLevelChunkWithLightPacket.class, ClientboundLevelChunkWithLightPacket::new);
        clientboundPackets.register(0x23, ClientboundLevelEventPacket.class, ClientboundLevelEventPacket::new);
        clientboundPackets.register(0x24, ClientboundLevelParticlesPacket.class, ClientboundLevelParticlesPacket::new);
        clientboundPackets.register(0x25, ClientboundLightUpdatePacket.class, ClientboundLightUpdatePacket::new);
        clientboundPackets.register(0x26, ClientboundLoginPacket.class, ClientboundLoginPacket::new);
        clientboundPackets.register(0x27, ClientboundMapItemDataPacket.class, ClientboundMapItemDataPacket::new);
        clientboundPackets.register(0x28, ClientboundMerchantOffersPacket.class, ClientboundMerchantOffersPacket::new);
        clientboundPackets.register(0x29, ClientboundMoveEntityPosPacket.class, ClientboundMoveEntityPosPacket::new);
        clientboundPackets.register(0x2A, ClientboundMoveEntityPosRotPacket.class, ClientboundMoveEntityPosRotPacket::new);
        clientboundPackets.register(0x2B, ClientboundMoveEntityRotPacket.class, ClientboundMoveEntityRotPacket::new);
        clientboundPackets.register(0x2C, ClientboundMoveVehiclePacket.class, ClientboundMoveVehiclePacket::new);
        clientboundPackets.register(0x2D, ClientboundOpenBookPacket.class, ClientboundOpenBookPacket::new);
        clientboundPackets.register(0x2E, ClientboundOpenScreenPacket.class, ClientboundOpenScreenPacket::new);
        clientboundPackets.register(0x2F, ClientboundOpenSignEditorPacket.class, ClientboundOpenSignEditorPacket::new);
        clientboundPackets.register(0x30, ClientboundPingPacket.class, ClientboundPingPacket::new);
        clientboundPackets.register(0x31, ClientboundPlaceGhostRecipePacket.class, ClientboundPlaceGhostRecipePacket::new);
        clientboundPackets.register(0x32, ClientboundPlayerAbilitiesPacket.class, ClientboundPlayerAbilitiesPacket::new);
        clientboundPackets.register(0x33, ClientboundPlayerCombatEndPacket.class, ClientboundPlayerCombatEndPacket::new);
        clientboundPackets.register(0x34, ClientboundPlayerCombatEnterPacket.class, ClientboundPlayerCombatEnterPacket::new);
        clientboundPackets.register(0x35, ClientboundPlayerCombatKillPacket.class, ClientboundPlayerCombatKillPacket::new);
        clientboundPackets.register(0x36, ClientboundPlayerInfoPacket.class, ClientboundPlayerInfoPacket::new);
        clientboundPackets.register(0x37, ClientboundPlayerLookAtPacket.class, ClientboundPlayerLookAtPacket::new);
        clientboundPackets.register(0x38, ClientboundPlayerPositionPacket.class, ClientboundPlayerPositionPacket::new);
        clientboundPackets.register(0x39, ClientboundRecipePacket.class, ClientboundRecipePacket::new);
        clientboundPackets.register(0x3A, ClientboundRemoveEntitiesPacket.class, ClientboundRemoveEntitiesPacket::new);
        clientboundPackets.register(0x3B, ClientboundRemoveMobEffectPacket.class, ClientboundRemoveMobEffectPacket::new);
        clientboundPackets.register(0x3C, ClientboundResourcePackPacket.class, ClientboundResourcePackPacket::new);
        clientboundPackets.register(0x3D, ClientboundRespawnPacket.class, ClientboundRespawnPacket::new);
        clientboundPackets.register(0x3E, ClientboundRotateHeadPacket.class, ClientboundRotateHeadPacket::new);
        clientboundPackets.register(0x3F, ClientboundSectionBlocksUpdatePacket.class, ClientboundSectionBlocksUpdatePacket::new);
        clientboundPackets.register(0x40, ClientboundSelectAdvancementsTabPacket.class, ClientboundSelectAdvancementsTabPacket::new);
        clientboundPackets.register(0x41, ClientboundSetActionBarTextPacket.class, ClientboundSetActionBarTextPacket::new);
        clientboundPackets.register(0x42, ClientboundSetBorderCenterPacket.class, ClientboundSetBorderCenterPacket::new);
        clientboundPackets.register(0x43, ClientboundSetBorderLerpSizePacket.class, ClientboundSetBorderLerpSizePacket::new);
        clientboundPackets.register(0x44, ClientboundSetBorderSizePacket.class, ClientboundSetBorderSizePacket::new);
        clientboundPackets.register(0x45, ClientboundSetBorderWarningDelayPacket.class, ClientboundSetBorderWarningDelayPacket::new);
        clientboundPackets.register(0x46, ClientboundSetBorderWarningDistancePacket.class, ClientboundSetBorderWarningDistancePacket::new);
        clientboundPackets.register(0x47, ClientboundSetCameraPacket.class, ClientboundSetCameraPacket::new);
        clientboundPackets.register(0x48, ClientboundSetCarriedItemPacket.class, ClientboundSetCarriedItemPacket::new);
        clientboundPackets.register(0x49, ClientboundSetChunkCacheCenterPacket.class, ClientboundSetChunkCacheCenterPacket::new);
        clientboundPackets.register(0x4A, ClientboundSetChunkCacheRadiusPacket.class, ClientboundSetChunkCacheRadiusPacket::new);
        clientboundPackets.register(0x4B, ClientboundSetDefaultSpawnPositionPacket.class, ClientboundSetDefaultSpawnPositionPacket::new);
        clientboundPackets.register(0x4C, ClientboundSetDisplayObjectivePacket.class, ClientboundSetDisplayObjectivePacket::new);
        clientboundPackets.register(0x4D, ClientboundSetEntityDataPacket.class, ClientboundSetEntityDataPacket::new);
        clientboundPackets.register(0x4E, ClientboundSetEntityLinkPacket.class, ClientboundSetEntityLinkPacket::new);
        clientboundPackets.register(0x4F, ClientboundSetEntityMotionPacket.class, ClientboundSetEntityMotionPacket::new);
        clientboundPackets.register(0x50, ClientboundSetEquipmentPacket.class, ClientboundSetEquipmentPacket::new);
        clientboundPackets.register(0x51, ClientboundSetExperiencePacket.class, ClientboundSetExperiencePacket::new);
        clientboundPackets.register(0x52, ClientboundSetHealthPacket.class, ClientboundSetHealthPacket::new);
        clientboundPackets.register(0x53, ClientboundSetObjectivePacket.class, ClientboundSetObjectivePacket::new);
        clientboundPackets.register(0x54, ClientboundSetPassengersPacket.class, ClientboundSetPassengersPacket::new);
        clientboundPackets.register(0x55, ClientboundSetPlayerTeamPacket.class, ClientboundSetPlayerTeamPacket::new);
        clientboundPackets.register(0x56, ClientboundSetScorePacket.class, ClientboundSetScorePacket::new);
        clientboundPackets.register(0x57, ClientboundSetSimulationDistancePacket.class, ClientboundSetSimulationDistancePacket::new);
        clientboundPackets.register(0x58, ClientboundSetSubtitleTextPacket.class, ClientboundSetSubtitleTextPacket::new);
        clientboundPackets.register(0x59, ClientboundSetTimePacket.class, ClientboundSetTimePacket::new);
        clientboundPackets.register(0x5A, ClientboundSetTitleTextPacket.class, ClientboundSetTitleTextPacket::new);
        clientboundPackets.register(0x5B, ClientboundSetTitlesAnimationPacket.class, ClientboundSetTitlesAnimationPacket::new);
        clientboundPackets.register(0x5C, ClientboundSoundEntityPacket.class, ClientboundSoundEntityPacket::new);
        clientboundPackets.register(0x5D, ClientboundSoundPacket.class, ClientboundSoundPacket::new);
        clientboundPackets.register(0x5E, ClientboundStopSoundPacket.class, ClientboundStopSoundPacket::new);
        clientboundPackets.register(0x5F, ClientboundTabListPacket.class, ClientboundTabListPacket::new);
        clientboundPackets.register(0x60, ClientboundTagQueryPacket.class, ClientboundTagQueryPacket::new);
        clientboundPackets.register(0x61, ClientboundTakeItemEntityPacket.class, ClientboundTakeItemEntityPacket::new);
        clientboundPackets.register(0x62, ClientboundTeleportEntityPacket.class, ClientboundTeleportEntityPacket::new);
        clientboundPackets.register(0x63, ClientboundUpdateAdvancementsPacket.class, ClientboundUpdateAdvancementsPacket::new);
        clientboundPackets.register(0x64, ClientboundUpdateAttributesPacket.class, ClientboundUpdateAttributesPacket::new);
        clientboundPackets.register(0x65, ClientboundUpdateMobEffectPacket.class, ClientboundUpdateMobEffectPacket::new);
        clientboundPackets.register(0x66, ClientboundUpdateRecipesPacket.class, ClientboundUpdateRecipesPacket::new);
        clientboundPackets.register(0x67, ClientboundUpdateTagsPacket.class, ClientboundUpdateTagsPacket::new);

        serverboundPackets.register(0x00, ServerboundAcceptTeleportationPacket.class, ServerboundAcceptTeleportationPacket::new);
        serverboundPackets.register(0x01, ServerboundBlockEntityTagQuery.class, ServerboundBlockEntityTagQuery::new);
        serverboundPackets.register(0x02, ServerboundChangeDifficultyPacket.class, ServerboundChangeDifficultyPacket::new);
        serverboundPackets.register(0x03, ServerboundChatPacket.class, ServerboundChatPacket::new);
        serverboundPackets.register(0x04, ServerboundClientCommandPacket.class, ServerboundClientCommandPacket::new);
        serverboundPackets.register(0x05, ServerboundClientInformationPacket.class, ServerboundClientInformationPacket::new);
        serverboundPackets.register(0x06, ServerboundCommandSuggestionPacket.class, ServerboundCommandSuggestionPacket::new);
        serverboundPackets.register(0x07, ServerboundContainerButtonClickPacket.class, ServerboundContainerButtonClickPacket::new);
        serverboundPackets.register(0x08, ServerboundContainerClickPacket.class, ServerboundContainerClickPacket::new);
        serverboundPackets.register(0x09, ServerboundContainerClosePacket.class, ServerboundContainerClosePacket::new);
        serverboundPackets.register(0x0A, ServerboundCustomPayloadPacket.class, ServerboundCustomPayloadPacket::new);
        serverboundPackets.register(0x0B, ServerboundEditBookPacket.class, ServerboundEditBookPacket::new);
        serverboundPackets.register(0x0C, ServerboundEntityTagQuery.class, ServerboundEntityTagQuery::new);
        serverboundPackets.register(0x0D, ServerboundInteractPacket.class, ServerboundInteractPacket::new);
        serverboundPackets.register(0x0E, ServerboundJigsawGeneratePacket.class, ServerboundJigsawGeneratePacket::new);
        serverboundPackets.register(0x0F, ServerboundKeepAlivePacket.class, ServerboundKeepAlivePacket::new);
        serverboundPackets.register(0x10, ServerboundLockDifficultyPacket.class, ServerboundLockDifficultyPacket::new);
        serverboundPackets.register(0x11, ServerboundMovePlayerPosPacket.class, ServerboundMovePlayerPosPacket::new);
        serverboundPackets.register(0x12, ServerboundMovePlayerPosRotPacket.class, ServerboundMovePlayerPosRotPacket::new);
        serverboundPackets.register(0x13, ServerboundMovePlayerRotPacket.class, ServerboundMovePlayerRotPacket::new);
        serverboundPackets.register(0x14, ServerboundMovePlayerStatusOnlyPacket.class, ServerboundMovePlayerStatusOnlyPacket::new);
        serverboundPackets.register(0x15, ServerboundMoveVehiclePacket.class, ServerboundMoveVehiclePacket::new);
        serverboundPackets.register(0x16, ServerboundPaddleBoatPacket.class, ServerboundPaddleBoatPacket::new);
        serverboundPackets.register(0x17, ServerboundPickItemPacket.class, ServerboundPickItemPacket::new);
        serverboundPackets.register(0x18, ServerboundPlaceRecipePacket.class, ServerboundPlaceRecipePacket::new);
        serverboundPackets.register(0x19, ServerboundPlayerAbilitiesPacket.class, ServerboundPlayerAbilitiesPacket::new);
        serverboundPackets.register(0x1A, ServerboundPlayerActionPacket.class, ServerboundPlayerActionPacket::new);
        serverboundPackets.register(0x1B, ServerboundPlayerCommandPacket.class, ServerboundPlayerCommandPacket::new);
        serverboundPackets.register(0x1C, ServerboundPlayerInputPacket.class, ServerboundPlayerInputPacket::new);
        serverboundPackets.register(0x1D, ServerboundPongPacket.class, ServerboundPongPacket::new);
        serverboundPackets.register(0x1E, ServerboundRecipeBookChangeSettingsPacket.class, ServerboundRecipeBookChangeSettingsPacket::new);
        serverboundPackets.register(0x1F, ServerboundRecipeBookSeenRecipePacket.class, ServerboundRecipeBookSeenRecipePacket::new);
        serverboundPackets.register(0x20, ServerboundRenameItemPacket.class, ServerboundRenameItemPacket::new);
        serverboundPackets.register(0x21, ServerboundResourcePackPacket.class, ServerboundResourcePackPacket::new);
        serverboundPackets.register(0x22, ServerboundSeenAdvancementsPacket.class, ServerboundSeenAdvancementsPacket::new);
        serverboundPackets.register(0x23, ServerboundSelectTradePacket.class, ServerboundSelectTradePacket::new);
        serverboundPackets.register(0x24, ServerboundSetBeaconPacket.class, ServerboundSetBeaconPacket::new);
        serverboundPackets.register(0x25, ServerboundSetCarriedItemPacket.class, ServerboundSetCarriedItemPacket::new);
        serverboundPackets.register(0x26, ServerboundSetCommandBlockPacket.class, ServerboundSetCommandBlockPacket::new);
        serverboundPackets.register(0x27, ServerboundSetCommandMinecartPacket.class, ServerboundSetCommandMinecartPacket::new);
        serverboundPackets.register(0x28, ServerboundSetCreativeModeSlotPacket.class, ServerboundSetCreativeModeSlotPacket::new);
        serverboundPackets.register(0x29, ServerboundSetJigsawBlockPacket.class, ServerboundSetJigsawBlockPacket::new);
        serverboundPackets.register(0x2A, ServerboundSetStructureBlockPacket.class, ServerboundSetStructureBlockPacket::new);
        serverboundPackets.register(0x2B, ServerboundSignUpdatePacket.class, ServerboundSignUpdatePacket::new);
        serverboundPackets.register(0x2C, ServerboundSwingPacket.class, ServerboundSwingPacket::new);
        serverboundPackets.register(0x2D, ServerboundTeleportToEntityPacket.class, ServerboundTeleportToEntityPacket::new);
        serverboundPackets.register(0x2E, ServerboundUseItemOnPacket.class, ServerboundUseItemOnPacket::new);
        serverboundPackets.register(0x2F, ServerboundUseItemPacket.class, ServerboundUseItemPacket::new);
    }

    private void initStatus(PacketRegisterFactory clientboundPackets, PacketRegisterFactory serverboundPackets) {
        clientboundPackets.register(0x00, ClientboundStatusResponsePacket.class, ClientboundStatusResponsePacket::new);
        clientboundPackets.register(0x01, ClientboundPongResponsePacket.class, ClientboundPongResponsePacket::new);

        serverboundPackets.register(0x00, ServerboundStatusRequestPacket.class, ServerboundStatusRequestPacket::new);
        serverboundPackets.register(0x01, ServerboundPingRequestPacket.class, ServerboundPingRequestPacket::new);
    }

    @FunctionalInterface
    public interface PacketRegisterFactory {
        <T extends Packet> void register(int id, Class<T> packetClass, PacketFactory<T> factory);
    }
}
