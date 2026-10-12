package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.type.ObjectDataComponent;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@Data
@AllArgsConstructor
public abstract class DataComponent<V, T extends DataComponentType<V>> {
    protected final T type;

    /**
     * May be null depending on type
     */
    public abstract @Nullable V getValue();

    /**
     * Overridden for primitive classes. This write method still checks for these primitives in the event
     * they are manually created using {@link ObjectDataComponent}.
     */
    public void write(ByteBuf out) {
        this.type.writeDataComponent(out, Objects.requireNonNull(this.getValue(), "value must not be null to write data component"));
    }

    @Override
    public String toString() {
        return "DataComponent(type=" + type + ", value=" + getValue() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataComponent<?, ?> that)) {
            return false;
        }
        return this.type == that.type && Objects.equals(this.getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, getValue());
    }
}
