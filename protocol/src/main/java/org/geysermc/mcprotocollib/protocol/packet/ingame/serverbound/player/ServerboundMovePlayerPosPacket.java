package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundMovePlayerPosPacket implements MinecraftPacket {
    private final boolean onGround;
    private final double x;
    private final double y;
    private final double z;

    public ServerboundMovePlayerPosPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
        this.onGround = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.z);
        out.writeBoolean(this.onGround);
    }
}
