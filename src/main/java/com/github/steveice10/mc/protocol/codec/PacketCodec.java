package com.github.steveice10.mc.protocol.codec;

import com.github.steveice10.mc.protocol.data.ProtocolState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.EnumMap;
import java.util.function.Supplier;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PacketCodec {

    @Getter
    private final int protocolVersion;

    @Getter
    private final String minecraftVersion;

    private final EnumMap<ProtocolState, PacketStateCodec> stateProtocols;

    @Getter
    private final Supplier<MinecraftCodecHelper> helperFactory;

    public PacketStateCodec getCodec(ProtocolState protocolState) {
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
        builder.helperFactory = this.helperFactory;

        return builder;
    }

    public static class Builder {
        private int protocolVersion = -1;
        private String minecraftVersion = null;
        private EnumMap<ProtocolState, PacketStateCodec> stateProtocols = new EnumMap<>(ProtocolState.class);
        private Supplier<MinecraftCodecHelper> helperFactory;

        public Builder protocolVersion(int protocolVersion) {
            this.protocolVersion = protocolVersion;
            return this;
        }

        public Builder minecraftVersion(String minecraftVersion) {
            this.minecraftVersion = minecraftVersion;
            return this;
        }

        public Builder state(ProtocolState state, PacketStateCodec.Builder protocol) {
            this.stateProtocols.put(state, protocol.build());
            return this;
        }

        public Builder helper(Supplier<MinecraftCodecHelper> helperFactory) {
            this.helperFactory = helperFactory;
            return this;
        }

        public PacketCodec build() {
            return new PacketCodec(this.protocolVersion, this.minecraftVersion, this.stateProtocols, this.helperFactory);
        }
    }
}
