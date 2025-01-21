package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.entity.EntityEvent;

@Data
@With
@AllArgsConstructor
public class ClientboundEntityEventPacket implements MinecraftPacket {
    private final int entityId;
    private final @NonNull EntityEvent event;

    public ClientboundEntityEventPacket(ByteBuf in) {
        this.entityId = in.readInt();
        this.event = MinecraftTypes.readEntityEvent(in);
    }

    @Override
    public void serialize(ByteBuf out) {
        out.writeInt(this.entityId);
        MinecraftTypes.writeEntityEvent(out, this.event);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
