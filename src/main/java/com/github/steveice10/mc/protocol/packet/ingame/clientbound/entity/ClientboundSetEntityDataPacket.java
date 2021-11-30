package com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundSetEntityDataPacket implements Packet {
    private final int entityId;
    private final @NonNull EntityMetadata<?, ?>[] metadata;

    public ClientboundSetEntityDataPacket(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.metadata = EntityMetadata.read(in);
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        EntityMetadata.write(out, this.metadata);
    }
}
