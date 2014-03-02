package org.spacehq.mc.auth.response;

import org.spacehq.mc.auth.ProfileTexture;

import java.util.Map;

public class MinecraftTexturesPayload {

	private long timestamp;
	private String profileId;
	private String profileName;
	private Map<String, ProfileTexture> textures;

	public long getTimestamp() {
		return this.timestamp;
	}

	public String getProfileId() {
		return this.profileId;
	}

	public String getProfileName() {
		return this.profileName;
	}

	public Map<String, ProfileTexture> getTextures() {
		return this.textures;
	}

}
