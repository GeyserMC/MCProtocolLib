package org.spacehq.mc.auth;

public class ProfileCriteria {

	private final String name;

	public ProfileCriteria(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public boolean equals(Object o) {
		if(this == o) {
			return true;
		} else if(o != null && this.getClass() == o.getClass()) {
			ProfileCriteria that = (ProfileCriteria) o;
			return this.name.toLowerCase().equals(that.name.toLowerCase());
		} else {
			return false;
		}
	}

	public int hashCode() {
		return 31 * this.name.toLowerCase().hashCode();
	}

	public String toString() {
		return "GameProfileRepository{name=" + this.name + "}";
	}

}
