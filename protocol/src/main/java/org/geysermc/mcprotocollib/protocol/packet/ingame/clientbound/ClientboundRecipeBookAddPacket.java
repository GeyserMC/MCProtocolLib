package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.HolderSet;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.BasicRecipeBookCategory;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.Ingredient;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.display.RecipeDisplay;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.display.RecipeDisplayEntry;

import java.util.List;
import java.util.OptionalInt;

@Data
@With
@AllArgsConstructor
public class ClientboundRecipeBookAddPacket implements MinecraftPacket {
    private final List<Entry> entries;

    public ClientboundRecipeBookAddPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.entries = helper.readList(in, buf -> {
            int id = helper.readVarInt(buf);
            RecipeDisplay display = helper.readRecipeDisplay(buf);

            int optionalInt = helper.readVarInt(buf);
            OptionalInt group = optionalInt == 0 ? OptionalInt.empty() : OptionalInt.of(optionalInt - 1);
            BasicRecipeBookCategory category = BasicRecipeBookCategory.from(helper.readVarInt(buf));
            List<HolderSet> craftingRequirements = helper.readNullable(in, buf1 -> helper.readList(buf1, helper::readHolderSet));

            byte flags = buf.readByte();
            boolean notification = (flags & 1) != 0;
            boolean highlight = (flags & 2) != 0;
            return new Entry(new RecipeDisplayEntry(id, display, group, category, craftingRequirements), notification, highlight);
        });
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeList(out, this.entries, (buf, entry) -> {
            helper.writeVarInt(buf, entry.contents().id());
            helper.writeRecipeDisplay(buf, entry.contents().display());
            helper.writeVarInt(buf, entry.contents().group().isEmpty() ? 0 : entry.contents().group().getAsInt());
            helper.writeVarInt(buf, entry.contents().category().ordinal());
            helper.writeNullable(buf, entry.contents().craftingRequirements(), (buf1, reqs) -> helper.writeList(buf1, reqs, helper::writeHolderSet));

            int flags = 0;
            if (entry.notification()) {
                flags |= 0x1;
            }

            if (entry.highlight()) {
                flags |= 0x2;
            }
            buf.writeByte(flags);
        });
    }

    private record Entry(RecipeDisplayEntry contents, boolean notification, boolean highlight) {
    }
}
