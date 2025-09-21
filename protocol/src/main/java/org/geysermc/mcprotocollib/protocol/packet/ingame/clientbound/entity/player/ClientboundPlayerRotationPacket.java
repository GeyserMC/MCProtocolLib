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
    private final boolean relativeY;
    private final float xRot;
    private final boolean relativeX;

    public ClientboundPlayerRotationPacket(ByteBuf in) {
        this.yRot = in.readFloat();
        this.relativeY = in.readBoolean();
        this.xRot = in.readFloat();
        this.relativeX = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out) {
        out.writeFloat(this.yRot);
        out.writeBoolean(this.relativeY);
        out.writeFloat(this.xRot);
        out.writeBoolean(this.relativeX);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
