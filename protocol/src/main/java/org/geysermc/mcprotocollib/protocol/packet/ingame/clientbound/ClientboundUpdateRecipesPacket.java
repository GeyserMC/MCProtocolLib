package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.key.Key;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.Ingredient;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.display.slot.SlotDisplay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@With
@AllArgsConstructor
public class ClientboundUpdateRecipesPacket implements MinecraftPacket {
    private final Map<Key, int[]> itemSets;
    private final List<SelectableRecipe> stonecutterRecipes;

    public ClientboundUpdateRecipesPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.itemSets = new HashMap<>();
        int itemCount = helper.readVarInt(in);
        for (int i = 0; i < itemCount; i++) {
            Key propertySetType = helper.readResourceLocation(in);
            int[] propertySet = new int[helper.readVarInt(in)];
            for (int j = 0; j < propertySet.length; j++) {
                propertySet[j] = helper.readVarInt(in);
            }
            this.itemSets.put(propertySetType, propertySet);
        }

        this.stonecutterRecipes = helper.readList(in, buf -> {
            Ingredient input = helper.readRecipeIngredient(buf);
            SlotDisplay recipe = helper.readSlotDisplay(buf);
            return new SelectableRecipe(input, recipe);
        });
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.itemSets.size());
        for (Map.Entry<Key, int[]> itemSet : this.itemSets.entrySet()) {
            helper.writeResourceLocation(out, itemSet.getKey());

            helper.writeVarInt(out, itemSet.getValue().length);
            for (int property : itemSet.getValue()) {
                helper.writeVarInt(out, property);
            }
        }

        helper.writeList(out, this.stonecutterRecipes, (buf, recipes) -> {
            helper.writeRecipeIngredient(buf, recipes.input());
            helper.writeSlotDisplay(buf, recipes.recipe());
        });
    }

    public record SelectableRecipe(Ingredient input, SlotDisplay recipe) {
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
