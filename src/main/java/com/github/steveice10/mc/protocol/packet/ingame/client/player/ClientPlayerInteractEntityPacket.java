package com.github.steveice10.mc.protocol.packet.ingame.client.player;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.data.game.entity.player.InteractAction;
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

import java.io.IOException;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ClientPlayerInteractEntityPacket implements Packet {
    private int entityId;
    private @NonNull InteractAction action;

    private float targetX;
    private float targetY;
    private float targetZ;
    private @NonNull Hand hand;
    private boolean isSneaking;

    public ClientPlayerInteractEntityPacket(int entityId, InteractAction action, boolean isSneaking) {
        this(entityId, action, Hand.MAIN_HAND, isSneaking);
    }

    public ClientPlayerInteractEntityPacket(int entityId, InteractAction action, Hand hand, boolean isSneaking) {
        this(entityId, action, 0, 0, 0, hand, isSneaking);
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.action = MagicValues.key(InteractAction.class, in.readVarInt());
        if (this.action == InteractAction.INTERACT_AT) {
            this.targetX = in.readFloat();
            this.targetY = in.readFloat();
            this.targetZ = in.readFloat();
        }

        if (this.action == InteractAction.INTERACT || this.action == InteractAction.INTERACT_AT) {
            this.hand = MagicValues.key(Hand.class, in.readVarInt());
        }
        this.isSneaking = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeVarInt(MagicValues.value(Integer.class, this.action));
        if (this.action == InteractAction.INTERACT_AT) {
            out.writeFloat(this.targetX);
            out.writeFloat(this.targetY);
            out.writeFloat(this.targetZ);
        }

        if (this.action == InteractAction.INTERACT || this.action == InteractAction.INTERACT_AT) {
            out.writeVarInt(MagicValues.value(Integer.class, this.hand));
        }
        out.writeBoolean(this.isSneaking);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
