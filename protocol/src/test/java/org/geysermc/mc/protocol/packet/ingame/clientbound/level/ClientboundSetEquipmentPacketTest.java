package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level;

import org.geysermc.mcprotocollib.protocol.data.game.entity.EquipmentSlot;
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.Equipment;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;
import org.geysermc.mcprotocollib.protocol.packet.PacketTest;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.ClientboundSetEquipmentPacket;
import org.junit.jupiter.api.BeforeEach;

public class ClientboundSetEquipmentPacketTest extends PacketTest {

    @BeforeEach
    public void setup() {
        this.setPackets(
                new ClientboundSetEquipmentPacket(1, new Equipment[]{
                        new Equipment(EquipmentSlot.BOOTS, new ItemStack(1))}),
                new ClientboundSetEquipmentPacket(2, new Equipment[]{
                        new Equipment(EquipmentSlot.CHESTPLATE, new ItemStack(2)),
                        new Equipment(EquipmentSlot.HELMET, new ItemStack(3))
                })
        );
    }
}
