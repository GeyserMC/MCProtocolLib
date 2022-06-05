package com.github.steveice10.mc.protocol.data.game.entity.metadata.type;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.BooleanMetadataType;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import io.netty.buffer.ByteBuf;
import lombok.NonNull;

import java.io.IOException;

public class BooleanEntityMetadata extends EntityMetadata<Boolean, BooleanMetadataType> {
    private final boolean value;

    public BooleanEntityMetadata(int id, @NonNull BooleanMetadataType type, boolean value) {
        super(id, type);
        this.value = value;
    }

    public boolean getPrimitiveValue() {
        return this.value;
    }

    @Override
    @Deprecated
    public Boolean getValue() {
        return this.value;
    }

    @Override
    public void write(MinecraftCodecHelper helper, ByteBuf out) throws IOException {
        this.type.writeMetadataPrimitive(out, this.value);
    }
}
