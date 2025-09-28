package org.geysermc.mcprotocollib.protocol.packet.configuration.serverbound;

import io.netty.buffer.ByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

public class ServerboundAcceptCodeOfConductPacket implements MinecraftPacket {

    public ServerboundAcceptCodeOfConductPacket(ByteBuf in) {}

    @Override
    public void serialize(ByteBuf out) {}

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
