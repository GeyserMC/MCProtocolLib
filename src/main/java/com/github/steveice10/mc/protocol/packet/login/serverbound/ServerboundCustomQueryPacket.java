package com.github.steveice10.mc.protocol.packet.login.serverbound;

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
public class ServerboundCustomQueryPacket implements MinecraftPacket {
    private final int messageId;
    private final byte[] data;

    public ServerboundCustomQueryPacket(int messageId) {
        this(messageId, null);
    }

    public ServerboundCustomQueryPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.messageId = helper.readVarInt(in);
        if (in.readBoolean()) {
            this.data = helper.readByteArray(in, ByteBuf::readableBytes);
        } else {
            this.data = null;
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.messageId);
        if (this.data != null) {
            out.writeBoolean(true);
            out.writeBytes(this.data);
        } else {
            out.writeBoolean(false);
        }
    }
}
