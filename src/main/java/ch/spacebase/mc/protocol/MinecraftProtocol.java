package ch.spacebase.mc.protocol;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.UUID;

import ch.spacebase.mc.auth.GameProfile;
import ch.spacebase.mc.auth.UserAuthentication;
import ch.spacebase.mc.auth.exceptions.AuthenticationException;
import ch.spacebase.mc.protocol.packet.handshake.client.HandshakePacket;
import ch.spacebase.mc.protocol.packet.ingame.client.ClientChatPacket;
import ch.spacebase.mc.protocol.packet.ingame.client.ClientKeepAlivePacket;
import ch.spacebase.mc.protocol.packet.ingame.client.ClientPluginMessagePacket;
import ch.spacebase.mc.protocol.packet.ingame.client.ClientRequestPacket;
import ch.spacebase.mc.protocol.packet.ingame.client.ClientSettingsPacket;
import ch.spacebase.mc.protocol.packet.ingame.client.ClientTabCompletePacket;
import ch.spacebase.mc.protocol.packet.ingame.client.entity.ClientAnimationPacket;
import ch.spacebase.mc.protocol.packet.ingame.client.entity.ClientEntityActionPacket;
import ch.spacebase.mc.protocol.packet.ingame.client.entity.ClientEntityInteractPacket;
import ch.spacebase.mc.protocol.packet.ingame.client.entity.player.ClientChangeHeldItemPacket;
import ch.spacebase.mc.protocol.packet.ingame.client.entity.player.ClientPlayerAbilitiesPacket;
import ch.spacebase.mc.protocol.packet.ingame.client.entity.player.ClientPlayerDigPacket;
import ch.spacebase.mc.protocol.packet.ingame.client.entity.player.ClientPlayerMovementPacket;
import ch.spacebase.mc.protocol.packet.ingame.client.entity.player.ClientPlayerPlaceBlockPacket;
import ch.spacebase.mc.protocol.packet.ingame.client.entity.player.ClientPlayerPositionPacket;
import ch.spacebase.mc.protocol.packet.ingame.client.entity.player.ClientPlayerPositionRotationPacket;
import ch.spacebase.mc.protocol.packet.ingame.client.entity.player.ClientPlayerRotationPacket;
import ch.spacebase.mc.protocol.packet.ingame.client.entity.player.ClientSteerVehiclePacket;
import ch.spacebase.mc.protocol.packet.ingame.client.window.ClientCloseWindowPacket;
import ch.spacebase.mc.protocol.packet.ingame.client.window.ClientConfirmTransactionPacket;
import ch.spacebase.mc.protocol.packet.ingame.client.window.ClientCreativeInventoryActionPacket;
import ch.spacebase.mc.protocol.packet.ingame.client.window.ClientEnchantItemPacket;
import ch.spacebase.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;
import ch.spacebase.mc.protocol.packet.ingame.client.world.ClientUpdateSignPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.ServerChatPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.ServerDisconnectPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import ch.spacebase.mc.protocol.packet.ingame.server.ServerKeepAlivePacket;
import ch.spacebase.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.ServerPluginMessagePacket;
import ch.spacebase.mc.protocol.packet.ingame.server.ServerRespawnPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.ServerStatisticsPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.ServerTabCompletePacket;
import ch.spacebase.mc.protocol.packet.ingame.server.entity.ServerAnimationPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.entity.ServerCollectItemPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.entity.ServerDestroyEntitiesPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.entity.ServerEntityAttachPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.entity.ServerEntityEffectPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.entity.ServerEntityEquipmentPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.entity.ServerEntityHeadLookPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.entity.ServerEntityMetadataPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.entity.ServerEntityMovementPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.entity.ServerEntityPositionPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.entity.ServerEntityPositionRotationPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.entity.ServerEntityPropertiesPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.entity.ServerEntityRotationPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.entity.ServerEntityStatusPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.entity.ServerEntityTeleportPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.entity.ServerEntityVelocityPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.entity.ServerEntityRemoveEffectPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.entity.player.ServerChangeHeldItemPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.entity.player.ServerPlayerAbilitiesPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.entity.player.ServerPlayerUseBedPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.entity.player.ServerSetExperiencePacket;
import ch.spacebase.mc.protocol.packet.ingame.server.entity.player.ServerUpdateHealthPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnExpOrbPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnGlobalEntityPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPaintingPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.scoreboard.ServerDisplayScoreboardPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.scoreboard.ServerScoreboardObjectivePacket;
import ch.spacebase.mc.protocol.packet.ingame.server.scoreboard.ServerTeamPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.scoreboard.ServerUpdateScorePacket;
import ch.spacebase.mc.protocol.packet.ingame.server.window.ServerCloseWindowPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.window.ServerConfirmTransactionPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.window.ServerOpenWindowPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.window.ServerWindowItemsPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.window.ServerWindowPropertyPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.world.ServerBlockBreakAnimPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.world.ServerBlockChangePacket;
import ch.spacebase.mc.protocol.packet.ingame.server.world.ServerBlockValuePacket;
import ch.spacebase.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.world.ServerExplosionPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.world.ServerMapDataPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket;
import ch.spacebase.mc.protocol.packet.ingame.server.world.ServerMultiChunkDataPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.world.ServerNotifyClientPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.world.ServerOpenTileEntityEditorPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.world.ServerPlayEffectPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.world.ServerPlaySoundPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.world.ServerUpdateSignPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.world.ServerSpawnParticlePacket;
import ch.spacebase.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.world.ServerUpdateTileEntityPacket;
import ch.spacebase.mc.protocol.packet.ingame.server.world.ServerUpdateTimePacket;
import ch.spacebase.mc.protocol.packet.login.client.EncryptionResponsePacket;
import ch.spacebase.mc.protocol.packet.login.client.LoginStartPacket;
import ch.spacebase.mc.protocol.packet.login.server.EncryptionRequestPacket;
import ch.spacebase.mc.protocol.packet.login.server.LoginDisconnectPacket;
import ch.spacebase.mc.protocol.packet.login.server.LoginSuccessPacket;
import ch.spacebase.mc.protocol.packet.status.client.StatusPingPacket;
import ch.spacebase.mc.protocol.packet.status.client.StatusQueryPacket;
import ch.spacebase.mc.protocol.packet.status.server.StatusPongPacket;
import ch.spacebase.mc.protocol.packet.status.server.StatusResponsePacket;
import ch.spacebase.packetlib.Client;
import ch.spacebase.packetlib.Server;
import ch.spacebase.packetlib.Session;
import ch.spacebase.packetlib.crypt.AESEncryption;
import ch.spacebase.packetlib.crypt.PacketEncryption;
import ch.spacebase.packetlib.packet.PacketProtocol;

