package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.NBT;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
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
public class ClientboundBlockEntityDataPacket implements Packet {
    private final @NonNull Position position;
    private final @NonNull BlockEntityType type;
    private final @NonNull CompoundTag nbt;

    public ClientboundBlockEntityDataPacket(NetInput in) throws IOException {
        this.position = Position.read(in);
        this.type = MagicValues.key(BlockEntityType.class, in.readUnsignedByte());
        this.nbt = NBT.read(in);
    }

    @Override
    public void write(NetOutput out) throws IOException {
        Position.write(out, this.position);
        out.writeByte(MagicValues.value(Integer.class, this.type));
        NBT.write(out, this.nbt);
    }
}
