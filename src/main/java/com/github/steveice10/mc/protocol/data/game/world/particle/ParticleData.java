package com.github.steveice10.mc.protocol.data.game.world.particle;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public interface ParticleData {
    static ParticleData read(NetInput in, ParticleType type) throws IOException {
        switch (type) {
            case BLOCK:
                return new BlockParticleData(in.readVarInt());
            case DUST:
                double red = in.readDouble();
                double green = in.readDouble();
                double blue = in.readDouble();
                float scale = in.readFloat();
                return new DustParticleData(red, green, blue, scale);
            case DUST_COLOR_TRANSITION:
                red = in.readDouble();
                green = in.readDouble();
                blue = in.readDouble();
                scale = in.readFloat();
                double newRed = in.readDouble();
                double newGreen = in.readDouble();
                double newBlue = in.readDouble();
                return new DustColorTransitionParticleData(red, green, blue, scale, newRed, newGreen, newBlue);
            case FALLING_DUST:
                return new FallingDustParticleData(in.readVarInt());
            case ITEM:
                return new ItemParticleData(ItemStack.read(in));
            default:
                return null;
        }
    }

    static void write(NetOutput out, ParticleType type, ParticleData data) throws IOException {
        switch (type) {
            case BLOCK:
                out.writeVarInt(((BlockParticleData) data).getBlockState());
                break;
            case DUST:
                out.writeDouble(((DustParticleData) data).getRed());
                out.writeDouble(((DustParticleData) data).getGreen());
                out.writeDouble(((DustParticleData) data).getBlue());
                out.writeFloat(((DustParticleData) data).getScale());
                break;
            case DUST_COLOR_TRANSITION:
                out.writeDouble(((DustParticleData) data).getRed());
                out.writeDouble(((DustParticleData) data).getGreen());
                out.writeDouble(((DustParticleData) data).getBlue());
                out.writeFloat(((DustParticleData) data).getScale());
                out.writeDouble(((DustColorTransitionParticleData) data).getNewRed());
                out.writeDouble(((DustColorTransitionParticleData) data).getNewGreen());
                out.writeDouble(((DustColorTransitionParticleData) data).getNewBlue());
                break;
            case FALLING_DUST:
                out.writeVarInt(((FallingDustParticleData) data).getBlockState());
                break;
            case ITEM:
                ItemStack.write(out, ((ItemParticleData) data).getItemStack());
                break;
        }
    }
}
