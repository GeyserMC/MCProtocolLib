package com.github.steveice10.mc.protocol.packet.ingame.clientbound.level;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.MetadataType;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.MetadataTypes;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.type.*;
import com.github.steveice10.mc.protocol.data.game.entity.object.Direction;
import com.github.steveice10.mc.protocol.packet.PacketTest;
import com.github.steveice10.mc.protocol.packet.ingame.clientbound.entity.ClientboundSetEntityDataPacket;
import org.cloudburstmc.math.vector.Vector3i;
import org.junit.jupiter.api.BeforeEach;

import java.util.OptionalInt;

public class ClientboundSetEntityDataPacketTest extends PacketTest {

    @BeforeEach
    public void setup() {
        this.setPackets(
                new ClientboundSetEntityDataPacket(0, new EntityMetadata<?, ?>[0]),
                new ClientboundSetEntityDataPacket(20, new EntityMetadata<?, ?>[]{
                        new ObjectEntityMetadata<>(1, MetadataTypes.STRING, "Hello!")
                }),
                new ClientboundSetEntityDataPacket(2, new EntityMetadata<?, ?>[]{
                        new BooleanEntityMetadata(0, MetadataTypes.BOOLEAN, true),
                        new ByteEntityMetadata(4, MetadataTypes.BYTE, (byte) 45),
                        new IntEntityMetadata(2, MetadataTypes.INT, 555),
                        new FloatEntityMetadata(3, MetadataTypes.FLOAT, 3.0f),
                        new LongEntityMetadata(8, MetadataTypes.LONG, 123456789L),
                        new ObjectEntityMetadata<>(5, MetadataTypes.POSITION, Vector3i.from(0, 1, 0)),
                        new ObjectEntityMetadata<>(2, MetadataTypes.BLOCK_STATE, 60),
                        new ObjectEntityMetadata<>(6, MetadataTypes.DIRECTION, Direction.EAST),
                        new ObjectEntityMetadata<>(7, MetadataTypes.OPTIONAL_VARINT, OptionalInt.of(1038))
                }),
                new ClientboundSetEntityDataPacket(700, new EntityMetadata<?, ?>[]{
                        // Boxed variation test
                        new ObjectEntityMetadata<>(0, MetadataTypes.INT, 0),
                        new ObjectEntityMetadata<>(1, MetadataTypes.FLOAT, 1.0f)
                })
        );
    }
}
