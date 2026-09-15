package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerboundPunchPacket implements MinecraftPacket {
    public static final ServerboundPunchPacket INSTANCE = new ServerboundPunchPacket();

    public ServerboundPunchPacket(ByteBuf in) {
    }

    @Override
    public void serialize(ByteBuf out) {
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
