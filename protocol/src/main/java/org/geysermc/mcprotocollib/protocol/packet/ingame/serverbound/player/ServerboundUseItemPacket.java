package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.Hand;

@Data
@With
@AllArgsConstructor
public class ServerboundUseItemPacket implements MinecraftPacket {
    private final @NonNull Hand hand;
    private final int sequence;

    public ServerboundUseItemPacket(MinecraftByteBuf buf) {
        this.hand = Hand.from(buf.readVarInt());
        this.sequence = buf.readVarInt();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.hand.ordinal());
        buf.writeVarInt(this.sequence);
    }
}
