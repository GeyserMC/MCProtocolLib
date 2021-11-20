package com.github.steveice10.mc.protocol.data.game.entity.metadata.type;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.IntMetadataType;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.MetadataType;
import com.github.steveice10.packetlib.io.NetOutput;
import lombok.NonNull;

import java.io.IOException;

/**
 * Used for both {@link MetadataType#INT} and {@link MetadataType#BLOCK_STATE}.
 */
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
    protected void write(NetOutput out) throws IOException {
        this.type.writeMetadataPrimitive(out, value);
    }
}
