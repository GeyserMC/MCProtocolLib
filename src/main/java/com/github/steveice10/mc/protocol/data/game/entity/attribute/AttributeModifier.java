package com.github.steveice10.mc.protocol.data.game.entity.attribute;

import lombok.NonNull;

import java.util.UUID;

/**
 * Represents an attribute modifier.
 *
 * @param uuid      Use {@link ModifierType} to determine built-in modifiers.
 * @param amount    The amount of the modifier.
 * @param operation The operation of the modifier.
 */
public record AttributeModifier(@NonNull UUID uuid, double amount, @NonNull ModifierOperation operation) {
}
