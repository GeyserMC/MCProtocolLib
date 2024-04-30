package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSelectAdvancementsTabPacket implements MinecraftPacket {
    private final String tabId;

    public ClientboundSelectAdvancementsTabPacket(ByteBuf in, MinecraftCodecHelper helper) {
        if (in.readBoolean()) {
            this.tabId = helper.readString(in);
        } else {
            this.tabId = null;
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        if (this.tabId != null) {
            out.writeBoolean(true);
            helper.writeString(out, this.tabId);
        } else {
            out.writeBoolean(false);
        }
    }
}