public class MinecraftProtocol extends PacketProtocol {
	
	private ProtocolMode mode = ProtocolMode.HANDSHAKE;
	private AESEncryption encrypt = null;
	
	private GameProfile profile = null;
	private ClientListener clientListener = null;
	
	@SuppressWarnings("unused")
	private MinecraftProtocol() {
	}
	
	public MinecraftProtocol(ProtocolMode mode) {
		if(mode != ProtocolMode.LOGIN && mode != ProtocolMode.STATUS) {
			throw new IllegalArgumentException("Only login and status modes are permitted.");
		}
		
		this.mode = mode;
		if(mode == ProtocolMode.LOGIN) {
			this.profile = new GameProfile("", "Player");
		}
		
		this.clientListener = new ClientListener("");
	}
	
	public MinecraftProtocol(String username) {
		this(ProtocolMode.LOGIN);
		this.profile = new GameProfile("", username);
		this.clientListener = new ClientListener("");
	}
	
	public MinecraftProtocol(String username, String using, boolean token) throws AuthenticationException {
		this(ProtocolMode.LOGIN);
		String clientToken = UUID.randomUUID().toString();
		UserAuthentication auth = new UserAuthentication(clientToken);
		auth.setUsername(username);
		if(token) {
			auth.setAccessToken(using);
		} else {
			auth.setPassword(using);
		}
		
		auth.login();
		this.profile = auth.getSelectedProfile();
		this.clientListener = new ClientListener(auth.getAccessToken());
	}
	
