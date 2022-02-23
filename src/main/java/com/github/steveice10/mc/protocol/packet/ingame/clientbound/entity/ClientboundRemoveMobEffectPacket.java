package com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity;

import com.github.steveice10.mc.protocol.data.game.entity.Effect;
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
public class ClientboundRemoveMobEffectPacket implements Packet {
    private final int entityId;
    private final @NonNull Effect effect;

    public ClientboundRemoveMobEffectPacket(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.effect = Effect.fromNetworkId(in.readVarInt());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeVarInt(Effect.toNetworkId(this.effect));
    }
}
