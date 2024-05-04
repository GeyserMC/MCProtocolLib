package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.inventory.UpdateStructureBlockAction;
import org.geysermc.mcprotocollib.protocol.data.game.inventory.UpdateStructureBlockMode;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.StructureMirror;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.StructureRotation;

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

    public ServerboundSetStructureBlockPacket(MinecraftByteBuf buf) {
        this.position = buf.readPosition();
        this.action = UpdateStructureBlockAction.from(buf.readVarInt());
        this.mode = UpdateStructureBlockMode.from(buf.readVarInt());
        this.name = buf.readString();
        this.offset = Vector3i.from(buf.readByte(), buf.readByte(), buf.readByte());
        this.size = Vector3i.from(buf.readUnsignedByte(), buf.readUnsignedByte(), buf.readUnsignedByte());
        this.mirror = StructureMirror.from(buf.readVarInt());
        this.rotation = StructureRotation.from(buf.readVarInt());
        this.metadata = buf.readString();
        this.integrity = buf.readFloat();
        this.seed = buf.readVarLong();

        int flags = buf.readUnsignedByte();
        this.ignoreEntities = (flags & FLAG_IGNORE_ENTITIES) != 0;
        this.showAir = (flags & FLAG_SHOW_AIR) != 0;
        this.showBoundingBox = (flags & FLAG_SHOW_BOUNDING_BOX) != 0;
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writePosition(this.position);
        buf.writeVarInt(this.action.ordinal());
        buf.writeVarInt(this.mode.ordinal());
        buf.writeString(this.name);
        buf.writeByte(this.offset.getX());
        buf.writeByte(this.offset.getY());
        buf.writeByte(this.offset.getZ());
        buf.writeByte(this.size.getX());
        buf.writeByte(this.size.getY());
        buf.writeByte(this.size.getZ());
        buf.writeVarInt(this.mirror.ordinal());
        buf.writeVarInt(this.rotation.ordinal());
        buf.writeString(this.metadata);
        buf.writeFloat(this.integrity);
        buf.writeVarLong(this.seed);

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

        buf.writeByte(flags);
    }
}
