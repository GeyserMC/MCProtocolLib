package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.auth.data.GameProfile;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftCodecHelper;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.PlayerListEntry;
import org.geysermc.mcprotocollib.protocol.data.game.PlayerListEntryAction;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.GameMode;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import net.kyori.adventure.text.Component;

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

    public ClientboundPlayerInfoUpdatePacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.actions = helper.readEnumSet(in, PlayerListEntryAction.VALUES);
        this.entries = new PlayerListEntry[helper.readVarInt(in)];
        for (int count = 0; count < this.entries.length; count++) {
            PlayerListEntry entry = new PlayerListEntry(helper.readUUID(in));
            for (PlayerListEntryAction action : this.actions) {
                switch (action) {
                    case ADD_PLAYER -> {
                        GameProfile profile = new GameProfile(entry.getProfileId(), helper.readString(in, 16));
                        int propertyCount = helper.readVarInt(in);
                        List<GameProfile.Property> propertyList = new ArrayList<>();
                        for (int index = 0; index < propertyCount; index++) {
                            propertyList.add(helper.readProperty(in));
                        }

                        profile.setProperties(propertyList);

                        entry.setProfile(profile);
                    }
                    case INITIALIZE_CHAT -> {
                        if (in.readBoolean()) {
                            entry.setSessionId(helper.readUUID(in));
                            entry.setExpiresAt(in.readLong());
                            byte[] keyBytes = helper.readByteArray(in);
                            entry.setKeySignature(helper.readByteArray(in));

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
                        GameMode gameMode = GameMode.byId(helper.readVarInt(in));

                        entry.setGameMode(gameMode);
                    }
                    case UPDATE_LISTED -> {
                        boolean listed = in.readBoolean();

                        entry.setListed(listed);
                    }
                    case UPDATE_LATENCY -> {
                        int latency = helper.readVarInt(in);

                        entry.setLatency(latency);
                    }
                    case UPDATE_DISPLAY_NAME -> {
                        Component displayName = helper.readNullable(in, helper::readComponent);

                        entry.setDisplayName(displayName);
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
            helper.writeUUID(out, entry.getProfileId());
            for (PlayerListEntryAction action : this.actions) {
                switch (action) {
                    case ADD_PLAYER -> {
                        GameProfile profile = entry.getProfile();
                        if (profile == null) {
                            throw new IllegalArgumentException("Cannot ADD " + entry.getProfileId() + " without a profile.");
                        }

                        helper.writeString(out, profile.getName());
                        helper.writeVarInt(out, profile.getProperties().size());
                        for (GameProfile.Property property : profile.getProperties()) {
                            helper.writeProperty(out, property);
                        }
                    }
                    case INITIALIZE_CHAT -> {
                        out.writeBoolean(entry.getPublicKey() != null);
                        if (entry.getPublicKey() != null) {
                            helper.writeUUID(out, entry.getSessionId());
                            out.writeLong(entry.getExpiresAt());
                            helper.writeByteArray(out, entry.getPublicKey().getEncoded());
                            helper.writeByteArray(out, entry.getKeySignature());
                        }
                    }
                    case UPDATE_GAME_MODE -> helper.writeVarInt(out, entry.getGameMode().ordinal());
                    case UPDATE_LISTED -> out.writeBoolean(entry.isListed());
                    case UPDATE_LATENCY -> helper.writeVarInt(out, entry.getLatency());
                    case UPDATE_DISPLAY_NAME ->
                            helper.writeNullable(out, entry.getDisplayName(), helper::writeComponent);
                }
            }
        }
    }
}
