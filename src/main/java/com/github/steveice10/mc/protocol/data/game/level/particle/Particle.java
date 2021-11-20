package com.github.steveice10.mc.protocol.data.game.level.particle;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.io.IOException;

@Data
@AllArgsConstructor
public class Particle {
    private final @NonNull ParticleType type;
    private final ParticleData data;

    public static Particle read(NetInput in) throws IOException {
        ParticleType particleType = in.readEnum(ParticleType.VALUES);
        return new Particle(particleType, ParticleData.read(in, particleType));
    }

    public static void write(NetOutput out, Particle particle) throws IOException {
        out.writeEnum(particle.getType());
        ParticleData.write(out, particle.getType(), particle.getData());
    }
}
