package org.geysermc.mcprotocollib.network.crypt;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import java.security.GeneralSecurityException;
import java.security.Key;

/**
 * An encryption implementation using "AES/CFB8/NoPadding" encryption.
 */
public class AESEncryption implements PacketEncryption {
    private final Cipher inCipher;
    private final Cipher outCipher;

    /**
     * Creates a new AESEncryption instance.
     *
     * @param key Key to use when encrypting/decrypting data.
     * @throws GeneralSecurityException If a security error occurs.
     */
    public AESEncryption(Key key) throws GeneralSecurityException {
        this.inCipher = Cipher.getInstance("AES/CFB8/NoPadding");
        this.inCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(key.getEncoded()));

        this.outCipher = Cipher.getInstance("AES/CFB8/NoPadding");
        this.outCipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(key.getEncoded()));
    }

    @Override
    public void decrypt(byte[] input, int inputOffset, int inputLength, byte[] output, int outputOffset) throws Exception {
        this.inCipher.update(input, inputOffset, inputLength, output, outputOffset);
    }

    @Override
    public void encrypt(byte[] input, int inputOffset, int inputLength, byte[] output, int outputOffset) throws Exception {
        this.outCipher.update(input, inputOffset, inputLength, output, outputOffset);
    }
}
