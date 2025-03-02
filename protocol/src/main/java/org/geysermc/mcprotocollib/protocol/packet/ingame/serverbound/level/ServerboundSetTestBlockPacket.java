package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.level;

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
public class ServerboundSetTestBlockPacket implements MinecraftPacket {
    private final Vector3i position;
    private final int mode;
    private final String message;

    public ServerboundSetTestBlockPacket(ByteBuf in) {
        this.position = MinecraftTypes.readPosition(in);
        this.mode = MinecraftTypes.readVarInt(in);
        this.message = MinecraftTypes.readString(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writePosition(out, this.position);
        MinecraftTypes.writeVarInt(out, this.mode);
        MinecraftTypes.writeString(out, this.message);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
