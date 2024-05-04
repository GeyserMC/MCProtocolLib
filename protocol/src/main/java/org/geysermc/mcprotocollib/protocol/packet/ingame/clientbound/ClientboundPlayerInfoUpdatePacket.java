package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.auth.data.GameProfile;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.PlayerListEntry;
import org.geysermc.mcprotocollib.protocol.data.game.PlayerListEntryAction;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.GameMode;

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

    public ClientboundPlayerInfoUpdatePacket(MinecraftByteBuf buf) {
        this.actions = buf.readEnumSet(PlayerListEntryAction.VALUES);
        this.entries = new PlayerListEntry[buf.readVarInt()];
        for (int count = 0; count < this.entries.length; count++) {
            PlayerListEntry entry = new PlayerListEntry(buf.readUUID());
            for (PlayerListEntryAction action : this.actions) {
                switch (action) {
                    case ADD_PLAYER -> {
                        GameProfile profile = new GameProfile(entry.getProfileId(), buf.readString(16));
                        int propertyCount = buf.readVarInt();
                        List<GameProfile.Property> propertyList = new ArrayList<>();
                        for (int index = 0; index < propertyCount; index++) {
                            propertyList.add(buf.readProperty());
                        }

                        profile.setProperties(propertyList);

                        entry.setProfile(profile);
                    }
                    case INITIALIZE_CHAT -> {
                        if (buf.readBoolean()) {
                            entry.setSessionId(buf.readUUID());
                            entry.setExpiresAt(buf.readLong());
                            byte[] keyBytes = buf.readByteArray();
                            entry.setKeySignature(buf.readByteArray());

                            PublicKey publicKey;
                            try {
                                publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
                            } catch (GeneralSecurityException e) {
                                throw new IllegalStateException("Could not decode public key.", e);
                            }

                            entry.setPublicKey(publicKey);
                        }
                    }
                    case UPDATE_GAME_MODE -> {
                        GameMode gameMode = GameMode.byId(buf.readVarInt());

                        entry.setGameMode(gameMode);
                    }
                    case UPDATE_LISTED -> {
                        boolean listed = buf.readBoolean();

                        entry.setListed(listed);
                    }
                    case UPDATE_LATENCY -> {
                        int latency = buf.readVarInt();

                        entry.setLatency(latency);
                    }
                    case UPDATE_DISPLAY_NAME -> {
                        Component displayName = buf.readNullable(buf::readComponent);

                        entry.setDisplayName(displayName);
                    }
                }
            }

            this.entries[count] = entry;
        }
    }

    public void serialize(MinecraftByteBuf buf) {
        buf.writeEnumSet(this.actions, PlayerListEntryAction.VALUES);
        buf.writeVarInt(this.entries.length);
        for (PlayerListEntry entry : this.entries) {
            buf.writeUUID(entry.getProfileId());
            for (PlayerListEntryAction action : this.actions) {
                switch (action) {
                    case ADD_PLAYER -> {
                        GameProfile profile = entry.getProfile();
                        if (profile == null) {
                            throw new IllegalArgumentException("Cannot ADD " + entry.getProfileId() + " without a profile.");
                        }

                        buf.writeString(profile.getName());
                        buf.writeVarInt(profile.getProperties().size());
                        for (GameProfile.Property property : profile.getProperties()) {
                            buf.writeProperty(property);
                        }
                    }
                    case INITIALIZE_CHAT -> {
                        buf.writeBoolean(entry.getPublicKey() != null);
                        if (entry.getPublicKey() != null) {
                            buf.writeUUID(entry.getSessionId());
                            buf.writeLong(entry.getExpiresAt());
                            buf.writeByteArray(entry.getPublicKey().getEncoded());
                            buf.writeByteArray(entry.getKeySignature());
                        }
                    }
                    case UPDATE_GAME_MODE -> buf.writeVarInt(entry.getGameMode().ordinal());
                    case UPDATE_LISTED -> buf.writeBoolean(entry.isListed());
                    case UPDATE_LATENCY -> buf.writeVarInt(entry.getLatency());
                    case UPDATE_DISPLAY_NAME -> buf.writeNullable(entry.getDisplayName(), buf::writeComponent);
                }
            }
        }
    }
}
