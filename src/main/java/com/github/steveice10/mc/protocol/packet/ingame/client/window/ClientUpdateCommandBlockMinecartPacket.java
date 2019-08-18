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

import java.io.IOException;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ClientUpdateCommandBlockMinecartPacket implements Packet {
    private int entityId;
    private @NonNull String command;
    private boolean doesTrackOutput;

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.command = in.readString();
        this.doesTrackOutput = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeString(this.command);
        out.writeBoolean(this.doesTrackOutput);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
