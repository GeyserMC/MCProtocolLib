package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.data.game.level.notify.DemoMessageValue;
import com.github.steveice10.mc.protocol.data.game.level.notify.EnterCreditsValue;
import com.github.steveice10.mc.protocol.data.game.level.notify.GameEvent;
import com.github.steveice10.mc.protocol.data.game.level.notify.GameEventValue;
import com.github.steveice10.mc.protocol.data.game.level.notify.RainStrengthValue;
import com.github.steveice10.mc.protocol.data.game.level.notify.RespawnScreenValue;
import com.github.steveice10.mc.protocol.data.game.level.notify.ThunderStrengthValue;
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
public class ClientboundGameEventPacket implements Packet {
    private final @NonNull GameEvent notification;
    private final GameEventValue value;

    public ClientboundGameEventPacket(NetInput in) throws IOException {
        this.notification = MagicValues.key(GameEvent.class, in.readUnsignedByte());
        float value = in.readFloat();
        if (this.notification == GameEvent.CHANGE_GAMEMODE) {
            this.value = MagicValues.key(GameMode.class, ((int) value == -1) ? 255 : (int) value); // https://bugs.mojang.com/browse/MC-189885 - since we read as a float this bug doesn't apply here
        } else if (this.notification == GameEvent.DEMO_MESSAGE) {
            this.value = MagicValues.key(DemoMessageValue.class, (int) value);
        } else if (this.notification == GameEvent.ENTER_CREDITS) {
            this.value = MagicValues.key(EnterCreditsValue.class, (int) value);
        } else if (this.notification == GameEvent.ENABLE_RESPAWN_SCREEN) {
            this.value = MagicValues.key(RespawnScreenValue.class, (int) value);
        } else if (this.notification == GameEvent.RAIN_STRENGTH) {
            this.value = new RainStrengthValue(value);
        } else if (this.notification == GameEvent.THUNDER_STRENGTH) {
            this.value = new ThunderStrengthValue(value);
        } else {
            this.value = null;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeByte(MagicValues.value(Integer.class, this.notification));
        float value = 0;
        if (this.value instanceof GameMode && this.value == GameMode.UNKNOWN) {
            value = -1;
        } else if (this.value instanceof Enum<?>) {
            value = MagicValues.value(Integer.class, (Enum<?>) this.value);
        } else if (this.value instanceof RainStrengthValue) {
            value = ((RainStrengthValue) this.value).getStrength();
        } else if (this.value instanceof ThunderStrengthValue) {
            value = ((ThunderStrengthValue) this.value).getStrength();
        }

        out.writeFloat(value);
    }
}
