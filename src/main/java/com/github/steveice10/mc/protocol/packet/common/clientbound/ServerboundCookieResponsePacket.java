package com.github.steveice10.mc.protocol.packet.common.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ServerboundCookieResponsePacket implements MinecraftPacket {
    private final String key;
    private final byte @Nullable[] payload;

    public ServerboundCookieResponsePacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.key = helper.readResourceLocation(in);
        this.payload = helper.readNullable(in, helper::readByteArray);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeResourceLocation(out, this.key);
        helper.writeNullable(out, this.payload, helper::writeByteArray);
    }
}
