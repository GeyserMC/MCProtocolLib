package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntry;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntryAction;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import io.netty.buffer.ByteBuf;
import net.kyori.adventure.text.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class ClientboundPlayerInfoUpdatePacket implements MinecraftPacket {
    private final EnumSet<PlayerListEntryAction> actions;
    private final PlayerListEntry[] entries;

    public ClientboundPlayerInfoUpdatePacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.actions = helper.readEnumSet(in, PlayerListEntryAction.class);
        this.entries = new PlayerListEntry[helper.readVarInt(in)];
        for (int count = 0; count < this.entries.length; count++) {
            for (PlayerListEntryAction action : this.actions) {
                UUID profileId = helper.readUUID(in);
                GameProfile profile;
                if (action == PlayerListEntryAction.ADD_PLAYER) {
                    profile = new GameProfile(profileId, helper.readString(in, 16));
                } else {
                    profile = new GameProfile(profileId, null);
                }

                PlayerListEntry entry = null;
                switch (action) {
                    case ADD_PLAYER: {
                        int properties = helper.readVarInt(in);
                        List<GameProfile.Property> propertyList = new ArrayList<>();
                        for (int index = 0; index < properties; index++) {
                            propertyList.add(helper.readProperty(in));
                        }

                        profile.setProperties(propertyList);

                        entry = new PlayerListEntry(profileId, profile);
                        break;
                    }
                    case INITIALIZE_CHAT: {
                        UUID sessionId = helper.readUUID(in);
                        if (in.readBoolean()) {
                            long expiresAt = in.readLong();
                            byte[] keyBytes = helper.readByteArray(in);
                            byte[] keySignature = helper.readByteArray(in);

                            PublicKey publicKey = null;
                            try {
                                publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
                            } catch (GeneralSecurityException e) {
                                throw new IOException("Could not decode public key.", e);
                            }

                            entry = new PlayerListEntry(profileId, profile, sessionId, expiresAt, publicKey, keySignature);
                        } else {
                            entry = new PlayerListEntry(profileId, profile, sessionId, 0L, null, null);
                        }
                        break;
                    }
                    case UPDATE_GAME_MODE: {
                        int rawGameMode = helper.readVarInt(in);
                        GameMode gameMode = MagicValues.key(GameMode.class, Math.max(rawGameMode, 0));

                        entry = new PlayerListEntry(profileId, profile, gameMode);
                        break;
                    }
                    case UPDATE_LISTED: {
                        boolean listed = in.readBoolean();

                        entry = new PlayerListEntry(profileId, profile, listed);
                        break;
                    }
                    case UPDATE_LATENCY: {
                        int latency = helper.readVarInt(in);

                        entry = new PlayerListEntry(profileId, profile, latency);
                        break;
                    }
                    case UPDATE_DISPLAY_NAME: {
                        Component displayName = helper.readNullable(in, helper::readComponent);

                        entry = new PlayerListEntry(profileId, profile, displayName);
                        break;
                    }
                }

                this.entries[count] = entry;
            }
        }
    }

    public void serialize(ByteBuf out, MinecraftCodecHelper helper) throws IOException {
        helper.writeEnumSet(out, this.actions, PlayerListEntryAction.class);
        helper.writeVarInt(out, this.entries.length);
        for (PlayerListEntry entry : this.entries) {
            helper.writeUUID(out, entry.getProfile().getId());
            for (PlayerListEntryAction action : this.actions) {
                switch (action) {
                    case ADD_PLAYER:
                        helper.writeString(out, entry.getProfile().getName());
                        for (GameProfile.Property property : entry.getProfile().getProperties()) {
                            helper.writeProperty(out, property);
                        }
                        break;
                    case INITIALIZE_CHAT:
                        helper.writeUUID(out, entry.getSessionId());

                        out.writeBoolean(entry.getPublicKey() != null);
                        if (entry.getPublicKey() != null) {
                            out.writeLong(entry.getExpiresAt());
                            helper.writeByteArray(out, entry.getPublicKey().getEncoded());
                            helper.writeByteArray(out, entry.getKeySignature());
                        }
                        break;
                    case UPDATE_GAME_MODE:
                        helper.writeVarInt(out, MagicValues.value(Integer.class, entry.getGameMode()));
                        break;
                    case UPDATE_LISTED:
                        out.writeBoolean(entry.isListed());
                        break;
                    case UPDATE_LATENCY:
                        helper.writeVarInt(out, entry.getLatency());
                        break;
                    case UPDATE_DISPLAY_NAME:
                        helper.writeNullable(out, entry.getDisplayName(), helper::writeComponent);
                        break;
                }
            }
        }
    }
}
