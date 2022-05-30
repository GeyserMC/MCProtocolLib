package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.data.game.level.notify.*;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

@Data
@With
@AllArgsConstructor
public class ClientboundGameEventPacket implements MinecraftPacket {
    private final @NonNull GameEvent notification;
    private final GameEventValue value;

    public ClientboundGameEventPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.notification = MagicValues.key(GameEvent.class, in.readUnsignedByte());
        float value = in.readFloat();
        // TODO: Handle this in MinecraftCodecHelper
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
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        out.writeByte(MagicValues.value(Integer.class, this.notification));
        float value = 0;
        // TODO: Handle this in MinecraftCodecHelper
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
