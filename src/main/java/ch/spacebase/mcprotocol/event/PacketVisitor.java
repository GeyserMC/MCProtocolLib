package ch.spacebase.mcprotocol.event;

import ch.spacebase.mcprotocol.standard.packet.*;

/**
 * Visitor interface for each packet
 * 
 * @author dconnor
 */
public interface PacketVisitor {

    public void visit(PacketKeepAlive packet);

    public void visit(PacketLogin packet);

    public void visit(PacketHandshake packet);

    public void visit(PacketChat packet);

    public void visit(PacketTimeUpdate packet);

    public void visit(PacketEntityEquipment packet);

    public void visit(PacketSpawnPosition packet);

    public void visit(PacketUseEntity packet);

    public void visit(PacketHealthUpdate packet);

    public void visit(PacketRespawn packet);

    public void visit(PacketPlayer packet);

    public void visit(PacketPlayerPosition packet);

    public void visit(PacketPlayerLook packet);

    public void visit(PacketPlayerPositionLook packet);

    public void visit(PacketPlayerDigging packet);

    public void visit(PacketPlayerBlockPlace packet);

    public void visit(PacketHeldItemChange packet);

    public void visit(PacketUseBed packet);

    public void visit(PacketAnimation packet);

    public void visit(PacketEntityAction packet);

    public void visit(PacketSpawnNamedEntity packet);

    public void visit(PacketCollectItem packet);

    public void visit(PacketSpawnObject packet);

    public void visit(PacketSpawnMob packet);

    public void visit(PacketSpawnPainting packet);

    public void visit(PacketSpawnExpOrb packet);

    public void visit(PacketSteerVehicle packet);

    public void visit(PacketEntityVelocity packet);

    public void visit(PacketDestroyEntity packet);

    public void visit(PacketEntity packet);

    public void visit(PacketEntityRelativeMove packet);

    public void visit(PacketEntityLook packet);

    public void visit(PacketEntityLookRelativeMove packet);

    public void visit(PacketEntityTeleport packet);

    public void visit(PacketEntityHeadYaw packet);

    public void visit(PacketEntityStatus packet);

    public void visit(PacketAttachEntity packet);

    public void visit(PacketEntityMetadata packet);

    public void visit(PacketEntityEffect packet);

    public void visit(PacketRemoveEntityEffect packet);

    public void visit(PacketSetExperience packet);

    public void visit(PacketEntityAttributes packet);

    public void visit(PacketMapChunk packet);

    public void visit(PacketMultiBlockChange packet);

    public void visit(PacketBlockChange packet);

    public void visit(PacketBlockAction packet);

    public void visit(PacketBlockBreakAnimation packet);

    public void visit(PacketMapChunkBulk packet);

    public void visit(PacketExplosion packet);

    public void visit(PacketEffect packet);

    public void visit(PacketNamedSound packet);

    public void visit(PacketSpawnParticle packet);

    public void visit(PacketGameState packet);

    public void visit(PacketLightning packet);

    public void visit(PacketOpenWindow packet);

    public void visit(PacketCloseWindow packet);

    public void visit(PacketWindowClick packet);

    public void visit(PacketSetSlot packet);

    public void visit(PacketWindowItems packet);

    public void visit(PacketWindowProperty packet);

    public void visit(PacketConfirmTransaction packet);

    public void visit(PacketCreativeSlot packet);

    public void visit(PacketEnchantItem packet);

    public void visit(PacketUpdateSign packet);

    public void visit(PacketItemData packet);

    public void visit(PacketUpdateTileEntity packet);

    public void visit(PacketOpenTileEditor packet);

    public void visit(PacketIncrementStatistic packet);

    public void visit(PacketPlayerListItem packet);

    public void visit(PacketPlayerAbilities packet);

    public void visit(PacketTabComplete packet);

    public void visit(PacketClientInfo packet);

    public void visit(PacketClientStatus packet);

    public void visit(PacketScoreboardObjective packet);

    public void visit(PacketUpdateScoreboard packet);

    public void visit(PacketDisplayScoreboard packet);

    public void visit(PacketTeam packet);

    public void visit(PacketPluginMessage packet);

    public void visit(PacketKeyResponse packet);

    public void visit(PacketKeyRequest packet);

    public void visit(PacketServerPing packet);

    public void visit(PacketDisconnect packet);
}
