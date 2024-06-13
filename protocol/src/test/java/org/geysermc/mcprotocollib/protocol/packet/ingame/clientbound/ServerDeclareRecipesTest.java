package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import net.kyori.adventure.key.Key;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.CraftingBookCategory;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.Ingredient;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.Recipe;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.RecipeType;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.data.CookedRecipeData;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.data.ShapedRecipeData;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.data.ShapelessRecipeData;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.data.SmithingTransformRecipeData;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.data.StoneCuttingRecipeData;
import org.geysermc.mcprotocollib.protocol.packet.PacketTest;
import org.junit.jupiter.api.BeforeEach;

public class ServerDeclareRecipesTest extends PacketTest {
    @BeforeEach
    public void setup() {
        this.setPackets(
                new ClientboundUpdateRecipesPacket(
                        new Recipe[]{
                                new Recipe(
                                        RecipeType.CRAFTING_SHAPELESS,
                                        Key.key("minecraft:recipe1"),
                                        new ShapelessRecipeData(
                                                "Group1",
                                                CraftingBookCategory.MISC,
                                                new Ingredient[]{
                                                        new Ingredient(new ItemStack[]{
                                                                new ItemStack(0)
                                                        })
                                                },
                                                new ItemStack(10)
                                        )
                                ),
                                new Recipe(
                                        RecipeType.CRAFTING_SHAPED,
                                        Key.key("minecraft:recipe2"),
                                        new ShapedRecipeData(
                                                2,
                                                3,
                                                "Group2",
                                                CraftingBookCategory.BUILDING,
                                                new Ingredient[]{
                                                        new Ingredient(new ItemStack[]{
                                                                new ItemStack(1)
                                                        }),
                                                        new Ingredient(new ItemStack[]{
                                                                new ItemStack(2)
                                                        }),
                                                        new Ingredient(new ItemStack[]{
                                                                new ItemStack(3)
                                                        }),
                                                        new Ingredient(new ItemStack[]{
                                                                new ItemStack(4)
                                                        }),
                                                        new Ingredient(new ItemStack[]{
                                                                new ItemStack(5)
                                                        }),
                                                        new Ingredient(new ItemStack[]{
                                                                new ItemStack(6)
                                                        })
                                                },
                                                new ItemStack(20),
                                                true
                                        )
                                ),
                                new Recipe(
                                        RecipeType.SMELTING,
                                        Key.key("minecraft:recipe3"),
                                        new CookedRecipeData(
                                                "Group3",
                                                CraftingBookCategory.EQUIPMENT,
                                                new Ingredient(new ItemStack[]{
                                                        new ItemStack(7)
                                                }),
                                                new ItemStack(30),
                                                10.5f,
                                                10
                                        )
                                ),
                                new Recipe(
                                        RecipeType.STONECUTTING,
                                        Key.key("minecraft:recipe4"),
                                        new StoneCuttingRecipeData(
                                                "Group4",
                                                new Ingredient(new ItemStack[]{
                                                        new ItemStack(8),
                                                        new ItemStack(9)
                                                }),
                                                new ItemStack(40)
                                        )
                                ),
                                new Recipe(
                                        RecipeType.SMITHING_TRANSFORM,
                                        Key.key("minecraft:recipe5"),
                                        new SmithingTransformRecipeData(
                                                new Ingredient(new ItemStack[]{
                                                        new ItemStack(10)
                                                }),
                                                new Ingredient(new ItemStack[]{
                                                        new ItemStack(11)
                                                }),
                                                new Ingredient(new ItemStack[]{
                                                        new ItemStack(12)
                                                }),
                                                new ItemStack(13)
                                        )
                                )
                        }
                )
        );
    }
}
