package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntry;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntryAction;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import net.kyori.adventure.text.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@With
@AllArgsConstructor
public class ClientboundPlayerInfoPacket implements MinecraftPacket {
    private final @NonNull PlayerListEntryAction action;
    private final @NonNull PlayerListEntry[] entries;

    public ClientboundPlayerInfoPacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.action = MagicValues.key(PlayerListEntryAction.class, helper.readVarInt(in));
        this.entries = new PlayerListEntry[helper.readVarInt(in)];
        for (int count = 0; count < this.entries.length; count++) {
            UUID uuid = helper.readUUID(in);
            GameProfile profile;
            if (this.action == PlayerListEntryAction.ADD_PLAYER) {
                profile = new GameProfile(uuid, helper.readString(in));
            } else {
                profile = new GameProfile(uuid, null);
            }

            PlayerListEntry entry = null;
            switch (this.action) {
                case ADD_PLAYER: {
                    int properties = helper.readVarInt(in);
                    List<GameProfile.Property> propertyList = new ArrayList<>();
                    for (int index = 0; index < properties; index++) {
                        String propertyName = helper.readString(in);
                        String value = helper.readString(in);
                        String signature = null;
                        if (in.readBoolean()) {
                            signature = helper.readString(in);
                        }

                        propertyList.add(new GameProfile.Property(propertyName, value, signature));
                    }

                    profile.setProperties(propertyList);

                    int rawGameMode = helper.readVarInt(in);
                    GameMode gameMode = MagicValues.key(GameMode.class, Math.max(rawGameMode, 0));
                    int ping = helper.readVarInt(in);
                    Component displayName = null;
                    if (in.readBoolean()) {
                        displayName = helper.readComponent(in);
                    }

                    long expiresAt;
                    PublicKey publicKey = null;
                    byte[] keySignature = null;
                    if (in.readBoolean()) {
                        expiresAt = in.readLong();
                        byte[] keyBytes = helper.readByteArray(in);
                        keySignature = helper.readByteArray(in);

                        try {
                            publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
                        } catch (GeneralSecurityException e) {
                            throw new IOException("Could not decode public key.", e);
                        }
                    } else {
                        expiresAt = 0L;
                    }

                    entry = new PlayerListEntry(profile, gameMode, ping, displayName, expiresAt, publicKey, keySignature);
                    break;
                }
                case UPDATE_GAMEMODE: {
                    int rawGameMode = helper.readVarInt(in);
                    GameMode mode = MagicValues.key(GameMode.class, Math.max(rawGameMode, 0));

                    entry = new PlayerListEntry(profile, mode);
                    break;
                }
                case UPDATE_LATENCY: {
                    int ping = helper.readVarInt(in);

                    entry = new PlayerListEntry(profile, ping);
                    break;
                }
                case UPDATE_DISPLAY_NAME: {
                    Component displayName = null;
                    if (in.readBoolean()) {
                        displayName = helper.readComponent(in);
                    }

                    entry = new PlayerListEntry(profile, displayName);
                    break;
                }
                case REMOVE_PLAYER:
                    entry = new PlayerListEntry(profile);
                    break;
            }

            this.entries[count] = entry;
        }
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeVarInt(out, MagicValues.value(Integer.class, this.action));
        helper.writeVarInt(out, this.entries.length);
        for (PlayerListEntry entry : this.entries) {
            helper.writeUUID(out, entry.getProfile().getId());
            switch (this.action) {
                case ADD_PLAYER:
                    helper.writeString(out, entry.getProfile().getName());
                    helper.writeVarInt(out, entry.getProfile().getProperties().size());
                    for (GameProfile.Property property : entry.getProfile().getProperties()) {
                        helper.writeString(out, property.getName());
                        helper.writeString(out, property.getValue());
                        out.writeBoolean(property.hasSignature());
                        if (property.hasSignature()) {
                            helper.writeString(out, property.getSignature());
                        }
                    }

                    helper.writeVarInt(out, MagicValues.value(Integer.class, entry.getGameMode()));
                    helper.writeVarInt(out, entry.getPing());
                    out.writeBoolean(entry.getDisplayName() != null);
                    if (entry.getDisplayName() != null) {
                        helper.writeString(out, DefaultComponentSerializer.get().serialize(entry.getDisplayName()));
                    }

                    if (entry.getPublicKey() != null) {
                        out.writeLong(entry.getExpiresAt());
                        byte[] encoded = entry.getPublicKey().getEncoded();
                        helper.writeVarInt(out, encoded.length);
                        out.writeBytes(encoded);
                        helper.writeVarInt(out, entry.getKeySignature().length);
                        out.writeBytes(entry.getKeySignature());
                    }

                    break;
                case UPDATE_GAMEMODE:
                    helper.writeVarInt(out, MagicValues.value(Integer.class, entry.getGameMode()));
                    break;
                case UPDATE_LATENCY:
                    helper.writeVarInt(out, entry.getPing());
                    break;
                case UPDATE_DISPLAY_NAME:
                    out.writeBoolean(entry.getDisplayName() != null);
                    if (entry.getDisplayName() != null) {
                        helper.writeString(out, DefaultComponentSerializer.get().serialize(entry.getDisplayName()));
                    }

                    break;
                case REMOVE_PLAYER:
                    break;
            }
        }
    }
}
