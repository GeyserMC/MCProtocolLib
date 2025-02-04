package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ServerboundTeleportToEntityPacket implements MinecraftPacket {
    private final @NonNull UUID target;

    public ServerboundTeleportToEntityPacket(ByteBuf in) {
        this.target = MinecraftTypes.readUUID(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeUUID(out, this.target);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
