package com.github.steveice10.mc.protocol.data;

import com.github.steveice10.mc.protocol.data.game.BossBarAction;
import com.github.steveice10.mc.protocol.data.game.BossBarColor;
import com.github.steveice10.mc.protocol.data.game.BossBarDivision;
import com.github.steveice10.mc.protocol.data.game.ClientCommand;
import com.github.steveice10.mc.protocol.data.game.MessageType;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntryAction;
import com.github.steveice10.mc.protocol.data.game.ResourcePackStatus;
import com.github.steveice10.mc.protocol.data.game.UnlockRecipesAction;
import com.github.steveice10.mc.protocol.data.game.advancement.Advancement;
import com.github.steveice10.mc.protocol.data.game.command.CommandParser;
import com.github.steveice10.mc.protocol.data.game.command.CommandType;
import com.github.steveice10.mc.protocol.data.game.command.SuggestionType;
import com.github.steveice10.mc.protocol.data.game.command.properties.StringProperties;
import com.github.steveice10.mc.protocol.data.game.entity.EntityEvent;
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
import com.github.steveice10.mc.protocol.data.game.entity.type.PaintingType;
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
import com.github.steveice10.mc.protocol.data.game.level.event.ParticleEvent;
import com.github.steveice10.mc.protocol.data.game.level.event.SmokeEventData;
import com.github.steveice10.mc.protocol.data.game.level.event.SoundEvent;
import com.github.steveice10.mc.protocol.data.game.level.map.MapIconType;
import com.github.steveice10.mc.protocol.data.game.level.notify.DemoMessageValue;
import com.github.steveice10.mc.protocol.data.game.level.notify.EnterCreditsValue;
import com.github.steveice10.mc.protocol.data.game.level.notify.GameEvent;
import com.github.steveice10.mc.protocol.data.game.level.sound.SoundCategory;
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
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MagicValuesTest {
    private Map<Class<? extends Enum<?>>, List<Class<?>>> typeMappings = new HashMap<>();

    private void register(Class<? extends Enum<?>> keyClass, Class<?> valueClass) {
        this.typeMappings.computeIfAbsent(keyClass, k -> new ArrayList<>()).add(valueClass);
    }

    @Before
    public void setup() {
        this.register(HandshakeIntent.class, Integer.class);
        this.register(ClientCommand.class, Integer.class);
        this.register(ChatVisibility.class, Integer.class);
        this.register(PlayerState.class, Integer.class);
        this.register(InteractAction.class, Integer.class);
        this.register(PlayerAction.class, Integer.class);
        this.register(ContainerActionType.class, Integer.class);
        this.register(ClickItemAction.class, Integer.class);
        this.register(ShiftClickItemAction.class, Integer.class);
        this.register(MoveToHotbarAction.class, Integer.class);
        this.register(CreativeGrabAction.class, Integer.class);
        this.register(DropItemAction.class, Integer.class);
        this.register(SpreadItemAction.class, Integer.class);
        this.register(FillStackAction.class, Integer.class);
        this.register(MessageType.class, Integer.class);
        this.register(GameMode.class, Integer.class);
        this.register(Difficulty.class, Integer.class);
        this.register(Animation.class, Integer.class);
        this.register(EntityEvent.class, Integer.class);
        this.register(PositionElement.class, Integer.class);
        this.register(MinecartType.class, Integer.class);
        this.register(PaintingType.class, Integer.class);
        this.register(ScoreboardPosition.class, Integer.class);
        this.register(ObjectiveAction.class, Integer.class);
        this.register(TeamAction.class, Integer.class);
        this.register(ScoreboardAction.class, Integer.class);
        this.register(MapIconType.class, Integer.class);
        this.register(ContainerType.class, Integer.class);
        this.register(BrewingStandProperty.class, Integer.class);
        this.register(EnchantmentTableProperty.class, Integer.class);
        this.register(FurnaceProperty.class, Integer.class);
        this.register(AnvilProperty.class, Integer.class);
        this.register(GameEvent.class, Integer.class);
        this.register(CommandBlockMode.class, Integer.class);
        this.register(UpdateStructureBlockAction.class, Integer.class);
        this.register(UpdateStructureBlockMode.class, Integer.class);
        this.register(StructureRotation.class, Integer.class);
        this.register(StructureMirror.class, Integer.class);
        this.register(DemoMessageValue.class, Integer.class);
        this.register(EnterCreditsValue.class, Integer.class);
        this.register(NoteBlockValueType.class, Integer.class);
        this.register(PistonValueType.class, Integer.class);
        this.register(MobSpawnerValueType.class, Integer.class);
        this.register(ChestValueType.class, Integer.class);
        this.register(EndGatewayValueType.class, Integer.class);
        this.register(GenericBlockValueType.class, Integer.class);
        this.register(PistonValue.class, Integer.class);
        this.register(SoundEvent.class, Integer.class);
        this.register(ParticleEvent.class, Integer.class);
        this.register(SmokeEventData.class, Integer.class);
        this.register(ComposterEventData.class, Integer.class);
        this.register(NameTagVisibility.class, String.class);
        this.register(CollisionRule.class, String.class);
        this.register(ScoreType.class, Integer.class);
        this.register(Advancement.DisplayData.FrameType.class, Integer.class);
        this.register(PlayerListEntryAction.class, Integer.class);
        this.register(UnlockRecipesAction.class, Integer.class);
        this.register(CraftingBookStateType.class, Integer.class);
        this.register(AdvancementTabAction.class, Integer.class);
        this.register(ResourcePackStatus.class, Integer.class);
        this.register(Hand.class, Integer.class);
        this.register(HandPreference.class, Integer.class);
        this.register(BossBarAction.class, Integer.class);
        this.register(BossBarColor.class, Integer.class);
        this.register(BossBarDivision.class, Integer.class);
        this.register(EquipmentSlot.class, Integer.class);
        this.register(RotationOrigin.class, Integer.class);
        this.register(RotationOrigin.class, Integer.class);
        this.register(RecipeType.class, String.class);
        this.register(CommandType.class, Integer.class);
        this.register(CommandParser.class, String.class);
        this.register(SuggestionType.class, String.class);
        this.register(StringProperties.class, Integer.class);
        this.register(SoundCategory.class, Integer.class);
    }

    @Test
    public void testMagicValues() {
        this.typeMappings.forEach((keyClass, valueClasses) -> valueClasses.forEach(valueClass -> {
            try {
                Method valuesMethod = keyClass.getDeclaredMethod("values");
                Enum<?>[] keys = (Enum<?>[]) valuesMethod.invoke(null);

                for (Enum<?> key : keys) {
                    try {
                        Object value = MagicValues.value(valueClass, key);
                        Object mappedKey = MagicValues.key(keyClass, value);

                        assertThat("Mapped key did not match original key.", mappedKey, is(key));
                    } catch (IllegalArgumentException e) {
                        throw new AssertionError("Key \"" + key + "\" of type \"" + keyClass.getName() + "\" is not properly mapped to value type \"" + valueClass.getName() + "\".");
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to test magic values for class pair (" + keyClass.getName() + ", " + valueClass.getName() + ").", e);
            }
        }));
    }
}
