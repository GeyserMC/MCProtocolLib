package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.recipe.CraftingBookCategory;
import com.github.steveice10.mc.protocol.data.game.recipe.Ingredient;
import com.github.steveice10.mc.protocol.data.game.recipe.Recipe;
import com.github.steveice10.mc.protocol.data.game.recipe.RecipeType;
import com.github.steveice10.mc.protocol.data.game.recipe.data.*;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundUpdateRecipesPacket implements MinecraftPacket {
    private final @NotNull Recipe[] recipes;

    public ClientboundUpdateRecipesPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.recipes = new Recipe[helper.readVarInt(in)];
        for (int i = 0; i < this.recipes.length; i++) {
            RecipeType type = RecipeType.from(helper.readResourceLocation(in));
            String identifier = helper.readResourceLocation(in);
            RecipeData data;
            switch (type) {
                case CRAFTING_SHAPELESS: {
                    String group = helper.readString(in);
                    CraftingBookCategory category = CraftingBookCategory.from(helper.readVarInt(in));
                    Ingredient[] ingredients = new Ingredient[helper.readVarInt(in)];
                    for (int j = 0; j < ingredients.length; j++) {
                        ingredients[j] = helper.readRecipeIngredient(in);
                    }

                    ItemStack result = helper.readItemStack(in);

                    data = new ShapelessRecipeData(group, category, ingredients, result);
                    break;
                }
                case CRAFTING_SHAPED: {
                    int width = helper.readVarInt(in);
                    int height = helper.readVarInt(in);
                    String group = helper.readString(in);
                    CraftingBookCategory category = CraftingBookCategory.from(helper.readVarInt(in));
                    Ingredient[] ingredients = new Ingredient[width * height];
                    for (int j = 0; j < ingredients.length; j++) {
                        ingredients[j] = helper.readRecipeIngredient(in);
                    }

                    ItemStack result = helper.readItemStack(in);
                    boolean showNotification = in.readBoolean();

                    data = new ShapedRecipeData(width, height, group, category, ingredients, result, showNotification);
                    break;
                }
                case SMELTING:
                case BLASTING:
                case SMOKING:
                case CAMPFIRE_COOKING: {
                    String group = helper.readString(in);
                    CraftingBookCategory category = CraftingBookCategory.from(helper.readVarInt(in));
                    Ingredient ingredient = helper.readRecipeIngredient(in);
                    ItemStack result = helper.readItemStack(in);
                    float experience = in.readFloat();
                    int cookingTime = helper.readVarInt(in);

                    data = new CookedRecipeData(group, category, ingredient, result, experience, cookingTime);
                    break;
                }
                case STONECUTTING: {
                    String group = helper.readString(in);
                    Ingredient ingredient = helper.readRecipeIngredient(in);
                    ItemStack result = helper.readItemStack(in);

                    data = new StoneCuttingRecipeData(group, ingredient, result);
                    break;
                }
                case SMITHING_TRANSFORM: {
                    Ingredient template = helper.readRecipeIngredient(in);
                    Ingredient base = helper.readRecipeIngredient(in);
                    Ingredient addition = helper.readRecipeIngredient(in);
                    ItemStack result = helper.readItemStack(in);

                    data = new SmithingTransformRecipeData(template, base, addition, result);
                    break;
                }
                case SMITHING_TRIM: {
                    Ingredient template = helper.readRecipeIngredient(in);
                    Ingredient base = helper.readRecipeIngredient(in);
                    Ingredient addition = helper.readRecipeIngredient(in);

                    data = new SmithingTrimRecipeData(template, base, addition);
                    break;
                }
                default: {
                    CraftingBookCategory category = CraftingBookCategory.from(helper.readVarInt(in));

                    data = new SimpleCraftingRecipeData(category);
                    break;
                }
            }

            this.recipes[i] = new Recipe(type, identifier, data);
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.recipes.length);
        for (Recipe recipe : this.recipes) {
            helper.writeResourceLocation(out, recipe.type().getResourceLocation());
            helper.writeResourceLocation(out, recipe.identifier());
            switch (recipe.type()) {
                case CRAFTING_SHAPELESS: {
                    ShapelessRecipeData data = (ShapelessRecipeData) recipe.data();

                    helper.writeString(out, data.group());
                    helper.writeVarInt(out, data.category().ordinal());
                    helper.writeVarInt(out, data.ingredients().length);
                    for (Ingredient ingredient : data.ingredients()) {
                        helper.writeRecipeIngredient(out, ingredient);
                    }

                    helper.writeItemStack(out, data.result());
                    break;
                }
                case CRAFTING_SHAPED: {
                    ShapedRecipeData data = (ShapedRecipeData) recipe.data();
                    if (data.ingredients().length != data.width() * data.height()) {
                        throw new IllegalStateException("Shaped recipe must have ingredient count equal to width * height.");
                    }

                    helper.writeVarInt(out, data.width());
                    helper.writeVarInt(out, data.height());
                    helper.writeString(out, data.group());
                    helper.writeVarInt(out, data.category().ordinal());
                    for (Ingredient ingredient : data.ingredients()) {
                        helper.writeRecipeIngredient(out, ingredient);
                    }

                    helper.writeItemStack(out, data.result());
                    out.writeBoolean(data.showNotification());
                    break;
                }
                case SMELTING:
                case BLASTING:
                case SMOKING:
                case CAMPFIRE_COOKING: {
                    CookedRecipeData data = (CookedRecipeData) recipe.data();

                    helper.writeString(out, data.group());
                    helper.writeVarInt(out, data.category().ordinal());
                    helper.writeRecipeIngredient(out, data.ingredient());
                    helper.writeItemStack(out, data.result());
                    out.writeFloat(data.experience());
                    helper.writeVarInt(out, data.cookingTime());
                    break;
                }
                case STONECUTTING: {
                    StoneCuttingRecipeData data = (StoneCuttingRecipeData) recipe.data();

                    helper.writeString(out, data.group());
                    helper.writeRecipeIngredient(out, data.ingredient());
                    helper.writeItemStack(out, data.result());
                    break;
                }
                case SMITHING_TRANSFORM: {
                    SmithingTransformRecipeData data = (SmithingTransformRecipeData) recipe.data();

                    helper.writeRecipeIngredient(out, data.template());
                    helper.writeRecipeIngredient(out, data.base());
                    helper.writeRecipeIngredient(out, data.addition());
                    helper.writeItemStack(out, data.result());
                    break;
                }
                case SMITHING_TRIM: {
                    SmithingTrimRecipeData data = (SmithingTrimRecipeData) recipe.data();

                    helper.writeRecipeIngredient(out, data.template());
                    helper.writeRecipeIngredient(out, data.base());
                    helper.writeRecipeIngredient(out, data.addition());
                    break;
                }
                default: {
                    SimpleCraftingRecipeData data = (SimpleCraftingRecipeData) recipe.data();

                    helper.writeVarInt(out, data.category().ordinal());
                    break;
                }
            }
        }
    }
}
