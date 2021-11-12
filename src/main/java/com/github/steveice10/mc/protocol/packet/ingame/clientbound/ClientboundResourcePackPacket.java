package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;

import javax.annotation.Nullable;
import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundResourcePackPacket implements Packet {
    private final @NonNull String url;
    private final @NonNull String hash;
    private final boolean required;
    private final @Nullable Component prompt;

    public ClientboundResourcePackPacket(NetInput in) throws IOException {
        this.url = in.readString();
        this.hash = in.readString();
        this.required = in.readBoolean();
        if (in.readBoolean()) {
            this.prompt = DefaultComponentSerializer.get().deserialize(in.readString());
        } else {
            this.prompt = null;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(this.url);
        out.writeString(this.hash);
        out.writeBoolean(this.required);
        out.writeBoolean(this.prompt != null);
        if (this.prompt != null) {
            out.writeString(DefaultComponentSerializer.get().serialize(this.prompt));
        }
    }
}
