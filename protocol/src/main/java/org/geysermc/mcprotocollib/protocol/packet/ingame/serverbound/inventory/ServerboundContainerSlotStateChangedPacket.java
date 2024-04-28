package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundContainerSlotStateChangedPacket implements MinecraftPacket {

    private final int slotId;
    private final int containerId;
    private final boolean newState;

    public ServerboundContainerSlotStateChangedPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.slotId = helper.readVarInt(in);
        this.containerId = helper.readVarInt(in);
        this.newState = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.slotId);
        helper.writeVarInt(out, this.containerId);
        out.writeBoolean(this.newState);
    }
}
