package org.geysermc.mcprotocollib.protocol.data.game.chunk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class NibbleArray3d {
    private final byte @NonNull [] data;

    public NibbleArray3d(int size) {
        this(new byte[size >> 1]);
    }

    public int get(int x, int y, int z) {
        int key = y << 8 | z << 4 | x;
        int index = key >> 1;
        int part = key & 1;
        return part == 0 ? this.data[index] & 15 : this.data[index] >> 4 & 15;
    }

    public void set(int x, int y, int z, int val) {
        int key = y << 8 | z << 4 | x;
        int index = key >> 1;
        int part = key & 1;
        if (part == 0) {
            this.data[index] = (byte) (this.data[index] & 240 | val & 15);
        } else {
            this.data[index] = (byte) (this.data[index] & 15 | (val & 15) << 4);
        }
    }

    public void fill(int val) {
        for (int index = 0; index < this.data.length << 1; index++) {
            int ind = index >> 1;
            int part = index & 1;
            if (part == 0) {
                this.data[ind] = (byte) (this.data[ind] & 240 | val & 15);
            } else {
                this.data[ind] = (byte) (this.data[ind] & 15 | (val & 15) << 4);
            }
        }
    }
}
