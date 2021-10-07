package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
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
import net.kyori.adventure.text.Component;

import javax.annotation.Nullable;
import java.io.IOException;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerResourcePackSendPacket implements Packet {
    private @NonNull String url;
    private @NonNull String hash;
    private boolean required;
    private @Nullable
    Component prompt;

    @Override
    public void read(NetInput in) throws IOException {
        this.url = in.readString();
        this.hash = in.readString();
        this.required = in.readBoolean();
        if (in.readBoolean()) {
            this.prompt = DefaultComponentSerializer.get().deserialize(in.readString());
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

    @Override
    public boolean isPriority() {
        return false;
    }
}
