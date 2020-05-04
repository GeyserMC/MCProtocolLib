package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.player.CombatState;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public class ServerCombatPacket extends MinecraftPacket {
    private CombatState state;
    private int entityId;
    private int duration;
    private int playerId;
    private String message;

    public ServerCombatPacket() {
        this.state = CombatState.ENTER_COMBAT;
    }

    public ServerCombatPacket(int entityId, int duration) {
        this.state = CombatState.END_COMBAT;
        this.entityId = entityId;
        this.duration = duration;
    }

    public ServerCombatPacket(int entityId, int playerId, String message, boolean escape) {
        this.state = CombatState.ENTITY_DEAD;
        this.entityId = entityId;
        this.playerId = playerId;
        this.message = escape ? ServerChatPacket.escapeText(message) : message;
    }

    public CombatState getCombatState() {
        return this.state;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getPlayerId() {
        return this.playerId;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.state = MagicValues.key(CombatState.class, in.readVarInt());
        if(this.state == CombatState.END_COMBAT) {
            this.duration = in.readVarInt();
            this.entityId = in.readInt();
        } else if(this.state == CombatState.ENTITY_DEAD) {
            this.playerId = in.readVarInt();
            this.entityId = in.readInt();
            this.message = in.readString();
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(MagicValues.value(Integer.class, this.state));
        if(this.state == CombatState.END_COMBAT) {
            out.writeVarInt(this.duration);
            out.writeInt(this.entityId);
        } else if(this.state == CombatState.ENTITY_DEAD) {
            out.writeVarInt(this.playerId);
            out.writeInt(this.entityId);
            out.writeString(this.message);
        }
    }
}
