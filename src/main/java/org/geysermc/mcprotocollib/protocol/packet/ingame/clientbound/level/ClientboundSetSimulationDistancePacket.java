package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

@Data
@With
@AllArgsConstructor
public class ClientboundSetSimulationDistancePacket implements MinecraftPacket {
    private final int simulationDistance;

    public ClientboundSetSimulationDistancePacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.simulationDistance = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.simulationDistance);
    }
}
