package com.github.steveice10.mc.protocol.packet.ingame.serverbound.inventory;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

import java.io.IOException;

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
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.slotId);
        helper.writeVarInt(out, this.containerId);
        out.writeBoolean(this.newState);
    }
}
