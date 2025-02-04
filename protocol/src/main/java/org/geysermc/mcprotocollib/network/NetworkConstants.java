package org.geysermc.mcprotocollib.network;

import io.netty.util.AttributeKey;
import org.geysermc.mcprotocollib.network.compression.CompressionConfig;
import org.geysermc.mcprotocollib.network.crypt.EncryptionConfig;

public class NetworkConstants {
    public static final AttributeKey<CompressionConfig> COMPRESSION_ATTRIBUTE_KEY = AttributeKey.valueOf("compression");
    public static final AttributeKey<EncryptionConfig> ENCRYPTION_ATTRIBUTE_KEY = AttributeKey.valueOf("encryption");
    public static final String PROXY_NAME = "proxy";
    public static final String PROXY_PROTOCOL_ENCODER_NAME = "proxy-protocol-encoder";
    public static final String PROXY_PROTOCOL_PACKET_SENDER_NAME = "proxy-protocol-packet-sender";
    public static final String READ_TIMEOUT_NAME = "read-timeout";
    public static final String WRITE_TIMEOUT_NAME = "write-timeout";
    public static final String ENCRYPTION_NAME = "encryption";
    public static final String SIZER_NAME = "sizer";
    public static final String COMPRESSION_NAME = "compression";
    public static final String FLOW_CONTROL_NAME = "flow-control";
    public static final String CODEC_NAME = "codec";
    public static final String FLUSH_HANDLER_NAME = "flush-handler";
    public static final String MANAGER_NAME = "manager";
}
