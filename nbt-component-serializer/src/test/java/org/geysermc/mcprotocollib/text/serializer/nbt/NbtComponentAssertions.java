package org.geysermc.mcprotocollib.text.serializer.nbt;

import net.kyori.adventure.text.Component;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The three assertions every test in this package repeats, plus the two serializer instances they
 * are made against.
 */
final class NbtComponentAssertions {

    /**
     * The shared, vanilla matching serializer.
     */
    static final NbtComponentSerializer NBT = NbtComponentSerializer.nbt();

    /**
     * The variant that writes the {@code type} discriminator vanilla's encoder leaves out.
     */
    static final NbtComponentSerializer TYPED = NbtComponentSerializer.builder()
        .emitComponentType(true)
        .build();

    private NbtComponentAssertions() {
    }

    /**
     * Asserts that writing a component and reading the result back gives the component again.
     */
    static void assertRoundTrips(final Component component) {
        final Object tag = NBT.serialize(component);
        assertEquals(component, NBT.deserialize(tag), () -> "round tripped through " + tag);
    }

    /**
     * Asserts that a tag reads as a particular component.
     */
    static void assertReads(final Object tag, final Component expected) {
        assertEquals(expected, NBT.deserialize(tag));
    }

    /**
     * Asserts that a component writes as a particular tag.
     */
    static void assertWrites(final Component component, final Object expected) {
        assertEquals(expected, NBT.serialize(component));
    }
}
