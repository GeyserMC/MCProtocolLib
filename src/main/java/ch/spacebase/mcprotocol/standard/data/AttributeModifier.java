package ch.spacebase.mcprotocol.standard.data;

import java.util.UUID;

/**
 * A modifier of an entity's attribute.
 */
public class AttributeModifier {

	/**
	 * The modifier's unique id.
	 */
	private UUID uid;
	/**
	 * The modifier's amount.
	 */
	private double amount;
	/**
	 * The modifier's operation.
	 */
	private int operation;
	
	/**
	 * Creates a new modifier.
	 * @param uid Unique id of the modifier.
	 * @param amount Amount of the modifier.
	 * @param operation Operation of the modifier.
	 */
	public AttributeModifier(UUID uid, double amount, int operation) {
		this.uid = uid;
		this.amount = amount;
		this.operation = operation;
	}
	
	/**
	 * Gets the modifier's unique id.
	 * @return The modifier's unique id.
	 */
	public UUID getUID() {
		return this.uid;
	}
	
	/**
	 * Gets the modifier's amount.
	 * @return The modifier's amount.
	 */
	public double getAmount() {
		return this.amount;
	}
	
	/**
	 * Gets the modifier's operation.
	 * @return The modifier's operation.
	 */
	public int getOperation() {
		return this.operation;
	}
	
}
