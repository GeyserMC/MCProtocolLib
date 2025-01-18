package org.geysermc.mcprotocollib.protocol.data.game.item.component.type;

import io.netty.buffer.ByteBuf;
import lombok.NonNull;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponent;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.IntComponentType;


public class IntDataComponent extends DataComponent<Integer, IntComponentType> {
    private final Integer value;

    public IntDataComponent(@NonNull IntComponentType type, Integer value) {
        super(type);
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public void write(ByteBuf out) {
        this.type.writeDataComponentPrimitive(out, this.value);
    }
}
