package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.nbt.NbtMap;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.BlockEntityType;

@Data
@With
@AllArgsConstructor
public class ClientboundBlockEntityDataPacket implements MinecraftPacket {
    private final @NonNull Vector3i position;
    private final @NonNull BlockEntityType type;
    private final @Nullable NbtMap nbt;

    public ClientboundBlockEntityDataPacket(ByteBuf in) {
        this.position = MinecraftTypes.readPosition(in);
        this.type = MinecraftTypes.readBlockEntityType(in);
        this.nbt = MinecraftTypes.readCompoundTag(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writePosition(out, this.position);
        MinecraftTypes.writeBlockEntityType(out, this.type);
        MinecraftTypes.writeAnyTag(out, this.nbt);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
