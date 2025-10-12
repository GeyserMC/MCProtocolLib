package org.geysermc.mcprotocollib.protocol.packet.configuration.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundClientTickEndPacket;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerboundAcceptCodeOfConductPacket implements MinecraftPacket {
    public static final ServerboundAcceptCodeOfConductPacket INSTANCE = new ServerboundAcceptCodeOfConductPacket();

    @Override
    public void serialize(ByteBuf out) {}

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
