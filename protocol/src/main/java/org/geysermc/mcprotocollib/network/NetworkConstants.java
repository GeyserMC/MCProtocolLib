package org.geysermc.mcprotocollib.network;

import io.netty.util.AttributeKey;
import org.geysermc.mcprotocollib.network.compression.CompressionConfig;
import org.geysermc.mcprotocollib.network.crypt.EncryptionConfig;

public class NetworkConstants {
    public static final AttributeKey<CompressionConfig> COMPRESSION_ATTRIBUTE_KEY = AttributeKey.valueOf("compression_threshold");
    public static final AttributeKey<EncryptionConfig> ENCRYPTION_ATTRIBUTE_KEY = AttributeKey.valueOf("encryption");
}
