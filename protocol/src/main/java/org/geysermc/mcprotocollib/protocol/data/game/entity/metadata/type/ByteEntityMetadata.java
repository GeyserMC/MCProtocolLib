package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.ByteMetadataType;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.EntityMetadata;
import io.netty.buffer.ByteBuf;
import lombok.NonNull;

import java.io.IOException;

public class ByteEntityMetadata extends EntityMetadata<Byte, ByteMetadataType> {
    private final byte value;

    public ByteEntityMetadata(int id, @NonNull ByteMetadataType type, byte value) {
        super(id, type);
        this.value = value;
    }

    public byte getPrimitiveValue() {
        return this.value;
    }

    @Override
    @Deprecated
    public Byte getValue() {
        return this.value;
    }

    @Override
    public void write(MinecraftCodecHelper helper, ByteBuf out) {
        this.type.writeMetadataPrimitive(out, this.value);
    }
}
