package ch.spacebase.mc.protocol.data.game;

import java.util.UUID;

public class AttributeModifier {

	private UUID uuid;
	private double amount;
	private int operation;
	
	public AttributeModifier(UUID uuid, double amount, int operation) {
		this.uuid = uuid;
		this.amount = amount;
		this.operation = operation;
	}
	
	public UUID getUUID() {
		return this.uuid;
	}
	
	public double getAmount() {
		return this.amount;
	}
	
	public int getOperation() {
		return this.operation;
	}
	
	public static class Operations {
		public static final int ADD = 0;
		public static final int ADD_MULTIPLIED = 1;
		public static final int MULTIPLY = 2;
	}

	public static class UUIDs {
		public static final UUID CREATURE_FLEE_SPEED_BONUS = UUID.fromString("E199AD21-BA8A-4C53-8D13-6182D5C69D3A");
		public static final UUID ENDERMAN_ATTACK_SPEED_BOOST = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
		public static final UUID SPRINT_SPEED_BOOST = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
		public static final UUID PIGZOMBIE_ATTACK_SPEED_BOOST = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
		public static final UUID WITCH_DRINKING_SPEED_PENALTY = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
		public static final UUID ZOMBIE_BABY_SPEED_BOOST = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
		public static final UUID ITEM_MODIFIER = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
		public static final UUID SPEED_POTION_MODIFIER = UUID.fromString("91AEAA56-376B-4498-935B-2F7F68070635");
		public static final UUID HEALTH_BOOST_POTION_MODIFIER = UUID.fromString("5D6F0BA2-1186-46AC-B896-C61C5CEE99CC");
		public static final UUID SLOW_POTION_MODIFIER = UUID.fromString("7107DE5E-7CE8-4030-940E-514C1F160890");
		public static final UUID STRENGTH_POTION_MODIFIER = UUID.fromString("648D7064-6A60-4F59-8ABE-C2C23A6DD7A9");
		public static final UUID WEAKNESS_POTION_MODIFIER = UUID.fromString("22653B89-116E-49DC-9B6B-9971489B5BE5");
	}
	
}
