package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.player;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSetHeldSlotPacket implements MinecraftPacket {
    private final int slot;

    public ClientboundSetHeldSlotPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.slot = in.readByte();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeByte(this.slot);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
