package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.data.game.NBT;
import com.github.steveice10.mc.protocol.data.game.level.LightUpdateData;
import com.github.steveice10.mc.protocol.data.game.level.block.BlockEntityInfo;
import com.github.steveice10.mc.protocol.data.game.level.block.BlockEntityType;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundLevelChunkWithLightPacket implements Packet {
    private final int x;
    private final int z;
    private final @NonNull byte[] chunkData;
    private final @NonNull CompoundTag heightMaps;
    private final @NonNull BlockEntityInfo[] blockEntities;
    private final @NonNull LightUpdateData lightData;

    public ClientboundLevelChunkWithLightPacket(NetInput in) throws IOException {
        this.x = in.readInt();
        this.z = in.readInt();
        this.heightMaps = NBT.read(in);
        this.chunkData = in.readBytes(in.readVarInt());

        this.blockEntities = new BlockEntityInfo[in.readVarInt()];
        for (int i = 0; i < this.blockEntities.length; i++) {
            byte xz = in.readByte();
            int blockEntityX = (xz >> 4) & 15;
            int blockEntityZ = xz & 15;
            int blockEntityY = in.readShort();
            BlockEntityType type = BlockEntityType.read(in);
            CompoundTag tag = NBT.read(in);
            this.blockEntities[i] = new BlockEntityInfo(blockEntityX, blockEntityY, blockEntityZ, type, tag);
        }

        this.lightData = LightUpdateData.read(in);
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeInt(this.x);
        out.writeInt(this.z);
        NBT.write(out, this.heightMaps);
        out.writeVarInt(this.chunkData.length);
        out.writeBytes(this.chunkData);

        out.writeVarInt(this.blockEntities.length);
        for (BlockEntityInfo blockEntity : this.blockEntities) {
            out.writeByte(((blockEntity.getX() & 15) << 4) | blockEntity.getZ() & 15);
            out.writeShort(blockEntity.getY());
            BlockEntityType.write(out, blockEntity.getType());
            NBT.write(out, blockEntity.getNbt());
        }

        LightUpdateData.write(out, this.lightData);
    }
}