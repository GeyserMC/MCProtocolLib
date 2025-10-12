package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;

@Data
@With
@AllArgsConstructor
public class ClientboundRecipeBookSettingsPacket implements MinecraftPacket {
    private final TypeSettings crafting;
    private final TypeSettings furnace;
    private final TypeSettings blastFurnace;
    private final TypeSettings smoker;

    public ClientboundRecipeBookSettingsPacket(ByteBuf in) {
        this.crafting = new TypeSettings(in.readBoolean(), in.readBoolean());
        this.furnace = new TypeSettings(in.readBoolean(), in.readBoolean());
        this.blastFurnace = new TypeSettings(in.readBoolean(), in.readBoolean());
        this.smoker = new TypeSettings(in.readBoolean(), in.readBoolean());
    }

    @Override
    public void serialize(ByteBuf out) {
        out.writeBoolean(this.crafting.open());
        out.writeBoolean(this.crafting.filtering());
        out.writeBoolean(this.furnace.open());
        out.writeBoolean(this.furnace.filtering());
        out.writeBoolean(this.blastFurnace.open());
        out.writeBoolean(this.blastFurnace.filtering());
        out.writeBoolean(this.smoker.open());
        out.writeBoolean(this.smoker.filtering());
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }

    private record TypeSettings(boolean open, boolean filtering) {
    }
}
