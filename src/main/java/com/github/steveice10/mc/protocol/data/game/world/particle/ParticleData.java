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
                float red = in.readFloat();
                float green = in.readFloat();
                float blue = in.readFloat();
                float scale = in.readFloat();
                return new DustParticleData(red, green, blue, scale);
            case DUST_COLOR_TRANSITION:
                red = in.readFloat();
                green = in.readFloat();
                blue = in.readFloat();
                scale = in.readFloat();
                float newRed = in.readFloat();
                float newGreen = in.readFloat();
                float newBlue = in.readFloat();
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
                out.writeFloat(((DustParticleData) data).getRed());
                out.writeFloat(((DustParticleData) data).getGreen());
                out.writeFloat(((DustParticleData) data).getBlue());
                out.writeFloat(((DustParticleData) data).getScale());
                break;
            case DUST_COLOR_TRANSITION:
                out.writeFloat(((DustParticleData) data).getRed());
                out.writeFloat(((DustParticleData) data).getGreen());
                out.writeFloat(((DustParticleData) data).getBlue());
                out.writeFloat(((DustParticleData) data).getScale());
                out.writeFloat(((DustColorTransitionParticleData) data).getNewRed());
                out.writeFloat(((DustColorTransitionParticleData) data).getNewGreen());
                out.writeFloat(((DustColorTransitionParticleData) data).getNewBlue());
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
