package org.geysermc.mcprotocollib.auth.texture;

import java.util.Map;

/**
 * A texture contained within a profile.
 */
public class Texture {
    private final String url;
    private final Map<String, String> metadata;

    /**
     * Creates a new Texture instance.
     *
     * @param url      URL of the texture.
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
