package org.geysermc.adventure.text.serializer.nbt;

import net.kyori.adventure.text.object.PlayerHeadObjectContents;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;
import org.cloudburstmc.nbt.NbtType;

import java.util.List;

final class ResolvableProfileSerializerImpl {

    static PlayerHeadObjectContents.Builder deserialize(NbtMap map) {

    }

    static NbtMap serialize(PlayerHeadObjectContents profile) {
        NbtMapBuilder builder = NbtMap.builder();
        NbtSerializationUtil.checkNonNull(profile.name(), name -> builder.putString("name", name));
        NbtSerializationUtil.checkNonNull(profile.id(), uuid -> builder.putIntArray("id", NbtSerializationUtil.serializeUUID(uuid)));
        NbtSerializationUtil.checkNonNull(profile.texture(), texture -> builder.putString("texture", texture.asString()));

        List<NbtMap> properties = profile.profileProperties().stream().map(ResolvableProfileSerializerImpl::serializeProfileProperty).toList();
        if (!properties.isEmpty()) {
            builder.putList("properties", NbtType.COMPOUND, properties);
        }

        return builder.build();
    }

    private static NbtMap serializeProfileProperty(PlayerHeadObjectContents.ProfileProperty property) {
        NbtMapBuilder builder = NbtMap.builder();
        builder.putString("name", property.name());
        builder.putString("value", property.value());
        NbtSerializationUtil.checkNonNull(property.signature(), signature -> builder.putString("signature", signature));
        return builder.build();
    }
}
