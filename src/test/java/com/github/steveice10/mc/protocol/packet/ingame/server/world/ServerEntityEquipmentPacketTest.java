package com.github.steveice10.mc.protocol.packet.ingame.server.world;

import com.github.steveice10.mc.protocol.data.game.entity.EquipmentSlot;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Equipment;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.packet.PacketTest;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityEquipmentPacket;
import org.junit.Before;

public class ServerEntityEquipmentPacketTest extends PacketTest {

    @Before
    public void setup() {
        this.setPackets(
                new ServerEntityEquipmentPacket(1, new Equipment[]{
                        new Equipment(EquipmentSlot.BOOTS, new ItemStack(1))}),
                new ServerEntityEquipmentPacket(2, new Equipment[]{
                        new Equipment(EquipmentSlot.CHESTPLATE, new ItemStack(2)),
                        new Equipment(EquipmentSlot.HELMET, new ItemStack(3))
                })
        );
    }
}
