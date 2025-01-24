package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ServerboundPickItemFromBlockPacket implements MinecraftPacket {
    private final Vector3i pos;
    private final boolean includeData;

    public ServerboundPickItemFromBlockPacket(ByteBuf in) {
        this.pos = MinecraftTypes.readPosition(in);
        this.includeData = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writePosition(out, this.pos);
        out.writeBoolean(this.includeData);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
