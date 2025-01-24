package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.Hand;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.InteractAction;

@Data
@With
@AllArgsConstructor
public class ServerboundInteractPacket implements MinecraftPacket {
    private final int entityId;
    private final @NonNull InteractAction action;

    private final float targetX;
    private final float targetY;
    private final float targetZ;
    private final Hand hand;
    private final boolean isSneaking;

    public ServerboundInteractPacket(int entityId, InteractAction action, boolean isSneaking) {
        this(entityId, action, Hand.MAIN_HAND, isSneaking);
    }

    public ServerboundInteractPacket(int entityId, InteractAction action, @NonNull Hand hand, boolean isSneaking) {
        this(entityId, action, 0, 0, 0, hand, isSneaking);
    }

    public ServerboundInteractPacket(ByteBuf in) {
        this.entityId = MinecraftTypes.readVarInt(in);
        this.action = InteractAction.from(MinecraftTypes.readVarInt(in));
        if (this.action == InteractAction.INTERACT_AT) {
            this.targetX = in.readFloat();
            this.targetY = in.readFloat();
            this.targetZ = in.readFloat();
        } else {
            this.targetX = 0;
            this.targetY = 0;
            this.targetZ = 0;
        }

        if (this.action == InteractAction.INTERACT || this.action == InteractAction.INTERACT_AT) {
            this.hand = Hand.from(MinecraftTypes.readVarInt(in));
        } else {
            this.hand = null;
        }
        this.isSneaking = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeVarInt(out, this.entityId);
        MinecraftTypes.writeVarInt(out, this.action.ordinal());
        if (this.action == InteractAction.INTERACT_AT) {
            out.writeFloat(this.targetX);
            out.writeFloat(this.targetY);
            out.writeFloat(this.targetZ);
        }

        if (this.action == InteractAction.INTERACT || this.action == InteractAction.INTERACT_AT) {
            MinecraftTypes.writeVarInt(out, this.hand.ordinal());
        }
        out.writeBoolean(this.isSneaking);
    }

    @Override
    public boolean shouldRunOnGameThread() {
        return true;
    }
}
