package ch.spacebase.mc.protocol.data.game;

import java.util.ArrayList;
import java.util.List;

public class Attribute {
	
	private String key;
	private double value;
	private List<AttributeModifier> modifiers;
	
	public Attribute(String key, double value) {
		this(key, value, new ArrayList<AttributeModifier>());
	}
	
	public Attribute(String key, double value, List<AttributeModifier> modifiers) {
		this.key = key;
		this.value = value;
		this.modifiers = modifiers;
	}
	
	public String getKey() {
		return this.key;
	}
	
	public double getValue() {
		return this.value;
	}
	
	public List<AttributeModifier> getModifiers() {
		return this.modifiers;
	}
	
}
