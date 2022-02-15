package com.github.steveice10.mc.protocol.packet.ingame.serverbound.inventory;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.inventory.UpdateStructureBlockAction;
import com.github.steveice10.mc.protocol.data.game.inventory.UpdateStructureBlockMode;
import com.github.steveice10.mc.protocol.data.game.level.block.StructureMirror;
import com.github.steveice10.mc.protocol.data.game.level.block.StructureRotation;
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
public class ServerboundSetStructureBlockPacket implements Packet {
    private static final int FLAG_IGNORE_ENTITIES = 0x01;
    private static final int FLAG_SHOW_AIR = 0x02;
    private static final int FLAG_SHOW_BOUNDING_BOX = 0x04;

    private final @NonNull Position position;
    private final @NonNull UpdateStructureBlockAction action;
    private final @NonNull UpdateStructureBlockMode mode;
    private final @NonNull String name;
    private final @NonNull Position offset;
    private final @NonNull Position size;
    private final @NonNull StructureMirror mirror;
    private final @NonNull StructureRotation rotation;
    private final @NonNull String metadata;
    private final float integrity;
    private final long seed;
    private final boolean ignoreEntities;
    private final boolean showAir;
    private final boolean showBoundingBox;

    public ServerboundSetStructureBlockPacket(NetInput in) throws IOException {
        this.position = Position.read(in);
        this.action = MagicValues.key(UpdateStructureBlockAction.class, in.readVarInt());
        this.mode = MagicValues.key(UpdateStructureBlockMode.class, in.readVarInt());
        this.name = in.readString();
        this.offset = new Position(in.readByte(), in.readByte(), in.readByte());
        this.size = new Position(in.readUnsignedByte(), in.readUnsignedByte(), in.readUnsignedByte());
        this.mirror = MagicValues.key(StructureMirror.class, in.readVarInt());
        this.rotation = MagicValues.key(StructureRotation.class, in.readVarInt());
        this.metadata = in.readString();
        this.integrity = in.readFloat();
        this.seed = in.readVarLong();

        int flags = in.readUnsignedByte();
        this.ignoreEntities = (flags & FLAG_IGNORE_ENTITIES) != 0;
        this.showAir = (flags & FLAG_SHOW_AIR) != 0;
        this.showBoundingBox = (flags & FLAG_SHOW_BOUNDING_BOX) != 0;
    }

    @Override
    public void write(NetOutput out) throws IOException {
        Position.write(out, this.position);
        out.writeVarInt(MagicValues.value(Integer.class, this.action));
        out.writeVarInt(MagicValues.value(Integer.class, this.mode));
        out.writeString(this.name);
        out.writeByte(this.offset.getX());
        out.writeByte(this.offset.getY());
        out.writeByte(this.offset.getZ());
        out.writeByte(this.size.getX());
        out.writeByte(this.size.getY());
        out.writeByte(this.size.getZ());
        out.writeVarInt(MagicValues.value(Integer.class, this.mirror));
        out.writeVarInt(MagicValues.value(Integer.class, this.rotation));
        out.writeString(this.metadata);
        out.writeFloat(this.integrity);
        out.writeVarLong(this.seed);

        int flags = 0;
        if (this.ignoreEntities) {
            flags |= FLAG_IGNORE_ENTITIES;
        }

        if (this.showAir) {
            flags |= FLAG_SHOW_AIR;
        }

        if (this.showBoundingBox) {
            flags |= FLAG_SHOW_BOUNDING_BOX;
        }

        out.writeByte(flags);
    }
}
