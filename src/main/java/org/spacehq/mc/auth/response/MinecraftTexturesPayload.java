package org.spacehq.mc.auth.response;

import org.spacehq.mc.auth.ProfileTexture;
import org.spacehq.mc.auth.ProfileTextureType;

import java.util.Map;
import java.util.UUID;

public class MinecraftTexturesPayload {

	private long timestamp;
	private UUID profileId;
	private String profileName;
	private boolean isPublic;
	private Map<ProfileTextureType, ProfileTexture> textures;

	public long getTimestamp() {
		return this.timestamp;
	}

	public UUID getProfileId() {
		return this.profileId;
	}

	public String getProfileName() {
		return this.profileName;
	}

	public boolean isPublic() {
		return this.isPublic;
	}

	public Map<ProfileTextureType, ProfileTexture> getTextures() {
		return this.textures;
	}

}
