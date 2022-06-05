package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.level.LightUpdateData;
import com.github.steveice10.mc.protocol.data.game.level.block.BlockEntityInfo;
import com.github.steveice10.mc.protocol.data.game.level.block.BlockEntityType;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundLevelChunkWithLightPacket implements MinecraftPacket {
    private final int x;
    private final int z;
    private final @NonNull byte[] chunkData;
    private final @NonNull CompoundTag heightMaps;
    private final @NonNull BlockEntityInfo[] blockEntities;
    private final @NonNull LightUpdateData lightData;

    public ClientboundLevelChunkWithLightPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.x = in.readInt();
        this.z = in.readInt();
        this.heightMaps = helper.readTag(in);
        this.chunkData = helper.readByteArray(in);

        this.blockEntities = new BlockEntityInfo[helper.readVarInt(in)];
        for (int i = 0; i < this.blockEntities.length; i++) {
            byte xz = in.readByte();
            int blockEntityX = (xz >> 4) & 15;
            int blockEntityZ = xz & 15;
            int blockEntityY = in.readShort();
            BlockEntityType type = helper.readBlockEntityType(in);
            CompoundTag tag = helper.readTag(in);
            this.blockEntities[i] = new BlockEntityInfo(blockEntityX, blockEntityY, blockEntityZ, type, tag);
        }

        this.lightData = helper.readLightUpdateData(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        out.writeInt(this.x);
        out.writeInt(this.z);
        helper.writeTag(out, this.heightMaps);
        helper.writeVarInt(out, this.chunkData.length);
        out.writeBytes(this.chunkData);

        helper.writeVarInt(out, this.blockEntities.length);
        for (BlockEntityInfo blockEntity : this.blockEntities) {
            out.writeByte(((blockEntity.getX() & 15) << 4) | blockEntity.getZ() & 15);
            out.writeShort(blockEntity.getY());
            helper.writeBlockEntityType(out, blockEntity.getType());
            helper.writeTag(out, blockEntity.getNbt());
        }

        helper.writeLightUpdateData(out, this.lightData);
    }
}