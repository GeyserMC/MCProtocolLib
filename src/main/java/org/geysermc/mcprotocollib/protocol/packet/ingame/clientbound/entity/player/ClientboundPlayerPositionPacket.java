package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.player;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.PositionElement;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ClientboundPlayerPositionPacket implements MinecraftPacket {
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;
    private final int teleportId;
    private final @NonNull List<PositionElement> relative;

    public ClientboundPlayerPositionPacket(double x, double y, double z, float yaw, float pitch, int teleportId, PositionElement... relative) {
        this(x, y, z, yaw, pitch, teleportId, Arrays.asList(relative != null ? relative : new PositionElement[0]));
    }

    public ClientboundPlayerPositionPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
        this.yaw = in.readFloat();
        this.pitch = in.readFloat();

        this.relative = new ArrayList<>();
        int flags = in.readUnsignedByte();
        for (PositionElement element : PositionElement.values()) {
            int bit = 1 << element.ordinal();
            if ((flags & bit) == bit) {
                this.relative.add(element);
            }
        }

        this.teleportId = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.z);
        out.writeFloat(this.yaw);
        out.writeFloat(this.pitch);

        int flags = 0;
        for (PositionElement element : this.relative) {
            flags |= 1 << element.ordinal();
        }

        out.writeByte(flags);

        helper.writeVarInt(out, this.teleportId);
    }
}
