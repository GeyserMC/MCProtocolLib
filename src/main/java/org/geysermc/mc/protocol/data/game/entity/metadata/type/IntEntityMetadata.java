package org.geysermc.mc.protocol.data.game.entity.metadata.type;

import org.geysermc.mc.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mc.protocol.data.game.entity.metadata.EntityMetadata;
import org.geysermc.mc.protocol.data.game.entity.metadata.IntMetadataType;
import io.netty.buffer.ByteBuf;
import lombok.NonNull;

import java.io.IOException;

public class IntEntityMetadata extends EntityMetadata<Integer, IntMetadataType> {
    private final int value;

    public IntEntityMetadata(int id, @NonNull IntMetadataType type, int value) {
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
    public void write(MinecraftCodecHelper helper, ByteBuf out) throws IOException {
        this.type.writeMetadataPrimitive(helper, out, value);
    }
}
