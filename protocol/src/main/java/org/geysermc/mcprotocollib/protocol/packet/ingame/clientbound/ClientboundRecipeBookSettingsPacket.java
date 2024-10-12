package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.inventory.CraftingBookStateType;

import java.util.EnumMap;
import java.util.Map;

@Data
@With
@AllArgsConstructor
public class ClientboundRecipeBookSettingsPacket implements MinecraftPacket {
    private final Map<CraftingBookStateType, TypeSettings> states;

    public ClientboundRecipeBookSettingsPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.states = new EnumMap<>(CraftingBookStateType.class);

        for (CraftingBookStateType type : CraftingBookStateType.values()) {
            boolean open = in.readBoolean();
            boolean filtering = in.readBoolean();
            if (open || filtering) {
                this.states.put(type, new TypeSettings(open, filtering));
            }
        }
    }

    @Override
    public void serialize(ByteBuf buf, MinecraftCodecHelper helper) {
        for (CraftingBookStateType type : CraftingBookStateType.values()) {
            TypeSettings typeSettings = this.states.get(type);
            buf.writeBoolean(typeSettings == null ? false : typeSettings.open());
            buf.writeBoolean(typeSettings == null ? false : typeSettings.filtering());
        }
    }

    private record TypeSettings(boolean open, boolean filtering) {
    }
}
