package com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.player;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.RotationOrigin;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

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

    public ClientboundPlayerLookAtPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.origin = MagicValues.key(RotationOrigin.class, helper.readVarInt(in));
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();

        if (in.readBoolean()) {
            this.targetEntityId = helper.readVarInt(in);
            this.targetEntityOrigin = MagicValues.key(RotationOrigin.class, helper.readVarInt(in));
        } else {
            this.targetEntityId = 0;
            this.targetEntityOrigin = null;
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, MagicValues.value(Integer.class, this.origin));
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.z);

        if (this.targetEntityOrigin != null) {
            out.writeBoolean(true);
            helper.writeVarInt(out, this.targetEntityId);
            helper.writeVarInt(out, MagicValues.value(Integer.class, this.targetEntityOrigin));
        } else {
            out.writeBoolean(false);
        }
    }
}
