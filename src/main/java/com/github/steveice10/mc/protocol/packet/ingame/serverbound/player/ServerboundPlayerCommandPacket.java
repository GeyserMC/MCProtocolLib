package com.github.steveice10.mc.protocol.packet.ingame.serverbound.player;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerState;
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
public class ServerboundPlayerCommandPacket implements Packet {
    private final int entityId;
    private final @NonNull PlayerState state;
    private final int jumpBoost;

    public ServerboundPlayerCommandPacket(int entityId, PlayerState state) {
        this(entityId, state, 0);
    }

    public ServerboundPlayerCommandPacket(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.state = MagicValues.key(PlayerState.class, in.readVarInt());
        this.jumpBoost = in.readVarInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeVarInt(MagicValues.value(Integer.class, this.state));
        out.writeVarInt(this.jumpBoost);
    }
}
