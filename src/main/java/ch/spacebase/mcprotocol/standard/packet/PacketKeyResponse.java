package ch.spacebase.mcprotocol.standard.packet;

import ch.spacebase.mcprotocol.event.PacketVisitor;
import ch.spacebase.mcprotocol.net.io.NetInput;
import ch.spacebase.mcprotocol.net.io.NetOutput;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import ch.spacebase.mcprotocol.net.Client;
import ch.spacebase.mcprotocol.net.ServerConnection;
import ch.spacebase.mcprotocol.packet.Packet;
import ch.spacebase.mcprotocol.standard.StandardClient;
import ch.spacebase.mcprotocol.standard.StandardServer;
import ch.spacebase.mcprotocol.standard.StandardServerConnection;

public class PacketKeyResponse extends Packet {

	public byte[] sharedKey;
	public byte[] verifyToken;

	public PacketKeyResponse() {
	}

	public PacketKeyResponse(byte[] sharedKey, byte[] verifyToken) {
		this.sharedKey = sharedKey;
		this.verifyToken = verifyToken;
	}

	public byte[] getSharedKey() {
		return this.sharedKey;
	}

	public byte[] getVerifyToken() {
		return this.verifyToken;
	}

	@Override
	public void read(NetInput in) throws IOException {
		byte sharedKey[] = in.readBytes(in.readShort());
		this.sharedKey = sharedKey;

		byte verifyToken[] = in.readBytes(in.readShort());
		this.verifyToken = verifyToken;
	}

	@Override
	public void write(NetOutput out) throws IOException {
		out.writeShort(this.sharedKey.length);
		out.writeBytes(this.sharedKey);
		out.writeShort(this.verifyToken.length);
		out.writeBytes(this.verifyToken);
	}

	@Override
	public void handleClient(Client conn) {
		((StandardClient) conn).setAES(conn);
		conn.send(new PacketClientStatus((byte) 0));
	}

	@Override
	public void handleServer(ServerConnection conn) {
		PrivateKey priv = ((StandardServer) conn.getServer()).getKeys().getPrivate();

		((StandardServerConnection) conn).setSecretKey(new SecretKeySpec(encryptBytes(priv, this.sharedKey), "AES"));
		if(!Arrays.equals(((StandardServerConnection) conn).getToken(), encryptBytes(priv, this.verifyToken))) {
			conn.disconnect("Invalid client reply");
			return;
		}

		conn.send(new PacketKeyResponse(new byte[0], new byte[0]));
		((StandardServerConnection) conn).setAES(conn);
	}

	@Override
	public int getId() {
		return 252;
	}

	private static byte[] encryptBytes(PrivateKey key, byte[] bytes) {
		try {
			Cipher cipher = Cipher.getInstance(key.getAlgorithm());
			cipher.init(2, key);
			return cipher.doFinal(bytes);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}

		return null;
	}

        @Override
        public void accept(PacketVisitor visitor) {
                visitor.visit(this);
        }

}
