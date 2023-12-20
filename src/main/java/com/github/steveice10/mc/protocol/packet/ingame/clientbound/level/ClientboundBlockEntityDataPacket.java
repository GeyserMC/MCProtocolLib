package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.level.block.BlockEntityType;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;
import org.checkerframework.checker.nullness.qual.Nullable;

@Data
@With
@AllArgsConstructor
public class ClientboundBlockEntityDataPacket implements MinecraftPacket {
    private final @NonNull Vector3i position;
    private final @NonNull BlockEntityType type;
    private final @Nullable CompoundTag nbt;

    public ClientboundBlockEntityDataPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.position = helper.readPosition(in);
        this.type = helper.readBlockEntityType(in);
        this.nbt = helper.readAnyTag(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writePosition(out, this.position);
        helper.writeBlockEntityType(out, this.type);
        helper.writeAnyTag(out, this.nbt);
    }
}
