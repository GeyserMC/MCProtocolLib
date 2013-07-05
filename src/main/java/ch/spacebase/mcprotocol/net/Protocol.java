package ch.spacebase.mcprotocol.net;

import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.standard.packet.PacketAnimation;
import ch.spacebase.mcprotocol.standard.packet.PacketAttachEntity;
import ch.spacebase.mcprotocol.standard.packet.PacketBlockAction;
import ch.spacebase.mcprotocol.standard.packet.PacketBlockBreakAnimation;
import ch.spacebase.mcprotocol.standard.packet.PacketBlockChange;
import ch.spacebase.mcprotocol.standard.packet.PacketChat;
import ch.spacebase.mcprotocol.standard.packet.PacketClientInfo;
import ch.spacebase.mcprotocol.standard.packet.PacketClientStatus;
import ch.spacebase.mcprotocol.standard.packet.PacketCloseWindow;
import ch.spacebase.mcprotocol.standard.packet.PacketCollectItem;
import ch.spacebase.mcprotocol.standard.packet.PacketConfirmTransaction;
import ch.spacebase.mcprotocol.standard.packet.PacketCreativeSlot;
import ch.spacebase.mcprotocol.standard.packet.PacketDestroyEntity;
import ch.spacebase.mcprotocol.standard.packet.PacketDisconnect;
import ch.spacebase.mcprotocol.standard.packet.PacketDisplayScoreboard;
import ch.spacebase.mcprotocol.standard.packet.PacketEffect;
import ch.spacebase.mcprotocol.standard.packet.PacketEnchantItem;
import ch.spacebase.mcprotocol.standard.packet.PacketEntity;
import ch.spacebase.mcprotocol.standard.packet.PacketEntityAction;
import ch.spacebase.mcprotocol.standard.packet.PacketEntityEffect;
import ch.spacebase.mcprotocol.standard.packet.PacketEntityEquipment;
import ch.spacebase.mcprotocol.standard.packet.PacketEntityHeadYaw;
import ch.spacebase.mcprotocol.standard.packet.PacketEntityLook;
import ch.spacebase.mcprotocol.standard.packet.PacketEntityLookRelativeMove;
import ch.spacebase.mcprotocol.standard.packet.PacketEntityMetadata;
import ch.spacebase.mcprotocol.standard.packet.PacketEntityProperties;
import ch.spacebase.mcprotocol.standard.packet.PacketEntityRelativeMove;
import ch.spacebase.mcprotocol.standard.packet.PacketEntityStatus;
import ch.spacebase.mcprotocol.standard.packet.PacketEntityTeleport;
import ch.spacebase.mcprotocol.standard.packet.PacketEntityVelocity;
import ch.spacebase.mcprotocol.standard.packet.PacketExplosion;
import ch.spacebase.mcprotocol.standard.packet.PacketGameState;
import ch.spacebase.mcprotocol.standard.packet.PacketHandshake;
import ch.spacebase.mcprotocol.standard.packet.PacketHealthUpdate;
import ch.spacebase.mcprotocol.standard.packet.PacketHeldItemChange;
import ch.spacebase.mcprotocol.standard.packet.PacketIncrementStatistic;
import ch.spacebase.mcprotocol.standard.packet.PacketItemData;
import ch.spacebase.mcprotocol.standard.packet.PacketKeepAlive;
import ch.spacebase.mcprotocol.standard.packet.PacketKeyRequest;
import ch.spacebase.mcprotocol.standard.packet.PacketKeyResponse;
import ch.spacebase.mcprotocol.standard.packet.PacketLightning;
import ch.spacebase.mcprotocol.standard.packet.PacketLogin;
import ch.spacebase.mcprotocol.standard.packet.PacketMapChunk;
import ch.spacebase.mcprotocol.standard.packet.PacketMapChunkBulk;
import ch.spacebase.mcprotocol.standard.packet.PacketMultiBlockChange;
import ch.spacebase.mcprotocol.standard.packet.PacketNamedSound;
import ch.spacebase.mcprotocol.standard.packet.PacketOpenWindow;
import ch.spacebase.mcprotocol.standard.packet.PacketPlayer;
import ch.spacebase.mcprotocol.standard.packet.PacketPlayerAbilities;
import ch.spacebase.mcprotocol.standard.packet.PacketPlayerBlockPlace;
import ch.spacebase.mcprotocol.standard.packet.PacketPlayerDigging;
import ch.spacebase.mcprotocol.standard.packet.PacketPlayerListItem;
import ch.spacebase.mcprotocol.standard.packet.PacketPlayerLook;
import ch.spacebase.mcprotocol.standard.packet.PacketPlayerPosition;
import ch.spacebase.mcprotocol.standard.packet.PacketPlayerPositionLook;
import ch.spacebase.mcprotocol.standard.packet.PacketPluginMessage;
import ch.spacebase.mcprotocol.standard.packet.PacketRemoveEntityEffect;
import ch.spacebase.mcprotocol.standard.packet.PacketRespawn;
import ch.spacebase.mcprotocol.standard.packet.PacketScoreboardObjective;
import ch.spacebase.mcprotocol.standard.packet.PacketServerPing;
import ch.spacebase.mcprotocol.standard.packet.PacketSetExperience;
import ch.spacebase.mcprotocol.standard.packet.PacketSetSlot;
import ch.spacebase.mcprotocol.standard.packet.PacketSpawnExpOrb;
import ch.spacebase.mcprotocol.standard.packet.PacketSpawnMob;
import ch.spacebase.mcprotocol.standard.packet.PacketSpawnNamedEntity;
import ch.spacebase.mcprotocol.standard.packet.PacketSpawnPainting;
import ch.spacebase.mcprotocol.standard.packet.PacketSpawnPosition;
import ch.spacebase.mcprotocol.standard.packet.PacketSpawnObject;
import ch.spacebase.mcprotocol.standard.packet.PacketSteerVehicle;
import ch.spacebase.mcprotocol.standard.packet.PacketTabComplete;
import ch.spacebase.mcprotocol.standard.packet.PacketTeam;
import ch.spacebase.mcprotocol.standard.packet.PacketTimeUpdate;
import ch.spacebase.mcprotocol.standard.packet.PacketUpdateScoreboard;
import ch.spacebase.mcprotocol.standard.packet.PacketUpdateSign;
import ch.spacebase.mcprotocol.standard.packet.PacketUpdateTileEntity;
import ch.spacebase.mcprotocol.standard.packet.PacketUseBed;
import ch.spacebase.mcprotocol.standard.packet.PacketUseEntity;
import ch.spacebase.mcprotocol.standard.packet.PacketWindowClick;
import ch.spacebase.mcprotocol.standard.packet.PacketWindowItems;
import ch.spacebase.mcprotocol.standard.packet.PacketWindowProperty;

