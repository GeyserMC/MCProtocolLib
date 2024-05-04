package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundMovePlayerStatusOnlyPacket implements MinecraftPacket {
    private final boolean onGround;

    public ServerboundMovePlayerStatusOnlyPacket(MinecraftByteBuf buf) {
        this.onGround = buf.readBoolean();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeBoolean(this.onGround);
    }
}
