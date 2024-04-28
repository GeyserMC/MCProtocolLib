package org.geysermc.mcprotocollib.network.crypt;

/**
 * An interface for encrypting packets.
 */
public interface PacketEncryption {
    /**
     * Gets the output size from decrypting.
     *
     * @param length Length of the data being decrypted.
     * @return The output size from decrypting.
     */
    int getDecryptOutputSize(int length);

    /**
     * Gets the output size from encrypting.
     *
     * @param length Length of the data being encrypted.
     * @return The output size from encrypting.
     */
    int getEncryptOutputSize(int length);

    /**
     * Decrypts the given data.
     *
     * @param input Input data to decrypt.
     * @param inputOffset Offset of the data to start decrypting at.
     * @param inputLength Length of the data to be decrypted.
     * @param output Array to output decrypted data to.
     * @param outputOffset Offset of the output array to start at.
     * @return The number of bytes stored in the output array.
     * @throws Exception If an error occurs.
     */
    int decrypt(byte[] input, int inputOffset, int inputLength, byte[] output, int outputOffset) throws Exception;

    /**
     * Encrypts the given data.
     *
     * @param input Input data to encrypt.
     * @param inputOffset Offset of the data to start encrypting at.
     * @param inputLength Length of the data to be encrypted.
     * @param output Array to output encrypted data to.
     * @param outputOffset Offset of the output array to start at.
     * @return The number of bytes stored in the output array.
     * @throws Exception If an error occurs.
     */
    int encrypt(byte[] input, int inputOffset, int inputLength, byte[] output, int outputOffset) throws Exception;
}
