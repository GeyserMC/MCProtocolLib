package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;
import org.jetbrains.annotations.NotNull;

@Data
@With
@AllArgsConstructor
public class ClientboundSetDefaultSpawnPositionPacket implements MinecraftPacket {
    private final @NotNull Vector3i position;
    private final float angle;

    public ClientboundSetDefaultSpawnPositionPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.position = helper.readPosition(in);
        this.angle = in.readFloat();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writePosition(out, this.position);
        out.writeFloat(this.angle);
    }
}
