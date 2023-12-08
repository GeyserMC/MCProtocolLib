package com.github.steveice10.mc.protocol.data.game.entity.metadata;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import io.netty.buffer.ByteBuf;

import java.util.OptionalInt;

public class OptionalIntMetadataType extends MetadataType<OptionalInt> {
    protected OptionalIntMetadataType(int id, EntityMetadataFactory<OptionalInt> metadataFactory) {
        super(id, OptionalIntReader.INSTANCE, OptionalIntWriter.INSTANCE, metadataFactory);
    }

    public static class OptionalIntReader implements Reader<OptionalInt> {
        protected static final OptionalIntReader INSTANCE = new OptionalIntReader();

        @Override
        public OptionalInt read(MinecraftCodecHelper helper, ByteBuf input) {
            int value = helper.readVarInt(input);
            if (value == 0) {
                return OptionalInt.empty();
            }

            return OptionalInt.of(value - 1);
        }
    }

    public static class OptionalIntWriter implements Writer<OptionalInt> {
        protected static final OptionalIntWriter INSTANCE = new OptionalIntWriter();

        @Override
        public void write(MinecraftCodecHelper helper, ByteBuf output, OptionalInt value) {
            if (value.isPresent()) {
                helper.writeVarInt(output, value.getAsInt() + 1);
            } else {
                helper.writeVarInt(output, 0);
            }
        }
    }
}
