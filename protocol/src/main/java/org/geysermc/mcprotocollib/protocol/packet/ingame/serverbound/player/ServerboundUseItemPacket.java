package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.Hand;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

@Data
@With
@AllArgsConstructor
public class ServerboundUseItemPacket implements MinecraftPacket {
    private final @NonNull Hand hand;
    private final int sequence;

    public ServerboundUseItemPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.hand = Hand.from(helper.readVarInt(in));
        this.sequence = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.hand.ordinal());
        helper.writeVarInt(out, this.sequence);
    }
}
