package ch.spacebase.mcprotocol.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Random;
import java.util.logging.Logger;

import javax.crypto.SecretKey;

public class Util {

	private static final Logger logger = Logger.getLogger("mc-protocol-lib");
	private static final Random rand = new Random();
	
	public static Logger logger() {
		return logger;
	}
	
	public static Random random() {
		return rand;
	}
	
	public static String stripColor(String str) {
		StringBuilder build = new StringBuilder();
		for(int index = 0; index < str.length(); index++) {
			if(str.charAt(index) == '\247') {
				index++;
				continue;
			}
			
			build.append(str.charAt(index));
		}
		
		return build.toString();
	}
	
	public static byte[] encrypt(String serverId, PublicKey key, SecretKey secret) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(serverId.getBytes("ISO_8859_1"));
			digest.update(secret.getEncoded());
			digest.update(key.getEncoded());
			return digest.digest();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
