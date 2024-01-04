package org.geysermc.mc.protocol.packet.ingame.serverbound.inventory;

import org.geysermc.mc.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mc.protocol.codec.MinecraftPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

@Data
@With
@AllArgsConstructor
public class ServerboundPlaceRecipePacket implements MinecraftPacket {
    private final int containerId;
    private final @NonNull String recipeId;
    private final boolean makeAll;

    public ServerboundPlaceRecipePacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.containerId = in.readByte();
        this.recipeId = helper.readString(in);
        this.makeAll = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        out.writeByte(this.containerId);
        helper.writeString(out, this.recipeId);
        out.writeBoolean(this.makeAll);
    }
}
