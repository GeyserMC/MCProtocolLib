package org.geysermc.mcprotocollib.protocol.data.game.item.component;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.type.ObjectDataComponent;

import java.util.Objects;

@Data
@AllArgsConstructor
public abstract class DataComponent<V, T extends DataComponentType<V>> {
    protected final @NonNull T type;

    /**
     * May be null depending on type
     */
    public abstract V getValue();

    /**
     * Overridden for primitive classes. This write method still checks for these primitives in the event
     * they are manually created using {@link ObjectDataComponent}.
     */
    public void write(ByteBuf out) {
        this.type.writeDataComponent(out, this.getValue());
    }

    @Override
    public String toString() {
        return "DataComponent(type=" + type + ", value=" + getValue().toString() + ")";
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
