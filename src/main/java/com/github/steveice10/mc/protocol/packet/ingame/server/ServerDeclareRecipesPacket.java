package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.Identifier;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.recipe.Ingredient;
import com.github.steveice10.mc.protocol.data.game.recipe.Recipe;
import com.github.steveice10.mc.protocol.data.game.recipe.RecipeType;
import com.github.steveice10.mc.protocol.data.game.recipe.data.CookedRecipeData;
import com.github.steveice10.mc.protocol.data.game.recipe.data.RecipeData;
import com.github.steveice10.mc.protocol.data.game.recipe.data.ShapedRecipeData;
import com.github.steveice10.mc.protocol.data.game.recipe.data.ShapelessRecipeData;
import com.github.steveice10.mc.protocol.data.game.recipe.data.SmithingRecipeData;
import com.github.steveice10.mc.protocol.data.game.recipe.data.StoneCuttingRecipeData;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.With;

import java.io.IOException;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerDeclareRecipesPacket implements Packet {
    private @NonNull Recipe[] recipes;

    @Override
    public void read(NetInput in) throws IOException {
        this.recipes = new Recipe[in.readVarInt()];
        for (int i = 0; i < this.recipes.length; i++) {
            RecipeType type = MagicValues.key(RecipeType.class, Identifier.formalize(in.readString()));
            String identifier = in.readString();
            RecipeData data = null;
            switch (type) {
                case CRAFTING_SHAPELESS: {
                    String group = in.readString();
                    Ingredient[] ingredients = new Ingredient[in.readVarInt()];
                    for (int j = 0; j < ingredients.length; j++) {
                        ingredients[j] = this.readIngredient(in);
                    }

                    ItemStack result = ItemStack.read(in);

                    data = new ShapelessRecipeData(group, ingredients, result);
                    break;
                }
                case CRAFTING_SHAPED: {
                    int width = in.readVarInt();
                    int height = in.readVarInt();
                    String group = in.readString();
                    Ingredient[] ingredients = new Ingredient[width * height];
                    for (int j = 0; j < ingredients.length; j++) {
                        ingredients[j] = this.readIngredient(in);
                    }

                    ItemStack result = ItemStack.read(in);

                    data = new ShapedRecipeData(width, height, group, ingredients, result);
                    break;
                }
                case SMELTING:
                case BLASTING:
                case SMOKING:
                case CAMPFIRE_COOKING: {
                    String group = in.readString();
                    Ingredient ingredient = this.readIngredient(in);
                    ItemStack result = ItemStack.read(in);
                    float experience = in.readFloat();
                    int cookingTime = in.readVarInt();

                    data = new CookedRecipeData(group, ingredient, result, experience, cookingTime);
                    break;
                }
                case STONECUTTING: {
                    String group = in.readString();
                    Ingredient ingredient = this.readIngredient(in);
                    ItemStack result = ItemStack.read(in);

                    data = new StoneCuttingRecipeData(group, ingredient, result);
                    break;
                }
                case SMITHING: {
                    Ingredient base = this.readIngredient(in);
                    Ingredient addition = this.readIngredient(in);
                    ItemStack result = ItemStack.read(in);

                    data = new SmithingRecipeData(base, addition, result);
                    break;
                }
                default:
                    break;
            }

            this.recipes[i] = new Recipe(type, identifier, data);
        }
    }

    private Ingredient readIngredient(NetInput in) throws IOException {
        ItemStack[] options = new ItemStack[in.readVarInt()];
        for (int i = 0; i < options.length; i++) {
            options[i] = ItemStack.read(in);
        }

        return new Ingredient(options);
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.recipes.length);
        for (Recipe recipe : this.recipes) {
            out.writeString(MagicValues.value(String.class, recipe.getType()));
            out.writeString(recipe.getIdentifier());
            switch (recipe.getType()) {
                case CRAFTING_SHAPELESS: {
                    ShapelessRecipeData data = (ShapelessRecipeData) recipe.getData();

                    out.writeString(data.getGroup());
                    out.writeVarInt(data.getIngredients().length);
                    for (Ingredient ingredient : data.getIngredients()) {
                        this.writeIngredient(out, ingredient);
                    }

                    ItemStack.write(out, data.getResult());
                    break;
                }
                case CRAFTING_SHAPED: {
                    ShapedRecipeData data = (ShapedRecipeData) recipe.getData();
                    if (data.getIngredients().length != data.getWidth() * data.getHeight()) {
                        throw new IllegalStateException("Shaped recipe must have ingredient count equal to width * height.");
                    }

                    out.writeVarInt(data.getWidth());
                    out.writeVarInt(data.getHeight());
                    out.writeString(data.getGroup());
                    for (Ingredient ingredient : data.getIngredients()) {
                        this.writeIngredient(out, ingredient);
                    }

                    ItemStack.write(out, data.getResult());
                    break;
                }
                case SMELTING:
                case BLASTING:
                case SMOKING:
                case CAMPFIRE_COOKING: {
                    CookedRecipeData data = (CookedRecipeData) recipe.getData();

                    out.writeString(data.getGroup());
                    this.writeIngredient(out, data.getIngredient());
                    ItemStack.write(out, data.getResult());
                    out.writeFloat(data.getExperience());
                    out.writeVarInt(data.getCookingTime());
                    break;
                }
                case STONECUTTING: {
                    StoneCuttingRecipeData data = (StoneCuttingRecipeData) recipe.getData();

                    out.writeString(data.getGroup());
                    this.writeIngredient(out, data.getIngredient());
                    ItemStack.write(out, data.getResult());
                    break;
                }
                case SMITHING: {
                    SmithingRecipeData data = (SmithingRecipeData) recipe.getData();

                    this.writeIngredient(out, data.getBase());
                    this.writeIngredient(out, data.getAddition());
                    ItemStack.write(out, data.getResult());
                    break;
                }
                default:
                    break;
            }
        }
    }

    private void writeIngredient(NetOutput out, Ingredient ingredient) throws IOException {
        out.writeVarInt(ingredient.getOptions().length);
        for (ItemStack option : ingredient.getOptions()) {
            ItemStack.write(out, option);
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
