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
import lombok.With;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ClientboundStatusResponsePacket implements MinecraftPacket {
    private static final String FAVICON_PREFIX = "data:image/png;base64,";
    private static final int FAVICON_PREFIX_LENGTH = FAVICON_PREFIX.length();
    private final @NotNull ServerStatusInfo info;

    public ClientboundStatusResponsePacket(ByteBuf in, MinecraftCodecHelper helper) {
        JsonObject obj = new Gson().fromJson(helper.readString(in), JsonObject.class);

        JsonElement descriptionElement = obj.get("description");
        Component description = null;
        if (descriptionElement != null) {
            description = DefaultComponentSerializer.get().serializer().fromJson(descriptionElement, Component.class);
        }

        JsonElement playersElement = obj.get("players");
        PlayerInfo playerInfo = null;
        if (playersElement != null) {
            JsonObject playersObject = playersElement.getAsJsonObject();

            JsonElement sampleElement = playersObject.get("sample");
            List<GameProfile> sampleProfiles = null;
            if (sampleElement != null) {
                JsonArray sampleArray = sampleElement.getAsJsonArray();
                sampleProfiles = new ArrayList<>(sampleArray.size());
                for (JsonElement sampleEntryElement : sampleArray) {
                    JsonObject sampleEntryObject = sampleEntryElement.getAsJsonObject();
                    sampleProfiles.add(new GameProfile(
                            sampleEntryObject.get("id").getAsString(),
                            sampleEntryObject.get("name").getAsString()
                    ));
                }
            }

            playerInfo = new PlayerInfo(
                    playersObject.get("max").getAsInt(),
                    playersObject.get("online").getAsInt(),
                    sampleProfiles
            );
        }

        JsonElement versionElement = obj.get("version");
        VersionInfo versionInfo = null;
        if (versionElement != null) {
            JsonObject versionObject = versionElement.getAsJsonObject();
            versionInfo = new VersionInfo(
                    versionObject.get("name").getAsString(),
                    versionObject.get("protocol").getAsInt()
            );
        }

        JsonElement iconElement = obj.get("favicon");
        byte[] icon = null;
        if (iconElement != null) {
            icon = this.stringToIcon(iconElement.getAsString());
        }

        JsonElement enforcesSecureChatElement = obj.get("enforcesSecureChat");
        Boolean enforcesSecureChat = null;
        if (enforcesSecureChatElement != null) {
            enforcesSecureChat = enforcesSecureChatElement.getAsBoolean();
        }

        this.info = new ServerStatusInfo(
                description,
                playerInfo,
                versionInfo,
                icon,
                enforcesSecureChat
        );
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        JsonObject obj = new JsonObject();

        Component description = this.info.description();
        if (description != null) {
            obj.add("description", DefaultComponentSerializer.get().serializer().toJsonTree(description));
        }

        PlayerInfo playerInfo = this.info.playerInfo();
        if (playerInfo != null) {
            JsonObject playersObject = new JsonObject();

            playersObject.addProperty("max", playerInfo.maxPlayers());
            playersObject.addProperty("online", playerInfo.onlinePlayers());

            if (playerInfo.players() != null) {
                JsonArray sampleArray = new JsonArray();
                for (GameProfile profile : playerInfo.players()) {
                    JsonObject o = new JsonObject();
                    o.addProperty("name", profile.getName());
                    o.addProperty("id", profile.getIdAsString());
                    sampleArray.add(o);
                }

                playersObject.add("sample", sampleArray);
            }

            obj.add("players", playersObject);
        }

        VersionInfo versionInfo = this.info.versionInfo();
        if (versionInfo != null) {
            JsonObject versionObject = new JsonObject();

            versionObject.addProperty("name", versionInfo.versionName());
            versionObject.addProperty("protocol", versionInfo.protocolVersion());

            obj.add("version", versionObject);
        }

        if (this.info.iconPng() != null) {
            obj.addProperty("favicon", this.iconToString(this.info.iconPng()));
        }

        Boolean enforcesSecureChat = this.info.enforcesSecureChat();
        if (enforcesSecureChat != null) {
            obj.addProperty("enforcesSecureChat", enforcesSecureChat);
        }
        if (this.info.enforcesSecureChat() != null) {
            obj.addProperty("enforcesSecureChat", this.info.enforcesSecureChat());
        }

        helper.writeString(out, obj.toString());
    }

    private byte[] stringToIcon(String str) {
        if (str.startsWith(FAVICON_PREFIX)) {
            str = str.substring(FAVICON_PREFIX_LENGTH);
        }

        return Base64.decode(str.getBytes(StandardCharsets.UTF_8));
    }

    private String iconToString(byte[] icon) {
        return FAVICON_PREFIX + new String(Base64.encode(icon), StandardCharsets.UTF_8);
    }
}
