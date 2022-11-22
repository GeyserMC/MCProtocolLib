package com.github.steveice10.mc.protocol.data;

import com.github.steveice10.mc.protocol.data.game.BossBarAction;
import com.github.steveice10.mc.protocol.data.game.BossBarColor;
import com.github.steveice10.mc.protocol.data.game.BossBarDivision;
import com.github.steveice10.mc.protocol.data.game.ClientCommand;
import com.github.steveice10.mc.protocol.data.game.ResourcePackStatus;
import com.github.steveice10.mc.protocol.data.game.UnlockRecipesAction;
import com.github.steveice10.mc.protocol.data.game.advancement.Advancement;
import com.github.steveice10.mc.protocol.data.game.command.CommandParser;
import com.github.steveice10.mc.protocol.data.game.command.CommandType;
import com.github.steveice10.mc.protocol.data.game.command.SuggestionType;
import com.github.steveice10.mc.protocol.data.game.command.properties.StringProperties;
import com.github.steveice10.mc.protocol.data.game.entity.EquipmentSlot;
import com.github.steveice10.mc.protocol.data.game.entity.RotationOrigin;
import com.github.steveice10.mc.protocol.data.game.entity.object.MinecartType;
import com.github.steveice10.mc.protocol.data.game.entity.player.Animation;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.data.game.entity.player.HandPreference;
import com.github.steveice10.mc.protocol.data.game.entity.player.InteractAction;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerAction;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerState;
import com.github.steveice10.mc.protocol.data.game.entity.player.PositionElement;
import com.github.steveice10.mc.protocol.data.game.inventory.AdvancementTabAction;
import com.github.steveice10.mc.protocol.data.game.inventory.ClickItemAction;
import com.github.steveice10.mc.protocol.data.game.inventory.ContainerActionType;
import com.github.steveice10.mc.protocol.data.game.inventory.ContainerType;
import com.github.steveice10.mc.protocol.data.game.inventory.CraftingBookStateType;
import com.github.steveice10.mc.protocol.data.game.inventory.CreativeGrabAction;
import com.github.steveice10.mc.protocol.data.game.inventory.DropItemAction;
import com.github.steveice10.mc.protocol.data.game.inventory.FillStackAction;
import com.github.steveice10.mc.protocol.data.game.inventory.MoveToHotbarAction;
import com.github.steveice10.mc.protocol.data.game.inventory.ShiftClickItemAction;
import com.github.steveice10.mc.protocol.data.game.inventory.SpreadItemAction;
import com.github.steveice10.mc.protocol.data.game.inventory.UpdateStructureBlockAction;
import com.github.steveice10.mc.protocol.data.game.inventory.UpdateStructureBlockMode;
import com.github.steveice10.mc.protocol.data.game.inventory.property.AnvilProperty;
import com.github.steveice10.mc.protocol.data.game.inventory.property.BrewingStandProperty;
import com.github.steveice10.mc.protocol.data.game.inventory.property.EnchantmentTableProperty;
import com.github.steveice10.mc.protocol.data.game.inventory.property.FurnaceProperty;
import com.github.steveice10.mc.protocol.data.game.level.block.CommandBlockMode;
import com.github.steveice10.mc.protocol.data.game.level.block.StructureMirror;
import com.github.steveice10.mc.protocol.data.game.level.block.StructureRotation;
import com.github.steveice10.mc.protocol.data.game.level.block.value.ChestValueType;
import com.github.steveice10.mc.protocol.data.game.level.block.value.EndGatewayValueType;
import com.github.steveice10.mc.protocol.data.game.level.block.value.GenericBlockValueType;
import com.github.steveice10.mc.protocol.data.game.level.block.value.MobSpawnerValueType;
import com.github.steveice10.mc.protocol.data.game.level.block.value.NoteBlockValueType;
import com.github.steveice10.mc.protocol.data.game.level.block.value.PistonValue;
import com.github.steveice10.mc.protocol.data.game.level.block.value.PistonValueType;
import com.github.steveice10.mc.protocol.data.game.level.event.ComposterEventData;
import com.github.steveice10.mc.protocol.data.game.level.event.DragonFireballEventData;
import com.github.steveice10.mc.protocol.data.game.level.event.SmokeEventData;
import com.github.steveice10.mc.protocol.data.game.level.map.MapIconType;
import com.github.steveice10.mc.protocol.data.game.level.notify.DemoMessageValue;
import com.github.steveice10.mc.protocol.data.game.level.notify.EnterCreditsValue;
import com.github.steveice10.mc.protocol.data.game.level.notify.GameEvent;
import com.github.steveice10.mc.protocol.data.game.level.notify.RespawnScreenValue;
import com.github.steveice10.mc.protocol.data.game.level.particle.positionsource.PositionSourceType;
import com.github.steveice10.mc.protocol.data.game.recipe.RecipeType;
import com.github.steveice10.mc.protocol.data.game.scoreboard.CollisionRule;
import com.github.steveice10.mc.protocol.data.game.scoreboard.NameTagVisibility;
import com.github.steveice10.mc.protocol.data.game.scoreboard.ObjectiveAction;
import com.github.steveice10.mc.protocol.data.game.scoreboard.ScoreType;
import com.github.steveice10.mc.protocol.data.game.scoreboard.ScoreboardAction;
import com.github.steveice10.mc.protocol.data.game.scoreboard.ScoreboardPosition;
import com.github.steveice10.mc.protocol.data.game.scoreboard.TeamAction;
import com.github.steveice10.mc.protocol.data.game.setting.ChatVisibility;
import com.github.steveice10.mc.protocol.data.game.setting.Difficulty;
import com.github.steveice10.mc.protocol.data.handshake.HandshakeIntent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MagicValues {
    private static final Map<Object, List<Object>> VALUES = new HashMap<>();

    static {
        register(HandshakeIntent.STATUS, 1);
        register(HandshakeIntent.LOGIN, 2);

        register(ClientCommand.RESPAWN, 0);
        register(ClientCommand.STATS, 1);

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
        register(PlayerState.OPEN_VEHICLE_INVENTORY, 7);
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

        register(ContainerActionType.CLICK_ITEM, 0);
        register(ContainerActionType.SHIFT_CLICK_ITEM, 1);
        register(ContainerActionType.MOVE_TO_HOTBAR_SLOT, 2);
        register(ContainerActionType.CREATIVE_GRAB_MAX_STACK, 3);
        register(ContainerActionType.DROP_ITEM, 4);
        register(ContainerActionType.SPREAD_ITEM, 5);
        register(ContainerActionType.FILL_STACK, 6);

        register(ClickItemAction.LEFT_CLICK, 0);
        register(ClickItemAction.RIGHT_CLICK, 1);

        register(ShiftClickItemAction.LEFT_CLICK, 0);
        register(ShiftClickItemAction.RIGHT_CLICK, 1);

        register(MoveToHotbarAction.SLOT_1, 0);
        register(MoveToHotbarAction.SLOT_2, 1);
        register(MoveToHotbarAction.SLOT_3, 2);
        register(MoveToHotbarAction.SLOT_4, 3);
        register(MoveToHotbarAction.SLOT_5, 4);
        register(MoveToHotbarAction.SLOT_6, 5);
        register(MoveToHotbarAction.SLOT_7, 6);
        register(MoveToHotbarAction.SLOT_8, 7);
        register(MoveToHotbarAction.SLOT_9, 8);
        register(MoveToHotbarAction.OFF_HAND, 40);

        register(CreativeGrabAction.GRAB, 2);

        register(DropItemAction.LEFT_CLICK_OUTSIDE_NOT_HOLDING, 0);
        register(DropItemAction.RIGHT_CLICK_OUTSIDE_NOT_HOLDING, 1);
        register(DropItemAction.DROP_FROM_SELECTED, 2);
        register(DropItemAction.DROP_SELECTED_STACK, 3);

        register(SpreadItemAction.LEFT_MOUSE_BEGIN_DRAG, 0);
        register(SpreadItemAction.LEFT_MOUSE_ADD_SLOT, 1);
        register(SpreadItemAction.LEFT_MOUSE_END_DRAG, 2);
        register(SpreadItemAction.RIGHT_MOUSE_BEGIN_DRAG, 4);
        register(SpreadItemAction.RIGHT_MOUSE_ADD_SLOT, 5);
        register(SpreadItemAction.RIGHT_MOUSE_END_DRAG, 6);
        register(SpreadItemAction.MIDDLE_MOUSE_BEGIN_DRAG, 8);
        register(SpreadItemAction.MIDDLE_MOUSE_ADD_SLOT, 9);
        register(SpreadItemAction.MIDDLE_MOUSE_END_DRAG, 10);

        register(FillStackAction.FILL, 0);

        register(GameMode.UNKNOWN, -1);
        register(GameMode.SURVIVAL, 0);
        register(GameMode.CREATIVE, 1);
        register(GameMode.ADVENTURE, 2);
        register(GameMode.SPECTATOR, 3);

        register(Difficulty.PEACEFUL, 0);
        register(Difficulty.EASY, 1);
        register(Difficulty.NORMAL, 2);
        register(Difficulty.HARD, 3);

        register(Animation.SWING_ARM, 0);
        register(Animation.DAMAGE, 1);
        register(Animation.LEAVE_BED, 2);
        register(Animation.SWING_OFFHAND, 3);
        register(Animation.CRITICAL_HIT, 4);
        register(Animation.ENCHANTMENT_CRITICAL_HIT, 5);

        register(PositionElement.X, 0);
        register(PositionElement.Y, 1);
        register(PositionElement.Z, 2);
        register(PositionElement.PITCH, 3);
        register(PositionElement.YAW, 4);

        register(MinecartType.NORMAL, 0);
        register(MinecartType.CHEST, 1);
        register(MinecartType.POWERED, 2);
        register(MinecartType.TNT, 3);
        register(MinecartType.MOB_SPAWNER, 4);
        register(MinecartType.HOPPER, 5);
        register(MinecartType.COMMAND_BLOCK, 6);

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

        register(ContainerType.GENERIC_9X1, 0);
        register(ContainerType.GENERIC_9X2, 1);
        register(ContainerType.GENERIC_9X3, 2);
        register(ContainerType.GENERIC_9X4, 3);
        register(ContainerType.GENERIC_9X5, 4);
        register(ContainerType.GENERIC_9X6, 5);
        register(ContainerType.GENERIC_3X3, 6);
        register(ContainerType.ANVIL, 7);
        register(ContainerType.BEACON, 8);
        register(ContainerType.BLAST_FURNACE, 9);
        register(ContainerType.BREWING_STAND, 10);
        register(ContainerType.CRAFTING, 11);
        register(ContainerType.ENCHANTMENT, 12);
        register(ContainerType.FURNACE, 13);
        register(ContainerType.GRINDSTONE, 14);
        register(ContainerType.HOPPER, 15);
        register(ContainerType.LECTERN, 16);
        register(ContainerType.LOOM, 17);
        register(ContainerType.MERCHANT, 18);
        register(ContainerType.SHULKER_BOX, 19);
        register(ContainerType.SMITHING, 20);
        register(ContainerType.SMOKER, 21);
        register(ContainerType.CARTOGRAPHY, 22);
        register(ContainerType.STONECUTTER, 23);

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

        register(GameEvent.INVALID_BED, 0);
        register(GameEvent.STOP_RAIN, 1);
        register(GameEvent.START_RAIN, 2);
        register(GameEvent.CHANGE_GAMEMODE, 3);
        register(GameEvent.ENTER_CREDITS, 4);
        register(GameEvent.DEMO_MESSAGE, 5);
        register(GameEvent.ARROW_HIT_PLAYER, 6);
        register(GameEvent.RAIN_STRENGTH, 7);
        register(GameEvent.THUNDER_STRENGTH, 8);
        register(GameEvent.PUFFERFISH_STING_SOUND, 9);
        register(GameEvent.AFFECTED_BY_ELDER_GUARDIAN, 10);
        register(GameEvent.ENABLE_RESPAWN_SCREEN, 11);

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

        register(RespawnScreenValue.ENABLE_RESPAWN_SCREEN, 0);
        register(RespawnScreenValue.IMMEDIATE_RESPAWN, 1);

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

        register(EndGatewayValueType.TRIGGER_BEAM, 1);

        register(GenericBlockValueType.GENERIC_0, 0);
        register(GenericBlockValueType.GENERIC_1, 1);

        register(PistonValue.DOWN, 0);
        register(PistonValue.UP, 1);
        register(PistonValue.NORTH, 2);
        register(PistonValue.SOUTH, 3);
        register(PistonValue.WEST, 4);
        register(PistonValue.EAST, 5);

        register(SmokeEventData.DOWN, 0);
        register(SmokeEventData.UP, 1);
        register(SmokeEventData.NORTH, 2);
        register(SmokeEventData.SOUTH, 3);
        register(SmokeEventData.WEST, 4);
        register(SmokeEventData.EAST, 5);

        register(ComposterEventData.FILL, 0);
        register(ComposterEventData.FILL_SUCCESS, 1);

        register(DragonFireballEventData.NO_SOUND, 0);
        register(DragonFireballEventData.HAS_SOUND, 1);

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

        register(ScoreType.INTEGER, 0);
        register(ScoreType.HEARTS, 1);

        register(Advancement.DisplayData.FrameType.TASK, 0);
        register(Advancement.DisplayData.FrameType.CHALLENGE, 1);
        register(Advancement.DisplayData.FrameType.GOAL, 2);

        register(UnlockRecipesAction.INIT, 0);
        register(UnlockRecipesAction.ADD, 1);
        register(UnlockRecipesAction.REMOVE, 2);

        register(CraftingBookStateType.CRAFTING, 0);
        register(CraftingBookStateType.FURNACE, 1);
        register(CraftingBookStateType.BLAST_FURNACE, 2);
        register(CraftingBookStateType.SMOKER, 3);

        register(AdvancementTabAction.OPENED_TAB, 0);
        register(AdvancementTabAction.CLOSED_SCREEN, 1);

        register(ResourcePackStatus.SUCCESSFULLY_LOADED, 0);
        register(ResourcePackStatus.DECLINED, 1);
        register(ResourcePackStatus.FAILED_DOWNLOAD, 2);
        register(ResourcePackStatus.ACCEPTED, 3);

        register(Hand.MAIN_HAND, 0);
        register(Hand.OFF_HAND, 1);

        register(HandPreference.LEFT_HAND, 0);
        register(HandPreference.RIGHT_HAND, 1);

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
        register(RecipeType.CRAFTING_SPECIAL_SHIELDDECORATION, "minecraft:crafting_special_shielddecoration");
        register(RecipeType.CRAFTING_SPECIAL_SHULKERBOXCOLORING, "minecraft:crafting_special_shulkerboxcoloring");
        register(RecipeType.CRAFTING_SPECIAL_SUSPICIOUSSTEW, "minecraft:crafting_special_suspiciousstew");
        register(RecipeType.SMELTING, "minecraft:smelting");
        register(RecipeType.BLASTING, "minecraft:blasting");
        register(RecipeType.SMOKING, "minecraft:smoking");
        register(RecipeType.CAMPFIRE_COOKING, "minecraft:campfire_cooking");
        register(RecipeType.STONECUTTING, "minecraft:stonecutting");
        register(RecipeType.SMITHING, "minecraft:smithing");

        register(CommandType.ROOT, 0);
        register(CommandType.LITERAL, 1);
        register(CommandType.ARGUMENT, 2);

        register(CommandParser.BOOL, 0);
        register(CommandParser.FLOAT, 1);
        register(CommandParser.DOUBLE, 2);
        register(CommandParser.INTEGER, 3);
        register(CommandParser.LONG, 4);
        register(CommandParser.STRING, 5);
        register(CommandParser.ENTITY, 6);
        register(CommandParser.GAME_PROFILE, 7);
        register(CommandParser.BLOCK_POS, 8);
        register(CommandParser.COLUMN_POS, 9);
        register(CommandParser.VEC3, 10);
        register(CommandParser.VEC2, 11);
        register(CommandParser.BLOCK_STATE, 12);
        register(CommandParser.BLOCK_PREDICATE, 13);
        register(CommandParser.ITEM_STACK, 14);
        register(CommandParser.ITEM_PREDICATE, 15);
        register(CommandParser.COLOR, 16);
        register(CommandParser.COMPONENT, 17);
        register(CommandParser.MESSAGE, 18);
        register(CommandParser.NBT_COMPOUND_TAG, 19);
        register(CommandParser.NBT_TAG, 20);
        register(CommandParser.NBT_PATH, 21);
        register(CommandParser.OBJECTIVE, 22);
        register(CommandParser.OBJECTIVE_CRITERIA, 23);
        register(CommandParser.OPERATION, 24);
        register(CommandParser.PARTICLE, 25);
        register(CommandParser.ANGLE, 26);
        register(CommandParser.ROTATION, 27);
        register(CommandParser.SCOREBOARD_SLOT, 28);
        register(CommandParser.SCORE_HOLDER, 29);
        register(CommandParser.SWIZZLE, 30);
        register(CommandParser.TEAM, 31);
        register(CommandParser.ITEM_SLOT, 32);
        register(CommandParser.RESOURCE_LOCATION, 33);
        register(CommandParser.MOB_EFFECT, 34);
        register(CommandParser.FUNCTION, 35);
        register(CommandParser.ENTITY_ANCHOR, 36);
        register(CommandParser.INT_RANGE, 37);
        register(CommandParser.FLOAT_RANGE, 38);
        register(CommandParser.ITEM_ENCHANTMENT, 39);
        register(CommandParser.ENTITY_SUMMON, 40);
        register(CommandParser.DIMENSION, 41);
        register(CommandParser.TIME, 42);
        register(CommandParser.RESOURCE_OR_TAG, 43);
        register(CommandParser.RESOURCE, 44);
        register(CommandParser.TEMPLATE_MIRROR, 45);
        register(CommandParser.TEMPLATE_ROTATION, 46);
        register(CommandParser.UUID, 47);

        register(SuggestionType.ASK_SERVER, "minecraft:ask_server");
        register(SuggestionType.ALL_RECIPES, "minecraft:all_recipes");
        register(SuggestionType.AVAILABLE_SOUNDS, "minecraft:available_sounds");
        register(SuggestionType.AVAILABLE_BIOMES, "minecraft:available_biomes");
        register(SuggestionType.SUMMONABLE_ENTITIES, "minecraft:summonable_entities");

        register(StringProperties.SINGLE_WORD, 0);
        register(StringProperties.QUOTABLE_PHRASE, 1);
        register(StringProperties.GREEDY_PHRASE, 2);

        register(PositionSourceType.BLOCK, "minecraft:block");
        register(PositionSourceType.ENTITY, "minecraft:entity");
    }

    private MagicValues() {
    }

    private static void register(Enum<?> key, Object value) {
        VALUES.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T key(Class<T> keyType, Object value) {
        for (Map.Entry<Object, List<Object>> entry : VALUES.entrySet()) {
            if (keyType.isAssignableFrom(entry.getKey().getClass())) {
                for (Object val : entry.getValue()) {
                    if (val == value || val.equals(value)) {
                        return (T) entry.getKey();
                    } else if (Number.class.isAssignableFrom(val.getClass()) && Number.class.isAssignableFrom(value.getClass())) {
                        Number num = (Number) val;
                        Number num2 = (Number) value;
                        if (num.doubleValue() == num2.doubleValue()) {
                            return (T) entry.getKey();
                        }
                    } else if (String.class.isAssignableFrom(val.getClass()) && String.class.isAssignableFrom(value.getClass())) {
                        String str = (String) val;
                        String str2 = (String) value;
                        if (str.equalsIgnoreCase(str2)) {
                            return (T) entry.getKey();
                        }
                    }
                }
            }
        }

        throw new UnmappedValueException(value, keyType);
    }

    @SuppressWarnings("unchecked")
    public static <T> T value(Class<T> valueType, Object key) {
        List<Object> values = VALUES.get(key);
        if (values != null) {
            for (Object val : values) {
                if (valueType.isAssignableFrom(val.getClass())) {
                    return (T) val;
                } else if (Number.class.isAssignableFrom(val.getClass())) {
                    if (valueType == Byte.class) {
                        return (T) (Object) ((Number) val).byteValue();
                    } else if (valueType == Short.class) {
                        return (T) (Object) ((Number) val).shortValue();
                    } else if (valueType == Integer.class) {
                        return (T) (Object) ((Number) val).intValue();
                    } else if (valueType == Long.class) {
                        return (T) (Object) ((Number) val).longValue();
                    } else if (valueType == Float.class) {
                        return (T) (Object) ((Number) val).floatValue();
                    } else if (valueType == Double.class) {
                        return (T) (Object) ((Number) val).doubleValue();
                    }
                }
            }
        }

        throw new UnmappedKeyException(key, valueType);
    }
}
