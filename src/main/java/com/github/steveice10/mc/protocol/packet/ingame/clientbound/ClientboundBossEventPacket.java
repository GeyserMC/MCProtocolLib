package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.BossBarAction;
import com.github.steveice10.mc.protocol.data.game.BossBarColor;
import com.github.steveice10.mc.protocol.data.game.BossBarDivision;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.*;
import net.kyori.adventure.text.Component;

import java.io.IOException;
import java.util.UUID;

@Data
@With
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientboundBossEventPacket implements Packet {
    private final @NonNull UUID uuid;
    private final @NonNull BossBarAction action;

    private final Component title;

    private final float health;

    private final BossBarColor color;
    private final BossBarDivision division;

    private final boolean darkenSky;
    private final boolean playEndMusic;
    private final boolean showFog;

    public ClientboundBossEventPacket(@NonNull UUID uuid) {
        this(uuid, BossBarAction.REMOVE, null, 0f, null, null, false, false, false);
    }

    public ClientboundBossEventPacket(@NonNull UUID uuid, @NonNull Component title) {
        this(uuid, BossBarAction.UPDATE_TITLE, title, 0f, null, null, false, false, false);
    }

    public ClientboundBossEventPacket(@NonNull UUID uuid, float health) {
        this(uuid, BossBarAction.UPDATE_HEALTH, null, health, null, null, false, false, false);
    }

    public ClientboundBossEventPacket(@NonNull UUID uuid, @NonNull BossBarColor color, @NonNull BossBarDivision division) {
        this(uuid, BossBarAction.UPDATE_STYLE, null, 0f, color, division, false, false, false);
    }

    public ClientboundBossEventPacket(@NonNull UUID uuid, boolean darkenSky, boolean playEndMusic, boolean showFog) {
        this(uuid, BossBarAction.UPDATE_FLAGS, null, 0f, null, null, darkenSky, playEndMusic, showFog);
    }

    public ClientboundBossEventPacket(@NonNull UUID uuid, @NonNull Component title, float health, @NonNull BossBarColor color,
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

    public ClientboundBossEventPacket(NetInput in) throws IOException {
        this.uuid = in.readUUID();
        this.action = MagicValues.key(BossBarAction.class, in.readVarInt());

        if (this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_TITLE) {
            this.title = DefaultComponentSerializer.get().deserialize(in.readString());
        } else {
            this.title = null;
        }

        if (this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_HEALTH) {
            this.health = in.readFloat();
        } else {
            this.health = 0f;
        }

        if (this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_STYLE) {
            this.color = MagicValues.key(BossBarColor.class, in.readVarInt());
            this.division = MagicValues.key(BossBarDivision.class, in.readVarInt());
        } else {
            this.color = null;
            this.division = null;
        }

        if (this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_FLAGS) {
            int flags = in.readUnsignedByte();
            this.darkenSky = (flags & 0x1) == 0x1;
            this.playEndMusic = (flags & 0x2) == 0x2;
            this.showFog = (flags & 0x4) == 0x4;
        } else {
            this.darkenSky = false;
            this.playEndMusic = false;
            this.showFog = false;
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
}
