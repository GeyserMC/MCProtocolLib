package com.github.steveice10.mc.protocol.packet.ingame.client.window;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.window.CraftingBookDataType;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public class ClientCraftingBookDataPacket extends MinecraftPacket {
    private CraftingBookDataType type;
    private int recipeId;
    private boolean craftingBookOpen;
    private boolean filterActive;

    @SuppressWarnings("unused")
    private ClientCraftingBookDataPacket() {
    }

    public ClientCraftingBookDataPacket(int recipeId) {
        this.type = CraftingBookDataType.DISPLAYED_RECIPE;
        this.recipeId = recipeId;
    }

    public ClientCraftingBookDataPacket(boolean craftingBookOpen, boolean filterActive) {
        this.type = CraftingBookDataType.CRAFTING_BOOK_STATUS;
        this.craftingBookOpen = craftingBookOpen;
        this.filterActive = filterActive;
    }

    public CraftingBookDataType getType() {
        return type;
    }

    private void ensureType(CraftingBookDataType type, String what) {
        if(this.type != type) {
            throw new IllegalStateException(what + " is only set when type is " + type + " but it is " + this.type);
        }
    }

    public int getRecipeId() {
        ensureType(CraftingBookDataType.DISPLAYED_RECIPE, "recipeId");
        return recipeId;
    }

    public boolean isCraftingBookOpen() {
        ensureType(CraftingBookDataType.CRAFTING_BOOK_STATUS, "craftingBookOpen");
        return craftingBookOpen;
    }

    public boolean isFilterActive() {
        ensureType(CraftingBookDataType.CRAFTING_BOOK_STATUS, "filterActive");
        return filterActive;
    }

    @Override
    public void read(NetInput in) throws IOException {
        switch(this.type = MagicValues.key(CraftingBookDataType.class, in.readVarInt())) {
            case DISPLAYED_RECIPE:
                this.recipeId = in.readInt();
                break;
            case CRAFTING_BOOK_STATUS:
                this.craftingBookOpen = in.readBoolean();
                this.filterActive = in.readBoolean();
                break;
            default:
                throw new IOException("Unknown crafting book data type: " + this.type);
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(MagicValues.value(Integer.class, this.type));
        switch(this.type) {
            case DISPLAYED_RECIPE:
                out.writeInt(this.recipeId);
                break;
            case CRAFTING_BOOK_STATUS:
                out.writeBoolean(this.craftingBookOpen);
                out.writeBoolean(this.filterActive);
                break;
            default:
                throw new IOException("Unknown crafting book data type: " + this.type);
        }
    }
}
