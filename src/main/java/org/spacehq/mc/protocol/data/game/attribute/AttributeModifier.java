package org.spacehq.mc.protocol.data.game.attribute;

import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.entity.ModifierOperation;
import org.spacehq.mc.protocol.data.game.values.entity.ModifierType;

import java.util.UUID;

public class AttributeModifier {

	private ModifierType type;
	private UUID uuid;
	private double amount;
	private ModifierOperation operation;

	public AttributeModifier(ModifierType type, double amount, ModifierOperation operation) {
		this.type = type;
		this.uuid = MagicValues.value(UUID.class, type);
		this.amount = amount;
		this.operation = operation;
	}

	public AttributeModifier(UUID uuid, double amount, ModifierOperation operation) {
		this.type = MagicValues.key(ModifierType.class, uuid);
		this.uuid = uuid;
		this.amount = amount;
		this.operation = operation;
	}

	public ModifierType getType() {
		return this.type;
	}

	public UUID getUUID() {
		return this.uuid;
	}

	public double getAmount() {
		return this.amount;
	}

	public ModifierOperation getOperation() {
		return this.operation;
	}

}
