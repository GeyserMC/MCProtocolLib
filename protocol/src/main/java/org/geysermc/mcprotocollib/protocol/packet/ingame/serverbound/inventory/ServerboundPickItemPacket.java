package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundPickItemPacket implements MinecraftPacket {
    private final int slot;

    public ServerboundPickItemPacket(MinecraftByteBuf buf) {
        this.slot = buf.readVarInt();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.slot);
    }
}
