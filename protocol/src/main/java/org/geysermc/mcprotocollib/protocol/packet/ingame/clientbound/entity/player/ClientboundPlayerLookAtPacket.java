package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.player;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
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

    public ClientboundPlayerLookAtPacket(ByteBuf in) {
        this.origin = RotationOrigin.from(MinecraftTypes.readVarInt(in));
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();

        if (in.readBoolean()) {
            this.targetEntityId = MinecraftTypes.readVarInt(in);
            this.targetEntityOrigin = RotationOrigin.from(MinecraftTypes.readVarInt(in));
        } else {
            this.targetEntityId = 0;
            this.targetEntityOrigin = null;
        }
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.origin.ordinal());
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.z);

        if (this.targetEntityOrigin != null) {
            out.writeBoolean(true);
            MinecraftTypes.writeVarInt(out, this.targetEntityId);
            MinecraftTypes.writeVarInt(out, this.origin.ordinal());
        } else {
            out.writeBoolean(false);
        }
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
