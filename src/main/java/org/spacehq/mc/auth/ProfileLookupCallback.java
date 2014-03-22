package org.spacehq.mc.auth;

public interface ProfileLookupCallback {

	public void onProfileLookupSucceeded(GameProfile profile);

	public void onProfileLookupFailed(GameProfile profile, Exception e);

}
