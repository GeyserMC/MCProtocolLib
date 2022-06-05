package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.Identifier;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.recipe.Ingredient;
import com.github.steveice10.mc.protocol.data.game.recipe.Recipe;
import com.github.steveice10.mc.protocol.data.game.recipe.RecipeType;
import com.github.steveice10.mc.protocol.data.game.recipe.data.*;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundUpdateRecipesPacket implements MinecraftPacket {
    private final @NonNull Recipe[] recipes;

    public ClientboundUpdateRecipesPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.recipes = new Recipe[helper.readVarInt(in)];
        for (int i = 0; i < this.recipes.length; i++) {
            RecipeType type = MagicValues.key(RecipeType.class, Identifier.formalize(helper.readString(in)));
            String identifier = helper.readString(in);
            RecipeData data = null;
            switch (type) {
                case CRAFTING_SHAPELESS: {
                    String group = helper.readString(in);
                    Ingredient[] ingredients = new Ingredient[helper.readVarInt(in)];
                    for (int j = 0; j < ingredients.length; j++) {
                        ingredients[j] = helper.readRecipeIngredient(in);
                    }

                    ItemStack result = helper.readItemStack(in);

                    data = new ShapelessRecipeData(group, ingredients, result);
                    break;
                }
                case CRAFTING_SHAPED: {
                    int width = helper.readVarInt(in);
                    int height = helper.readVarInt(in);
                    String group = helper.readString(in);
                    Ingredient[] ingredients = new Ingredient[width * height];
                    for (int j = 0; j < ingredients.length; j++) {
                        ingredients[j] = helper.readRecipeIngredient(in);
                    }

                    ItemStack result = helper.readItemStack(in);

                    data = new ShapedRecipeData(width, height, group, ingredients, result);
                    break;
                }
                case SMELTING:
                case BLASTING:
                case SMOKING:
                case CAMPFIRE_COOKING: {
                    String group = helper.readString(in);
                    Ingredient ingredient = helper.readRecipeIngredient(in);
                    ItemStack result = helper.readItemStack(in);
                    float experience = in.readFloat();
                    int cookingTime = helper.readVarInt(in);

                    data = new CookedRecipeData(group, ingredient, result, experience, cookingTime);
                    break;
                }
                case STONECUTTING: {
                    String group = helper.readString(in);
                    Ingredient ingredient = helper.readRecipeIngredient(in);
                    ItemStack result = helper.readItemStack(in);

                    data = new StoneCuttingRecipeData(group, ingredient, result);
                    break;
                }
                case SMITHING: {
                    Ingredient base = helper.readRecipeIngredient(in);
                    Ingredient addition = helper.readRecipeIngredient(in);
                    ItemStack result = helper.readItemStack(in);

                    data = new SmithingRecipeData(base, addition, result);
                    break;
                }
                default:
                    break;
            }

            this.recipes[i] = new Recipe(type, identifier, data);
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.recipes.length);
        for (Recipe recipe : this.recipes) {
            helper.writeString(out, MagicValues.value(String.class, recipe.getType()));
            helper.writeString(out, recipe.getIdentifier());
            switch (recipe.getType()) {
                case CRAFTING_SHAPELESS: {
                    ShapelessRecipeData data = (ShapelessRecipeData) recipe.getData();

                    helper.writeString(out, data.getGroup());
                    helper.writeVarInt(out, data.getIngredients().length);
                    for (Ingredient ingredient : data.getIngredients()) {
                        helper.writeRecipeIngredient(out, ingredient);
                    }

                    helper.writeItemStack(out, data.getResult());
                    break;
                }
                case CRAFTING_SHAPED: {
                    ShapedRecipeData data = (ShapedRecipeData) recipe.getData();
                    if (data.getIngredients().length != data.getWidth() * data.getHeight()) {
                        throw new IllegalStateException("Shaped recipe must have ingredient count equal to width * height.");
                    }

                    helper.writeVarInt(out, data.getWidth());
                    helper.writeVarInt(out, data.getHeight());
                    helper.writeString(out, data.getGroup());
                    for (Ingredient ingredient : data.getIngredients()) {
                        helper.writeRecipeIngredient(out, ingredient);
                    }

                    helper.writeItemStack(out, data.getResult());
                    break;
                }
                case SMELTING:
                case BLASTING:
                case SMOKING:
                case CAMPFIRE_COOKING: {
                    CookedRecipeData data = (CookedRecipeData) recipe.getData();

                    helper.writeString(out, data.getGroup());
                    helper.writeRecipeIngredient(out, data.getIngredient());
                    helper.writeItemStack(out, data.getResult());
                    out.writeFloat(data.getExperience());
                    helper.writeVarInt(out, data.getCookingTime());
                    break;
                }
                case STONECUTTING: {
                    StoneCuttingRecipeData data = (StoneCuttingRecipeData) recipe.getData();

                    helper.writeString(out, data.getGroup());
                    helper.writeRecipeIngredient(out, data.getIngredient());
                    helper.writeItemStack(out, data.getResult());
                    break;
                }
                case SMITHING: {
                    SmithingRecipeData data = (SmithingRecipeData) recipe.getData();

                    helper.writeRecipeIngredient(out, data.getBase());
                    helper.writeRecipeIngredient(out, data.getAddition());
                    helper.writeItemStack(out, data.getResult());
                    break;
                }
                default:
                    break;
            }
        }
    }
}
