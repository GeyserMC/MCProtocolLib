package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.MetadataType;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.type.*;
import com.github.steveice10.mc.protocol.data.game.entity.object.Direction;
import com.github.steveice10.mc.protocol.packet.PacketTest;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.ClientboundSetEntityDataPacket;
import org.junit.Before;

import java.util.OptionalInt;

public class ClientboundSetEntityDataPacketTest extends PacketTest {

    @Before
    public void setup() {
        this.setPackets(
                new ClientboundSetEntityDataPacket(0, new EntityMetadata[0]),
                new ClientboundSetEntityDataPacket(20, new EntityMetadata[] {
                        new ObjectEntityMetadata<>(1, MetadataType.STRING, "Hello!")
                }),
                new ClientboundSetEntityDataPacket(2, new EntityMetadata[] {
                        new BooleanEntityMetadata(0, MetadataType.BOOLEAN, true),
                        new ByteEntityMetadata(4, MetadataType.BYTE, (byte) 45),
                        new IntEntityMetadata(2, MetadataType.INT, 555),
                        new FloatEntityMetadata(3, MetadataType.FLOAT, 3.0f),
                        new ObjectEntityMetadata<>(5, MetadataType.POSITION, new Position(0, 1, 0)),
                        new IntEntityMetadata(2, MetadataType.BLOCK_STATE, 60),
                        new ObjectEntityMetadata<>(6, MetadataType.DIRECTION, Direction.EAST),
                        new ObjectEntityMetadata<>(7, MetadataType.OPTIONAL_VARINT, OptionalInt.of(1038))
                }),
                new ClientboundSetEntityDataPacket(700, new EntityMetadata[] {
                        // Boxed variation test
                        new ObjectEntityMetadata<>(0, MetadataType.INT, 0),
                        new ObjectEntityMetadata<>(1, MetadataType.FLOAT, 1.0f)
                })
        );
    }
}
