package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.EntityMetadata;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.LongMetadataType;
import io.netty.buffer.ByteBuf;
import lombok.NonNull;

import java.io.IOException;

public class LongEntityMetadata extends EntityMetadata<Long, LongMetadataType> {
    private final long value;

    public LongEntityMetadata(int id, @NonNull LongMetadataType type, long value) {
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
    public void write(MinecraftCodecHelper helper, ByteBuf out) throws IOException {
        this.type.writeMetadataPrimitive(helper, out, value);
    }
}
