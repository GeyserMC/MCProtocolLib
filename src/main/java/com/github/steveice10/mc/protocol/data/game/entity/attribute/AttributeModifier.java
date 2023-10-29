package com.github.steveice10.mc.protocol.data.game.entity.attribute;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Represents an attribute modifier.
 *
 * @param uuid      Use {@link ModifierType} to determine built-in modifiers.
 * @param amount    The amount of the modifier.
 * @param operation The operation of the modifier.
 */
public record AttributeModifier(@NotNull UUID uuid, double amount, @NotNull ModifierOperation operation) {
}
