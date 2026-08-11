package org.geysermc.mcprotocollib.text.serializer.nbt;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.object.ObjectContents;
import net.kyori.adventure.text.object.PlayerHeadObjectContents;
import net.kyori.adventure.text.object.SpriteObjectContents;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Reads and writes the {@code ObjectInfo} that vanilla's {@code ObjectContents} inlines into the
 * component compound.
 *
 * <p>Because the info is inlined rather than nested, these methods work on the component's own
 * compound: {@code atlas}, {@code sprite}, {@code player} and {@code hat} sit next to {@code type}
 * and {@code extra}. The component's {@code fallback} belongs to {@code ObjectContents} itself and
 * stays with the core serializer.
 *
 * <p>Adventure's {@code PlayerHeadObjectContents#texture()} has no counterpart in vanilla's
 * {@code ResolvableProfile}, which only carries a name, a uuid and signed properties. There is no
 * field to put it in, so a player head built from a bare texture key is written as an empty profile
 * and the texture is lost. Everything a vanilla server can express round trips intact.
 */
final class ObjectContentsSerializer {

    private ObjectContentsSerializer() {
    }

    // Reading

    /**
     * Picks the object variant and reads its fields.
     *
     * <p>As with content types, an explicit {@code object} discriminator wins and anything else is
     * inferred from which identifying field is present. {@code atlas} is optional on the sprite
     * variant, so {@code sprite} is the field that identifies it.
     */
    static ObjectContents deserialize(final NbtMap tag) {
        final String type = Tags.getString(tag, ComponentFields.OBJECT);
        if (type != null) {
            return switch (type) {
                case ComponentFields.OBJECT_ATLAS_SPRITE -> deserializeSprite(tag);
                case ComponentFields.OBJECT_PLAYER_HEAD -> deserializePlayerHead(tag);
                default -> throw new NbtSerializationException("Unknown object type '" + type + "'");
            };
        }

        if (tag.containsKey(ComponentFields.SPRITE)) {
            return deserializeSprite(tag);
        } else if (tag.containsKey(ComponentFields.PLAYER)) {
            return deserializePlayerHead(tag);
        }
        throw new NbtSerializationException("An object component has no sprite or player field: " + tag.keySet());
    }

    private static SpriteObjectContents deserializeSprite(final NbtMap tag) {
        final Key sprite = Key.key(Tags.getRequiredString(tag, ComponentFields.SPRITE));
        final String atlas = Tags.getString(tag, ComponentFields.ATLAS);
        return atlas == null ? ObjectContents.sprite(sprite) : ObjectContents.sprite(Key.key(atlas), sprite);
    }

    private static PlayerHeadObjectContents deserializePlayerHead(final NbtMap tag) {
        final Object profile = tag.get(ComponentFields.PLAYER);
        if (profile == null) {
            throw new NbtSerializationException("Missing required field '" + ComponentFields.PLAYER + "'");
        }

        final PlayerHeadObjectContents.Builder builder = ObjectContents.playerHead();
        if (profile instanceof String name) {
            // ResolvableProfile's codec accepts a bare string as the shorthand for a name only profile
            builder.name(name);
        } else if (profile instanceof NbtMap compound) {
            deserializeProfile(compound, builder);
        } else {
            throw new NbtSerializationException("Expected field '" + ComponentFields.PLAYER
                + "' to be a compound or a string, got " + Tags.typeNameOf(profile));
        }

        return builder
            .hat(Tags.getBooleanOrDefault(tag, ComponentFields.HAT, PlayerHeadObjectContents.DEFAULT_HAT))
            .build();
    }

    private static void deserializeProfile(final NbtMap tag, final PlayerHeadObjectContents.Builder builder) {
        final String name = Tags.getString(tag, ComponentFields.PROFILE_NAME);
        if (name != null) {
            builder.name(name);
        }

        final int[] id = getIntArray(tag, ComponentFields.PROFILE_ID);
        if (id != null) {
            builder.id(uuidFromIntArray(id));
        }

        final List<Object> properties = Tags.getList(tag, ComponentFields.PROFILE_PROPERTIES);
        if (!properties.isEmpty()) {
            final List<PlayerHeadObjectContents.ProfileProperty> converted = new ArrayList<>(properties.size());
            for (final Object property : properties) {
                converted.add(deserializeProfileProperty(property));
            }
            builder.profileProperties(converted);
        }
    }

