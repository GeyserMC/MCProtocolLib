package org.spacehq.mc.auth;

public class ProfileTexture {

	private String url;

	public ProfileTexture(String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

	public String getHash() {
		String url = this.url.endsWith("/") ? this.url.substring(0, this.url.length() - 1) : this.url;
		int slash = url.lastIndexOf("/");
		int dot = url.lastIndexOf(".", slash);
		return url.substring(slash + 1, dot != -1 ? dot : url.length());
	}

	@Override
	public String toString() {
		return "ProfileTexture{url=" + this.url + ", hash=" + this.getHash() + "}";
	}

}
