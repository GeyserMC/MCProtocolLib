package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.level.block.value.BlockValue;
import com.github.steveice10.mc.protocol.data.game.level.block.value.BlockValueType;
import com.github.steveice10.mc.protocol.data.game.level.block.value.ChestValue;
import com.github.steveice10.mc.protocol.data.game.level.block.value.ChestValueType;
import com.github.steveice10.mc.protocol.data.game.level.block.value.EndGatewayValue;
import com.github.steveice10.mc.protocol.data.game.level.block.value.EndGatewayValueType;
import com.github.steveice10.mc.protocol.data.game.level.block.value.GenericBlockValue;
import com.github.steveice10.mc.protocol.data.game.level.block.value.GenericBlockValueType;
import com.github.steveice10.mc.protocol.data.game.level.block.value.MobSpawnerValue;
import com.github.steveice10.mc.protocol.data.game.level.block.value.MobSpawnerValueType;
import com.github.steveice10.mc.protocol.data.game.level.block.value.NoteBlockValue;
import com.github.steveice10.mc.protocol.data.game.level.block.value.NoteBlockValueType;
import com.github.steveice10.mc.protocol.data.game.level.block.value.PistonValue;
import com.github.steveice10.mc.protocol.data.game.level.block.value.PistonValueType;
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
public class ClientboundBlockEventPacket implements Packet {
    private static final int NOTE_BLOCK = 80;
    private static final int STICKY_PISTON = 99;
    private static final int PISTON = 106;
    private static final int MOB_SPAWNER = 151; // Value does not show in 1.16
    private static final int CHEST = 153;
    private static final int ENDER_CHEST = 283;
    private static final int TRAPPED_CHEST = 342;
    private static final int END_GATEWAY = 513;
    private static final int SHULKER_BOX_LOWER = 523;
    private static final int SHULKER_BOX_HIGHER = 539;

    private final @NonNull Position position;
    private final @NonNull BlockValueType type;
    private final @NonNull BlockValue value;
    private final int blockId;

    public ClientboundBlockEventPacket(NetInput in) throws IOException {
        this.position = Position.read(in);
        int type = in.readUnsignedByte();
        int value = in.readUnsignedByte();
        this.blockId = in.readVarInt() & 0xFFF;

        if (this.blockId == NOTE_BLOCK) {
            this.type = MagicValues.key(NoteBlockValueType.class, type);
            this.value = new NoteBlockValue(value);
        } else if (this.blockId == STICKY_PISTON || this.blockId == PISTON) {
            this.type = MagicValues.key(PistonValueType.class, type);
            this.value = MagicValues.key(PistonValue.class, value);
        } else if (this.blockId == MOB_SPAWNER) {
            this.type = MagicValues.key(MobSpawnerValueType.class, type);
            this.value = new MobSpawnerValue();
        } else if (this.blockId == CHEST || this.blockId == ENDER_CHEST || this.blockId == TRAPPED_CHEST
                || (this.blockId >= SHULKER_BOX_LOWER && this.blockId <= SHULKER_BOX_HIGHER)) {
            this.type = MagicValues.key(ChestValueType.class, type);
            this.value = new ChestValue(value);
        } else if (this.blockId == END_GATEWAY) {
            this.type = MagicValues.key(EndGatewayValueType.class, type);
            this.value = new EndGatewayValue();
        } else {
            this.type = MagicValues.key(GenericBlockValueType.class, type);
            this.value = new GenericBlockValue(value);
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        int val = 0;
        if (this.type instanceof NoteBlockValueType) {
            val = ((NoteBlockValue) this.value).getPitch();
        } else if (this.type instanceof PistonValueType) {
            val = MagicValues.value(Integer.class, this.value);
        } else if (this.type instanceof ChestValueType) {
            val = ((ChestValue) this.value).getViewers();
        } else if (this.type instanceof GenericBlockValueType) {
            val = ((GenericBlockValue) this.value).getValue();
        }

        Position.write(out, this.position);
        out.writeByte(MagicValues.value(Integer.class, this.type));
        out.writeByte(val);
        out.writeVarInt(this.blockId & 4095);
    }
}
