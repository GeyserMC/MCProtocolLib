package org.geysermc.mc.protocol.packet.ingame.serverbound.player;

import org.geysermc.mc.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

@Data
@With
@AllArgsConstructor
public class ServerboundMovePlayerRotPacket implements MinecraftPacket {
    private final boolean onGround;
    private final float yaw;
    private final float pitch;

    public ServerboundMovePlayerRotPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.yaw = in.readFloat();
        this.pitch = in.readFloat();
        this.onGround = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeFloat(this.yaw);
        out.writeFloat(this.pitch);
        out.writeBoolean(this.onGround);
    }
}
