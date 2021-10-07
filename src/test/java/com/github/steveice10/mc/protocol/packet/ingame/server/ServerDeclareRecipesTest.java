package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.recipe.Ingredient;
import com.github.steveice10.mc.protocol.data.game.recipe.Recipe;
import com.github.steveice10.mc.protocol.data.game.recipe.RecipeType;
import com.github.steveice10.mc.protocol.data.game.recipe.data.CookedRecipeData;
import com.github.steveice10.mc.protocol.data.game.recipe.data.ShapedRecipeData;
import com.github.steveice10.mc.protocol.data.game.recipe.data.ShapelessRecipeData;
import com.github.steveice10.mc.protocol.data.game.recipe.data.SmithingRecipeData;
import com.github.steveice10.mc.protocol.data.game.recipe.data.StoneCuttingRecipeData;
import com.github.steveice10.mc.protocol.packet.PacketTest;
import org.junit.Before;

public class ServerDeclareRecipesTest extends PacketTest {
    @Before
    public void setup() {
        this.setPackets(
                new ServerDeclareRecipesPacket(
                        new Recipe[]{
                                new Recipe(
                                        RecipeType.CRAFTING_SHAPELESS,
                                        "Recipe1",
                                        new ShapelessRecipeData(
                                                "Group1",
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
                                        "Recipe2",
                                        new ShapedRecipeData(
                                                2,
                                                3,
                                                "Group2",
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
                                                new ItemStack(20)
                                        )
                                ),
                                new Recipe(
                                        RecipeType.SMELTING,
                                        "Recipe3",
                                        new CookedRecipeData(
                                                "Group3",
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
                                        "Recipe4",
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
                                        RecipeType.SMITHING,
                                        "Recipe5",
                                        new SmithingRecipeData(
                                                new Ingredient(new ItemStack[]{
                                                        new ItemStack(10)
                                                }),
                                                new Ingredient(new ItemStack[]{
                                                        new ItemStack(11)
                                                }),
                                                new ItemStack(12)
                                        )
                                )
                        }
                )
        );
    }
}
