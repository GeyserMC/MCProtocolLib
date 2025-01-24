package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.HolderSet;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.Ingredient;
import org.geysermc.mcprotocollib.protocol.data.game.recipe.display.slot.ItemStackSlotDisplay;
import org.geysermc.mcprotocollib.protocol.packet.PacketTest;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerDeclareRecipesTest extends PacketTest {
    @BeforeEach
    public void setup() {
        this.setPackets(
                new ClientboundUpdateRecipesPacket(
                        new HashMap<>(){{
//                            put(Key.key("smithing_addition"), new int[]{829, 837, 833, 830, 831, 671, 827, 828, 835, 838}); // Uncomment when Key comparison works
                        }},
                        new ArrayList<>(){{
                            add(new ClientboundUpdateRecipesPacket.SelectableRecipe(
                                new Ingredient(new HolderSet(new int[]{6})),
                                new ItemStackSlotDisplay(new ItemStack(662, 2, null))
                            ));
                        }}
                )
        );
    }
}
