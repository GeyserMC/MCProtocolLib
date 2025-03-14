package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
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

    public ClientboundBossEventPacket(ByteBuf in) {
        this.uuid = MinecraftTypes.readUUID(in);
        this.action = BossBarAction.from(MinecraftTypes.readVarInt(in));

        if (this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_TITLE) {
            this.title = MinecraftTypes.readComponent(in);
        } else {
            this.title = null;
        }

        if (this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_HEALTH) {
            this.health = in.readFloat();
        } else {
            this.health = 0f;
        }

        if (this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_STYLE) {
            this.color = BossBarColor.from(MinecraftTypes.readVarInt(in));
            this.division = BossBarDivision.from(MinecraftTypes.readVarInt(in));
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
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeUUID(out, this.uuid);
        MinecraftTypes.writeVarInt(out, this.action.ordinal());

        if (this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_TITLE) {
            MinecraftTypes.writeComponent(out, this.title);
        }

        if (this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_HEALTH) {
            out.writeFloat(this.health);
        }

        if (this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_STYLE) {
            MinecraftTypes.writeVarInt(out, this.color.ordinal());
            MinecraftTypes.writeVarInt(out, this.division.ordinal());
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
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
