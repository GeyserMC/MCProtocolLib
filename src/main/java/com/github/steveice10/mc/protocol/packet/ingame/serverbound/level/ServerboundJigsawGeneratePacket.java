package com.github.steveice10.mc.protocol.packet.ingame.serverbound.level;

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
public class ServerboundJigsawGeneratePacket implements MinecraftPacket {
    private final @NotNull Vector3i position;
    private final int levels;
    private final boolean keepJigsaws;

    public ServerboundJigsawGeneratePacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.position = helper.readPosition(in);
        this.levels = helper.readVarInt(in);
        this.keepJigsaws = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writePosition(out, this.position);
        helper.writeVarInt(out, this.levels);
        out.writeBoolean(this.keepJigsaws);
    }
}
