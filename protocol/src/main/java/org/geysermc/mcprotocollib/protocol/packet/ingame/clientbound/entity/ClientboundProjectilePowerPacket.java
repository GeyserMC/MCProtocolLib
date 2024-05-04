package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundProjectilePowerPacket implements MinecraftPacket {
    private final int id;
    private final double xPower;
    private final double yPower;
    private final double zPower;

    public ClientboundProjectilePowerPacket(MinecraftByteBuf buf) {
        this.id = buf.readVarInt();
        this.xPower = buf.readDouble();
        this.yPower = buf.readDouble();
        this.zPower = buf.readDouble();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.id);
        buf.writeDouble(this.xPower);
        buf.writeDouble(this.yPower);
        buf.writeDouble(this.zPower);
    }
}
