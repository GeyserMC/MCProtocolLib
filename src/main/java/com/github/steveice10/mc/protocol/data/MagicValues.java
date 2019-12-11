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
import com.github.steveice10.mc.protocol.data.game.command.CommandParser;
import com.github.steveice10.mc.protocol.data.game.command.CommandType;
import com.github.steveice10.mc.protocol.data.game.command.SuggestionType;
import com.github.steveice10.mc.protocol.data.game.command.properties.StringProperties;
import com.github.steveice10.mc.protocol.data.game.entity.Effect;
import com.github.steveice10.mc.protocol.data.game.entity.EntityStatus;
import com.github.steveice10.mc.protocol.data.game.entity.EquipmentSlot;
import com.github.steveice10.mc.protocol.data.game.entity.RotationOrigin;
import com.github.steveice10.mc.protocol.data.game.entity.attribute.AttributeType;
import com.github.steveice10.mc.protocol.data.game.entity.attribute.ModifierOperation;
import com.github.steveice10.mc.protocol.data.game.entity.attribute.ModifierType;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.MetadataType;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Pose;
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
import com.github.steveice10.mc.protocol.data.game.recipe.RecipeType;
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
import com.github.steveice10.mc.protocol.data.game.world.WorldBorderAction;
import com.github.steveice10.mc.protocol.data.game.world.WorldType;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockFace;
import com.github.steveice10.mc.protocol.data.game.world.block.CommandBlockMode;
import com.github.steveice10.mc.protocol.data.game.world.block.StructureMirror;
import com.github.steveice10.mc.protocol.data.game.world.block.StructureRotation;
import com.github.steveice10.mc.protocol.data.game.world.block.UpdatedTileType;
import com.github.steveice10.mc.protocol.data.game.world.block.value.BeaconValueType;
import com.github.steveice10.mc.protocol.data.game.world.block.value.ChestValueType;
import com.github.steveice10.mc.protocol.data.game.world.block.value.EndGatewayValueType;
import com.github.steveice10.mc.protocol.data.game.world.block.value.GenericBlockValueType;
import com.github.steveice10.mc.protocol.data.game.world.block.value.MobSpawnerValueType;
import com.github.steveice10.mc.protocol.data.game.world.block.value.NoteBlockValueType;
import com.github.steveice10.mc.protocol.data.game.world.block.value.PistonValue;
import com.github.steveice10.mc.protocol.data.game.world.block.value.PistonValueType;
import com.github.steveice10.mc.protocol.data.game.world.effect.ComposterEffectData;
import com.github.steveice10.mc.protocol.data.game.world.effect.ParticleEffect;
import com.github.steveice10.mc.protocol.data.game.world.effect.SmokeEffectData;
import com.github.steveice10.mc.protocol.data.game.world.effect.SoundEffect;
import com.github.steveice10.mc.protocol.data.game.world.map.MapIconType;
import com.github.steveice10.mc.protocol.data.game.world.notify.ClientNotification;
import com.github.steveice10.mc.protocol.data.game.world.notify.DemoMessageValue;
import com.github.steveice10.mc.protocol.data.game.world.notify.EnterCreditsValue;
import com.github.steveice10.mc.protocol.data.game.world.particle.ParticleType;
import com.github.steveice10.mc.protocol.data.game.world.sound.BuiltinSound;
import com.github.steveice10.mc.protocol.data.game.world.sound.SoundCategory;
import com.github.steveice10.mc.protocol.data.handshake.HandshakeIntent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MagicValues {
    private static final Map<Object, List<Object>> VALUES = new HashMap<>();

    static {
        register(Pose.STANDING, 0);
        register(Pose.FALL_FLYING, 1);
        register(Pose.SLEEPING, 2);
        register(Pose.SWIMMING, 3);
        register(Pose.SPIN_ATTACK, 4);
        register(Pose.SNEAKING, 5);
        register(Pose.DYING, 6);

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
        register(MetadataType.VILLAGER_DATA, 16);
        register(MetadataType.OPTIONAL_VARINT, 17);
        register(MetadataType.POSE, 18);

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
        register(WorldType.BUFFET, "buffet");
        register(WorldType.DEBUG, "debug_all_block_states");
        register(WorldType.DEFAULT_1_1, "default_1_1");

        register(Animation.SWING_ARM, 0);
        register(Animation.DAMAGE, 1);
        register(Animation.LEAVE_BED, 2);
        register(Animation.EAT_FOOD, 3);
        register(Animation.CRITICAL_HIT, 4);
        register(Animation.ENCHANTMENT_CRITICAL_HIT, 5);

        register(Effect.FASTER_MOVEMENT, 1);
        register(Effect.SLOWER_MOVEMENT, 2);
        register(Effect.FASTER_DIG, 3);
        register(Effect.SLOWER_DIG, 4);
        register(Effect.INCREASE_DAMAGE, 5);
        register(Effect.HEAL, 6);
        register(Effect.HARM, 7);
        register(Effect.JUMP, 8);
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
        register(Effect.WITHER, 20);
        register(Effect.HEALTH_BOOST, 21);
        register(Effect.ABSORBTION, 22);
        register(Effect.SATURATION, 23);
        register(Effect.GLOWING, 24);
        register(Effect.LEVITATION, 25);
        register(Effect.LUCK, 26);
        register(Effect.UNLUCK, 27);
        register(Effect.SLOW_FALLING, 28);
        register(Effect.CONDUIT_POWER, 29);
        register(Effect.DOLPHINS_GRACE, 30);
        register(Effect.BAD_OMEN, 31);
        register(Effect.HERO_OF_THE_VILLAGE, 32);

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
        register(EntityStatus.DOLPHIN_HAPPY, 38);
        register(EntityStatus.RAVAGER_STUNNED, 39);
        register(EntityStatus.OCELOT_TAMING_FAILED, 40);
        register(EntityStatus.OCELOT_TAMING_SUCCEEDED, 41);
        register(EntityStatus.VILLAGER_SWEAT, 42);
        register(EntityStatus.PLAYER_EMIT_CLOUD, 43);
        register(EntityStatus.LIVING_HURT_SWEET_BERRY_BUSH, 44);
        register(EntityStatus.FOX_EATING, 45);
        register(EntityStatus.LIVING_TELEPORT, 46);
        register(EntityStatus.LIVING_EQUIPMENT_BREAK_MAIN_HAND, 47);
        register(EntityStatus.LIVING_EQUIPMENT_BREAK_OFF_HAND, 48);
        register(EntityStatus.LIVING_EQUIPMENT_BREAK_HEAD, 49);
        register(EntityStatus.LIVING_EQUIPMENT_BREAK_CHEST, 50);
        register(EntityStatus.LIVING_EQUIPMENT_BREAK_LEGS, 51);
        register(EntityStatus.LIVING_EQUIPMENT_BREAK_FEET, 52);

        register(PositionElement.X, 0);
        register(PositionElement.Y, 1);
        register(PositionElement.Z, 2);
        register(PositionElement.PITCH, 3);
        register(PositionElement.YAW, 4);

        register(GlobalEntityType.LIGHTNING_BOLT, 1);

        register(MobType.AREA_EFFECT_CLOUD, 0);
        register(MobType.ARMOR_STAND, 1);
        register(MobType.ARROW, 2);
        register(MobType.BAT, 3);
        register(MobType.BEE, 4);
        register(MobType.BLAZE, 5);
        register(MobType.BOAT, 6);
        register(MobType.CAT, 7);
        register(MobType.CAVE_SPIDER, 8);
        register(MobType.CHICKEN, 9);
        register(MobType.COD, 10);
        register(MobType.COW, 11);
        register(MobType.CREEPER, 12);
        register(MobType.DONKEY, 13);
        register(MobType.DOLPHIN, 14);
        register(MobType.DRAGON_FIREBALL, 15);
        register(MobType.DROWNED, 16);
        register(MobType.ELDER_GUARDIAN, 17);
        register(MobType.END_CRYSTAL, 18);
        register(MobType.ENDER_DRAGON, 19);
        register(MobType.ENDERMAN, 20);
        register(MobType.ENDERMITE, 21);
        register(MobType.EVOKER_FANGS, 22);
        register(MobType.EVOKER, 23);
        register(MobType.EXPERIENCE_ORB, 24);
        register(MobType.EYE_OF_ENDER, 25);
        register(MobType.FALLING_BLOCK, 26);
        register(MobType.FIREWORK_ROCKET, 27);
        register(MobType.FOX, 28);
        register(MobType.GHAST, 29);
        register(MobType.GIANT, 30);
        register(MobType.GUARDIAN, 31);
        register(MobType.HORSE, 32);
        register(MobType.HUSK, 33);
        register(MobType.ILLUSIONER, 34);
        register(MobType.ITEM, 35);
        register(MobType.ITEM_FRAME, 36);
        register(MobType.FIREBALL, 37);
        register(MobType.LEASH_KNOT, 38);
        register(MobType.LLAMA, 39);
        register(MobType.LLAMA_SPIT, 40);
        register(MobType.MAGMA_CUBE, 41);
        register(MobType.MINECART, 42);
        register(MobType.MINECART_CHEST, 43);
        register(MobType.MINECART_COMMAND_BLOCK, 44);
        register(MobType.MINECART_FURNACE, 45);
        register(MobType.MINECART_HOPPER, 46);
        register(MobType.MINECART_SPAWNER, 47);
        register(MobType.MINECART_TNT, 48);
        register(MobType.MULE, 49);
        register(MobType.MOOSHROOM, 50);
        register(MobType.OCELOT, 51);
        register(MobType.PAINTING, 52);
        register(MobType.PANDA, 53);
        register(MobType.PARROT, 54);
        register(MobType.PIG, 55);
        register(MobType.PUFFERFISH, 56);
        register(MobType.ZOMBIE_PIGMAN, 57);
        register(MobType.POLAR_BEAR, 58);
        register(MobType.PRIMED_TNT, 59);
        register(MobType.RABBIT, 60);
        register(MobType.SALMON, 61);
        register(MobType.SHEEP, 62);
        register(MobType.SHULKER, 63);
        register(MobType.SHULKER_BULLET, 64);
        register(MobType.SILVERFISH, 65);
        register(MobType.SKELETON, 66);
        register(MobType.SKELETON_HORSE, 67);
        register(MobType.SLIME, 68);
        register(MobType.SMALL_FIREBALL, 69);
        register(MobType.SNOW_GOLEM, 70);
        register(MobType.SNOWBALL, 71);
        register(MobType.SPECTRAL_ARROW, 72);
        register(MobType.SPIDER, 73);
        register(MobType.SQUID, 74);
        register(MobType.STRAY, 75);
        register(MobType.TRADER_LLAMA, 76);
        register(MobType.TROPICAL_FISH, 77);
        register(MobType.TURTLE, 78);
        register(MobType.THROWN_EGG, 79);
        register(MobType.THROWN_ENDERPEARL, 80);
        register(MobType.THROWN_EXP_BOTTLE, 81);
        register(MobType.THROWN_POTION, 82);
        register(MobType.TRIDENT, 83);
        register(MobType.VEX, 84);
        register(MobType.VILLAGER, 85);
        register(MobType.IRON_GOLEM, 86);
        register(MobType.VINDICATOR, 87);
        register(MobType.PILLAGER, 88);
        register(MobType.WANDERING_TRADER, 89);
        register(MobType.WITCH, 90);
        register(MobType.WITHER, 91);
        register(MobType.WITHER_SKELETON, 92);
        register(MobType.WITHER_SKULL, 93);
        register(MobType.WOLF, 94);
        register(MobType.ZOMBIE, 95);
        register(MobType.ZOMBIE_HORSE, 96);
        register(MobType.ZOMBIE_VILLAGER, 97);
        register(MobType.PHANTOM, 98);
        register(MobType.RAVAGER, 99);
        register(MobType.PLAYER, 101);
        register(MobType.FISHING_BOBBER, 102);

        register(ObjectType.AREA_EFFECT_CLOUD, 0);
        register(ObjectType.ARMOR_STAND, 1);
        register(ObjectType.ARROW, 2);
        register(ObjectType.BOAT, 6);
        register(ObjectType.DRAGON_FIREBALL, 15);
        register(ObjectType.END_CRYSTAL, 18);
        register(ObjectType.EVOKER_FANGS, 22);
        register(ObjectType.EXPERIENCE_ORB, 24);
        register(ObjectType.EYE_OF_ENDER, 25);
        register(ObjectType.FALLING_BLOCK, 26);
        register(ObjectType.FIREWORK_ROCKET, 27);
        register(ObjectType.ITEM, 35);
        register(ObjectType.ITEM_FRAME, 36);
        register(ObjectType.FIREBALL, 37);
        register(ObjectType.LEASH_KNOT, 38);
        register(ObjectType.LLAMA_SPIT, 40);
        register(ObjectType.MINECART, 42);
        register(ObjectType.CHEST_MINECART, 43);
        register(ObjectType.COMMAND_BLOCK_MINECART, 44);
        register(ObjectType.FURNACE_MINECART, 45);
        register(ObjectType.HOPPER_MINECART, 46);
        register(ObjectType.SPAWNER_MINECART, 47);
        register(ObjectType.TNT_MINECART, 48);
        register(ObjectType.TNT, 59);
        register(ObjectType.SHULKER_BULLET, 64);
        register(ObjectType.SMALL_FIREBALL, 69);
        register(ObjectType.SNOWBALL, 71);
        register(ObjectType.SPECTRAL_ARROW, 72);
        register(ObjectType.EGG, 79);
        register(ObjectType.ENDER_PEARL, 80);
        register(ObjectType.EXPERIENCE_BOTTLE, 81);
        register(ObjectType.POTION, 82);
        register(ObjectType.TRIDENT, 83);
        register(ObjectType.WITHER_SKULL, 93);
        register(ObjectType.FISHING_BOBBER, 102);

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

        register(WindowType.GENERIC_9X1, 0);
        register(WindowType.GENERIC_9X2, 1);
        register(WindowType.GENERIC_9X3, 2);
        register(WindowType.GENERIC_9X4, 3);
        register(WindowType.GENERIC_9X5, 4);
        register(WindowType.GENERIC_9X6, 5);
        register(WindowType.GENERIC_3X3, 6);
        register(WindowType.ANVIL, 7);
        register(WindowType.BEACON, 8);
        register(WindowType.BLAST_FURNACE, 9);
        register(WindowType.BREWING_STAND, 10);
        register(WindowType.CRAFTING, 11);
        register(WindowType.ENCHANTMENT, 12);
        register(WindowType.FURNACE, 13);
        register(WindowType.GRINDSTONE, 14);
        register(WindowType.HOPPER, 15);
        register(WindowType.LECTERN, 16);
        register(WindowType.LOOM, 17);
        register(WindowType.MERCHANT, 18);
        register(WindowType.SHULKER_BOX, 19);
        register(WindowType.SMOKER, 20);
        register(WindowType.CARTOGRAPHY, 21);
        register(WindowType.STONECUTTER, 22);

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
        register(UpdatedTileType.BED, 11);
        register(UpdatedTileType.JIGSAW_BLOCK, 12);
        register(UpdatedTileType.CAMPFIRE, 13);
        register(UpdatedTileType.BEEHIVE, 14);

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

        register(GenericStatistic.LEAVE_GAME, 0);
        register(GenericStatistic.PLAY_ONE_MINUTE, 1);
        register(GenericStatistic.TIME_SINCE_DEATH, 2);
        register(GenericStatistic.TIME_SINCE_REST, 3);
        register(GenericStatistic.SNEAK_TIME, 4);
        register(GenericStatistic.WALK_ONE_CM, 5);
        register(GenericStatistic.CROUCH_ONE_CM, 6);
        register(GenericStatistic.SPRINT_ONE_CM, 7);
        register(GenericStatistic.WALK_ON_WATER_ONE_CM, 8);
        register(GenericStatistic.FALL_ONE_CM, 9);
        register(GenericStatistic.CLIMB_ONE_CM, 10);
        register(GenericStatistic.FLY_ONE_CM, 11);
        register(GenericStatistic.WALK_UNDER_WATER_ONE_CM, 12);
        register(GenericStatistic.MINECART_ONE_CM, 13);
        register(GenericStatistic.BOAT_ONE_CM, 14);
        register(GenericStatistic.PIG_ONE_CM, 15);
        register(GenericStatistic.HORSE_ONE_CM, 16);
        register(GenericStatistic.AVIATE_ONE_CM, 17);
        register(GenericStatistic.SWIM_ONE_CM, 18);
        register(GenericStatistic.JUMP, 19);
        register(GenericStatistic.DROP, 20);
        register(GenericStatistic.DAMAGE_DEALT, 21);
        register(GenericStatistic.DAMAGE_DEALT_ABSORBED, 22);
        register(GenericStatistic.DAMAGE_DEALT_RESISTED, 23);
        register(GenericStatistic.DAMAGE_TAKEN, 24);
        register(GenericStatistic.DAMAGE_BLOCKED_BY_SHIELD, 25);
        register(GenericStatistic.DAMAGE_ABSORBED, 26);
        register(GenericStatistic.DAMAGE_RESISTED, 27);
        register(GenericStatistic.DEATHS, 28);
        register(GenericStatistic.MOB_KILLS, 29);
        register(GenericStatistic.ANIMALS_BRED, 30);
        register(GenericStatistic.PLAYER_KILLS, 31);
        register(GenericStatistic.FISH_CAUGHT, 32);
        register(GenericStatistic.TALKED_TO_VILLAGER, 33);
        register(GenericStatistic.TRADED_WITH_VILLAGER, 34);
        register(GenericStatistic.EAT_CAKE_SLICE, 35);
        register(GenericStatistic.FILL_CAULDRON, 36);
        register(GenericStatistic.USE_CAULDRON, 37);
        register(GenericStatistic.CLEAN_ARMOR, 38);
        register(GenericStatistic.CLEAN_BANNER, 39);
        register(GenericStatistic.CLEAN_SHULKER_BOX, 40);
        register(GenericStatistic.INTERACT_WITH_BREWINGSTAND, 41);
        register(GenericStatistic.INTERACT_WITH_BEACON, 42);
        register(GenericStatistic.INSPECT_DROPPER, 43);
        register(GenericStatistic.INSPECT_HOPPER, 44);
        register(GenericStatistic.INSPECT_DISPENSER, 45);
        register(GenericStatistic.PLAY_NOTEBLOCK, 46);
        register(GenericStatistic.TUNE_NOTEBLOCK, 47);
        register(GenericStatistic.POT_FLOWER, 48);
        register(GenericStatistic.TRIGGER_TRAPPED_CHEST, 49);
        register(GenericStatistic.OPEN_ENDERCHEST, 50);
        register(GenericStatistic.ENCHANT_ITEM, 51);
        register(GenericStatistic.PLAY_RECORD, 52);
        register(GenericStatistic.INTERACT_WITH_FURNACE, 53);
        register(GenericStatistic.INTERACT_WITH_CRAFTING_TABLE, 54);
        register(GenericStatistic.OPEN_CHEST, 55);
        register(GenericStatistic.SLEEP_IN_BED, 56);
        register(GenericStatistic.OPEN_SHULKER_BOX, 57);
        register(GenericStatistic.OPEN_BARREL, 58);
        register(GenericStatistic.INTERACT_WITH_BLAST_FURNACE, 59);
        register(GenericStatistic.INTERACT_WITH_SMOKER, 60);
        register(GenericStatistic.INTERACT_WITH_LECTERN, 61);
        register(GenericStatistic.INTERACT_WITH_CAMPFIRE, 62);
        register(GenericStatistic.INTERACT_WITH_CARTOGRAPHY_TABLE, 63);
        register(GenericStatistic.INTERACT_WITH_LOOM, 64);
        register(GenericStatistic.INTERACT_WITH_STONECUTTER, 65);
        register(GenericStatistic.BELL_RING, 66);
        register(GenericStatistic.RAID_TRIGGER, 67);
        register(GenericStatistic.RAID_WIN, 68);

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
        register(ParticleType.FALLING_LAVA, 10);
        register(ParticleType.LANDING_LAVA, 11);
        register(ParticleType.DRIPPING_WATER, 12);
        register(ParticleType.FALLING_WATER, 13);
        register(ParticleType.DUST, 14);
        register(ParticleType.EFFECT, 15);
        register(ParticleType.ELDER_GUARDIAN, 16);
        register(ParticleType.ENCHANTED_HIT, 17);
        register(ParticleType.ENCHANT, 18);
        register(ParticleType.END_ROD, 19);
        register(ParticleType.ENTITY_EFFECT, 20);
        register(ParticleType.EXPLOSION_EMITTER, 21);
        register(ParticleType.EXPLOSION, 22);
        register(ParticleType.FALLING_DUST, 23);
        register(ParticleType.FIREWORK, 24);
        register(ParticleType.FISHING, 25);
        register(ParticleType.FLAME, 26);
        register(ParticleType.FLASH, 27);
        register(ParticleType.HAPPY_VILLAGER, 28);
        register(ParticleType.COMPOSTER, 29);
        register(ParticleType.HEART, 30);
        register(ParticleType.INSTANT_EFFECT, 31);
        register(ParticleType.ITEM, 32);
        register(ParticleType.ITEM_SLIME, 33);
        register(ParticleType.ITEM_SNOWBALL, 34);
        register(ParticleType.LARGE_SMOKE, 35);
        register(ParticleType.LAVA, 36);
        register(ParticleType.MYCELIUM, 37);
        register(ParticleType.NOTE, 38);
        register(ParticleType.POOF, 39);
        register(ParticleType.PORTAL, 40);
        register(ParticleType.RAIN, 41);
        register(ParticleType.SMOKE, 42);
        register(ParticleType.SNEEZE, 43);
        register(ParticleType.SPIT, 44);
        register(ParticleType.SQUID_INK, 45);
        register(ParticleType.SWEEP_ATTACK, 46);
        register(ParticleType.TOTEM_OF_UNDYING, 47);
        register(ParticleType.UNDERWATER, 48);
        register(ParticleType.SPLASH, 49);
        register(ParticleType.WITCH, 50);
        register(ParticleType.BUBBLE_POP, 51);
        register(ParticleType.CURRENT_DOWN, 52);
        register(ParticleType.BUBBLE_COLUMN_UP, 53);
        register(ParticleType.NAUTILUS, 54);
        register(ParticleType.DOLPHIN, 55);
        register(ParticleType.CAMPFIRE_COSY_SMOKE, 56);
        register(ParticleType.CAMPFIRE_SIGNAL_SMOKE, 57);

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
        register(NoteBlockValueType.IRON_XYLOPHONE, 10);
        register(NoteBlockValueType.COW_BELL, 11);
        register(NoteBlockValueType.DIDGERIDOO, 12);
        register(NoteBlockValueType.BIT, 13);
        register(NoteBlockValueType.BANJO, 14);
        register(NoteBlockValueType.PLING, 15);

        register(PistonValueType.PUSHING, 0);
        register(PistonValueType.PULLING, 1);
        register(PistonValueType.CANCELLED_MID_PUSH, 2);

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
        register(SoundEffect.BLOCK_END_PORTAL_SPAWN, 1038);
        register(SoundEffect.ENTITY_PHANTOM_BITE, 1039);
        register(SoundEffect.ENTITY_ZOMBIE_CONVERTED_TO_DROWNED, 1040);
        register(SoundEffect.ENTITY_HUSK_CONVERTED_TO_ZOMBIE, 1041);
        register(SoundEffect.BLOCK_GRINDSTONE_USE, 1042);
        register(SoundEffect.ITEM_BOOK_PAGE_TURN, 1043);
        register(SoundEffect.ENTITY_ENDERDRAGON_GROWL, 3001);

        register(ParticleEffect.COMPOSTER, 1500);
        register(ParticleEffect.BLOCK_LAVA_EXTINGUISH, 1501);
        register(ParticleEffect.BLOCK_REDSTONE_TORCH_BURNOUT, 1502);
        register(ParticleEffect.BLOCK_END_PORTAL_FRAME_FILL, 1503);
        register(ParticleEffect.SMOKE, 2000);
        register(ParticleEffect.BREAK_BLOCK, 2001);
        register(ParticleEffect.BREAK_SPLASH_POTION, 2002);
        register(ParticleEffect.BREAK_EYE_OF_ENDER, 2003);
        register(ParticleEffect.MOB_SPAWN, 2004);
        register(ParticleEffect.BONEMEAL_GROW, 2005);
        register(ParticleEffect.ENDERDRAGON_FIREBALL_EXPLODE, 2006);
        register(ParticleEffect.BREAK_SPLASH_POTION, 2007);
        register(ParticleEffect.EXPLOSION, 2008);
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

        register(ComposterEffectData.FILL, 0);
        register(ComposterEffectData.FILL_SUCCESS, 1);

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

        register(RotationOrigin.FEET, 0);
        register(RotationOrigin.EYES, 1);

        register(RecipeType.CRAFTING_SHAPELESS, "minecraft:crafting_shapeless");
        register(RecipeType.CRAFTING_SHAPED, "minecraft:crafting_shaped");
        register(RecipeType.CRAFTING_SPECIAL_ARMORDYE, "minecraft:crafting_special_armordye");
        register(RecipeType.CRAFTING_SPECIAL_BOOKCLONING, "minecraft:crafting_special_bookcloning");
        register(RecipeType.CRAFTING_SPECIAL_MAPCLONING, "minecraft:crafting_special_mapcloning");
        register(RecipeType.CRAFTING_SPECIAL_MAPEXTENDING, "minecraft:crafting_special_mapextending");
        register(RecipeType.CRAFTING_SPECIAL_FIREWORK_ROCKET, "minecraft:crafting_special_firework_rocket");
        register(RecipeType.CRAFTING_SPECIAL_FIREWORK_STAR, "minecraft:crafting_special_firework_star");
        register(RecipeType.CRAFTING_SPECIAL_FIREWORK_STAR_FADE, "minecraft:crafting_special_firework_star_fade");
        register(RecipeType.CRAFTING_SPECIAL_REPAIRITEM, "minecraft:crafting_special_repairitem");
        register(RecipeType.CRAFTING_SPECIAL_TIPPEDARROW, "minecraft:crafting_special_tippedarrow");
        register(RecipeType.CRAFTING_SPECIAL_BANNERDUPLICATE, "minecraft:crafting_special_bannerduplicate");
        register(RecipeType.CRAFTING_SPECIAL_BANNERADDPATTERN, "minecraft:crafting_special_banneraddpattern");
        register(RecipeType.CRAFTING_SPECIAL_SHIELDDECORATION, "minecraft:crafting_special_shielddecoration");
        register(RecipeType.CRAFTING_SPECIAL_SHULKERBOXCOLORING, "minecraft:crafting_special_shulkerboxcoloring");
        register(RecipeType.CRAFTING_SPECIAL_SUSPICIOUSSTEW, "minecraft:crafting_special_suspiciousstew");
        register(RecipeType.SMELTING, "minecraft:smelting");
        register(RecipeType.BLASTING, "minecraft:blasting");
        register(RecipeType.SMOKING, "minecraft:smoking");
        register(RecipeType.CAMPFIRE_COOKING, "minecraft:campfire_cooking");
        register(RecipeType.STONECUTTING, "minecraft:stonecutting");

        register(CommandType.ROOT, 0);
        register(CommandType.LITERAL, 1);
        register(CommandType.ARGUMENT, 2);

        register(CommandParser.BOOL, "brigadier:bool");
        register(CommandParser.DOUBLE, "brigadier:double");
        register(CommandParser.FLOAT, "brigadier:float");
        register(CommandParser.INTEGER, "brigadier:integer");
        register(CommandParser.STRING, "brigadier:string");
        register(CommandParser.ENTITY, "minecraft:entity");
        register(CommandParser.GAME_PROFILE, "minecraft:game_profile");
        register(CommandParser.BLOCK_POS, "minecraft:block_pos");
        register(CommandParser.COLUMN_POS, "minecraft:column_pos");
        register(CommandParser.VEC3, "minecraft:vec3");
        register(CommandParser.VEC2, "minecraft:vec2");
        register(CommandParser.BLOCK_STATE, "minecraft:block_state");
        register(CommandParser.BLOCK_PREDICATE, "minecraft:block_predicate");
        register(CommandParser.ITEM_STACK, "minecraft:item_stack");
        register(CommandParser.ITEM_PREDICATE, "minecraft:item_predicate");
        register(CommandParser.COLOR, "minecraft:color");
        register(CommandParser.COMPONENT, "minecraft:component");
        register(CommandParser.MESSAGE, "minecraft:message");
        register(CommandParser.NBT, "minecraft:nbt");
        register(CommandParser.NBT_PATH, "minecraft:nbt_path");
        register(CommandParser.OBJECTIVE, "minecraft:objective");
        register(CommandParser.OBJECTIVE_CRITERIA, "minecraft:objective_criteria");
        register(CommandParser.OPERATION, "minecraft:operation");
        register(CommandParser.PARTICLE, "minecraft:particle");
        register(CommandParser.ROTATION, "minecraft:rotation");
        register(CommandParser.SCOREBOARD_SLOT, "minecraft:scoreboard_slot");
        register(CommandParser.SCORE_HOLDER, "minecraft:score_holder");
        register(CommandParser.SWIZZLE, "minecraft:swizzle");
        register(CommandParser.TEAM, "minecraft:team");
        register(CommandParser.ITEM_SLOT, "minecraft:item_slot");
        register(CommandParser.RESOURCE_LOCATION, "minecraft:resource_location");
        register(CommandParser.MOB_EFFECT, "minecraft:mob_effect");
        register(CommandParser.FUNCTION, "minecraft:function");
        register(CommandParser.ENTITY_ANCHOR, "minecraft:entity_anchor");
        register(CommandParser.RANGE, "minecraft:range");
        register(CommandParser.INT_RANGE, "minecraft:int_range");
        register(CommandParser.FLOAT_RANGE, "minecraft:float_range");
        register(CommandParser.ITEM_ENCHANTMENT, "minecraft:item_enchantment");
        register(CommandParser.ENTITY_SUMMON, "minecraft:entity_summon");
        register(CommandParser.DIMENSION, "minecraft:dimension");
        register(CommandParser.TIME, "minecraft:time");
        register(CommandParser.NBT_COMPOUND_TAG, "minecraft:nbt_compound_tag");
        register(CommandParser.NBT_TAG, "minecraft:nbt_tag");

        register(SuggestionType.ASK_SERVER, "minecraft:ask_server");
        register(SuggestionType.ALL_RECIPES, "minecraft:all_recipes");
        register(SuggestionType.AVAILABLE_SOUNDS, "minecraft:available_sounds");
        register(SuggestionType.SUMMONABLE_ENTITIES, "minecraft:summonable_entities");

        register(StringProperties.SINGLE_WORD, 0);
        register(StringProperties.QUOTABLE_PHRASE, 1);
        register(StringProperties.GREEDY_PHRASE, 2);

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
            register(sound, sound.getName());
        }
    }

    private MagicValues() {
    }

    private static void register(Enum<?> key, Object value) {
        VALUES.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T key(Class<T> keyType, Object value) {
        for(Object key : VALUES.keySet()) {
            if(keyType.isAssignableFrom(key.getClass())) {
                for(Object val : VALUES.get(key)) {
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
        if(VALUES.containsKey(key)) {
            for(Object val : VALUES.get(key)) {
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
