package ch.spacebase.mcprotocol.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Random;
import java.util.logging.Logger;

import javax.crypto.SecretKey;

import ch.spacebase.mcprotocol.standard.packet.PacketPluginMessage;
import ch.spacebase.mcprotocol.standard.util.PluginMessageBuilder;

/**
 * A set of generic protocol utilities.
 */
public class Util {

	/**
	 * The library's logger.
	 */
	private static final Logger logger = Logger.getLogger("mc-protocol-lib");
	
	/**
	 * The library's Random.
	 */
	private static final Random rand = new Random();

	/**
	 * Gets the library's logger.
	 * @return The library's logger.
	 */
	public static Logger logger() {
		return logger;
	}

	/**
	 * Gets the library's Random.
	 * @return The library's Random.
	 */
	public static Random random() {
		return rand;
	}

	/**
	 * Strips a string of color codes.
	 * @param str String to strip of color.
	 * @return The resulting stripped string.
	 */
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

	/**
	 * Creates encrypted server data.
	 * @param loginKey The login key to use.
	 * @param key The public key to use.
	 * @param secret The secret key to use.
	 * @return The encrypted server data.
	 */
	public static byte[] encrypt(String loginKey, PublicKey key, SecretKey secret) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(loginKey.getBytes("ISO_8859_1"));
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
	
	/**
	 * Formats parameters into a ping response string.
	 * @param motd MOTD of the server.
	 * @param players Number of players online.
	 * @param maxplayers Maximum number of players allowed online.
	 * @return Formatted string response.
	 */
	public static String formatPingResponse(String motd, int players, int maxplayers) {
		return "§1\0" + Constants.StandardProtocol.PROTOCOL_VERSION + "\0" + Constants.StandardProtocol.MINECRAFT_VERSION + "\0" + motd + "\0" + players + "\0" + maxplayers;
	}
	
	/**
	 * Prepares a plugin message packet with client data to be sent after a ping request packet.
	 * @param serverIp IP of the server.
	 * @param serverPort Port of the server.
	 * @return The prepared packet.
	 */
	public static PacketPluginMessage prepareClientPingData(String serverIp, int serverPort) {
		PluginMessageBuilder builder = new PluginMessageBuilder(Constants.StandardProtocol.PluginChannels.CLIENT_PING_DATA);
		builder.writeByte(Constants.StandardProtocol.PROTOCOL_VERSION);
		builder.writeString(serverIp);
		builder.writeInt(serverPort);
		return builder.build();
	}
	
	/**
	 * Gets the specified bit from the given value.
	 * @param value Value to use.
	 * @param bit Bit to get.
	 * @return The value of the bit.
	 */
	public static boolean getBit(int value, int bit) {
		return (value & bit) == bit;
	}

	/**
	 * Sets the given bit in the given value.
	 * @param value Value to change.
	 * @param bit Bit to change.
	 * @param state New value of the bit.
	 * @return The resulting value.
	 */
	public static byte setBit(byte value, int bit, boolean state) {
		return state ? (byte) (value | bit) : (byte) (value & ~bit);
	}

}
