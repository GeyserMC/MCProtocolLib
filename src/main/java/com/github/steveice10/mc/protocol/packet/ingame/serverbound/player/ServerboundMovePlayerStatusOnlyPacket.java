package com.github.steveice10.mc.protocol.packet.ingame.serverbound.player;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
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
