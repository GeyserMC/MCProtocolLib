package org.spacehq.mc.protocol.packet.ingame.server.world;

import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.entity.player.GameMode;
import org.spacehq.mc.protocol.data.game.values.world.notify.*;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerNotifyClientPacket implements Packet {

	private ClientNotification notification;
	private ClientNotificationValue value;

	@SuppressWarnings("unused")
	private ServerNotifyClientPacket() {
	}

	public ServerNotifyClientPacket(ClientNotification notification, ClientNotificationValue value) {
		this.value = value;
	}

	public ClientNotification getNotification() {
		return this.notification;
	}

	public ClientNotificationValue getValue() {
		return this.value;
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.notification = MagicValues.key(ClientNotification.class, in.readUnsignedByte());
		float value = in.readFloat();
		if(this.notification == ClientNotification.CHANGE_GAMEMODE) {
			this.value = MagicValues.key(GameMode.class, (int) value);
		} else if(this.notification == ClientNotification.DEMO_MESSAGE) {
			this.value = MagicValues.key(DemoMessageValue.class, (int) value);
		} else if(this.notification == ClientNotification.RAIN_STRENGTH) {
			this.value = new RainStrengthValue(value);
		} else if(this.notification == ClientNotification.THUNDER_STRENGTH) {
			this.value = new ThunderStrengthValue(value);
		}
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeByte(MagicValues.value(Integer.class, this.notification));
		float value = 0;
		if(this.value instanceof GameMode) {
			value = MagicValues.value(Integer.class, (Enum<?>) this.value);
		}

		if(this.value instanceof DemoMessageValue) {
			value = MagicValues.value(Integer.class, (Enum<?>) this.value);
		}

		if(this.value instanceof RainStrengthValue) {
			value = ((RainStrengthValue) this.value).getStrength();
		}

		if(this.value instanceof ThunderStrengthValue) {
			value = ((ThunderStrengthValue) this.value).getStrength();
		}

		out.writeFloat(value);
	}

	@Override
	public boolean isPriority() {
		return false;
	}

}
