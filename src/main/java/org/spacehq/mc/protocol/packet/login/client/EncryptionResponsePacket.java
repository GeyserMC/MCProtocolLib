package org.spacehq.mc.protocol.packet.login.client;

import org.spacehq.mc.protocol.util.CryptUtil;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class EncryptionResponsePacket implements Packet {

	private byte sharedKey[];
	private byte verifyToken[];

	@SuppressWarnings("unused")
	private EncryptionResponsePacket() {
	}

	public EncryptionResponsePacket(SecretKey secretKey, PublicKey publicKey, byte verifyToken[]) {
		this.sharedKey = CryptUtil.encryptData(publicKey, secretKey.getEncoded());
		this.verifyToken = CryptUtil.encryptData(publicKey, verifyToken);
	}

	public SecretKey getSecretKey(PrivateKey privateKey) {
		return CryptUtil.decryptSharedKey(privateKey, this.sharedKey);
	}

	public byte[] getVerifyToken(PrivateKey privateKey) {
		return CryptUtil.decryptData(privateKey, this.verifyToken);
	}

	@Override
	public void read(NetInput in) throws IOException {
		this.sharedKey = in.readPrefixedBytes();
		this.verifyToken = in.readPrefixedBytes();
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writePrefixedBytes(this.sharedKey);
		out.writePrefixedBytes(this.verifyToken);
	}

	@Override
	public boolean isPriority() {
		return true;
	}

}
