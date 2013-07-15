package ch.spacebase.mcprotocol.util;

import java.util.UUID;

/**
 * Contains constant values relating to protocol.
 */
public class Constants {

	/**
	 * The current Minecraft launcher version.
	 */
	public static final int LAUNCHER_VERSION = 13;

	/**
	 * Contains constant values relating to standard Minecraft protocol.
	 */
	public static class StandardProtocol {
		/**
		 * The current protocol version.
		 */
		public static final byte PROTOCOL_VERSION = 74;
		
		/**
		 * The current game version.
		 */
		public static final String MINECRAFT_VERSION = "1.6.2";
		
		/**
		 * The server list ping magic value.
		 */
		public static final byte PING_MAGIC = 1;
	
		/**
		 * Contains animation ids.
		 */
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
		
		/**
		 * Contains view distance ids.
		 */
		public static class ViewDistanceIds {
			public static final byte FAR = 0;
			public static final byte NORMAL = 1;
			public static final byte SHORT = 2;
			public static final byte TINY = 3;
		}
		
		/**
		 * Contains difficulty ids.
		 */
		public static class DifficultyIds {
			public static final byte PEACEFUL = 0;
			public static final byte EASY = 1;
			public static final byte NORMAL = 2;
			public static final byte HARD = 3;
		}
		
		/**
		 * Contains client status ids.
		 */
		public static class ClientStatusIds {
			public static final byte INITIAL_SPAWN = 0;
			public static final byte RESPAWN = 1;
		}
		
		/**
		 * Contains entity action ids.
		 */
		public static class EntityActionIds {
			public static final byte CROUCH = 1;
			public static final byte UNCROUCH = 2;
			public static final byte LEAVE_BED = 3;
			public static final byte START_SPRINTING = 4;
			public static final byte STOP_SPRINTING = 5;
		}
		
		/**
		 * Contains entity status ids.
		 */
		public static class EntityStatusIds {
			public static final byte HURT = 2;
			public static final byte DEAD = 3;
			public static final byte TAMING = 6;
			public static final byte TAMED = 7;
			public static final byte SHAKING_WATER = 8;
			public static final byte EATING_ACCEPT = 9;
			public static final byte SHEEP_EATING = 10;
		}
		
		/**
		 * Contains game state ids.
		 */
		public static class GameStateIds {
			public static final byte INVALID_BED = 0;
			public static final byte BEGIN_RAIN = 1;
			public static final byte END_RAIN = 2;
			public static final byte CHANGE_GAMEMODE = 3;
			public static final byte ENTER_CREDITS = 4;
		}
		
		/**
		 * Contains game mode ids.
		 */
		public static class GameModeIds {
			public static final byte SURVIVAL = 0;
			public static final byte CREATIVE = 1;
			public static final byte ADVENTURE = 2;
		}
		
		/**
		 * Contains dimension ids.
		 */
		public static class DimensionIds {
			public static final byte NETHER = -1;
			public static final byte NORMAL = 0;
			public static final byte END = 1;
		}
		
		/**
		 * Contains window type ids.
		 */
		public static class WindowTypeIds {
			public static final byte CHEST = 0;
			public static final byte WORKBENCH = 1;
			public static final byte FURNACE = 2;
			public static final byte DISPENSER = 3;
			public static final byte ENCHANTMENT_TABLE = 4;
			public static final byte BREWING_STAND = 5;
			public static final byte NPC_TRADE = 6;
			public static final byte BEACON = 7;
			public static final byte ANVIL = 8;
			public static final byte HOPPER = 9;
			public static final byte DROPPER = 10;
			public static final byte HORSE = 11;
		}
		
		/**
		 * Contains scoreboard objective ids.
		 */
		public static class SBObjectiveIds {
			public static final byte CREATE = 0;
			public static final byte REMOVE = 1;
			public static final byte UPDATE_TEXT = 2;
		}
		
		/**
		 * Contains team action ids.
		 */
		public static class TeamActionIds {
			public static final byte CREATE = 0;
			public static final byte REMOVE = 1;
			public static final byte UPDATE = 2;
			public static final byte ADD_PLAYER = 3;
			public static final byte REMOVE_PLAYER = 4;
		}
		
		/**
		 * Contains friendly fire ids.
		 */
		public static class FriendlyFireIds {
			public static final byte OFF = 0;
			public static final byte ON = 1;
			public static final byte SEE_INVISIBLE_TEAMMATES = 3;
		}
		
		/**
		 * Contains ids for updating scoreboards.
		 */
		public static class UpdateSBIds {
			public static final byte CREATE_OR_UPDATE = 0;
			public static final byte REMOVE = 1;
		}
		
		/**
		 * Contains ids for updating tile entities.
		 */
		public static class UpdateTileEntityIds {
			public static final byte SET_MOB = 1;
		}
		
		/**
		 * Contains window property ids.
		 */
		public static class WindowPropertyIds {
			public static final byte FURNACE_SET_PROGRESS = 0;
			public static final byte FURNACE_SET_FUEL = 0;
			public static final byte ENCHANT_SLOT_0 = 0;
			public static final byte ENCHANT_SLOT_1 = 1;
			public static final byte ENCHANT_SLOT_2 = 2;
		}
		
		/**
		 * Contains mob ids.
		 */
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
		
		/**
		 * Contains object ids.
		 */
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
		
