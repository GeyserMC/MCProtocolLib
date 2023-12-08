package com.github.steveice10.mc.protocol.data.game.entity.metadata.type;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.LongMetadataType;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

public class LongEntityMetadata extends EntityMetadata<Long, LongMetadataType> {
    private final long value;

    public LongEntityMetadata(int id, @NotNull LongMetadataType type, long value) {
        super(id, type);
        this.value = value;
    }

    public long getPrimitiveValue() {
        return this.value;
    }

    @Override
    @Deprecated
    public Long getValue() {
        return this.value;
    }

    @Override
    public void write(MinecraftCodecHelper helper, ByteBuf out) {
        this.type.writeMetadataPrimitive(helper, out, value);
    }
}
