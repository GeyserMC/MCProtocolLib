package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.type;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.NonNull;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.ByteMetadataType;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.EntityMetadata;

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
    public void write(MinecraftByteBuf out) {
        this.type.writeMetadataPrimitive(out, this.value);
    }
}
