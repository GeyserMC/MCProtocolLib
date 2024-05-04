package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.level.map.MapData;
import org.geysermc.mcprotocollib.protocol.data.game.level.map.MapIcon;
import org.geysermc.mcprotocollib.protocol.data.game.level.map.MapIconType;

@Data
@With
@AllArgsConstructor
public class ClientboundMapItemDataPacket implements MinecraftPacket {
    private final int mapId;
    private final byte scale;
    private final boolean locked;
    private final @NonNull MapIcon[] icons;

    private final MapData data;

    public ClientboundMapItemDataPacket(int mapId, byte scale, boolean locked, @NonNull MapIcon[] icons) {
        this(mapId, scale, locked, icons, null);
    }

    public ClientboundMapItemDataPacket(MinecraftByteBuf buf) {
        this.mapId = buf.readVarInt();
        this.scale = buf.readByte();
        this.locked = buf.readBoolean();
        boolean hasIcons = buf.readBoolean();
        this.icons = new MapIcon[hasIcons ? buf.readVarInt() : 0];
        if (hasIcons) {
            for (int index = 0; index < this.icons.length; index++) {
                int type = buf.readVarInt();
                int x = buf.readByte();
                int z = buf.readByte();
                int rotation = buf.readByte();
                Component displayName = null;
                if (buf.readBoolean()) {
                    displayName = buf.readComponent();
                }

                this.icons[index] = new MapIcon(x, z, MapIconType.from(type), rotation, displayName);
            }
        }

        int columns = buf.readUnsignedByte();
        if (columns > 0) {
            int rows = buf.readUnsignedByte();
            int x = buf.readUnsignedByte();
            int y = buf.readUnsignedByte();
            byte[] data = buf.readByteArray();

            this.data = new MapData(columns, rows, x, y, data);
        } else {
            this.data = null;
        }
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.mapId);
        buf.writeByte(this.scale);
        buf.writeBoolean(this.locked);
        if (this.icons.length != 0) {
            buf.writeBoolean(true);
            buf.writeVarInt(this.icons.length);
            for (MapIcon icon : this.icons) {
                int type = icon.getIconType().ordinal();
                buf.writeVarInt(type);
                buf.writeByte(icon.getCenterX());
                buf.writeByte(icon.getCenterZ());
                buf.writeByte(icon.getIconRotation());
                if (icon.getDisplayName() != null) {
                    buf.writeBoolean(true);
                    buf.writeComponent(icon.getDisplayName());
                } else {
                    buf.writeBoolean(false);
                }
            }
        } else {
            buf.writeBoolean(false);
        }

        if (this.data != null && this.data.getColumns() != 0) {
            buf.writeByte(this.data.getColumns());
            buf.writeByte(this.data.getRows());
            buf.writeByte(this.data.getX());
            buf.writeByte(this.data.getY());
            buf.writeVarInt(this.data.getData().length);
            buf.writeBytes(this.data.getData());
        } else {
            buf.writeByte(0);
        }
    }
}
