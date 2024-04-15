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
public class ServerboundContainerButtonClickPacket implements MinecraftPacket {
    private final int containerId;
    private final int buttonId;

    public ServerboundContainerButtonClickPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.containerId = helper.readVarInt(in);
        this.buttonId = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.containerId);
        helper.writeVarInt(out, this.buttonId);
    }
}
