package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata;

import io.netty.buffer.ByteBuf;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;

import java.util.OptionalInt;

public class OptionalIntMetadataType extends MetadataType<OptionalInt> {
    protected OptionalIntMetadataType(int id, EntityMetadataFactory<OptionalInt> metadataFactory) {
        super(id, OptionalIntReader.INSTANCE, OptionalIntWriter.INSTANCE, metadataFactory);
    }

    public static class OptionalIntReader implements Reader<OptionalInt> {
        protected static final OptionalIntReader INSTANCE = new OptionalIntReader();

        @Override
        public OptionalInt read(ByteBuf input) {
            int value = MinecraftTypes.readVarInt(input);
            if (value == 0) {
                return OptionalInt.empty();
            }

            return OptionalInt.of(value - 1);
        }
    }

    public static class OptionalIntWriter implements Writer<OptionalInt> {
        protected static final OptionalIntWriter INSTANCE = new OptionalIntWriter();

        @Override
        public void write(ByteBuf output, OptionalInt value) {
            if (value.isPresent()) {
                MinecraftTypes.writeVarInt(output, value.getAsInt() + 1);
            } else {
                MinecraftTypes.writeVarInt(output, 0);
            }
        }
    }
}
