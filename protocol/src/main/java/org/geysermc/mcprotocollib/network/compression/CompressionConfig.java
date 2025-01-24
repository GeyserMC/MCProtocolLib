package org.geysermc.mcprotocollib.network.compression;

public record CompressionConfig(int threshold, PacketCompression compression, boolean validateDecompression) {
}
