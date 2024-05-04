package org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
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

    public ServerboundInteractPacket(MinecraftByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.action = InteractAction.from(buf.readVarInt());
        if (this.action == InteractAction.INTERACT_AT) {
            this.targetX = buf.readFloat();
            this.targetY = buf.readFloat();
            this.targetZ = buf.readFloat();
        } else {
            this.targetX = 0;
            this.targetY = 0;
            this.targetZ = 0;
        }

        if (this.action == InteractAction.INTERACT || this.action == InteractAction.INTERACT_AT) {
            this.hand = Hand.from(buf.readVarInt());
        } else {
            this.hand = null;
        }
        this.isSneaking = buf.readBoolean();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.entityId);
        buf.writeVarInt(this.action.ordinal());
        if (this.action == InteractAction.INTERACT_AT) {
            buf.writeFloat(this.targetX);
            buf.writeFloat(this.targetY);
            buf.writeFloat(this.targetZ);
        }

        if (this.action == InteractAction.INTERACT || this.action == InteractAction.INTERACT_AT) {
            buf.writeVarInt(this.hand.ordinal());
        }
        buf.writeBoolean(this.isSneaking);
    }
}
