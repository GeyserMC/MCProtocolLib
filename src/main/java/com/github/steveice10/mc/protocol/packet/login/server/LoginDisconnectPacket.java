package com.github.steveice10.mc.protocol.packet.login.server;

import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.data.message.MessageSerializer;
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
public class LoginDisconnectPacket implements Packet {
    private @NonNull Message reason;

    public LoginDisconnectPacket(String text) {
        this(MessageSerializer.fromString(text));
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.reason = MessageSerializer.fromString(in.readString());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(MessageSerializer.toJsonString(this.reason));
    }

    @Override
    public boolean isPriority() {
        return true;
    }
}
