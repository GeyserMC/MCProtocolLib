package ch.spacebase.mc.auth;

public class GameProfile {

	private String id;
	private String name;

	public GameProfile(String id, String name) {
		if((id == null || id.equals("")) && (name == null || name.equals(""))) {
			throw new IllegalArgumentException("Name and ID cannot both be blank");
		} else {
			this.id = id;
			this.name = name;
		}
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public boolean isComplete() {
		return this.id != null && !this.id.equals("") && this.name != null && !this.name.equals("");
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		} else if(o != null && this.getClass() == o.getClass()) {
			GameProfile that = (GameProfile) o;
			return !this.id.equals(that.id) ? false : this.name.equals(that.name);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int result = this.id.hashCode();
		result = 31 * result + this.name.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "GameProfile{id=" + this.id + ", name=" + this.name + "}";
	}
	
}
