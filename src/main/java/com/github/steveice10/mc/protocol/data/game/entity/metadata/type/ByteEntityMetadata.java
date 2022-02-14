package com.github.steveice10.mc.protocol.data.game.entity.metadata.type;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ByteMetadataType;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.packetlib.io.NetOutput;
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
    protected void write(NetOutput out) throws IOException {
        this.type.writeMetadataPrimitive(out, this.value);
    }
}
