package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundSetSimulationDistancePacket implements MinecraftPacket {
    private final int simulationDistance;

    public ClientboundSetSimulationDistancePacket(ByteBuf in) {
        this.simulationDistance = MinecraftTypes.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.simulationDistance);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
