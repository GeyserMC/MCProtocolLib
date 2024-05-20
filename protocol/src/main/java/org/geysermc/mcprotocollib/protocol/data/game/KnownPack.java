package org.geysermc.mcprotocollib.protocol.data.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.geysermc.mcprotocollib.protocol.codec.NetworkCodec;

@Data
@AllArgsConstructor
public class KnownPack {
    public static NetworkCodec<KnownPack> NETWORK_CODEC = NetworkCodec.of((knownPack, out, helper) -> {
        helper.writeString(out, knownPack.getNamespace());
        helper.writeString(out, knownPack.getId());
        helper.writeString(out, knownPack.getVersion());
    }, (in, helper) -> {
        String namespace = helper.readString(in);
        String id = helper.readString(in);
        String version = helper.readString(in);
        return new KnownPack(namespace, id, version);
    });

    private String namespace;
    private String id;
    private String version;
}
