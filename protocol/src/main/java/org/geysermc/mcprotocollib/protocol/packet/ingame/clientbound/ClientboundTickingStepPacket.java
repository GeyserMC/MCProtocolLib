package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundTickingStepPacket implements MinecraftPacket {

    private final int tickSteps;

    public ClientboundTickingStepPacket(ByteBuf in) {
        this.tickSteps = MinecraftTypes.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.tickSteps);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
