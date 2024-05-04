package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.player;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSetCarriedItemPacket implements MinecraftPacket {
    private final int slot;

    public ClientboundSetCarriedItemPacket(MinecraftByteBuf buf) {
        this.slot = buf.readByte();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeByte(this.slot);
    }
}
