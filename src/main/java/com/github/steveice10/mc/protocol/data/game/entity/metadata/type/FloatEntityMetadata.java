package com.github.steveice10.mc.protocol.data.game.entity.metadata.type;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.FloatMetadataType;
import io.netty.buffer.ByteBuf;
import lombok.NonNull;

import java.io.IOException;

public class FloatEntityMetadata extends EntityMetadata<Float, FloatMetadataType> {
    private final float value;

    public FloatEntityMetadata(int id, @NonNull FloatMetadataType type, float value) {
        super(id, type);
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
    public void write(MinecraftCodecHelper helper, ByteBuf out) throws IOException {
        this.type.writeMetadataPrimitive(out, this.value);
    }
}
