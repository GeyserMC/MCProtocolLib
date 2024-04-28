package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundProjectilePowerPacket implements MinecraftPacket {
    private final int id;
    private final double xPower;
    private final double yPower;
    private final double zPower;

    public ClientboundProjectilePowerPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.id = helper.readVarInt(in);
        this.xPower = in.readDouble();
        this.yPower = in.readDouble();
        this.zPower = in.readDouble();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.id);
        out.writeDouble(this.xPower);
        out.writeDouble(this.yPower);
        out.writeDouble(this.zPower);
    }
}
