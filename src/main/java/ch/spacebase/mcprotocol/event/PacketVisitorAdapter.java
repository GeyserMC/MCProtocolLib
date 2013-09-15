package ch.spacebase.mcprotocol.event;

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
import ch.spacebase.mcprotocol.standard.packet.PacketEntityAttributes;
import ch.spacebase.mcprotocol.standard.packet.PacketEntityEffect;
import ch.spacebase.mcprotocol.standard.packet.PacketEntityEquipment;
import ch.spacebase.mcprotocol.standard.packet.PacketEntityHeadYaw;
import ch.spacebase.mcprotocol.standard.packet.PacketEntityLook;
import ch.spacebase.mcprotocol.standard.packet.PacketEntityLookRelativeMove;
import ch.spacebase.mcprotocol.standard.packet.PacketEntityMetadata;
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
import ch.spacebase.mcprotocol.standard.packet.PacketOpenTileEditor;
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
import ch.spacebase.mcprotocol.standard.packet.PacketSpawnObject;
import ch.spacebase.mcprotocol.standard.packet.PacketSpawnPainting;
import ch.spacebase.mcprotocol.standard.packet.PacketSpawnParticle;
import ch.spacebase.mcprotocol.standard.packet.PacketSpawnPosition;
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

/**
 * Empty implementation of the PacketVisitor interface for convenience.
 * Usually used when most methods are not implemented.
 * 
 * @author dconnor
 */
public class PacketVisitorAdapter implements PacketVisitor {

    public void visit(PacketKeepAlive packet) {
    }

    public void visit(PacketLogin packet) {
    }

    public void visit(PacketHandshake packet) {
    }

    public void visit(PacketChat packet) {
    }

    public void visit(PacketTimeUpdate packet) {
    }

    public void visit(PacketEntityEquipment packet) {
    }

    public void visit(PacketSpawnPosition packet) {
    }

    public void visit(PacketUseEntity packet) {
    }

    public void visit(PacketHealthUpdate packet) {
    }

    public void visit(PacketRespawn packet) {
    }

    public void visit(PacketPlayer packet) {
    }

    public void visit(PacketPlayerPosition packet) {
    }

    public void visit(PacketPlayerLook packet) {
    }

    public void visit(PacketPlayerPositionLook packet) {
    }

    public void visit(PacketPlayerDigging packet) {
    }

    public void visit(PacketPlayerBlockPlace packet) {
    }

    public void visit(PacketHeldItemChange packet) {
    }

    public void visit(PacketUseBed packet) {
    }

    public void visit(PacketAnimation packet) {
    }

    public void visit(PacketEntityAction packet) {
    }

    public void visit(PacketSpawnNamedEntity packet) {
    }

    public void visit(PacketCollectItem packet) {
    }

    public void visit(PacketSpawnObject packet) {
    }

    public void visit(PacketSpawnMob packet) {
    }

    public void visit(PacketSpawnPainting packet) {
    }

    public void visit(PacketSpawnExpOrb packet) {
    }

    public void visit(PacketSteerVehicle packet) {
    }

    public void visit(PacketEntityVelocity packet) {
    }

    public void visit(PacketDestroyEntity packet) {
    }

    public void visit(PacketEntity packet) {
    }

    public void visit(PacketEntityRelativeMove packet) {
    }

    public void visit(PacketEntityLook packet) {
    }

    public void visit(PacketEntityLookRelativeMove packet) {
    }

    public void visit(PacketEntityTeleport packet) {
    }

    public void visit(PacketEntityHeadYaw packet) {
    }

    public void visit(PacketEntityStatus packet) {
    }

    public void visit(PacketAttachEntity packet) {
    }

    public void visit(PacketEntityMetadata packet) {
    }

    public void visit(PacketEntityEffect packet) {
    }

    public void visit(PacketRemoveEntityEffect packet) {
    }

    public void visit(PacketSetExperience packet) {
    }

    public void visit(PacketEntityAttributes packet) {
    }

    public void visit(PacketMapChunk packet) {
    }

    public void visit(PacketMultiBlockChange packet) {
    }

    public void visit(PacketBlockChange packet) {
    }

    public void visit(PacketBlockAction packet) {
    }

    public void visit(PacketBlockBreakAnimation packet) {
    }

    public void visit(PacketMapChunkBulk packet) {
    }

    public void visit(PacketExplosion packet) {
    }

    public void visit(PacketEffect packet) {
    }

    public void visit(PacketNamedSound packet) {
    }

    public void visit(PacketSpawnParticle packet) {
    }

    public void visit(PacketGameState packet) {
    }

    public void visit(PacketLightning packet) {
    }

    public void visit(PacketOpenWindow packet) {
    }

    public void visit(PacketCloseWindow packet) {
    }

    public void visit(PacketWindowClick packet) {
    }

    public void visit(PacketSetSlot packet) {
    }

    public void visit(PacketWindowItems packet) {
    }

    public void visit(PacketWindowProperty packet) {
    }

    public void visit(PacketConfirmTransaction packet) {
    }

    public void visit(PacketCreativeSlot packet) {
    }

    public void visit(PacketEnchantItem packet) {
    }

    public void visit(PacketUpdateSign packet) {
    }

    public void visit(PacketItemData packet) {
    }

    public void visit(PacketUpdateTileEntity packet) {
    }

    public void visit(PacketOpenTileEditor packet) {
    }

    public void visit(PacketIncrementStatistic packet) {
    }

    public void visit(PacketPlayerListItem packet) {
    }

    public void visit(PacketPlayerAbilities packet) {
    }

    public void visit(PacketTabComplete packet) {
    }

    public void visit(PacketClientInfo packet) {
    }

    public void visit(PacketClientStatus packet) {
    }

    public void visit(PacketScoreboardObjective packet) {
    }

    public void visit(PacketUpdateScoreboard packet) {
    }

    public void visit(PacketDisplayScoreboard packet) {
    }

    public void visit(PacketTeam packet) {
    }

    public void visit(PacketPluginMessage packet) {
    }

    public void visit(PacketKeyResponse packet) {
    }

    public void visit(PacketKeyRequest packet) {
    }

    public void visit(PacketServerPing packet) {
    }

    public void visit(PacketDisconnect packet) {
    }
}
