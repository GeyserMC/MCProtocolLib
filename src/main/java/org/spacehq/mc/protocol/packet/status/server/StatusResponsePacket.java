package org.spacehq.mc.protocol.packet.status.server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.auth.util.Base64;
import org.spacehq.mc.protocol.data.message.Message;
import org.spacehq.mc.protocol.data.status.PlayerInfo;
import org.spacehq.mc.protocol.data.status.ServerStatusInfo;
import org.spacehq.mc.protocol.data.status.VersionInfo;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class StatusResponsePacket implements Packet {

	private ServerStatusInfo info;

	@SuppressWarnings("unused")
	private StatusResponsePacket() {
	}

	public StatusResponsePacket(ServerStatusInfo info) {
		this.info = info;
	}

	public ServerStatusInfo getInfo() {
		return this.info;
	}

	public void read(NetInput in) throws IOException {
		JsonObject obj = new Gson().fromJson(in.readString(), JsonObject.class);
		JsonObject ver = obj.get("version").getAsJsonObject();
		VersionInfo version = new VersionInfo(ver.get("name").getAsString(), ver.get("protocol").getAsInt());
		JsonObject plrs = obj.get("players").getAsJsonObject();
		GameProfile profiles[] = new GameProfile[0];
		if(plrs.has("sample")) {
			JsonArray prof = plrs.get("sample").getAsJsonArray();
			if(prof.size() > 0) {
				profiles = new GameProfile[prof.size()];
				for(int index = 0; index < prof.size(); index++) {
					JsonObject o = prof.get(index).getAsJsonObject();
					profiles[index] = new GameProfile(o.get("id").getAsString(), o.get("name").getAsString());
				}
			}
		}

		PlayerInfo players = new PlayerInfo(plrs.get("max").getAsInt(), plrs.get("online").getAsInt(), profiles);
		JsonElement desc = obj.get("description");
		Message description = Message.fromJson(desc);
		BufferedImage icon = null;
		if(obj.has("favicon")) {
			icon = this.stringToIcon(obj.get("favicon").getAsString());
		}

		this.info = new ServerStatusInfo(version, players, description, icon);
	}

	public void write(NetOutput out) throws IOException {
		JsonObject obj = new JsonObject();
		JsonObject ver = new JsonObject();
		ver.addProperty("name", this.info.getVersionInfo().getVersionName());
		ver.addProperty("protocol", this.info.getVersionInfo().getProtocolVersion());
		JsonObject plrs = new JsonObject();
		plrs.addProperty("max", this.info.getPlayerInfo().getMaxPlayers());
		plrs.addProperty("online", this.info.getPlayerInfo().getOnlinePlayers());
		if(this.info.getPlayerInfo().getPlayers().length > 0) {
			JsonArray array = new JsonArray();
			for(GameProfile profile : this.info.getPlayerInfo().getPlayers()) {
				JsonObject o = new JsonObject();
				o.addProperty("name", profile.getName());
				o.addProperty("id", profile.getIdAsString());
				array.add(o);
			}

			plrs.add("sample", array);
		}

		obj.add("version", ver);
		obj.add("players", plrs);
		obj.add("description", this.info.getDescription().toJson());
		if(this.info.getIcon() != null) {
			obj.addProperty("favicon", this.iconToString(this.info.getIcon()));
		}

		out.writeString(obj.toString());
	}

	private BufferedImage stringToIcon(String str) throws IOException {
		if(str.startsWith("data:image/png;base64,")) {
			str = str.substring("data:image/png;base64,".length());
		}

		byte bytes[] = Base64.decode(str.getBytes("UTF-8"));
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		BufferedImage icon = ImageIO.read(in);
		in.close();
		if(icon != null && (icon.getWidth() != 64 || icon.getHeight() != 64)) {
			throw new IOException("Icon must be 64x64.");
		}

		return icon;
	}

	private String iconToString(BufferedImage icon) throws IOException {
		if(icon.getWidth() != 64 || icon.getHeight() != 64) {
			throw new IOException("Icon must be 64x64.");
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(icon, "PNG", out);
		out.close();
		byte encoded[] = Base64.encode(out.toByteArray());
		return "data:image/png;base64," + new String(encoded, "UTF-8");
	}

	public boolean isPriority() {
		return false;
	}

}
