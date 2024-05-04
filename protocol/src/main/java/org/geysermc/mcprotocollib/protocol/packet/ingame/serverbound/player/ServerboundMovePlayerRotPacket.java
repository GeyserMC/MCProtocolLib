package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundMovePlayerRotPacket implements MinecraftPacket {
    private final boolean onGround;
    private final float yaw;
    private final float pitch;

    public ServerboundMovePlayerRotPacket(MinecraftByteBuf buf) {
        this.yaw = buf.readFloat();
        this.pitch = buf.readFloat();
        this.onGround = buf.readBoolean();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeFloat(this.yaw);
        buf.writeFloat(this.pitch);
        buf.writeBoolean(this.onGround);
    }
}
