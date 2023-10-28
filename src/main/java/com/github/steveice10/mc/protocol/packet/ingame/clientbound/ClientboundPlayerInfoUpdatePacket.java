package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntry;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntryAction;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Data
@With
@AllArgsConstructor
public class ClientboundPlayerInfoUpdatePacket implements MinecraftPacket {
    private final EnumSet<PlayerListEntryAction> actions;
    private final PlayerListEntry[] entries;

    public ClientboundPlayerInfoUpdatePacket(ByteBuf in, MinecraftCodecHelper helper) throws IOException {
        this.actions = helper.readEnumSet(in, PlayerListEntryAction.VALUES);
        this.entries = new PlayerListEntry[helper.readVarInt(in)];
        for (int count = 0; count < this.entries.length; count++) {
            PlayerListEntry entry = new PlayerListEntry(helper.readUUID(in));
            for (PlayerListEntryAction action : this.actions) {
                switch (action) {
                    case ADD_PLAYER: {
                        GameProfile profile = new GameProfile(entry.getProfileId(), helper.readString(in, 16));
                        int propertyCount = helper.readVarInt(in);
                        List<GameProfile.Property> propertyList = new ArrayList<>();
                        for (int index = 0; index < propertyCount; index++) {
                            propertyList.add(helper.readProperty(in));
                        }

                        profile.setProperties(propertyList);

                        entry.setProfile(profile);
                        break;
                    }
                    case INITIALIZE_CHAT: {
                        if (in.readBoolean()) {
                            entry.setSessionId(helper.readUUID(in));
                            entry.setExpiresAt(in.readLong());
                            byte[] keyBytes = helper.readByteArray(in);
                            entry.setKeySignature(helper.readByteArray(in));

                            PublicKey publicKey;
                            try {
                                publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
                            } catch (GeneralSecurityException e) {
                                throw new IOException("Could not decode public key.", e);
                            }

                            entry.setPublicKey(publicKey);
                        }
                        break;
                    }
                    case UPDATE_GAME_MODE: {
                        GameMode gameMode = GameMode.byId(helper.readVarInt(in));

                        entry.setGameMode(gameMode);
                        break;
                    }
                    case UPDATE_LISTED: {
                        boolean listed = in.readBoolean();

                        entry.setListed(listed);
                        break;
                    }
                    case UPDATE_LATENCY: {
                        int latency = helper.readVarInt(in);

                        entry.setLatency(latency);
                        break;
                    }
                    case UPDATE_DISPLAY_NAME: {
                        Component displayName = helper.readNullable(in, helper::readComponent);

                        entry.setDisplayName(displayName);
                        break;
                    }
                }
            }

            this.entries[count] = entry;
        }
    }

    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeEnumSet(out, this.actions, PlayerListEntryAction.VALUES);
        helper.writeVarInt(out, this.entries.length);
        for (PlayerListEntry entry : this.entries) {
            helper.writeUUID(out, entry.getProfile().getId());
            for (PlayerListEntryAction action : this.actions) {
                switch (action) {
                    case ADD_PLAYER:
                        helper.writeString(out, entry.getProfile().getName());
                        helper.writeVarInt(out, entry.getProfile().getProperties().size());
                        for (GameProfile.Property property : entry.getProfile().getProperties()) {
                            helper.writeProperty(out, property);
                        }
                        break;
                    case INITIALIZE_CHAT:
                        out.writeBoolean(entry.getPublicKey() != null);
                        if (entry.getPublicKey() != null) {
                            helper.writeUUID(out, entry.getSessionId());
                            out.writeLong(entry.getExpiresAt());
                            helper.writeByteArray(out, entry.getPublicKey().getEncoded());
                            helper.writeByteArray(out, entry.getKeySignature());
                        }
                        break;
                    case UPDATE_GAME_MODE:
                        helper.writeVarInt(out, entry.getGameMode().ordinal());
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
