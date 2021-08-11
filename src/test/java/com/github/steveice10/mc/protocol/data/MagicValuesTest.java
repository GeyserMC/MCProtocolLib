package com.github.steveice10.mc.protocol.data;

import com.github.steveice10.mc.protocol.data.game.BossBarAction;
import com.github.steveice10.mc.protocol.data.game.BossBarColor;
import com.github.steveice10.mc.protocol.data.game.BossBarDivision;
import com.github.steveice10.mc.protocol.data.game.ClientRequest;
import com.github.steveice10.mc.protocol.data.game.MessageType;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntryAction;
import com.github.steveice10.mc.protocol.data.game.ResourcePackStatus;
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
import com.github.steveice10.mc.protocol.data.game.entity.object.HangingDirection;
import com.github.steveice10.mc.protocol.data.game.entity.object.MinecartType;
import com.github.steveice10.mc.protocol.data.game.entity.player.Animation;
import com.github.steveice10.mc.protocol.data.game.entity.player.BlockBreakStage;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.data.game.entity.player.HandPreference;
import com.github.steveice10.mc.protocol.data.game.entity.player.InteractAction;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerAction;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerState;
import com.github.steveice10.mc.protocol.data.game.entity.player.PositionElement;
import com.github.steveice10.mc.protocol.data.game.entity.type.EntityType;
import com.github.steveice10.mc.protocol.data.game.entity.type.PaintingType;
import com.github.steveice10.mc.protocol.data.game.entity.type.WeatherEntityType;
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
import com.github.steveice10.mc.protocol.data.game.window.CraftingBookStateType;
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
import com.github.steveice10.mc.protocol.data.game.world.block.BlockFace;
import com.github.steveice10.mc.protocol.data.game.world.block.CommandBlockMode;
import com.github.steveice10.mc.protocol.data.game.world.block.StructureMirror;
import com.github.steveice10.mc.protocol.data.game.world.block.StructureRotation;
import com.github.steveice10.mc.protocol.data.game.world.block.UpdatedTileType;
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
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class MagicValuesTest {
    private Map<Class<? extends Enum<?>>, List<Class<?>>> typeMappings = new HashMap<>();

    private void register(Class<? extends Enum<?>> keyClass, Class<?> valueClass) {
        this.typeMappings.computeIfAbsent(keyClass, k -> new ArrayList<>()).add(valueClass);
    }

    @Before
    public void setup() {
        this.register(Pose.class, Integer.class);
        this.register(AttributeType.class, String.class);
        this.register(ModifierType.class, UUID.class);
        this.register(ModifierOperation.class, Integer.class);
        this.register(MetadataType.class, Integer.class);
        this.register(HandshakeIntent.class, Integer.class);
        this.register(ClientRequest.class, Integer.class);
        this.register(ChatVisibility.class, Integer.class);
        this.register(PlayerState.class, Integer.class);
        this.register(InteractAction.class, Integer.class);
        this.register(PlayerAction.class, Integer.class);
        this.register(WindowAction.class, Integer.class);
        this.register(ClickItemParam.class, Integer.class);
        this.register(ShiftClickItemParam.class, Integer.class);
        this.register(MoveToHotbarParam.class, Integer.class);
        this.register(CreativeGrabParam.class, Integer.class);
        this.register(DropItemParam.class, Integer.class);
        this.register(SpreadItemParam.class, Integer.class);
        this.register(FillStackParam.class, Integer.class);
        this.register(MessageType.class, Integer.class);
        this.register(GameMode.class, Integer.class);
        this.register(Difficulty.class, Integer.class);
        this.register(Animation.class, Integer.class);
        this.register(Effect.class, Integer.class);
        this.register(EntityStatus.class, Integer.class);
        this.register(PositionElement.class, Integer.class);
        this.register(WeatherEntityType.class, Integer.class);
        this.register(EntityType.class, Integer.class);
        this.register(MinecartType.class, Integer.class);
        this.register(HangingDirection.class, Integer.class);
        this.register(PaintingType.class, Integer.class);
        this.register(ScoreboardPosition.class, Integer.class);
        this.register(ObjectiveAction.class, Integer.class);
        this.register(TeamAction.class, Integer.class);
        this.register(ScoreboardAction.class, Integer.class);
        this.register(MapIconType.class, Integer.class);
        this.register(WindowType.class, Integer.class);
        this.register(BrewingStandProperty.class, Integer.class);
        this.register(EnchantmentTableProperty.class, Integer.class);
        this.register(FurnaceProperty.class, Integer.class);
        this.register(AnvilProperty.class, Integer.class);
        this.register(BlockBreakStage.class, Integer.class);
        this.register(UpdatedTileType.class, Integer.class);
        this.register(ClientNotification.class, Integer.class);
        this.register(CommandBlockMode.class, Integer.class);
        this.register(UpdateStructureBlockAction.class, Integer.class);
        this.register(UpdateStructureBlockMode.class, Integer.class);
        this.register(StructureRotation.class, Integer.class);
        this.register(StructureMirror.class, Integer.class);
        this.register(DemoMessageValue.class, Integer.class);
        this.register(EnterCreditsValue.class, Integer.class);
        this.register(GenericStatistic.class, Integer.class);
        this.register(StatisticCategory.class, Integer.class);
        this.register(NoteBlockValueType.class, Integer.class);
        this.register(PistonValueType.class, Integer.class);
        this.register(MobSpawnerValueType.class, Integer.class);
        this.register(ChestValueType.class, Integer.class);
        this.register(EndGatewayValueType.class, Integer.class);
        this.register(GenericBlockValueType.class, Integer.class);
        this.register(PistonValue.class, Integer.class);
        this.register(SoundEffect.class, Integer.class);
        this.register(ParticleEffect.class, Integer.class);
        this.register(SmokeEffectData.class, Integer.class);
        this.register(ComposterEffectData.class, Integer.class);
        this.register(NameTagVisibility.class, String.class);
        this.register(CollisionRule.class, String.class);
        this.register(TeamColor.class, Integer.class);
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
        this.register(BlockFace.class, Integer.class);
        this.register(EquipmentSlot.class, Integer.class);
        this.register(RotationOrigin.class, Integer.class);
        this.register(RotationOrigin.class, Integer.class);
        this.register(RecipeType.class, String.class);
        this.register(CommandType.class, Integer.class);
        this.register(CommandParser.class, String.class);
        this.register(SuggestionType.class, String.class);
        this.register(StringProperties.class, Integer.class);
        this.register(SoundCategory.class, Integer.class);
        this.register(BuiltinSound.class, Integer.class);
        this.register(BuiltinSound.class, String.class);
    }

    @Test
    public void testMagicValues() {
        this.typeMappings.forEach((keyClass, valueClasses) -> {
            valueClasses.forEach(valueClass -> {
                try {
                    Method valuesMethod = keyClass.getDeclaredMethod("values");
                    Enum<?>[] keys = (Enum<?>[]) valuesMethod.invoke(null);

                    for(Enum<?> key : keys) {
                        try {
                            Object value = MagicValues.value(valueClass, key);
                            Object mappedKey = MagicValues.key(keyClass, value);

                            assertThat("Mapped key did not match original key.", mappedKey, is(key));
                        } catch(IllegalArgumentException e) {
                            throw new AssertionError("Key \"" + key + "\" of type \"" + keyClass.getName() + "\" is not properly mapped to value type \"" + valueClass.getName() + "\".");
                        }
                    }
                } catch(Exception e) {
                    throw new RuntimeException("Failed to test magic values for class pair (" + keyClass.getName() + ", " + valueClass.getName() + ").", e);
                }
            });
        });
    }
}
