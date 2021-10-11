package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.BossBarAction;
import com.github.steveice10.mc.protocol.data.game.BossBarColor;
import com.github.steveice10.mc.protocol.data.game.BossBarDivision;
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
import java.util.UUID;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerBossBarPacket implements Packet {
    private @NonNull UUID uuid;
    private @NonNull BossBarAction action;

    private Component title;

    private float health;

    private BossBarColor color;
    private BossBarDivision division;

    private boolean darkenSky;
    private boolean playEndMusic;
    private boolean showFog;

    public ServerBossBarPacket(@NonNull UUID uuid) {
        this.uuid = uuid;
        this.action = BossBarAction.REMOVE;
    }

    public ServerBossBarPacket(@NonNull UUID uuid, @NonNull Component title) {
        this.uuid = uuid;
        this.action = BossBarAction.UPDATE_TITLE;

        this.title = title;
    }

    public ServerBossBarPacket(@NonNull UUID uuid, float health) {
        this.uuid = uuid;
        this.action = BossBarAction.UPDATE_HEALTH;

        this.health = health;
    }

    public ServerBossBarPacket(@NonNull UUID uuid, @NonNull BossBarColor color, @NonNull BossBarDivision division) {
        this.uuid = uuid;
        this.action = BossBarAction.UPDATE_STYLE;

        this.color = color;
        this.division = division;
    }

    public ServerBossBarPacket(@NonNull UUID uuid, boolean darkenSky, boolean playEndMusic, boolean showFog) {
        this.uuid = uuid;
        this.action = BossBarAction.UPDATE_FLAGS;

        this.darkenSky = darkenSky;
        this.playEndMusic = playEndMusic;
        this.showFog = showFog;
    }

    public ServerBossBarPacket(@NonNull UUID uuid, @NonNull Component title, float health, @NonNull BossBarColor color,
                               @NonNull BossBarDivision division, boolean darkenSky, boolean playEndMusic, boolean showFog) {
        this.uuid = uuid;
        this.action = BossBarAction.ADD;

        this.title = title;
        this.health = health;
        this.color = color;
        this.division = division;
        this.darkenSky = darkenSky;
        this.playEndMusic = playEndMusic;
        this.showFog = showFog;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.uuid = in.readUUID();
        this.action = MagicValues.key(BossBarAction.class, in.readVarInt());

        if (this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_TITLE) {
            this.title = DefaultComponentSerializer.get().deserialize(in.readString());
        }

        if (this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_HEALTH) {
            this.health = in.readFloat();
        }

        if (this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_STYLE) {
            this.color = MagicValues.key(BossBarColor.class, in.readVarInt());
            this.division = MagicValues.key(BossBarDivision.class, in.readVarInt());
        }

        if (this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_FLAGS) {
            int flags = in.readUnsignedByte();
            this.darkenSky = (flags & 0x1) == 0x1;
            this.playEndMusic = (flags & 0x2) == 0x2;
            this.showFog = (flags & 0x4) == 0x4;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeUUID(this.uuid);
        out.writeVarInt(MagicValues.value(Integer.class, this.action));

        if (this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_TITLE) {
            out.writeString(DefaultComponentSerializer.get().serialize(this.title));
        }

        if (this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_HEALTH) {
            out.writeFloat(this.health);
        }

        if (this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_STYLE) {
            out.writeVarInt(MagicValues.value(Integer.class, this.color));
            out.writeVarInt(MagicValues.value(Integer.class, this.division));
        }

        if (this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_FLAGS) {
            int flags = 0;
            if (this.darkenSky) {
                flags |= 0x1;
            }

            if (this.playEndMusic) {
                flags |= 0x2;
            }

            if (this.showFog) {
                flags |= 0x4;
            }

            out.writeByte(flags);
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
