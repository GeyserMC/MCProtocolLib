package com.github.steveice10.mc.protocol.packet.ingame.server.entity;

import com.github.steveice10.mc.protocol.data.game.entity.Effect;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.With;

import java.io.IOException;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerEntityRemoveEffectPacket implements Packet {
    private int entityId;
    private @NonNull Effect effect;

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.effect = Effect.fromNetworkId(in.readUnsignedByte());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeByte(Effect.toNetworkId(this.effect));
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
