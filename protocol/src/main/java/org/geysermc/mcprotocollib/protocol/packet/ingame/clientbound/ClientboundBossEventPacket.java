package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.BossBarAction;
import org.geysermc.mcprotocollib.protocol.data.game.BossBarColor;
import org.geysermc.mcprotocollib.protocol.data.game.BossBarDivision;

import java.util.UUID;

@Data
@With
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientboundBossEventPacket implements MinecraftPacket {
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

    public ClientboundBossEventPacket(MinecraftByteBuf buf) {
        this.uuid = buf.readUUID();
        this.action = BossBarAction.from(buf.readVarInt());

        if (this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_TITLE) {
            this.title = buf.readComponent();
        } else {
            this.title = null;
        }

        if (this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_HEALTH) {
            this.health = buf.readFloat();
        } else {
            this.health = 0f;
        }

        if (this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_STYLE) {
            this.color = BossBarColor.from(buf.readVarInt());
            this.division = BossBarDivision.from(buf.readVarInt());
        } else {
            this.color = null;
            this.division = null;
        }

        if (this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_FLAGS) {
            int flags = buf.readUnsignedByte();
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
    public void serialize(MinecraftByteBuf buf) {
        buf.writeUUID(this.uuid);
        buf.writeVarInt(this.action.ordinal());

        if (this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_TITLE) {
            buf.writeComponent(this.title);
        }

        if (this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_HEALTH) {
            buf.writeFloat(this.health);
        }

        if (this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_STYLE) {
            buf.writeVarInt(this.color.ordinal());
            buf.writeVarInt(this.division.ordinal());
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

            buf.writeByte(flags);
        }
    }
}
