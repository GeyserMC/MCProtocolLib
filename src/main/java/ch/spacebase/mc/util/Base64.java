package ch.spacebase.mc.util;

public class Base64 {

	private final static byte EQUALS_SIGN = (byte) '=';
	private final static byte WHITE_SPACE_ENC = -5;
	private final static byte EQUALS_SIGN_ENC = -1;
	private final static byte[] _STANDARD_ALPHABET = { (byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F', (byte) 'G', (byte) 'H', (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L', (byte) 'M', (byte) 'N', (byte) 'O', (byte) 'P', (byte) 'Q', (byte) 'R', (byte) 'S', (byte) 'T', (byte) 'U', (byte) 'V', (byte) 'W', (byte) 'X', (byte) 'Y', (byte) 'Z', (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f', (byte) 'g', (byte) 'h', (byte) 'i', (byte) 'j', (byte) 'k', (byte) 'l', (byte) 'm', (byte) 'n', (byte) 'o', (byte) 'p', (byte) 'q', (byte) 'r', (byte) 's', (byte) 't', (byte) 'u', (byte) 'v', (byte) 'w', (byte) 'x', (byte) 'y', (byte) 'z', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) '+', (byte) '/' };
	private final static byte[] _STANDARD_DECODABET = { -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, -9, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, -9, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9 };

	private static byte[] getAlphabet() {
		return _STANDARD_ALPHABET;
	}

	private static byte[] getDecodabet() {
		return _STANDARD_DECODABET;
	}

	private static byte[] encode3to4(byte[] source, int srcOffset, int numSigBytes, byte[] destination, int destOffset) {
		byte[] ALPHABET = getAlphabet();
		int inBuff = (numSigBytes > 0 ? ((source[srcOffset] << 24) >>> 8) : 0) | (numSigBytes > 1 ? ((source[srcOffset + 1] << 24) >>> 16) : 0) | (numSigBytes > 2 ? ((source[srcOffset + 2] << 24) >>> 24) : 0);
		switch(numSigBytes) {
			case 3:
				destination[destOffset] = ALPHABET[(inBuff >>> 18)];
				destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 0x3f];
				destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 0x3f];
				destination[destOffset + 3] = ALPHABET[(inBuff) & 0x3f];
				return destination;
			case 2:
				destination[destOffset] = ALPHABET[(inBuff >>> 18)];
				destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 0x3f];
				destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 0x3f];
				destination[destOffset + 3] = EQUALS_SIGN;
				return destination;
			case 1:
				destination[destOffset] = ALPHABET[(inBuff >>> 18)];
				destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 0x3f];
				destination[destOffset + 2] = EQUALS_SIGN;
				destination[destOffset + 3] = EQUALS_SIGN;
				return destination;
			default:
				return destination;
		}
	}

	public static byte[] encode(byte[] source) {
		return encode(source, 0, source.length);
	}

	public static byte[] encode(byte[] source, int off, int len) {
		if(source == null) {
			throw new NullPointerException("Cannot serialize a null array.");
		}

		if(off < 0) {
			throw new IllegalArgumentException("Cannot have negative offset: " + off);
		}

		if(len < 0) {
			throw new IllegalArgumentException("Cannot have length offset: " + len);
		}

		if(off + len > source.length) {
			throw new IllegalArgumentException(String.format("Cannot have offset of %d and length of %d with array of length %d", off, len, source.length));
		}

		int encLen = (len / 3) * 4 + (len % 3 > 0 ? 4 : 0);
		byte[] outBuff = new byte[encLen];
		int d = 0;
		int e = 0;
		int len2 = len - 2;
		for(; d < len2; d += 3, e += 4) {
			encode3to4(source, d + off, 3, outBuff, e);
		}

		if(d < len) {
			encode3to4(source, d + off, len - d, outBuff, e);
			e += 4;
		}

		if(e <= outBuff.length - 1) {
			byte[] finalOut = new byte[e];
			System.arraycopy(outBuff, 0, finalOut, 0, e);
			return finalOut;
		} else {
			return outBuff;
		}
	}

	private static int decode4to3(byte[] source, int srcOffset, byte[] destination, int destOffset) {
		if(source == null) {
			throw new NullPointerException("Source array was null.");
		}

		if(destination == null) {
			throw new NullPointerException("Destination array was null.");
		}

		if(srcOffset < 0 || srcOffset + 3 >= source.length) {
			throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and still process four bytes.", source.length, srcOffset));
		}

		if(destOffset < 0 || destOffset + 2 >= destination.length) {
			throw new IllegalArgumentException(String.format("Destination array with length %d cannot have offset of %d and still store three bytes.", destination.length, destOffset));
		}

		byte[] DECODABET = getDecodabet();
		if(source[srcOffset + 2] == EQUALS_SIGN) {
			int outBuff = ((DECODABET[source[srcOffset]] & 0xFF) << 18) | ((DECODABET[source[srcOffset + 1]] & 0xFF) << 12);
			destination[destOffset] = (byte) (outBuff >>> 16);
			return 1;
		} else if(source[srcOffset + 3] == EQUALS_SIGN) {
			int outBuff = ((DECODABET[source[srcOffset]] & 0xFF) << 18) | ((DECODABET[source[srcOffset + 1]] & 0xFF) << 12) | ((DECODABET[source[srcOffset + 2]] & 0xFF) << 6);
			destination[destOffset] = (byte) (outBuff >>> 16);
			destination[destOffset + 1] = (byte) (outBuff >>> 8);
			return 2;
		} else {
			int outBuff = ((DECODABET[source[srcOffset]] & 0xFF) << 18) | ((DECODABET[source[srcOffset + 1]] & 0xFF) << 12) | ((DECODABET[source[srcOffset + 2]] & 0xFF) << 6) | ((DECODABET[source[srcOffset + 3]] & 0xFF));
			destination[destOffset] = (byte) (outBuff >> 16);
			destination[destOffset + 1] = (byte) (outBuff >> 8);
			destination[destOffset + 2] = (byte) (outBuff);
			return 3;
		}
	}

	public static byte[] decode(byte[] source) throws java.io.IOException {
		return decode(source, 0, source.length);
	}

	public static byte[] decode(byte[] source, int off, int len) throws java.io.IOException {
		if(source == null) {
			throw new NullPointerException("Cannot decode null source array.");
		}

		if(off < 0 || off + len > source.length) {
			throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and process %d bytes.", source.length, off, len));
		}

		if(len == 0) {
			return new byte[0];
		} else if(len < 4) {
			throw new IllegalArgumentException("Base64-encoded string must have at least four characters, but length specified was " + len);
		}

		byte[] DECODABET = getDecodabet();
		int len34 = len * 3 / 4;
		byte[] outBuff = new byte[len34];
		int outBuffPosn = 0;
		byte[] b4 = new byte[4];
		int b4Posn = 0;
		int i = 0;
		byte sbiDecode = 0;
		for(i = off; i < off + len; i++) {
			sbiDecode = DECODABET[source[i] & 0xFF];
			if(sbiDecode >= WHITE_SPACE_ENC) {
				if(sbiDecode >= EQUALS_SIGN_ENC) {
					b4[b4Posn++] = source[i];
					if(b4Posn > 3) {
						outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn);
						b4Posn = 0;
						if(source[i] == EQUALS_SIGN) {
							break;
						}
					}
				}
			} else {
				throw new java.io.IOException(String.format("Bad Base64 input character decimal %d in array position %d", source[i] & 0xFF, i));
			}
		}

		byte[] out = new byte[outBuffPosn];
		System.arraycopy(outBuff, 0, out, 0, outBuffPosn);
		return out;
	}

}
