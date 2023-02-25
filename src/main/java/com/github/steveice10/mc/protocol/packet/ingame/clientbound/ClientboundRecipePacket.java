package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.UnlockRecipesAction;
import io.netty.buffer.ByteBuf;
import lombok.*;

import java.io.IOException;
import java.util.Arrays;

@Data
@With
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientboundRecipePacket implements MinecraftPacket {
    private final @NonNull UnlockRecipesAction action;

    private final @NonNull String[] recipes;
    private final boolean openCraftingBook;
    private final boolean activateCraftingFiltering;
    private final boolean openSmeltingBook;
    private final boolean activateSmeltingFiltering;
    private final boolean openBlastingBook;
    private final boolean activateBlastingFiltering;
    private final boolean openSmokingBook;
    private final boolean activateSmokingFiltering;

    private final String[] alreadyKnownRecipes;

    public ClientboundRecipePacket(@NonNull String[] recipes,
                                   boolean openCraftingBook, boolean activateCraftingFiltering,
                                   boolean openSmeltingBook, boolean activateSmeltingFiltering,
                                   boolean openBlastingBook, boolean activateBlastingFiltering,
                                   boolean openSmokingBook, boolean activateSmokingFiltering,
                                   @NonNull UnlockRecipesAction action) {
        if (action != UnlockRecipesAction.ADD && action != UnlockRecipesAction.REMOVE) {
            throw new IllegalArgumentException("Action must be ADD or REMOVE.");
        }

        this.action = action;
        this.recipes = Arrays.copyOf(recipes, recipes.length);
        this.openCraftingBook = openCraftingBook;
        this.activateCraftingFiltering = activateCraftingFiltering;
        this.openSmeltingBook = openSmeltingBook;
        this.activateSmeltingFiltering = activateSmeltingFiltering;
        this.openBlastingBook = openBlastingBook;
        this.activateBlastingFiltering = activateBlastingFiltering;
        this.openSmokingBook = openSmokingBook;
        this.activateSmokingFiltering = activateSmokingFiltering;

        this.alreadyKnownRecipes = null;
    }

    public ClientboundRecipePacket(@NonNull String[] recipes,
                                   boolean openCraftingBook, boolean activateCraftingFiltering,
                                   boolean openSmeltingBook, boolean activateSmeltingFiltering,
                                   boolean openBlastingBook, boolean activateBlastingFiltering,
                                   boolean openSmokingBook, boolean activateSmokingFiltering,
                                   @NonNull String[] alreadyKnownRecipes) {
        this.action = UnlockRecipesAction.INIT;
        this.recipes = Arrays.copyOf(recipes, recipes.length);
        this.openCraftingBook = openCraftingBook;
        this.activateCraftingFiltering = activateCraftingFiltering;
        this.openSmeltingBook = openSmeltingBook;
        this.activateSmeltingFiltering = activateSmeltingFiltering;
        this.openBlastingBook = openBlastingBook;
        this.activateBlastingFiltering = activateBlastingFiltering;
        this.openSmokingBook = openSmokingBook;
        this.activateSmokingFiltering = activateSmokingFiltering;

        this.alreadyKnownRecipes = Arrays.copyOf(alreadyKnownRecipes, alreadyKnownRecipes.length);
    }

    public ClientboundRecipePacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.action = UnlockRecipesAction.from(helper.readVarInt(in));

        this.openCraftingBook = in.readBoolean();
        this.activateCraftingFiltering = in.readBoolean();
        this.openSmeltingBook = in.readBoolean();
        this.activateSmeltingFiltering = in.readBoolean();
        this.openBlastingBook = in.readBoolean();
        this.activateBlastingFiltering = in.readBoolean();
        this.openSmokingBook = in.readBoolean();
        this.activateSmokingFiltering = in.readBoolean();

        if (this.action == UnlockRecipesAction.INIT) {
            this.alreadyKnownRecipes = new String[helper.readVarInt(in)];
            for (int i = 0; i < this.alreadyKnownRecipes.length; i++) {
                this.alreadyKnownRecipes[i] = helper.readString(in);
            }
        } else {
            this.alreadyKnownRecipes = null;
        }

        this.recipes = new String[helper.readVarInt(in)];
        for (int i = 0; i < this.recipes.length; i++) {
            this.recipes[i] = helper.readString(in);
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.action.ordinal());

        out.writeBoolean(this.openCraftingBook);
        out.writeBoolean(this.activateCraftingFiltering);
        out.writeBoolean(this.openSmeltingBook);
        out.writeBoolean(this.activateSmeltingFiltering);
        out.writeBoolean(this.openBlastingBook);
        out.writeBoolean(this.activateBlastingFiltering);
        out.writeBoolean(this.openSmokingBook);
        out.writeBoolean(this.activateSmokingFiltering);

        if (this.action == UnlockRecipesAction.INIT) {
            helper.writeVarInt(out, this.alreadyKnownRecipes.length);
            for (String recipeId : this.alreadyKnownRecipes) {
                helper.writeString(out, recipeId);
            }
        }

        helper.writeVarInt(out, this.recipes.length);
        for (String recipeId : this.recipes) {
            helper.writeString(out, recipeId);
        }
    }
}
