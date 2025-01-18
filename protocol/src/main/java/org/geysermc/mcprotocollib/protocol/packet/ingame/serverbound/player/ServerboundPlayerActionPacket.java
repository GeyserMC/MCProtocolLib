package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
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

    public ServerboundPlayerActionPacket(ByteBuf in) {
        this.action = PlayerAction.from(MinecraftTypes.readVarInt(in));
        this.position = MinecraftTypes.readPosition(in);
        this.face = Direction.VALUES[in.readUnsignedByte()];
        this.sequence = MinecraftTypes.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.action.ordinal());
        MinecraftTypes.writePosition(out, this.position);
        out.writeByte(this.face.ordinal());
        MinecraftTypes.writeVarInt(out, this.sequence);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
