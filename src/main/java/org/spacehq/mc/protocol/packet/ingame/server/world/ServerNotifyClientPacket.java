package org.spacehq.mc.protocol.packet.ingame.server.world;

import org.spacehq.mc.protocol.data.MagicValues;
import org.spacehq.mc.protocol.data.game.entity.player.GameMode;
import org.spacehq.mc.protocol.data.game.world.notify.ClientNotification;
import org.spacehq.mc.protocol.data.game.world.notify.ClientNotificationValue;
import org.spacehq.mc.protocol.data.game.world.notify.DemoMessageValue;
import org.spacehq.mc.protocol.data.game.world.notify.EnterCreditsValue;
import org.spacehq.mc.protocol.data.game.world.notify.RainStrengthValue;
import org.spacehq.mc.protocol.data.game.world.notify.ThunderStrengthValue;
import org.spacehq.mc.protocol.util.ReflectionToString;
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
        this.notification = notification;
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
        } else if(this.notification == ClientNotification.ENTER_CREDITS) {
            this.value = MagicValues.key(EnterCreditsValue.class, (int) value);
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
        if(this.value instanceof Enum<?>) {
            value = MagicValues.value(Integer.class, (Enum<?>) this.value);
        } else if(this.value instanceof RainStrengthValue) {
            value = ((RainStrengthValue) this.value).getStrength();
        } else if(this.value instanceof ThunderStrengthValue) {
            value = ((ThunderStrengthValue) this.value).getStrength();
        }

        out.writeFloat(value);
    }

    @Override
    public boolean isPriority() {
        return false;
    }

    @Override
    public String toString() {
        return ReflectionToString.toString(this);
    }
}
