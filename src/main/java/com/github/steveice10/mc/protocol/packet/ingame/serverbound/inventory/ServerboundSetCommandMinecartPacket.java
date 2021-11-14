package com.github.steveice10.mc.protocol.packet.ingame.serverbound.inventory;

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
public class ServerboundSetCommandMinecartPacket implements Packet {
    private final int entityId;
    private final @NonNull String command;
    private final boolean doesTrackOutput;

    public ServerboundSetCommandMinecartPacket(NetInput in) throws IOException {
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
}
