package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.player;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.PositionElement;

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

    public ClientboundPlayerPositionPacket(MinecraftByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.yaw = buf.readFloat();
        this.pitch = buf.readFloat();

        this.relative = new ArrayList<>();
        int flags = buf.readUnsignedByte();
        for (PositionElement element : PositionElement.values()) {
            int bit = 1 << element.ordinal();
            if ((flags & bit) == bit) {
                this.relative.add(element);
            }
        }

        this.teleportId = buf.readVarInt();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeFloat(this.yaw);
        buf.writeFloat(this.pitch);

        int flags = 0;
        for (PositionElement element : this.relative) {
            flags |= 1 << element.ordinal();
        }

        buf.writeByte(flags);

        buf.writeVarInt(this.teleportId);
    }
}
