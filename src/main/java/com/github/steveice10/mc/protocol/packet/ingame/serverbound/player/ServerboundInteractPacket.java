package com.github.steveice10.mc.protocol.packet.ingame.serverbound.player;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.data.game.entity.player.InteractAction;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;

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

    public ServerboundInteractPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.entityId = helper.readVarInt(in);
        this.action = MagicValues.key(InteractAction.class, helper.readVarInt(in));
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
            this.hand = MagicValues.key(Hand.class, helper.readVarInt(in));
        } else {
            this.hand = null;
        }
        this.isSneaking = in.readBoolean();
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.entityId);
        helper.writeVarInt(out, MagicValues.value(Integer.class, this.action));
        if (this.action == InteractAction.INTERACT_AT) {
            out.writeFloat(this.targetX);
            out.writeFloat(this.targetY);
            out.writeFloat(this.targetZ);
        }

        if (this.action == InteractAction.INTERACT || this.action == InteractAction.INTERACT_AT) {
            helper.writeVarInt(out, MagicValues.value(Integer.class, this.hand));
        }
        out.writeBoolean(this.isSneaking);
    }
}
