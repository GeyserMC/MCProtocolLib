package com.github.steveice10.mc.protocol.packet.ingame.serverbound.level;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ServerboundTeleportToEntityPacket implements MinecraftPacket {
    private final @NonNull UUID target;

    public ServerboundTeleportToEntityPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.target = helper.readUUID(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeUUID(out, this.target);
    }
}
