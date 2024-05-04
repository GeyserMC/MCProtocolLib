package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundMovePlayerPosPacket implements MinecraftPacket {
    private final boolean onGround;
    private final double x;
    private final double y;
    private final double z;

    public ServerboundMovePlayerPosPacket(MinecraftByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.onGround = buf.readBoolean();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeBoolean(this.onGround);
    }
}
