package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ServerboundSelectTradePacket implements MinecraftPacket {
    private final int slot;

    public ServerboundSelectTradePacket(ByteBuf in) {
        this.slot = MinecraftTypes.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.slot);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
