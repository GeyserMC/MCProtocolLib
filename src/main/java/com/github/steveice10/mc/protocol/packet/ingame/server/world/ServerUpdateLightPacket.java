package com.github.steveice10.mc.protocol.packet.ingame.server.world;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.steveice10.mc.protocol.data.game.chunk.NibbleArray3d;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

public class ServerUpdateLightPacket extends MinecraftPacket {
    private static final NibbleArray3d EMPTY = new NibbleArray3d(4096);
    private int x;
    private int z;
    private List<NibbleArray3d> skyLight;
    private List<NibbleArray3d> blockLight;

    @SuppressWarnings("unused")
    private ServerUpdateLightPacket() {
    }

    public ServerUpdateLightPacket(int x, int z, List<NibbleArray3d> skyLight, List<NibbleArray3d> blockLight) {
        if (skyLight.size() != 18) {
            throw new IllegalArgumentException("skyLight must have exactly 18 entries (null entries are permitted)");
        }
        if (blockLight.size() != 18) {
            throw new IllegalArgumentException("blockLight must have exactly 18 entries (null entries are permitted)");
        }
        this.x = x;
        this.z = z;
        this.skyLight = skyLight;
        this.blockLight = blockLight;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public List<NibbleArray3d> getSkyLight() {
        return this.skyLight;
    }

    public List<NibbleArray3d> getBlockLight() {
        return this.blockLight;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.x = in.readVarInt();
        this.z = in.readVarInt();

        int skyLightMask = in.readVarInt();
        int blockLightMask = in.readVarInt();
        int emptySkyLightMask = in.readVarInt();
        int emptyBlockLightMask = in.readVarInt();

        this.skyLight = new ArrayList<>(18);
        for (int i = 0; i < 18; i++) {
            if ((skyLightMask & 1 << i) != 0) {
                if (in.readVarInt() != 2048) {
                    throw new IOException("Expected sky light byte array to be of length 2048");
                }
                this.skyLight.add(new NibbleArray3d(in, 2048)); // 2048 bytes read = 4096 entries
            } else if ((emptySkyLightMask & 1 << i) != 0) {
                this.skyLight.add(new NibbleArray3d(4096));
            } else {
                this.skyLight.add(null);
            }
        }

        this.blockLight = new ArrayList<>(18);
        for (int i = 0; i < 18; i++) {
            if ((blockLightMask & 1 << i) != 0) {
                if (in.readVarInt() != 2048) {
                    throw new IOException("Expected block light byte array to be of length 2048");
                }
                this.blockLight.add(new NibbleArray3d(in, 2048)); // 2048 bytes read = 4096 entries
            } else if ((emptyBlockLightMask & 1 << i) != 0) {
                this.blockLight.add(new NibbleArray3d(4096));
            } else {
                this.blockLight.add(null);
            }
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.x);
        out.writeVarInt(this.z);

        int skyLightMask = 0;
        int blockLightMask = 0;
        int emptySkyLightMask = 0;
        int emptyBlockLightMask = 0;

        for (int i = 0; i < 18; i++) {
            NibbleArray3d skyLight = this.skyLight.get(i);
            if (skyLight != null) {
                if (EMPTY.equals(skyLight)) {
                    emptySkyLightMask |= 1 << i;
                } else {
                    skyLightMask |= 1 << i;
                }
            }
            NibbleArray3d blockLight = this.blockLight.get(i);
            if (blockLight != null) {
                if (EMPTY.equals(blockLight)) {
                    emptyBlockLightMask |= 1 << i;
                } else {
                    blockLightMask |= 1 << i;
                }
            }
        }

        out.writeVarInt(skyLightMask);
        out.writeVarInt(blockLightMask);
        out.writeVarInt(emptySkyLightMask);
        out.writeVarInt(emptyBlockLightMask);

        for (int i = 0; i < 18; i++) {
            if ((skyLightMask & 1 << i) != 0) {
                out.writeVarInt(2048); // dunno why Minecraft feels the need to send these
                this.skyLight.get(i).write(out);
            }
        }

        for (int i = 0; i < 18; i++) {
            if ((blockLightMask & 1 << i) != 0) {
                out.writeVarInt(2048); // dunno why Minecraft feels the need to send these
                this.blockLight.get(i).write(out);
            }
        }
    }
}
