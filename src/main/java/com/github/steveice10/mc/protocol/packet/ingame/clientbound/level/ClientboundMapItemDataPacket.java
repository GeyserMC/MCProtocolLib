package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.level.map.MapData;
import com.github.steveice10.mc.protocol.data.game.level.map.MapIcon;
import com.github.steveice10.mc.protocol.data.game.level.map.MapIconType;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

@Data
@With
@AllArgsConstructor
public class ClientboundMapItemDataPacket implements MinecraftPacket {
    private final int mapId;
    private final byte scale;
    private final boolean locked;
    private final @NotNull MapIcon[] icons;

    private final MapData data;

    public ClientboundMapItemDataPacket(int mapId, byte scale, boolean locked, @NotNull MapIcon[] icons) {
        this(mapId, scale, locked, icons, null);
    }

    public ClientboundMapItemDataPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.mapId = helper.readVarInt(in);
        this.scale = in.readByte();
        this.locked = in.readBoolean();
        boolean hasIcons = in.readBoolean();
        this.icons = new MapIcon[hasIcons ? helper.readVarInt(in) : 0];
        if (hasIcons) {
            for (int index = 0; index < this.icons.length; index++) {
                int type = helper.readVarInt(in);
                int x = in.readUnsignedByte();
                int z = in.readUnsignedByte();
                int rotation = in.readUnsignedByte();
                Component displayName = null;
                if (in.readBoolean()) {
                    displayName = helper.readComponent(in);
                }

                this.icons[index] = new MapIcon(x, z, MapIconType.from(type), rotation, displayName);
            }
        }

        int columns = in.readUnsignedByte();
        if (columns > 0) {
            int rows = in.readUnsignedByte();
            int x = in.readUnsignedByte();
            int y = in.readUnsignedByte();
            byte[] data = helper.readByteArray(in);

            this.data = new MapData(columns, rows, x, y, data);
        } else {
            this.data = null;
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.mapId);
        out.writeByte(this.scale);
        out.writeBoolean(this.locked);
        if (this.icons.length != 0) {
            out.writeBoolean(true);
            helper.writeVarInt(out, this.icons.length);
            for (MapIcon icon : this.icons) {
                int type = icon.iconType().ordinal();
                helper.writeVarInt(out, type);
                out.writeByte(icon.centerX());
                out.writeByte(icon.centerZ());
                out.writeByte(icon.iconRotation());
                if (icon.displayName() != null) {
                    out.writeBoolean(true);
                    helper.writeComponent(out, icon.displayName());
                } else {
                    out.writeBoolean(false);
                }
            }
        } else {
            out.writeBoolean(false);
        }

        if (this.data != null && this.data.columns() != 0) {
            out.writeByte(this.data.columns());
            out.writeByte(this.data.rows());
            out.writeByte(this.data.x());
            out.writeByte(this.data.y());
            helper.writeVarInt(out, this.data.data().length);
            out.writeBytes(this.data.data());
        } else {
            out.writeByte(0);
        }
    }
}
