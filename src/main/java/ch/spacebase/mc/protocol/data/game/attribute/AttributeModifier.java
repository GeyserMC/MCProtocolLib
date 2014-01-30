package ch.spacebase.mc.protocol.data.game.attribute;

import ch.spacebase.mc.protocol.data.game.values.entity.ModifierOperation;
import ch.spacebase.mc.protocol.data.game.values.entity.ModifierType;

public class AttributeModifier {

	private ModifierType type;
	private double amount;
	private ModifierOperation operation;
	
	public AttributeModifier(ModifierType type, double amount, ModifierOperation operation) {
		this.type = type;
		this.amount = amount;
		this.operation = operation;
	}
	
	public ModifierType getType() {
		return this.type;
	}
	
	public double getAmount() {
		return this.amount;
	}
	
	public ModifierOperation getOperation() {
		return this.operation;
	}
	
}
