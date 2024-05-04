package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.player;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.entity.RotationOrigin;

@Data
@With
@AllArgsConstructor
public class ClientboundPlayerLookAtPacket implements MinecraftPacket {
    private final @NonNull RotationOrigin origin;
    private final double x;
    private final double y;
    private final double z;

    private final int targetEntityId;
    private final RotationOrigin targetEntityOrigin;

    public ClientboundPlayerLookAtPacket(RotationOrigin origin, double x, double y, double z) {
        this(origin, x, y, z, 0, null);
    }

    public ClientboundPlayerLookAtPacket(MinecraftByteBuf buf) {
        this.origin = RotationOrigin.from(buf.readVarInt());
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();

        if (buf.readBoolean()) {
            this.targetEntityId = buf.readVarInt();
            this.targetEntityOrigin = RotationOrigin.from(buf.readVarInt());
        } else {
            this.targetEntityId = 0;
            this.targetEntityOrigin = null;
        }
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.origin.ordinal());
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);

        if (this.targetEntityOrigin != null) {
            buf.writeBoolean(true);
            buf.writeVarInt(this.targetEntityId);
            buf.writeVarInt(this.origin.ordinal());
        } else {
            buf.writeBoolean(false);
        }
    }
}
