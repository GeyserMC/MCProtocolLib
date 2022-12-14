package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.level.block.value.*;
import org.cloudburstmc.math.vector.Vector3i;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundBlockEventPacket implements MinecraftPacket {
    // Do we really want these hardcoded values?
    private static final int NOTE_BLOCK = 93;
    private static final int STICKY_PISTON = 112;
    private static final int PISTON = 119;
    private static final int MOB_SPAWNER = 165; // Value does not show in 1.16
    private static final int CHEST = 167;
    private static final int ENDER_CHEST = 328;
    private static final int TRAPPED_CHEST = 392;
    private static final int END_GATEWAY = 576;
    private static final int SHULKER_BOX_LOWER = 586;
    private static final int SHULKER_BOX_HIGHER = 602;

    private final @NonNull Vector3i position;
    private final @NonNull BlockValueType type;
    private final @NonNull BlockValue value;
    private final int blockId;

    public ClientboundBlockEventPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.position = helper.readPosition(in);
        int type = in.readUnsignedByte();
        int value = in.readUnsignedByte();
        this.blockId = helper.readVarInt(in);

        // TODO: Handle this in MinecraftCodecHelper
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
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        int val = 0;
        // TODO: Handle this in MinecraftCodecHelper
        if (this.type instanceof NoteBlockValueType) {
            val = ((NoteBlockValue) this.value).getPitch();
        } else if (this.type instanceof PistonValueType) {
            val = MagicValues.value(Integer.class, this.value);
        } else if (this.type instanceof ChestValueType) {
            val = ((ChestValue) this.value).getViewers();
        } else if (this.type instanceof GenericBlockValueType) {
            val = ((GenericBlockValue) this.value).getValue();
        }

        helper.writePosition(out, this.position);
        out.writeByte(MagicValues.value(Integer.class, this.type));
        out.writeByte(val);
        helper.writeVarInt(out, this.blockId);
    }
}
