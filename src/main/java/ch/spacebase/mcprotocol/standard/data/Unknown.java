package ch.spacebase.mcprotocol.standard.data;

import java.util.UUID;

/**
 * An (unknown), used in an entity's properties.
 */
public class Unknown {

	/**
	 * The (unknown)'s unique id.
	 */
	private UUID uid;
	/**
	 * The (unknown)'s amount.
	 */
	private double amount;
	/**
	 * The (unknown)'s operation.
	 */
	private byte operation;
	
	/**
	 * Creates a new (unknown).
	 * @param uid Unique id of the (unknown).
	 * @param amount Amount of the (unknown).
	 * @param operation Operation of the (unknown).
	 */
	public Unknown(UUID uid, double amount, byte operation) {
		this.uid = uid;
		this.amount = amount;
		this.operation = operation;
	}
	
	/**
	 * Gets the (unknown)'s unique id.
	 * @return The (unknown)'s unique id.
	 */
	public UUID getUID() {
		return this.uid;
	}
	
	/**
	 * Gets the (unknown)'s amount.
	 * @return The (unknown)'s amount.
	 */
	public double getAmount() {
		return this.amount;
	}
	
	/**
	 * Gets the (unknown)'s operation.
	 * @return The (unknown)'s operation.
	 */
	public byte getOperation() {
		return this.operation;
	}
	
}
