package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.inventory;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.Hand;

@Data
@With
@AllArgsConstructor
public class ClientboundOpenBookPacket implements MinecraftPacket {
    private final @NonNull Hand hand;

    public ClientboundOpenBookPacket(MinecraftByteBuf buf) {
        this.hand = Hand.from(buf.readVarInt());
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.hand.ordinal());
    }
}
