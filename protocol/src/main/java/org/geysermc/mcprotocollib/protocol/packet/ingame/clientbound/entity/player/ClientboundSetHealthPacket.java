package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.player;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundSetHealthPacket implements MinecraftPacket {
    private final float health;
    private final int food;
    private final float saturation;

    public ClientboundSetHealthPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.health = in.readFloat();
        this.food = helper.readVarInt(in);
        this.saturation = in.readFloat();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeFloat(this.health);
        helper.writeVarInt(out, this.food);
        out.writeFloat(this.saturation);
    }
}