		/**
		 * Contains standard plugin channels.
		 */
		public static class PluginChannels {
			public static final String REGISTER = "REGISTER";
			public static final String UNREGISTER = "UNREGISTER";
			public static final String COMMAND_BLOCK = "MC|AdvCdm";
			public static final String BEACON = "MC|Beacon";
			public static final String EDIT_BOOK = "MC|BEdit";
			public static final String SIGN_BOOK = "MC|BSign";
			public static final String ANVIL_NAME = "MC|ItemName";
			public static final String SERVER_TEXTURE_PACK = "MC|TPack";
			public static final String NPC_TRADES = "MC|TrList";
			public static final String SELECT_TRADE = "MC|TrSel";
			public static final String CLIENT_PING_DATA = "MC|PingHost";
			public static final String BRAND = "MC|Brand";
		}
		
		/**
		 * Contains entity attribute ids.
		 */
		public static class EntityAttributes {
			public static final String MAX_HEALTH = "generic.maxHealth";
			public static final String FOLLOW_RANGE = "generic.followRange";
			public static final String KNOCKBACK_RESISTANCE = "generic.knockbackResistance";
			public static final String MOVEMENT_SPEED = "generic.movementSpeed";
			public static final String ATTACK_DAMAGE = "generic.attackDamage";
			public static final String HORSE_JUMP_STRENGTH = "horse.jumpStrength";
			public static final String ZOMBIE_SPAWN_REINFORCEMENTS = "zombie.spawnReinforcements";
		}
		
		/**
		 * Contains entity attribute modifier operations.
		 */
		public static class ModifierOperations {
			public static final int ADD = 0;
			public static final int ADD_MULTIPLIED = 1;
			public static final int MULTIPLY = 2;
		}
		
		/**
		 * Contains entity attribute modifier unique ids.
		 */
		public static class ModifierUIDs {
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
		
		/**
		 * Contains statistic ids.
		 */
		public static class Statistics {
			public static final int START_GAME = 1000;
			public static final int CREATE_WORLD = 1001;
			public static final int LOAD_WORLD = 1002;
			public static final int JOIN_MULTIPLAYER = 1003;
			public static final int LEAVE_GAME = 1004;
			public static final int PLAY_ONE_MINUTE = 1100;
			public static final int WALK_ONE_CM = 2000;
			public static final int SWIM_ONE_CM = 2001;
			public static final int FALL_ONE_CM = 2002;
			public static final int CLIMB_ONE_CM = 2003;
			public static final int FLY_ONE_CM = 2004;
			public static final int DIVE_ONE_CM = 2005;
			public static final int MINECART_ONE_CM = 2006;
			public static final int BOAT_ONE_CM = 2007;
			public static final int PIG_ONE_CM = 2008;
			public static final int JUMP = 2010;
			public static final int DROP = 2011;
			public static final int DAMAGE_DEALT = 2020;
			public static final int DAMAGE_TAKEN = 2021;
			public static final int DEATHS = 2022;
			public static final int MOB_KILLS = 2023;
			public static final int PLAYER_KILLS = 2024;
			public static final int FISH_CAUGHT = 2025;
			
			private static final int MINE_BLOCK_BASE = 16777216;
			private static final int CRAFT_ITEM_BASE = 16842752;
			private static final int USE_ITEM_BASE = 16908288;
			private static final int BREAK_ITEM_BASE = 16973824;
			
			/**
			 * Gets the statistic value for mining a block.
			 * @param block Block mined.
			 * @return Statistic value for the block.
			 */
			public static int mineBlock(int block) {
				return MINE_BLOCK_BASE + block;
			}
			
			/**
			 * Gets the statistic value for crafting an item.
			 * @param item Item crafted.
			 * @return Statistic value for the item.
			 */
			public static int craftItem(int item) {
				return CRAFT_ITEM_BASE + item;
			}
			
			/**
			 * Gets the statistic value for using an item.
			 * @param item Item used.
			 * @return Statistic value for the item.
			 */
			public static int useItem(int item) {
				return USE_ITEM_BASE + item;
			}

			/**
			 * Gets the statistic value for breaking an item.
			 * @param item Item broken.
			 * @return Statistic value for the item.
			 */
			public static int breakItem(int item) {
				return BREAK_ITEM_BASE + item;
			}
		}
		
		/**
		 * Contains watchable object ids.
		 */
		public static class WatchableObjectIds {
			public static final byte BYTE = 0;
			public static final byte SHORT = 1;
			public static final byte INT = 2;
			public static final byte FLOAT = 3;
			public static final byte STRING = 4;
			public static final byte ITEM_STACK = 5;
			public static final byte COORDINATES = 6;
		}
		
		/**
		 * Contains tile editor ids.
		 */
		public static class TileEditorIds {
			public static final byte SIGN = 0;
		}
	}
	
	/**
	 * Contains constant values relating to classic Minecraft protocol.
	 */
	public static class ClassicProtocol {
		/**
		 * The current protocol version.
		 */
		public static final byte PROTOCOL_VERSION = 7;
	}
	
	/**
	 * Contains constant values relating to Minecraft Pocket Edition protocol.
	 */
	public static class PocketProtocol {
		/**
		 * The protocol magic value.
		 */
		public static final byte[] MAGIC = new byte[] { 0, -1, -1, 0, -2, -2, -2, -2, -3, -3, -3, -3, 18, 52, 86, 120 };
	}
	
}