public enum Protocol {

	/**
	 * The standard Minecraft protocol.
	 */
	STANDARD,
	
	/**
	 * The classic Minecraft protocol.
	 */
	CLASSIC,
	
	/**
	 * The Minecraft Pocket Edition protocol.
	 */
	POCKET;

	static {
		// standard protocol
		STANDARD.registerPacket(0, PacketKeepAlive.class);
		STANDARD.registerPacket(1, PacketLogin.class);
		STANDARD.registerPacket(2, PacketHandshake.class);
		STANDARD.registerPacket(3, PacketChat.class);
		STANDARD.registerPacket(4, PacketTimeUpdate.class);
		STANDARD.registerPacket(5, PacketEntityEquipment.class);
		STANDARD.registerPacket(6, PacketSpawnPosition.class);
		STANDARD.registerPacket(7, PacketUseEntity.class);
		STANDARD.registerPacket(8, PacketHealthUpdate.class);
		STANDARD.registerPacket(9, PacketRespawn.class);
		STANDARD.registerPacket(10, PacketPlayer.class);
		STANDARD.registerPacket(11, PacketPlayerPosition.class);
		STANDARD.registerPacket(12, PacketPlayerLook.class);
		STANDARD.registerPacket(13, PacketPlayerPositionLook.class);
		STANDARD.registerPacket(14, PacketPlayerDigging.class);
		STANDARD.registerPacket(15, PacketPlayerBlockPlace.class);
		STANDARD.registerPacket(16, PacketHeldItemChange.class);
		STANDARD.registerPacket(17, PacketUseBed.class);
		STANDARD.registerPacket(18, PacketAnimation.class);
		STANDARD.registerPacket(19, PacketEntityAction.class);
		STANDARD.registerPacket(20, PacketSpawnNamedEntity.class);
		STANDARD.registerPacket(22, PacketCollectItem.class);
		STANDARD.registerPacket(23, PacketSpawnObject.class);
		STANDARD.registerPacket(24, PacketSpawnMob.class);
		STANDARD.registerPacket(25, PacketSpawnPainting.class);
		STANDARD.registerPacket(26, PacketSpawnExpOrb.class);
		STANDARD.registerPacket(27, PacketSteerVehicle.class);
		STANDARD.registerPacket(28, PacketEntityVelocity.class);
		STANDARD.registerPacket(29, PacketDestroyEntity.class);
		STANDARD.registerPacket(30, PacketEntity.class);
		STANDARD.registerPacket(31, PacketEntityRelativeMove.class);
		STANDARD.registerPacket(32, PacketEntityLook.class);
		STANDARD.registerPacket(33, PacketEntityLookRelativeMove.class);
		STANDARD.registerPacket(34, PacketEntityTeleport.class);
		STANDARD.registerPacket(35, PacketEntityHeadYaw.class);
		STANDARD.registerPacket(38, PacketEntityStatus.class);
		STANDARD.registerPacket(39, PacketAttachEntity.class);
		STANDARD.registerPacket(40, PacketEntityMetadata.class);
		STANDARD.registerPacket(41, PacketEntityEffect.class);
		STANDARD.registerPacket(42, PacketRemoveEntityEffect.class);
		STANDARD.registerPacket(43, PacketSetExperience.class);
		STANDARD.registerPacket(44, PacketEntityProperties.class);
		STANDARD.registerPacket(51, PacketMapChunk.class);
		STANDARD.registerPacket(52, PacketMultiBlockChange.class);
		STANDARD.registerPacket(53, PacketBlockChange.class);
		STANDARD.registerPacket(54, PacketBlockAction.class);
		STANDARD.registerPacket(55, PacketBlockBreakAnimation.class);
		STANDARD.registerPacket(56, PacketMapChunkBulk.class);
		STANDARD.registerPacket(60, PacketExplosion.class);
		STANDARD.registerPacket(61, PacketEffect.class);
		STANDARD.registerPacket(62, PacketNamedSound.class);
		STANDARD.registerPacket(70, PacketGameState.class);
		STANDARD.registerPacket(71, PacketLightning.class);
		STANDARD.registerPacket(100, PacketOpenWindow.class);
		STANDARD.registerPacket(101, PacketCloseWindow.class);
		STANDARD.registerPacket(102, PacketWindowClick.class);
		STANDARD.registerPacket(103, PacketSetSlot.class);
		STANDARD.registerPacket(104, PacketWindowItems.class);
		STANDARD.registerPacket(105, PacketWindowProperty.class);
		STANDARD.registerPacket(106, PacketConfirmTransaction.class);
		STANDARD.registerPacket(107, PacketCreativeSlot.class);
		STANDARD.registerPacket(108, PacketEnchantItem.class);
		STANDARD.registerPacket(130, PacketUpdateSign.class);
		STANDARD.registerPacket(131, PacketItemData.class);
		STANDARD.registerPacket(132, PacketUpdateTileEntity.class);
		STANDARD.registerPacket(200, PacketIncrementStatistic.class);
		STANDARD.registerPacket(201, PacketPlayerListItem.class);
		STANDARD.registerPacket(202, PacketPlayerAbilities.class);
		STANDARD.registerPacket(203, PacketTabComplete.class);
		STANDARD.registerPacket(204, PacketClientInfo.class);
		STANDARD.registerPacket(205, PacketClientStatus.class);
		STANDARD.registerPacket(206, PacketScoreboardObjective.class);
		STANDARD.registerPacket(207, PacketUpdateScoreboard.class);
		STANDARD.registerPacket(208, PacketDisplayScoreboard.class);
		STANDARD.registerPacket(209, PacketTeam.class);
		STANDARD.registerPacket(250, PacketPluginMessage.class);
		STANDARD.registerPacket(252, PacketKeyResponse.class);
		STANDARD.registerPacket(253, PacketKeyRequest.class);
		STANDARD.registerPacket(254, PacketServerPing.class);
		STANDARD.registerPacket(255, PacketDisconnect.class);

		// classic protocol
		// TODO

		// pocket protocol
		// TODO
	}

	/**
	 * The packet registry of the protocol type.
	 */
	@SuppressWarnings("unchecked")
	private final Class<? extends Packet> packets[] = new Class[256];

	/**
	 * Registers a packet to the protocol type.
	 * @param id Id of the packet.
	 * @param packet Class of the packet.
	 */
	public void registerPacket(int id, Class<? extends Packet> packet) {
		this.packets[id] = packet;
	}

	/**
	 * Gets a packet class from the given id.
	 * @param id Id of the packet.
	 * @return The packet class.
	 */
	public Class<? extends Packet> getPacket(int id) {
		try {
			return this.packets[id];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
}
