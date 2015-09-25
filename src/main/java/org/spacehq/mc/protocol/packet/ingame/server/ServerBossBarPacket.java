package org.spacehq.mc.protocol.packet.ingame.server;

import org.spacehq.mc.protocol.data.game.BossBarAction;
import org.spacehq.mc.protocol.data.game.BossBarColor;
import org.spacehq.mc.protocol.data.game.MagicValues;
import org.spacehq.mc.protocol.data.message.Message;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;
import java.util.UUID;

public class ServerBossBarPacket implements Packet {
    private UUID uuid;
    private BossBarAction action;

    private Message title;
    private float health;
    private BossBarColor color;
    private int dividers;
    private int flags;

    @SuppressWarnings("unused")
    private ServerBossBarPacket() {
    }

    public ServerBossBarPacket(UUID uuid, BossBarAction action, Message title, float health, BossBarColor color, int dividers, int flags) {
        this.uuid = uuid;
        this.action = BossBarAction.ADD;

        this.title = title;
        this.health = health;
        this.color = color;
        this.dividers = dividers;
        this.flags = flags;
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

    public ServerBossBarPacket(UUID uuid, BossBarAction action, Message title) {
        this.uuid = uuid;
        this.action = BossBarAction.UPDATE_TITLE;

        this.title = title;
    }

    public ServerBossBarPacket(UUID uuid, BossBarAction action, BossBarColor color, int dividers) {
        this.uuid = uuid;
        this.action = BossBarAction.UPDATE_STYLE;

        this.color = color;
        this.dividers = dividers;
    }

    public ServerBossBarPacket(UUID uuid, BossBarAction action, int flags) {
        this.uuid = uuid;
        this.action = BossBarAction.UPDATE_FLAGS;

        this.flags = flags;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public BossBarAction getAction() {
        return this.action;
    }

    public Message getTitle() {
        return this.title;
    }

    public float getHealth() {
        return this.health;
    }

    public BossBarColor getColor() {
        return this.color;
    }

    public int getDividers() {
        return this.dividers;
    }

    public int getFlags() {
        return this.flags;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.uuid = in.readUUID();
        this.action = MagicValues.key(BossBarAction.class, in.readVarInt());

        if(this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_TITLE) {
            this.title = Message.fromString(in.readString());
        }

        if(this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_HEALTH) {
            this.health = in.readFloat();
        }

        if(this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_STYLE) {
            this.color = MagicValues.key(BossBarColor.class, in.readVarInt());
            this.dividers = in.readVarInt();
        }

        if(this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_FLAGS) {
            this.flags = in.readUnsignedByte();
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeUUID(this.uuid);
        out.writeVarInt(MagicValues.value(Integer.class, this.action));

        if(this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_TITLE) {
            out.writeString(this.title.toJsonString());
        }

        if(this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_HEALTH) {
            out.writeFloat(this.health);
        }

        if(this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_STYLE) {
            out.writeVarInt(MagicValues.value(Integer.class, this.color));
            out.writeVarInt(this.dividers);
        }

        if(this.action == BossBarAction.ADD || this.action == BossBarAction.UPDATE_FLAGS) {
            out.writeByte(this.flags);
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
