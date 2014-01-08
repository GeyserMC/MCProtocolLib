package ch.spacebase.mc.protocol.packet.login.server;

import java.io.IOException;
import java.security.PublicKey;

import ch.spacebase.mc.util.CryptUtil;
import ch.spacebase.packetlib.io.NetInput;
import ch.spacebase.packetlib.io.NetOutput;
import ch.spacebase.packetlib.packet.Packet;

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
		this.publicKey = CryptUtil.decodePublicKey(in.readPrefixedBytes());
		this.verifyToken = in.readPrefixedBytes();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeString(this.serverId);
		out.writePrefixedBytes(this.publicKey.getEncoded());
		out.writePrefixedBytes(this.verifyToken);
	}
	
	@Override
	public boolean isPriority() {
		return true;
	}
	
}
