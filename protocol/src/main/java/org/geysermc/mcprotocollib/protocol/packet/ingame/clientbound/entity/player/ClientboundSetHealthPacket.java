package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.player;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

@Data
@With
@AllArgsConstructor
public class ClientboundSetHealthPacket implements MinecraftPacket {
    private final float health;
    private final int food;
    private final float saturation;

    public ClientboundSetHealthPacket(ByteBuf in) {
        this.health = in.readFloat();
        this.food = MinecraftTypes.readVarInt(in);
        this.saturation = in.readFloat();
    }

    @Override
    public void serialize(ByteBuf out) {
        out.writeFloat(this.health);
        MinecraftTypes.writeVarInt(out, this.food);
        out.writeFloat(this.saturation);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
