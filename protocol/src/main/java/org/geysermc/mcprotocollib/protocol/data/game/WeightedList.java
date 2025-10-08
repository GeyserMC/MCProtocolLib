package org.geysermc.mcprotocollib.protocol.data.game;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.checker.index.qual.NonNegative;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Data
@AllArgsConstructor
public class WeightedList<T> {
    private final List<Entry<T>> entries;

    public WeightedList(ByteBuf in, Function<ByteBuf, T> reader) {
        entries = MinecraftTypes.readList(in, input -> new Entry<>(input, reader));
    }

    public void write(ByteBuf out, BiConsumer<T, ByteBuf> writer) {
        MinecraftTypes.writeList(out, entries, (output, entry) -> entry.write(out, writer));
    }

    public WeightedList<T> with(T value, @NonNegative int weight) {
        add(value, weight);
        return this;
    }

    public void add(T value, @NonNegative int weight) {
        add(new Entry<>(value, weight));
    }

    public void add(Entry<T> entry) {
        entries.add(entry);
    }

    public Entry<T> get(int index) {
        return entries.get(index);
    }

    public boolean contains(T value) {
        for (Entry<T> entry : entries) {
            if (entry.value.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public record Entry<T>(T value, @NonNegative int weight) {

        public Entry(ByteBuf in, Function<ByteBuf, T> reader) {
            this(reader.apply(in), MinecraftTypes.readVarInt(in));
        }

        public void write(ByteBuf out, BiConsumer<T, ByteBuf> writer) {
            writer.accept(value, out);
            MinecraftTypes.writeVarInt(out, weight);
        }
    }
}
