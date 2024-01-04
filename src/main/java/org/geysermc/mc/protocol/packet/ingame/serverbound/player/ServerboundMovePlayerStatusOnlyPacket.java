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
public class ServerboundMovePlayerStatusOnlyPacket implements MinecraftPacket {
    private final boolean onGround;

    public ServerboundMovePlayerStatusOnlyPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.onGround = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeBoolean(this.onGround);
    }
}
