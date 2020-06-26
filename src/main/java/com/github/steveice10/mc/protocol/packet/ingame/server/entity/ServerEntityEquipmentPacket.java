package com.github.steveice10.mc.protocol.packet.ingame.server.entity;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.EquipmentSlot;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Equipment;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerEntityEquipmentPacket implements Packet {
    private int entityId;
    private @NonNull Equipment[] equipment;

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        boolean hasNextEntry = true;
        List<Equipment> list = new ArrayList<>();
        while (hasNextEntry) {
            int rawSlot = in.readVarInt();
            EquipmentSlot slot = MagicValues.key(EquipmentSlot.class, ((byte) rawSlot) & 127);
            ItemStack item = ItemStack.read(in);
            list.add(new Equipment(slot, item));
            hasNextEntry = (rawSlot & 128) == 128;
        }
        this.equipment = list.toArray(new Equipment[list.size()]);
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        for (int i = 0; i < this.equipment.length; i++) {
            int rawSlot = MagicValues.value(Integer.class, this.equipment);
            if (i != equipment.length - 1) {
                rawSlot = rawSlot | 128;
            }
            out.writeVarInt(rawSlot);
            ItemStack.write(out, this.equipment[i].getItem());
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
