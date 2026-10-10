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
import java.util.stream.Stream;

/**
 * A wrapper around a heterogeneous NBT list.
 *
 * <p>The NBT format does not support heterogeneous NBT lists, however Java Edition's codecs do. This poses a problem when Java Edition wants to save different types in
 * the same NBT list: for example {@code ["some text", {text: " some more text", color: red}]} would normally not be possible. As a solution, Java Edition, when
 * saving heterogeneous NBT lists, wraps all non-compound types in a compound tag with a single, empty-string key.</p>
 *
 * <p>This class exists to construct and parse heterogeneous NBT lists. To write heterogeneous NBT lists (i.e., convert a list consisting of one or more NBT types into a list consisting of one),
 * create a new instance of this class, add objects using {@link HeterogeneousNbtList#add(Object)} or {@link HeterogeneousNbtList#add(NbtType, Object)}, and finally,
 * use {@link HeterogeneousNbtList#build()} to convert the heterogenous NBT list to a homogeneous one. Alternatively, you can use the {@link HeterogeneousNbtList#of(Object...)} shorthand method,
 * or {@link HeterogeneousNbtList#collector()} to collect a stream of NBT tags.</p>
 *
 * <p>To parse heterogeneous NBT lists, use {@link HeterogeneousNbtList#tryUnwrap(NbtList)}, which will take any NBT list and try to unwrap it, if it is a heterogeneous NBT list (if it isn't, the
 * input is returned unmodified). Alternatively, you can use {@link HeterogeneousNbtList#getList(NbtMap, String)} to easily get a heterogeneous NBT list from an {@link NbtMap}.</p>
 *
 * @see <a href="https://mcsrc.dev/2/26.3/net/minecraft/nbt/ListTag#L155-215">Java Edition's implementation</a>
 */
public final class HeterogeneousNbtList {
    private final List<NbtType<?>> types = new ArrayList<>();
    private final List<Object> values = new ArrayList<>();

    /**
     * Shorthand method for creating an empty heterogeneous NBT list. This essentially returns {@link NbtList#EMPTY}.
     *
     * @return an empty heterogeneous NBT list
     * @see HeterogeneousNbtList#of(Object...)
     */
    public static NbtList<?> of() {
        return NbtList.EMPTY;
    }

    /**
     * Constructs a heterogeneous NBT list from the given array of NBT tags.
     *
     * @param values the NBT tags to construct a heterogeneous NBT list of
     * @return the constructed heterogeneous NBT list
     */
    public static NbtList<?> of(Object... values) {
        HeterogeneousNbtList list = new HeterogeneousNbtList();
        for (Object tag : values) {
            list.add(tag);
        }
        return list.build();
    }

    /**
     * Attempts to parse a heterogeneous NBT list from the given {@link NbtMap}, using the given {@code key}.
     *
     * <p>If {@code key} is not present in the {@link NbtMap}, an empty list is returned.</p>
     *
     * @param map the {@link NbtMap} to fetch the {@code key} from
     * @param key the {@code key}
     * @return the parsed heterogeneous NBT list, if it was present, or an empty list if it was not
     * @see HeterogeneousNbtList#tryUnwrap(NbtList)
     */
    public static List<Object> getList(NbtMap map, String key) {
        Object list = map.get(key);
        if (list instanceof NbtList<?> nbtList) {
            return tryUnwrap(nbtList);
        }
        return Collections.emptyList();
    }

    /**
     * Tries to unwrap (parse) the given heterogeneous NBT list.
     *
     * <p>If the given list is not a heterogeneous NBT list, it is returned unmodified.</p>
     *
     * @param list the NBT list to unwrap
     * @return the unwrapped heterogeneous NBT list, or the input if it was a homogeneous NBT list
     * @see HeterogeneousNbtList#getList(NbtMap, String)
     */
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

    /**
     * Adds the given NBT tag to this heterogeneous NBT list.
     *
     * <p>This method uses {@link NbtType#byClass(Class)} to get the {@link NbtType} of the given tag. If it was not an NBT tag, an {@link IllegalArgumentException} is thrown.</p>
     *
     * @param value the NBT tag to add to this heterogeneous NBT list
     * @throws IllegalArgumentException when the given value was not an NBT tag
     * @see HeterogeneousNbtList#add(NbtType, Object)
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void add(Object value) {
        add((NbtType) NbtType.byClass(value.getClass()), value);
    }

    /**
     * Adds the given NBT tag to this heterogeneous NBT list.
     *
     * @param type the {@link NbtType} of the value
     * @param value the NBT tag to add to this heterogeneous NBT list
     * @see HeterogeneousNbtList#add(Object)
     */
    public <T> void add(NbtType<T> type, T value) {
        types.add(type);
        values.add(value);
    }

    /**
     * Flattens this heterogeneous NBT list into a homogeneous one.
     *
     * @return the flattened heterogeneous NBT list
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public NbtList<?> build() {
        NbtType<?> singleType = getSingleType();
        if (singleType != NbtType.COMPOUND) {
            // Java stuff
            return new NbtList(singleType, values);
        }
        // Java stuff
        return new NbtList(NbtType.COMPOUND, values.stream().map(HeterogeneousNbtList::wrapIfNeeded).toArray());
    }

    /**
     * Creates a {@link Collector} to use with {@link Stream#collect(Collector)} to construct a homogeneous NBT list from a heterogeneous stream of NBT tags.
     *
     * @param <T> the type of the {@link Collector}, usually {@link Object} for heterogeneous NBT lists
     * @return the {@link Collector}
     */
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

    private static NbtMap wrapIfNeeded(Object object) {
        // type == compound
        return object instanceof NbtMap map && !isWrapper(map) ? map : wrapElement(object);
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
