package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.nbt.NbtMap;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.level.LightUpdateData;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.BlockEntityInfo;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.BlockEntityType;

@Data
@With
@AllArgsConstructor
public class ClientboundLevelChunkWithLightPacket implements MinecraftPacket {
    private final int x;
    private final int z;
    private final byte @NonNull [] chunkData;
    private final @NonNull NbtMap heightMaps;
    private final @NonNull BlockEntityInfo @NonNull [] blockEntities;
    private final @NonNull LightUpdateData lightData;

    public ClientboundLevelChunkWithLightPacket(MinecraftByteBuf buf) {
        this.x = buf.readInt();
        this.z = buf.readInt();
        this.heightMaps = buf.readCompoundTagOrThrow();
        this.chunkData = buf.readByteArray();

        this.blockEntities = new BlockEntityInfo[buf.readVarInt()];
        for (int i = 0; i < this.blockEntities.length; i++) {
            byte xz = buf.readByte();
            int blockEntityX = (xz >> 4) & 15;
            int blockEntityZ = xz & 15;
            int blockEntityY = buf.readShort();
            BlockEntityType type = buf.readBlockEntityType();
            NbtMap tag = buf.readCompoundTag();
            this.blockEntities[i] = new BlockEntityInfo(blockEntityX, blockEntityY, blockEntityZ, type, tag);
        }

        this.lightData = buf.readLightUpdateData();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.z);
        buf.writeAnyTag(this.heightMaps);
        buf.writeVarInt(this.chunkData.length);
        buf.writeBytes(this.chunkData);

        buf.writeVarInt(this.blockEntities.length);
        for (BlockEntityInfo blockEntity : this.blockEntities) {
            buf.writeByte(((blockEntity.getX() & 15) << 4) | blockEntity.getZ() & 15);
            buf.writeShort(blockEntity.getY());
            buf.writeBlockEntityType(blockEntity.getType());
            buf.writeAnyTag(blockEntity.getNbt());
        }

        buf.writeLightUpdateData(this.lightData);
    }
}
