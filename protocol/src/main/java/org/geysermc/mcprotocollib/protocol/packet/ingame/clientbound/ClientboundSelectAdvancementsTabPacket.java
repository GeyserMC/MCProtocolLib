package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSelectAdvancementsTabPacket implements MinecraftPacket {
    private final @Nullable String tabId;

    public ClientboundSelectAdvancementsTabPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.tabId = helper.readNullable(in, helper::readString);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeNullable(out, this.tabId, helper::writeString);
    }
}
