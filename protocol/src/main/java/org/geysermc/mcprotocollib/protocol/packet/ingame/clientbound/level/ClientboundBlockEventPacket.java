package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.entity.object.Direction;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.WobbleStyle;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.value.BellValue;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.value.BellValueType;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.value.BlockValue;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.value.BlockValueType;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.value.ChestValue;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.value.ChestValueType;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.value.DecoratedPotValue;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.value.DecoratedPotValueType;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.value.EndGatewayValue;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.value.EndGatewayValueType;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.value.GenericBlockValue;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.value.GenericBlockValueType;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.value.MobSpawnerValue;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.value.MobSpawnerValueType;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.value.NoteBlockValue;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.value.NoteBlockValueType;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.value.PistonValue;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.value.PistonValueType;

@Data
@With
@AllArgsConstructor
public class ClientboundBlockEventPacket implements MinecraftPacket {
    // Do we really want these hardcoded values?
    private static final int NOTE_BLOCK = 109;
    private static final int STICKY_PISTON = 128;
    private static final int PISTON = 135;
    private static final int MOB_SPAWNER = 182;
    private static final int CHEST = 185;
    private static final int ENDER_CHEST = 358;
    private static final int TRAPPED_CHEST = 427;
    private static final int END_GATEWAY = 624;
    private static final int SHULKER_BOX_LOWER = 634;
    private static final int SHULKER_BOX_HIGHER = 650;
    private static final int BELL = 804;
    private static final int DECORATED_POT = 1076;

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
            this.type = NoteBlockValueType.from(type);
            this.value = new NoteBlockValue();
        } else if (this.blockId == STICKY_PISTON || this.blockId == PISTON) {
            this.type = PistonValueType.from(type);
            this.value = new PistonValue(Direction.from(Math.abs((value & 7) % 6)));
        } else if (this.blockId == MOB_SPAWNER) {
            this.type = MobSpawnerValueType.from(type - 1);
            this.value = new MobSpawnerValue();
        } else if (this.blockId == CHEST || this.blockId == ENDER_CHEST || this.blockId == TRAPPED_CHEST
                || (this.blockId >= SHULKER_BOX_LOWER && this.blockId <= SHULKER_BOX_HIGHER)) {
            this.type = ChestValueType.from(type - 1);
            this.value = new ChestValue(value);
        } else if (this.blockId == END_GATEWAY) {
            this.type = EndGatewayValueType.from(type - 1);
            this.value = new EndGatewayValue();
        } else if (this.blockId == BELL) {
            this.type = BellValueType.from(type - 1);
            this.value = new BellValue(Direction.from(Math.abs(value % 6)));
        } else if (this.blockId == DECORATED_POT) {
            this.type = DecoratedPotValueType.from(type - 1);
            this.value = new DecoratedPotValue(WobbleStyle.from(Math.abs(value % 2)));
        } else {
            this.type = GenericBlockValueType.from(type);
            this.value = new GenericBlockValue(value);
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        int val = 0;
        int type = 0;
        // TODO: Handle this in MinecraftCodecHelper
        if (this.type instanceof PistonValueType) {
            val = ((PistonValue) this.value).getDirection().ordinal();
            type = ((PistonValueType) this.type).ordinal();
        } else if (this.type instanceof MobSpawnerValueType) {
            type = ((MobSpawnerValueType) this.type).ordinal() + 1;
        } else if (this.type instanceof ChestValueType) {
            val = ((ChestValue) this.value).getViewers();
            type = ((ChestValueType) this.type).ordinal() + 1;
        } else if (this.type instanceof EndGatewayValueType) {
            type = ((EndGatewayValueType) this.type).ordinal() + 1;
        } else if (this.type instanceof BellValueType) {
            val = ((BellValue) this.value).getDirection().ordinal();
            type = ((BellValueType) this.type).ordinal() + 1;
        } else if (this.type instanceof GenericBlockValueType) {
            val = ((GenericBlockValue) this.value).getValue();
            type = ((GenericBlockValueType) this.type).ordinal();
        }

        helper.writePosition(out, this.position);
        out.writeByte(type);
        out.writeByte(val);
        helper.writeVarInt(out, this.blockId);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
