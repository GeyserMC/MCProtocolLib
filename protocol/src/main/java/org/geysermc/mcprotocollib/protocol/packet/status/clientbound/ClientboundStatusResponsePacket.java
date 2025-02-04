package org.geysermc.mcprotocollib.protocol.packet.status.clientbound;

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
import org.geysermc.mcprotocollib.auth.GameProfile;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftTypes;
import org.geysermc.mcprotocollib.protocol.data.DefaultComponentSerializer;
import org.geysermc.mcprotocollib.protocol.data.status.PlayerInfo;
import org.geysermc.mcprotocollib.protocol.data.status.ServerStatusInfo;
import org.geysermc.mcprotocollib.protocol.data.status.VersionInfo;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ClientboundStatusResponsePacket implements MinecraftPacket {

    // vanilla behavior falls back to false if the field was not sent
    private static final boolean ENFORCES_SECURE_CHAT_DEFAULT = false;

    private final @NonNull JsonObject data;

    public ClientboundStatusResponsePacket(@NonNull ServerStatusInfo info) {
        this(toJson(info));
    }

    public ClientboundStatusResponsePacket(ByteBuf in) {
        data = new Gson().fromJson(MinecraftTypes.readString(in), JsonObject.class);
    }

    @Override
    public void serialize(ByteBuf out) {
        MinecraftTypes.writeString(out, data.toString());
    }

    public ServerStatusInfo parseInfo() {
        JsonElement desc = data.get("description");
        Component description = DefaultComponentSerializer.get().deserializeFromTree(desc);

        PlayerInfo players = null;
        if (data.has("players")) {
            JsonObject plrs = data.get("players").getAsJsonObject();
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

            players = new PlayerInfo(plrs.get("max").getAsInt(), plrs.get("online").getAsInt(), profiles);
        }

        VersionInfo version = null;
        if (data.has("version")) {
            JsonObject ver = data.get("version").getAsJsonObject();
            version = new VersionInfo(ver.get("name").getAsString(), ver.get("protocol").getAsInt());
        }

        byte[] icon = null;
        if (data.has("favicon")) {
            icon = stringToIcon(data.get("favicon").getAsString());
        }

        boolean enforcesSecureChat = ENFORCES_SECURE_CHAT_DEFAULT;
        if (data.has("enforcesSecureChat")) {
            enforcesSecureChat = data.get("enforcesSecureChat").getAsBoolean();
        }

        return new ServerStatusInfo(description, players, version, icon, enforcesSecureChat);
    }

    public ClientboundStatusResponsePacket withInfo(@NonNull ServerStatusInfo info) {
        return withData(toJson(info));
    }

    private static JsonObject toJson(ServerStatusInfo info) {
        JsonObject obj = new JsonObject();

        obj.add("description", DefaultComponentSerializer.get().serializeToTree(info.getDescription()));

        if (info.getPlayerInfo() != null) {
            JsonObject plrs = new JsonObject();
            plrs.addProperty("max", info.getPlayerInfo().getMaxPlayers());
            plrs.addProperty("online", info.getPlayerInfo().getOnlinePlayers());
            if (!info.getPlayerInfo().getPlayers().isEmpty()) {
                JsonArray array = new JsonArray();
                for (GameProfile profile : info.getPlayerInfo().getPlayers()) {
                    JsonObject o = new JsonObject();
                    o.addProperty("name", profile.getName());
                    o.addProperty("id", profile.getIdAsString());
                    array.add(o);
                }

                plrs.add("sample", array);
            }
            obj.add("players", plrs);
        }

        if (info.getVersionInfo() != null) {
            JsonObject ver = new JsonObject();
            ver.addProperty("name", info.getVersionInfo().getVersionName());
            ver.addProperty("protocol", info.getVersionInfo().getProtocolVersion());
            obj.add("version", ver);
        }

        if (info.getIconPng() != null) {
            obj.addProperty("favicon", iconToString(info.getIconPng()));
        }

        obj.addProperty("enforcesSecureChat", info.isEnforcesSecureChat());

        return obj;
    }

    public static byte[] stringToIcon(String str) {
        if (str.startsWith("data:image/png;base64,")) {
            str = str.substring("data:image/png;base64,".length());
        }

        return Base64.getDecoder().decode(str.getBytes(StandardCharsets.UTF_8));
    }

    public static String iconToString(byte[] icon) {
        return "data:image/png;base64," + new String(Base64.getEncoder().encode(icon), StandardCharsets.UTF_8);
    }
}
