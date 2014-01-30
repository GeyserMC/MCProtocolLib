package ch.spacebase.mc.protocol.data.game.attribute;

import java.util.ArrayList;
import java.util.List;

import ch.spacebase.mc.protocol.data.game.values.entity.AttributeType;

public class Attribute {
	
	private AttributeType type;
	private double value;
	private List<AttributeModifier> modifiers;
	
	public Attribute(AttributeType type) {
		this(type, type.getDefault());
	}
	
	public Attribute(AttributeType type, double value) {
		this(type, value, new ArrayList<AttributeModifier>());
	}
	
	public Attribute(AttributeType type, double value, List<AttributeModifier> modifiers) {
		this.type = type;
		this.value = value;
		this.modifiers = modifiers;
	}
	
	public AttributeType getType() {
		return this.type;
	}
	
	public double getValue() {
		return this.value;
	}
	
	public List<AttributeModifier> getModifiers() {
		return new ArrayList<AttributeModifier>(this.modifiers);
	}
	
}
