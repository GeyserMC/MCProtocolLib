package org.spacehq.mc.protocol;

import org.spacehq.mc.auth.data.GameProfile;
import org.spacehq.mc.auth.service.AuthenticationService;
import org.spacehq.mc.auth.exception.request.RequestException;
import org.spacehq.mc.protocol.data.SubProtocol;
import org.spacehq.mc.protocol.packet.handshake.client.HandshakePacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientKeepAlivePacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientPluginMessagePacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientRequestPacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientResourcePackStatusPacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientSettingsPacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientTabCompletePacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientChangeHeldItemPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerAbilitiesPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerActionPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerInteractEntityPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerMovementPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerPlaceBlockPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerPositionPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerStatePacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientSpectatePacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientSteerVehiclePacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientSwingArmPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientCloseWindowPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientConfirmTransactionPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientCreativeInventoryActionPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientEnchantItemPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;
import org.spacehq.mc.protocol.packet.ingame.client.world.ClientUpdateSignPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerCombatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerDifficultyPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerDisconnectPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerKeepAlivePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerPlayerListDataPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerPluginMessagePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerResourcePackSendPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerRespawnPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerSetCompressionPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerStatisticsPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerSwitchCameraPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerTabCompletePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerTitlePacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerAnimationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerCollectItemPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerDestroyEntitiesPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityAttachPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityEffectPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityEquipmentPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityHeadLookPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityMetadataPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityMovementPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityNBTUpdatePacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityPositionPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityPositionRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityPropertiesPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityRemoveEffectPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityStatusPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityTeleportPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityVelocityPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerChangeHeldItemPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerAbilitiesPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerUseBedPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerSetExperiencePacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerUpdateHealthPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnExpOrbPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnGlobalEntityPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPaintingPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import org.spacehq.mc.protocol.packet.ingame.server.scoreboard.ServerDisplayScoreboardPacket;
import org.spacehq.mc.protocol.packet.ingame.server.scoreboard.ServerScoreboardObjectivePacket;
import org.spacehq.mc.protocol.packet.ingame.server.scoreboard.ServerTeamPacket;
import org.spacehq.mc.protocol.packet.ingame.server.scoreboard.ServerUpdateScorePacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerCloseWindowPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerConfirmTransactionPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerOpenWindowPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerWindowItemsPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerWindowPropertyPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerBlockBreakAnimPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerBlockChangePacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerBlockValuePacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerExplosionPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerMapDataPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerMultiChunkDataPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerNotifyClientPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerOpenTileEntityEditorPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerPlayEffectPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerPlaySoundPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerSpawnParticlePacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerUpdateSignPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerUpdateTileEntityPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerUpdateTimePacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerWorldBorderPacket;
import org.spacehq.mc.protocol.packet.login.client.EncryptionResponsePacket;
import org.spacehq.mc.protocol.packet.login.client.LoginStartPacket;
import org.spacehq.mc.protocol.packet.login.server.EncryptionRequestPacket;
import org.spacehq.mc.protocol.packet.login.server.LoginDisconnectPacket;
import org.spacehq.mc.protocol.packet.login.server.LoginSetCompressionPacket;
import org.spacehq.mc.protocol.packet.login.server.LoginSuccessPacket;
import org.spacehq.mc.protocol.packet.status.client.StatusPingPacket;
import org.spacehq.mc.protocol.packet.status.client.StatusQueryPacket;
import org.spacehq.mc.protocol.packet.status.server.StatusPongPacket;
import org.spacehq.mc.protocol.packet.status.server.StatusResponsePacket;
import org.spacehq.packetlib.Client;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.crypt.AESEncryption;
import org.spacehq.packetlib.crypt.PacketEncryption;
import org.spacehq.packetlib.packet.DefaultPacketHeader;
import org.spacehq.packetlib.packet.PacketHeader;
import org.spacehq.packetlib.packet.PacketProtocol;

import java.net.Proxy;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.UUID;

public class MinecraftProtocol extends PacketProtocol {

    private SubProtocol subProtocol = SubProtocol.HANDSHAKE;
    private PacketHeader header = new DefaultPacketHeader();
    private AESEncryption encrypt;

    private GameProfile profile;
    private String accessToken = "";

    @SuppressWarnings("unused")
    private MinecraftProtocol() {
    }

    public MinecraftProtocol(SubProtocol subProtocol) {
        if(subProtocol != SubProtocol.LOGIN && subProtocol != SubProtocol.STATUS) {
            throw new IllegalArgumentException("Only login and status modes are permitted.");
        }

        this.subProtocol = subProtocol;
        if(subProtocol == SubProtocol.LOGIN) {
            this.profile = new GameProfile((UUID) null, "Player");
        }
    }

