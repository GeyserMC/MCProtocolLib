package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.Identifier;
import org.geysermc.mcprotocollib.protocol.data.game.entity.attribute.Attribute;
import org.geysermc.mcprotocollib.protocol.data.game.entity.attribute.AttributeModifier;
import org.geysermc.mcprotocollib.protocol.data.game.entity.attribute.AttributeType;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

import java.util.ArrayList;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ClientboundUpdateAttributesPacket implements MinecraftPacket {
    private final int entityId;
    private final @NonNull List<Attribute> attributes;

    public ClientboundUpdateAttributesPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.entityId = helper.readVarInt(in);
        this.attributes = new ArrayList<>();
        int length = helper.readVarInt(in);
        for (int index = 0; index < length; index++) {
            int attributeId = helper.readVarInt(in);
            double value = in.readDouble();
            List<AttributeModifier> modifiers = new ArrayList<>();
            int len = helper.readVarInt(in);
            for (int ind = 0; ind < len; ind++) {
                modifiers.add(new AttributeModifier(helper.readUUID(in), in.readDouble(), helper.readModifierOperation(in)));
            }

            AttributeType type = AttributeType.Builtin.BUILTIN.get(attributeId); //.computeIfAbsent(attributeId, AttributeType.Custom::new); TODO
            this.attributes.add(new Attribute(type, value, modifiers));
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.entityId);
        helper.writeVarInt(out, this.attributes.size());
        for (Attribute attribute : this.attributes) {
            helper.writeVarInt(out, attribute.getType().getId());
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
