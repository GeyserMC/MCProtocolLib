package com.github.steveice10.mc.protocol.packet.ingame.client.window;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.window.CraftingBookDataType;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import java.io.IOException;

@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientCraftingBookDataPacket implements Packet {
    @Getter
    private @NonNull CraftingBookDataType type;

    private String recipeId;

    private boolean craftingBookOpen;
    private boolean filterCraftingActive;
    private boolean smeltingBookOpen;
    private boolean filterSmeltingActive;
    private boolean blastingBookOpen;
    private boolean filterBlastingActive;
    private boolean smokingBookOpen;
    private boolean filterSmokingActive;

    public ClientCraftingBookDataPacket(@NonNull String recipeId) {
        this.type = CraftingBookDataType.DISPLAYED_RECIPE;

        this.recipeId = recipeId;
    }

    public ClientCraftingBookDataPacket(boolean craftingBookOpen, boolean filterCraftingActive,
                                        boolean smeltingBookOpen, boolean filterSmeltingActive,
                                        boolean blastingBookOpen, boolean filterBlastingActive,
                                        boolean smokingBookOpen, boolean filterSmokingActive) {
        this.type = CraftingBookDataType.CRAFTING_BOOK_STATUS;

        this.craftingBookOpen = craftingBookOpen;
        this.filterCraftingActive = filterCraftingActive;
        this.smeltingBookOpen = smeltingBookOpen;
        this.filterSmeltingActive = filterSmeltingActive;
        this.blastingBookOpen = blastingBookOpen;
        this.filterBlastingActive = filterBlastingActive;
        this.smokingBookOpen = smokingBookOpen;
        this.filterSmokingActive = filterSmokingActive;
    }

    private void ensureType(CraftingBookDataType type, String what) {
        if (this.type != type) {
            throw new IllegalStateException(what + " is only set when type is " + type + " but it is " + this.type);
        }
    }

    public String getRecipeId() {
        this.ensureType(CraftingBookDataType.DISPLAYED_RECIPE, "recipeId");
        return this.recipeId;
    }

    public boolean isCraftingBookOpen() {
        this.ensureType(CraftingBookDataType.CRAFTING_BOOK_STATUS, "craftingBookOpen");
        return this.craftingBookOpen;
    }

    public boolean isFilterCraftingActive() {
        this.ensureType(CraftingBookDataType.CRAFTING_BOOK_STATUS, "filterCraftingActive");
        return this.filterCraftingActive;
    }

    public boolean isSmeltingBookOpen() {
        this.ensureType(CraftingBookDataType.CRAFTING_BOOK_STATUS, "smeltingBookOpen");
        return this.smeltingBookOpen;
    }

    public boolean isFilterSmeltingActive() {
        this.ensureType(CraftingBookDataType.CRAFTING_BOOK_STATUS, "filterSmeltingActive");
        return this.filterSmeltingActive;
    }

    public boolean isBlastingBookOpen() {
        this.ensureType(CraftingBookDataType.CRAFTING_BOOK_STATUS, "blastingBookOpen");
        return this.blastingBookOpen;
    }

    public boolean isFilterBlastingActive() {
        this.ensureType(CraftingBookDataType.CRAFTING_BOOK_STATUS, "filterBlastingActive");
        return this.filterBlastingActive;
    }

    public boolean isSmokingBookOpen() {
        this.ensureType(CraftingBookDataType.CRAFTING_BOOK_STATUS, "smokingBookOpen");
        return this.smokingBookOpen;
    }

    public boolean isFilterSmokingActive() {
        this.ensureType(CraftingBookDataType.CRAFTING_BOOK_STATUS, "filterSmokingActive");
        return this.filterSmokingActive;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.type = MagicValues.key(CraftingBookDataType.class, in.readVarInt());
        switch(this.type) {
            case DISPLAYED_RECIPE:
                this.recipeId = in.readString();
                break;
            case CRAFTING_BOOK_STATUS:
                this.craftingBookOpen = in.readBoolean();
                this.filterCraftingActive = in.readBoolean();
                this.smeltingBookOpen = in.readBoolean();
                this.filterSmeltingActive = in.readBoolean();
                this.blastingBookOpen = in.readBoolean();
                this.filterBlastingActive = in.readBoolean();
                this.smokingBookOpen = in.readBoolean();
                this.filterSmokingActive = in.readBoolean();
                break;
            default:
                throw new IOException("Unknown crafting book data type: " + this.type);
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(MagicValues.value(Integer.class, this.type));
        switch (this.type) {
            case DISPLAYED_RECIPE:
                out.writeString(this.recipeId);
                break;
            case CRAFTING_BOOK_STATUS:
                out.writeBoolean(this.craftingBookOpen);
                out.writeBoolean(this.filterCraftingActive);
                out.writeBoolean(this.smeltingBookOpen);
                out.writeBoolean(this.filterSmeltingActive);
                out.writeBoolean(this.blastingBookOpen);
                out.writeBoolean(this.filterBlastingActive);
                out.writeBoolean(this.smokingBookOpen);
                out.writeBoolean(this.filterSmokingActive);
                break;
            default:
                throw new IOException("Unknown crafting book data type: " + this.type);
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
