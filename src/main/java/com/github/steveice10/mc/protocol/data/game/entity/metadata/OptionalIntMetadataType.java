package com.github.steveice10.mc.protocol.data.game.entity.metadata;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;
import java.util.OptionalInt;

public class OptionalIntMetadataType extends MetadataType<OptionalInt> {
    protected OptionalIntMetadataType(EntityMetadataFactory<OptionalInt> metadataFactory) {
        super(OptionalIntReader.INSTANCE, OptionalIntWriter.INSTANCE, metadataFactory);
    }

    public static class OptionalIntReader implements Reader<OptionalInt> {
        protected static final OptionalIntReader INSTANCE = new OptionalIntReader();

        @Override
        public OptionalInt read(NetInput input) throws IOException {
            if (!input.readBoolean()) {
                return OptionalInt.empty();
            }

            return OptionalInt.of(input.readVarInt());
        }
    }

    public static class OptionalIntWriter implements Writer<OptionalInt> {
        protected static final OptionalIntWriter INSTANCE = new OptionalIntWriter();

        @Override
        public void write(NetOutput output, OptionalInt value) throws IOException {
            output.writeBoolean(value.isPresent());
            if (value.isPresent()) {
                output.writeVarInt(value.getAsInt());
            }
        }
    }
}
