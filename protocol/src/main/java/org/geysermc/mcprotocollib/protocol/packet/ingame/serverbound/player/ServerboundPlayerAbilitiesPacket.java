package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundPlayerAbilitiesPacket implements MinecraftPacket {
    private static final int FLAG_FLYING = 0x02;

    private final boolean flying;

    public ServerboundPlayerAbilitiesPacket(ByteBuf in, MinecraftCodecHelper helper) {
        byte flags = in.readByte();
        this.flying = (flags & FLAG_FLYING) > 0;
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        int flags = 0;

        if (this.flying) {
            flags |= FLAG_FLYING;
        }

        out.writeByte(flags);
    }
}
