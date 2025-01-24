package org.geysermc.mcprotocollib.protocol.data.game.item.component.type;

import io.netty.buffer.ByteBuf;
import lombok.NonNull;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.BooleanComponentType;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponent;

public class BooleanDataComponent extends DataComponent<Boolean, BooleanComponentType> {
    private final Boolean value;

    public BooleanDataComponent(@NonNull BooleanComponentType type, Boolean value) {
        super(type);
        this.value = value;
    }

    @Override
    public Boolean getValue() {
        return this.value;
    }

    @Override
    public void write(ByteBuf out) {
        this.type.writeDataComponentPrimitive(out, this.value);
    }
}
