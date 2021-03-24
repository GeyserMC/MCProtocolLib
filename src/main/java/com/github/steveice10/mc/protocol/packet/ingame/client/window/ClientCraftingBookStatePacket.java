package com.github.steveice10.mc.protocol.packet.ingame.client.window;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.window.CraftingBookStateType;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.With;

import java.io.IOException;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ClientCraftingBookStatePacket implements Packet {
    private @NonNull CraftingBookStateType type;
    private boolean bookOpen;
    private boolean filterActive;

    @Override
    public void read(NetInput in) throws IOException {
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

    @Override
    public boolean isPriority() {
        return false;
    }
}
