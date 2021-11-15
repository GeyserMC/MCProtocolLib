package com.github.steveice10.mc.protocol.data.game.entity.metadata.type;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.MetadataType;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public class BooleanEntityMetadata extends EntityMetadata<Boolean> {
    private final boolean value;

    public BooleanEntityMetadata(int id, boolean value) {
        super(id, MetadataType.BOOLEAN);
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
    protected void write(NetOutput out) throws IOException {
        out.writeBoolean(this.value);
    }
}
