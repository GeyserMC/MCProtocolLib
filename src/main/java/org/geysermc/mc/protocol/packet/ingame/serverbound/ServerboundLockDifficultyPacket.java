package org.geysermc.mc.protocol.packet.ingame.serverbound;

import org.geysermc.mc.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

@Data
@With
@AllArgsConstructor
public class ServerboundLockDifficultyPacket implements MinecraftPacket {
    private final boolean locked;

    public ServerboundLockDifficultyPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.locked = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeBoolean(this.locked);
    }
}
