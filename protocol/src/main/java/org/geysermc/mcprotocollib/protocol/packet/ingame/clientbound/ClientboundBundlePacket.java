package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

import java.util.List;

@Data
@AllArgsConstructor
public class ClientboundBundlePacket implements MinecraftPacket {
    private final List<MinecraftPacket> packets;

    @Override
    public void serialize(MinecraftByteBuf buf) {
    }
}