    private static PlayerHeadObjectContents.ProfileProperty deserializeProfileProperty(final Object tag) {
        if (!(tag instanceof NbtMap property)) {
            throw new NbtSerializationException("Expected a profile property to be a compound, got " + Tags.typeNameOf(tag));
        }

        final String name = Tags.getRequiredString(property, ComponentFields.PROFILE_PROPERTY_NAME);
        final String value = Tags.getRequiredString(property, ComponentFields.PROFILE_PROPERTY_VALUE);
        final String signature = Tags.getString(property, ComponentFields.PROFILE_PROPERTY_SIGNATURE);
        return signature == null
            ? PlayerHeadObjectContents.property(name, value)
            : PlayerHeadObjectContents.property(name, value, signature);
    }

    // Writing

    /**
     * Writes the object variant into the component compound.
     *
     * @param emitDiscriminator whether to write the {@code object} discriminator. Vanilla's own
     *                          encoder never does, leaving the variant to be inferred from whether
     *                          {@code sprite} or {@code player} is present, so this follows the same
     *                          setting as the component type discriminator rather than being forced on.
     */
    static void serialize(final ObjectContents contents, final NbtMapBuilder tag, final boolean emitDiscriminator) {
        if (contents instanceof SpriteObjectContents sprite) {
            if (emitDiscriminator) {
                tag.putString(ComponentFields.OBJECT, ComponentFields.OBJECT_ATLAS_SPRITE);
            }
            tag.putString(ComponentFields.SPRITE, sprite.sprite().asString());
            if (!sprite.atlas().equals(SpriteObjectContents.DEFAULT_ATLAS)) {
                tag.putString(ComponentFields.ATLAS, sprite.atlas().asString());
            }
        } else if (contents instanceof PlayerHeadObjectContents head) {
            if (emitDiscriminator) {
                tag.putString(ComponentFields.OBJECT, ComponentFields.OBJECT_PLAYER_HEAD);
            }
            tag.putCompound(ComponentFields.PLAYER, serializeProfile(head));
            if (head.hat() != PlayerHeadObjectContents.DEFAULT_HAT) {
                tag.putBoolean(ComponentFields.HAT, head.hat());
            }
        } else {
            throw new NbtSerializationException("Unknown object contents type " + contents.getClass().getName());
        }
    }

    private static NbtMap serializeProfile(final PlayerHeadObjectContents head) {
        final NbtMapBuilder profile = NbtMap.builder();

        final String name = head.name();
        if (name != null) {
            profile.putString(ComponentFields.PROFILE_NAME, name);
        }

        final UUID id = head.id();
        if (id != null) {
            profile.putIntArray(ComponentFields.PROFILE_ID, uuidToIntArray(id));
        }

        final List<PlayerHeadObjectContents.ProfileProperty> properties = head.profileProperties();
        if (!properties.isEmpty()) {
            final List<Object> serialized = new ArrayList<>(properties.size());
            for (final PlayerHeadObjectContents.ProfileProperty property : properties) {
                serialized.add(serializeProfileProperty(property));
            }
            profile.put(ComponentFields.PROFILE_PROPERTIES, Tags.buildList(serialized));
        }

        return profile.build();
    }

    private static NbtMap serializeProfileProperty(final PlayerHeadObjectContents.ProfileProperty property) {
        final NbtMapBuilder tag = NbtMap.builder()
            .putString(ComponentFields.PROFILE_PROPERTY_NAME, property.name())
            .putString(ComponentFields.PROFILE_PROPERTY_VALUE, property.value());

        final String signature = property.signature();
        if (signature != null) {
            tag.putString(ComponentFields.PROFILE_PROPERTY_SIGNATURE, signature);
        }

        return tag.build();
    }

    // Shared

    private static int @Nullable [] getIntArray(final NbtMap tag, final String key) {
        final Object value = tag.get(key);
        if (value == null) {
            return null;
        } else if (value instanceof int[] array) {
            return array;
        }
        throw new NbtSerializationException("Expected field '" + key + "' to be an int array, got " + Tags.typeNameOf(value));
    }

    /**
     * Mirrors vanilla's {@code UUIDUtil#uuidFromIntArray}: the most significant half comes first,
     * high int before low int, and the least significant half follows in the same order.
     */
    private static UUID uuidFromIntArray(final int[] value) {
        if (value.length != 4) {
            throw new NbtSerializationException("A uuid must be four ints, got " + value.length);
        }
        return new UUID(
            (long) value[0] << 32 | (value[1] & 0xFFFFFFFFL),
            (long) value[2] << 32 | (value[3] & 0xFFFFFFFFL));
    }

    private static int[] uuidToIntArray(final UUID uuid) {
        final long most = uuid.getMostSignificantBits();
        final long least = uuid.getLeastSignificantBits();
        return new int[]{(int) (most >> 32), (int) most, (int) (least >> 32), (int) least};
    }
}
