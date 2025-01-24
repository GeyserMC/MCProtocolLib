package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundProjectilePowerPacket implements MinecraftPacket {
    private final int id;
    private final double accelerationPower;

    public ClientboundProjectilePowerPacket(ByteBuf in) {
        this.id = MinecraftTypes.readVarInt(in);
        this.accelerationPower = in.readDouble();
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.id);
        out.writeDouble(this.accelerationPower);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
