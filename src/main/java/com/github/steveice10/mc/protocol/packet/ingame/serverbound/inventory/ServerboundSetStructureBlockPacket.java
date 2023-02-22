package com.github.steveice10.mc.protocol.packet.ingame.serverbound.inventory;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.inventory.UpdateStructureBlockAction;
import com.github.steveice10.mc.protocol.data.game.inventory.UpdateStructureBlockMode;
import com.github.steveice10.mc.protocol.data.game.level.block.StructureMirror;
import com.github.steveice10.mc.protocol.data.game.level.block.StructureRotation;
import com.nukkitx.math.vector.Vector3i;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ServerboundSetStructureBlockPacket implements MinecraftPacket {
    private static final int FLAG_IGNORE_ENTITIES = 0x01;
    private static final int FLAG_SHOW_AIR = 0x02;
    private static final int FLAG_SHOW_BOUNDING_BOX = 0x04;

    private final @NonNull Vector3i position;
    private final @NonNull UpdateStructureBlockAction action;
    private final @NonNull UpdateStructureBlockMode mode;
    private final @NonNull String name;
    private final @NonNull Vector3i offset;
    private final @NonNull Vector3i size;
    private final @NonNull StructureMirror mirror;
    private final @NonNull StructureRotation rotation;
    private final @NonNull String metadata;
    private final float integrity;
    private final long seed;
    private final boolean ignoreEntities;
    private final boolean showAir;
    private final boolean showBoundingBox;

    public ServerboundSetStructureBlockPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.position = helper.readPosition(in);
        this.action = UpdateStructureBlockAction.from(helper.readVarInt(in));
        this.mode = UpdateStructureBlockMode.from(helper.readVarInt(in));
        this.name = helper.readString(in);
        this.offset = Vector3i.from(in.readByte(), in.readByte(), in.readByte());
        this.size = Vector3i.from(in.readUnsignedByte(), in.readUnsignedByte(), in.readUnsignedByte());
        this.mirror = StructureMirror.from(helper.readVarInt(in));
        this.rotation = StructureRotation.from(helper.readVarInt(in));
        this.metadata = helper.readString(in);
        this.integrity = in.readFloat();
        this.seed = helper.readVarLong(in);

        int flags = in.readUnsignedByte();
        this.ignoreEntities = (flags & FLAG_IGNORE_ENTITIES) != 0;
        this.showAir = (flags & FLAG_SHOW_AIR) != 0;
        this.showBoundingBox = (flags & FLAG_SHOW_BOUNDING_BOX) != 0;
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writePosition(out, this.position);
        helper.writeVarInt(out, this.action.ordinal());
        helper.writeVarInt(out, this.mode.ordinal());
        helper.writeString(out, this.name);
        out.writeByte(this.offset.getX());
        out.writeByte(this.offset.getY());
        out.writeByte(this.offset.getZ());
        out.writeByte(this.size.getX());
        out.writeByte(this.size.getY());
        out.writeByte(this.size.getZ());
        helper.writeVarInt(out, this.mirror.ordinal());
        helper.writeVarInt(out, this.rotation.ordinal());
        helper.writeString(out, this.metadata);
        out.writeFloat(this.integrity);
        helper.writeVarLong(out, this.seed);

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
