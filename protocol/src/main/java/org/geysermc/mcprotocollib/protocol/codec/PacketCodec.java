package org.geysermc.mcprotocollib.protocol.codec;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.geysermc.mcprotocollib.network.packet.PacketRegistry;
import org.geysermc.mcprotocollib.protocol.data.ProtocolState;

import java.util.EnumMap;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PacketCodec {

    @Getter
    private final int protocolVersion;

    @Getter
    private final String minecraftVersion;

    private final EnumMap<ProtocolState, PacketRegistry> stateProtocols;

    public PacketRegistry getCodec(ProtocolState protocolState) {
        return this.stateProtocols.get(protocolState);
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        Builder builder = new Builder();

        builder.protocolVersion = this.protocolVersion;
        builder.stateProtocols = this.stateProtocols;
        builder.minecraftVersion = this.minecraftVersion;

        return builder;
    }

    public static class Builder {
        private int protocolVersion = -1;
        private String minecraftVersion = null;
        private EnumMap<ProtocolState, PacketRegistry> stateProtocols = new EnumMap<>(ProtocolState.class);

        public Builder protocolVersion(int protocolVersion) {
            this.protocolVersion = protocolVersion;
            return this;
        }

        public Builder minecraftVersion(String minecraftVersion) {
            this.minecraftVersion = minecraftVersion;
            return this;
        }

        public Builder state(ProtocolState state, MinecraftPacketRegistry protocol) {
            this.stateProtocols.put(state, protocol.build());
            return this;
        }

        public PacketCodec build() {
            return new PacketCodec(this.protocolVersion, this.minecraftVersion, this.stateProtocols);
        }
    }
}
