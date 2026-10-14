package org.geysermc.mcprotocollib.protocol.data.game.item.component.type;

import io.netty.buffer.ByteBuf;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponent;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.IntComponentType;
import org.jspecify.annotations.Nullable;

import java.util.Objects;


public class IntDataComponent extends DataComponent<Integer, IntComponentType> {
    private final @Nullable Integer value;

    public IntDataComponent(IntComponentType type, @Nullable Integer value) {
        super(type);
        this.value = value;
    }

    @Override
    public @Nullable Integer getValue() {
        return this.value;
    }

    @Override
    public void write(ByteBuf out) {
        this.type.writeDataComponentPrimitive(out, Objects.requireNonNull(this.value, "value must not be null to write data component"));
    }
}
