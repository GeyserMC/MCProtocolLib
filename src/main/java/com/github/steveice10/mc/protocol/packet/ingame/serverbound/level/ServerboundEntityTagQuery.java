package com.github.steveice10.mc.protocol.packet.ingame.serverbound.level;

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
public class ServerboundEntityTagQuery implements MinecraftPacket {
    private final int transactionId;
    private final int entityId;

    public ServerboundEntityTagQuery(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.transactionId = helper.readVarInt(in);
        this.entityId = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.transactionId);
        helper.writeVarInt(out, this.entityId);
    }
}
