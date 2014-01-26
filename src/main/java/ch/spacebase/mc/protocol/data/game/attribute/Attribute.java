package ch.spacebase.mc.protocol.data.game.attribute;

import java.util.ArrayList;
import java.util.List;

public class Attribute {
	
	private Type type;
	private double value;
	private List<AttributeModifier> modifiers;
	
	public Attribute(Type type) {
		this(type, type.getDefault());
	}
	
	public Attribute(Type type, double value) {
		this(type, value, new ArrayList<AttributeModifier>());
	}
	
	public Attribute(Type type, double value, List<AttributeModifier> modifiers) {
		this.type = type;
		this.value = value;
		this.modifiers = modifiers;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public double getValue() {
		return this.value;
	}
	
	public List<AttributeModifier> getModifiers() {
		return new ArrayList<AttributeModifier>(this.modifiers);
	}
	
	public static enum Type {
		MAX_HEALTH("generic.maxHealth", 20, 0, Double.MAX_VALUE),
		FOLLOW_RANGE("generic.followRange", 32, 0, 2048),
		KNOCKBACK_RESISTANCE("generic.knockbackResistance", 0, 0, 1),
		MOVEMENT_SPEED("generic.movementSpeed", 0.699999988079071, 0, Double.MAX_VALUE),
		ATTACK_DAMAGE("generic.attackStrength", 2, 0, Double.MAX_VALUE),
		HORSE_JUMP_STRENGTH("generic.maxHealth", 0.7, 0, 2),
		ZOMBIE_SPAWN_REINFORCEMENTS_CHANCE("generic.maxHealth", 0, 0, 1);
		
		private String key;
		private double def;
		private double min;
		private double max;
		
		private Type(String key, double def, double min, double max) {
			this.key = key;
			this.def = def;
			this.min = min;
			this.max = max;
		}
		
		public String getKey() {
			return this.key;
		}
		
		public double getDefault() {
			return this.def;
		}
		
		public double getMin() {
			return this.min;
		}
		
		public double getMax() {
			return this.max;
		}
		
		public static Type fromKey(String key) {
			for(Type type : values()) {
				if(type.getKey().equals(key)) {
					return type;
				}
			}
			
			return null;
		}
	}
	
}
