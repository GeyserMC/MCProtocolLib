package com.github.steveice10.mc.protocol.packet.ingame.server.entity;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.Identifier;
import com.github.steveice10.mc.protocol.data.game.entity.attribute.Attribute;
import com.github.steveice10.mc.protocol.data.game.entity.attribute.AttributeModifier;
import com.github.steveice10.mc.protocol.data.game.entity.attribute.AttributeType;
import com.github.steveice10.mc.protocol.data.game.entity.attribute.ModifierOperation;
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
import java.util.ArrayList;
import java.util.List;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerEntityPropertiesPacket implements Packet {
    private int entityId;
    private @NonNull List<Attribute> attributes;

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.attributes = new ArrayList<>();
        int length = in.readVarInt();
        for(int index = 0; index < length; index++) {
            String key = in.readString();
            double value = in.readDouble();
            List<AttributeModifier> modifiers = new ArrayList<AttributeModifier>();
            int len = in.readVarInt();
            for(int ind = 0; ind < len; ind++) {
                modifiers.add(new AttributeModifier(in.readUUID(), in.readDouble(), MagicValues.key(ModifierOperation.class, in.readByte())));
            }

            this.attributes.add(new Attribute(MagicValues.key(AttributeType.class, Identifier.formalize(key)), value, modifiers));
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeVarInt(this.attributes.size());
        for(Attribute attribute : this.attributes) {
            out.writeString(MagicValues.value(String.class, attribute.getType()));
            out.writeDouble(attribute.getValue());
            out.writeVarInt(attribute.getModifiers().size());
            for(AttributeModifier modifier : attribute.getModifiers()) {
                out.writeUUID(modifier.getUuid());
                out.writeDouble(modifier.getAmount());
                out.writeByte(MagicValues.value(Integer.class, modifier.getOperation()));
            }
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
