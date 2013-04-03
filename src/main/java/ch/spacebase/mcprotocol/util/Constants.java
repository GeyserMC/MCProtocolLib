package ch.spacebase.mcprotocol.util;

public class Constants {

	public static final int LAUNCHER_VERSION = 13;
	public static final byte STANDARD_PROTOCOL_VERSION = 60;
	public static final String STANDARD_MINECRAFT_VERSION = "1.5.1";
	public static final byte CLASSIC_PROTOCOL_VERSION = 7;
	public static final byte[] POCKET_MAGIC = new byte[] { 0, -1, -1, 0, -2, -2, -2, -2, -3, -3, -3, -3, 18, 52, 86, 120 };

	public static class AnimationIds {
		public static final byte NONE = 0;
		public static final byte ARM_SWING = 1;
		public static final byte DAMAGE = 2;
		public static final byte LEAVE_BED = 3;
		public static final byte EAT_FOOD = 5;
		public static final byte UNKNOWN = 102;
		public static final byte CROUCH = 104;
		public static final byte UNCROUCH = 105;
	}
	
	public static class ViewDistanceIds {
		public static final byte FAR = 0;
		public static final byte NORMAL = 1;
		public static final byte SHORT = 2;
		public static final byte TINY = 3;
	}
	
	public static class DifficultyIds {
		public static final byte PEACEFUL = 0;
		public static final byte EASY = 1;
		public static final byte NORMAL = 2;
		public static final byte HARD = 3;
	}
	
	public static class ClientStatusIds {
		public static final byte INITIAL_SPAWN = 0;
		public static final byte RESPAWN = 1;
	}
	
	public static class EntityActionIds {
		public static final byte CROUCH = 1;
		public static final byte UNCROUCH = 2;
		public static final byte LEAVE_BED = 3;
		public static final byte START_SPRINTING = 4;
		public static final byte STOP_SPRINTING = 5;
	}
	
	public static class EntityStatusIds {
		public static final byte HURT = 2;
		public static final byte DEAD = 3;
		public static final byte TAMING = 6;
		public static final byte TAMED = 7;
		public static final byte SHAKING_WATER = 8;
		public static final byte EATING_ACCEPT = 9;
		public static final byte SHEEP_EATING = 10;
	}
	
	public static class GameStateIds {
		public static final byte INVALID_BED = 0;
		public static final byte BEGIN_RAIN = 1;
		public static final byte END_RAIN = 2;
		public static final byte CHANGE_GAMEMODE = 3;
		public static final byte ENTER_CREDITS = 4;
	}
	
	public static class GameModeIds {
		public static final byte SURVIVAL = 0;
		public static final byte CREATIVE = 1;
		public static final byte ADVENTURE = 2;
	}
	
	public static class DimensionIds {
		public static final byte NETHER = -1;
		public static final byte NORMAL = 0;
		public static final byte END = 1;
	}
	
	public static class WindowTypeIds {
		public static final byte CHEST = 0;
		public static final byte WORKBENCH = 1;
		public static final byte FURNACE = 2;
		public static final byte DISPENSER = 3;
		public static final byte ENCHANTMENT_TABLE = 4;
		public static final byte BREWING_STAND = 5;
	}
	
	public static class SBObjectiveIds {
		public static final byte CREATE = 0;
		public static final byte REMOVE = 1;
		public static final byte UPDATE_TEXT = 2;
	}
	
	public static class TeamActionIds {
		public static final byte CREATE = 0;
		public static final byte REMOVE = 1;
		public static final byte UPDATE = 2;
		public static final byte ADD_PLAYER = 3;
		public static final byte REMOVE_PLAYER = 4;
	}
	
	public static class FriendlyFireIds {
		public static final byte OFF = 0;
		public static final byte ON = 1;
		public static final byte SEE_INVISIBLE_TEAMMATES = 3;
	}
	
	public static class UpdateSBIds {
		public static final byte CREATE_OR_UPDATE = 0;
		public static final byte REMOVE = 1;
	}
	
	public static class UpdateTileEntityIds {
		public static final byte SET_MOB = 1;
	}
	
	public static class WindowPropertyIds {
		public static final byte FURNACE_SET_PROGRESS = 0;
		public static final byte FURNACE_SET_FUEL = 0;
		public static final byte ENCHANT_SLOT_0 = 0;
		public static final byte ENCHANT_SLOT_1 = 1;
		public static final byte ENCHANT_SLOT_2 = 2;
	}
	
	public static class MobIds {
		public static final byte CREEPER = 50;
		public static final byte SKELETON = 51;
		public static final byte SPIDER = 52;
		public static final byte GIANT_ZOMBIE = 53;
		public static final byte ZOMBIE = 54;
		public static final byte SLIME = 55;
		public static final byte GHAST = 56;
		public static final byte ZOMBIE_PIGMAN = 57;
		public static final byte ENDERMAN = 58;
		public static final byte CAVE_SPIDER = 59;
		public static final byte SILVERFISH = 60;
		public static final byte BLAZE = 61;
		public static final byte MAGMA_CUBE = 62;
		public static final byte ENDER_DRAGON = 63;
		public static final byte WITHER = 64;
		public static final byte BAT = 65;
		public static final byte WITCH = 66;
		public static final byte PIG = 90;
		public static final byte SHEEP = 91;
		public static final byte COW = 92;
		public static final byte CHICKEN = 93;
		public static final byte SQUID = 94;
		public static final byte WOLF = 95;
		public static final byte MOOSHROOM = 96;
		public static final byte SNOWMAN = 97;
		public static final byte OCELOT = 98;
		public static final byte IRON_GOLEM = 99;
		public static final byte VILLAGER = 120;
	}
	
	public static class ObjectIds {
		public static final byte BOAT = 1;
		public static final byte ITEM_STACK = 2;
		public static final byte MINECART = 10;
		public static final byte ACTIVE_TNT = 50;
		public static final byte ENDERCRYSTAL = 51;
		public static final byte ARROW = 60;
		public static final byte SNOWBALL = 61;
		public static final byte EGG = 62;
		public static final byte ENDERPEARL = 65;
		public static final byte WITHER_SKULL = 66;
		public static final byte FALLING_OBJECT = 70;
		public static final byte ITEM_FRAME = 71;
		public static final byte EYE_OF_ENDER = 72;
		public static final byte THROWN_POTION = 73;
		public static final byte FALLING_DRAGON_EGG = 74;
		public static final byte XP_BOTTLE = 75;
		public static final byte FISHING_HOOK = 90;
	}
	
}
