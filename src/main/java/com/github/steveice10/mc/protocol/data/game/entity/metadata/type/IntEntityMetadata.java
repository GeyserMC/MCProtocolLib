package com.github.steveice10.mc.protocol.data.game.entity.metadata.type;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.IntMetadataType;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

public class IntEntityMetadata extends EntityMetadata<Integer, IntMetadataType> {
    private final int value;

    public IntEntityMetadata(int id, @NotNull IntMetadataType type, int value) {
        super(id, type);
        this.value = value;
    }

    public int getPrimitiveValue() {
        return this.value;
    }

    @Override
    @Deprecated
    public Integer getValue() {
        return this.value;
    }

    @Override
    public void write(MinecraftCodecHelper helper, ByteBuf out) {
        this.type.writeMetadataPrimitive(helper, out, value);
    }
}
