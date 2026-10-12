package org.geysermc.mcprotocollib.protocol.data.game.item.component.type;

import io.netty.buffer.ByteBuf;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.BooleanComponentType;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponent;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public class BooleanDataComponent extends DataComponent<Boolean, BooleanComponentType> {
    private final @Nullable Boolean value;

    public BooleanDataComponent(BooleanComponentType type, @Nullable Boolean value) {
        super(type);
        this.value = value;
    }

    @Override
    public @Nullable Boolean getValue() {
        return this.value;
    }

    @Override
    public void write(ByteBuf out) {
        this.type.writeDataComponentPrimitive(out, Objects.requireNonNull(this.value, "value must not be null to write data component"));
    }
}
