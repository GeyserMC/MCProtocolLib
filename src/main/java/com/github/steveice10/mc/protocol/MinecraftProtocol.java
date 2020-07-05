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
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientConfirmTransactionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientCraftingBookDataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientCreativeInventoryActionPacket;
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
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerCombatPacket;
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
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerTitlePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerUnlockRecipesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityAnimationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityAttachPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityCollectItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityDestroyPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityEffectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityEquipmentPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityHeadLookPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityMetadataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityMovementPacket;
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
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerAbilitiesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerActionAckPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerChangeHeldItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerFacingPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerHealthPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerSetExperiencePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnExpOrbPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnLivingEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPaintingPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerDisplayScoreboardPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerScoreboardObjectivePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerTeamPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerUpdateScorePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerCloseWindowPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerConfirmTransactionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerOpenBookPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerOpenHorseWindowPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerOpenWindowPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerPreparedCraftingGridPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerTradeListPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerWindowItemsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerWindowPropertyPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerBlockBreakAnimPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerBlockChangePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerBlockValuePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerExplosionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerMapDataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerNBTResponsePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerNotifyClientPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerOpenTileEntityEditorPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerPlayBuiltinSoundPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerPlayEffectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerPlaySoundPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerSpawnParticlePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUnloadChunkPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateLightPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateTileEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateTimePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateViewDistancePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateViewPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerWorldBorderPacket;
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
import com.github.steveice10.packetlib.packet.PacketHeader;
import com.github.steveice10.packetlib.packet.PacketProtocol;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.Proxy;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MinecraftProtocol extends PacketProtocol {
    private SubProtocol subProtocol = SubProtocol.HANDSHAKE;
    private final PacketHeader packetHeader = new DefaultPacketHeader();
    private AESEncryption encryption = null;

    private SubProtocol targetSubProtocol;
    @Getter
    private GameProfile profile = null;
    @Getter
    private String clientToken = "";
    @Getter
    private String accessToken = "";

    @Getter
    @Setter
    private boolean useDefaultListeners = true;

    public MinecraftProtocol(SubProtocol subProtocol) {
        if(subProtocol != SubProtocol.LOGIN && subProtocol != SubProtocol.STATUS) {
            throw new IllegalArgumentException("Only login and status modes are permitted.");
        }

        this.targetSubProtocol = subProtocol;
        if(subProtocol == SubProtocol.LOGIN) {
            this.profile = new GameProfile((UUID) null, "Player");
        }
    }

    public MinecraftProtocol(String username) {
        this(SubProtocol.LOGIN);

        this.profile = new GameProfile((UUID) null, username);
    }

    public MinecraftProtocol(String username, String password) throws RequestException {
        this(username, password, Proxy.NO_PROXY);
    }

    public MinecraftProtocol(String username, String clientToken, String accessToken) throws RequestException {
        this(username, clientToken, accessToken, Proxy.NO_PROXY);
    }

    public MinecraftProtocol(String username, String password, Proxy proxy) throws RequestException {
        this(username, UUID.randomUUID().toString(), password, false, proxy);
    }

    public MinecraftProtocol(String username, String clientToken, String accessToken, Proxy proxy) throws RequestException {
        this(username, clientToken, accessToken, true, proxy);
    }

    private MinecraftProtocol(String username, String clientToken, String using, boolean token, Proxy authProxy) throws RequestException {
        this(SubProtocol.LOGIN);

        AuthenticationService auth = new AuthenticationService(clientToken);
        auth.setProxy(authProxy);
        auth.setUsername(username);
        if(token) {
            auth.setAccessToken(using);
        } else {
            auth.setPassword(using);
        }

        auth.login();
        this.profile = auth.getSelectedProfile();
        this.clientToken =  auth.getClientToken();
        this.accessToken = auth.getAccessToken();
    }

    public MinecraftProtocol(GameProfile profile, String clientToken, String accessToken) {
        this(SubProtocol.LOGIN);
        this.profile = profile;
        this.clientToken = clientToken;
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

    public SubProtocol getSubProtocol() {
        return this.subProtocol;
    }

    protected void setSubProtocol(SubProtocol subProtocol, boolean client, Session session) {
        this.clearPackets();
        switch(subProtocol) {
            case HANDSHAKE:
                if(client) {
                    this.initClientHandshake(session);
                } else {
                    this.initServerHandshake(session);
                }

                break;
            case LOGIN:
                if(client) {
                    this.initClientLogin(session);
                } else {
                    this.initServerLogin(session);
                }

                break;
            case GAME:
                if(client) {
                    this.initClientGame(session);
                } else {
                    this.initServerGame(session);
                }

                break;
            case STATUS:
                if(client) {
                    this.initClientStatus(session);
                } else {
                    this.initServerStatus(session);
                }

                break;
        }

        this.subProtocol = subProtocol;
    }

    private void initClientHandshake(Session session) {
        this.registerOutgoing(0, HandshakePacket.class);
    }

    private void initServerHandshake(Session session) {
        this.registerIncoming(0, HandshakePacket.class);
    }

    private void initClientLogin(Session session) {
        this.registerIncoming(0x00, LoginDisconnectPacket.class);
        this.registerIncoming(0x01, EncryptionRequestPacket.class);
        this.registerIncoming(0x02, LoginSuccessPacket.class);
        this.registerIncoming(0x03, LoginSetCompressionPacket.class);
        this.registerIncoming(0x04, LoginPluginRequestPacket.class);

        this.registerOutgoing(0x00, LoginStartPacket.class);
        this.registerOutgoing(0x01, EncryptionResponsePacket.class);
        this.registerOutgoing(0x02, LoginPluginResponsePacket.class);
    }

    private void initServerLogin(Session session) {
        this.registerIncoming(0x00, LoginStartPacket.class);
        this.registerIncoming(0x01, EncryptionResponsePacket.class);
        this.registerIncoming(0x02, LoginPluginResponsePacket.class);

        this.registerOutgoing(0x00, LoginDisconnectPacket.class);
        this.registerOutgoing(0x01, EncryptionRequestPacket.class);
        this.registerOutgoing(0x02, LoginSuccessPacket.class);
        this.registerOutgoing(0x03, LoginSetCompressionPacket.class);
        this.registerOutgoing(0x04, LoginPluginRequestPacket.class);
    }

    private void initClientGame(Session session) {
        this.registerIncoming(0x00, ServerSpawnEntityPacket.class);
        this.registerIncoming(0x01, ServerSpawnExpOrbPacket.class);
        this.registerIncoming(0x02, ServerSpawnLivingEntityPacket.class);
        this.registerIncoming(0x03, ServerSpawnPaintingPacket.class);
        this.registerIncoming(0x04, ServerSpawnPlayerPacket.class);
        this.registerIncoming(0x05, ServerEntityAnimationPacket.class);
        this.registerIncoming(0x06, ServerStatisticsPacket.class);
        this.registerIncoming(0x07, ServerPlayerActionAckPacket.class);
        this.registerIncoming(0x08, ServerBlockBreakAnimPacket.class);
        this.registerIncoming(0x09, ServerUpdateTileEntityPacket.class);
        this.registerIncoming(0x0A, ServerBlockValuePacket.class);
        this.registerIncoming(0x0B, ServerBlockChangePacket.class);
        this.registerIncoming(0x0C, ServerBossBarPacket.class);
        this.registerIncoming(0x0D, ServerDifficultyPacket.class);
        this.registerIncoming(0x0E, ServerChatPacket.class);
        this.registerIncoming(0x0F, ServerMultiBlockChangePacket.class);
        this.registerIncoming(0x10, ServerTabCompletePacket.class);
        this.registerIncoming(0x11, ServerDeclareCommandsPacket.class);
        this.registerIncoming(0x12, ServerConfirmTransactionPacket.class);
        this.registerIncoming(0x13, ServerCloseWindowPacket.class);
        this.registerIncoming(0x14, ServerWindowItemsPacket.class);
        this.registerIncoming(0x15, ServerWindowPropertyPacket.class);
        this.registerIncoming(0x16, ServerSetSlotPacket.class);
        this.registerIncoming(0x17, ServerSetCooldownPacket.class);
        this.registerIncoming(0x18, ServerPluginMessagePacket.class);
        this.registerIncoming(0x19, ServerPlaySoundPacket.class);
        this.registerIncoming(0x1A, ServerDisconnectPacket.class);
        this.registerIncoming(0x1B, ServerEntityStatusPacket.class);
        this.registerIncoming(0x1C, ServerExplosionPacket.class);
        this.registerIncoming(0x1D, ServerUnloadChunkPacket.class);
        this.registerIncoming(0x1E, ServerNotifyClientPacket.class);
        this.registerIncoming(0x2F, ServerOpenHorseWindowPacket.class);
        this.registerIncoming(0x20, ServerKeepAlivePacket.class);
        this.registerIncoming(0x21, ServerChunkDataPacket.class);
        this.registerIncoming(0x22, ServerPlayEffectPacket.class);
        this.registerIncoming(0x23, ServerSpawnParticlePacket.class);
        this.registerIncoming(0x24, ServerUpdateLightPacket.class);
        this.registerIncoming(0x25, ServerJoinGamePacket.class);
        this.registerIncoming(0x26, ServerMapDataPacket.class);
        this.registerIncoming(0x27, ServerTradeListPacket.class);
        this.registerIncoming(0x28, ServerEntityPositionPacket.class);
        this.registerIncoming(0x29, ServerEntityPositionRotationPacket.class);
        this.registerIncoming(0x2A, ServerEntityRotationPacket.class);
        this.registerIncoming(0x2B, ServerEntityMovementPacket.class);
        this.registerIncoming(0x2C, ServerVehicleMovePacket.class);
        this.registerIncoming(0x2D, ServerOpenBookPacket.class);
        this.registerIncoming(0x2E, ServerOpenWindowPacket.class);
        this.registerIncoming(0x2F, ServerOpenTileEntityEditorPacket.class);
        this.registerIncoming(0x30, ServerPreparedCraftingGridPacket.class);
        this.registerIncoming(0x31, ServerPlayerAbilitiesPacket.class);
        this.registerIncoming(0x32, ServerCombatPacket.class);
        this.registerIncoming(0x33, ServerPlayerListEntryPacket.class);
        this.registerIncoming(0x34, ServerPlayerFacingPacket.class);
        this.registerIncoming(0x35, ServerPlayerPositionRotationPacket.class);
        this.registerIncoming(0x36, ServerUnlockRecipesPacket.class);
        this.registerIncoming(0x37, ServerEntityDestroyPacket.class);
        this.registerIncoming(0x38, ServerEntityRemoveEffectPacket.class);
        this.registerIncoming(0x39, ServerResourcePackSendPacket.class);
        this.registerIncoming(0x3A, ServerRespawnPacket.class);
        this.registerIncoming(0x3B, ServerEntityHeadLookPacket.class);
        this.registerIncoming(0x3C, ServerAdvancementTabPacket.class);
        this.registerIncoming(0x3D, ServerWorldBorderPacket.class);
        this.registerIncoming(0x3E, ServerSwitchCameraPacket.class);
        this.registerIncoming(0x3F, ServerPlayerChangeHeldItemPacket.class);
        this.registerIncoming(0x40, ServerUpdateViewPositionPacket.class);
        this.registerIncoming(0x41, ServerUpdateViewDistancePacket.class);
        this.registerIncoming(0x42, ServerSpawnPositionPacket.class);
        this.registerIncoming(0x43, ServerDisplayScoreboardPacket.class);
        this.registerIncoming(0x44, ServerEntityMetadataPacket.class);
        this.registerIncoming(0x45, ServerEntityAttachPacket.class);
        this.registerIncoming(0x46, ServerEntityVelocityPacket.class);
        this.registerIncoming(0x47, ServerEntityEquipmentPacket.class);
        this.registerIncoming(0x48, ServerPlayerSetExperiencePacket.class);
        this.registerIncoming(0x49, ServerPlayerHealthPacket.class);
        this.registerIncoming(0x4A, ServerScoreboardObjectivePacket.class);
        this.registerIncoming(0x4B, ServerEntitySetPassengersPacket.class);
        this.registerIncoming(0x4C, ServerTeamPacket.class);
        this.registerIncoming(0x4D, ServerUpdateScorePacket.class);
        this.registerIncoming(0x4E, ServerUpdateTimePacket.class);
        this.registerIncoming(0x4F, ServerTitlePacket.class);
        this.registerIncoming(0x50, ServerEntitySoundEffectPacket.class);
        this.registerIncoming(0x51, ServerPlayBuiltinSoundPacket.class);
        this.registerIncoming(0x52, ServerStopSoundPacket.class);
        this.registerIncoming(0x53, ServerPlayerListDataPacket.class);
        this.registerIncoming(0x54, ServerNBTResponsePacket.class);
        this.registerIncoming(0x55, ServerEntityCollectItemPacket.class);
        this.registerIncoming(0x56, ServerEntityTeleportPacket.class);
        this.registerIncoming(0x57, ServerAdvancementsPacket.class);
        this.registerIncoming(0x58, ServerEntityPropertiesPacket.class);
        this.registerIncoming(0x59, ServerEntityEffectPacket.class);
        this.registerIncoming(0x5A, ServerDeclareRecipesPacket.class);
        this.registerIncoming(0x5B, ServerDeclareTagsPacket.class);

        this.registerOutgoing(0x00, ClientTeleportConfirmPacket.class);
        this.registerOutgoing(0x01, ClientBlockNBTRequestPacket.class);
        this.registerOutgoing(0x02, ClientSetDifficultyPacket.class);
        this.registerOutgoing(0x03, ClientChatPacket.class);
        this.registerOutgoing(0x04, ClientRequestPacket.class);
        this.registerOutgoing(0x05, ClientSettingsPacket.class);
        this.registerOutgoing(0x06, ClientTabCompletePacket.class);
        this.registerOutgoing(0x07, ClientConfirmTransactionPacket.class);
        this.registerOutgoing(0x08, ClientClickWindowButtonPacket.class);
        this.registerOutgoing(0x09, ClientWindowActionPacket.class);
        this.registerOutgoing(0x0A, ClientCloseWindowPacket.class);
        this.registerOutgoing(0x0B, ClientPluginMessagePacket.class);
        this.registerOutgoing(0x0C, ClientEditBookPacket.class);
        this.registerOutgoing(0x0D, ClientEntityNBTRequestPacket.class);
        this.registerOutgoing(0x0E, ClientPlayerInteractEntityPacket.class);
        this.registerOutgoing(0x0F, ClientGenerateStructuresPacket.class);
        this.registerOutgoing(0x10, ClientKeepAlivePacket.class);
        this.registerOutgoing(0x11, ClientLockDifficultyPacket.class);
        this.registerOutgoing(0x12, ClientPlayerPositionPacket.class);
        this.registerOutgoing(0x13, ClientPlayerPositionRotationPacket.class);
        this.registerOutgoing(0x14, ClientPlayerRotationPacket.class);
        this.registerOutgoing(0x15, ClientPlayerMovementPacket.class);
        this.registerOutgoing(0x16, ClientVehicleMovePacket.class);
        this.registerOutgoing(0x17, ClientSteerBoatPacket.class);
        this.registerOutgoing(0x18, ClientMoveItemToHotbarPacket.class);
        this.registerOutgoing(0x19, ClientPrepareCraftingGridPacket.class);
        this.registerOutgoing(0x1A, ClientPlayerAbilitiesPacket.class);
        this.registerOutgoing(0x1B, ClientPlayerActionPacket.class);
        this.registerOutgoing(0x1C, ClientPlayerStatePacket.class);
        this.registerOutgoing(0x1D, ClientSteerVehiclePacket.class);
        this.registerOutgoing(0x1E, ClientCraftingBookDataPacket.class);
        this.registerOutgoing(0x1F, ClientRenameItemPacket.class);
        this.registerOutgoing(0x20, ClientResourcePackStatusPacket.class);
        this.registerOutgoing(0x21, ClientAdvancementTabPacket.class);
        this.registerOutgoing(0x22, ClientSelectTradePacket.class);
        this.registerOutgoing(0x23, ClientSetBeaconEffectPacket.class);
        this.registerOutgoing(0x24, ClientPlayerChangeHeldItemPacket.class);
        this.registerOutgoing(0x25, ClientUpdateCommandBlockPacket.class);
        this.registerOutgoing(0x26, ClientUpdateCommandBlockMinecartPacket.class);
        this.registerOutgoing(0x27, ClientCreativeInventoryActionPacket.class);
        this.registerOutgoing(0x28, ClientUpdateJigsawBlockPacket.class);
        this.registerOutgoing(0x29, ClientUpdateStructureBlockPacket.class);
        this.registerOutgoing(0x2A, ClientUpdateSignPacket.class);
        this.registerOutgoing(0x2B, ClientPlayerSwingArmPacket.class);
        this.registerOutgoing(0x2C, ClientSpectatePacket.class);
        this.registerOutgoing(0x2D, ClientPlayerPlaceBlockPacket.class);
        this.registerOutgoing(0x2E, ClientPlayerUseItemPacket.class);
    }

    private void initServerGame(Session session) {
        this.registerIncoming(0x00, ClientTeleportConfirmPacket.class);
        this.registerIncoming(0x01, ClientBlockNBTRequestPacket.class);
        this.registerIncoming(0x02, ClientSetDifficultyPacket.class);
        this.registerIncoming(0x03, ClientChatPacket.class);
        this.registerIncoming(0x04, ClientRequestPacket.class);
        this.registerIncoming(0x05, ClientSettingsPacket.class);
        this.registerIncoming(0x06, ClientTabCompletePacket.class);
        this.registerIncoming(0x07, ClientConfirmTransactionPacket.class);
        this.registerIncoming(0x08, ClientClickWindowButtonPacket.class);
        this.registerIncoming(0x09, ClientWindowActionPacket.class);
        this.registerIncoming(0x0A, ClientCloseWindowPacket.class);
        this.registerIncoming(0x0B, ClientPluginMessagePacket.class);
        this.registerIncoming(0x0C, ClientEditBookPacket.class);
        this.registerIncoming(0x0D, ClientEntityNBTRequestPacket.class);
        this.registerIncoming(0x0E, ClientPlayerInteractEntityPacket.class);
        this.registerIncoming(0x0F, ClientGenerateStructuresPacket.class);
        this.registerIncoming(0x10, ClientKeepAlivePacket.class);
        this.registerIncoming(0x11, ClientLockDifficultyPacket.class);
        this.registerIncoming(0x12, ClientPlayerPositionPacket.class);
        this.registerIncoming(0x13, ClientPlayerPositionRotationPacket.class);
        this.registerIncoming(0x14, ClientPlayerRotationPacket.class);
        this.registerIncoming(0x15, ClientPlayerMovementPacket.class);
        this.registerIncoming(0x16, ClientVehicleMovePacket.class);
        this.registerIncoming(0x17, ClientSteerBoatPacket.class);
        this.registerIncoming(0x18, ClientMoveItemToHotbarPacket.class);
        this.registerIncoming(0x19, ClientPrepareCraftingGridPacket.class);
        this.registerIncoming(0x1A, ClientPlayerAbilitiesPacket.class);
        this.registerIncoming(0x1B, ClientPlayerActionPacket.class);
        this.registerIncoming(0x1C, ClientPlayerStatePacket.class);
        this.registerIncoming(0x1D, ClientSteerVehiclePacket.class);
        this.registerIncoming(0x1E, ClientCraftingBookDataPacket.class);
        this.registerIncoming(0x1F, ClientRenameItemPacket.class);
        this.registerIncoming(0x20, ClientResourcePackStatusPacket.class);
        this.registerIncoming(0x21, ClientAdvancementTabPacket.class);
        this.registerIncoming(0x22, ClientSelectTradePacket.class);
        this.registerIncoming(0x23, ClientSetBeaconEffectPacket.class);
        this.registerIncoming(0x24, ClientPlayerChangeHeldItemPacket.class);
        this.registerIncoming(0x25, ClientUpdateCommandBlockPacket.class);
        this.registerIncoming(0x26, ClientUpdateCommandBlockMinecartPacket.class);
        this.registerIncoming(0x27, ClientCreativeInventoryActionPacket.class);
        this.registerIncoming(0x28, ClientUpdateJigsawBlockPacket.class);
        this.registerIncoming(0x29, ClientUpdateStructureBlockPacket.class);
        this.registerIncoming(0x2A, ClientUpdateSignPacket.class);
        this.registerIncoming(0x2B, ClientPlayerSwingArmPacket.class);
        this.registerIncoming(0x2C, ClientSpectatePacket.class);
        this.registerIncoming(0x2D, ClientPlayerPlaceBlockPacket.class);
        this.registerIncoming(0x2E, ClientPlayerUseItemPacket.class);

        this.registerOutgoing(0x00, ServerSpawnEntityPacket.class);
        this.registerOutgoing(0x01, ServerSpawnExpOrbPacket.class);
        this.registerOutgoing(0x02, ServerSpawnLivingEntityPacket.class);
        this.registerOutgoing(0x03, ServerSpawnPaintingPacket.class);
        this.registerOutgoing(0x04, ServerSpawnPlayerPacket.class);
        this.registerOutgoing(0x05, ServerEntityAnimationPacket.class);
        this.registerOutgoing(0x06, ServerStatisticsPacket.class);
        this.registerOutgoing(0x07, ServerPlayerActionAckPacket.class);
        this.registerOutgoing(0x08, ServerBlockBreakAnimPacket.class);
        this.registerOutgoing(0x09, ServerUpdateTileEntityPacket.class);
        this.registerOutgoing(0x0A, ServerBlockValuePacket.class);
        this.registerOutgoing(0x0B, ServerBlockChangePacket.class);
        this.registerOutgoing(0x0C, ServerBossBarPacket.class);
        this.registerOutgoing(0x0D, ServerDifficultyPacket.class);
        this.registerOutgoing(0x0E, ServerChatPacket.class);
        this.registerOutgoing(0x0F, ServerMultiBlockChangePacket.class);
        this.registerOutgoing(0x10, ServerTabCompletePacket.class);
        this.registerOutgoing(0x11, ServerDeclareCommandsPacket.class);
        this.registerOutgoing(0x12, ServerConfirmTransactionPacket.class);
        this.registerOutgoing(0x13, ServerCloseWindowPacket.class);
        this.registerOutgoing(0x14, ServerWindowItemsPacket.class);
        this.registerOutgoing(0x15, ServerWindowPropertyPacket.class);
        this.registerOutgoing(0x16, ServerSetSlotPacket.class);
        this.registerOutgoing(0x17, ServerSetCooldownPacket.class);
        this.registerOutgoing(0x18, ServerPluginMessagePacket.class);
        this.registerOutgoing(0x19, ServerPlaySoundPacket.class);
        this.registerOutgoing(0x1A, ServerDisconnectPacket.class);
        this.registerOutgoing(0x1B, ServerEntityStatusPacket.class);
        this.registerOutgoing(0x1C, ServerExplosionPacket.class);
        this.registerOutgoing(0x1D, ServerUnloadChunkPacket.class);
        this.registerOutgoing(0x1E, ServerNotifyClientPacket.class);
        this.registerOutgoing(0x1F, ServerOpenHorseWindowPacket.class);
        this.registerOutgoing(0x20, ServerKeepAlivePacket.class);
        this.registerOutgoing(0x21, ServerChunkDataPacket.class);
        this.registerOutgoing(0x22, ServerPlayEffectPacket.class);
        this.registerOutgoing(0x23, ServerSpawnParticlePacket.class);
        this.registerOutgoing(0x24, ServerUpdateLightPacket.class);
        this.registerOutgoing(0x25, ServerJoinGamePacket.class);
        this.registerOutgoing(0x26, ServerMapDataPacket.class);
        this.registerOutgoing(0x27, ServerTradeListPacket.class);
        this.registerOutgoing(0x28, ServerEntityPositionPacket.class);
        this.registerOutgoing(0x29, ServerEntityPositionRotationPacket.class);
        this.registerOutgoing(0x2A, ServerEntityRotationPacket.class);
        this.registerOutgoing(0x2B, ServerEntityMovementPacket.class);
        this.registerOutgoing(0x2C, ServerVehicleMovePacket.class);
        this.registerOutgoing(0x2D, ServerOpenBookPacket.class);
        this.registerOutgoing(0x2E, ServerOpenWindowPacket.class);
        this.registerOutgoing(0x2F, ServerOpenTileEntityEditorPacket.class);
        this.registerOutgoing(0x30, ServerPreparedCraftingGridPacket.class);
        this.registerOutgoing(0x31, ServerPlayerAbilitiesPacket.class);
        this.registerOutgoing(0x32, ServerCombatPacket.class);
        this.registerOutgoing(0x33, ServerPlayerListEntryPacket.class);
        this.registerOutgoing(0x34, ServerPlayerFacingPacket.class);
        this.registerOutgoing(0x35, ServerPlayerPositionRotationPacket.class);
        this.registerOutgoing(0x36, ServerUnlockRecipesPacket.class);
        this.registerOutgoing(0x37, ServerEntityDestroyPacket.class);
        this.registerOutgoing(0x38, ServerEntityRemoveEffectPacket.class);
        this.registerOutgoing(0x39, ServerResourcePackSendPacket.class);
        this.registerOutgoing(0x3A, ServerRespawnPacket.class);
        this.registerOutgoing(0x3B, ServerEntityHeadLookPacket.class);
        this.registerOutgoing(0x3C, ServerAdvancementTabPacket.class);
        this.registerOutgoing(0x3D, ServerWorldBorderPacket.class);
        this.registerOutgoing(0x3E, ServerSwitchCameraPacket.class);
        this.registerOutgoing(0x3F, ServerPlayerChangeHeldItemPacket.class);
        this.registerOutgoing(0x40, ServerUpdateViewPositionPacket.class);
        this.registerOutgoing(0x41, ServerUpdateViewDistancePacket.class);
        this.registerOutgoing(0x42, ServerSpawnPositionPacket.class);
        this.registerOutgoing(0x43, ServerDisplayScoreboardPacket.class);
        this.registerOutgoing(0x44, ServerEntityMetadataPacket.class);
        this.registerOutgoing(0x45, ServerEntityAttachPacket.class);
        this.registerOutgoing(0x46, ServerEntityVelocityPacket.class);
        this.registerOutgoing(0x47, ServerEntityEquipmentPacket.class);
        this.registerOutgoing(0x48, ServerPlayerSetExperiencePacket.class);
        this.registerOutgoing(0x49, ServerPlayerHealthPacket.class);
        this.registerOutgoing(0x4A, ServerScoreboardObjectivePacket.class);
        this.registerOutgoing(0x4B, ServerEntitySetPassengersPacket.class);
        this.registerOutgoing(0x4C, ServerTeamPacket.class);
        this.registerOutgoing(0x4D, ServerUpdateScorePacket.class);
        this.registerOutgoing(0x4E, ServerUpdateTimePacket.class);
        this.registerOutgoing(0x4F, ServerTitlePacket.class);
        this.registerOutgoing(0x50, ServerEntitySoundEffectPacket.class);
        this.registerOutgoing(0x51, ServerPlayBuiltinSoundPacket.class);
        this.registerOutgoing(0x52, ServerStopSoundPacket.class);
        this.registerOutgoing(0x53, ServerPlayerListDataPacket.class);
        this.registerOutgoing(0x54, ServerNBTResponsePacket.class);
        this.registerOutgoing(0x55, ServerEntityCollectItemPacket.class);
        this.registerOutgoing(0x56, ServerEntityTeleportPacket.class);
        this.registerOutgoing(0x57, ServerAdvancementsPacket.class);
        this.registerOutgoing(0x58, ServerEntityPropertiesPacket.class);
        this.registerOutgoing(0x59, ServerEntityEffectPacket.class);
        this.registerOutgoing(0x5A, ServerDeclareRecipesPacket.class);
        this.registerOutgoing(0x5B, ServerDeclareTagsPacket.class);
    }

    private void initClientStatus(Session session) {
        this.registerIncoming(0x00, StatusResponsePacket.class);
        this.registerIncoming(0x01, StatusPongPacket.class);

        this.registerOutgoing(0x00, StatusQueryPacket.class);
        this.registerOutgoing(0x01, StatusPingPacket.class);
    }

    private void initServerStatus(Session session) {
        this.registerIncoming(0x00, StatusQueryPacket.class);
        this.registerIncoming(0x01, StatusPingPacket.class);

        this.registerOutgoing(0x00, StatusResponsePacket.class);
        this.registerOutgoing(0x01, StatusPongPacket.class);
    }
}
