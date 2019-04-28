package com.github.steveice10.mc.protocol.packet.ingame.server.world;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

public class ServerUpdateLightPacket extends MinecraftPacket {

    private int chunkX;
    private int chunkZ;
    private int skyLightMask;
    private int blockLightMask;
    private int emptySkyLightMask;
    private int emptyBlockLightMask;
    private List<byte[]> skyLightArrays;
    private List<byte[]> blockLightArrays;

    @Override
    public void read(NetInput in) throws IOException {
        this.chunkX = in.readVarInt();
        this.chunkZ = in.readVarInt();
        this.skyLightMask = in.readVarInt();
        this.blockLightMask = in.readVarInt();
        this.emptySkyLightMask = in.readVarInt();
        this.emptyBlockLightMask = in.readVarInt();
        this.skyLightArrays = new ArrayList<>();
        for (int i = 0; i < 18; i++) {
            if ((this.skyLightMask & 1 << i) != 0) {
                this.skyLightArrays.add(in.readBytes(2048));
            }
        }
        this.blockLightArrays = new ArrayList<>();
        for (int i = 0; i < 18; i++) {
            if ((this.blockLightMask & 1 << i) != 0) {
                this.blockLightArrays.add(in.readBytes(2048));
            }
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.chunkX);
        out.writeVarInt(this.chunkZ);
        out.writeVarInt(this.skyLightMask);
        out.writeVarInt(this.blockLightMask);
        out.writeVarInt(this.emptySkyLightMask);
        out.writeVarInt(this.emptyBlockLightMask);
        for (byte[] bytes : skyLightArrays) {
            out.writeBytes(bytes);
        }
        for (byte[] bytes : blockLightArrays) {
            out.writeBytes(bytes);
        }
    }
}
