package com.github.steveice10.mc.protocol.packet.ingame.client.window;

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
public class ClientDisplayedRecipePacket implements Packet {
    private @NonNull String recipeId;

    @Override
    public void read(NetInput in) throws IOException {
        this.recipeId = in.readString();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(this.recipeId);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
