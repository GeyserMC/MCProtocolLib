package com.github.steveice10.mc.protocol.data.game.chunk;

import lombok.Data;
import lombok.NonNull;

import java.util.Arrays;

@Data
public class FlexibleStorage {
    private final @NonNull long[] data;
    private final int bitsPerEntry;
    private final int size;
    private final long maxEntryValue;

    public FlexibleStorage(int bitsPerEntry, int size) {
        this(bitsPerEntry, new long[roundToNearest(size * bitsPerEntry, 64) / 64]);
    }

    public FlexibleStorage(int bitsPerEntry, @NonNull long[] data) {
        this.bitsPerEntry = bitsPerEntry;
        if(bitsPerEntry <= 8) {
            this.data = padArray(bitsPerEntry, Arrays.copyOf(data, data.length));
        } else {
            this.data = padArray(bitsPerEntry, 14, Arrays.copyOf(data, data.length));
        }
        this.size = data.length * 64 / this.bitsPerEntry;
        this.maxEntryValue = (1L << this.bitsPerEntry) - 1;
    }

    private static int roundToNearest(int value, int roundTo) {
        if(roundTo == 0) {
            return 0;
        } else if(value == 0) {
            return roundTo;
        } else {
            if(value < 0) {
                roundTo *= -1;
            }

            int remainder = value % roundTo;
            return remainder != 0 ? value + roundTo - remainder : value;
        }
    }

    public int get(int index) {
        if(index < 0 || index > this.size - 1) {
            throw new IndexOutOfBoundsException();
        }

        int bitIndex = index * this.bitsPerEntry;
        int startIndex = bitIndex / 64;
        int endIndex = ((index + 1) * this.bitsPerEntry - 1) / 64;
        int startBitSubIndex = bitIndex % 64;
        if(startIndex == endIndex) {
            return (int) (this.data[startIndex] >>> startBitSubIndex & this.maxEntryValue);
        } else {
            int endBitSubIndex = 64 - startBitSubIndex;
            return (int) ((this.data[startIndex] >>> startBitSubIndex | this.data[endIndex] << endBitSubIndex) & this.maxEntryValue);
        }
    }

    public void set(int index, int value) {
        if(index < 0 || index > this.size - 1) {
            throw new IndexOutOfBoundsException();
        }

        if(value < 0 || value > this.maxEntryValue) {
            throw new IllegalArgumentException("Value cannot be outside of accepted range.");
        }

        int bitIndex = index * this.bitsPerEntry;
        int startIndex = bitIndex / 64;
        int endIndex = ((index + 1) * this.bitsPerEntry - 1) / 64;
        int startBitSubIndex = bitIndex % 64;
        this.data[startIndex] = this.data[startIndex] & ~(this.maxEntryValue << startBitSubIndex) | ((long) value & this.maxEntryValue) << startBitSubIndex;
        if(startIndex != endIndex) {
            int endBitSubIndex = 64 - startBitSubIndex;
            this.data[endIndex] = this.data[endIndex] >>> endBitSubIndex << endBitSubIndex | ((long) value & this.maxEntryValue) >> endBitSubIndex;
        }
    }

    private static final int[] MAGIC_CHUNK_VALUES = {
            -1, -1, 0, Integer.MIN_VALUE, 0, 0, 1431655765, 1431655765, 0, Integer.MIN_VALUE,
            0, 1, 858993459, 858993459, 0, 715827882, 715827882, 0, 613566756, 613566756,
            0, Integer.MIN_VALUE, 0, 2, 477218588, 477218588, 0, 429496729, 429496729, 0,
            390451572, 390451572, 0, 357913941, 357913941, 0, 330382099, 330382099, 0, 306783378,
            306783378, 0, 286331153, 286331153, 0, Integer.MIN_VALUE, 0, 3, 252645135, 252645135,
            0, 238609294, 238609294, 0, 226050910, 226050910, 0, 214748364, 214748364, 0,
            204522252, 204522252, 0, 195225786, 195225786, 0, 186737708, 186737708, 0, 178956970,
            178956970, 0, 171798691, 171798691, 0, 165191049, 165191049, 0, 159072862, 159072862,
            0, 153391689, 153391689, 0, 148102320, 148102320, 0, 143165576, 143165576, 0,
            138547332, 138547332, 0, Integer.MIN_VALUE, 0, 4, 130150524, 130150524, 0, 126322567,
            126322567, 0, 122713351, 122713351, 0, 119304647, 119304647, 0, 116080197, 116080197,
            0, 113025455, 113025455, 0, 110127366, 110127366, 0, 107374182, 107374182, 0,
            104755299, 104755299, 0, 102261126, 102261126, 0, 99882960, 99882960, 0, 97612893,
            97612893, 0, 95443717, 95443717, 0, 93368854, 93368854, 0, 91382282, 91382282,
            0, 89478485, 89478485, 0, 87652393, 87652393, 0, 85899345, 85899345, 0,
            84215045, 84215045, 0, 82595524, 82595524, 0, 81037118, 81037118, 0, 79536431,
            79536431, 0, 78090314, 78090314, 0, 76695844, 76695844, 0, 75350303, 75350303,
            0, 74051160, 74051160, 0, 72796055, 72796055, 0, 71582788, 71582788, 0,
            70409299, 70409299, 0, 69273666, 69273666, 0, 68174084, 68174084, 0, Integer.MIN_VALUE,
            0, 5 };

    private static long[] padArray(int bitsPerEntry, long[] oldData) {
        return padArray(bitsPerEntry, bitsPerEntry, oldData);
    }

    private static long[] padArray(int bitsPerEntry, int newBitsPerEntry, long[] oldData) {
        long maxEntryValue = (1L << bitsPerEntry) - 1;
        char valuesPerLong = (char) (64 / newBitsPerEntry);
        int magicIndex = (valuesPerLong - 1) * 3;
        long divideMultiply = Integer.toUnsignedLong(MAGIC_CHUNK_VALUES[magicIndex]);
        long divideAdd = Integer.toUnsignedLong(MAGIC_CHUNK_VALUES[magicIndex + 1]);
        int divideShift = MAGIC_CHUNK_VALUES[magicIndex + 2];
        long[] data = new long[(4096 + valuesPerLong - 1) / valuesPerLong];
        for(int index = 0; index < 4096; index++) {
            int bitIndex = index * bitsPerEntry;
            int startIndex = bitIndex / 64;
            int endIndex = ((index + 1) * bitsPerEntry - 1) / 64;
            int startBitSubIndex = bitIndex % 64;
            int value;
            if(startIndex != endIndex) {
                int endBitSubIndex = 64 - startBitSubIndex;
                value = (int) ((oldData[startIndex] >>> startBitSubIndex | oldData[endIndex] << endBitSubIndex) & maxEntryValue);
            } else {
                value = (int) (oldData[startIndex] >>> startBitSubIndex & maxEntryValue);
            }

            int cellIndex = (int) (index * divideMultiply + divideAdd >> 32L >> divideShift);
            int newBitIndex = (index - cellIndex * valuesPerLong) * newBitsPerEntry;
            data[cellIndex] = data[cellIndex] & ~(maxEntryValue << newBitIndex) | (value & maxEntryValue) << newBitIndex;
        }
        return data;
    }
}
