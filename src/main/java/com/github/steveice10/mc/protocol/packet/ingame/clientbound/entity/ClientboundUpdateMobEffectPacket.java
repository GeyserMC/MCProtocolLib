package com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity;

import com.github.steveice10.mc.protocol.data.game.NBT;
import com.github.steveice10.mc.protocol.data.game.entity.Effect;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import javax.annotation.Nullable;
import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundUpdateMobEffectPacket implements Packet {
    private static final int FLAG_AMBIENT = 0x01;
    private static final int FLAG_SHOW_PARTICLES = 0x02;

    private final int entityId;
    private final @NonNull Effect effect;
    private final int amplifier;
    private final int duration;
    private final boolean ambient;
    private final boolean showParticles;
    private final @Nullable CompoundTag factorData;

    public ClientboundUpdateMobEffectPacket(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.effect = Effect.fromNetworkId(in.readVarInt());
        this.amplifier = in.readByte();
        this.duration = in.readVarInt();

        int flags = in.readByte();
        this.ambient = (flags & FLAG_AMBIENT) != 0;
        this.showParticles = (flags & FLAG_SHOW_PARTICLES) != 0;
        if (in.readBoolean()) {
            this.factorData = NBT.read(in);
        } else {
            this.factorData = null;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeVarInt(Effect.toNetworkId(this.effect));
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

        boolean hasFactorData = this.factorData != null;
        out.writeBoolean(hasFactorData);
        if (hasFactorData) {
            NBT.write(out, this.factorData);
        }
    }
}
