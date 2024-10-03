package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.key.Key;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
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
import org.geysermc.mcprotocollib.protocol.data.game.recipe.data.TransmuteRecipeData;

@Data
@With
@AllArgsConstructor
public class ClientboundUpdateRecipesPacket implements MinecraftPacket {
    private final @NonNull Recipe[] recipes;

    public ClientboundUpdateRecipesPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.recipes = new Recipe[helper.readVarInt(in)];
        for (int i = 0; i < this.recipes.length; i++) {
            Key identifier = helper.readResourceLocation(in);
            RecipeType type = RecipeType.from(helper.readVarInt(in));
            RecipeData data;
            switch (type) {
                case CRAFTING_SHAPELESS -> {
                    String group = helper.readString(in);
                    CraftingBookCategory category = CraftingBookCategory.from(helper.readVarInt(in));
                    Ingredient[] ingredients = new Ingredient[helper.readVarInt(in)];
                    for (int j = 0; j < ingredients.length; j++) {
                        ingredients[j] = helper.readRecipeIngredient(in);
                    }

                    ItemStack result = helper.readOptionalItemStack(in);

                    data = new ShapelessRecipeData(group, category, ingredients, result);
                }
                case CRAFTING_SHAPED -> {
                    String group = helper.readString(in);
                    CraftingBookCategory category = CraftingBookCategory.from(helper.readVarInt(in));

                    // ShapedRecipePattern in vanilla
                    int width = helper.readVarInt(in);
                    int height = helper.readVarInt(in);
                    Ingredient[] ingredients = new Ingredient[width * height];
                    for (int j = 0; j < ingredients.length; j++) {
                        ingredients[j] = helper.readRecipeIngredient(in);
                    }

                    ItemStack result = helper.readOptionalItemStack(in);
                    boolean showNotification = in.readBoolean();

                    data = new ShapedRecipeData(width, height, group, category, ingredients, result, showNotification);
                }
                case CRAFTING_TRANSMUTE -> {
                    String group = helper.readString(in);
                    CraftingBookCategory category = CraftingBookCategory.from(helper.readVarInt(in));
                    Ingredient input = helper.readRecipeIngredient(in);
                    Ingredient material = helper.readRecipeIngredient(in);
                    int result = helper.readVarInt(in);

                    data = new TransmuteRecipeData(group, category, input, material, result);
                }
                case SMELTING, BLASTING, SMOKING, CAMPFIRE_COOKING -> {
                    String group = helper.readString(in);
                    CraftingBookCategory category = CraftingBookCategory.from(helper.readVarInt(in));
                    Ingredient ingredient = helper.readRecipeIngredient(in);
                    ItemStack result = helper.readOptionalItemStack(in);
                    float experience = in.readFloat();
                    int cookingTime = helper.readVarInt(in);

                    data = new CookedRecipeData(group, category, ingredient, result, experience, cookingTime);
                }
                case STONECUTTING -> {
                    String group = helper.readString(in);
                    Ingredient ingredient = helper.readRecipeIngredient(in);
                    ItemStack result = helper.readOptionalItemStack(in);

                    data = new StoneCuttingRecipeData(group, ingredient, result);
                }
                case SMITHING_TRANSFORM -> {
                    Ingredient template = helper.readRecipeIngredient(in);
                    Ingredient base = helper.readRecipeIngredient(in);
                    Ingredient addition = helper.readRecipeIngredient(in);
                    ItemStack result = helper.readOptionalItemStack(in);

                    data = new SmithingTransformRecipeData(template, base, addition, result);
                }
                case SMITHING_TRIM -> {
                    Ingredient template = helper.readRecipeIngredient(in);
                    Ingredient base = helper.readRecipeIngredient(in);
                    Ingredient addition = helper.readRecipeIngredient(in);

                    data = new SmithingTrimRecipeData(template, base, addition);
                }
                default -> {
                    CraftingBookCategory category = CraftingBookCategory.from(helper.readVarInt(in));

                    data = new SimpleCraftingRecipeData(category);
                }
            }

            this.recipes[i] = new Recipe(type, identifier, data);
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.recipes.length);
        for (Recipe recipe : this.recipes) {
            helper.writeResourceLocation(out, recipe.getIdentifier());
            helper.writeVarInt(out, recipe.getType().ordinal());
            switch (recipe.getType()) {
                case CRAFTING_SHAPELESS -> {
                    ShapelessRecipeData data = (ShapelessRecipeData) recipe.getData();

                    helper.writeString(out, data.getGroup());
                    helper.writeVarInt(out, data.getCategory().ordinal());
                    helper.writeVarInt(out, data.getIngredients().length);
                    for (Ingredient ingredient : data.getIngredients()) {
                        helper.writeRecipeIngredient(out, ingredient);
                    }

                    helper.writeOptionalItemStack(out, data.getResult());
                }
                case CRAFTING_SHAPED -> {
                    ShapedRecipeData data = (ShapedRecipeData) recipe.getData();
                    if (data.getIngredients().length != data.getWidth() * data.getHeight()) {
                        throw new IllegalStateException("Shaped recipe must have ingredient count equal to width * height.");
                    }

                    helper.writeString(out, data.getGroup());
                    helper.writeVarInt(out, data.getCategory().ordinal());

                    // ShapedRecipePattern in vanilla
                    helper.writeVarInt(out, data.getWidth());
                    helper.writeVarInt(out, data.getHeight());
                    for (Ingredient ingredient : data.getIngredients()) {
                        helper.writeRecipeIngredient(out, ingredient);
                    }

                    helper.writeOptionalItemStack(out, data.getResult());
                    out.writeBoolean(data.isShowNotification());
                }
                case CRAFTING_TRANSMUTE -> {
                    TransmuteRecipeData data = (TransmuteRecipeData) recipe.getData();

                    helper.writeString(out, data.group());
                    helper.writeVarInt(out, data.category().ordinal());
                    helper.writeRecipeIngredient(out, data.input());
                    helper.writeRecipeIngredient(out, data.material());
                    helper.writeVarInt(out, data.result());
                }
                case SMELTING, BLASTING, SMOKING, CAMPFIRE_COOKING -> {
                    CookedRecipeData data = (CookedRecipeData) recipe.getData();

                    helper.writeString(out, data.getGroup());
                    helper.writeVarInt(out, data.getCategory().ordinal());
                    helper.writeRecipeIngredient(out, data.getIngredient());
                    helper.writeOptionalItemStack(out, data.getResult());
                    out.writeFloat(data.getExperience());
                    helper.writeVarInt(out, data.getCookingTime());
                }
                case STONECUTTING -> {
                    StoneCuttingRecipeData data = (StoneCuttingRecipeData) recipe.getData();

                    helper.writeString(out, data.getGroup());
                    helper.writeRecipeIngredient(out, data.getIngredient());
                    helper.writeOptionalItemStack(out, data.getResult());
                }
                case SMITHING_TRANSFORM -> {
                    SmithingTransformRecipeData data = (SmithingTransformRecipeData) recipe.getData();

                    helper.writeRecipeIngredient(out, data.getTemplate());
                    helper.writeRecipeIngredient(out, data.getBase());
                    helper.writeRecipeIngredient(out, data.getAddition());
                    helper.writeOptionalItemStack(out, data.getResult());
                }
                case SMITHING_TRIM -> {
                    SmithingTrimRecipeData data = (SmithingTrimRecipeData) recipe.getData();

                    helper.writeRecipeIngredient(out, data.getTemplate());
                    helper.writeRecipeIngredient(out, data.getBase());
                    helper.writeRecipeIngredient(out, data.getAddition());
                }
                default -> {
                    SimpleCraftingRecipeData data = (SimpleCraftingRecipeData) recipe.getData();

                    helper.writeVarInt(out, data.getCategory().ordinal());
                }
            }
        }
    }
}
