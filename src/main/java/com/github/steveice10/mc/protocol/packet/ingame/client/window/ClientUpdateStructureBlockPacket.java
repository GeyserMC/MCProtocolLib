package com.github.steveice10.mc.protocol.packet.ingame.client.window;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.window.UpdateStructureBlockAction;
import com.github.steveice10.mc.protocol.data.game.window.UpdateStructureBlockMode;
import com.github.steveice10.mc.protocol.data.game.world.block.StructureMirror;
import com.github.steveice10.mc.protocol.data.game.world.block.StructureRotation;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.With;

import java.io.IOException;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ClientUpdateStructureBlockPacket implements Packet {
    private static final int FLAG_IGNORE_ENTITIES = 0x01;
    private static final int FLAG_SHOW_AIR = 0x02;
    private static final int FLAG_SHOW_BOUNDING_BOX = 0x04;

    private @NonNull Position position;
    private @NonNull UpdateStructureBlockAction action;
    private @NonNull UpdateStructureBlockMode mode;
    private @NonNull String name;
    private @NonNull Position offset;
    private @NonNull Position size;
    private @NonNull StructureMirror mirror;
    private @NonNull StructureRotation rotation;
    private @NonNull String metadata;
    private float integrity;
    private long seed;
    private boolean ignoreEntities;
    private boolean showAir;
    private boolean showBoundingBox;

    @Override
    public void read(NetInput in) throws IOException {
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

    @Override
    public boolean isPriority() {
        return false;
    }
}
