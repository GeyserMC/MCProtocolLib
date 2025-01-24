package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ServerboundSetCarriedItemPacket implements MinecraftPacket {
    private final int slot;

    public ServerboundSetCarriedItemPacket(ByteBuf in) {
        this.slot = in.readShort();
    }

    @Override
    public void serialize(ByteBuf out) {
        out.writeShort(this.slot);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
