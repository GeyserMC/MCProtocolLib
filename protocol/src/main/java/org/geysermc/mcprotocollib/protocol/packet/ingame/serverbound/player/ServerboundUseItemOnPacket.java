package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.entity.object.Direction;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.Hand;

@Data
@With
@AllArgsConstructor
public class ServerboundUseItemOnPacket implements MinecraftPacket {
    private final @NonNull Vector3i position;
    private final @NonNull Direction face;
    private final @NonNull Hand hand;
    private final float cursorX;
    private final float cursorY;
    private final float cursorZ;
    private final boolean insideBlock;
    private final int sequence;

    public ServerboundUseItemOnPacket(MinecraftByteBuf buf) {
        this.hand = Hand.from(buf.readVarInt());
        this.position = buf.readPosition();
        this.face = buf.readDirection();
        this.cursorX = buf.readFloat();
        this.cursorY = buf.readFloat();
        this.cursorZ = buf.readFloat();
        this.insideBlock = buf.readBoolean();
        this.sequence = buf.readVarInt();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.hand.ordinal());
        buf.writePosition(this.position);
        buf.writeDirection(this.face);
        buf.writeFloat(this.cursorX);
        buf.writeFloat(this.cursorY);
        buf.writeFloat(this.cursorZ);
        buf.writeBoolean(this.insideBlock);
        buf.writeVarInt(this.sequence);
    }
}
