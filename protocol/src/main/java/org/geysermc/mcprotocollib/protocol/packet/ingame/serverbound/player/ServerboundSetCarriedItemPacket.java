package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundSetCarriedItemPacket implements MinecraftPacket {
    private final int slot;

    public ServerboundSetCarriedItemPacket(MinecraftByteBuf buf) {
        this.slot = buf.readShort();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeShort(this.slot);
    }
}
