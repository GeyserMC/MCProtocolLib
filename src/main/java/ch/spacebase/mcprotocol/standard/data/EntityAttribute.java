package ch.spacebase.mcprotocol.standard.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains a attribute of an entity.
 */
public class EntityAttribute {

	/**
	 * The attribute's name.
	 */
	private String name;
	/**
	 * The attribute's value.
	 */
	private double value;
	/**
	 * The attribute's modifiers.
	 */
	private List<AttributeModifier> modifiers;
	
	/**
	 * Creates a new entity attribute instance.
	 * @param name Name of the attribute.
	 * @param value Attribute value.
	 */
	public EntityAttribute(String name, double value) {
		this(name, value, new ArrayList<AttributeModifier>());
	}
	
	/**
	 * Creates a new entity attribute instance.
	 * @param name Name of the attribute.
	 * @param value Attribute value.
	 * @param modifiers A list of modifiers.
	 */
	public EntityAttribute(String name, double value, List<AttributeModifier> modifiers) {
		this.name = name;
		this.value = value;
		this.modifiers = modifiers;
	}
	
	/**
	 * Gets the attribute's name.
	 * @return The attribute's name.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Gets the attribute's value.
	 * @return The attribute's value.
	 */
	public double getValue() {
		return this.value;
	}
	
	/**
	 * Gets the attribute's modifiers.
	 * @return The attribute's modifiers.
	 */
	public List<AttributeModifier> getModifiers() {
		return this.modifiers;
	}
	
	/**
	 * Adds a modifier to the attribute.
	 * @param mod Modifier to add.
	 */
	public void addModifier(AttributeModifier mod) {
		this.modifiers.add(mod);
	}
	
}
