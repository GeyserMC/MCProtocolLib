package ch.spacebase.mcprotocol.net.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;

public abstract class Packet {

	@SuppressWarnings("unchecked")
	private static final Class<? extends Packet> packets[] = new Class[256];
	
	static {
		register(0, PacketKeepAlive.class);
		register(1, PacketLogin.class);
		register(2, PacketHandshake.class);
		register(3, PacketChat.class);
        register(4, PacketTimeUpdate.class);
        register(5, PacketEntityEquipment.class);
        register(6, PacketSpawnPosition.class);
        register(7, PacketUseEntity.class);
        register(8, PacketHealthUpdate.class);
        register(9, PacketRespawn.class);
        register(10, PacketPlayer.class);
        register(11, PacketPlayerPosition.class);
        register(12, PacketPlayerLook.class);
        register(13, PacketPlayerPositionLook.class);
        register(14, PacketPlayerDigging.class);
        register(15, PacketPlayerBlockPlace.class);
        register(16, PacketHeldItemChange.class);
        register(17, PacketUseBed.class);
        register(18, PacketAnimation.class);
        register(19, PacketEntityAction.class);
        register(20, PacketSpawnNamedEntity.class);
        register(21, PacketSpawnDroppedItem.class);
        register(22, PacketCollectItem.class);
        register(23, PacketSpawnVehicle.class);
        register(24, PacketSpawnMob.class);
        register(25, PacketSpawnPainting.class);
        register(26, PacketSpawnExpOrb.class);
        register(28, PacketEntityVelocity.class);
        register(29, PacketDestroyEntity.class);
        register(30, PacketEntity.class);
        register(31, PacketEntityRelativeMove.class);
        register(32, PacketEntityLook.class);
        register(33, PacketEntityLookRelativeMove.class);
        register(34, PacketEntityTeleport.class);
        register(35, PacketEntityHeadYaw.class);
        register(38, PacketEntityStatus.class);
        register(39, PacketAttachEntity.class);
        register(40, PacketEntityMetadata.class);
        register(41, PacketEntityEffect.class);
        register(42, PacketRemoveEntityEffect.class);
        register(43, PacketSetExperience.class);
        register(51, PacketMapChunk.class);
        register(52, PacketMultiBlockChange.class);
        register(53, PacketBlockChange.class);
        register(54, PacketBlockAction.class);
        register(55, PacketBlockBreakAnimation.class);
        register(56, PacketMapChunkBulk.class);
        register(60, PacketExplosion.class);
        register(61, PacketEffect.class);
        register(62, PacketNamedSound.class);
        register(70, PacketGameState.class);
        register(71, PacketLightning.class);
        register(100, PacketOpenWindow.class);
        register(101, PacketCloseWindow.class);
        register(102, PacketWindowClick.class);
        register(103, PacketSetSlot.class);
        register(104, PacketWindowItems.class);
        register(105, PacketWindowProperty.class);
        register(106, PacketConfirmTransaction.class);
        register(107, PacketCreativeSlot.class);
        register(108, PacketEnchantItem.class);
        register(130, PacketUpdateSign.class);
        register(131, PacketItemData.class);
        register(132, PacketUpdateTileEntity.class);
        register(200, PacketIncrementStatistic.class);
        register(201, PacketPlayerListItem.class);
        register(202, PacketPlayerAbilities.class);
        register(203, PacketTabComplete.class);
        register(204, PacketClientInfo.class);
		register(205, PacketClientStatus.class);
		register(250, PacketPluginMessage.class);
		register(252, PacketKeyResponse.class);
		register(253, PacketKeyRequest.class);
		register(254, PacketServerPing.class);
		register(255, PacketDisconnect.class);
	}
	
	public static void register(int id, Class<? extends Packet> packet) {
		packets[id] = packet;
	}
	
	public static Class<? extends Packet> get(int id) {
		try {
			return packets[id];
		} catch(ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public Packet() {
	}
	
	public abstract void read(DataInputStream in) throws IOException;
	
	public abstract void write(DataOutputStream out) throws IOException;
	
	public abstract void handleClient(Client conn);
	
	public abstract void handleServer(ServerConnection conn);
	
	public abstract int getId();
	
}
