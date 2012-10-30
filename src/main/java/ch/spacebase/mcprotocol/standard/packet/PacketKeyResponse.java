package ch.spacebase.mcprotocol.standard.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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
import ch.spacebase.mcprotocol.standard.StandardProtocol;

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
	public void read(DataInputStream in) throws IOException {
		byte sharedKey[] = new byte[in.readShort()];
		in.readFully(sharedKey);
		this.sharedKey = sharedKey;
		
		byte verifyToken[] = new byte[in.readShort()];
		in.readFully(verifyToken);
		this.verifyToken = verifyToken;
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeShort(this.sharedKey.length);
		out.write(this.sharedKey);
		out.writeShort(this.verifyToken.length);
		out.write(this.verifyToken);
	}

	@Override
	public void handleClient(Client conn) {
		((StandardProtocol) conn.getProtocol()).setAES(conn);
		conn.send(new PacketClientStatus((byte) 0)); 
	}
	
	@Override
	public void handleServer(ServerConnection conn) {
        PrivateKey priv = conn.getServer().getKeys().getPrivate();

        ((StandardProtocol) conn.getProtocol()).setSecretKey(new SecretKeySpec(encryptBytes(priv, this.sharedKey), "AES"));
        if (!Arrays.equals(((StandardProtocol) conn.getProtocol()).getToken(), encryptBytes(priv, this.verifyToken))) {
            conn.disconnect("Invalid client reply");
            return;
        }
        
        conn.send(new PacketKeyResponse(new byte[0], new byte[0]));
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
	
}
