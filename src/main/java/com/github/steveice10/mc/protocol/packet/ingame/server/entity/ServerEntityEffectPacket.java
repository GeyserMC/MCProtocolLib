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
public class ServerEntityEffectPacket implements Packet {
    private static final int FLAG_AMBIENT = 0x01;
    private static final int FLAG_SHOW_PARTICLES = 0x02;

    private int entityId;
    private @NonNull Effect effect;
    private int amplifier;
    private int duration;
    private boolean ambient;
    private boolean showParticles;

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.effect = Effect.fromNetworkId(in.readByte());
        this.amplifier = in.readByte();
        this.duration = in.readVarInt();

        int flags = in.readByte();
        this.ambient = (flags & FLAG_AMBIENT) != 0;
        this.showParticles = (flags & FLAG_SHOW_PARTICLES) != 0;
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeByte(Effect.toNetworkId(this.effect));
        out.writeByte(this.amplifier);
        out.writeVarInt(this.duration);

        int flags = 0;
        if (this.ambient) {
            flags |= FLAG_AMBIENT;
        }

        if (this.showParticles) {
            flags |= FLAG_SHOW_PARTICLES;
        }

        out.writeByte(flags);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
