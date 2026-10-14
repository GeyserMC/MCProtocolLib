package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.nbt.NbtMap;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.BlockEntityType;
import org.jspecify.annotations.Nullable;

@Data
@With
@AllArgsConstructor
public class ClientboundBlockEntityDataPacket implements MinecraftPacket {
    private final Vector3i position;
    private final BlockEntityType type;
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
