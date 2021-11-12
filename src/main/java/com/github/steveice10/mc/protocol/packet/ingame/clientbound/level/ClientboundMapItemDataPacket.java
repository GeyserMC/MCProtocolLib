package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.level.map.MapData;
import com.github.steveice10.mc.protocol.data.game.level.map.MapIcon;
import com.github.steveice10.mc.protocol.data.game.level.map.MapIconType;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundMapItemDataPacket implements Packet {
    private final int mapId;
    private final byte scale;
    private final boolean locked;
    private final @NonNull MapIcon[] icons;

    private final MapData data;

    public ClientboundMapItemDataPacket(int mapId, byte scale, boolean locked, @NonNull MapIcon[] icons) {
        this(mapId, scale, locked, icons, null);
    }

    public ClientboundMapItemDataPacket(NetInput in) throws IOException {
        this.mapId = in.readVarInt();
        this.scale = in.readByte();
        this.locked = in.readBoolean();
        boolean hasIcons = in.readBoolean();
        this.icons = new MapIcon[hasIcons ? in.readVarInt() : 0];
        if (hasIcons) {
            for (int index = 0; index < this.icons.length; index++) {
                int type = in.readVarInt();
                int x = in.readUnsignedByte();
                int z = in.readUnsignedByte();
                int rotation = in.readUnsignedByte();
                Component displayName = null;
                if (in.readBoolean()) {
                    displayName = DefaultComponentSerializer.get().deserialize(in.readString());
                }

                this.icons[index] = new MapIcon(x, z, MagicValues.key(MapIconType.class, type), rotation, displayName);
            }
        }

        int columns = in.readUnsignedByte();
        if (columns > 0) {
            int rows = in.readUnsignedByte();
            int x = in.readUnsignedByte();
            int y = in.readUnsignedByte();
            byte[] data = in.readBytes(in.readVarInt());

            this.data = new MapData(columns, rows, x, y, data);
        } else {
            this.data = null;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.mapId);
        out.writeByte(this.scale);
        out.writeBoolean(this.locked);
        if (this.icons.length != 0) {
            out.writeBoolean(true);
            out.writeVarInt(this.icons.length);
            for (MapIcon icon : this.icons) {
                int type = MagicValues.value(Integer.class, icon.getIconType());
                out.writeVarInt(type);
                out.writeByte(icon.getCenterX());
                out.writeByte(icon.getCenterZ());
                out.writeByte(icon.getIconRotation());
                if (icon.getDisplayName() != null) {
                    out.writeBoolean(true);
                    out.writeString(DefaultComponentSerializer.get().serialize(icon.getDisplayName()));
                } else {
                    out.writeBoolean(false);
                }
            }
        } else {
            out.writeBoolean(false);
        }

        if (this.data != null && this.data.getColumns() != 0) {
            out.writeByte(this.data.getColumns());
            out.writeByte(this.data.getRows());
            out.writeByte(this.data.getX());
            out.writeByte(this.data.getY());
            out.writeVarInt(this.data.getData().length);
            out.writeBytes(this.data.getData());
        } else {
            out.writeByte(0);
        }
    }
}
