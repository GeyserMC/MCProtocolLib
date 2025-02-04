package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.player;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundPlayerRotationPacket implements MinecraftPacket {
    private final float yRot;
    private final float xRot;

    public ClientboundPlayerRotationPacket(ByteBuf in) {
        this.yRot = in.readFloat();
        this.xRot = in.readFloat();
    }

    @Override
    public void serialize(ByteBuf out) {
        out.writeFloat(this.yRot);
        out.writeFloat(this.xRot);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
