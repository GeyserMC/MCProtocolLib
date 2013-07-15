package ch.spacebase.mcprotocol.standard.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains a property value of an entity. Used in PacketEntityProperties.
 */
public class EntityProperty {

	/**
	 * The property name.
	 */
	private String name;
	/**
	 * The property's value.
	 */
	private double value;
	/**
	 * The property's (unknown)s.
	 */
	private List<Unknown> unknowns;
	
	/**
	 * Creates a new entity property instance.
	 * @param name Name of the property.
	 * @param value Property value.
	 */
	public EntityProperty(String name, double value) {
		this(name, value, new ArrayList<Unknown>());
	}
	
	/**
	 * Creates a new entity property instance.
	 * @param name Name of the property.
	 * @param value Property value.
	 * @param unknowns A list of (unknown)s.
	 */
	public EntityProperty(String name, double value, List<Unknown> unknowns) {
		this.name = name;
		this.value = value;
		this.unknowns = unknowns;
	}
	
	/**
	 * Gets the property's name.
	 * @return The property's name.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Gets the property's value.
	 * @return The property's value.
	 */
	public double getValue() {
		return this.value;
	}
	
	/**
	 * Gets the property's (unknown)s.
	 * @return The property's (unknown)s.
	 */
	public List<Unknown> getUnknowns() {
		return this.unknowns;
	}
	
	/**
	 * Adds an (unknown) to the property.
	 * @param u (unknown) to add.
	 */
	public void addUnknown(Unknown u) {
		this.unknowns.add(u);
	}
	
}
