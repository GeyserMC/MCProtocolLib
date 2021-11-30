package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.UnlockRecipesAction;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.*;

import java.io.IOException;
import java.util.Arrays;

@Data
@With
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientboundRecipePacket implements Packet {
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

    public ClientboundRecipePacket(NetInput in) throws IOException {
        this.action = MagicValues.key(UnlockRecipesAction.class, in.readVarInt());

        this.openCraftingBook = in.readBoolean();
        this.activateCraftingFiltering = in.readBoolean();
        this.openSmeltingBook = in.readBoolean();
        this.activateSmeltingFiltering = in.readBoolean();
        this.openBlastingBook = in.readBoolean();
        this.activateBlastingFiltering = in.readBoolean();
        this.openSmokingBook = in.readBoolean();
        this.activateSmokingFiltering = in.readBoolean();

        if (this.action == UnlockRecipesAction.INIT) {
            this.alreadyKnownRecipes = new String[in.readVarInt()];
            for (int i = 0; i < this.alreadyKnownRecipes.length; i++) {
                this.alreadyKnownRecipes[i] = in.readString();
            }
        } else {
            this.alreadyKnownRecipes = null;
        }

        this.recipes = new String[in.readVarInt()];
        for (int i = 0; i < this.recipes.length; i++) {
            this.recipes[i] = in.readString();
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(MagicValues.value(Integer.class, this.action));

        out.writeBoolean(this.openCraftingBook);
        out.writeBoolean(this.activateCraftingFiltering);
        out.writeBoolean(this.openSmeltingBook);
        out.writeBoolean(this.activateSmeltingFiltering);
        out.writeBoolean(this.openBlastingBook);
        out.writeBoolean(this.activateBlastingFiltering);
        out.writeBoolean(this.openSmokingBook);
        out.writeBoolean(this.activateSmokingFiltering);

        if (this.action == UnlockRecipesAction.INIT) {
            out.writeVarInt(this.alreadyKnownRecipes.length);
            for (String recipeId : this.alreadyKnownRecipes) {
                out.writeString(recipeId);
            }
        }

        out.writeVarInt(this.recipes.length);
        for (String recipeId : this.recipes) {
            out.writeString(recipeId);
        }
    }
}
