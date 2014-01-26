package ch.spacebase.mc.protocol.data.game.values;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MagicValues {

	private static final Map<Enum<?>, Object> values = new HashMap<Enum<?>, Object>();
	
	static {
		register(AttributeType.MAX_HEALTH, "generic.maxHealth");
		register(AttributeType.FOLLOW_RANGE, "generic.followRange");
		register(AttributeType.KNOCKBACK_RESISTANCE, "generic.knockbackResistance");
		register(AttributeType.MOVEMENT_SPEED, "generic.movementSpeed");
		register(AttributeType.ATTACK_DAMAGE, "generic.attackStrength");
		register(AttributeType.HORSE_JUMP_STRENGTH, "generic.maxHealth");
		register(AttributeType.ZOMBIE_SPAWN_REINFORCEMENTS_CHANCE, "generic.maxHealth");
		
		register(ModifierType.CREATURE_FLEE_SPEED_BONUS, UUID.fromString("E199AD21-BA8A-4C53-8D13-6182D5C69D3A"));
		register(ModifierType.ENDERMAN_ATTACK_SPEED_BOOST, UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0"));
		register(ModifierType.SPRINT_SPEED_BOOST, UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D"));
		register(ModifierType.PIGZOMBIE_ATTACK_SPEED_BOOST, UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718"));
		register(ModifierType.WITCH_DRINKING_SPEED_PENALTY, UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E"));
		register(ModifierType.ZOMBIE_BABY_SPEED_BOOST, UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836"));
		register(ModifierType.ITEM_MODIFIER, UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF"));
		register(ModifierType.SPEED_POTION_MODIFIER, UUID.fromString("91AEAA56-376B-4498-935B-2F7F68070635"));
		register(ModifierType.HEALTH_BOOST_POTION_MODIFIER, UUID.fromString("5D6F0BA2-1186-46AC-B896-C61C5CEE99CC"));
		register(ModifierType.SLOW_POTION_MODIFIER, UUID.fromString("7107DE5E-7CE8-4030-940E-514C1F160890"));
		register(ModifierType.STRENGTH_POTION_MODIFIER, UUID.fromString("648D7064-6A60-4F59-8ABE-C2C23A6DD7A9"));
		register(ModifierType.WEAKNESS_POTION_MODIFIER, UUID.fromString("22653B89-116E-49DC-9B6B-9971489B5BE5"));
		
		register(ModifierOperation.ADD, 0);
		register(ModifierOperation.ADD_MULTIPLIED, 1);
		register(ModifierOperation.MULTIPLY, 2);
		
		register(MetadataType.BYTE, 0);
		register(MetadataType.SHORT, 1);
		register(MetadataType.INT, 2);
		register(MetadataType.FLOAT, 3);
		register(MetadataType.STRING, 4);
		register(MetadataType.ITEM, 5);
		register(MetadataType.POSITION, 6);
		
		register(HandshakeIntent.STATUS, 1);
		register(HandshakeIntent.LOGIN, 2);
		
		register(ClientRequest.RESPAWN, 0);
		register(ClientRequest.STATS, 1);
		register(ClientRequest.OPEN_INVENTORY_ACHIEVEMENT, 2);
		
		register(ChatVisibility.FULL, 0);
		register(ChatVisibility.SYSTEM, 1);
		register(ChatVisibility.HIDDEN, 2);
		
		register(PlayerState.START_SNEAKING, 1);
		register(PlayerState.STOP_SNEAKING, 2);
		register(PlayerState.LEAVE_BED, 3);
		register(PlayerState.START_SPRINTING, 4);
		register(PlayerState.STOP_SPRINTING, 5);
		register(PlayerState.RIDING_JUMP, 6);
		register(PlayerState.OPEN_INVENTORY, 7);
		
		register(InteractAction.INTERACT, 0);
		register(InteractAction.ATTACK, 1);
		
		register(PlayerAction.START_DIGGING, 0);
		register(PlayerAction.CANCEL_DIGGING, 1);
		register(PlayerAction.FINISH_DIGGING, 2);
		register(PlayerAction.DROP_ITEM_STACK, 3);
		register(PlayerAction.DROP_ITEM, 4);
		register(PlayerAction.RELEASE_USE_ITEM, 5);
		
		register(Face.INVALID, -1);
		register(Face.BOTTOM, 0);
		register(Face.TOP, 1);
		register(Face.EAST, 2);
		register(Face.WEST, 3);
		register(Face.NORTH, 4);
		register(Face.SOUTH, 5);
		register(Face.INVALID, 255);
		
		register(WindowAction.CLICK_ITEM, 0);
		register(WindowAction.SHIFT_CLICK_ITEM, 1);
		register(WindowAction.MOVE_TO_HOTBAR_SLOT, 2);
		register(WindowAction.CREATIVE_GRAB_MAX_STACK, 3);
		register(WindowAction.DROP_ITEM, 4);
		register(WindowAction.SPREAD_ITEM, 5);
		register(WindowAction.FILL_STACK, 6);
		
		register(ClickItemParam.LEFT_CLICK, 0);
		register(ClickItemParam.RIGHT_CLICK, 1);
		
		register(ShiftClickItemParam.LEFT_CLICK, 0);
		register(ShiftClickItemParam.RIGHT_CLICK, 1);
		
		register(MoveToHotbarParam.SLOT_1, 0);
		register(MoveToHotbarParam.SLOT_2, 1);
		register(MoveToHotbarParam.SLOT_3, 2);
		register(MoveToHotbarParam.SLOT_4, 3);
		register(MoveToHotbarParam.SLOT_5, 4);
		register(MoveToHotbarParam.SLOT_6, 5);
		register(MoveToHotbarParam.SLOT_7, 6);
		register(MoveToHotbarParam.SLOT_8, 7);
		register(MoveToHotbarParam.SLOT_9, 8);
		
		register(CreativeGrabParam.GRAB, 2);
		
		register(DropItemParam.LEFT_CLICK_OUTSIDE_NOT_HOLDING, 0);
		register(DropItemParam.RIGHT_CLICK_OUTSIDE_NOT_HOLDING, 1);
		register(DropItemParam.DROP_FROM_SELECTED, 2);
		register(DropItemParam.DROP_SELECTED_STACK, 3);
		
		register(SpreadItemParam.LEFT_MOUSE_BEGIN_DRAG, 0);
		register(SpreadItemParam.LEFT_MOUSE_ADD_SLOT, 1);
		register(SpreadItemParam.LEFT_MOUSE_END_DRAG, 2);
		register(SpreadItemParam.RIGHT_MOUSE_BEGIN_DRAG, 4);
		register(SpreadItemParam.RIGHT_MOUSE_ADD_SLOT, 5);
		register(SpreadItemParam.RIGHT_MOUSE_END_DRAG, 6);
		
		register(FillStackParam.FILL, 0);
		
		register(MessageType.CHAT, 0);
		register(MessageType.SYSTEM, 1);
		register(MessageType.NOTIFICATION, 2);
		
		register(CombatState.ENTER_COMBAT, 0);
		register(CombatState.END_COMBAT, 1);
		register(CombatState.ENTITY_DEAD, 2);
		
		register(GameMode.SURVIVAL, 0);
		register(GameMode.CREATIVE, 1);
		register(GameMode.ADVENTURE, 2);
		
		register(Difficulty.PEACEFUL, 0);
		register(Difficulty.EASY, 1);
		register(Difficulty.NORMAL, 2);
		register(Difficulty.HARD, 3);
		
		register(WorldType.DEFAULT, "default");
		register(WorldType.FLAT, "flat");
		register(WorldType.LARGE_BIOMES, "largeBiomes");
		register(WorldType.AMPLIFIED, "amplified");
		register(WorldType.DEFAULT_1_1, "default_1_1");
		
		register(Animation.SWING_ARM, 0);
		register(Animation.DAMAGE, 1);
		register(Animation.LEAVE_BED, 2);
		register(Animation.EAT_FOOD, 3);
		register(Animation.CRITICAL_HIT, 4);
		register(Animation.ENCHANTMENT_CRITICAL_HIT, 5);
		
		register(Effect.SPEED, 1);
		register(Effect.SLOWNESS, 2);
		register(Effect.DIG_SPEED, 3);
		register(Effect.DIG_SLOWNESS, 4);
		register(Effect.DAMAGE_BOOST, 5);
		register(Effect.HEAL, 6);
		register(Effect.DAMAGE, 7);
		register(Effect.JUMP_BOOST, 8);
		register(Effect.CONFUSION, 9);
		register(Effect.REGENERATION, 10);
		register(Effect.RESISTANCE, 11);
		register(Effect.FIRE_RESISTANCE, 12);
		register(Effect.WATER_BREATHING, 13);
		register(Effect.INVISIBILITY, 14);
		register(Effect.BLINDNESS, 15);
		register(Effect.NIGHT_VISION, 16);
		register(Effect.HUNGER, 17);
		register(Effect.WEAKNESS, 18);
		register(Effect.POISON, 19);
		register(Effect.WITHER_EFFECT, 20);
		register(Effect.HEALTH_BOOST, 21);
		register(Effect.ABSORPTION, 22);
		register(Effect.SATURATION, 23);
		
		register(EntityStatus.HURT_OR_MINECART_SPAWNER_DELAY_RESET, 1);
		register(EntityStatus.LIVING_HURT, 2);
		register(EntityStatus.DEAD, 3);
		register(EntityStatus.IRON_GOLEM_THROW, 4);
		register(EntityStatus.TAMING, 6);
		register(EntityStatus.TAMED, 7);
		register(EntityStatus.WOLF_SHAKING, 8);
		register(EntityStatus.FINISHED_EATING, 9);
		register(EntityStatus.SHEEP_GRAZING_OR_TNT_CART_EXPLODING, 10);
		register(EntityStatus.IRON_GOLEM_ROSE, 11);
		register(EntityStatus.VILLAGER_HEARTS, 12);
		register(EntityStatus.VILLAGER_ANGRY, 13);
		register(EntityStatus.VILLAGER_HAPPY, 14);
		register(EntityStatus.WITCH_MAGIC_PARTICLES, 15);
		register(EntityStatus.ZOMBIE_VILLAGER_SHAKING, 16);
		register(EntityStatus.FIREWORK_EXPLODING, 17);
		register(EntityStatus.ANIMAL_HEARTS, 18);
		
		register(PositionElement.X, 0);
		register(PositionElement.Y, 1);
		register(PositionElement.Z, 2);
		register(PositionElement.PITCH, 3);
		register(PositionElement.YAW, 4);
		
		register(GlobalEntityType.LIGHTNING_BOLT, 1);
		
		register(MobType.CREEPER, 50);
		register(MobType.SKELETON, 51);
		register(MobType.SPIDER, 52);
		register(MobType.GIANT_ZOMBIE, 53);
		register(MobType.ZOMBIE, 54);
		register(MobType.SLIME, 55);
		register(MobType.GHAST, 56);
		register(MobType.ZOMBIE_PIGMAN, 57);
		register(MobType.ENDERMAN, 58);
		register(MobType.CAVE_SPIDER, 59);
		register(MobType.SILVERFISH, 60);
		register(MobType.BLAZE, 61);
		register(MobType.MAGMA_CUBE, 62);
		register(MobType.ENDER_DRAGON, 63);
		register(MobType.WITHER, 64);
		register(MobType.BAT, 65);
		register(MobType.WITCH, 66);
		register(MobType.PIG, 90);
		register(MobType.SHEEP, 91);
		register(MobType.COW, 92);
		register(MobType.CHICKEN, 93);
		register(MobType.SQUID, 94);
		register(MobType.WOLF, 95);
		register(MobType.MOOSHROOM, 96);
		register(MobType.SNOWMAN, 97);
		register(MobType.OCELOT, 98);
		register(MobType.IRON_GOLEM, 99);
		register(MobType.HORSE, 100);
		register(MobType.VILLAGER, 120);
		
		register(ObjectType.BOAT, 1);
		register(ObjectType.ITEM, 2);
		register(ObjectType.MINECART, 10);
		register(ObjectType.PRIMED_TNT, 50);
		register(ObjectType.ENDER_CRYSTAL, 51);
		register(ObjectType.ARROW, 60);
		register(ObjectType.SNOWBALL, 61);
		register(ObjectType.EGG, 62);
		register(ObjectType.GHAST_FIREBALL, 63);
		register(ObjectType.BLAZE_FIREBALL, 64);
		register(ObjectType.ENDER_PEARL, 65);
		register(ObjectType.WITHER_HEAD_PROJECTILE, 66);
		register(ObjectType.FALLING_BLOCK, 70);
		register(ObjectType.ITEM_FRAME, 71);
		register(ObjectType.EYE_OF_ENDER, 72);
		register(ObjectType.POTION, 73);
		register(ObjectType.EXP_BOTTLE, 75);
		register(ObjectType.FIREWORK_ROCKET, 76);
		register(ObjectType.LEASH_KNOT, 77);
		register(ObjectType.FISH_HOOK, 90);
		
		register(MinecartType.NORMAL, 0);
		register(MinecartType.CHEST, 1);
		register(MinecartType.POWERED, 2);
		register(MinecartType.TNT, 3);
		register(MinecartType.MOB_SPAWNER, 4);
		register(MinecartType.HOPPER, 5);
		register(MinecartType.COMMAND_BLOCK, 6);
		
		register(HangingDirection.SOUTH, 0);
		register(HangingDirection.WEST, 1);
		register(HangingDirection.NORTH, 2);
		register(HangingDirection.EAST, 3);
		
		register(Art.KEBAB, "Kebab");
		register(Art.AZTEC, "Aztec");
		register(Art.ALBAN, "Alban");
		register(Art.AZTEC2, "Aztec2");
		register(Art.BOMB, "Bomb");
		register(Art.PLANT, "Plant");
		register(Art.WASTELAND, "Wasteland");
		register(Art.POOL, "Pool");
		register(Art.COURBET, "Courbet");
		register(Art.SEA, "Sea");
		register(Art.SUNSET, "Sunset");
		register(Art.CREEBET, "Creebet");
		register(Art.WANDERER, "Wanderer");
		register(Art.GRAHAM, "Graham");
		register(Art.MATCH, "Match");
		register(Art.BUST, "Bust");
		register(Art.STAGE, "Stage");
		register(Art.VOID, "Void");
		register(Art.SKULL_AND_ROSES, "SkullAndRoses");
		register(Art.WITHER, "Wither");
		register(Art.FIGHTERS, "Fighters");
		register(Art.POINTER, "Pointer");
		register(Art.PIG_SCENE, "Pigscene");
		register(Art.BURNING_SKULL, "BurningSkull");
		register(Art.SKELETON, "Skeleton");
		register(Art.DONKEY_KONG, "DonkeyKong");
		
		register(ScoreboardPosition.PLAYER_LIST, 0);
		register(ScoreboardPosition.SIDEBAR, 1);
		register(ScoreboardPosition.BELOW_NAME, 2);
		
		register(ObjectiveAction.ADD, 0);
		register(ObjectiveAction.REMOVE, 1);
		register(ObjectiveAction.UPDATE, 2);
		
		register(TeamAction.CREATE, 0);
		register(TeamAction.REMOVE, 1);
		register(TeamAction.UPDATE, 2);
		register(TeamAction.ADD_PLAYER, 3);
		register(TeamAction.REMOVE_PLAYER, 4);
		
		register(FriendlyFire.OFF, 0);
		register(FriendlyFire.ON, 1);
		register(FriendlyFire.FRIENDLY_INVISIBLES_VISIBLE, 3);
		
		register(ScoreboardAction.ADD_OR_UPDATE, 0);
		register(ScoreboardAction.REMOVE, 1);
		
		register(WindowType.GENERIC_INVENTORY, "minecraft:container");
		register(WindowType.ANVIL, "minecraft:anvil");
		register(WindowType.BEACON, "minecraft:beacon");
		register(WindowType.BREWING_STAND, "minecraft:brewing_stand");
		register(WindowType.CHEST, "minecraft:chest");
		register(WindowType.CRAFTING_TABLE, "minecraft:crafting_table");
		register(WindowType.DISPENSER, "minecraft:dispenser");
		register(WindowType.DROPPER, "minecraft:dropper");
		register(WindowType.ENCHANTING_TABLE, "minecraft:enchanting_table");
		register(WindowType.FURNACE, "minecraft:furnace");
		register(WindowType.HOPPER, "minecraft:hopper");
		register(WindowType.VILLAGER, "minecraft:villager");
		register(WindowType.HORSE, "EntityHorse");
		
		register(WindowProperty.FURNACE_PROGRESS_OR_ENCHANT_1, 0);
		register(WindowProperty.FURNACE_FUEL_OR_ENCHANT_2, 1);
		register(WindowProperty.ENCHANT_3, 2);
		
		register(BlockBreakStage.RESET, -1);
		register(BlockBreakStage.STAGE_1, 0);
		register(BlockBreakStage.STAGE_2, 1);
		register(BlockBreakStage.STAGE_3, 2);
		register(BlockBreakStage.STAGE_4, 3);
		register(BlockBreakStage.STAGE_5, 4);
		register(BlockBreakStage.STAGE_6, 5);
		register(BlockBreakStage.STAGE_7, 6);
		register(BlockBreakStage.STAGE_8, 7);
		register(BlockBreakStage.STAGE_9, 8);
		register(BlockBreakStage.STAGE_10, 9);
		register(BlockBreakStage.RESET, 255);
		
		register(MapDataType.IMAGE, 0);
		register(MapDataType.PLAYERS, 1);
		register(MapDataType.SCALE, 2);
		
		register(UpdatedTileType.MOB_SPAWNER, 1);
		register(UpdatedTileType.COMMAND_BLOCK, 2);
		register(UpdatedTileType.BEACON, 3);
		register(UpdatedTileType.SKULL, 4);
		register(UpdatedTileType.FLOWER_POT, 5);
		
		register(ClientNotification.INVALID_BED, 0);
		register(ClientNotification.START_RAIN, 1);
		register(ClientNotification.STOP_RAIN, 2);
		register(ClientNotification.CHANGE_GAMEMODE, 3);
		register(ClientNotification.ENTER_CREDITS, 4);
		register(ClientNotification.DEMO_MESSAGE, 5);
		register(ClientNotification.ARROW_HIT_PLAYER, 6);
		register(ClientNotification.RAIN_STRENGTH, 7);
		register(ClientNotification.THUNDER_STRENGTH, 8);
		
		register(DemoMessageValue.WELCOME, 0);
		register(DemoMessageValue.MOVEMENT_CONTROLS, 101);
		register(DemoMessageValue.JUMP_CONTROL, 102);
		register(DemoMessageValue.INVENTORY_CONTROL, 103);
	}
	
	private static void register(Enum<?> key, Object value) {
		values.put(key, value);
	}
	
	@SuppressWarnings({ "unchecked", "cast" })
	public static <T extends Enum<?>> T key(Class<T> keyType, Object value) {
		for(Enum<?> key : values.keySet()) {
			Object val = values.get(key);
			if(keyType.isAssignableFrom(key.getClass())) {
				if(val == value || val.equals(value)) {
					return (T) key;
				} else if(Number.class.isAssignableFrom(val.getClass())) {
					Number num = (Number) val;
					if(val.getClass() == Byte.class && (Object) num.byteValue() == value) {
						return (T) key;
					} else if(val.getClass() == Short.class && (Object) num.shortValue() == value) {
						return (T) key;
					} else if(val.getClass() == Integer.class && (Object) num.intValue() == value) {
						return (T) key;
					} else if(val.getClass() == Long.class && (Object) num.longValue() == value) {
						return (T) key;
					} else if(val.getClass() == Float.class && (Object) num.floatValue() == value) {
						return (T) key;
					} else if(val.getClass() == Double.class && (Object) num.doubleValue() == value) {
						return (T) key;
					}
				}
			}
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T value(Class<T> valueType, Enum<?> key) {
		Object val = values.get(key);
		if(valueType.isAssignableFrom(val.getClass())) {
			return (T) val;
		} else if(Number.class.isAssignableFrom(val.getClass())) {
			if(valueType == Byte.class) {
				return (T) (Object) ((Number) val).byteValue();
			} else if(valueType == Short.class) {
				return (T) (Object) ((Number) val).shortValue();
			} else if(valueType == Integer.class) {
				return (T) (Object) ((Number) val).intValue();
			} else if(valueType == Long.class) {
				return (T) (Object) ((Number) val).longValue();
			} else if(valueType == Float.class) {
				return (T) (Object) ((Number) val).floatValue();
			} else if(valueType == Double.class) {
				return (T) (Object) ((Number) val).doubleValue();
			}
		}
		
		return null;
	}
	
}
