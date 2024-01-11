package com.github.steveice10.mc.protocol.packet.common.clientbound;

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
public class ClientboundTransferPacket implements MinecraftPacket {
    private final String host;
    private final int port;

    public ClientboundTransferPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.host = helper.readString(in);
        this.port = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeString(out, this.host);
        helper.writeVarInt(out, this.port);
    }
}
