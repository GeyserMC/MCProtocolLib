package org.geysermc.mcprotocollib.network.crypt;

/**
 * An interface for encrypting packets.
 * The outputLength should always be the same as the inputLength.
 */
public interface PacketEncryption {
    /**
     * Decrypts the given data.
     *
     * @param input Input data to decrypt.
     * @param inputOffset Offset of the data to start decrypting at.
     * @param inputLength Length of the data to be decrypted.
     * @param output Array to output decrypted data to.
     * @param outputOffset Offset of the output array to start at.
     * @throws Exception If an error occurs.
     */
    void decrypt(byte[] input, int inputOffset, int inputLength, byte[] output, int outputOffset) throws Exception;

    /**
     * Encrypts the given data.
     *
     * @param input Input data to encrypt.
     * @param inputOffset Offset of the data to start encrypting at.
     * @param inputLength Length of the data to be encrypted.
     * @param output Array to output encrypted data to.
     * @param outputOffset Offset of the output array to start at.
     * @throws Exception If an error occurs.
     */
    void encrypt(byte[] input, int inputOffset, int inputLength, byte[] output, int outputOffset) throws Exception;
}
