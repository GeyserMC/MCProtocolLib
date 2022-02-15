package com.github.steveice10.mc.protocol.data.game.entity.metadata;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.type.ObjectEntityMetadata;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.io.IOException;
import java.util.*;

@Data
@AllArgsConstructor
public abstract class EntityMetadata<V, T extends MetadataType<V>> {
    protected final int id;
    protected final @NonNull T type;

    /**
     * May be null depending on type
     */
    public abstract V getValue();

    public static EntityMetadata<?, ?>[] read(NetInput in) throws IOException {
        List<EntityMetadata<?, ?>> ret = new ArrayList<>();
        int id;
        while ((id = in.readUnsignedByte()) != 255) {
            MetadataType<?> type = MetadataType.read(in);
            ret.add(type.readMetadata(in, id));
        }

        return ret.toArray(new EntityMetadata[0]);
    }

    /**
     * Overridden for primitive classes. This write method still checks for these primitives in the event
     * they are manually created using {@link ObjectEntityMetadata}.
     */
    protected void write(NetOutput out) throws IOException {
        this.type.writeMetadata(out, this.getValue());
    }

    public static void write(NetOutput out, EntityMetadata<?, ?>[] metadata) throws IOException {
        for (EntityMetadata<?, ?> meta : metadata) {
            out.writeByte(meta.getId());
            MetadataType.write(out, meta.getType());
            meta.write(out);
        }

        out.writeByte(255);
    }

    @Override
    public String toString() {
        return "EntityMetadata(id=" + id + ", type=" + type + ", value=" + getValue().toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EntityMetadata)) {
            return false;
        }
        EntityMetadata<?, ?> that = (EntityMetadata<?, ?>) o;
        return this.id == that.id && this.type == that.type && Objects.equals(this.getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, getValue());
    }
}
