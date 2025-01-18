package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ServerboundContainerSlotStateChangedPacket implements MinecraftPacket {

    private final int slotId;
    private final int containerId;
    private final boolean newState;

    public ServerboundContainerSlotStateChangedPacket(ByteBuf in) {
        this.slotId = MinecraftTypes.readVarInt(in);
        this.containerId = MinecraftTypes.readVarInt(in);
        this.newState = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.slotId);
        MinecraftTypes.writeVarInt(out, this.containerId);
        out.writeBoolean(this.newState);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
