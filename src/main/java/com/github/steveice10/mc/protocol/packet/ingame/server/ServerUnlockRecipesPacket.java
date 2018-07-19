package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.UnlockRecipesAction;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerUnlockRecipesPacket extends MinecraftPacket {
    private UnlockRecipesAction action;

    private List<String> recipes;
    private List<String> alreadyKnownRecipes;

    private boolean openCraftingBook;
    private boolean activateCraftingFiltering;
    private boolean openSmeltingBook;
    private boolean activateSmeltingFiltering;

    @SuppressWarnings("unused")
    private ServerUnlockRecipesPacket() {
    }

    private ServerUnlockRecipesPacket(boolean openCraftingBook, boolean activateCraftingFiltering,
                                      boolean openSmeltingBook, boolean activateSmeltingFiltering,
                                      List<String> recipes) {
        this.openCraftingBook = openCraftingBook;
        this.activateCraftingFiltering = activateCraftingFiltering;
        this.openSmeltingBook = openSmeltingBook;
        this.activateSmeltingFiltering = activateSmeltingFiltering;
        this.recipes = recipes;
    }

    public ServerUnlockRecipesPacket(boolean openCraftingBook, boolean activateCraftingFiltering,
                                     boolean openSmeltingBook, boolean activateSmeltingFiltering,
                                     List<String> recipes, List<String> alreadyKnownRecipes) {
        this(openCraftingBook, activateCraftingFiltering, openSmeltingBook, activateSmeltingFiltering, recipes);
        this.action = UnlockRecipesAction.INIT;
        this.alreadyKnownRecipes = alreadyKnownRecipes;
    }

    public ServerUnlockRecipesPacket(boolean openCraftingBook, boolean activateCraftingFiltering,
                                     boolean openSmeltingBook, boolean activateSmeltingFiltering,
                                     List<String> recipes, UnlockRecipesAction action) {
        this(openCraftingBook, activateCraftingFiltering, openSmeltingBook, activateSmeltingFiltering, recipes);
        if(action != UnlockRecipesAction.ADD && action != UnlockRecipesAction.REMOVE) {
            throw new IllegalArgumentException("action must be ADD or REMOVE");
        }
        this.action = action;
    }

    public UnlockRecipesAction getAction() {
        return this.action;
    }

    public List<String> getRecipes() {
        return this.recipes;
    }

    public List<String> getAlreadyKnownRecipes() {
        if(this.action != UnlockRecipesAction.INIT) {
            throw new IllegalStateException("alreadyKnownRecipes is only set if action is " + UnlockRecipesAction.INIT
                    + " but it was " + this.action);
        }
        return this.alreadyKnownRecipes;
    }

    public boolean getOpenCraftingBook() {
        return this.openCraftingBook;
    }

    public boolean getActivateCraftingFiltering() {
        return this.activateCraftingFiltering;
    }

    public boolean getOpenSmeltingBook() {
        return this.openSmeltingBook;
    }

    public boolean getActivateSmeltingFiltering() {
        return this.activateSmeltingFiltering;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.action = MagicValues.key(UnlockRecipesAction.class, in.readVarInt());

        this.openCraftingBook = in.readBoolean();
        this.activateCraftingFiltering = in.readBoolean();
        this.openSmeltingBook = in.readBoolean();
        this.activateSmeltingFiltering = in.readBoolean();

        if(this.action == UnlockRecipesAction.INIT) {
            int size = in.readVarInt();
            this.alreadyKnownRecipes = new ArrayList<>(size);
            for(int i = 0; i < size; i++) {
                this.alreadyKnownRecipes.add(in.readString());
            }
        }

        int size = in.readVarInt();
        this.recipes = new ArrayList<>(size);
        for(int i = 0; i < size; i++) {
            this.recipes.add(in.readString());
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
            out.writeVarInt(this.alreadyKnownRecipes.size());
            for(String recipeId : this.alreadyKnownRecipes) {
                out.writeString(recipeId);
            }
        }

        out.writeVarInt(this.recipes.size());
        for(String recipeId : this.recipes) {
            out.writeString(recipeId);
        }
    }
}
