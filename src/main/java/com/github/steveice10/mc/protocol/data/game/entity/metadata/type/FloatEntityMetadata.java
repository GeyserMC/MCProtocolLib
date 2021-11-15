package com.github.steveice10.mc.protocol.data.game.entity.metadata.type;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.MetadataType;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public class FloatEntityMetadata extends EntityMetadata<Float> {
    private final float value;

    public FloatEntityMetadata(int id, float value) {
        super(id, MetadataType.FLOAT);
        this.value = value;
    }

    public float getPrimitiveValue() {
        return this.value;
    }

    @Override
    @Deprecated
    public Float getValue() {
        return this.value;
    }

    @Override
    protected void write(NetOutput out) throws IOException {
        out.writeFloat(this.value);
    }
}
