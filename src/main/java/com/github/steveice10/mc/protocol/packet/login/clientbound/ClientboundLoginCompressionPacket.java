package com.github.steveice10.mc.protocol.packet.login.clientbound;

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
public class ClientboundLoginCompressionPacket implements MinecraftPacket {
    private final int threshold;

    public ClientboundLoginCompressionPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.threshold = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.threshold);
    }

    @Override
    public boolean isPriority() {
        return true;
    }
}
