package org.geysermc.mcprotocollib.auth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.geysermc.mcprotocollib.auth.util.TextureUrlChecker;
import org.geysermc.mcprotocollib.auth.util.UndashedUUIDAdapter;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Information about a user profile.
 */
public class GameProfile {
    private static final PublicKey SIGNATURE_KEY = loadSignatureKey();
    private static final Gson GSON = new GsonBuilder()
        .registerTypeAdapter(UUID.class, new UndashedUUIDAdapter())
        .create();

    private static PublicKey loadSignatureKey() {
        try (InputStream in = Objects.requireNonNull(SessionService.class.getResourceAsStream("/yggdrasil_session_pubkey.der"))) {
            return KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(in.readAllBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Missing/invalid yggdrasil public key.", e);
        }
    }

    private final UUID id;
    private final String name;

    private List<Property> properties;
    private Map<TextureType, Texture> textures;
    private boolean texturesVerified;

    /**
     * Creates a new GameProfile instance.
     *
     * @param id ID of the profile.
     * @param name Name of the profile.
     */
    public GameProfile(String id, String name) {
        this(id == null || id.isEmpty() ? null : UUID.fromString(id), name);
    }

    /**
     * Creates a new GameProfile instance.
     *
     * @param id ID of the profile.
     * @param name Name of the profile.
     */
    public GameProfile(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Gets whether the profile is complete.
     *
     * @return Whether the profile is complete.
     */
    public boolean isComplete() {
        return this.id != null && this.name != null && !this.name.isEmpty();
    }

    /**
     * Gets the ID of the profile.
     *
     * @return The profile's ID.
     */
    public UUID getId() {
        return this.id;
    }

    /**
     * Gets the ID of the profile as a String.
     *
     * @return The profile's ID as a string.
     */
    public String getIdAsString() {
        return this.id != null ? this.id.toString() : "";
    }

    /**
     * Gets the name of the profile.
     *
     * @return The profile's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets an immutable list of properties contained in the profile.
     *
     * @return The profile's properties.
     */
    public List<Property> getProperties() {
        if (this.properties == null) {
            this.properties = new ArrayList<>();
        }

        return Collections.unmodifiableList(this.properties);
    }

    /**
     * Sets the properties of this profile.
     *
     * @param properties Properties belonging to this profile.
     */
    public void setProperties(List<Property> properties) {
        if (this.properties == null) {
            this.properties = new ArrayList<>();
        } else {
            this.properties.clear();
        }

        if (properties != null) {
            this.properties.addAll(properties);
        }

        // Invalidate cached decoded textures.
        this.textures = null;
        this.texturesVerified = false;
    }

    /**
     * Gets a property contained in the profile.
     *
     * @param name Name of the property.
     * @return The property with the specified name.
     */
    public Property getProperty(String name) {
        for (Property property : this.getProperties()) {
            if (property.getName().equals(name)) {
                return property;
            }
        }

        return null;
    }

    /**
     * Gets an immutable map of texture types to textures contained in the profile.
     *
     * @return The profile's textures.
     * @throws IllegalStateException If an error occurs decoding the profile's texture property.
     */
    public Map<TextureType, Texture> getTextures() throws IllegalStateException {
        return this.getTextures(true);
    }

    /**
     * Gets an immutable map of texture types to textures contained in the profile.
     *
     * @param requireSecure Whether to require the profile's texture payload to be securely signed.
     * @return The profile's textures.
     * @throws IllegalStateException If an error occurs decoding the profile's texture property.
     */
    public Map<TextureType, Texture> getTextures(boolean requireSecure) throws IllegalStateException {
        if (this.textures == null || (requireSecure && !this.texturesVerified)) {
            GameProfile.Property textures = this.getProperty("textures");
            if (textures != null) {
                if (requireSecure) {
                    if (!textures.hasSignature()) {
                        throw new IllegalStateException("Signature is missing from textures payload.");
                    }

                    if (!textures.isSignatureValid(SIGNATURE_KEY)) {
                        throw new IllegalStateException("Textures payload has been tampered with. (signature invalid)");
                    }
                }

                MinecraftTexturesPayload result;
                try {
                    String json = new String(Base64.getDecoder().decode(textures.getValue().getBytes(StandardCharsets.UTF_8)));
                    result = GSON.fromJson(json, MinecraftTexturesPayload.class);
                } catch (Exception e) {
                    throw new IllegalStateException("Could not decode texture payload.", e);
                }

                if (result != null && result.textures != null) {
                    if (requireSecure) {
                        for (GameProfile.Texture texture : result.textures.values()) {
                            if (TextureUrlChecker.isAllowedTextureDomain(texture.getURL())) {
                                continue;
                            }

                            throw new IllegalStateException("Textures payload has been tampered with. (non-whitelisted domain)");
                        }
                    }

                    this.textures = result.textures;
                } else {
                    this.textures = Collections.emptyMap();
                }

                this.texturesVerified = requireSecure;
            } else {
                return Collections.emptyMap();
            }
        }

        return Collections.unmodifiableMap(this.textures);
    }

    /**
     * Gets a texture contained in the profile.
     *
     * @param type Type of texture to get.
     * @return The texture of the specified type.
     * @throws IllegalStateException If an error occurs decoding the profile's texture property.
     */
    public Texture getTexture(TextureType type) throws IllegalStateException {
        return this.getTextures().get(type);
    }

    /**
     * Gets a texture contained in the profile.
     *
     * @param type Type of texture to get.
     * @param requireSecure Whether to require the profile's texture payload to be securely signed.
     * @return The texture of the specified type.
     * @throws IllegalStateException If an error occurs decoding the profile's texture property.
     */
    public Texture getTexture(TextureType type, boolean requireSecure) throws IllegalStateException {
        return this.getTextures(requireSecure).get(type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            GameProfile that = (GameProfile) o;
            return Objects.equals(this.id, that.id) && Objects.equals(this.name, that.name);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = this.id != null ? this.id.hashCode() : 0;
        result = 31 * result + (this.name != null ? this.name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GameProfile{id=" + this.id + ", name=" + this.name + ", properties=" + this.getProperties() + "}";
    }

    /**
     * A property belonging to a profile.
     */
    public static class Property {
        private final String name;
        private final String value;
        private final String signature;

        /**
         * Creates a new Property instance.
         *
         * @param name Name of the property.
         * @param value Value of the property.
         */
        public Property(String name, String value) {
            this(name, value, null);
        }

        /**
         * Creates a new Property instance.
         *
         * @param name Name of the property.
         * @param value Value of the property.
         * @param signature Signature used to verify the property.
         */
        public Property(String name, String value, String signature) {
            this.name = name;
            this.value = value;
            this.signature = signature;
        }

        /**
         * Gets the name of the property.
         *
         * @return The property's name.
         */
        public String getName() {
            return this.name;
        }

        /**
         * Gets the value of the property.
         *
         * @return The property's value.
         */
        public String getValue() {
            return this.value;
        }

        /**
         * Gets whether this property has a signature to verify it.
         *
         * @return Whether this property is signed.
         */
        public boolean hasSignature() {
            return this.signature != null;
        }

        /**
         * Gets the signature used to verify the property.
         *
         * @return The property's signature.
         */
        public String getSignature() {
            return this.signature;
        }

        /**
         * Gets whether this property's signature is valid.
         *
         * @param key Public key to validate the signature against.
         * @return Whether the signature is valid.
         * @throws IllegalStateException If the signature could not be validated.
         */
        public boolean isSignatureValid(PublicKey key) throws IllegalStateException {
            if (!this.hasSignature()) {
                return false;
            }

            try {
                Signature sig = Signature.getInstance("SHA1withRSA");
                sig.initVerify(key);
                sig.update(this.value.getBytes());
                return sig.verify(Base64.getDecoder().decode(this.signature.getBytes(StandardCharsets.UTF_8)));
            } catch (Exception e) {
                throw new IllegalStateException("Could not validate property signature.", e);
            }
        }

        @Override
        public String toString() {
            return "Property{name=" + this.name + ", value=" + this.value + ", signature=" + this.signature + "}";
        }
    }

    /**
     * The type of a profile texture.
     */
    public enum TextureType {
        SKIN,
        CAPE,
        ELYTRA;
    }

    /**
     * The model used for a profile texture.
     */
    public enum TextureModel {
        WIDE,
        SLIM;
    }

    /**
     * A texture contained within a profile.
     */
    public static class Texture {
        private final String url;
        private final Map<String, String> metadata;

        /**
         * Creates a new Texture instance.
         *
         * @param url URL of the texture.
         * @param metadata Metadata of the texture.
         */
        public Texture(String url, Map<String, String> metadata) {
            this.url = url;
            this.metadata = metadata;
        }

        /**
         * Gets the URL of the texture.
         *
         * @return The texture's URL.
         */
        public String getURL() {
            return this.url;
        }

        /**
         * Gets a metadata string from the texture.
         *
         * @return The metadata value corresponding to the given key.
         */
        public String getMetadata(String key) {
            if (this.metadata == null) {
                return null;
            }

            return this.metadata.get(key);
        }

        /**
         * Gets the model of the texture.
         *
         * @return The texture's model.
         */
        public TextureModel getModel() {
            String model = this.getMetadata("model");
            return model != null && model.equals("slim") ? TextureModel.SLIM : TextureModel.WIDE;
        }

        /**
         * Gets the hash of the texture.
         *
         * @return The texture's hash.
         */
        public String getHash() {
            String url = this.url.endsWith("/") ? this.url.substring(0, this.url.length() - 1) : this.url;
            int slash = url.lastIndexOf("/");
            int dot = url.lastIndexOf(".");
            if (dot < slash) {
                dot = url.length();
            }

            return url.substring(slash + 1, dot != -1 ? dot : url.length());
        }

        @Override
        public String toString() {
            return "Texture{url=" + this.url + ", model=" + this.getModel() + ", hash=" + this.getHash() + "}";
        }
    }

    private static class MinecraftTexturesPayload {
        public long timestamp;
        public UUID profileId;
        public String profileName;
        public boolean isPublic;
        public Map<GameProfile.TextureType, GameProfile.Texture> textures;
    }
}