    public MinecraftProtocol(String username) {
        this(SubProtocol.LOGIN);
        this.profile = new GameProfile((UUID) null, username);
    }

    public MinecraftProtocol(String username, String password) throws RequestException {
        this(username, password, false);
    }

    public MinecraftProtocol(String username, String using, boolean token) throws RequestException {
        this(username, using, token, Proxy.NO_PROXY);
    }

    public MinecraftProtocol(String username, String using, boolean token, Proxy authProxy) throws RequestException {
        this(SubProtocol.LOGIN);
        String clientToken = UUID.randomUUID().toString();
        AuthenticationService auth = new AuthenticationService(clientToken, authProxy);
        auth.setUsername(username);
        if(token) {
            auth.setAccessToken(using);
        } else {
            auth.setPassword(using);
        }

        auth.login();
        this.profile = auth.getSelectedProfile();
        this.accessToken = auth.getAccessToken();
    }

    public MinecraftProtocol(GameProfile profile, String accessToken) {
        this(SubProtocol.LOGIN);
        this.profile = profile;
        this.accessToken = accessToken;
    }

    public GameProfile getProfile() {
        return this.profile;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    @Override
    public String getSRVRecordPrefix() {
        return "_minecraft";
    }

    @Override
    public PacketHeader getPacketHeader() {
        return this.header;
    }

    @Override
    public PacketEncryption getEncryption() {
        return this.encrypt;
    }

    @Override
    public void newClientSession(Client client, Session session) {
        if(this.profile != null) {
            session.setFlag(MinecraftConstants.PROFILE_KEY, this.profile);
            session.setFlag(MinecraftConstants.ACCESS_TOKEN_KEY, this.accessToken);
        }

        this.setSubProtocol(this.subProtocol, true, session);
        session.addListener(new ClientListener());
    }

    @Override
    public void newServerSession(Server server, Session session) {
        this.setSubProtocol(SubProtocol.HANDSHAKE, false, session);
        session.addListener(new ServerListener());
    }

    protected void enableEncryption(Key key) {
        try {
            this.encrypt = new AESEncryption(key);
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
        this.registerIncoming(0, LoginDisconnectPacket.class);
        this.registerIncoming(1, EncryptionRequestPacket.class);
        this.registerIncoming(2, LoginSuccessPacket.class);
        this.registerIncoming(3, LoginSetCompressionPacket.class);

        this.registerOutgoing(0, LoginStartPacket.class);
        this.registerOutgoing(1, EncryptionResponsePacket.class);
    }

    private void initServerLogin(Session session) {
        this.registerIncoming(0, LoginStartPacket.class);
        this.registerIncoming(1, EncryptionResponsePacket.class);

        this.registerOutgoing(0, LoginDisconnectPacket.class);
        this.registerOutgoing(1, EncryptionRequestPacket.class);
        this.registerOutgoing(2, LoginSuccessPacket.class);
        this.registerOutgoing(3, LoginSetCompressionPacket.class);
    }

    private void initClientGame(Session session) {
        this.registerIncoming(0, ServerKeepAlivePacket.class);
        this.registerIncoming(1, ServerJoinGamePacket.class);
        this.registerIncoming(2, ServerChatPacket.class);
        this.registerIncoming(3, ServerUpdateTimePacket.class);
        this.registerIncoming(4, ServerEntityEquipmentPacket.class);
        this.registerIncoming(5, ServerSpawnPositionPacket.class);
        this.registerIncoming(6, ServerUpdateHealthPacket.class);
        this.registerIncoming(7, ServerRespawnPacket.class);
        this.registerIncoming(8, ServerPlayerPositionRotationPacket.class);
        this.registerIncoming(9, ServerChangeHeldItemPacket.class);
        this.registerIncoming(10, ServerPlayerUseBedPacket.class);
        this.registerIncoming(11, ServerAnimationPacket.class);
        this.registerIncoming(12, ServerSpawnPlayerPacket.class);
        this.registerIncoming(13, ServerCollectItemPacket.class);
        this.registerIncoming(14, ServerSpawnObjectPacket.class);
        this.registerIncoming(15, ServerSpawnMobPacket.class);
        this.registerIncoming(16, ServerSpawnPaintingPacket.class);
        this.registerIncoming(17, ServerSpawnExpOrbPacket.class);
        this.registerIncoming(18, ServerEntityVelocityPacket.class);
        this.registerIncoming(19, ServerDestroyEntitiesPacket.class);
        this.registerIncoming(20, ServerEntityMovementPacket.class);
        this.registerIncoming(21, ServerEntityPositionPacket.class);
        this.registerIncoming(22, ServerEntityRotationPacket.class);
        this.registerIncoming(23, ServerEntityPositionRotationPacket.class);
        this.registerIncoming(24, ServerEntityTeleportPacket.class);
        this.registerIncoming(25, ServerEntityHeadLookPacket.class);
        this.registerIncoming(26, ServerEntityStatusPacket.class);
        this.registerIncoming(27, ServerEntityAttachPacket.class);
        this.registerIncoming(28, ServerEntityMetadataPacket.class);
        this.registerIncoming(29, ServerEntityEffectPacket.class);
        this.registerIncoming(30, ServerEntityRemoveEffectPacket.class);
        this.registerIncoming(31, ServerSetExperiencePacket.class);
        this.registerIncoming(32, ServerEntityPropertiesPacket.class);
        this.registerIncoming(33, ServerChunkDataPacket.class);
        this.registerIncoming(34, ServerMultiBlockChangePacket.class);
        this.registerIncoming(35, ServerBlockChangePacket.class);
        this.registerIncoming(36, ServerBlockValuePacket.class);
        this.registerIncoming(37, ServerBlockBreakAnimPacket.class);
        this.registerIncoming(38, ServerMultiChunkDataPacket.class);
        this.registerIncoming(39, ServerExplosionPacket.class);
        this.registerIncoming(40, ServerPlayEffectPacket.class);
        this.registerIncoming(41, ServerPlaySoundPacket.class);
        this.registerIncoming(42, ServerSpawnParticlePacket.class);
        this.registerIncoming(43, ServerNotifyClientPacket.class);
        this.registerIncoming(44, ServerSpawnGlobalEntityPacket.class);
        this.registerIncoming(45, ServerOpenWindowPacket.class);
        this.registerIncoming(46, ServerCloseWindowPacket.class);
        this.registerIncoming(47, ServerSetSlotPacket.class);
        this.registerIncoming(48, ServerWindowItemsPacket.class);
        this.registerIncoming(49, ServerWindowPropertyPacket.class);
        this.registerIncoming(50, ServerConfirmTransactionPacket.class);
        this.registerIncoming(51, ServerUpdateSignPacket.class);
        this.registerIncoming(52, ServerMapDataPacket.class);
        this.registerIncoming(53, ServerUpdateTileEntityPacket.class);
        this.registerIncoming(54, ServerOpenTileEntityEditorPacket.class);
        this.registerIncoming(55, ServerStatisticsPacket.class);
        this.registerIncoming(56, ServerPlayerListEntryPacket.class);
        this.registerIncoming(57, ServerPlayerAbilitiesPacket.class);
        this.registerIncoming(58, ServerTabCompletePacket.class);
        this.registerIncoming(59, ServerScoreboardObjectivePacket.class);
        this.registerIncoming(60, ServerUpdateScorePacket.class);
        this.registerIncoming(61, ServerDisplayScoreboardPacket.class);
        this.registerIncoming(62, ServerTeamPacket.class);
        this.registerIncoming(63, ServerPluginMessagePacket.class);
        this.registerIncoming(64, ServerDisconnectPacket.class);
        this.registerIncoming(65, ServerDifficultyPacket.class);
        this.registerIncoming(66, ServerCombatPacket.class);
        this.registerIncoming(67, ServerSwitchCameraPacket.class);
        this.registerIncoming(68, ServerWorldBorderPacket.class);
        this.registerIncoming(69, ServerTitlePacket.class);
        this.registerIncoming(70, ServerSetCompressionPacket.class);
        this.registerIncoming(71, ServerPlayerListDataPacket.class);
        this.registerIncoming(72, ServerResourcePackSendPacket.class);
        this.registerIncoming(73, ServerEntityNBTUpdatePacket.class);

        this.registerOutgoing(0, ClientKeepAlivePacket.class);
        this.registerOutgoing(1, ClientChatPacket.class);
        this.registerOutgoing(2, ClientPlayerInteractEntityPacket.class);
        this.registerOutgoing(3, ClientPlayerMovementPacket.class);
        this.registerOutgoing(4, ClientPlayerPositionPacket.class);
        this.registerOutgoing(5, ClientPlayerRotationPacket.class);
        this.registerOutgoing(6, ClientPlayerPositionRotationPacket.class);
        this.registerOutgoing(7, ClientPlayerActionPacket.class);
        this.registerOutgoing(8, ClientPlayerPlaceBlockPacket.class);
        this.registerOutgoing(9, ClientChangeHeldItemPacket.class);
        this.registerOutgoing(10, ClientSwingArmPacket.class);
        this.registerOutgoing(11, ClientPlayerStatePacket.class);
        this.registerOutgoing(12, ClientSteerVehiclePacket.class);
        this.registerOutgoing(13, ClientCloseWindowPacket.class);
        this.registerOutgoing(14, ClientWindowActionPacket.class);
        this.registerOutgoing(15, ClientConfirmTransactionPacket.class);
        this.registerOutgoing(16, ClientCreativeInventoryActionPacket.class);
        this.registerOutgoing(17, ClientEnchantItemPacket.class);
        this.registerOutgoing(18, ClientUpdateSignPacket.class);
        this.registerOutgoing(19, ClientPlayerAbilitiesPacket.class);
        this.registerOutgoing(20, ClientTabCompletePacket.class);
        this.registerOutgoing(21, ClientSettingsPacket.class);
        this.registerOutgoing(22, ClientRequestPacket.class);
        this.registerOutgoing(23, ClientPluginMessagePacket.class);
        this.registerOutgoing(24, ClientSpectatePacket.class);
        this.registerOutgoing(25, ClientResourcePackStatusPacket.class);
    }

    private void initServerGame(Session session) {
        this.registerIncoming(0, ClientKeepAlivePacket.class);
        this.registerIncoming(1, ClientChatPacket.class);
        this.registerIncoming(2, ClientPlayerInteractEntityPacket.class);
        this.registerIncoming(3, ClientPlayerMovementPacket.class);
        this.registerIncoming(4, ClientPlayerPositionPacket.class);
        this.registerIncoming(5, ClientPlayerRotationPacket.class);
        this.registerIncoming(6, ClientPlayerPositionRotationPacket.class);
        this.registerIncoming(7, ClientPlayerActionPacket.class);
        this.registerIncoming(8, ClientPlayerPlaceBlockPacket.class);
        this.registerIncoming(9, ClientChangeHeldItemPacket.class);
        this.registerIncoming(10, ClientSwingArmPacket.class);
        this.registerIncoming(11, ClientPlayerStatePacket.class);
        this.registerIncoming(12, ClientSteerVehiclePacket.class);
        this.registerIncoming(13, ClientCloseWindowPacket.class);
        this.registerIncoming(14, ClientWindowActionPacket.class);
        this.registerIncoming(15, ClientConfirmTransactionPacket.class);
        this.registerIncoming(16, ClientCreativeInventoryActionPacket.class);
        this.registerIncoming(17, ClientEnchantItemPacket.class);
        this.registerIncoming(18, ClientUpdateSignPacket.class);
        this.registerIncoming(19, ClientPlayerAbilitiesPacket.class);
        this.registerIncoming(20, ClientTabCompletePacket.class);
        this.registerIncoming(21, ClientSettingsPacket.class);
        this.registerIncoming(22, ClientRequestPacket.class);
        this.registerIncoming(23, ClientPluginMessagePacket.class);
        this.registerIncoming(24, ClientSpectatePacket.class);
        this.registerIncoming(25, ClientResourcePackStatusPacket.class);

        this.registerOutgoing(0, ServerKeepAlivePacket.class);
        this.registerOutgoing(1, ServerJoinGamePacket.class);
        this.registerOutgoing(2, ServerChatPacket.class);
        this.registerOutgoing(3, ServerUpdateTimePacket.class);
        this.registerOutgoing(4, ServerEntityEquipmentPacket.class);
        this.registerOutgoing(5, ServerSpawnPositionPacket.class);
        this.registerOutgoing(6, ServerUpdateHealthPacket.class);
        this.registerOutgoing(7, ServerRespawnPacket.class);
        this.registerOutgoing(8, ServerPlayerPositionRotationPacket.class);
        this.registerOutgoing(9, ServerChangeHeldItemPacket.class);
        this.registerOutgoing(10, ServerPlayerUseBedPacket.class);
        this.registerOutgoing(11, ServerAnimationPacket.class);
        this.registerOutgoing(12, ServerSpawnPlayerPacket.class);
        this.registerOutgoing(13, ServerCollectItemPacket.class);
        this.registerOutgoing(14, ServerSpawnObjectPacket.class);
        this.registerOutgoing(15, ServerSpawnMobPacket.class);
        this.registerOutgoing(16, ServerSpawnPaintingPacket.class);
        this.registerOutgoing(17, ServerSpawnExpOrbPacket.class);
        this.registerOutgoing(18, ServerEntityVelocityPacket.class);
        this.registerOutgoing(19, ServerDestroyEntitiesPacket.class);
        this.registerOutgoing(20, ServerEntityMovementPacket.class);
        this.registerOutgoing(21, ServerEntityPositionPacket.class);
        this.registerOutgoing(22, ServerEntityRotationPacket.class);
        this.registerOutgoing(23, ServerEntityPositionRotationPacket.class);
        this.registerOutgoing(24, ServerEntityTeleportPacket.class);
        this.registerOutgoing(25, ServerEntityHeadLookPacket.class);
        this.registerOutgoing(26, ServerEntityStatusPacket.class);
        this.registerOutgoing(27, ServerEntityAttachPacket.class);
        this.registerOutgoing(28, ServerEntityMetadataPacket.class);
        this.registerOutgoing(29, ServerEntityEffectPacket.class);
        this.registerOutgoing(30, ServerEntityRemoveEffectPacket.class);
        this.registerOutgoing(31, ServerSetExperiencePacket.class);
        this.registerOutgoing(32, ServerEntityPropertiesPacket.class);
        this.registerOutgoing(33, ServerChunkDataPacket.class);
        this.registerOutgoing(34, ServerMultiBlockChangePacket.class);
        this.registerOutgoing(35, ServerBlockChangePacket.class);
        this.registerOutgoing(36, ServerBlockValuePacket.class);
        this.registerOutgoing(37, ServerBlockBreakAnimPacket.class);
        this.registerOutgoing(38, ServerMultiChunkDataPacket.class);
        this.registerOutgoing(39, ServerExplosionPacket.class);
        this.registerOutgoing(40, ServerPlayEffectPacket.class);
        this.registerOutgoing(41, ServerPlaySoundPacket.class);
        this.registerOutgoing(42, ServerSpawnParticlePacket.class);
        this.registerOutgoing(43, ServerNotifyClientPacket.class);
        this.registerOutgoing(44, ServerSpawnGlobalEntityPacket.class);
        this.registerOutgoing(45, ServerOpenWindowPacket.class);
        this.registerOutgoing(46, ServerCloseWindowPacket.class);
        this.registerOutgoing(47, ServerSetSlotPacket.class);
        this.registerOutgoing(48, ServerWindowItemsPacket.class);
        this.registerOutgoing(49, ServerWindowPropertyPacket.class);
        this.registerOutgoing(50, ServerConfirmTransactionPacket.class);
        this.registerOutgoing(51, ServerUpdateSignPacket.class);
        this.registerOutgoing(52, ServerMapDataPacket.class);
        this.registerOutgoing(53, ServerUpdateTileEntityPacket.class);
        this.registerOutgoing(54, ServerOpenTileEntityEditorPacket.class);
        this.registerOutgoing(55, ServerStatisticsPacket.class);
        this.registerOutgoing(56, ServerPlayerListEntryPacket.class);
        this.registerOutgoing(57, ServerPlayerAbilitiesPacket.class);
        this.registerOutgoing(58, ServerTabCompletePacket.class);
        this.registerOutgoing(59, ServerScoreboardObjectivePacket.class);
        this.registerOutgoing(60, ServerUpdateScorePacket.class);
        this.registerOutgoing(61, ServerDisplayScoreboardPacket.class);
        this.registerOutgoing(62, ServerTeamPacket.class);
        this.registerOutgoing(63, ServerPluginMessagePacket.class);
        this.registerOutgoing(64, ServerDisconnectPacket.class);
        this.registerOutgoing(65, ServerDifficultyPacket.class);
        this.registerOutgoing(66, ServerCombatPacket.class);
        this.registerOutgoing(67, ServerSwitchCameraPacket.class);
        this.registerOutgoing(68, ServerWorldBorderPacket.class);
        this.registerOutgoing(69, ServerTitlePacket.class);
        this.registerOutgoing(70, ServerSetCompressionPacket.class);
        this.registerOutgoing(71, ServerPlayerListDataPacket.class);
        this.registerOutgoing(72, ServerResourcePackSendPacket.class);
        this.registerOutgoing(73, ServerEntityNBTUpdatePacket.class);
    }

    private void initClientStatus(Session session) {
        this.registerIncoming(0, StatusResponsePacket.class);
        this.registerIncoming(1, StatusPongPacket.class);

        this.registerOutgoing(0, StatusQueryPacket.class);
        this.registerOutgoing(1, StatusPingPacket.class);
    }

    private void initServerStatus(Session session) {
        this.registerIncoming(0, StatusQueryPacket.class);
        this.registerIncoming(1, StatusPingPacket.class);

        this.registerOutgoing(0, StatusResponsePacket.class);
        this.registerOutgoing(1, StatusPongPacket.class);
    }

}
