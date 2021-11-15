package com.github.steveice10.mc.protocol.data.game.entity.metadata.type;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.MetadataType;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public class ByteEntityMetadata extends EntityMetadata<Byte> {
    private final byte value;

    public ByteEntityMetadata(int id, byte value) {
        super(id, MetadataType.BYTE);
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
        out.writeByte(this.value);
    }
}
