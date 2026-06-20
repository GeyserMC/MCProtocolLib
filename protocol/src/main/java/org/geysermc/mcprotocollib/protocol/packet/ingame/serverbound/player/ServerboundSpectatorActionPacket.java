package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

import java.util.OptionalInt;

@Data
@With
@AllArgsConstructor
public class ServerboundSpectatorActionPacket implements MinecraftPacket {
    private final OptionalInt entityId;

    public ServerboundSpectatorActionPacket(ByteBuf in) {
        int i = MinecraftTypes.readVarInt(in);
        this.entityId = i == 0 ? OptionalInt.empty() : OptionalInt.of(i - 1);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.entityId.isPresent() ? this.entityId.getAsInt() + 1 : 0);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
