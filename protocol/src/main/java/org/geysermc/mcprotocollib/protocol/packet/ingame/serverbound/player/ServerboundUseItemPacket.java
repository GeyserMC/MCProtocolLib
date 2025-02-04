package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.Hand;

@Data
@With
@AllArgsConstructor
public class ServerboundUseItemPacket implements MinecraftPacket {
    private final @NonNull Hand hand;
    private final int sequence;
    private final float yRot;
    private final float xRot;

    public ServerboundUseItemPacket(ByteBuf in) {
        this.hand = Hand.from(MinecraftTypes.readVarInt(in));
        this.sequence = MinecraftTypes.readVarInt(in);
        this.yRot = in.readFloat();
        this.xRot = in.readFloat();
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.hand.ordinal());
        MinecraftTypes.writeVarInt(out, this.sequence);
        out.writeFloat(this.yRot);
        out.writeFloat(this.xRot);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
