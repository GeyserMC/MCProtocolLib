package com.github.steveice10.mc.protocol.packet.status.server;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.auth.util.Base64;
import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.mc.protocol.data.status.PlayerInfo;
import com.github.steveice10.mc.protocol.data.status.ServerStatusInfo;
import com.github.steveice10.mc.protocol.data.status.VersionInfo;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.With;
import net.kyori.adventure.text.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class StatusResponsePacket implements Packet {
    private @NonNull ServerStatusInfo info;

    @Override
    public void read(NetInput in) throws IOException {
        JsonObject obj = new Gson().fromJson(in.readString(), JsonObject.class);
        JsonObject ver = obj.get("version").getAsJsonObject();
        VersionInfo version = new VersionInfo(ver.get("name").getAsString(), ver.get("protocol").getAsInt());
        JsonObject plrs = obj.get("players").getAsJsonObject();
        GameProfile[] profiles = new GameProfile[0];
        if (plrs.has("sample")) {
            JsonArray prof = plrs.get("sample").getAsJsonArray();
            if (prof.size() > 0) {
                profiles = new GameProfile[prof.size()];
                for (int index = 0; index < prof.size(); index++) {
                    JsonObject o = prof.get(index).getAsJsonObject();
                    profiles[index] = new GameProfile(o.get("id").getAsString(), o.get("name").getAsString());
                }
            }
        }

        PlayerInfo players = new PlayerInfo(plrs.get("max").getAsInt(), plrs.get("online").getAsInt(), profiles);
        JsonElement desc = obj.get("description");
        Component description = DefaultComponentSerializer.get().serializer().fromJson(desc, Component.class);
        byte[] icon = null;
        if (obj.has("favicon")) {
            icon = this.stringToIcon(obj.get("favicon").getAsString());
        }

        this.info = new ServerStatusInfo(version, players, description, icon);
    }

    @Override
    public void write(NetOutput out) throws IOException {
        JsonObject obj = new JsonObject();
        JsonObject ver = new JsonObject();
        ver.addProperty("name", this.info.getVersionInfo().getVersionName());
        ver.addProperty("protocol", this.info.getVersionInfo().getProtocolVersion());
        JsonObject plrs = new JsonObject();
        plrs.addProperty("max", this.info.getPlayerInfo().getMaxPlayers());
        plrs.addProperty("online", this.info.getPlayerInfo().getOnlinePlayers());
        if (this.info.getPlayerInfo().getPlayers().length > 0) {
            JsonArray array = new JsonArray();
            for (GameProfile profile : this.info.getPlayerInfo().getPlayers()) {
                JsonObject o = new JsonObject();
                o.addProperty("name", profile.getName());
                o.addProperty("id", profile.getIdAsString());
                array.add(o);
            }

            plrs.add("sample", array);
        }

        obj.add("version", ver);
        obj.add("players", plrs);
        obj.add("description", new Gson().fromJson(DefaultComponentSerializer.get().serialize(this.info.getDescription()), JsonElement.class));
        if (this.info.getIconPng() != null) {
            obj.addProperty("favicon", this.iconToString(this.info.getIconPng()));
        }

        out.writeString(obj.toString());
    }

    @Override
    public boolean isPriority() {
        return false;
    }

    private byte[] stringToIcon(String str) {
        if (str.startsWith("data:image/png;base64,")) {
            str = str.substring("data:image/png;base64,".length());
        }

        return Base64.decode(str.getBytes(StandardCharsets.UTF_8));
    }

    private String iconToString(byte[] icon) {
        return "data:image/png;base64," + new String(Base64.encode(icon), StandardCharsets.UTF_8);
    }
}
