package org.geysermc.mc.protocol.packet.ingame.clientbound.inventory;

import org.geysermc.mc.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mc.protocol.codec.MinecraftPacket;
import org.geysermc.mc.protocol.data.game.entity.player.Hand;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

@Data
@With
@AllArgsConstructor
public class ClientboundOpenBookPacket implements MinecraftPacket {
    private final @NonNull Hand hand;

    public ClientboundOpenBookPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.hand = Hand.from(helper.readVarInt(in));
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.hand.ordinal());
    }
}
