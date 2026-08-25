package org.geysermc.adventure.text.serializer.nbt;

import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

final class HeterogeneousNbtList {
    private final List<NbtType<?>> types = new ArrayList<>();
    private final List<Object> values = new ArrayList<>();

    public static List<Object> getList(NbtMap map, String key) {
        Object list = map.get(key);
        if (list instanceof NbtList<?> nbtList) {
            return tryUnwrap(nbtList);
        }
        return Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    public static List<Object> tryUnwrap(NbtList<?> list) {
        if (list.getType() != NbtType.COMPOUND) {
            return (List<Object>) list;
        }

        List<NbtMap> maps = (List<NbtMap>) list;
        List<Object> unwrapped = new ArrayList<>();
        for (NbtMap map : maps) {
            unwrapped.add(tryUnwrap(map));
        }
        return unwrapped;
    }

    public void add(Object value) {
        //noinspection unchecked,rawtypes
        add((NbtType) NbtType.byClass(value.getClass()), value);
    }

    public <T> void add(NbtType<T> type, T value) {
        types.add(type);
        values.add(value);
    }

    public NbtList<?> build() {
        return flattenList(getSingleType());
    }

    public static <T> Collector<T, ?, NbtList<?>> collector() {
        return new Collector<T, HeterogeneousNbtList, NbtList<?>>() {
            @Override
            public Supplier<HeterogeneousNbtList> supplier() {
                return HeterogeneousNbtList::new;
            }

            @Override
            public BiConsumer<HeterogeneousNbtList, T> accumulator() {
                return HeterogeneousNbtList::add;
            }

            @Override
            public BinaryOperator<HeterogeneousNbtList> combiner() {
                return (first, second) -> {
                    first.types.addAll(second.types);
                    first.values.addAll(second.values);
                    return first;
                };
            }

            @Override
            public Function<HeterogeneousNbtList, NbtList<?>> finisher() {
                return HeterogeneousNbtList::build;
            }

            @Override
            public Set<Characteristics> characteristics() {
                return Set.of();
            }
        };
    }

    private <E> NbtList<E> flattenList(NbtType<E> singleType) {
        return new NbtList<>(singleType, values.stream().map(value -> wrapIfNeeded(singleType, value)).toList());
    }

    private NbtType<?> getSingleType() {
        NbtType<?> singleType = NbtType.END;

        for (NbtType<?> type : types) {
            if (singleType == NbtType.END) {
                singleType = type;
            } else if (singleType != type) {
                return NbtType.COMPOUND;
            }
        }
        return singleType;
    }

    private static boolean isWrapper(NbtMap map) {
        return map.size() == 1 && map.containsKey("");
    }

    @SuppressWarnings("unchecked")
    private static <T> T wrapIfNeeded(NbtType<T> type, Object object) {
        if (type != NbtType.COMPOUND) {
            return (T) object;
        }
        // type == compound
        return (T) (object instanceof NbtMap map && !isWrapper(map) ? map : wrapElement(object));
    }

    private static NbtMap wrapElement(Object object) {
        return NbtMap.fromMap(Map.of("", object));
    }

    private static Object tryUnwrap(NbtMap map) {
        if (map.size() == 1) {
            Object value = map.get("");
            if (value != null) {
                return value;
            }
        }
        return map;
    }
}
