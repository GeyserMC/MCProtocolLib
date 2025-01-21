package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

public class ClientboundDelimiterPacket implements MinecraftPacket {
    public ClientboundDelimiterPacket(ByteBuf in) {
    }

    @Override
    public void serialize(ByteBuf out) {
    }
}
