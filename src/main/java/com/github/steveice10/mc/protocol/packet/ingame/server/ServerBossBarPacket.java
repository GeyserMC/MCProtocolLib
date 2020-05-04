package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.BossBarAction;
import com.github.steveice10.mc.protocol.data.game.BossBarColor;
import com.github.steveice10.mc.protocol.data.game.BossBarDivision;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;
import java.util.UUID;

public class ServerBossBarPacket extends MinecraftPacket {
    private UUID uuid;
    private BossBarAction action;

    private String title;
    private float health;
    private BossBarColor color;
    private BossBarDivision division;
    private boolean darkenSky;
    private boolean dragonBar;

    @SuppressWarnings("unused")
    private ServerBossBarPacket() {
    }

    public ServerBossBarPacket(UUID uuid, BossBarAction action, String title, float health, BossBarColor color, BossBarDivision division, boolean darkenSky, boolean dragonBar, boolean escape) {
        this.uuid = uuid;
        this.action = BossBarAction.ADD;

        this.title = escape ? ServerChatPacket.escapeText(title) : title;
        this.health = health;
        this.color = color;
        this.division = division;
        this.darkenSky = darkenSky;
        this.dragonBar = dragonBar;
    }

    public ServerBossBarPacket(UUID uuid) {
        this.uuid = uuid;
        this.action = BossBarAction.REMOVE;
    }

    public ServerBossBarPacket(UUID uuid, BossBarAction action, float health) {
        this.uuid = uuid;
        this.action = BossBarAction.UPDATE_HEALTH;

        this.health = health;
    }

    public ServerBossBarPacket(UUID uuid, BossBarAction action, String title, boolean escape) {
        this.uuid = uuid;
        this.action = BossBarAction.UPDATE_TITLE;

        this.title = escape ? ServerChatPacket.escapeText(title) : title;
    }

    public ServerBossBarPacket(UUID uuid, BossBarAction action, BossBarColor color, BossBarDivision division) {
        this.uuid = uuid;
        this.action = BossBarAction.UPDATE_STYLE;

        this.color = color;
        this.division = division;
    }

    public ServerBossBarPacket(UUID uuid, BossBarAction action, boolean darkenSky, boolean dragonBar) {
        this.uuid = uuid;
        this.action = BossBarAction.UPDATE_FLAGS;

        this.darkenSky = darkenSky;
        this.dragonBar = dragonBar;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public BossBarAction getAction() {
        return this.action;
    }

    public String getTitle() {
        return this.title;
    }

    public float getHealth() {
        return this.health;
    }

    public BossBarColor getColor() {
        return this.color;
    }

    public BossBarDivision getDivision() {
        return this.division;
    }

    public boolean getDarkenSky() {
        return this.darkenSky;
    }

    public boolean isDragonBar() {
        return this.dragonBar;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.uuid = in.readUUID();
        this.action = MagicValues.key(BossBarAction.class, in.readVarInt());

        if(this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_TITLE) {
            this.title = in.readString();
        }

        if(this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_HEALTH) {
            this.health = in.readFloat();
        }

        if(this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_STYLE) {
            this.color = MagicValues.key(BossBarColor.class, in.readVarInt());
            this.division = MagicValues.key(BossBarDivision.class, in.readVarInt());
        }

        if(this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_FLAGS) {
            int flags = in.readUnsignedByte();
            this.darkenSky = (flags & 0x1) == 0x1;
            this.dragonBar = (flags & 0x2) == 0x2;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeUUID(this.uuid);
        out.writeVarInt(MagicValues.value(Integer.class, this.action));

        if(this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_TITLE) {
            out.writeString(this.title);
        }

        if(this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_HEALTH) {
            out.writeFloat(this.health);
        }

        if(this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_STYLE) {
            out.writeVarInt(MagicValues.value(Integer.class, this.color));
            out.writeVarInt(MagicValues.value(Integer.class, this.division));
        }

        if(this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_FLAGS) {
            int flags = 0;
            if(this.darkenSky) {
                flags |= 0x1;
            }

            if(this.dragonBar) {
                flags |= 0x2;
            }

            out.writeByte(flags);
        }
    }
}
