package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSetSimulationDistancePacket implements MinecraftPacket {
    private final int simulationDistance;

    public ClientboundSetSimulationDistancePacket(MinecraftByteBuf buf) {
        this.simulationDistance = buf.readVarInt();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.simulationDistance);
    }
}
