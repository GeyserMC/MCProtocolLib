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
import com.github.steveice10.mc.protocol.data.game.entity.FeetOrEyes;
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
import com.github.steveice10.mc.protocol.data.game.statistic.StatisticCategory;
import com.github.steveice10.mc.protocol.data.game.window.AdvancementTabAction;
import com.github.steveice10.mc.protocol.data.game.window.ClickItemParam;
import com.github.steveice10.mc.protocol.data.game.window.CraftingBookDataType;
import com.github.steveice10.mc.protocol.data.game.window.CreativeGrabParam;
import com.github.steveice10.mc.protocol.data.game.window.DropItemParam;
import com.github.steveice10.mc.protocol.data.game.window.FillStackParam;
import com.github.steveice10.mc.protocol.data.game.window.MoveToHotbarParam;
import com.github.steveice10.mc.protocol.data.game.window.ShiftClickItemParam;
import com.github.steveice10.mc.protocol.data.game.window.SpreadItemParam;
import com.github.steveice10.mc.protocol.data.game.window.UpdateStructureBlockAction;
import com.github.steveice10.mc.protocol.data.game.window.UpdateStructureBlockMode;
import com.github.steveice10.mc.protocol.data.game.window.WindowAction;
import com.github.steveice10.mc.protocol.data.game.window.WindowType;
import com.github.steveice10.mc.protocol.data.game.window.property.AnvilProperty;
import com.github.steveice10.mc.protocol.data.game.window.property.BrewingStandProperty;
import com.github.steveice10.mc.protocol.data.game.window.property.EnchantmentTableProperty;
import com.github.steveice10.mc.protocol.data.game.window.property.FurnaceProperty;
import com.github.steveice10.mc.protocol.data.game.world.block.StructureMirror;
import com.github.steveice10.mc.protocol.data.game.world.block.StructureRotation;
import com.github.steveice10.mc.protocol.data.game.world.block.CommandBlockMode;
import com.github.steveice10.mc.protocol.data.game.world.block.value.*;
import com.github.steveice10.mc.protocol.data.game.world.particle.ParticleType;
import com.github.steveice10.mc.protocol.data.game.world.WorldBorderAction;
import com.github.steveice10.mc.protocol.data.game.world.WorldType;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockFace;
import com.github.steveice10.mc.protocol.data.game.world.block.UpdatedTileType;
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
        register(MetadataType.OPTIONAL_CHAT, 5);
        register(MetadataType.ITEM, 6);
        register(MetadataType.BOOLEAN, 7);
        register(MetadataType.ROTATION, 8);
        register(MetadataType.POSITION, 9);
        register(MetadataType.OPTIONAL_POSITION, 10);
        register(MetadataType.BLOCK_FACE, 11);
        register(MetadataType.OPTIONAL_UUID, 12);
        register(MetadataType.BLOCK_STATE, 13);
        register(MetadataType.NBT_TAG, 14);
        register(MetadataType.PARTICLE, 15);

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
        register(SpreadItemParam.MIDDLE_MOUSE_BEGIN_DRAG, 8);
        register(SpreadItemParam.MIDDLE_MOUSE_ADD_SLOT, 9);
        register(SpreadItemParam.MIDDLE_MOUSE_END_DRAG, 10);

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

        register(MobType.ARMOR_STAND, 1);
        register(MobType.BAT, 3);
        register(MobType.BLAZE, 4);
        register(MobType.CAVE_SPIDER, 6);
        register(MobType.CHICKEN, 7);
        register(MobType.COD, 8);
        register(MobType.COW, 9);
        register(MobType.CREEPER, 10);
        register(MobType.DONKEY, 11);
        register(MobType.DOLPHIN, 12);
        register(MobType.DROWNED, 14);
        register(MobType.ELDER_GUARDIAN, 15);
        register(MobType.ENDER_DRAGON, 17);
        register(MobType.ENDERMAN, 18);
        register(MobType.ENDERMITE, 19);
        register(MobType.EVOCATION_ILLAGER, 21);
        register(MobType.GHAST, 26);
        register(MobType.GIANT_ZOMBIE, 27);
        register(MobType.GUARDIAN, 28);
        register(MobType.HORSE, 29);
        register(MobType.HUSK, 30);
        register(MobType.ILLUSION_ILLAGER, 31);
        register(MobType.LLAMA, 36);
        register(MobType.MAGMA_CUBE, 38);
        register(MobType.MULE, 46);
        register(MobType.MOOSHROOM, 47);
        register(MobType.OCELOT, 48);
        register(MobType.PARROT, 50);
        register(MobType.PIG, 51);
        register(MobType.PUFFERFISH, 52);
        register(MobType.ZOMBIE_PIGMAN, 53);
        register(MobType.POLAR_BEAR, 54);
        register(MobType.RABBIT, 56);
        register(MobType.SALMON, 57);
        register(MobType.SHEEP, 58);
        register(MobType.SHULKER, 59);
        register(MobType.SILVERFISH, 61);
        register(MobType.SKELETON, 62);
        register(MobType.SKELETON_HORSE, 63);
        register(MobType.SLIME, 64);
        register(MobType.SNOWMAN, 66);
        register(MobType.SPIDER, 69);
        register(MobType.SQUID, 70);
        register(MobType.STRAY, 71);
        register(MobType.TROPICAL_FISH, 72);
        register(MobType.TURTLE, 73);
        register(MobType.VEX, 78);
        register(MobType.VILLAGER, 79);
        register(MobType.IRON_GOLEM, 80);
        register(MobType.VINDICATION_ILLAGER, 81);
        register(MobType.WITCH, 82);
        register(MobType.WITHER, 83);
        register(MobType.WITHER_SKELETON, 84);
        register(MobType.WOLF, 86);
        register(MobType.ZOMBIE, 87);
        register(MobType.ZOMBIE_HORSE, 88);
        register(MobType.ZOMBIE_VILLAGER, 89);
        register(MobType.PHANTOM, 90);

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
        register(ObjectType.TRIDENT, 94);

        register(MinecartType.NORMAL, 0);
        register(MinecartType.CHEST, 1);
        register(MinecartType.POWERED, 2);
        register(MinecartType.TNT, 3);
        register(MinecartType.MOB_SPAWNER, 4);
        register(MinecartType.HOPPER, 5);
        register(MinecartType.COMMAND_BLOCK, 6);

        register(HangingDirection.DOWN, 0);
        register(HangingDirection.UP, 1);
        register(HangingDirection.NORTH, 2);
        register(HangingDirection.SOUTH, 3);
        register(HangingDirection.WEST, 4);
        register(HangingDirection.EAST, 5);

        register(PaintingType.KEBAB, 0);
        register(PaintingType.AZTEC, 1);
        register(PaintingType.ALBAN, 2);
        register(PaintingType.AZTEC2, 3);
        register(PaintingType.BOMB, 4);
        register(PaintingType.PLANT, 5);
        register(PaintingType.WASTELAND, 6);
        register(PaintingType.POOL, 7);
        register(PaintingType.COURBET, 8);
        register(PaintingType.SEA, 9);
        register(PaintingType.SUNSET, 10);
        register(PaintingType.CREEBET, 11);
        register(PaintingType.WANDERER, 12);
        register(PaintingType.GRAHAM, 13);
        register(PaintingType.MATCH, 14);
        register(PaintingType.BUST, 15);
        register(PaintingType.STAGE, 16);
        register(PaintingType.VOID, 17);
        register(PaintingType.SKULL_AND_ROSES, 18);
        register(PaintingType.WITHER, 19);
        register(PaintingType.FIGHTERS, 20);
        register(PaintingType.POINTER, 21);
        register(PaintingType.PIG_SCENE, 22);
        register(PaintingType.BURNING_SKULL, 23);
        register(PaintingType.SKELETON, 24);
        register(PaintingType.DONKEY_KONG, 25);

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
        register(MapIconType.WHITE_BANNER, 10);
        register(MapIconType.ORANGE_BANNER, 11);
        register(MapIconType.MAGENTA_BANNER, 12);
        register(MapIconType.LIGHT_BLUE_BANNER, 13);
        register(MapIconType.YELLOW_BANNER, 14);
        register(MapIconType.LIME_BANNER, 15);
        register(MapIconType.PINK_BANNER, 16);
        register(MapIconType.GRAY_BANNER, 17);
        register(MapIconType.LIGHT_GRAY_BANNER, 18);
        register(MapIconType.CYAN_BANNER, 19);
        register(MapIconType.PURPLE_BANNER, 20);
        register(MapIconType.BLUE_BANNER, 21);
        register(MapIconType.BROWN_BANNER, 22);
        register(MapIconType.GREEN_BANNER, 23);
        register(MapIconType.RED_BANNER, 24);
        register(MapIconType.BLACK_BANNER, 25);
        register(MapIconType.TREASURE_MARKER, 26);

        for (int i = 0; i < WindowType.values().length; i++) {
            register(WindowType.values()[i], i);
        }

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
        register(UpdatedTileType.CONDUIT, 5);
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

        register(CommandBlockMode.SEQUENCE, 0);
        register(CommandBlockMode.AUTO, 1);
        register(CommandBlockMode.REDSTONE, 2);

        register(UpdateStructureBlockAction.UPDATE_DATA, 0);
        register(UpdateStructureBlockAction.SAVE_STRUCTURE, 1);
        register(UpdateStructureBlockAction.LOAD_STRUCTURE, 2);
        register(UpdateStructureBlockAction.DETECT_SIZE, 3);

        register(UpdateStructureBlockMode.SAVE, 0);
        register(UpdateStructureBlockMode.LOAD, 1);
        register(UpdateStructureBlockMode.CORNER, 2);
        register(UpdateStructureBlockMode.DATA, 3);

        register(StructureRotation.NONE, 0);
        register(StructureRotation.CLOCKWISE_90, 1);
        register(StructureRotation.CLOCKWISE_180, 2);
        register(StructureRotation.COUNTERCLOCKWISE_90, 3);

        register(StructureMirror.NONE, 0);
        register(StructureMirror.LEFT_RIGHT, 1);
        register(StructureMirror.FRONT_BACK, 2);

        register(DemoMessageValue.WELCOME, 0);
        register(DemoMessageValue.MOVEMENT_CONTROLS, 101);
        register(DemoMessageValue.JUMP_CONTROL, 102);
        register(DemoMessageValue.INVENTORY_CONTROL, 103);

        register(EnterCreditsValue.SEEN_BEFORE, 0);
        register(EnterCreditsValue.FIRST_TIME, 1);

        register(GenericStatistic.TIMES_LEFT_GAME, 0);
        register(GenericStatistic.TICKS_PLAYED, 1);
        register(GenericStatistic.TICKS_SINCE_DEATH, 2);
        register(GenericStatistic.TICKS_SNEAKED, 3);
        register(GenericStatistic.CENTIMETERS_WALKED, 4);
        register(GenericStatistic.CENTIMETERS_CROUCHED, 5);
        register(GenericStatistic.CENTIMETERS_SPRINTED, 6);
        register(GenericStatistic.CENTIMETERS_SWAM, 7);
        register(GenericStatistic.CENTIMETERS_FALLEN, 8);
        register(GenericStatistic.CENTIMETERS_CLIMBED, 9);
        register(GenericStatistic.CENTIMETERS_FLOWN, 10);
        register(GenericStatistic.CENTIMETERS_DOVE, 11);
        register(GenericStatistic.CENTIMETERS_TRAVELLED_IN_MINECART, 12);
        register(GenericStatistic.CENTIMETERS_TRAVELLED_IN_BOAT, 13);
        register(GenericStatistic.CENTIMETERS_RODE_ON_PIG, 14);
        register(GenericStatistic.CENTIMETERS_RODE_ON_HORSE, 15);
        register(GenericStatistic.CENTIMETERS_FLOWN_WITH_ELYTRA, 16);
        register(GenericStatistic.TIMES_JUMPED, 17);
        register(GenericStatistic.TIMES_DROPPED_ITEMS, 18);
        register(GenericStatistic.DAMAGE_DEALT, 19);
        register(GenericStatistic.DAMAGE_TAKEN, 20);
        register(GenericStatistic.DEATHS, 21);
        register(GenericStatistic.MOB_KILLS, 22);
        register(GenericStatistic.ANIMALS_BRED, 23);
        register(GenericStatistic.PLAYERS_KILLED, 24);
        register(GenericStatistic.FISH_CAUGHT, 25);
        register(GenericStatistic.TIMES_TALKED_TO_VILLAGERS, 26);
        register(GenericStatistic.TIMES_TRADED_WITH_VILLAGERS, 27);
        register(GenericStatistic.CAKE_SLICES_EATEN, 28);
        register(GenericStatistic.TIMES_CAULDRON_FILLED, 29);
        register(GenericStatistic.TIMES_CAULDRON_USED, 30);
        register(GenericStatistic.TIMES_ARMOR_CLEANED, 31);
        register(GenericStatistic.TIMES_BANNER_CLEANED, 32);
        register(GenericStatistic.TIMES_BREWING_STAND_GUI_OPENED, 33);
        register(GenericStatistic.TIMES_BEACON_GUI_OPENED, 34);
        register(GenericStatistic.TIMES_DROPPER_GUI_OPENED, 35);
        register(GenericStatistic.TIMES_HOPPER_GUI_OPENED, 36);
        register(GenericStatistic.TIMES_DISPENSER_GUI_OPENED, 37);
        register(GenericStatistic.TIMES_NOTEBLOCK_PLAYED, 38);
        register(GenericStatistic.TIMES_NOTEBLOCK_TUNED, 39);
        register(GenericStatistic.TIMES_PLANT_POTTED, 40);
        register(GenericStatistic.TIMES_TRAPPED_CHEST_GUI_OPENED, 41);
        register(GenericStatistic.TIMES_ENDER_CHEST_GUI_OPENED, 42);
        register(GenericStatistic.TIMES_ENCHANTED_ITEMS, 43);
        register(GenericStatistic.TIMES_RECORD_PLAYED, 44);
        register(GenericStatistic.TIMES_FURNACE_GUI_OPENED, 45);
        register(GenericStatistic.TIMES_CRAFTING_TABLE_GUI_OPENED, 46);
        register(GenericStatistic.TIMES_CHEST_GUI_OPENED, 47);
        register(GenericStatistic.TIMES_BED_ENTERED, 48);
        register(GenericStatistic.TIMES_SHULKER_BOX_GUI_OPENED, 49);

        register(StatisticCategory.BREAK_BLOCK, 0);
        register(StatisticCategory.CRAFT_ITEM, 1);
        register(StatisticCategory.USE_ITEM, 2);
        register(StatisticCategory.BREAK_ITEM, 3);
        register(StatisticCategory.PICKED_UP_ITEM, 4);
        register(StatisticCategory.DROP_ITEM, 5);
        register(StatisticCategory.KILL_ENTITY, 6);
        register(StatisticCategory.KILLED_BY_ENTITY, 7);
        register(StatisticCategory.GENERIC, 8);

        register(ParticleType.AMBIENT_ENTITY_EFFECT, 0);
        register(ParticleType.ANGRY_VILLAGER, 1);
        register(ParticleType.BARRIER, 2);
        register(ParticleType.BLOCK, 3);
        register(ParticleType.BUBBLE, 4);
        register(ParticleType.CLOUD, 5);
        register(ParticleType.CRIT, 6);
        register(ParticleType.DAMAGE_INDICATOR, 7);
        register(ParticleType.DRAGON_BREATH, 8);
        register(ParticleType.DRIPPING_LAVA, 9);
        register(ParticleType.DRIPPING_WATER, 10);
        register(ParticleType.DUST, 11);
        register(ParticleType.EFFECT, 12);
        register(ParticleType.ELDER_GUARDIAN, 13);
        register(ParticleType.ENCHANTED_HIT, 14);
        register(ParticleType.ENCHANT, 15);
        register(ParticleType.END_ROD, 16);
        register(ParticleType.ENTITY_EFFECT, 17);
        register(ParticleType.EXPLOSION_EMITTER, 18);
        register(ParticleType.EXPLOSION, 19);
        register(ParticleType.FALLING_DUST, 20);
        register(ParticleType.FIREWORK, 21);
        register(ParticleType.FISHING, 22);
        register(ParticleType.FLAME, 23);
        register(ParticleType.HAPPY_VILLAGER, 24);
        register(ParticleType.HEART, 25);
        register(ParticleType.INSTANT_EFFECT, 26);
        register(ParticleType.ITEM, 27);
        register(ParticleType.ITEM_SLIME, 28);
        register(ParticleType.ITEM_SNOWBALL, 29);
        register(ParticleType.LARGE_SMOKE, 30);
        register(ParticleType.LAVA, 31);
        register(ParticleType.MYCELIUM, 32);
        register(ParticleType.NOTE, 33);
        register(ParticleType.POOF, 34);
        register(ParticleType.PORTAL, 35);
        register(ParticleType.RAIN, 36);
        register(ParticleType.SMOKE, 37);
        register(ParticleType.SPIT, 38);
        register(ParticleType.SQUID_INK, 39);
        register(ParticleType.SWEEP_ATTACK, 40);
        register(ParticleType.TOTEM_OF_UNDYING, 41);
        register(ParticleType.UNDERWATER, 42);
        register(ParticleType.SPLASH, 43);
        register(ParticleType.WITCH, 44);
        register(ParticleType.BUBBLE_POP, 45);
        register(ParticleType.CURRENT_DOWN, 46);
        register(ParticleType.BUBBLE_COLUMN_UP, 47);
        register(ParticleType.NAUTILUS, 48);
        register(ParticleType.DOLPHIN, 49);

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

        register(BeaconValueType.RECALCULATE_BEAM, 1);

        register(EndGatewayValueType.TRIGGER_BEAM, 1);

        register(GenericBlockValueType.GENERIC_0, 0);
        register(GenericBlockValueType.GENERIC_1, 1);

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
        register(TeamColor.OBFUSCATED, 16);
        register(TeamColor.BOLD, 17);
        register(TeamColor.STRIKETHROUGH, 18);
        register(TeamColor.UNDERLINED, 19);
        register(TeamColor.ITALIC, 20);
        register(TeamColor.NONE, 21);

        register(ScoreType.INTEGER, 0);
        register(ScoreType.HEARTS, 1);

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

        register(FeetOrEyes.FEET, 0);
        register(FeetOrEyes.EYES, 1);

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
        register(BuiltinSound.AMBIENT_UNDERWATER_LOOP_ADDITIONS_ULTRA_RARE, "ambient.underwater.loop.additions.ultra_rare", true);
        register(BuiltinSound.BLOCK_BEACON_POWER_SELECT, "block.beacon.power_select", true);
        register(BuiltinSound.BLOCK_BREWING_STAND_BREW, "block.brewing_stand.brew", true);
        register(BuiltinSound.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, "block.bubble_column.bubble_pop", true);
        register(BuiltinSound.BLOCK_BUBBLE_COLUMN_UPWARDS_AMBIENT, "block.bubble_column.upwards_ambient", true);
        register(BuiltinSound.BLOCK_BUBBLE_COLUMN_UPWARDS_INSIDE, "block.bubble_column.upwards_inside", true);
        register(BuiltinSound.BLOCK_BUBBLE_COLUMN_WHIRLPOOL_AMBIENT, "block.bubble_column.whirlpool_ambient", true);
        register(BuiltinSound.BLOCK_BUBBLE_COLUMN_WHIRLPOOL_INSIDE, "block.bubble_column.whirlpool_inside", true);
        register(BuiltinSound.BLOCK_CHORUS_FLOWER_DEATH, "block.chorus_flower.death", true);
        register(BuiltinSound.BLOCK_CHORUS_FLOWER_GROW, "block.chorus_flower.grow", true);
        register(BuiltinSound.BLOCK_CORAL_BLOCK_BREAK, "block.coral_block.break", true);
        register(BuiltinSound.BLOCK_CORAL_BLOCK_FALL, "block.coral_block.fall", true);
        register(BuiltinSound.BLOCK_CORAL_BLOCK_HIT, "block.coral_block.hit", true);
        register(BuiltinSound.BLOCK_CORAL_BLOCK_PLACE, "block.coral_block.place", true);
        register(BuiltinSound.BLOCK_CORAL_BLOCK_STEP, "block.coral_block.step", true);
        register(BuiltinSound.BLOCK_ENCHANTMENT_TABLE_USE, "block.enchantment_table.use", true);
        register(BuiltinSound.BLOCK_END_GATEWAY_SPAWN, "block.end_gateway.spawn", true);
        register(BuiltinSound.BLOCK_END_PORTAL_SPAWN, "block.end_portal.spawn", true);
        register(BuiltinSound.BLOCK_END_PORTAL_FRAME_FILL, "block.end_portal_frame.fill", true);
        register(BuiltinSound.BLOCK_ENDER_CHEST_CLOSE, "block.ender_chest.close", true);
        register(BuiltinSound.BLOCK_ENDER_CHEST_OPEN, "block.ender_chest.open", true);
        register(BuiltinSound.BLOCK_FENCE_GATE_CLOSE, "block.fence_gate.close", true);
        register(BuiltinSound.BLOCK_FENCE_GATE_OPEN, "block.fence_gate.open", true);
        register(BuiltinSound.BLOCK_FURNACE_FIRE_CRACKLE, "block.furnace.fire_crackle", true);
        register(BuiltinSound.BLOCK_IRON_DOOR_CLOSE, "block.iron_door.close", true);
        register(BuiltinSound.BLOCK_IRON_DOOR_OPEN, "block.iron_door.open", true);
        register(BuiltinSound.BLOCK_IRON_TRAPDOOR_CLOSE, "block.iron_trapdoor.close", true);
        register(BuiltinSound.BLOCK_IRON_TRAPDOOR_CLOSE, "block.iron_trapdoor.open", true);
        register(BuiltinSound.BLOCK_LILY_PAD_PLACE, "block.lily_pad.place", true);
        register(BuiltinSound.BLOCK_METAL_PRESSURE_PLATE_CLICK_OFF, "block.metal_pressure_plate.click_off", true);
        register(BuiltinSound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON, "block.metal_pressure_plate.click_on", true);
        register(BuiltinSound.BLOCK_NOTE_BLOCK_BASEDRUM, "block.note_block.basedrum", true);
        register(BuiltinSound.BLOCK_NOTE_BLOCK_BASS, "block.note_block.bass", true);
        register(BuiltinSound.BLOCK_NOTE_BLOCK_BELL, "block.note_block.bell", true);
        register(BuiltinSound.BLOCK_NOTE_BLOCK_CHIME, "block.note_block.chime", true);
        register(BuiltinSound.BLOCK_NOTE_BLOCK_FLUTE, "block.note_block.flute", true);
        register(BuiltinSound.BLOCK_NOTE_BLOCK_GUITAR, "block.note_block.guitar", true);
        register(BuiltinSound.BLOCK_NOTE_BLOCK_HARP, "block.note_block.harp", true);
        register(BuiltinSound.BLOCK_NOTE_BLOCK_HAT, "block.note_block.hat", true);
        register(BuiltinSound.BLOCK_NOTE_BLOCK_PLING, "block.note_block.pling", true);
        register(BuiltinSound.BLOCK_NOTE_BLOCK_SNARE, "block.note_block.snare", true);
        register(BuiltinSound.BLOCK_NOTE_BLOCK_XYLOPHONE, "block.note_block.xylophone", true);
        register(BuiltinSound.BLOCK_REDSTONE_TORCH_BURNOUT, "block.redstone_torch.burnout", true);
        register(BuiltinSound.BLOCK_SHULKER_BOX_CLOSE, "block.shulker_box.close", true);
        register(BuiltinSound.BLOCK_SHULKER_BOX_OPEN, "block.shulker_box.open", true);
        register(BuiltinSound.BLOCK_SLIME_BLOCK_BREAK, "block.slime_block.break", true);
        register(BuiltinSound.BLOCK_SLIME_BLOCK_FALL, "block.slime_block.fall", true);
        register(BuiltinSound.BLOCK_SLIME_BLOCK_HIT, "block.slime_block.hit", true);
        register(BuiltinSound.BLOCK_SLIME_BLOCK_PLACE, "block.slime_block.place", true);
        register(BuiltinSound.BLOCK_SLIME_BLOCK_STEP, "block.slime_block.step", true);
        register(BuiltinSound.BLOCK_STONE_BUTTON_CLICK_OFF, "block.stone_button.click_off", true);
        register(BuiltinSound.BLOCK_STONE_BUTTON_CLICK_ON, "block.stone_button.click_on", true);
        register(BuiltinSound.BLOCK_STONE_PRESSURE_PLATE_CLICK_OFF, "block.stone_pressure_plate.click_off", true);
        register(BuiltinSound.BLOCK_STONE_PRESSURE_PLATE_CLICK_ON, "block.stone_pressure_plate.click_on", true);
        register(BuiltinSound.BLOCK_TRIPWIRE_CLICK_OFF, "block.tripwire.click_off", true);
        register(BuiltinSound.BLOCK_TRIPWIRE_CLICK_ON, "block.tripwire.click_on", true);
        register(BuiltinSound.BLOCK_WET_GRASS_BREAK, "block.wet_grass.break", true);
        register(BuiltinSound.BLOCK_WET_GRASS_FALL, "block.wet_grass.fall", true);
        register(BuiltinSound.BLOCK_WET_GRASS_HIT, "block.wet_grass.hit", true);
        register(BuiltinSound.BLOCK_WET_GRASS_PLACE, "block.wet_grass.place", true);
        register(BuiltinSound.BLOCK_WET_GRASS_STEP, "block.wet_grass.step", true);
        register(BuiltinSound.BLOCK_WOODEN_BUTTON_CLICK_OFF, "block.wooden_button.click_off", true);
        register(BuiltinSound.BLOCK_WOODEN_BUTTON_CLICK_ON, "block.wooden_button.click_on", true);
        register(BuiltinSound.BLOCK_WOODEN_DOOR_CLOSE, "block.wooden_door.close", true);
        register(BuiltinSound.BLOCK_WOODEN_DOOR_OPEN, "block.wooden_door.open", true);
        register(BuiltinSound.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_OFF, "block.wooden_pressure_plate.click_off", true);
        register(BuiltinSound.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON, "block.wooden_pressure_plate.click_on", true);
        register(BuiltinSound.BLOCK_WOODEN_TRAPDOOR_CLOSE, "block.wooden_trapdoor.close", true);
        register(BuiltinSound.BLOCK_WOODEN_TRAPDOOR_CLOSE, "block.wooden_trapdoor.open", true);
        register(BuiltinSound.ENTITY_ARMOR_STAND_BREAK, "entity.armor_stand.break", true);
        register(BuiltinSound.ENTITY_ARMOR_STAND_FALL, "entity.armor_stand.fall", true);
        register(BuiltinSound.ENTITY_ARMOR_STAND_HIT, "entity.armor_stand.hit", true);
        register(BuiltinSound.ENTITY_ARMOR_STAND_PLACE, "entity.armor_stand.place", true);
        register(BuiltinSound.ENTITY_ARROW_HIT_PLAYER, "entity.arrow.hit_player", true);
        register(BuiltinSound.ENTITY_BOAT_PADDLE_LAND, "entity.boat.paddle_land", true);
        register(BuiltinSound.ENTITY_BOAT_PADDLE_WATER, "entity.boat.paddle_water", true);
        register(BuiltinSound.ENTITY_DOLPHIN_AMBIENT_WATER, "entity.dolphin.ambient_water", true);
        register(BuiltinSound.ENTITY_DRAGON_FIREBALL_EXPLODE, "entity.dragon_fireball.explode", true);
        register(BuiltinSound.ENTITY_DROWNED_AMBIENT_WATER, "entity.drowned.ambient_water", true);
        register(BuiltinSound.ENTITY_DROWNED_DEATH_WATER, "entity.drowned.death_water", true);
        register(BuiltinSound.ENTITY_DROWNED_HURT_WATER, "entity.drowned.hurt_water", true);
        register(BuiltinSound.ENTITY_ELDER_GUARDIAN_AMBIENT, "entity.elder_guardian.ambient", true);
        register(BuiltinSound.ENTITY_ELDER_GUARDIAN_AMBIENT_LAND, "entity.elder_guardian.ambient_land", true);
        register(BuiltinSound.ENTITY_ELDER_GUARDIAN_CURSE, "entity.elder_guardian.curse", true);
        register(BuiltinSound.ENTITY_ELDER_GUARDIAN_DEATH, "entity.elder_guardian.death", true);
        register(BuiltinSound.ENTITY_ELDER_GUARDIAN_DEATH_LAND, "entity.elder_guardian.death_land", true);
        register(BuiltinSound.ENTITY_ELDER_GUARDIAN_FLOP, "entity.elder_guardian.flop", true);
        register(BuiltinSound.ENTITY_ELDER_GUARDIAN_HURT, "entity.elder_guardian.hurt", true);
        register(BuiltinSound.ENTITY_ELDER_GUARDIAN_HURT_LAND, "entity.elder_guardian.hurt_land", true);
        register(BuiltinSound.ENTITY_ENDER_DRAGON_AMBIENT, "entity.ender_dragon.ambient", true);
        register(BuiltinSound.ENTITY_ENDER_DRAGON_DEATH, "entity.ender_dragon.death", true);
        register(BuiltinSound.ENTITY_ENDER_DRAGON_FLAP, "entity.ender_dragon.flap", true);
        register(BuiltinSound.ENTITY_ENDER_DRAGON_GROWL, "entity.ender_dragon.growl", true);
        register(BuiltinSound.ENTITY_ENDER_DRAGON_HURT, "entity.ender_dragon.hurt", true);
        register(BuiltinSound.ENTITY_ENDER_DRAGON_SHOOT, "entity.ender_dragon.shoot", true);
        register(BuiltinSound.ENTITY_ENDER_EYE_DEATH, "entity.ender_eye.death", true);
        register(BuiltinSound.ENTITY_ENDER_EYE_LAUNCH, "entity.ender_eye.launch", true);
        register(BuiltinSound.ENTITY_ENDER_PEARL_THROW, "entity.ender_pearl.throw", true);
        register(BuiltinSound.ENTITY_EVOKER_CAST_SPELL, "entity.evoker.cast_spell", true);
        register(BuiltinSound.ENTITY_EVOKER_PREPARE_ATTACK, "entity.evoker.prepare_attack", true);
        register(BuiltinSound.ENTITY_EVOKER_PREPARE_SUMMON, "entity.evoker.prepare_summon", true);
        register(BuiltinSound.ENTITY_EVOKER_PREPARE_WOLOLO, "entity.evoker.prepare_wololo", true);
        register(BuiltinSound.ENTITY_EVOKER_FANGS_ATTACK, "entity.evoker_fangs.attack", true);
        register(BuiltinSound.ENTITY_EXPERIENCE_BOTTLE_THROW, "entity.experience_bottle.throw", true);
        register(BuiltinSound.ENTITY_EXPERIENCE_ORB_PICKUP, "entity.experience_orb.pickup", true);
        register(BuiltinSound.ENTITY_FIREWORK_ROCKET_BLAST, "entity.firework_rocket.blast", true);
        register(BuiltinSound.ENTITY_FIREWORK_ROCKET_BLAST_FAR, "entity.firework_rocket.blast_far", true);
        register(BuiltinSound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, "entity.firework_rocket.large_blast", true);
        register(BuiltinSound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST_FAR, "entity.firework_rocket.large_blast_far", true);
        register(BuiltinSound.ENTITY_FIREWORK_ROCKET_LAUNCH, "entity.firework_rocket.launch", true);
        register(BuiltinSound.ENTITY_FIREWORK_ROCKET_SHOOT, "entity.firework_rocket.shoot", true);
        register(BuiltinSound.ENTITY_FIREWORK_ROCKET_TWINKLE, "entity.firework_rocket.twinkle", true);
        register(BuiltinSound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, "entity.firework_rocket.twinkle_far", true);
        register(BuiltinSound.ENTITY_FISHING_BOBBER_RETRIEVE, "entity.fishing_bobber.retrieve", true);
        register(BuiltinSound.ENTITY_FISHING_BOBBER_SPLASH, "entity.fishing_bobber.splash", true);
        register(BuiltinSound.ENTITY_FISHING_BOBBER_THROW, "entity.fishing_bobber.throw", true);
        register(BuiltinSound.ENTITY_GENERIC_BIG_FALL, "entity.generic.big_fall", true);
        register(BuiltinSound.ENTITY_GENERIC_EXTINGUISH_FIRE, "entity.generic.extinguish_fire", true);
        register(BuiltinSound.ENTITY_GENERIC_SMALL_FALL, "entity.generic.small_fall", true);
        register(BuiltinSound.ENTITY_GUARDIAN_AMBIENT_LAND, "entity.guardian.ambient_land", true);
        register(BuiltinSound.ENTITY_GUARDIAN_DEATH_LAND, "entity.guardian.death_land", true);
        register(BuiltinSound.ENTITY_GUARDIAN_HURT_LAND, "entity.guardian.hurt_land", true);
        register(BuiltinSound.ENTITY_HORSE_STEP_WOOD, "entity.horse.step_wood", true);
        register(BuiltinSound.ENTITY_HOSTILE_BIG_FALL, "entity.hostile.big_fall", true);
        register(BuiltinSound.ENTITY_HOSTILE_SMALL_FALL, "entity.hostile.small_fall", true);
        register(BuiltinSound.ENTITY_HUSK_CONVERTED_TO_ZOMBIE, "entity.husk.converted_to_zombie", true);
        register(BuiltinSound.ENTITY_ILLUSIONER_CAST_SPELL, "entity.illusioner.cast_spell", true);
        register(BuiltinSound.ENTITY_ILLUSIONER_MIRROR_MOVE, "entity.illusioner.mirror_move", true);
        register(BuiltinSound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, "entity.illusioner.prepare_blindness", true);
        register(BuiltinSound.ENTITY_ILLUSIONER_PREPARE_MIRROR, "entity.illusioner.prepare_mirror", true);
        register(BuiltinSound.ENTITY_IRON_GOLEM_ATTACK, "entity.iron_golem.attack", true);
        register(BuiltinSound.ENTITY_IRON_GOLEM_DEATH, "entity.iron_golem.death", true);
        register(BuiltinSound.ENTITY_IRON_GOLEM_HURT, "entity.iron_golem.hurt", true);
        register(BuiltinSound.ENTITY_IRON_GOLEM_STEP, "entity.iron_golem.step", true);
        register(BuiltinSound.ENTITY_ITEM_FRAME_ADD_ITEM, "entity.item_frame.add_item", true);
        register(BuiltinSound.ENTITY_ITEM_FRAME_BREAK, "entity.item_frame.break", true);
        register(BuiltinSound.ENTITY_ITEM_FRAME_PLACE, "entity.item_frame.place", true);
        register(BuiltinSound.ENTITY_ITEM_FRAME_REMOVE_ITEM, "entity.item_frame.remove_item", true);
        register(BuiltinSound.ENTITY_ITEM_FRAME_ROTATE_ITEM, "entity.item_frame.rotate_item", true);
        register(BuiltinSound.ENTITY_LEASH_KNOT_BREAK, "entity.leash_knot.break", true);
        register(BuiltinSound.ENTITY_LEASH_KNOT_PLACE, "entity.leash_knot.place", true);
        register(BuiltinSound.ENTITY_LIGHTNING_BOLT_IMPACT, "entity.lightning_bolt.impact", true);
        register(BuiltinSound.ENTITY_LIGHTNING_BOLT_THUNDER, "entity.lightning_bolt.thunder", true);
        register(BuiltinSound.ENTITY_LINGERING_POTION_THROW, "entity.lingering_potion.throw", true);
        register(BuiltinSound.ENTITY_MAGMA_CUBE_DEATH, "entity.magma_cube.death", true);
        register(BuiltinSound.ENTITY_MAGMA_CUBE_DEATH_SMALL, "entity.magma_cube.death_small", true);
        register(BuiltinSound.ENTITY_MAGMA_CUBE_HURT, "entity.magma_cube.hurt", true);
        register(BuiltinSound.ENTITY_MAGMA_CUBE_HURT_SMALL, "entity.magma_cube.hurt_small", true);
        register(BuiltinSound.ENTITY_MAGMA_CUBE_JUMP, "entity.magma_cube.jump", true);
        register(BuiltinSound.ENTITY_MAGMA_CUBE_SQUISH, "entity.magma_cube.squish", true);
        register(BuiltinSound.ENTITY_MAGMA_CUBE_SQUISH_SMALL, "entity.magma_cube.squish_small", true);
        register(BuiltinSound.ENTITY_PARROT_IMITATE_ELDER_GUARDIAN, "entity.parrot.imitate.elder_guardian", true);
        register(BuiltinSound.ENTITY_PARROT_IMITATE_ENDER_DRAGON, "entity.parrot.imitate.ender_dragon", true);
        register(BuiltinSound.ENTITY_PARROT_IMITATE_MAGMA_CUBE, "entity.parrot.imitate.magma_cube", true);
        register(BuiltinSound.ENTITY_PARROT_IMITATE_POLAR_BEAR, "entity.parrot.imitate.polar_bear", true);
        register(BuiltinSound.ENTITY_PARROT_IMITATE_WITHER_SKELETON, "entity.parrot.imitate.wither_skeleton", true);
        register(BuiltinSound.ENTITY_PARROT_IMITATE_ZOMBIE_PIGMAN, "entity.parrot.imitate.zombie_pigman", true);
        register(BuiltinSound.ENTITY_PARROT_IMITATE_ZOMBIE_VILLAGER, "entity.parrot.imitate.zombie_villager", true);
        register(BuiltinSound.ENTITY_PLAYER_BIG_FALL, "entity.player.big_fall", true);
        register(BuiltinSound.ENTITY_PLAYER_HURT_DROWN, "entity.player.hurt_drown", true);
        register(BuiltinSound.ENTITY_PLAYER_HURT_ON_FIRE, "entity.player.hurt_on_fire", true);
        register(BuiltinSound.ENTITY_PLAYER_SMALL_FALL, "entity.player.small_fall", true);
        register(BuiltinSound.ENTITY_PLAYER_SPLASH_HIGH_SPEED, "entity.player.splash.high_speed", true);
        register(BuiltinSound.ENTITY_POLAR_BEAR_AMBIENT, "entity.polar_bear.ambient", true);
        register(BuiltinSound.ENTITY_POLAR_BEAR_AMBIENT_BABY, "entity.polar_bear.ambient_baby", true);
        register(BuiltinSound.ENTITY_POLAR_BEAR_DEATH, "entity.polar_bear.death", true);
        register(BuiltinSound.ENTITY_POLAR_BEAR_HURT, "entity.polar_bear.hurt", true);
        register(BuiltinSound.ENTITY_POLAR_BEAR_STEP, "entity.polar_bear.step", true);
        register(BuiltinSound.ENTITY_POLAR_BEAR_WARNING, "entity.polar_bear.warning", true);
        register(BuiltinSound.ENTITY_PUFFER_FISH_AMBIENT, "entity.puffer_fish.ambient", true);
        register(BuiltinSound.ENTITY_PUFFER_FISH_BLOW_OUT, "entity.puffer_fish.blow_out", true);
        register(BuiltinSound.ENTITY_PUFFER_FISH_BLOW_UP, "entity.puffer_fish.blow_up", true);
        register(BuiltinSound.ENTITY_PUFFER_FISH_DEATH, "entity.puffer_fish.death", true);
        register(BuiltinSound.ENTITY_PUFFER_FISH_FLOP, "entity.puffer_fish.flop", true);
        register(BuiltinSound.ENTITY_PUFFER_FISH_HURT, "entity.puffer_fish.hurt", true);
        register(BuiltinSound.ENTITY_PUFFER_FISH_STING, "entity.puffer_fish.sting", true);
        register(BuiltinSound.ENTITY_SHULKER_HURT_CLOSED, "entity.shulker.hurt_closed", true);
        register(BuiltinSound.ENTITY_SHULKER_BULLET_HIT, "entity.shulker_bullet.hit", true);
        register(BuiltinSound.ENTITY_SHULKER_BULLET_HURT, "entity.shulker_bullet.hurt", true);
        register(BuiltinSound.ENTITY_SKELETON_HORSE_AMBIENT, "entity.skeleton_horse.ambient", true);
        register(BuiltinSound.ENTITY_SKELETON_HORSE_AMBIENT_WATER, "entity.skeleton_horse.ambient_water", true);
        register(BuiltinSound.ENTITY_SKELETON_HORSE_DEATH, "entity.skeleton_horse.death", true);
        register(BuiltinSound.ENTITY_SKELETON_HORSE_GALLOP_WATER, "entity.skeleton_horse.gallop_water", true);
        register(BuiltinSound.ENTITY_SKELETON_HORSE_HURT, "entity.skeleton_horse.hurt", true);
        register(BuiltinSound.ENTITY_SKELETON_HORSE_JUMP_WATER, "entity.skeleton_horse.jump_water", true);
        register(BuiltinSound.ENTITY_SKELETON_HORSE_STEP_WATER, "entity.skeleton_horse.step_water", true);
        register(BuiltinSound.ENTITY_SKELETON_HORSE_SWIM, "entity.skeleton_horse.swim", true);
        register(BuiltinSound.ENTITY_SLIME_DEATH_SMALL, "entity.slime.death_small", true);
        register(BuiltinSound.ENTITY_SLIME_HURT_SMALL, "entity.slime.hurt_small", true);
        register(BuiltinSound.ENTITY_SLIME_JUMP_SMALL, "entity.slime.jump_small", true);
        register(BuiltinSound.ENTITY_SLIME_SQUISH_SMALL, "entity.slime.squish_small", true);
        register(BuiltinSound.ENTITY_SNOW_GOLEM_AMBIENT, "entity.snow_golem.ambient", true);
        register(BuiltinSound.ENTITY_SNOW_GOLEM_DEATH, "entity.snow_golem.death", true);
        register(BuiltinSound.ENTITY_SNOW_GOLEM_HURT, "entity.snow_golem.hurt", true);
        register(BuiltinSound.ENTITY_SNOW_GOLEM_SHOOT, "entity.snow_golem.shoot", true);
        register(BuiltinSound.ENTITY_SPLASH_POTION_BREAK, "entity.splash_potion.break", true);
        register(BuiltinSound.ENTITY_SPLASH_POTION_THROW, "entity.splash_potion.throw", true);
        register(BuiltinSound.ENTITY_TROPICAL_FISH_AMBIENT, "entity.tropical_fish.ambient", true);
        register(BuiltinSound.ENTITY_TROPICAL_FISH_DEATH, "entity.tropical_fish.death", true);
        register(BuiltinSound.ENTITY_TROPICAL_FISH_FLOP, "entity.tropical_fish.flop", true);
        register(BuiltinSound.ENTITY_TROPICAL_FISH_HURT, "entity.tropical_fish.hurt", true);
        register(BuiltinSound.ENTITY_TURTLE_AMBIENT_LAND, "entity.turtle.ambient_land", true);
        register(BuiltinSound.ENTITY_TURTLE_DEATH_BABY, "entity.turtle.death_baby", true);
        register(BuiltinSound.ENTITY_TURTLE_EGG_BREAK, "entity.turtle.egg_break", true);
        register(BuiltinSound.ENTITY_TURTLE_EGG_CRACK, "entity.turtle.egg_crack", true);
        register(BuiltinSound.ENTITY_TURTLE_EGG_HATCH, "entity.turtle.egg_hatch", true);
        register(BuiltinSound.ENTITY_TURTLE_HURT_BABY, "entity.turtle.hurt_baby", true);
        register(BuiltinSound.ENTITY_TURTLE_LAY_EGG, "entity.turtle.lay_egg", true);
        register(BuiltinSound.ENTITY_TURTLE_SHAMBLE_BABY, "entity.turtle.shamble_baby", true);
        register(BuiltinSound.ENTITY_WITHER_BREAK_BLOCK, "entity.wither.break_block", true);
        register(BuiltinSound.ENTITY_WITHER_SKELETON_AMBIENT, "entity.wither_skeleton.ambient", true);
        register(BuiltinSound.ENTITY_WITHER_SKELETON_DEATH, "entity.wither_skeleton.death", true);
        register(BuiltinSound.ENTITY_WITHER_SKELETON_HURT, "entity.wither_skeleton.hurt", true);
        register(BuiltinSound.ENTITY_WITHER_SKELETON_STEP, "entity.wither_skeleton.step", true);
        register(BuiltinSound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, "entity.zombie.attack_iron_door", true);
        register(BuiltinSound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, "entity.zombie.attack_wooden_door", true);
        register(BuiltinSound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, "entity.zombie.break_wooden_door", true);
        register(BuiltinSound.ENTITY_ZOMBIE_CONVERTED_TO_DROWNED, "entity.zombie.converted_to_drowned", true);
        register(BuiltinSound.ENTITY_ZOMBIE_DESTROY_EGG, "entity.zombie.destroy_egg", true);
        register(BuiltinSound.ENTITY_ZOMBIE_HORSE_AMBIENT, "entity.zombie_horse.ambient", true);
        register(BuiltinSound.ENTITY_ZOMBIE_HORSE_DEATH, "entity.zombie_horse.death", true);
        register(BuiltinSound.ENTITY_ZOMBIE_HORSE_HURT, "entity.zombie_horse.hurt", true);
        register(BuiltinSound.ENTITY_ZOMBIE_PIGMAN_AMBIENT, "entity.zombie_pigman.ambient", true);
        register(BuiltinSound.ENTITY_ZOMBIE_PIGMAN_ANGRY, "entity.zombie_pigman.angry", true);
        register(BuiltinSound.ENTITY_ZOMBIE_PIGMAN_DEATH, "entity.zombie_pigman.death", true);
        register(BuiltinSound.ENTITY_ZOMBIE_PIGMAN_HURT, "entity.zombie_pigman.hurt", true);
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
        register(BuiltinSound.ITEM_ARMOR_EQUIP_TURTLE, "item.armor.equip_turtle", true);
        register(BuiltinSound.ITEM_BOTTLE_FILL_DRAGONBREATH, "item.bottle.fill_dragonbreath", true);
        register(BuiltinSound.ITEM_BUCKET_EMPTY_FISH, "item.bucket.empty_fish", true);
        register(BuiltinSound.ITEM_BUCKET_EMPTY_LAVA, "item.bucket.empty_lava", true);
        register(BuiltinSound.ITEM_BUCKET_FILL_FISH, "item.bucket.fill_fish", true);
        register(BuiltinSound.ITEM_BUCKET_FILL_LAVA, "item.bucket.fill_lava", true);
        register(BuiltinSound.ITEM_CHORUS_FRUIT_TELEPORT, "item.chorus_fruit.teleport", true);
        register(BuiltinSound.ITEM_TRIDENT_HIT_GROUND, "item.trident.hit_ground", true);
        register(BuiltinSound.ITEM_TRIDENT_RIPTIDE_1, "item.trident.riptide_1", true);
        register(BuiltinSound.ITEM_TRIDENT_RIPTIDE_2, "item.trident.riptide_2", true);
        register(BuiltinSound.ITEM_TRIDENT_RIPTIDE_3, "item.trident.riptide_3", true);
        register(BuiltinSound.MUSIC_UNDER_WATER, "music.under_water", true);
        register(BuiltinSound.MUSIC_DISC_11, "music_disc.11", true);
        register(BuiltinSound.MUSIC_DISC_13, "music_disc.13", true);
        register(BuiltinSound.MUSIC_DISC_BLOCKS, "music_disc.blocks", true);
        register(BuiltinSound.MUSIC_DISC_CAT, "music_disc.cat", true);
        register(BuiltinSound.MUSIC_DISC_CHIRP, "music_disc.chirp", true);
        register(BuiltinSound.MUSIC_DISC_FAR, "music_disc.far", true);
        register(BuiltinSound.MUSIC_DISC_MALL, "music_disc.mall", true);
        register(BuiltinSound.MUSIC_DISC_MELLOHI, "music_disc.mellohi", true);
        register(BuiltinSound.MUSIC_DISC_STAL, "music_disc.stal", true);
        register(BuiltinSound.MUSIC_DISC_STRAD, "music_disc.strad", true);
        register(BuiltinSound.MUSIC_DISC_WAIT, "music_disc.wait", true);
        register(BuiltinSound.MUSIC_DISC_WARD, "music_disc.ward", true);
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
