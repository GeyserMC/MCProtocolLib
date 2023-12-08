package com.github.steveice10.mc.protocol.packet.status.clientbound;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.auth.util.Base64;
import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.mc.protocol.data.status.PlayerInfo;
import com.github.steveice10.mc.protocol.data.status.ServerStatusInfo;
import com.github.steveice10.mc.protocol.data.status.VersionInfo;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ClientboundStatusResponsePacket implements MinecraftPacket {

    // vanilla behavior falls back to false if the field was not sent
    private static final boolean ENFORCES_SECURE_CHAT_DEFAULT = false;

    private final @NonNull ServerStatusInfo info;

    public ClientboundStatusResponsePacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        JsonObject obj = new Gson().fromJson(helper.readString(in), JsonObject.class);
        JsonElement desc = obj.get("description");
        Component description = DefaultComponentSerializer.get().serializer().fromJson(desc, Component.class);
        JsonObject plrs = obj.get("players").getAsJsonObject();
        List<GameProfile> profiles = new ArrayList<>();
        if (plrs.has("sample")) {
            JsonArray prof = plrs.get("sample").getAsJsonArray();
            if (prof.size() > 0) {
                for (int index = 0; index < prof.size(); index++) {
                    JsonObject o = prof.get(index).getAsJsonObject();
                    profiles.add(new GameProfile(o.get("id").getAsString(), o.get("name").getAsString()));
                }
            }
        }

        PlayerInfo players = new PlayerInfo(plrs.get("max").getAsInt(), plrs.get("online").getAsInt(), profiles);
        JsonObject ver = obj.get("version").getAsJsonObject();
        VersionInfo version = new VersionInfo(ver.get("name").getAsString(), ver.get("protocol").getAsInt());
        byte[] icon = null;
        if (obj.has("favicon")) {
            icon = this.stringToIcon(obj.get("favicon").getAsString());
        }

        boolean enforcesSecureChat = ENFORCES_SECURE_CHAT_DEFAULT;
        if (obj.has("enforcesSecureChat")) {
            enforcesSecureChat = obj.get("enforcesSecureChat").getAsBoolean();
        }
        this.info = new ServerStatusInfo(version, players, description, icon, enforcesSecureChat);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        JsonObject obj = new JsonObject();
        JsonObject ver = new JsonObject();
        ver.addProperty("name", this.info.getVersionInfo().getVersionName());
        ver.addProperty("protocol", this.info.getVersionInfo().getProtocolVersion());
        JsonObject plrs = new JsonObject();
        plrs.addProperty("max", this.info.getPlayerInfo().getMaxPlayers());
        plrs.addProperty("online", this.info.getPlayerInfo().getOnlinePlayers());
        if (this.info.getPlayerInfo().getPlayers().size() > 0) {
            JsonArray array = new JsonArray();
            for (GameProfile profile : this.info.getPlayerInfo().getPlayers()) {
                JsonObject o = new JsonObject();
                o.addProperty("name", profile.getName());
                o.addProperty("id", profile.getIdAsString());
                array.add(o);
            }

            plrs.add("sample", array);
        }

        obj.add("description", new Gson().fromJson(DefaultComponentSerializer.get().serialize(this.info.getDescription()), JsonElement.class));
        obj.add("players", plrs);
        obj.add("version", ver);
        if (this.info.getIconPng() != null) {
            obj.addProperty("favicon", this.iconToString(this.info.getIconPng()));
        }
        obj.addProperty("enforcesSecureChat", this.info.isEnforcesSecureChat());

        helper.writeString(out, obj.toString());
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
