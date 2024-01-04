package org.geysermc.mc.protocol.packet.ingame.clientbound;

import org.geysermc.mc.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ClientboundBundlePacket implements MinecraftPacket {
    private final List<MinecraftPacket> packets;

    @Override
    public void serialize(ByteBuf buf, MinecraftCodecHelper helper) {
    }
}
