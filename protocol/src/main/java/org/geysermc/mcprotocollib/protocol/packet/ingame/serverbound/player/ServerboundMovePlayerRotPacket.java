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
public class ServerboundMovePlayerRotPacket implements MinecraftPacket {
    private final boolean onGround;
    private final boolean horizontalCollision;
    private final float yaw;
    private final float pitch;

    public ServerboundMovePlayerRotPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.yaw = in.readFloat();
        this.pitch = in.readFloat();
        int flags = in.readUnsignedByte();
        this.onGround = (flags & 0x1) != 0;
        this.horizontalCollision = (flags & 0x2) != 0;
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeFloat(this.yaw);
        out.writeFloat(this.pitch);
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
