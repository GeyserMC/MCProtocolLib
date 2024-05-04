package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.entity.object.Direction;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.PlayerAction;

@Data
@With
@AllArgsConstructor
public class ServerboundPlayerActionPacket implements MinecraftPacket {
    private final @NonNull PlayerAction action;
    private final @NonNull Vector3i position;
    private final @NonNull Direction face;
    private final int sequence;

    public ServerboundPlayerActionPacket(MinecraftByteBuf buf) {
        this.action = PlayerAction.from(buf.readVarInt());
        this.position = buf.readPosition();
        this.face = Direction.VALUES[buf.readUnsignedByte()];
        this.sequence = buf.readVarInt();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.action.ordinal());
        buf.writePosition(this.position);
        buf.writeByte(this.face.ordinal());
        buf.writeVarInt(this.sequence);
    }
}
