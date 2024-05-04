package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.level;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundAcceptTeleportationPacket implements MinecraftPacket {
    private final int id;

    public ServerboundAcceptTeleportationPacket(MinecraftByteBuf buf) {
        this.id = buf.readVarInt();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.id);
    }
}
