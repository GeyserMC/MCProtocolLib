package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.entity.attribute.Attribute;
import org.geysermc.mcprotocollib.protocol.data.game.entity.attribute.AttributeModifier;
import org.geysermc.mcprotocollib.protocol.data.game.entity.attribute.AttributeType;

import java.util.ArrayList;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ClientboundUpdateAttributesPacket implements MinecraftPacket {
    private final int entityId;
    private final @NonNull List<Attribute> attributes;

    public ClientboundUpdateAttributesPacket(MinecraftByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.attributes = new ArrayList<>();
        int length = buf.readVarInt();
        for (int index = 0; index < length; index++) {
            int attributeId = buf.readVarInt();
            double value = buf.readDouble();
            List<AttributeModifier> modifiers = new ArrayList<>();
            int len = buf.readVarInt();
            for (int ind = 0; ind < len; ind++) {
                modifiers.add(new AttributeModifier(buf.readUUID(), buf.readDouble(), buf.readModifierOperation()));
            }

            AttributeType type = AttributeType.Builtin.BUILTIN.get(attributeId); //.computeIfAbsent(attributeId, AttributeType.Custom::new); TODO
            this.attributes.add(new Attribute(type, value, modifiers));
        }
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.entityId);
        buf.writeVarInt(this.attributes.size());
        for (Attribute attribute : this.attributes) {
            buf.writeVarInt(attribute.getType().getId());
            buf.writeDouble(attribute.getValue());
            buf.writeVarInt(attribute.getModifiers().size());
            for (AttributeModifier modifier : attribute.getModifiers()) {
                buf.writeUUID(modifier.getUuid());
                buf.writeDouble(modifier.getAmount());
                buf.writeModifierOperation(modifier.getOperation());
            }
        }
    }
}
