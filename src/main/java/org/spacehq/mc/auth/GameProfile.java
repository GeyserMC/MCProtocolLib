package org.spacehq.mc.auth;

import org.spacehq.mc.auth.properties.PropertyMap;

import java.util.UUID;

public class GameProfile {

	private UUID id;
	private String name;
	private PropertyMap properties = new PropertyMap();
	private boolean legacy;

	public GameProfile(String id, String name) {
		this(id == null || id.equals("") ? null : UUID.fromString(id), name);
	}

	public GameProfile(UUID id, String name) {
		if(id == null && (name == null || name.equals(""))) {
			throw new IllegalArgumentException("Name and ID cannot both be blank");
		} else {
			this.id = id;
			this.name = name;
		}
	}

	public UUID getId() {
		return this.id;
	}

	public String getIdAsString() {
		return this.id != null ? this.id.toString() : "";
	}

	public String getName() {
		return this.name;
	}

	public PropertyMap getProperties() {
		return this.properties;
	}

	public boolean isLegacy() {
		return this.legacy;
	}

	public boolean isComplete() {
		return this.id != null && this.name != null && !this.name.equals("");
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		} else if(o != null && this.getClass() == o.getClass()) {
			GameProfile that = (GameProfile) o;
			return (this.id != null ? this.id.equals(that.id) : that.id == null) && (this.name != null ? this.name.equals(that.name) : that.name == null);
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
		return "GameProfile{id=" + this.id + ", name=" + this.name + ", properties=" + this.properties + ", legacy=" + this.legacy + "}";
	}

}
