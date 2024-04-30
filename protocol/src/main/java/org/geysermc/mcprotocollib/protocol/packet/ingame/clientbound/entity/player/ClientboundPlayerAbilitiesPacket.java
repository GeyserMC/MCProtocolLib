package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.player;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundPlayerAbilitiesPacket implements MinecraftPacket {
    private static final int FLAG_INVINCIBLE = 0x01;
    private static final int FLAG_FLYING = 0x02;
    private static final int FLAG_CAN_FLY = 0x04;
    private static final int FLAG_CREATIVE = 0x08;

    private final boolean invincible;
    private final boolean canFly;
    private final boolean flying;
    private final boolean creative;
    private final float flySpeed;
    private final float walkSpeed;

    public ClientboundPlayerAbilitiesPacket(ByteBuf in, MinecraftCodecHelper helper) {
        byte flags = in.readByte();
        this.invincible = (flags & FLAG_INVINCIBLE) > 0;
        this.canFly = (flags & FLAG_CAN_FLY) > 0;
        this.flying = (flags & FLAG_FLYING) > 0;
        this.creative = (flags & FLAG_CREATIVE) > 0;

        this.flySpeed = in.readFloat();
        this.walkSpeed = in.readFloat();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        int flags = 0;
        if (this.invincible) {
            flags |= FLAG_INVINCIBLE;
        }

        if (this.canFly) {
            flags |= FLAG_CAN_FLY;
        }

        if (this.flying) {
            flags |= FLAG_FLYING;
        }

        if (this.creative) {
            flags |= FLAG_CREATIVE;
        }

        out.writeByte(flags);

        out.writeFloat(this.flySpeed);
        out.writeFloat(this.walkSpeed);
    }
}
