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
public class ServerboundPickItemFromEntityPacket implements MinecraftPacket {
    private final int id;
    private final boolean includeData;

    public ServerboundPickItemFromEntityPacket(ByteBuf in) {
        this.id = MinecraftTypes.readVarInt(in);
        this.includeData = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.id);
        out.writeBoolean(this.includeData);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
