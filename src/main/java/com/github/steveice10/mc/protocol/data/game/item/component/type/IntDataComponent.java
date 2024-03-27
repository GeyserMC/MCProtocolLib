package com.github.steveice10.mc.protocol.data.game.item.component.type;

import com.github.steveice10.mc.protocol.data.game.item.component.DataComponent;
import com.github.steveice10.mc.protocol.data.game.item.component.IntComponentType;
import com.github.steveice10.mc.protocol.data.game.item.component.ItemCodecHelper;
import io.netty.buffer.ByteBuf;
import lombok.NonNull;

import java.io.IOException;

public class IntDataComponent extends DataComponent<Integer, IntComponentType> {
    private final Integer value;

    public IntDataComponent(@NonNull IntComponentType type, Integer value) {
        super(type);
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
    public void write(ItemCodecHelper helper, ByteBuf out) throws IOException {
        this.type.writeDataComponentPrimitive(helper, out, this.value);
    }
}
