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
public class ClientDisplayedRecipePacket implements Packet {
    @Getter
    private @NonNull CraftingBookDataType type;

    private String recipeId;

    public ClientDisplayedRecipePacket(@NonNull String recipeId) {
        this.type = CraftingBookDataType.DISPLAYED_RECIPE;

        this.recipeId = recipeId;
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

    @Override
    public void read(NetInput in) throws IOException {
        this.type = MagicValues.key(CraftingBookDataType.class, in.readVarInt());
        switch(this.type) {
            case DISPLAYED_RECIPE:
                this.recipeId = in.readString();
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
            default:
                throw new IOException("Unknown crafting book data type: " + this.type);
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
