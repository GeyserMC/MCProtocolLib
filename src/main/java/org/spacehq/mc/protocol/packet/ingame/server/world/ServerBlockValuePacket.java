package org.spacehq.mc.protocol.packet.ingame.server.world;

import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.world.block.value.BlockValue;
import org.spacehq.mc.protocol.data.game.values.world.block.value.BlockValueType;
import org.spacehq.mc.protocol.data.game.values.world.block.value.ChestValue;
import org.spacehq.mc.protocol.data.game.values.world.block.value.ChestValueType;
import org.spacehq.mc.protocol.data.game.values.world.block.value.GenericBlockValue;
import org.spacehq.mc.protocol.data.game.values.world.block.value.GenericBlockValueType;
import org.spacehq.mc.protocol.data.game.values.world.block.value.MobSpawnerValue;
import org.spacehq.mc.protocol.data.game.values.world.block.value.MobSpawnerValueType;
import org.spacehq.mc.protocol.data.game.values.world.block.value.NoteBlockValue;
import org.spacehq.mc.protocol.data.game.values.world.block.value.NoteBlockValueType;
import org.spacehq.mc.protocol.data.game.values.world.block.value.PistonValue;
import org.spacehq.mc.protocol.data.game.values.world.block.value.PistonValueType;
import org.spacehq.mc.protocol.util.NetUtil;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerBlockValuePacket implements Packet {

    private static final int NOTE_BLOCK = 25;
    private static final int STICKY_PISTON = 29;
    private static final int PISTON = 33;
    private static final int MOB_SPAWNER = 52;
    private static final int CHEST = 54;
    private static final int ENDER_CHEST = 130;
    private static final int TRAPPED_CHEST = 146;

    private Position position;
    private BlockValueType type;
    private BlockValue value;
    private int blockId;

    @SuppressWarnings("unused")
    private ServerBlockValuePacket() {
    }

    public ServerBlockValuePacket(Position position, BlockValueType type, BlockValue value, int blockId) {
        this.position = position;
        this.type = type;
        this.value = value;
        this.blockId = blockId;
    }

    public Position getPosition() {
        return this.position;
    }

    public BlockValueType getType() {
        return this.type;
    }

    public BlockValue getValue() {
        return this.value;
    }

    public int getBlockId() {
        return this.blockId;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.position = NetUtil.readPosition(in);
        int type = in.readUnsignedByte();
        if(this.blockId == NOTE_BLOCK) {
            this.type = MagicValues.key(NoteBlockValueType.class, type);
        } else if(this.blockId == STICKY_PISTON || this.blockId == PISTON) {
            this.type = MagicValues.key(PistonValueType.class, type);
        } else if(this.blockId == MOB_SPAWNER) {
            this.type = MagicValues.key(MobSpawnerValueType.class, type);
        } else if(this.blockId == CHEST || this.blockId == ENDER_CHEST || this.blockId == TRAPPED_CHEST) {
            this.type = MagicValues.key(ChestValueType.class, type);
        } else {
            this.type = MagicValues.key(GenericBlockValueType.class, type);
        }

        int value = in.readUnsignedByte();
        if(this.blockId == NOTE_BLOCK) {
            this.value = new NoteBlockValue(value);
        } else if(this.blockId == STICKY_PISTON || this.blockId == PISTON) {
            this.value = MagicValues.key(PistonValue.class, value);
        } else if(this.blockId == MOB_SPAWNER) {
            this.value = new MobSpawnerValue();
        } else if(this.blockId == CHEST || this.blockId == ENDER_CHEST || this.blockId == TRAPPED_CHEST) {
            this.value = new ChestValue(value);
        } else {
            this.value = new GenericBlockValue(value);
        }

        this.blockId = in.readVarInt() & 4095;
    }

    @Override
    public void write(NetOutput out) throws IOException {
        NetUtil.writePosition(out, this.position);
        int type = 0;
        if(this.type instanceof NoteBlockValueType) {
            type = MagicValues.value(Integer.class, (NoteBlockValueType) this.type);
        } else if(this.type instanceof PistonValueType) {
            type = MagicValues.value(Integer.class, (PistonValueType) this.type);
        } else if(this.type instanceof MobSpawnerValueType) {
            type = MagicValues.value(Integer.class, (MobSpawnerValueType) this.type);
        } else if(this.type instanceof ChestValueType) {
            type = MagicValues.value(Integer.class, (ChestValueType) this.type);
        } else if(this.type instanceof GenericBlockValueType) {
            type = MagicValues.value(Integer.class, (GenericBlockValueType) this.type);
        }

        out.writeByte(type);
        int val = 0;
        if(this.value instanceof NoteBlockValue) {
            val = ((NoteBlockValue) this.value).getPitch();
        } else if(this.value instanceof PistonValue) {
            val = MagicValues.value(Integer.class, (PistonValue) this.value);
        } else if(this.value instanceof MobSpawnerValue) {
            val = 0;
        } else if(this.value instanceof ChestValue) {
            val = ((ChestValue) this.value).getViewers();
        } else if(this.value instanceof GenericBlockValue) {
            val = ((GenericBlockValue) this.value).getValue();
        }

        out.writeByte(val);
        out.writeVarInt(this.blockId & 4095);
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
