package org.geysermc.mcprotocollib.protocol.data.game.item.component.type;

import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponent;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.IntComponentType;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.ItemCodecHelper;
import io.netty.buffer.ByteBuf;
import lombok.NonNull;


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
    public void write(ItemCodecHelper helper, ByteBuf out) {
        this.type.writeDataComponentPrimitive(helper, out, this.value);
    }
}
