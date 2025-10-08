package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.GlobalPos;

@Data
@With
@AllArgsConstructor
public class ClientboundSetDefaultSpawnPositionPacket implements MinecraftPacket {
    private final GlobalPos globalPos;
    private final float yaw;
    private final float pitch;

    public ClientboundSetDefaultSpawnPositionPacket(ByteBuf in) {
        this.globalPos = MinecraftTypes.readGlobalPos(in);
        this.yaw = in.readFloat();
        this.pitch = in.readFloat();
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeGlobalPos(out, this.globalPos);
        out.writeFloat(this.yaw);
        out.writeFloat(this.pitch);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
