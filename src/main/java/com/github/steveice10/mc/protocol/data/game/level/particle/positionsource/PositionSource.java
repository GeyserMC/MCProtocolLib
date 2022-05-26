package com.github.steveice10.mc.protocol.data.game.level.particle.positionsource;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public interface PositionSource {

    static PositionSource read(NetInput in) throws IOException {
        PositionSourceType type = PositionSourceType.read(in);
        switch (type) {
            case BLOCK:
                return new BlockPositionSource(Position.read(in));
            case ENTITY:
                return new EntityPositionSource(in.readVarInt(), in.readFloat());
            default:
                throw new IllegalStateException("Unknown position source type!");
        }
    }

    static void write(NetOutput out, PositionSource positionSource) throws IOException {
        positionSource.getType().write(out);
        if (positionSource instanceof BlockPositionSource) {
            Position.write(out, ((BlockPositionSource) positionSource).getPosition());
        } else if (positionSource instanceof EntityPositionSource) {
            out.writeVarInt(((EntityPositionSource) positionSource).getEntityId());
            out.writeFloat(((EntityPositionSource) positionSource).getYOffset());
        }
        throw new IllegalStateException("Unknown position source type!");
    }

    PositionSourceType getType();
}
