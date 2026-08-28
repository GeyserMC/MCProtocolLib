package org.geysermc.adventure.text.serializer.nbt;

import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.FieldSource;

import java.util.List;

public class HeterogeneousNbtListTest {
    static final List<List<Object>> HETEROGENEOUS_LISTS = List.of(
        List.of(5, 4.12F, 4.0, 5000L),
        List.of("1", 5, -6L),
        List.of(NbtMap.builder().putInt("test", 5).build(), 6.3),
        List.of("21325", new NbtList<>(NbtType.DOUBLE, List.of(5.0, 2.1, 3.5)), -4L,
            NbtMap.builder().putLong("213", 25L).build())
    );
    static final List<List<Object>> HOMOGENEOUS_LISTS = List.of(
        List.of(1, 2, 5, 4, 3),
        List.of(2L, 1L, 3L, 2L, 6L),
        List.of("hello", "this", "is", "a", "pretty", "gay", "list"),
        List.of(NbtMap.builder().putInt("1", 2).build(), NbtMap.builder().putLong("5", 4L).build()),
        List.of(new NbtList<>(NbtType.INT, List.of(4, 3, 2, 6, 1)), new NbtList<>(NbtType.FLOAT, List.of(1.3F, 5.4F, -2.3F))),
        List.of()
    );

    @ParameterizedTest
    @FieldSource("HETEROGENEOUS_LISTS")
    void testHomogeneousConversion(List<Object> values) {
        // First, use the class to wrap the heterogeneous list into a homogeneous NBT list of maps
        HeterogeneousNbtList nbtList = new HeterogeneousNbtList();
        values.forEach(nbtList::add);
        NbtList<?> wrappedList = nbtList.build();

        Assertions.assertEquals(NbtType.COMPOUND, wrappedList.getType(), "type of heterogeneous NBT list must be COMPOUND");

        // Check that each individual value has been wrapped correctly
        for (int i = 0; i < wrappedList.size(); i++) {
            Object originalValue = values.get(i);
            NbtMap wrappedValue = (NbtMap) wrappedList.get(i);

            if (originalValue instanceof NbtMap) {
                Assertions.assertEquals(originalValue, wrappedValue, "NBT maps in heterogeneous NBT lists must not be wrapped");
            } else {
                Assertions.assertEquals(1, wrappedValue.size(), "wrapped values in heterogeneous NBT lists must have exactly 1 key");
                Assertions.assertEquals(originalValue, wrappedValue.get(""), "wrapped values in heterogeneous NBT lists must be under the '' key");
            }
        }

        // Finally, check unwrapping too, just to be nice
        List<Object> unwrapped = HeterogeneousNbtList.tryUnwrap(wrappedList);
        Assertions.assertEquals(values, unwrapped, "unwrapped heterogeneous NBT list must equal source");
    }

    @ParameterizedTest
    @FieldSource("HOMOGENEOUS_LISTS")
    void testUnwrappedHomogeneousConversion(List<Object> values) {
        // First, use the class to try to "wrap" the heterogeneous list
        // This should essentially be the same as the original list, given the original already was homogeneous
        HeterogeneousNbtList nbtList = new HeterogeneousNbtList();
        values.forEach(nbtList::add);
        NbtList<?> wrappedList = nbtList.build();

        if (values.isEmpty()) {
            Assertions.assertEquals(NbtType.END, wrappedList.getType(), "type of empty homogeneous NBT list must be END");
        } else {
            Assertions.assertEquals(NbtType.byClass(values.getFirst().getClass()), wrappedList.getType(), "type of converted homogeneous NBT list must be same as original");
        }
        Assertions.assertEquals(values, wrappedList, "converted homogeneous NBT list must be same as original");
        if (wrappedList.getType() == NbtType.COMPOUND) {
            // tryUnwrap will try to unwrap the compounds and construct a new list
            Assertions.assertEquals(wrappedList, HeterogeneousNbtList.tryUnwrap(wrappedList), "unwrapping an already homogeneous NBT list must return the same list");
        } else {
            Assertions.assertSame(wrappedList, HeterogeneousNbtList.tryUnwrap(wrappedList), "unwrapping an already homogeneous NBT list must return the same list");
        }
    }

    @Test
    void testShorthandMethods() {
        Assertions.assertSame(NbtList.EMPTY, HeterogeneousNbtList.of(), "HeterogeneousNbtList#of must always return NbtList.EMPTY for empty lists");
        Assertions.assertEquals(List.of(1, "2", 4.5), HeterogeneousNbtList.tryUnwrap(HeterogeneousNbtList.of(1, "2", 4.5)));
    }
}
