package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.nbt.NbtMap;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.BlockEntityType;

@Data
@With
@AllArgsConstructor
public class ClientboundBlockEntityDataPacket implements MinecraftPacket {
    private final @NonNull Vector3i position;
    private final @NonNull BlockEntityType type;
    private final @Nullable NbtMap nbt;

    public ClientboundBlockEntityDataPacket(MinecraftByteBuf buf) {
        this.position = buf.readPosition();
        this.type = buf.readBlockEntityType();
        this.nbt = buf.readCompoundTag();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writePosition(this.position);
        buf.writeBlockEntityType(this.type);
        buf.writeAnyTag(this.nbt);
    }
}
