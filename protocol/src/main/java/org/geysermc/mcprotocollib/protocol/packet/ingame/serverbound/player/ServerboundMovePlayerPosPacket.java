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
    private final boolean horizontalCollision;
    private final double x;
    private final double y;
    private final double z;

    public ServerboundMovePlayerPosPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
        int flags = in.readUnsignedByte();
        this.onGround = (flags & 0x1) != 0;
        this.horizontalCollision = (flags & 0x2) != 0;
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.z);
        int flags = 0;
        if (this.onGround) {
            flags |= 0x1;
        }

        if (this.horizontalCollision) {
            flags |= 0x2;
        }

        out.writeByte(flags);
    }
}
