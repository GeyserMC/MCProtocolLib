package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.Hand;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.ItemTypes;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.SwingAnimation;

@Data
@With
@AllArgsConstructor
public class ClientboundSwingAnimationPacket implements MinecraftPacket {
    private final int entityId;
    private final Hand hand;
    private final SwingAnimation animation;

    public ClientboundSwingAnimationPacket(ByteBuf in) {
        this.entityId = MinecraftTypes.readVarInt(in);
        this.hand = Hand.from(MinecraftTypes.readVarInt(in));
        this.animation = ItemTypes.readSwingAnimation(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.entityId);
        MinecraftTypes.writeVarInt(out, this.hand.ordinal());
        ItemTypes.writeSwingAnimation(out, this.animation);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
