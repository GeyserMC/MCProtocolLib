package org.geysermc.adventure.text.serializer.nbt;

import net.kyori.adventure.text.event.DataComponentValue;

/**
 * Holds the raw value of a data component, serialized to NBT.
 *
 * @param tag the value of a data component
 */
public record NbtDataComponentValue(Object tag) implements DataComponentValue {}
