package com.github.steveice10.mc.protocol.packet.ingame.serverbound.inventory;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.inventory.CraftingBookStateType;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ServerboundRecipeBookChangeSettingsPacket implements Packet {
    private final @NonNull CraftingBookStateType type;
    private final boolean bookOpen;
    private final boolean filterActive;

    public ServerboundRecipeBookChangeSettingsPacket(NetInput in) throws IOException {
        this.type = MagicValues.key(CraftingBookStateType.class, in.readVarInt());
        this.bookOpen = in.readBoolean();
        this.filterActive = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(MagicValues.value(Integer.class, this.type));
        out.writeBoolean(this.bookOpen);
        out.writeBoolean(this.filterActive);
    }
}