	@Override
	public PacketEncryption getEncryption() {
		return this.encrypt;
	}
	
	@Override
	public void newClientSession(Client client, Session session) {
		if(this.profile != null) {
			session.setFlag(ProtocolConstants.PROFILE_KEY, this.profile);
		}
		
		this.setMode(this.mode, true, session);
		session.addListener(this.clientListener);
	}

	@Override
	public void newServerSession(Server server, Session session) {
		this.setMode(ProtocolMode.HANDSHAKE, false, session);
		session.addListener(new ServerListener());
	}
	
	protected void enableEncryption(Key key) {
		try {
			this.encrypt = new AESEncryption(key);
		} catch(GeneralSecurityException e) {
			throw new Error("Failed to enable protocol encryption.", e);
		}
	}
	
	public ProtocolMode getMode() {
		return this.mode;
	}
	
	protected void setMode(ProtocolMode mode, boolean client, Session session) {
		this.clearPackets();
		switch(mode) {
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
		
		this.mode = mode;
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
		
		this.registerOutgoing(0, LoginStartPacket.class);
		this.registerOutgoing(1, EncryptionResponsePacket.class);
	}
	
	private void initServerLogin(Session session) {
		this.registerIncoming(0, LoginStartPacket.class);
		this.registerIncoming(1, EncryptionResponsePacket.class);
		
		this.registerOutgoing(0, LoginDisconnectPacket.class);
		this.registerOutgoing(1, EncryptionRequestPacket.class);
		this.registerOutgoing(2, LoginSuccessPacket.class);
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
		
		this.registerOutgoing(0, ClientKeepAlivePacket.class);
		this.registerOutgoing(1, ClientChatPacket.class);
		this.registerOutgoing(2, ClientEntityInteractPacket.class);
		this.registerOutgoing(3, ClientPlayerMovementPacket.class);
		this.registerOutgoing(4, ClientPlayerPositionPacket.class);
		this.registerOutgoing(5, ClientPlayerRotationPacket.class);
		this.registerOutgoing(6, ClientPlayerPositionRotationPacket.class);
		this.registerOutgoing(7, ClientPlayerDigPacket.class);
		this.registerOutgoing(8, ClientPlayerPlaceBlockPacket.class);
		this.registerOutgoing(9, ClientChangeHeldItemPacket.class);
		this.registerOutgoing(10, ClientAnimationPacket.class);
		this.registerOutgoing(11, ClientEntityActionPacket.class);
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
	}
	
	private void initServerGame(Session session) {
		this.registerIncoming(0, ClientKeepAlivePacket.class);
		this.registerIncoming(1, ClientChatPacket.class);
		this.registerIncoming(2, ClientEntityInteractPacket.class);
		this.registerIncoming(3, ClientPlayerMovementPacket.class);
		this.registerIncoming(4, ClientPlayerPositionPacket.class);
		this.registerIncoming(5, ClientPlayerRotationPacket.class);
		this.registerIncoming(6, ClientPlayerPositionRotationPacket.class);
		this.registerIncoming(7, ClientPlayerDigPacket.class);
		this.registerIncoming(8, ClientPlayerPlaceBlockPacket.class);
		this.registerIncoming(9, ClientChangeHeldItemPacket.class);
		this.registerIncoming(10, ClientAnimationPacket.class);
		this.registerIncoming(11, ClientEntityActionPacket.class);
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
