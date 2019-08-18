package com.github.steveice10.mc.protocol.packet.ingame.server.world;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.world.map.MapData;
import com.github.steveice10.mc.protocol.data.game.world.map.MapIcon;
import com.github.steveice10.mc.protocol.data.game.world.map.MapIconType;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.io.IOException;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerMapDataPacket implements Packet {
    private int mapId;
    private byte scale;
    private boolean trackingPosition;
    private boolean locked;
    private @NonNull MapIcon[] icons;

    private MapData data;

    public ServerMapDataPacket(int mapId, byte scale, boolean trackingPosition, boolean locked, @NonNull MapIcon[] icons) {
        this(mapId, scale, trackingPosition, locked, icons, null);
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.mapId = in.readVarInt();
        this.scale = in.readByte();
        this.trackingPosition = in.readBoolean();
        this.locked = in.readBoolean();
        this.icons = new MapIcon[in.readVarInt()];
        for(int index = 0; index < this.icons.length; index++) {
            int type = in.readVarInt();
            int x = in.readUnsignedByte();
            int z = in.readUnsignedByte();
            int rotation = in.readUnsignedByte();
            Message displayName = null;
            if(in.readBoolean()) {
                displayName = Message.fromString(in.readString());
            }

            this.icons[index] = new MapIcon(x, z, MagicValues.key(MapIconType.class, type), rotation, displayName);
        }

        int columns = in.readUnsignedByte();
        if(columns > 0) {
            int rows = in.readUnsignedByte();
            int x = in.readUnsignedByte();
            int y = in.readUnsignedByte();
            byte[] data = in.readBytes(in.readVarInt());

            this.data = new MapData(columns, rows, x, y, data);
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.mapId);
        out.writeByte(this.scale);
        out.writeBoolean(this.trackingPosition);
        out.writeBoolean(this.locked);
        out.writeVarInt(this.icons.length);
        for(int index = 0; index < this.icons.length; index++) {
            MapIcon icon = this.icons[index];
            int type = MagicValues.value(Integer.class, icon.getIconType());
            out.writeVarInt(type);
            out.writeByte(icon.getCenterX());
            out.writeByte(icon.getCenterZ());
            out.writeByte(icon.getIconRotation());
            if (icon.getDisplayName() != null) {
                out.writeBoolean(false);
                out.writeString(icon.getDisplayName().toJsonString());
            } else {
                out.writeBoolean(true);
            }
        }

        if(this.data != null && this.data.getColumns() != 0) {
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

    @Override
    public boolean isPriority() {
        return false;
    }
}
