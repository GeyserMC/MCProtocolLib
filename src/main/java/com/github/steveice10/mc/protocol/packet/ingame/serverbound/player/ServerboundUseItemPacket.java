package com.github.steveice10.mc.protocol.packet.ingame.serverbound.player;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ServerboundUseItemPacket implements MinecraftPacket {
    private final @NonNull Hand hand;
    private final int sequence;

    public ServerboundUseItemPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.hand = Hand.from(helper.readVarInt(in));
        this.sequence = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.hand.ordinal());
        helper.writeVarInt(out, this.sequence);
    }
}
