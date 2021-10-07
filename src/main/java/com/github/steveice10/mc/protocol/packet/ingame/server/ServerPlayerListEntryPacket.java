package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.data.DefaultComponentSerializer;
import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntry;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntryAction;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.With;
import net.kyori.adventure.text.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerPlayerListEntryPacket implements Packet {
    private @NonNull PlayerListEntryAction action;
    private @NonNull PlayerListEntry[] entries;

    @Override
    public void read(NetInput in) throws IOException {
        this.action = MagicValues.key(PlayerListEntryAction.class, in.readVarInt());
        this.entries = new PlayerListEntry[in.readVarInt()];
        for (int count = 0; count < this.entries.length; count++) {
            UUID uuid = in.readUUID();
            GameProfile profile;
            if (this.action == PlayerListEntryAction.ADD_PLAYER) {
                profile = new GameProfile(uuid, in.readString());
            } else {
                profile = new GameProfile(uuid, null);
            }

            PlayerListEntry entry = null;
            switch (this.action) {
                case ADD_PLAYER: {
                    int properties = in.readVarInt();
                    List<GameProfile.Property> propertyList = new ArrayList<>();
                    for (int index = 0; index < properties; index++) {
                        String propertyName = in.readString();
                        String value = in.readString();
                        String signature = null;
                        if (in.readBoolean()) {
                            signature = in.readString();
                        }

                        propertyList.add(new GameProfile.Property(propertyName, value, signature));
                    }

                    profile.setProperties(propertyList);

                    int rawGameMode = in.readVarInt();
                    GameMode gameMode = MagicValues.key(GameMode.class, Math.max(rawGameMode, 0));
                    int ping = in.readVarInt();
                    Component displayName = null;
                    if (in.readBoolean()) {
                        displayName = DefaultComponentSerializer.get().deserialize(in.readString());
                    }

                    entry = new PlayerListEntry(profile, gameMode, ping, displayName);
                    break;
                }
                case UPDATE_GAMEMODE: {
                    int rawGameMode = in.readVarInt();
                    GameMode mode = MagicValues.key(GameMode.class, Math.max(rawGameMode, 0));

                    entry = new PlayerListEntry(profile, mode);
                    break;
                }
                case UPDATE_LATENCY: {
                    int ping = in.readVarInt();

                    entry = new PlayerListEntry(profile, ping);
                    break;
                }
                case UPDATE_DISPLAY_NAME: {
                    Component displayName = null;
                    if (in.readBoolean()) {
                        displayName = DefaultComponentSerializer.get().deserialize(in.readString());
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
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(MagicValues.value(Integer.class, this.action));
        out.writeVarInt(this.entries.length);
        for (PlayerListEntry entry : this.entries) {
            out.writeUUID(entry.getProfile().getId());
            switch (this.action) {
                case ADD_PLAYER:
                    out.writeString(entry.getProfile().getName());
                    out.writeVarInt(entry.getProfile().getProperties().size());
                    for (GameProfile.Property property : entry.getProfile().getProperties()) {
                        out.writeString(property.getName());
                        out.writeString(property.getValue());
                        out.writeBoolean(property.hasSignature());
                        if (property.hasSignature()) {
                            out.writeString(property.getSignature());
                        }
                    }

                    out.writeVarInt(MagicValues.value(Integer.class, entry.getGameMode()));
                    out.writeVarInt(entry.getPing());
                    out.writeBoolean(entry.getDisplayName() != null);
                    if (entry.getDisplayName() != null) {
                        out.writeString(DefaultComponentSerializer.get().serialize(entry.getDisplayName()));
                    }

                    break;
                case UPDATE_GAMEMODE:
                    out.writeVarInt(MagicValues.value(Integer.class, entry.getGameMode()));
                    break;
                case UPDATE_LATENCY:
                    out.writeVarInt(entry.getPing());
                    break;
                case UPDATE_DISPLAY_NAME:
                    out.writeBoolean(entry.getDisplayName() != null);
                    if (entry.getDisplayName() != null) {
                        out.writeString(DefaultComponentSerializer.get().serialize(entry.getDisplayName()));
                    }

                    break;
                case REMOVE_PLAYER:
                    break;
            }
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
