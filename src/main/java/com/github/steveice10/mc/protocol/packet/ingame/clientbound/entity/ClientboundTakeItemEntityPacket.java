package com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity;

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
public class ClientboundTakeItemEntityPacket implements MinecraftPacket {
    private final int collectedEntityId;
    private final int collectorEntityId;
    private final int itemCount;

    public ClientboundTakeItemEntityPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.collectedEntityId = helper.readVarInt(in);
        this.collectorEntityId = helper.readVarInt(in);
        this.itemCount = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.collectedEntityId);
        helper.writeVarInt(out, this.collectorEntityId);
        helper.writeVarInt(out, this.itemCount);
    }
}
