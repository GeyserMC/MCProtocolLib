package com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity;

import com.github.steveice10.mc.protocol.data.game.Identifier;
import com.github.steveice10.mc.protocol.data.game.entity.attribute.Attribute;
import com.github.steveice10.mc.protocol.data.game.entity.attribute.AttributeModifier;
import com.github.steveice10.mc.protocol.data.game.entity.attribute.AttributeType;
import com.github.steveice10.mc.protocol.data.game.entity.attribute.ModifierOperation;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
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
public class ClientboundUpdateAttributesPacket implements Packet {
    private final int entityId;
    private final @NonNull List<Attribute> attributes;

    public ClientboundUpdateAttributesPacket(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.attributes = new ArrayList<>();
        int length = in.readVarInt();
        for (int index = 0; index < length; index++) {
            String key = in.readString();
            double value = in.readDouble();
            List<AttributeModifier> modifiers = new ArrayList<>();
            int len = in.readVarInt();
            for (int ind = 0; ind < len; ind++) {
                modifiers.add(new AttributeModifier(in.readUUID(), in.readDouble(), ModifierOperation.read(in)));
            }

            AttributeType type = AttributeType.Builtin.BUILTIN.computeIfAbsent(Identifier.formalize(key), AttributeType.Custom::new);
            this.attributes.add(new Attribute(type, value, modifiers));
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeVarInt(this.attributes.size());
        for (Attribute attribute : this.attributes) {
            out.writeString(attribute.getType().getIdentifier());
            out.writeDouble(attribute.getValue());
            out.writeVarInt(attribute.getModifiers().size());
            for (AttributeModifier modifier : attribute.getModifiers()) {
                out.writeUUID(modifier.getUuid());
                out.writeDouble(modifier.getAmount());
                out.writeByte(modifier.getOperation().ordinal());
            }
        }
    }
}
