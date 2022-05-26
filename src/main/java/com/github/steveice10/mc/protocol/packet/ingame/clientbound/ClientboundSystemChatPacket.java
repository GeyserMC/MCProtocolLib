package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.mc.protocol.data.game.MessageType;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundSystemChatPacket implements Packet {
	private final Component content;
	private final MessageType type;

	public ClientboundSystemChatPacket(NetInput in) throws IOException {
		this.content = DefaultComponentSerializer.get().deserialize(in.readString());
		this.type = in.readEnum(MessageType.VALUES);
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeString(DefaultComponentSerializer.get().serialize(this.content));
		out.writeEnum(this.type);
	}
}
