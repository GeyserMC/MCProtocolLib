package org.spacehq.mc.protocol.packet.login.server;

import org.spacehq.mc.protocol.util.CryptUtil;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;
import java.security.PublicKey;

public class EncryptionRequestPacket implements Packet {

    private String serverId;
    private PublicKey publicKey;
    private byte verifyToken[];

    @SuppressWarnings("unused")
    private EncryptionRequestPacket() {
    }

    public EncryptionRequestPacket(String serverId, PublicKey publicKey, byte verifyToken[]) {
        this.serverId = serverId;
        this.publicKey = publicKey;
        this.verifyToken = verifyToken;
    }

    public String getServerId() {
        return this.serverId;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public byte[] getVerifyToken() {
        return this.verifyToken;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.serverId = in.readString();
        this.publicKey = CryptUtil.decodePublicKey(in.readBytes(in.readVarInt()));
        this.verifyToken = in.readBytes(in.readVarInt());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(this.serverId);
        byte encoded[] = this.publicKey.getEncoded();
        out.writeVarInt(encoded.length);
        out.writeBytes(encoded);
        out.writeVarInt(this.verifyToken.length);
        out.writeBytes(this.verifyToken);
    }

    @Override
    public boolean isPriority() {
        return true;
    }

}
