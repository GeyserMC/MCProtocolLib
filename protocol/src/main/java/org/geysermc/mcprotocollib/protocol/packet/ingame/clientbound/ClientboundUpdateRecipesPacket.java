package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.CraftingBookCategory;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.Ingredient;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.Recipe;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.RecipeType;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.data.CookedRecipeData;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.data.RecipeData;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.data.ShapedRecipeData;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.data.ShapelessRecipeData;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.data.SimpleCraftingRecipeData;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.data.SmithingTransformRecipeData;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.data.SmithingTrimRecipeData;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.data.StoneCuttingRecipeData;

@Data
@With
@AllArgsConstructor
public class ClientboundUpdateRecipesPacket implements MinecraftPacket {
    private final @NonNull Recipe[] recipes;

    public ClientboundUpdateRecipesPacket(MinecraftByteBuf buf) {
        this.recipes = new Recipe[buf.readVarInt()];
        for (int i = 0; i < this.recipes.length; i++) {
            String identifier = buf.readResourceLocation();
            RecipeType type = RecipeType.from(buf.readVarInt());
            RecipeData data;
            switch (type) {
                case CRAFTING_SHAPELESS -> {
                    String group = buf.readString();
                    CraftingBookCategory category = CraftingBookCategory.from(buf.readVarInt());
                    Ingredient[] ingredients = new Ingredient[buf.readVarInt()];
                    for (int j = 0; j < ingredients.length; j++) {
                        ingredients[j] = buf.readRecipeIngredient();
                    }

                    ItemStack result = buf.readOptionalItemStack();

                    data = new ShapelessRecipeData(group, category, ingredients, result);
                }
                case CRAFTING_SHAPED -> {
                    String group = buf.readString();
                    CraftingBookCategory category = CraftingBookCategory.from(buf.readVarInt());

                    // ShapedRecipePattern in vanilla
                    int width = buf.readVarInt();
                    int height = buf.readVarInt();
                    Ingredient[] ingredients = new Ingredient[width * height];
                    for (int j = 0; j < ingredients.length; j++) {
                        ingredients[j] = buf.readRecipeIngredient();
                    }

                    ItemStack result = buf.readOptionalItemStack();
                    boolean showNotification = buf.readBoolean();

                    data = new ShapedRecipeData(width, height, group, category, ingredients, result, showNotification);
                }
                case SMELTING, BLASTING, SMOKING, CAMPFIRE_COOKING -> {
                    String group = buf.readString();
                    CraftingBookCategory category = CraftingBookCategory.from(buf.readVarInt());
                    Ingredient ingredient = buf.readRecipeIngredient();
                    ItemStack result = buf.readOptionalItemStack();
                    float experience = buf.readFloat();
                    int cookingTime = buf.readVarInt();

                    data = new CookedRecipeData(group, category, ingredient, result, experience, cookingTime);
                }
                case STONECUTTING -> {
                    String group = buf.readString();
                    Ingredient ingredient = buf.readRecipeIngredient();
                    ItemStack result = buf.readOptionalItemStack();

                    data = new StoneCuttingRecipeData(group, ingredient, result);
                }
                case SMITHING_TRANSFORM -> {
                    Ingredient template = buf.readRecipeIngredient();
                    Ingredient base = buf.readRecipeIngredient();
                    Ingredient addition = buf.readRecipeIngredient();
                    ItemStack result = buf.readOptionalItemStack();

                    data = new SmithingTransformRecipeData(template, base, addition, result);
                }
                case SMITHING_TRIM -> {
                    Ingredient template = buf.readRecipeIngredient();
                    Ingredient base = buf.readRecipeIngredient();
                    Ingredient addition = buf.readRecipeIngredient();

                    data = new SmithingTrimRecipeData(template, base, addition);
                }
                default -> {
                    CraftingBookCategory category = CraftingBookCategory.from(buf.readVarInt());

                    data = new SimpleCraftingRecipeData(category);
                }
            }

            this.recipes[i] = new Recipe(type, identifier, data);
        }
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.recipes.length);
        for (Recipe recipe : this.recipes) {
            buf.writeResourceLocation(recipe.getIdentifier());
            buf.writeVarInt(recipe.getType().ordinal());
            switch (recipe.getType()) {
                case CRAFTING_SHAPELESS -> {
                    ShapelessRecipeData data = (ShapelessRecipeData) recipe.getData();

                    buf.writeString(data.getGroup());
                    buf.writeVarInt(data.getCategory().ordinal());
                    buf.writeVarInt(data.getIngredients().length);
                    for (Ingredient ingredient : data.getIngredients()) {
                        buf.writeRecipeIngredient(ingredient);
                    }

                    buf.writeOptionalItemStack(data.getResult());
                }
                case CRAFTING_SHAPED -> {
                    ShapedRecipeData data = (ShapedRecipeData) recipe.getData();
                    if (data.getIngredients().length != data.getWidth() * data.getHeight()) {
                        throw new IllegalStateException("Shaped recipe must have ingredient count equal to width * height.");
                    }

                    buf.writeString(data.getGroup());
                    buf.writeVarInt(data.getCategory().ordinal());

                    // ShapedRecipePattern in vanilla
                    buf.writeVarInt(data.getWidth());
                    buf.writeVarInt(data.getHeight());
                    for (Ingredient ingredient : data.getIngredients()) {
                        buf.writeRecipeIngredient(ingredient);
                    }

                    buf.writeOptionalItemStack(data.getResult());
                    buf.writeBoolean(data.isShowNotification());
                }
                case SMELTING, BLASTING, SMOKING, CAMPFIRE_COOKING -> {
                    CookedRecipeData data = (CookedRecipeData) recipe.getData();

                    buf.writeString(data.getGroup());
                    buf.writeVarInt(data.getCategory().ordinal());
                    buf.writeRecipeIngredient(data.getIngredient());
                    buf.writeOptionalItemStack(data.getResult());
                    buf.writeFloat(data.getExperience());
                    buf.writeVarInt(data.getCookingTime());
                }
                case STONECUTTING -> {
                    StoneCuttingRecipeData data = (StoneCuttingRecipeData) recipe.getData();

                    buf.writeString(data.getGroup());
                    buf.writeRecipeIngredient(data.getIngredient());
                    buf.writeOptionalItemStack(data.getResult());
                }
                case SMITHING_TRANSFORM -> {
                    SmithingTransformRecipeData data = (SmithingTransformRecipeData) recipe.getData();

                    buf.writeRecipeIngredient(data.getTemplate());
                    buf.writeRecipeIngredient(data.getBase());
                    buf.writeRecipeIngredient(data.getAddition());
                    buf.writeOptionalItemStack(data.getResult());
                }
                case SMITHING_TRIM -> {
                    SmithingTrimRecipeData data = (SmithingTrimRecipeData) recipe.getData();

                    buf.writeRecipeIngredient(data.getTemplate());
                    buf.writeRecipeIngredient(data.getBase());
                    buf.writeRecipeIngredient(data.getAddition());
                }
                default -> {
                    SimpleCraftingRecipeData data = (SimpleCraftingRecipeData) recipe.getData();

                    buf.writeVarInt(data.getCategory().ordinal());
                }
            }
        }
    }
}
