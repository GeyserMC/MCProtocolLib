package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3d;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.Hand;

@Data
@With
@AllArgsConstructor
public class ServerboundInteractPacket implements MinecraftPacket {
    private final int entityId;
    private final Hand hand;
    private final Vector3d location;
    private final boolean isSneaking;

    public ServerboundInteractPacket(int entityId, Vector3d location, boolean isSneaking) {
        this(entityId, Hand.MAIN_HAND, location, isSneaking);
    }

    public ServerboundInteractPacket(ByteBuf in) {
        this.entityId = MinecraftTypes.readVarInt(in);
        this.hand = Hand.from(MinecraftTypes.readVarInt(in));
        this.location = MinecraftTypes.readLpVec3(in);
        this.isSneaking = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.entityId);
        MinecraftTypes.writeVarInt(out, this.hand.ordinal());
        MinecraftTypes.writeLpVec3(out, this.location);
        out.writeBoolean(this.isSneaking);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
