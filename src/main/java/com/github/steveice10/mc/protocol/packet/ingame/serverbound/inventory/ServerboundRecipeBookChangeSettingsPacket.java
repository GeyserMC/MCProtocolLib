package com.github.steveice10.mc.protocol.packet.ingame.serverbound.inventory;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.inventory.CraftingBookStateType;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ServerboundRecipeBookChangeSettingsPacket implements MinecraftPacket {
    private final @NonNull CraftingBookStateType type;
    private final boolean bookOpen;
    private final boolean filterActive;

    public ServerboundRecipeBookChangeSettingsPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.type = CraftingBookStateType.from(helper.readVarInt(in));
        this.bookOpen = in.readBoolean();
        this.filterActive = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.type.ordinal());
        out.writeBoolean(this.bookOpen);
        out.writeBoolean(this.filterActive);
    }
}
