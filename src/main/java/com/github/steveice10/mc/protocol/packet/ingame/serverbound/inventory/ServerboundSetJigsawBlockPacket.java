package com.github.steveice10.mc.protocol.packet.ingame.serverbound.inventory;

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
public class ServerboundSetJigsawBlockPacket implements MinecraftPacket {
    private final @NotNull Vector3i position;
    private final @NotNull String name;
    private final @NotNull String target;
    private final @NotNull String pool;
    private final @NotNull String finalState;
    private final @NotNull String jointType;

    public ServerboundSetJigsawBlockPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.position = helper.readPosition(in);
        this.name = helper.readString(in);
        this.target = helper.readString(in);
        this.pool = helper.readString(in);
        this.finalState = helper.readString(in);
        this.jointType = helper.readString(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writePosition(out, this.position);
        helper.writeString(out, this.name);
        helper.writeString(out, this.target);
        helper.writeString(out, this.pool);
        helper.writeString(out, this.finalState);
        helper.writeString(out, this.jointType);
    }
}
