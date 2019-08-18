package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.UnlockRecipesAction;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.io.IOException;
import java.util.Arrays;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerUnlockRecipesPacket implements Packet {
    private @NonNull UnlockRecipesAction action;

    private @NonNull String[] recipes;
    private boolean openCraftingBook;
    private boolean activateCraftingFiltering;
    private boolean openSmeltingBook;
    private boolean activateSmeltingFiltering;

    private String[] alreadyKnownRecipes;

    public ServerUnlockRecipesPacket(@NonNull String[] recipes,
                                     boolean openCraftingBook, boolean activateCraftingFiltering,
                                     boolean openSmeltingBook, boolean activateSmeltingFiltering,
                                     @NonNull UnlockRecipesAction action) {
        if(action != UnlockRecipesAction.ADD && action != UnlockRecipesAction.REMOVE) {
            throw new IllegalArgumentException("Action must be ADD or REMOVE.");
        }

        this.action = action;
        this.recipes = Arrays.copyOf(recipes, recipes.length);
        this.openCraftingBook = openCraftingBook;
        this.activateCraftingFiltering = activateCraftingFiltering;
        this.openSmeltingBook = openSmeltingBook;
        this.activateSmeltingFiltering = activateSmeltingFiltering;
    }

    public ServerUnlockRecipesPacket(@NonNull String[] recipes,
                                     boolean openCraftingBook, boolean activateCraftingFiltering,
                                     boolean openSmeltingBook, boolean activateSmeltingFiltering,
                                     @NonNull String[] alreadyKnownRecipes) {
        this.action = UnlockRecipesAction.INIT;
        this.recipes = Arrays.copyOf(recipes, recipes.length);
        this.openCraftingBook = openCraftingBook;
        this.activateCraftingFiltering = activateCraftingFiltering;
        this.openSmeltingBook = openSmeltingBook;
        this.activateSmeltingFiltering = activateSmeltingFiltering;

        this.alreadyKnownRecipes = Arrays.copyOf(alreadyKnownRecipes, alreadyKnownRecipes.length);
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.action = MagicValues.key(UnlockRecipesAction.class, in.readVarInt());

        this.openCraftingBook = in.readBoolean();
        this.activateCraftingFiltering = in.readBoolean();
        this.openSmeltingBook = in.readBoolean();
        this.activateSmeltingFiltering = in.readBoolean();

        if(this.action == UnlockRecipesAction.INIT) {
            this.alreadyKnownRecipes = new String[in.readVarInt()];
            for(int i = 0; i < this.alreadyKnownRecipes.length; i++) {
                this.alreadyKnownRecipes[i] = in.readString();
            }
        }

        this.recipes = new String[in.readVarInt()];
        for(int i = 0; i < this.recipes.length; i++) {
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

        if(this.action == UnlockRecipesAction.INIT) {
            out.writeVarInt(this.alreadyKnownRecipes.length);
            for(String recipeId : this.alreadyKnownRecipes) {
                out.writeString(recipeId);
            }
        }

        out.writeVarInt(this.recipes.length);
        for(String recipeId : this.recipes) {
            out.writeString(recipeId);
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
