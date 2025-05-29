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
    public void serialize(ByteBuf buf) {
        buf.writeBoolean(this.crafting.open());
        buf.writeBoolean(this.crafting.filtering());
        buf.writeBoolean(this.furnace.open());
        buf.writeBoolean(this.furnace.filtering());
        buf.writeBoolean(this.blastFurnace.open());
        buf.writeBoolean(this.blastFurnace.filtering());
        buf.writeBoolean(this.smoker.open());
        buf.writeBoolean(this.smoker.filtering());
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }

    private record TypeSettings(boolean open, boolean filtering) {
    }
}
