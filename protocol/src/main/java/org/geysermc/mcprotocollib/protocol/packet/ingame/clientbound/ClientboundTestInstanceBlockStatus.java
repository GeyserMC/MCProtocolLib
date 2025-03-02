package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.math.vector.Vector3i;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.jetbrains.annotations.Nullable;

@Data
@With
@AllArgsConstructor
public class ClientboundTestInstanceBlockStatus implements MinecraftPacket {
    private final Component status;
    private final @Nullable Vector3i size;

    public ClientboundTestInstanceBlockStatus(ByteBuf in) {
        this.status = MinecraftTypes.readComponent(in);
        this.size = MinecraftTypes.readNullable(in, MinecraftTypes::readVec3i);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeComponent(out, this.status);
        MinecraftTypes.writeNullable(out, this.size, MinecraftTypes::writeVec3i);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
