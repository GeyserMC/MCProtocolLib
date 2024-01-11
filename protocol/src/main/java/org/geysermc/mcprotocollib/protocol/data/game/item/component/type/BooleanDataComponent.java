package com.github.steveice10.mc.protocol.data.game.item.component.type;

import com.github.steveice10.mc.protocol.data.game.item.component.BooleanComponentType;
import com.github.steveice10.mc.protocol.data.game.item.component.DataComponent;
import com.github.steveice10.mc.protocol.data.game.item.component.ItemCodecHelper;
import io.netty.buffer.ByteBuf;
import lombok.NonNull;

import java.io.IOException;

public class BooleanDataComponent extends DataComponent<Boolean, BooleanComponentType> {
    private final Boolean value;

    public BooleanDataComponent(@NonNull BooleanComponentType type, Boolean value) {
        super(type);
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
    public void write(ItemCodecHelper helper, ByteBuf out) throws IOException {
        this.type.writeDataComponentPrimitive(out, this.value);
    }
}
