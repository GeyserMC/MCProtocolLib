package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.entity.EquipmentSlot;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.Equipment;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ClientboundSetEquipmentPacket implements MinecraftPacket {
    private final int entityId;
    private final @NonNull Equipment[] equipment;

    public ClientboundSetEquipmentPacket(MinecraftByteBuf buf) {
        this.entityId = buf.readVarInt();
        boolean hasNextEntry = true;
        List<Equipment> list = new ArrayList<>();
        while (hasNextEntry) {
            int rawSlot = buf.readByte();
            EquipmentSlot slot = EquipmentSlot.from(((byte) rawSlot) & 127);
            ItemStack item = buf.readOptionalItemStack();
            list.add(new Equipment(slot, item));
            hasNextEntry = (rawSlot & 128) == 128;
        }
        this.equipment = list.toArray(new Equipment[0]);
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.entityId);
        for (int i = 0; i < this.equipment.length; i++) {
            int rawSlot = this.equipment[i].getSlot().ordinal();
            if (i != equipment.length - 1) {
                rawSlot = rawSlot | 128;
            }
            buf.writeByte(rawSlot);
            buf.writeOptionalItemStack(this.equipment[i].getItem());
        }
    }
}
