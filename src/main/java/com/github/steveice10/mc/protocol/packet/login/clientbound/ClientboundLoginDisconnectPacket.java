package com.github.steveice10.mc.protocol.packet.login.clientbound;

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

import java.io.IOException;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ClientboundLoginDisconnectPacket implements Packet {
    private @NonNull Component reason;

    public ClientboundLoginDisconnectPacket(String text) {
        this(DefaultComponentSerializer.get().deserialize(text));
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.reason = DefaultComponentSerializer.get().deserialize(in.readString());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(DefaultComponentSerializer.get().serialize(this.reason));
    }

    @Override
    public boolean isPriority() {
        return true;
    }
}
