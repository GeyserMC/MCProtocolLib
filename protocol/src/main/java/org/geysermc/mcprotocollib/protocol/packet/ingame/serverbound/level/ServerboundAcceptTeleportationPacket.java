package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ServerboundAcceptTeleportationPacket implements MinecraftPacket {
    private final int id;

    public ServerboundAcceptTeleportationPacket(ByteBuf in) {
        this.id = MinecraftTypes.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.id);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
