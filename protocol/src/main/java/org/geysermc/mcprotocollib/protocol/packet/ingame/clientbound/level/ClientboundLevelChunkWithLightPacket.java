package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.nbt.NbtMap;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
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

    public ClientboundLevelChunkWithLightPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.x = in.readInt();
        this.z = in.readInt();
        this.heightMaps = helper.readCompoundTagOrThrow(in);
        this.chunkData = helper.readByteArray(in);

        this.blockEntities = new BlockEntityInfo[helper.readVarInt(in)];
        for (int i = 0; i < this.blockEntities.length; i++) {
            byte xz = in.readByte();
            int blockEntityX = (xz >> 4) & 15;
            int blockEntityZ = xz & 15;
            int blockEntityY = in.readShort();
            BlockEntityType type = helper.readBlockEntityType(in);
            NbtMap tag = helper.readCompoundTag(in);
            this.blockEntities[i] = new BlockEntityInfo(blockEntityX, blockEntityY, blockEntityZ, type, tag);
        }

        this.lightData = helper.readLightUpdateData(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeInt(this.x);
        out.writeInt(this.z);
        helper.writeAnyTag(out, this.heightMaps);
        helper.writeVarInt(out, this.chunkData.length);
        out.writeBytes(this.chunkData);

        helper.writeVarInt(out, this.blockEntities.length);
        for (BlockEntityInfo blockEntity : this.blockEntities) {
            out.writeByte(((blockEntity.getX() & 15) << 4) | blockEntity.getZ() & 15);
            out.writeShort(blockEntity.getY());
            helper.writeBlockEntityType(out, blockEntity.getType());
            helper.writeAnyTag(out, blockEntity.getNbt());
        }

        helper.writeLightUpdateData(out, this.lightData);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
