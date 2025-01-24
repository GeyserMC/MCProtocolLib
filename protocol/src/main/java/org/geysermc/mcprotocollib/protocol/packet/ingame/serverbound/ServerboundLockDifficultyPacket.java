package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundLockDifficultyPacket implements MinecraftPacket {
    private final boolean locked;

    public ServerboundLockDifficultyPacket(ByteBuf in) {
        this.locked = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out) {
        out.writeBoolean(this.locked);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
