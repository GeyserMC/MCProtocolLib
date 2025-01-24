package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerboundClientTickEndPacket implements MinecraftPacket {
    public static final ServerboundClientTickEndPacket INSTANCE = new ServerboundClientTickEndPacket();

    @Override
    public void serialize(ByteBuf out) {
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
