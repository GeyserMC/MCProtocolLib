package com.github.steveice10.mc.protocol.data.game.world.particle;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public interface ParticleData {
    public static ParticleData read(NetInput in, ParticleType type) throws IOException {
        switch (type) {
            case BLOCK:
                return new BlockParticleData(BlockState.read(in));
            case DUST:
                float red = in.readFloat();
                float green = in.readFloat();
                float blue = in.readFloat();
                float scale = in.readFloat();
                return new DustParticleData(red, green, blue, scale);
            case FALLING_DUST:
                return new FallingDustParticleData(BlockState.read(in));
            case ITEM:
                return new ItemParticleData(ItemStack.read(in));
            default:
                return null;
        }
    }

    public static void write(NetOutput out, ParticleType type, ParticleData data) throws IOException {
        switch (type) {
            case BLOCK:
                BlockState.write(out, ((BlockParticleData) data).getBlockState());
                break;
            case DUST:
                out.writeFloat(((DustParticleData) data).getRed());
                out.writeFloat(((DustParticleData) data).getGreen());
                out.writeFloat(((DustParticleData) data).getBlue());
                out.writeFloat(((DustParticleData) data).getScale());
                break;
            case FALLING_DUST:
                BlockState.write(out, ((FallingDustParticleData) data).getBlockState());
                break;
            case ITEM:
                ItemStack.write(out, ((ItemParticleData) data).getItemStack());
                break;
        }
    }
}
