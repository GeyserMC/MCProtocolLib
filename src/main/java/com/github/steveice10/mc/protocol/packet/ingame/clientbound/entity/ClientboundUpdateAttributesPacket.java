package com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.Identifier;
import com.github.steveice10.mc.protocol.data.game.entity.attribute.Attribute;
import com.github.steveice10.mc.protocol.data.game.entity.attribute.AttributeModifier;
import com.github.steveice10.mc.protocol.data.game.entity.attribute.AttributeType;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ClientboundUpdateAttributesPacket implements MinecraftPacket {
    private final int entityId;
    private final @NonNull List<Attribute> attributes;

    public ClientboundUpdateAttributesPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.entityId = helper.readVarInt(in);
        this.attributes = new ArrayList<>();
        int length = helper.readVarInt(in);
        for (int index = 0; index < length; index++) {
            String key = helper.readString(in);
            double value = in.readDouble();
            List<AttributeModifier> modifiers = new ArrayList<>();
            int len = helper.readVarInt(in);
            for (int ind = 0; ind < len; ind++) {
                modifiers.add(new AttributeModifier(helper.readUUID(in), in.readDouble(), helper.readModifierOperation(in)));
            }

            AttributeType type = AttributeType.Builtin.BUILTIN.computeIfAbsent(Identifier.formalize(key), AttributeType.Custom::new);
            this.attributes.add(new Attribute(type, value, modifiers));
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, this.entityId);
        helper.writeVarInt(out, this.attributes.size());
        for (Attribute attribute : this.attributes) {
            helper.writeString(out, attribute.getType().getIdentifier());
            out.writeDouble(attribute.getValue());
            helper.writeVarInt(out, attribute.getModifiers().size());
            for (AttributeModifier modifier : attribute.getModifiers()) {
                helper.writeUUID(out, modifier.getUuid());
                out.writeDouble(modifier.getAmount());
                helper.writeModifierOperation(out, modifier.getOperation());
            }
        }
    }
}
