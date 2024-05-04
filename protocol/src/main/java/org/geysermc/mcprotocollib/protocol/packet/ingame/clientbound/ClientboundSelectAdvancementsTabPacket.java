package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSelectAdvancementsTabPacket implements MinecraftPacket {
    private final String tabId;

    public ClientboundSelectAdvancementsTabPacket(MinecraftByteBuf buf) {
        if (buf.readBoolean()) {
            this.tabId = buf.readString();
        } else {
            this.tabId = null;
        }
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        if (this.tabId != null) {
            buf.writeBoolean(true);
            buf.writeString(this.tabId);
        } else {
            buf.writeBoolean(false);
        }
    }
}
