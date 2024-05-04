package org.geysermc.mcprotocollib.protocol.data.game.item.component.type;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.NonNull;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponent;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.IntComponentType;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.ItemCodecByteBuf;


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
    public void write(ItemCodecByteBuf out) {
        this.type.writeDataComponentPrimitive(out, this.value);
    }
}
