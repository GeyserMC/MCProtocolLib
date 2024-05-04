package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundPlayerAbilitiesPacket implements MinecraftPacket {
    private static final int FLAG_FLYING = 0x02;

    private final boolean flying;

    public ServerboundPlayerAbilitiesPacket(MinecraftByteBuf buf) {
        byte flags = buf.readByte();
        this.flying = (flags & FLAG_FLYING) > 0;
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        int flags = 0;

        if (this.flying) {
            flags |= FLAG_FLYING;
        }

        buf.writeByte(flags);
    }
}
