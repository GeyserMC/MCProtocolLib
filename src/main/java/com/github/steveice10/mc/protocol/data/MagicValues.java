package com.github.steveice10.mc.protocol.data;

import com.github.steveice10.mc.protocol.data.game.BossBarAction;
import com.github.steveice10.mc.protocol.data.game.BossBarColor;
import com.github.steveice10.mc.protocol.data.game.BossBarDivision;
import com.github.steveice10.mc.protocol.data.game.ClientRequest;
import com.github.steveice10.mc.protocol.data.game.MessageType;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntryAction;
import com.github.steveice10.mc.protocol.data.game.ResourcePackStatus;
import com.github.steveice10.mc.protocol.data.game.TitleAction;
import com.github.steveice10.mc.protocol.data.game.UnlockRecipesAction;
import com.github.steveice10.mc.protocol.data.game.advancement.Advancement;
import com.github.steveice10.mc.protocol.data.game.entity.Effect;
import com.github.steveice10.mc.protocol.data.game.entity.EntityStatus;
import com.github.steveice10.mc.protocol.data.game.entity.EquipmentSlot;
import com.github.steveice10.mc.protocol.data.game.entity.attribute.AttributeType;
import com.github.steveice10.mc.protocol.data.game.entity.attribute.ModifierOperation;
import com.github.steveice10.mc.protocol.data.game.entity.attribute.ModifierType;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.MetadataType;
import com.github.steveice10.mc.protocol.data.game.entity.player.Animation;
import com.github.steveice10.mc.protocol.data.game.entity.player.BlockBreakStage;
import com.github.steveice10.mc.protocol.data.game.entity.player.CombatState;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.data.game.entity.player.InteractAction;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerAction;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerState;
import com.github.steveice10.mc.protocol.data.game.entity.player.PositionElement;
import com.github.steveice10.mc.protocol.data.game.entity.type.GlobalEntityType;
import com.github.steveice10.mc.protocol.data.game.entity.type.MobType;
import com.github.steveice10.mc.protocol.data.game.entity.type.PaintingType;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.HangingDirection;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.MinecartType;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.ObjectType;
import com.github.steveice10.mc.protocol.data.game.scoreboard.CollisionRule;
import com.github.steveice10.mc.protocol.data.game.scoreboard.NameTagVisibility;
import com.github.steveice10.mc.protocol.data.game.scoreboard.ObjectiveAction;
import com.github.steveice10.mc.protocol.data.game.scoreboard.ScoreType;
import com.github.steveice10.mc.protocol.data.game.scoreboard.ScoreboardAction;
import com.github.steveice10.mc.protocol.data.game.scoreboard.ScoreboardPosition;
import com.github.steveice10.mc.protocol.data.game.scoreboard.TeamAction;
import com.github.steveice10.mc.protocol.data.game.scoreboard.TeamColor;
import com.github.steveice10.mc.protocol.data.game.setting.ChatVisibility;
import com.github.steveice10.mc.protocol.data.game.setting.Difficulty;
import com.github.steveice10.mc.protocol.data.game.statistic.GenericStatistic;
import com.github.steveice10.mc.protocol.data.game.window.AdvancementTabAction;
import com.github.steveice10.mc.protocol.data.game.window.ClickItemParam;
import com.github.steveice10.mc.protocol.data.game.window.CraftingBookDataType;
import com.github.steveice10.mc.protocol.data.game.window.CreativeGrabParam;
import com.github.steveice10.mc.protocol.data.game.window.DropItemParam;
import com.github.steveice10.mc.protocol.data.game.window.FillStackParam;
import com.github.steveice10.mc.protocol.data.game.window.MoveToHotbarParam;
import com.github.steveice10.mc.protocol.data.game.window.ShiftClickItemParam;
import com.github.steveice10.mc.protocol.data.game.window.SpreadItemParam;
import com.github.steveice10.mc.protocol.data.game.window.WindowAction;
import com.github.steveice10.mc.protocol.data.game.window.WindowType;
import com.github.steveice10.mc.protocol.data.game.window.property.AnvilProperty;
import com.github.steveice10.mc.protocol.data.game.window.property.BrewingStandProperty;
import com.github.steveice10.mc.protocol.data.game.window.property.EnchantmentTableProperty;
import com.github.steveice10.mc.protocol.data.game.window.property.FurnaceProperty;
import com.github.steveice10.mc.protocol.data.game.world.Particle;
import com.github.steveice10.mc.protocol.data.game.world.WorldBorderAction;
import com.github.steveice10.mc.protocol.data.game.world.WorldType;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockFace;
import com.github.steveice10.mc.protocol.data.game.world.block.UpdatedTileType;
import com.github.steveice10.mc.protocol.data.game.world.block.value.ChestValueType;
import com.github.steveice10.mc.protocol.data.game.world.block.value.GenericBlockValueType;
import com.github.steveice10.mc.protocol.data.game.world.block.value.MobSpawnerValueType;
import com.github.steveice10.mc.protocol.data.game.world.block.value.NoteBlockValueType;
import com.github.steveice10.mc.protocol.data.game.world.block.value.PistonValue;
import com.github.steveice10.mc.protocol.data.game.world.block.value.PistonValueType;
import com.github.steveice10.mc.protocol.data.game.world.effect.ParticleEffect;
import com.github.steveice10.mc.protocol.data.game.world.effect.SmokeEffectData;
import com.github.steveice10.mc.protocol.data.game.world.effect.SoundEffect;
import com.github.steveice10.mc.protocol.data.game.world.map.MapIconType;
import com.github.steveice10.mc.protocol.data.game.world.notify.ClientNotification;
import com.github.steveice10.mc.protocol.data.game.world.notify.DemoMessageValue;
import com.github.steveice10.mc.protocol.data.game.world.notify.EnterCreditsValue;
import com.github.steveice10.mc.protocol.data.game.world.sound.BuiltinSound;
import com.github.steveice10.mc.protocol.data.game.world.sound.SoundCategory;
import com.github.steveice10.mc.protocol.data.handshake.HandshakeIntent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MagicValues {
    private static final Map<Object, List<Object>> values = new HashMap<Object, List<Object>>();

    static {
        register(AttributeType.GENERIC_MAX_HEALTH, "generic.maxHealth");
        register(AttributeType.GENERIC_FOLLOW_RANGE, "generic.followRange");
        register(AttributeType.GENERIC_KNOCKBACK_RESISTANCE, "generic.knockbackResistance");
        register(AttributeType.GENERIC_MOVEMENT_SPEED, "generic.movementSpeed");
        register(AttributeType.GENERIC_ATTACK_DAMAGE, "generic.attackDamage");
        register(AttributeType.GENERIC_ATTACK_SPEED, "generic.attackSpeed");
        register(AttributeType.GENERIC_ARMOR, "generic.armor");
        register(AttributeType.GENERIC_ARMOR_TOUGHNESS, "generic.armorToughness");
        register(AttributeType.GENERIC_LUCK, "generic.luck");
        register(AttributeType.GENERIC_FLYING_SPEED, "generic.flyingSpeed");
        register(AttributeType.HORSE_JUMP_STRENGTH, "horse.jumpStrength");
        register(AttributeType.ZOMBIE_SPAWN_REINFORCEMENTS, "zombie.spawnReinforcements");

        register(ModifierType.CREATURE_FLEE_SPEED_BONUS, UUID.fromString("E199AD21-BA8A-4C53-8D13-6182D5C69D3A"));
        register(ModifierType.ENDERMAN_ATTACK_SPEED_BOOST, UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0"));
        register(ModifierType.SPRINT_SPEED_BOOST, UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D"));
        register(ModifierType.PIGZOMBIE_ATTACK_SPEED_BOOST, UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718"));
        register(ModifierType.WITCH_DRINKING_SPEED_PENALTY, UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E"));
        register(ModifierType.ZOMBIE_BABY_SPEED_BOOST, UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836"));
        register(ModifierType.ATTACK_DAMAGE_MODIFIER, UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF"));
        register(ModifierType.ATTACK_SPEED_MODIFIER, UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3"));
        register(ModifierType.SPEED_POTION_MODIFIER, UUID.fromString("91AEAA56-376B-4498-935B-2F7F68070635"));
        register(ModifierType.HEALTH_BOOST_POTION_MODIFIER, UUID.fromString("5D6F0BA2-1186-46AC-B896-C61C5CEE99CC"));
        register(ModifierType.SLOW_POTION_MODIFIER, UUID.fromString("7107DE5E-7CE8-4030-940E-514C1F160890"));
        register(ModifierType.STRENGTH_POTION_MODIFIER, UUID.fromString("648D7064-6A60-4F59-8ABE-C2C23A6DD7A9"));
        register(ModifierType.WEAKNESS_POTION_MODIFIER, UUID.fromString("22653B89-116E-49DC-9B6B-9971489B5BE5"));
        register(ModifierType.HASTE_POTION_MODIFIER, UUID.fromString("AF8B6E3F-3328-4C0A-AA36-5BA2BB9DBEF3"));
        register(ModifierType.MINING_FATIGUE_POTION_MODIFIER, UUID.fromString("55FCED67-E92A-486E-9800-B47F202C4386"));
        register(ModifierType.LUCK_POTION_MODIFIER, UUID.fromString("03C3C89D-7037-4B42-869F-B146BCB64D2E"));
        register(ModifierType.UNLUCK_POTION_MODIFIER, UUID.fromString("CC5AF142-2BD2-4215-B636-2605AED11727"));
        register(ModifierType.BOOTS_MODIFIER, UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"));
        register(ModifierType.LEGGINGS_MODIFIER, UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"));
        register(ModifierType.CHESTPLATE_MODIFIER, UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"));
        register(ModifierType.HELMET_MODIFIER, UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"));
        register(ModifierType.COVERED_ARMOR_BONUS, UUID.fromString("7E0292F2-9434-48D5-A29F-9583AF7DF27F"));

        register(ModifierOperation.ADD, 0);
        register(ModifierOperation.ADD_MULTIPLIED, 1);
        register(ModifierOperation.MULTIPLY, 2);

        register(MetadataType.BYTE, 0);
        register(MetadataType.INT, 1);
        register(MetadataType.FLOAT, 2);
        register(MetadataType.STRING, 3);
        register(MetadataType.CHAT, 4);
        register(MetadataType.ITEM, 5);
        register(MetadataType.BOOLEAN, 6);
        register(MetadataType.ROTATION, 7);
        register(MetadataType.POSITION, 8);
        register(MetadataType.OPTIONAL_POSITION, 9);
        register(MetadataType.BLOCK_FACE, 10);
        register(MetadataType.OPTIONAL_UUID, 11);
        register(MetadataType.BLOCK_STATE, 12);
        register(MetadataType.NBT_TAG, 13);

        register(HandshakeIntent.STATUS, 1);
        register(HandshakeIntent.LOGIN, 2);

        register(ClientRequest.RESPAWN, 0);
        register(ClientRequest.STATS, 1);

        register(ChatVisibility.FULL, 0);
        register(ChatVisibility.SYSTEM, 1);
        register(ChatVisibility.HIDDEN, 2);

        register(PlayerState.START_SNEAKING, 0);
        register(PlayerState.STOP_SNEAKING, 1);
        register(PlayerState.LEAVE_BED, 2);
        register(PlayerState.START_SPRINTING, 3);
        register(PlayerState.STOP_SPRINTING, 4);
        register(PlayerState.START_HORSE_JUMP, 5);
        register(PlayerState.STOP_HORSE_JUMP, 6);
        register(PlayerState.OPEN_HORSE_INVENTORY, 7);
        register(PlayerState.START_ELYTRA_FLYING, 8);

        register(InteractAction.INTERACT, 0);
        register(InteractAction.ATTACK, 1);
        register(InteractAction.INTERACT_AT, 2);

        register(PlayerAction.START_DIGGING, 0);
        register(PlayerAction.CANCEL_DIGGING, 1);
        register(PlayerAction.FINISH_DIGGING, 2);
        register(PlayerAction.DROP_ITEM_STACK, 3);
        register(PlayerAction.DROP_ITEM, 4);
        register(PlayerAction.RELEASE_USE_ITEM, 5);
        register(PlayerAction.SWAP_HANDS, 6);

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
        register(GameMode.SPECTATOR, 3);

        register(Difficulty.PEACEFUL, 0);
        register(Difficulty.EASY, 1);
        register(Difficulty.NORMAL, 2);
        register(Difficulty.HARD, 3);

        register(WorldType.DEFAULT, "default");
        register(WorldType.FLAT, "flat");
        register(WorldType.LARGE_BIOMES, "largebiomes");
        register(WorldType.AMPLIFIED, "amplified");
        register(WorldType.CUSTOMIZED, "customized");
        register(WorldType.DEBUG, "debug_all_block_states");
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
        register(Effect.GLOWING, 24);
        register(Effect.LEVITATION, 25);
        register(Effect.LUCK, 26);
        register(Effect.BAD_LUCK, 27);

        register(EntityStatus.TIPPED_ARROW_EMIT_PARTICLES, 0);
        register(EntityStatus.RABBIT_JUMP_OR_MINECART_SPAWNER_DELAY_RESET, 1);
        register(EntityStatus.LIVING_HURT, 2);
        register(EntityStatus.LIVING_DEATH, 3);
        register(EntityStatus.IRON_GOLEM_ATTACK, 4);
        register(EntityStatus.TAMEABLE_TAMING_FAILED, 6);
        register(EntityStatus.TAMEABLE_TAMING_SUCCEEDED, 7);
        register(EntityStatus.WOLF_SHAKE_WATER, 8);
        register(EntityStatus.PLAYER_FINISH_USING_ITEM, 9);
        register(EntityStatus.SHEEP_GRAZE_OR_TNT_CART_EXPLODE, 10);
        register(EntityStatus.IRON_GOLEM_HOLD_POPPY, 11);
        register(EntityStatus.VILLAGER_MATE, 12);
        register(EntityStatus.VILLAGER_ANGRY, 13);
        register(EntityStatus.VILLAGER_HAPPY, 14);
        register(EntityStatus.WITCH_EMIT_PARTICLES, 15);
        register(EntityStatus.ZOMBIE_VILLAGER_CURE, 16);
        register(EntityStatus.FIREWORK_EXPLODE, 17);
        register(EntityStatus.ANIMAL_EMIT_HEARTS, 18);
        register(EntityStatus.SQUID_RESET_ROTATION, 19);
        register(EntityStatus.MOB_EMIT_SMOKE, 20);
        register(EntityStatus.GUARDIAN_MAKE_SOUND, 21);
        register(EntityStatus.PLAYER_ENABLE_REDUCED_DEBUG, 22);
        register(EntityStatus.PLAYER_DISABLE_REDUCED_DEBUG, 23);
        register(EntityStatus.PLAYER_OP_PERMISSION_LEVEL_0, 24);
        register(EntityStatus.PLAYER_OP_PERMISSION_LEVEL_1, 25);
        register(EntityStatus.PLAYER_OP_PERMISSION_LEVEL_2, 26);
        register(EntityStatus.PLAYER_OP_PERMISSION_LEVEL_3, 27);
        register(EntityStatus.PLAYER_OP_PERMISSION_LEVEL_4, 28);
        register(EntityStatus.LIVING_SHIELD_BLOCK, 29);
        register(EntityStatus.LIVING_SHIELD_BREAK, 30);
        register(EntityStatus.FISHING_HOOK_PULL_PLAYER, 31);
        register(EntityStatus.ARMOR_STAND_HIT, 32);
        register(EntityStatus.LIVING_HURT_THORNS, 33);
        register(EntityStatus.IRON_GOLEM_EMPTY_HAND, 34);
        register(EntityStatus.TOTEM_OF_UNDYING_MAKE_SOUND, 35);
        register(EntityStatus.LIVING_DROWN, 36);
        register(EntityStatus.LIVING_BURN, 37);

        register(PositionElement.X, 0);
        register(PositionElement.Y, 1);
        register(PositionElement.Z, 2);
        register(PositionElement.PITCH, 3);
        register(PositionElement.YAW, 4);

        register(GlobalEntityType.LIGHTNING_BOLT, 1);

        register(MobType.ELDER_GUARDIAN, 4);
        register(MobType.WITHER_SKELETON, 5);
        register(MobType.STRAY, 6);
        register(MobType.HUSK, 23);
        register(MobType.ZOMBIE_VILLAGER, 27);
        register(MobType.SKELETON_HORSE, 28);
        register(MobType.ZOMBIE_HORSE, 29);
        register(MobType.ARMOR_STAND, 30);
        register(MobType.DONKEY, 31);
        register(MobType.MULE, 32);
        register(MobType.EVOCATION_ILLAGER, 34);
        register(MobType.VEX, 35);
        register(MobType.VINDICATION_ILLAGER, 36);
        register(MobType.ILLUSION_ILLAGER, 37);
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
        register(MobType.ENDERMITE, 67);
        register(MobType.GUARDIAN, 68);
        register(MobType.SHULKER, 69);
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
        register(MobType.RABBIT, 101);
        register(MobType.POLAR_BEAR, 102);
        register(MobType.LLAMA, 103);
        register(MobType.PARROT, 105);
        register(MobType.VILLAGER, 120);

        register(ObjectType.BOAT, 1);
        register(ObjectType.ITEM, 2);
        register(ObjectType.AREA_EFFECT_CLOUD, 3);
        register(ObjectType.MINECART, 10);
        register(ObjectType.PRIMED_TNT, 50);
        register(ObjectType.ENDER_CRYSTAL, 51);
        register(ObjectType.TIPPED_ARROW, 60);
        register(ObjectType.SNOWBALL, 61);
        register(ObjectType.EGG, 62);
        register(ObjectType.GHAST_FIREBALL, 63);
        register(ObjectType.BLAZE_FIREBALL, 64);
        register(ObjectType.ENDER_PEARL, 65);
        register(ObjectType.WITHER_HEAD_PROJECTILE, 66);
        register(ObjectType.SHULKER_BULLET, 67);
        register(ObjectType.LLAMA_SPIT, 68);
        register(ObjectType.FALLING_BLOCK, 70);
        register(ObjectType.ITEM_FRAME, 71);
        register(ObjectType.EYE_OF_ENDER, 72);
        register(ObjectType.POTION, 73);
        register(ObjectType.EXP_BOTTLE, 75);
        register(ObjectType.FIREWORK_ROCKET, 76);
        register(ObjectType.LEASH_KNOT, 77);
        register(ObjectType.ARMOR_STAND, 78);
        register(ObjectType.EVOCATION_FANGS, 79);
        register(ObjectType.FISH_HOOK, 90);
        register(ObjectType.SPECTRAL_ARROW, 91);
        register(ObjectType.DRAGON_FIREBALL, 93);

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

        register(PaintingType.KEBAB, "Kebab");
        register(PaintingType.AZTEC, "Aztec");
        register(PaintingType.ALBAN, "Alban");
        register(PaintingType.AZTEC2, "Aztec2");
        register(PaintingType.BOMB, "Bomb");
        register(PaintingType.PLANT, "Plant");
        register(PaintingType.WASTELAND, "Wasteland");
        register(PaintingType.POOL, "Pool");
        register(PaintingType.COURBET, "Courbet");
        register(PaintingType.SEA, "Sea");
        register(PaintingType.SUNSET, "Sunset");
        register(PaintingType.CREEBET, "Creebet");
        register(PaintingType.WANDERER, "Wanderer");
        register(PaintingType.GRAHAM, "Graham");
        register(PaintingType.MATCH, "Match");
        register(PaintingType.BUST, "Bust");
        register(PaintingType.STAGE, "Stage");
        register(PaintingType.VOID, "Void");
        register(PaintingType.SKULL_AND_ROSES, "SkullAndRoses");
        register(PaintingType.WITHER, "Wither");
        register(PaintingType.FIGHTERS, "Fighters");
        register(PaintingType.POINTER, "Pointer");
        register(PaintingType.PIG_SCENE, "Pigscene");
        register(PaintingType.BURNING_SKULL, "BurningSkull");
        register(PaintingType.SKELETON, "Skeleton");
        register(PaintingType.DONKEY_KONG, "DonkeyKong");

        register(ScoreboardPosition.PLAYER_LIST, 0);
        register(ScoreboardPosition.SIDEBAR, 1);
        register(ScoreboardPosition.BELOW_NAME, 2);
        register(ScoreboardPosition.SIDEBAR_TEAM_BLACK, 3);
        register(ScoreboardPosition.SIDEBAR_TEAM_DARK_BLUE, 4);
        register(ScoreboardPosition.SIDEBAR_TEAM_DARK_GREEN, 5);
        register(ScoreboardPosition.SIDEBAR_TEAM_DARK_AQUA, 6);
        register(ScoreboardPosition.SIDEBAR_TEAM_DARK_RED, 7);
        register(ScoreboardPosition.SIDEBAR_TEAM_DARK_PURPLE, 8);
        register(ScoreboardPosition.SIDEBAR_TEAM_GOLD, 9);
        register(ScoreboardPosition.SIDEBAR_TEAM_GRAY, 10);
        register(ScoreboardPosition.SIDEBAR_TEAM_DARK_GRAY, 11);
        register(ScoreboardPosition.SIDEBAR_TEAM_BLUE, 12);
        register(ScoreboardPosition.SIDEBAR_TEAM_GREEN, 13);
        register(ScoreboardPosition.SIDEBAR_TEAM_AQUA, 14);
        register(ScoreboardPosition.SIDEBAR_TEAM_RED, 15);
        register(ScoreboardPosition.SIDEBAR_TEAM_LIGHT_PURPLE, 16);
        register(ScoreboardPosition.SIDEBAR_TEAM_YELLOW, 17);
        register(ScoreboardPosition.SIDEBAR_TEAM_WHITE, 18);

        register(ObjectiveAction.ADD, 0);
        register(ObjectiveAction.REMOVE, 1);
        register(ObjectiveAction.UPDATE, 2);

        register(TeamAction.CREATE, 0);
        register(TeamAction.REMOVE, 1);
        register(TeamAction.UPDATE, 2);
        register(TeamAction.ADD_PLAYER, 3);
        register(TeamAction.REMOVE_PLAYER, 4);

        register(ScoreboardAction.ADD_OR_UPDATE, 0);
        register(ScoreboardAction.REMOVE, 1);

        register(MapIconType.WHITE_ARROW, 0);
        register(MapIconType.GREEN_ARROW, 1);
        register(MapIconType.RED_ARROW, 2);
        register(MapIconType.BLUE_ARROW, 3);
        register(MapIconType.WHITE_CROSS, 4);
        register(MapIconType.RED_POINTER, 5);
        register(MapIconType.WHITE_CIRCLE, 6);
        register(MapIconType.SMALL_WHITE_CIRCLE, 7);
        register(MapIconType.MANSION, 8);
        register(MapIconType.TEMPLE, 9);
        register(MapIconType.UNUSED_10, 10);
        register(MapIconType.UNUSED_11, 11);
        register(MapIconType.UNUSED_12, 12);
        register(MapIconType.UNUSED_13, 13);
        register(MapIconType.UNUSED_14, 14);
        register(MapIconType.UNUSED_15, 15);

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
        register(WindowType.SHULKER_BOX, "minecraft:shulker_box");
        register(WindowType.HORSE, "EntityHorse");

        register(BrewingStandProperty.BREW_TIME, 0);

        register(EnchantmentTableProperty.LEVEL_SLOT_1, 0);
        register(EnchantmentTableProperty.LEVEL_SLOT_2, 1);
        register(EnchantmentTableProperty.LEVEL_SLOT_3, 2);
        register(EnchantmentTableProperty.XP_SEED, 3);
        register(EnchantmentTableProperty.ENCHANTMENT_SLOT_1, 4);
        register(EnchantmentTableProperty.ENCHANTMENT_SLOT_2, 5);
        register(EnchantmentTableProperty.ENCHANTMENT_SLOT_3, 6);

        register(FurnaceProperty.BURN_TIME, 0);
        register(FurnaceProperty.CURRENT_ITEM_BURN_TIME, 1);
        register(FurnaceProperty.COOK_TIME, 2);
        register(FurnaceProperty.TOTAL_COOK_TIME, 3);

        register(AnvilProperty.MAXIMUM_COST, 0);

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

        register(UpdatedTileType.MOB_SPAWNER, 1);
        register(UpdatedTileType.COMMAND_BLOCK, 2);
        register(UpdatedTileType.BEACON, 3);
        register(UpdatedTileType.SKULL, 4);
        register(UpdatedTileType.FLOWER_POT, 5);
        register(UpdatedTileType.BANNER, 6);
        register(UpdatedTileType.STRUCTURE_BLOCK, 7);
        register(UpdatedTileType.END_GATEWAY, 8);
        register(UpdatedTileType.SIGN, 9);
        register(UpdatedTileType.SHULKER_BOX, 10);
        register(UpdatedTileType.BED, 11);

        register(ClientNotification.INVALID_BED, 0);
        register(ClientNotification.START_RAIN, 2);
        register(ClientNotification.STOP_RAIN, 1);
        register(ClientNotification.CHANGE_GAMEMODE, 3);
        register(ClientNotification.ENTER_CREDITS, 4);
        register(ClientNotification.DEMO_MESSAGE, 5);
        register(ClientNotification.ARROW_HIT_PLAYER, 6);
        register(ClientNotification.RAIN_STRENGTH, 7);
        register(ClientNotification.THUNDER_STRENGTH, 8);
        register(ClientNotification.AFFECTED_BY_ELDER_GUARDIAN, 10);

        register(DemoMessageValue.WELCOME, 0);
        register(DemoMessageValue.MOVEMENT_CONTROLS, 101);
        register(DemoMessageValue.JUMP_CONTROL, 102);
        register(DemoMessageValue.INVENTORY_CONTROL, 103);

        register(EnterCreditsValue.SEEN_BEFORE, 0);
        register(EnterCreditsValue.FIRST_TIME, 1);

        register(GenericStatistic.CAKE_SLICES_EATEN, "stat.cakeSlicesEaten");
        register(GenericStatistic.TIMES_CAULDRON_FILLED, "stat.cauldronFilled");
        register(GenericStatistic.TIMES_CAULDRON_USED, "stat.cauldronUsed");
        register(GenericStatistic.TIMES_ARMOR_CLEANED, "stat.armorCleaned");
        register(GenericStatistic.TIMES_BANNER_CLEANED, "stat.bannerCleaned");
        register(GenericStatistic.TIMES_BREWING_STAND_GUI_OPENED, "stat.brewingstandInteraction");
        register(GenericStatistic.TIMES_BEACON_GUI_OPENED, "stat.beaconInteraction");
        register(GenericStatistic.TIMES_CRAFTING_TABLE_GUI_OPENED, "stat.craftingTableInteraction");
        register(GenericStatistic.TIMES_FURNACE_GUI_OPENED, "stat.furnaceInteraction");
        register(GenericStatistic.TIMES_DISPENSER_GUI_OPENED, "stat.dispenserInteraction");
        register(GenericStatistic.TIMES_DROPPER_GUI_OPENED, "stat.dropperInteraction");
        register(GenericStatistic.TIMES_HOPPER_GUI_OPENED, "stat.hopperInteraction");
        register(GenericStatistic.TIMES_CHEST_GUI_OPENED, "stat.chestInteraction");
        register(GenericStatistic.TIMES_SHULKER_BOX_GUI_OPENED, "stat.shulkerBoxOpened");
        register(GenericStatistic.TIMES_TRAPPED_CHEST_GUI_OPENED, "stat.trappedChestInteraction");
        register(GenericStatistic.TIMES_ENDER_CHEST_GUI_OPENED, "stat.enderchestInteraction");
        register(GenericStatistic.TIMES_NOTEBLOCK_PLAYED, "stat.noteblockPlayed");
        register(GenericStatistic.TIMES_NOTEBLOCK_TUNED, "stat.noteblockTuned");
        register(GenericStatistic.TIMES_PLANT_POTTED, "stat.flowerPotted");
        register(GenericStatistic.TIMES_RECORD_PLAYED, "stat.recordPlayed");
        register(GenericStatistic.TIMES_BED_ENTERED, "stat.sleepInBed");
        register(GenericStatistic.TIMES_LEFT_GAME, "stat.leaveGame");
        register(GenericStatistic.TICKS_PLAYED, "stat.playOneMinute");
        register(GenericStatistic.TICKS_SINCE_DEATH, "stat.timeSinceDeath");
        register(GenericStatistic.TICKS_SNEAKED, "stat.sneakTime");
        register(GenericStatistic.CENTIMETERS_WALKED, "stat.walkOneCm");
        register(GenericStatistic.CENTIMETERS_CROUCHED, "stat.crouchOneCm");
        register(GenericStatistic.CENTIMETERS_SPRINTED, "stat.sprintOneCm");
        register(GenericStatistic.CENTIMETERS_SWAM, "stat.swimOneCm");
        register(GenericStatistic.CENTIMETERS_FALLEN, "stat.fallOneCm");
        register(GenericStatistic.CENTIMETERS_CLIMBED, "stat.climbOneCm");
        register(GenericStatistic.CENTIMETERS_FLOWN, "stat.flyOneCm");
        register(GenericStatistic.CENTIMETERS_FLOWN_WITH_ELYTRA, "stat.aviateOneCm");
        register(GenericStatistic.CENTIMETERS_DOVE, "stat.diveOneCm");
        register(GenericStatistic.CENTIMETERS_TRAVELLED_IN_MINECART, "stat.minecartOneCm");
        register(GenericStatistic.CENTIMETERS_TRAVELLED_IN_BOAT, "stat.boatOneCm");
        register(GenericStatistic.CENTIMETERS_RODE_ON_PIG, "stat.pigOneCm");
        register(GenericStatistic.CENTIMETERS_RODE_ON_HORSE, "stat.horseOneCm");
        register(GenericStatistic.TIMES_JUMPED, "stat.jump");
        register(GenericStatistic.TIMES_DROPPED_ITEMS, "stat.drop");
        register(GenericStatistic.TIMES_ENCHANTED_ITEMS, "stat.itemEnchanted");
        register(GenericStatistic.TIMES_TALKED_TO_VILLAGERS, "stat.talkedToVillager");
        register(GenericStatistic.TIMES_TRADED_WITH_VILLAGERS, "stat.tradedWithVillager");
        register(GenericStatistic.DAMAGE_DEALT, "stat.damageDealt");
        register(GenericStatistic.DAMAGE_TAKEN, "stat.damageTaken");
        register(GenericStatistic.DEATHS, "stat.deaths");
        register(GenericStatistic.MOB_KILLS, "stat.mobKills");
        register(GenericStatistic.ANIMALS_BRED, "stat.animalsBred");
        register(GenericStatistic.PLAYERS_KILLED, "stat.playerKills");
        register(GenericStatistic.FISH_CAUGHT, "stat.fishCaught");

        register(Particle.EXPLOSION_NORMAL, 0);
        register(Particle.EXPLOSION_LARGE, 1);
        register(Particle.EXPLOSION_HUGE, 2);
        register(Particle.FIREWORKS_SPARK, 3);
        register(Particle.WATER_BUBBLE, 4);
        register(Particle.WATER_SPLASH, 5);
        register(Particle.WATER_WAKE, 6);
        register(Particle.SUSPENDED, 7);
        register(Particle.SUSPENDED_DEPTH, 8);
        register(Particle.CRIT, 9);
        register(Particle.CRIT_MAGIC, 10);
        register(Particle.SMOKE_NORMAL, 11);
        register(Particle.SMOKE_LARGE, 12);
        register(Particle.SPELL, 13);
        register(Particle.SPELL_INSTANT, 14);
        register(Particle.SPELL_MOB, 15);
        register(Particle.SPELL_MOB_AMBIENT, 16);
        register(Particle.SPELL_WITCH, 17);
        register(Particle.DRIP_WATER, 18);
        register(Particle.DRIP_LAVA, 19);
        register(Particle.VILLAGER_ANGRY, 20);
        register(Particle.VILLAGER_HAPPY, 21);
        register(Particle.TOWN_AURA, 22);
        register(Particle.NOTE, 23);
        register(Particle.PORTAL, 24);
        register(Particle.ENCHANTMENT_TABLE, 25);
        register(Particle.FLAME, 26);
        register(Particle.LAVA, 27);
        register(Particle.FOOTSTEP, 28);
        register(Particle.CLOUD, 29);
        register(Particle.REDSTONE, 30);
        register(Particle.SNOWBALL, 31);
        register(Particle.SNOW_SHOVEL, 32);
        register(Particle.SLIME, 33);
        register(Particle.HEART, 34);
        register(Particle.BARRIER, 35);
        register(Particle.ITEM_CRACK, 36);
        register(Particle.BLOCK_CRACK, 37);
        register(Particle.BLOCK_DUST, 38);
        register(Particle.WATER_DROP, 39);
        register(Particle.ITEM_TAKE, 40);
        register(Particle.MOB_APPEARANCE, 41);
        register(Particle.DRAGON_BREATH, 42);
        register(Particle.END_ROD, 43);
        register(Particle.DAMAGE_INDICATOR, 44);
        register(Particle.SWEEP_ATTACK, 45);
        register(Particle.FALLING_DUST, 46);
        register(Particle.TOTEM, 47);
        register(Particle.SPIT, 48);

        register(NoteBlockValueType.HARP, 0);
        register(NoteBlockValueType.DOUBLE_BASS, 1);
        register(NoteBlockValueType.SNARE_DRUM, 2);
        register(NoteBlockValueType.HI_HAT, 3);
        register(NoteBlockValueType.BASS_DRUM, 4);
        register(NoteBlockValueType.FLUTE, 5);
        register(NoteBlockValueType.BELL, 6);
        register(NoteBlockValueType.GUITAR, 7);
        register(NoteBlockValueType.CHIME, 8);
        register(NoteBlockValueType.XYLOPHONE, 9);

        register(PistonValueType.PUSHING, 0);
        register(PistonValueType.PULLING, 1);

        register(MobSpawnerValueType.RESET_DELAY, 1);

        register(ChestValueType.VIEWING_PLAYER_COUNT, 1);

        register(GenericBlockValueType.GENERIC, 1);

        register(PistonValue.DOWN, 0);
        register(PistonValue.UP, 1);
        register(PistonValue.SOUTH, 2);
        register(PistonValue.WEST, 3);
        register(PistonValue.NORTH, 4);
        register(PistonValue.EAST, 5);

        register(SoundEffect.BLOCK_DISPENSER_DISPENSE, 1000);
        register(SoundEffect.BLOCK_DISPENSER_FAIL, 1001);
        register(SoundEffect.BLOCK_DISPENSER_LAUNCH, 1002);
        register(SoundEffect.ENTITY_ENDEREYE_LAUNCH, 1003);
        register(SoundEffect.ENTITY_FIREWORK_SHOOT, 1004);
        register(SoundEffect.BLOCK_IRON_DOOR_OPEN, 1005);
        register(SoundEffect.BLOCK_WOODEN_DOOR_OPEN, 1006);
        register(SoundEffect.BLOCK_WOODEN_TRAPDOOR_OPEN, 1007);
        register(SoundEffect.BLOCK_FENCE_GATE_OPEN, 1008);
        register(SoundEffect.BLOCK_FIRE_EXTINGUISH, 1009);
        register(SoundEffect.RECORD, 1010);
        register(SoundEffect.BLOCK_IRON_DOOR_CLOSE, 1011);
        register(SoundEffect.BLOCK_WOODEN_DOOR_CLOSE, 1012);
        register(SoundEffect.BLOCK_WOODEN_TRAPDOOR_CLOSE, 1013);
        register(SoundEffect.BLOCK_FENCE_GATE_CLOSE, 1014);
        register(SoundEffect.ENTITY_GHAST_WARN, 1015);
        register(SoundEffect.ENTITY_GHAST_SHOOT, 1016);
        register(SoundEffect.ENTITY_ENDERDRAGON_SHOOT, 1017);
        register(SoundEffect.ENTITY_BLAZE_SHOOT, 1018);
        register(SoundEffect.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, 1019);
        register(SoundEffect.ENTITY_ZOMBIE_ATTACK_DOOR_IRON, 1020);
        register(SoundEffect.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, 1021);
        register(SoundEffect.ENTITY_WITHER_BREAK_BLOCK, 1022);
        register(SoundEffect.ENTITY_WITHER_SPAWN, 1023);
        register(SoundEffect.ENTITY_WITHER_SHOOT, 1024);
        register(SoundEffect.ENTITY_BAT_TAKEOFF, 1025);
        register(SoundEffect.ENTITY_ZOMBIE_INFECT, 1026);
        register(SoundEffect.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 1027);
        register(SoundEffect.ENTITY_ENDERDRAGON_DEATH, 1028);
        register(SoundEffect.BLOCK_ANVIL_DESTROY, 1029);
        register(SoundEffect.BLOCK_ANVIL_USE, 1030);
        register(SoundEffect.BLOCK_ANVIL_LAND, 1031);
        register(SoundEffect.BLOCK_PORTAL_TRAVEL, 1032);
        register(SoundEffect.BLOCK_CHORUS_FLOWER_GROW, 1033);
        register(SoundEffect.BLOCK_CHORUS_FLOWER_DEATH, 1034);
        register(SoundEffect.BLOCK_BREWING_STAND_BREW, 1035);
        register(SoundEffect.BLOCK_IRON_TRAPDOOR_CLOSE, 1036);
        register(SoundEffect.BLOCK_IRON_TRAPDOOR_OPEN, 1037);
        register(SoundEffect.ENTITY_ENDERDRAGON_GROWL, 3001);

        register(ParticleEffect.SMOKE, 2000);
        register(ParticleEffect.BREAK_BLOCK, 2001);
        register(ParticleEffect.BREAK_SPLASH_POTION, 2002);
        register(ParticleEffect.BREAK_EYE_OF_ENDER, 2003);
        register(ParticleEffect.MOB_SPAWN, 2004);
        register(ParticleEffect.BONEMEAL_GROW, 2005);
        register(ParticleEffect.ENDERDRAGON_FIREBALL_EXPLODE, 2006);
        register(ParticleEffect.BREAK_SPLASH_POTION, 2007);
        register(ParticleEffect.END_GATEWAY_SPAWN, 3000);

        register(SmokeEffectData.SOUTH_EAST, 0);
        register(SmokeEffectData.SOUTH, 1);
        register(SmokeEffectData.SOUTH_WEST, 2);
        register(SmokeEffectData.EAST, 3);
        register(SmokeEffectData.UP, 4);
        register(SmokeEffectData.WEST, 5);
        register(SmokeEffectData.NORTH_EAST, 6);
        register(SmokeEffectData.NORTH, 7);
        register(SmokeEffectData.NORTH_WEST, 8);

        register(NameTagVisibility.ALWAYS, "");
        register(NameTagVisibility.ALWAYS, "always");
        register(NameTagVisibility.NEVER, "never");
        register(NameTagVisibility.HIDE_FOR_OTHER_TEAMS, "hideForOtherTeams");
        register(NameTagVisibility.HIDE_FOR_OWN_TEAM, "hideForOwnTeam");

        register(CollisionRule.ALWAYS, "");
        register(CollisionRule.ALWAYS, "always");
        register(CollisionRule.NEVER, "never");
        register(CollisionRule.PUSH_OTHER_TEAMS, "pushOtherTeams");
        register(CollisionRule.PUSH_OWN_TEAM, "pushOwnTeam");

        register(TeamColor.NONE, -1);
        register(TeamColor.BLACK, 0);
        register(TeamColor.DARK_BLUE, 1);
        register(TeamColor.DARK_GREEN, 2);
        register(TeamColor.DARK_AQUA, 3);
        register(TeamColor.DARK_RED, 4);
        register(TeamColor.DARK_PURPLE, 5);
        register(TeamColor.GOLD, 6);
        register(TeamColor.GRAY, 7);
        register(TeamColor.DARK_GRAY, 8);
        register(TeamColor.BLUE, 9);
        register(TeamColor.GREEN, 10);
        register(TeamColor.AQUA, 11);
        register(TeamColor.RED, 12);
        register(TeamColor.LIGHT_PURPLE, 13);
        register(TeamColor.YELLOW, 14);
        register(TeamColor.WHITE, 15);

        register(ScoreType.INTEGER, "integer");
        register(ScoreType.HEARTS, "hearts");

        register(Advancement.DisplayData.FrameType.TASK, 0);
        register(Advancement.DisplayData.FrameType.CHALLENGE, 1);
        register(Advancement.DisplayData.FrameType.GOAL, 2);

        register(WorldBorderAction.SET_SIZE, 0);
        register(WorldBorderAction.LERP_SIZE, 1);
        register(WorldBorderAction.SET_CENTER, 2);
        register(WorldBorderAction.INITIALIZE, 3);
        register(WorldBorderAction.SET_WARNING_TIME, 4);
        register(WorldBorderAction.SET_WARNING_BLOCKS, 5);

        register(PlayerListEntryAction.ADD_PLAYER, 0);
        register(PlayerListEntryAction.UPDATE_GAMEMODE, 1);
        register(PlayerListEntryAction.UPDATE_LATENCY, 2);
        register(PlayerListEntryAction.UPDATE_DISPLAY_NAME, 3);
        register(PlayerListEntryAction.REMOVE_PLAYER, 4);

        register(TitleAction.TITLE, 0);
        register(TitleAction.SUBTITLE, 1);
        register(TitleAction.ACTION_BAR, 2);
        register(TitleAction.TIMES, 3);
        register(TitleAction.CLEAR, 4);
        register(TitleAction.RESET, 5);

        register(UnlockRecipesAction.INIT, 0);
        register(UnlockRecipesAction.ADD, 1);
        register(UnlockRecipesAction.REMOVE, 2);

        register(CraftingBookDataType.DISPLAYED_RECIPE, 0);
        register(CraftingBookDataType.CRAFTING_BOOK_STATUS, 1);

        register(AdvancementTabAction.OPENED_TAB, 0);
        register(AdvancementTabAction.CLOSED_SCREEN, 1);

        register(ResourcePackStatus.SUCCESSFULLY_LOADED, 0);
        register(ResourcePackStatus.DECLINED, 1);
        register(ResourcePackStatus.FAILED_DOWNLOAD, 2);
        register(ResourcePackStatus.ACCEPTED, 3);

        register(Hand.MAIN_HAND, 0);
        register(Hand.OFF_HAND, 1);

        register(BossBarAction.ADD, 0);
        register(BossBarAction.REMOVE, 1);
        register(BossBarAction.UPDATE_HEALTH, 2);
        register(BossBarAction.UPDATE_TITLE, 3);
        register(BossBarAction.UPDATE_STYLE, 4);
        register(BossBarAction.UPDATE_FLAGS, 5);

        register(BossBarColor.PINK, 0);
        register(BossBarColor.CYAN, 1);
        register(BossBarColor.RED, 2);
        register(BossBarColor.LIME, 3);
        register(BossBarColor.YELLOW, 4);
        register(BossBarColor.PURPLE, 5);
        register(BossBarColor.WHITE, 6);

        register(BossBarDivision.NONE, 0);
        register(BossBarDivision.NOTCHES_6, 1);
        register(BossBarDivision.NOTCHES_10, 2);
        register(BossBarDivision.NOTCHES_12, 3);
        register(BossBarDivision.NOTCHES_20, 4);

        register(BlockFace.DOWN, 0);
        register(BlockFace.UP, 1);
        register(BlockFace.NORTH, 2);
        register(BlockFace.SOUTH, 3);
        register(BlockFace.WEST, 4);
        register(BlockFace.EAST, 5);
        register(BlockFace.SPECIAL, 255);

        register(EquipmentSlot.MAIN_HAND, 0);
        register(EquipmentSlot.OFF_HAND, 1);
        register(EquipmentSlot.BOOTS, 2);
        register(EquipmentSlot.LEGGINGS, 3);
        register(EquipmentSlot.CHESTPLATE, 4);
        register(EquipmentSlot.HELMET, 5);

        register(SoundCategory.MASTER, 0);
        register(SoundCategory.MUSIC, 1);
        register(SoundCategory.RECORD, 2);
        register(SoundCategory.WEATHER, 3);
        register(SoundCategory.BLOCK, 4);
        register(SoundCategory.HOSTILE, 5);
        register(SoundCategory.NEUTRAL, 6);
        register(SoundCategory.PLAYER, 7);
        register(SoundCategory.AMBIENT, 8);
        register(SoundCategory.VOICE, 9);

        for(BuiltinSound sound : BuiltinSound.values()) {
            register(sound, sound.ordinal());
            register(sound, sound.name().toLowerCase().replace('_', '.'));
        }

        // Properly register sounds that actually have names containing underscores.
        register(BuiltinSound.BLOCK_BREWING_STAND_BREW, "block.brewing_stand.brew", true);
        register(BuiltinSound.BLOCK_CHORUS_FLOWER_DEATH, "block.chorus_flower.death", true);
        register(BuiltinSound.BLOCK_CHORUS_FLOWER_GROW, "block.chorus_flower.grow", true);
        register(BuiltinSound.BLOCK_ENCHANTMENT_TABLE_USE, "block.enchantment_table.use", true);
        register(BuiltinSound.BLOCK_END_GATEWAY_SPAWN, "block.end_gateway.spawn", true);
        register(BuiltinSound.BLOCK_END_PORTAL_SPAWN, "block.end_portal.spawn", true);
        register(BuiltinSound.BLOCK_END_PORTAL_FRAME_FILL, "block.end_portal_frame.fill", true);
        register(BuiltinSound.BLOCK_FENCE_GATE_CLOSE, "block.fence_gate.close", true);
        register(BuiltinSound.BLOCK_FENCE_GATE_OPEN, "block.fence_gate.open", true);
        register(BuiltinSound.BLOCK_FURNACE_FIRE_CRACKLE, "block.furnace.fire_crackle", true);
        register(BuiltinSound.BLOCK_IRON_DOOR_CLOSE, "block.iron_door.close", true);
        register(BuiltinSound.BLOCK_IRON_DOOR_OPEN, "block.iron_door.open", true);
        register(BuiltinSound.BLOCK_IRON_TRAPDOOR_CLOSE, "block.iron_trapdoor.close", true);
        register(BuiltinSound.BLOCK_IRON_TRAPDOOR_CLOSE, "block.iron_trapdoor.open", true);
        register(BuiltinSound.BLOCK_METAL_PRESSUREPLATE_CLICK_OFF, "block.metal_pressureplate.click_off", true);
        register(BuiltinSound.BLOCK_METAL_PRESSUREPLATE_CLICK_ON, "block.metal_pressureplate.click_on", true);
        register(BuiltinSound.BLOCK_REDSTONE_TORCH_BURNOUT, "block.redstone_torch.burnout", true);
        register(BuiltinSound.BLOCK_SHULKER_BOX_CLOSE, "block.shulker_box.close", true);
        register(BuiltinSound.BLOCK_SHULKER_BOX_OPEN, "block.shulker_box.open", true);
        register(BuiltinSound.BLOCK_STONE_BUTTON_CLICK_OFF, "block.stone_button.click_off", true);
        register(BuiltinSound.BLOCK_STONE_BUTTON_CLICK_ON, "block.stone_button.click_on", true);
        register(BuiltinSound.BLOCK_STONE_PRESSUREPLATE_CLICK_OFF, "block.stone_pressureplate.click_off", true);
        register(BuiltinSound.BLOCK_STONE_PRESSUREPLATE_CLICK_ON, "block.stone_pressureplate.click_on", true);
        register(BuiltinSound.BLOCK_TRIPWIRE_CLICK_OFF, "block.tripwire.click_off", true);
        register(BuiltinSound.BLOCK_TRIPWIRE_CLICK_ON, "block.tripwire.click_on", true);
        register(BuiltinSound.BLOCK_WOOD_BUTTON_CLICK_OFF, "block.wood_button.click_off", true);
        register(BuiltinSound.BLOCK_WOOD_BUTTON_CLICK_ON, "block.wood_button.click_on", true);
        register(BuiltinSound.BLOCK_WOOD_PRESSUREPLATE_CLICK_OFF, "block.wood_pressureplate.click_off", true);
        register(BuiltinSound.BLOCK_WOOD_PRESSUREPLATE_CLICK_ON, "block.wood_pressureplate.click_on", true);
        register(BuiltinSound.BLOCK_WOODEN_DOOR_CLOSE, "block.wooden_door.close", true);
        register(BuiltinSound.BLOCK_WOODEN_DOOR_OPEN, "block.wooden_door.open", true);
        register(BuiltinSound.BLOCK_WOODEN_TRAPDOOR_CLOSE, "block.wooden_trapdoor.close", true);
        register(BuiltinSound.BLOCK_WOODEN_TRAPDOOR_CLOSE, "block.wooden_trapdoor.open", true);
        register(BuiltinSound.ENTITY_ARROW_HIT_PLAYER, "entity.arrow.hit_player", true);
        register(BuiltinSound.ENTITY_BOAT_PADDLE_LAND, "entity.boat.paddle_land", true);
        register(BuiltinSound.ENTITY_BOAT_PADDLE_WATER, "entity.boat.paddle_water", true);
        register(BuiltinSound.ENTITY_ELDER_GUARDIAN_AMBIENT, "entity.elder_guardian.ambient", true);
        register(BuiltinSound.ENTITY_ELDER_GUARDIAN_AMBIENT_LAND, "entity.elder_guardian.ambient_land", true);
        register(BuiltinSound.ENTITY_ELDER_GUARDIAN_CURSE, "entity.elder_guardian.curse", true);
        register(BuiltinSound.ENTITY_ELDER_GUARDIAN_DEATH, "entity.elder_guardian.death", true);
        register(BuiltinSound.ENTITY_ELDER_GUARDIAN_DEATH_LAND, "entity.elder_guardian.death_land", true);
        register(BuiltinSound.ENTITY_ELDER_GUARDIAN_FLOP, "entity.elder_guardian.flop", true);
        register(BuiltinSound.ENTITY_ELDER_GUARDIAN_HURT, "entity.elder_guardian.hurt", true);
        register(BuiltinSound.ENTITY_ELDER_GUARDIAN_HURT_LAND, "entity.elder_guardian.hurt_land", true);
        register(BuiltinSound.ENTITY_ENDERDRAGON_FIREBALL_EXPLODE, "entity.enderdragon_fireball.explode", true);
        register(BuiltinSound.ENTITY_EVOCATION_FANGS_ATTACK, "entity.evocation_fangs.attack", true);
        register(BuiltinSound.ENTITY_EVOCATION_ILLAGER_AMBIENT, "entity.evocation_illager.ambient", true);
        register(BuiltinSound.ENTITY_EVOCATION_ILLAGER_CAST_SPELL, "entity.evocation_illager.cast_spell", true);
        register(BuiltinSound.ENTITY_EVOCATION_ILLAGER_DEATH, "entity.evocation_illager.death", true);
        register(BuiltinSound.ENTITY_EVOCATION_ILLAGER_HURT, "entity.evocation_illager.hurt", true);
        register(BuiltinSound.ENTITY_EVOCATION_ILLAGER_PREPARE_ATTACK, "entity.evocation_illager.prepare_attack", true);
        register(BuiltinSound.ENTITY_EVOCATION_ILLAGER_PREPARE_SUMMON, "entity.evocation_illager.prepare_summon", true);
        register(BuiltinSound.ENTITY_EVOCATION_ILLAGER_PREPARE_WOLOLO, "entity.evocation_illager.prepare_wololo", true);
        register(BuiltinSound.ENTITY_EXPERIENCE_BOTTLE_THROW, "entity.experience_bottle.throw", true);
        register(BuiltinSound.ENTITY_EXPERIENCE_ORB_PICKUP, "entity.experience_orb.pickup", true);
        register(BuiltinSound.ENTITY_FIREWORK_BLAST_FAR, "entity.firework.blast_far", true);
        register(BuiltinSound.ENTITY_FIREWORK_LARGE_BLAST, "entity.firework.large_blast", true);
        register(BuiltinSound.ENTITY_FIREWORK_LARGE_BLAST_FAR, "entity.firework.large_blast_far", true);
        register(BuiltinSound.ENTITY_FIREWORK_TWINKLE_FAR, "entity.firework.twinkle_far", true);
        register(BuiltinSound.ENTITY_GENERIC_BIG_FALL, "entity.generic.big_fall", true);
        register(BuiltinSound.ENTITY_GENERIC_EXTINGUISH_FIRE, "entity.generic.extinguish_fire", true);
        register(BuiltinSound.ENTITY_GENERIC_SMALL_FALL, "entity.generic.small_fall", true);
        register(BuiltinSound.ENTITY_GUARDIAN_AMBIENT_LAND, "entity.guardian.ambient_land", true);
        register(BuiltinSound.ENTITY_GUARDIAN_DEATH_LAND, "entity.guardian.death_land", true);
        register(BuiltinSound.ENTITY_GUARDIAN_HURT_LAND, "entity.guardian.hurt_land", true);
        register(BuiltinSound.ENTITY_HORSE_STEP_WOOD, "entity.horse.step_wood", true);
        register(BuiltinSound.ENTITY_HOSTILE_BIG_FALL, "entity.hostile.big_fall", true);
        register(BuiltinSound.ENTITY_HOSTILE_SMALL_FALL, "entity.hostile.small_fall", true);
        register(BuiltinSound.ENTITY_ILLUSION_ILLAGER_AMBIENT, "entity.illusion_illager.ambient", true);
        register(BuiltinSound.ENTITY_ILLUSION_ILLAGER_CAST_SPELL, "entity.illusion_illager.cast_spell", true);
        register(BuiltinSound.ENTITY_ILLUSION_ILLAGER_DEATH, "entity.illusion_illager.death", true);
        register(BuiltinSound.ENTITY_ILLUSION_ILLAGER_HURT, "entity.illusion_illager.hury", true);
        register(BuiltinSound.ENTITY_ILLUSION_ILLAGER_MIRROR_MOVE, "entity.illusion_illager.mirror_move", true);
        register(BuiltinSound.ENTITY_ILLUSION_ILLAGER_PREPARE_BLINDNESS, "entity.illusion_illager.prepare_blindness", true);
        register(BuiltinSound.ENTITY_ILLUSION_ILLAGER_PREPARE_MIRROR, "entity.illusion_illager.prepare_mirror", true);
        register(BuiltinSound.ENTITY_ITEMFRAME_ADD_ITEM, "entity.itemframe.add_item", true);
        register(BuiltinSound.ENTITY_ITEMFRAME_REMOVE_ITEM, "entity.itemframe.remove_item", true);
        register(BuiltinSound.ENTITY_ITEMFRAME_ROTATE_ITEM, "entity.itemframe.rotate_item", true);
        register(BuiltinSound.ENTITY_PARROT_IMITATE_ELDER_GUARDIAN, "entity.parrot.imitate.elder_guardian", true);
        register(BuiltinSound.ENTITY_PARROT_IMITATE_EVOCATION_ILLAGER, "entity.parrot.imitate.evocation_illager", true);
        register(BuiltinSound.ENTITY_PARROT_IMITATE_ILLUSION_ILLAGER, "entity.parrot.imitate.illusion_illager", true);
        register(BuiltinSound.ENTITY_PARROT_IMITATE_POLAR_BEAR, "entity.parrot.imitate.polar_bear", true);
        register(BuiltinSound.ENTITY_PARROT_IMITATE_VINDICATION_ILLAGER, "entity.parrot.imitate.vindication_illager", true);
        register(BuiltinSound.ENTITY_PARROT_IMITATE_WITHER_SKELETON, "entity.parrot.imitate.wither_skeleton", true);
        register(BuiltinSound.ENTITY_PARROT_IMITATE_ZOMBIE_PIGMAN, "entity.parrot.imitate.zombie_pigman", true);
        register(BuiltinSound.ENTITY_PARROT_IMITATE_ZOMBIE_VILLAGER, "entity.parrot.imitate.zombie_villager", true);
        register(BuiltinSound.ENTITY_PLAYER_BIG_FALL, "entity.player.big_fall", true);
        register(BuiltinSound.ENTITY_PLAYER_HURT_DROWN, "entity.player.hurt_drown", true);
        register(BuiltinSound.ENTITY_PLAYER_HURT_ON_FIRE, "entity.player.hurt_on_fire", true);
        register(BuiltinSound.ENTITY_PLAYER_SMALL_FALL, "entity.player.small_fall", true);
        register(BuiltinSound.ENTITY_POLAR_BEAR_AMBIENT, "entity.polar_bear.ambient", true);
        register(BuiltinSound.ENTITY_POLAR_BEAR_BABY_AMBIENT, "entity.polar_bear.baby_ambient", true);
        register(BuiltinSound.ENTITY_POLAR_BEAR_DEATH, "entity.polar_bear.death", true);
        register(BuiltinSound.ENTITY_POLAR_BEAR_HURT, "entity.polar_bear.hurt", true);
        register(BuiltinSound.ENTITY_POLAR_BEAR_STEP, "entity.polar_bear.step", true);
        register(BuiltinSound.ENTITY_POLAR_BEAR_WARNING, "entity.polar_bear.warning", true);
        register(BuiltinSound.ENTITY_SHULKER_HURT_CLOSED, "entity.shulker.hurt_closed", true);
        register(BuiltinSound.ENTITY_SHULKER_BULLET_HIT, "entity.shulker_bullet.hit", true);
        register(BuiltinSound.ENTITY_SHULKER_BULLET_HURT, "entity.shulker_bullet.hurt", true);
        register(BuiltinSound.ENTITY_SKELETON_HORSE_AMBIENT, "entity.skeleton_horse.ambient", true);
        register(BuiltinSound.ENTITY_SKELETON_HORSE_DEATH, "entity.skeleton_horse.death", true);
        register(BuiltinSound.ENTITY_SKELETON_HORSE_HURT, "entity.skeleton_horse.hurt", true);
        register(BuiltinSound.ENTITY_SMALL_MAGMACUBE_DEATH, "entity.small_magmacube.death", true);
        register(BuiltinSound.ENTITY_SMALL_MAGMACUBE_HURT, "entity.small_magmacube.hurt", true);
        register(BuiltinSound.ENTITY_SMALL_MAGMACUBE_SQUISH, "entity.small_magmacube.squish", true);
        register(BuiltinSound.ENTITY_SMALL_SLIME_DEATH, "entity.small_slime.death", true);
        register(BuiltinSound.ENTITY_SMALL_SLIME_HURT, "entity.small_slime.hurt", true);
        register(BuiltinSound.ENTITY_SMALL_SLIME_JUMP, "entity.small_slime.jump", true);
        register(BuiltinSound.ENTITY_SMALL_SLIME_SQUISH, "entity.small_slime.squish", true);
        register(BuiltinSound.ENTITY_SPLASH_POTION_BREAK, "entity.splash_potion.break", true);
        register(BuiltinSound.ENTITY_SPLASH_POTION_THROW, "entity.splash_potion.throw", true);
        register(BuiltinSound.ENTITY_VINDICATION_ILLAGER_AMBIENT, "entity.vindication_illager.ambient", true);
        register(BuiltinSound.ENTITY_VINDICATION_ILLAGER_DEATH, "entity.vindication_illager.death", true);
        register(BuiltinSound.ENTITY_VINDICATION_ILLAGER_HURT, "entity.vindication_illager.hurt", true);
        register(BuiltinSound.ENTITY_WITHER_BREAK_BLOCK, "entity.wither.break_block", true);
        register(BuiltinSound.ENTITY_WITHER_SKELETON_AMBIENT, "entity.wither_skeleton.ambient", true);
        register(BuiltinSound.ENTITY_WITHER_SKELETON_DEATH, "entity.wither_skeleton.death", true);
        register(BuiltinSound.ENTITY_WITHER_SKELETON_HURT, "entity.wither_skeleton.hurt", true);
        register(BuiltinSound.ENTITY_WITHER_SKELETON_STEP, "entity.wither_skeleton.step", true);
        register(BuiltinSound.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, "entity.zombie.attack_door_wood", true);
        register(BuiltinSound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, "entity.zombie.attack_iron_door", true);
        register(BuiltinSound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, "entity.zombie.break_door_wood", true);
        register(BuiltinSound.ENTITY_ZOMBIE_HORSE_AMBIENT, "entity.zombie_horse.ambient", true);
        register(BuiltinSound.ENTITY_ZOMBIE_HORSE_DEATH, "entity.zombie_horse.death", true);
        register(BuiltinSound.ENTITY_ZOMBIE_HORSE_HURT, "entity.zombie_horse.hurt", true);
        register(BuiltinSound.ENTITY_ZOMBIE_PIG_AMBIENT, "entity.zombie_pig.ambient", true);
        register(BuiltinSound.ENTITY_ZOMBIE_PIG_ANGRY, "entity.zombie_pig.angry", true);
        register(BuiltinSound.ENTITY_ZOMBIE_PIG_DEATH, "entity.zombie_pig.death", true);
        register(BuiltinSound.ENTITY_ZOMBIE_PIG_HURT, "entity.zombie_pig.hurt", true);
        register(BuiltinSound.ENTITY_ZOMBIE_VILLAGER_AMBIENT, "entity.zombie_villager.ambient", true);
        register(BuiltinSound.ENTITY_ZOMBIE_VILLAGER_CONVERTED, "entity.zombie_villager.converted", true);
        register(BuiltinSound.ENTITY_ZOMBIE_VILLAGER_CURE, "entity.zombie_villager.cure", true);
        register(BuiltinSound.ENTITY_ZOMBIE_VILLAGER_DEATH, "entity.zombie_villager.death", true);
        register(BuiltinSound.ENTITY_ZOMBIE_VILLAGER_HURT, "entity.zombie_villager.hurt", true);
        register(BuiltinSound.ENTITY_ZOMBIE_VILLAGER_STEP, "entity.zombie_villager.step", true);
        register(BuiltinSound.ITEM_ARMOR_EQUIP_CHAIN, "item.armor.equip_chain", true);
        register(BuiltinSound.ITEM_ARMOR_EQUIP_DIAMOND, "item.armor.equip_diamond", true);
        register(BuiltinSound.ITEM_ARMOR_EQUIP_ELYTRA, "item.armor.equip_elytra", true);
        register(BuiltinSound.ITEM_ARMOR_EQUIP_GENERIC, "item.armor.equip_generic", true);
        register(BuiltinSound.ITEM_ARMOR_EQUIP_GOLD, "item.armor.equip_gold", true);
        register(BuiltinSound.ITEM_ARMOR_EQUIP_IRON, "item.armor.equip_iron", true);
        register(BuiltinSound.ITEM_ARMOR_EQUIP_LEATHER, "item.armor.equip_leather", true);
        register(BuiltinSound.ITEM_BOTTLE_FILL_DRAGONBREATH, "item.bottle.fill_dragonbreath", true);
        register(BuiltinSound.ITEM_BUCKET_EMPTY_LAVA, "item.bucket.empty_lava", true);
        register(BuiltinSound.ITEM_BUCKET_FILL_LAVA, "item.bucket.fill_lava", true);
        register(BuiltinSound.ITEM_CHORUS_FRUIT_TELEPORT, "item.chorus_fruit.teleport", true);
        register(BuiltinSound.UI_TOAST_CHALLENGE_COMPLETE, "ui.toast.challenge_complete", true);
    }

    private static void register(Enum<?> key, Object value) {
        register(key, value, false);
    }

    private static void register(Enum<?> key, Object value, boolean overwrite) {
        if(!values.containsKey(key)) {
            values.put(key, new ArrayList<Object>());
        } else if(overwrite) {
            for(Iterator<Object> it = values.get(key).iterator(); it.hasNext(); ) {
                if(value.getClass().isAssignableFrom(it.next().getClass())) {
                    it.remove();
                }
            }
        }

        values.get(key).add(value);
    }

    @SuppressWarnings({"unchecked"})
    public static <T> T key(Class<T> keyType, Object value) {
        for(Object key : values.keySet()) {
            if(keyType.isAssignableFrom(key.getClass())) {
                for(Object val : values.get(key)) {
                    if(val == value || val.equals(value)) {
                        return (T) key;
                    } else if(Number.class.isAssignableFrom(val.getClass()) && Number.class.isAssignableFrom(value.getClass())) {
                        Number num = (Number) val;
                        Number num2 = (Number) value;
                        if(num.doubleValue() == num2.doubleValue()) {
                            return (T) key;
                        }
                    } else if(String.class.isAssignableFrom(val.getClass()) && String.class.isAssignableFrom(value.getClass())) {
                        String str = (String) val;
                        String str2 = (String) value;
                        if(str.equalsIgnoreCase(str2)) {
                            return (T) key;
                        }
                    }
                }
            }
        }

        throw new IllegalArgumentException("Value " + value + " has no mapping for key class " + keyType.getName() + ".");
    }

    @SuppressWarnings("unchecked")
    public static <T> T value(Class<T> valueType, Object key) {
        if(values.containsKey(key)) {
            for(Object val : values.get(key)) {
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
            }
        }

        throw new IllegalArgumentException("Key " + key + " has no mapping for value class " + valueType.getName() + ".");
    }
}
