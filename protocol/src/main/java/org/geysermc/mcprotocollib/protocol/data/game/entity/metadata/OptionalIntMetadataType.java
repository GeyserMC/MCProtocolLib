package org.geysermc.mcprotocollib.protocol.data.game.entity.metadata;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;

import java.util.OptionalInt;

public class OptionalIntMetadataType extends MetadataType<OptionalInt> {
    protected OptionalIntMetadataType(EntityMetadataFactory<OptionalInt> metadataFactory) {
        super(OptionalIntReader.INSTANCE, OptionalIntWriter.INSTANCE, metadataFactory);
    }

    public static class OptionalIntReader implements Reader<OptionalInt> {
        protected static final OptionalIntReader INSTANCE = new OptionalIntReader();

        @Override
        public OptionalInt read(MinecraftByteBuf input) {
            int value = input.readVarInt();
            if (value == 0) {
                return OptionalInt.empty();
            }

            return OptionalInt.of(value - 1);
        }
    }

    public static class OptionalIntWriter implements Writer<OptionalInt> {
        protected static final OptionalIntWriter INSTANCE = new OptionalIntWriter();

        @Override
        public void write(MinecraftByteBuf output, OptionalInt value) {
            if (value.isPresent()) {
                output.writeVarInt(value.getAsInt() + 1);
            } else {
                output.writeVarInt(0);
            }
        }
    }
}
