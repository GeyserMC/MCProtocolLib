package com.github.steveice10.mc.protocol.packet.common.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ClientboundResourcePackPopPacket implements MinecraftPacket {

    private final @Nullable UUID id;

    public ClientboundResourcePackPopPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.id = helper.readNullable(in, helper::readUUID);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeNullable(out, this.id, helper::writeUUID);
    }
}
