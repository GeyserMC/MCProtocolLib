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
public class ClientCraftingBookStatePacket implements Packet {
    @Getter
    private @NonNull CraftingBookDataType type;

    private int bookId;
    private boolean filterActive;
    private boolean bookOpen;

    public ClientCraftingBookStatePacket(int bookId, boolean filterActive,
                                        boolean bookOpen) {
        this.type = CraftingBookDataType.CRAFTING_BOOK_STATUS;

        this.bookId = bookId;
        this.filterActive = filterActive;
        this.bookOpen = bookOpen;
    }

    private void ensureType(CraftingBookDataType type, String what) {
        if (this.type != type) {
            throw new IllegalStateException(what + " is only set when type is " + type + " but it is " + this.type);
        }
    }

    public int bookId() {
        this.ensureType(CraftingBookDataType.CRAFTING_BOOK_STATUS, "bookId");
        return this.bookId;
    }

    public boolean isFilterActive() {
        this.ensureType(CraftingBookDataType.CRAFTING_BOOK_STATUS, "filterActive");
        return this.filterActive;
    }

    public boolean isBookOpen() {
        this.ensureType(CraftingBookDataType.CRAFTING_BOOK_STATUS, "bookOpen");
        return this.bookOpen;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.type = MagicValues.key(CraftingBookDataType.class, in.readVarInt());
        switch(this.type) {
            case CRAFTING_BOOK_STATUS:
                this.bookId = in.readVarInt();
                this.filterActive = in.readBoolean();
                this.bookOpen = in.readBoolean();
                break;
            default:
                throw new IOException("Unknown crafting book data type: " + this.type);
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(MagicValues.value(Integer.class, this.type));
        switch (this.type) {
            case CRAFTING_BOOK_STATUS:
                out.writeInt(this.bookId);
                out.writeBoolean(this.filterActive);
                out.writeBoolean(this.bookOpen);
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
