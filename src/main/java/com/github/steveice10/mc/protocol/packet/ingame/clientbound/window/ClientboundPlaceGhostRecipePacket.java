package com.github.steveice10.mc.protocol.packet.ingame.clientbound.window;

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
public class ClientboundPlaceGhostRecipePacket implements Packet {
    private final int windowId;
    private final @NonNull String recipeId;

    public ClientboundPlaceGhostRecipePacket(NetInput in) throws IOException {
        this.windowId = in.readByte();
        this.recipeId = in.readString();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeByte(this.windowId);
        out.writeString(this.recipeId);
    }
}
