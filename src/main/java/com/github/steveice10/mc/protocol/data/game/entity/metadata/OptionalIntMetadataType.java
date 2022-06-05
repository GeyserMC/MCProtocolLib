package com.github.steveice10.mc.protocol.data.game.entity.metadata;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.OptionalInt;

public class OptionalIntMetadataType extends MetadataType<OptionalInt> {
    protected OptionalIntMetadataType(EntityMetadataFactory<OptionalInt> metadataFactory) {
        super(OptionalIntReader.INSTANCE, OptionalIntWriter.INSTANCE, metadataFactory);
    }

    public static class OptionalIntReader implements HelperReader<OptionalInt> {
        protected static final OptionalIntReader INSTANCE = new OptionalIntReader();

        @Override
        public OptionalInt read(MinecraftCodecHelper helper, ByteBuf input) throws IOException {
            int value = helper.readVarInt(input);
            if (value == 0) {
                return OptionalInt.empty();
            }

            return OptionalInt.of(value - 1);
        }
    }

    public static class OptionalIntWriter implements HelperWriter<OptionalInt> {
        protected static final OptionalIntWriter INSTANCE = new OptionalIntWriter();

        @Override
        public void write(MinecraftCodecHelper helper, ByteBuf output, OptionalInt value) throws IOException {
            if (value.isPresent()) {
                helper.writeVarInt(output, value.getAsInt() + 1);
            } else {
                helper.writeVarInt(output, 0);
            }
        }
    }
}
