package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.UnlockRecipesAction;

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

    public ClientboundRecipePacket(MinecraftByteBuf buf) {
        this.action = UnlockRecipesAction.from(buf.readVarInt());

        this.openCraftingBook = buf.readBoolean();
        this.activateCraftingFiltering = buf.readBoolean();
        this.openSmeltingBook = buf.readBoolean();
        this.activateSmeltingFiltering = buf.readBoolean();
        this.openBlastingBook = buf.readBoolean();
        this.activateBlastingFiltering = buf.readBoolean();
        this.openSmokingBook = buf.readBoolean();
        this.activateSmokingFiltering = buf.readBoolean();

        if (this.action == UnlockRecipesAction.INIT) {
            this.alreadyKnownRecipes = new String[buf.readVarInt()];
            for (int i = 0; i < this.alreadyKnownRecipes.length; i++) {
                this.alreadyKnownRecipes[i] = buf.readString();
            }
        } else {
            this.alreadyKnownRecipes = null;
        }

        this.recipes = new String[buf.readVarInt()];
        for (int i = 0; i < this.recipes.length; i++) {
            this.recipes[i] = buf.readString();
        }
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.action.ordinal());

        buf.writeBoolean(this.openCraftingBook);
        buf.writeBoolean(this.activateCraftingFiltering);
        buf.writeBoolean(this.openSmeltingBook);
        buf.writeBoolean(this.activateSmeltingFiltering);
        buf.writeBoolean(this.openBlastingBook);
        buf.writeBoolean(this.activateBlastingFiltering);
        buf.writeBoolean(this.openSmokingBook);
        buf.writeBoolean(this.activateSmokingFiltering);

        if (this.action == UnlockRecipesAction.INIT) {
            buf.writeVarInt(this.alreadyKnownRecipes.length);
            for (String recipeId : this.alreadyKnownRecipes) {
                buf.writeString(recipeId);
            }
        }

        buf.writeVarInt(this.recipes.length);
        for (String recipeId : this.recipes) {
            buf.writeString(recipeId);
        }
    }
}
