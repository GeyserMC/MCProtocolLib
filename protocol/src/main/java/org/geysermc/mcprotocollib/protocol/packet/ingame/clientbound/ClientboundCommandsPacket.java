package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import org.geysermc.mcprotocollib.protocol.codec.MinecraftByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.With;
import org.geysermc.mcprotocollib.protocol.codec.MinecraftPacket;
import org.geysermc.mcprotocollib.protocol.data.game.command.CommandNode;
import org.geysermc.mcprotocollib.protocol.data.game.command.CommandParser;
import org.geysermc.mcprotocollib.protocol.data.game.command.CommandType;
import org.geysermc.mcprotocollib.protocol.data.game.command.properties.CommandProperties;
import org.geysermc.mcprotocollib.protocol.data.game.command.properties.DoubleProperties;
import org.geysermc.mcprotocollib.protocol.data.game.command.properties.EntityProperties;
import org.geysermc.mcprotocollib.protocol.data.game.command.properties.FloatProperties;
import org.geysermc.mcprotocollib.protocol.data.game.command.properties.IntegerProperties;
import org.geysermc.mcprotocollib.protocol.data.game.command.properties.LongProperties;
import org.geysermc.mcprotocollib.protocol.data.game.command.properties.ResourceProperties;
import org.geysermc.mcprotocollib.protocol.data.game.command.properties.ScoreHolderProperties;
import org.geysermc.mcprotocollib.protocol.data.game.command.properties.StringProperties;
import org.geysermc.mcprotocollib.protocol.data.game.command.properties.TimeProperties;

import java.util.OptionalInt;

@Data
@With
@AllArgsConstructor
public class ClientboundCommandsPacket implements MinecraftPacket {
    private static final int FLAG_TYPE_MASK = 0x03;
    private static final int FLAG_EXECUTABLE = 0x04;
    private static final int FLAG_REDIRECT = 0x08;
    private static final int FLAG_SUGGESTION_TYPE = 0x10;

    private static final int NUMBER_FLAG_MIN_DEFINED = 0x01;
    private static final int NUMBER_FLAG_MAX_DEFINED = 0x02;

    private static final int ENTITY_FLAG_SINGLE_TARGET = 0x01;
    private static final int ENTITY_FLAG_PLAYERS_ONLY = 0x02;

    private final @NonNull CommandNode[] nodes;
    private final int firstNodeIndex;

    public ClientboundCommandsPacket(MinecraftByteBuf buf) {
        this.nodes = new CommandNode[buf.readVarInt()];
        for (int i = 0; i < this.nodes.length; i++) {
            byte flags = buf.readByte();
            CommandType type = CommandType.from(flags & FLAG_TYPE_MASK);
            boolean executable = (flags & FLAG_EXECUTABLE) != 0;

            int[] children = new int[buf.readVarInt()];
            for (int j = 0; j < children.length; j++) {
                children[j] = buf.readVarInt();
            }

            OptionalInt redirectIndex;
            if ((flags & FLAG_REDIRECT) != 0) {
                redirectIndex = OptionalInt.of(buf.readVarInt());
            } else {
                redirectIndex = OptionalInt.empty();
            }

            String name = null;
            if (type == CommandType.LITERAL || type == CommandType.ARGUMENT) {
                name = buf.readString();
            }

            CommandParser parser = null;
            CommandProperties properties = null;
            String suggestionType = null;
            if (type == CommandType.ARGUMENT) {
                parser = CommandParser.from(buf.readVarInt());
                switch (parser) {
                    case DOUBLE -> {
                        byte numberFlags = buf.readByte();
                        double min = -Double.MAX_VALUE;
                        double max = Double.MAX_VALUE;
                        if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
                            min = buf.readDouble();
                        }

                        if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
                            max = buf.readDouble();
                        }

                        properties = new DoubleProperties(min, max);
                    }
                    case FLOAT -> {
                        byte numberFlags = buf.readByte();
                        float min = -Float.MAX_VALUE;
                        float max = Float.MAX_VALUE;
                        if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
                            min = buf.readFloat();
                        }

                        if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
                            max = buf.readFloat();
                        }

                        properties = new FloatProperties(min, max);
                    }
                    case INTEGER -> {
                        byte numberFlags = buf.readByte();
                        int min = Integer.MIN_VALUE;
                        int max = Integer.MAX_VALUE;
                        if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
                            min = buf.readInt();
                        }

                        if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
                            max = buf.readInt();
                        }

                        properties = new IntegerProperties(min, max);
                    }
                    case LONG -> {
                        byte numberFlags = buf.readByte();
                        long min = Long.MIN_VALUE;
                        long max = Long.MAX_VALUE;
                        if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
                            min = buf.readLong();
                        }

                        if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
                            max = buf.readLong();
                        }

                        properties = new LongProperties(min, max);
                    }
                    case STRING -> properties = StringProperties.from(buf.readVarInt());
                    case ENTITY -> {
                        byte entityFlags = buf.readByte();
                        properties = new EntityProperties((entityFlags & ENTITY_FLAG_SINGLE_TARGET) != 0,
                                (entityFlags & ENTITY_FLAG_PLAYERS_ONLY) != 0);
                    }
                    case SCORE_HOLDER -> properties = new ScoreHolderProperties(buf.readBoolean());
                    case TIME -> properties = new TimeProperties(buf.readInt());
                    case RESOURCE_OR_TAG, RESOURCE_OR_TAG_KEY, RESOURCE, RESOURCE_KEY -> properties = new ResourceProperties(buf.readString());
                    default -> {
                    }
                }

                if ((flags & FLAG_SUGGESTION_TYPE) != 0) {
                    suggestionType = buf.readResourceLocation();
                }
            }

            this.nodes[i] = new CommandNode(type, executable, children, redirectIndex, name, parser, properties, suggestionType);
        }

        this.firstNodeIndex = buf.readVarInt();
    }

    @Override
    public void serialize(MinecraftByteBuf buf) {
        buf.writeVarInt(this.nodes.length);
        for (CommandNode node : this.nodes) {
            int flags = node.getType().ordinal() & FLAG_TYPE_MASK;
            if (node.isExecutable()) {
                flags |= FLAG_EXECUTABLE;
            }

            if (node.getRedirectIndex().isPresent()) {
                flags |= FLAG_REDIRECT;
            }

            if (node.getSuggestionType() != null) {
                flags |= FLAG_SUGGESTION_TYPE;
            }

            buf.writeByte(flags);

            buf.writeVarInt(node.getChildIndices().length);
            for (int childIndex : node.getChildIndices()) {
                buf.writeVarInt(childIndex);
            }

            if (node.getRedirectIndex().isPresent()) {
                buf.writeVarInt(node.getRedirectIndex().getAsInt());
            }

            if (node.getType() == CommandType.LITERAL || node.getType() == CommandType.ARGUMENT) {
                buf.writeString(node.getName());
            }

            if (node.getType() == CommandType.ARGUMENT) {
                buf.writeVarInt(node.getParser().ordinal());
                switch (node.getParser()) {
                    case DOUBLE -> {
                        DoubleProperties properties = (DoubleProperties) node.getProperties();

                        int numberFlags = 0;
                        if (properties.getMin() != -Double.MAX_VALUE) {
                            numberFlags |= NUMBER_FLAG_MIN_DEFINED;
                        }

                        if (properties.getMax() != Double.MAX_VALUE) {
                            numberFlags |= NUMBER_FLAG_MAX_DEFINED;
                        }

                        buf.writeByte(numberFlags);
                        if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
                            buf.writeDouble(properties.getMin());
                        }

                        if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
                            buf.writeDouble(properties.getMax());
                        }
                    }
                    case FLOAT -> {
                        FloatProperties properties = (FloatProperties) node.getProperties();

                        int numberFlags = 0;
                        if (properties.getMin() != -Float.MAX_VALUE) {
                            numberFlags |= NUMBER_FLAG_MIN_DEFINED;
                        }

                        if (properties.getMax() != Float.MAX_VALUE) {
                            numberFlags |= NUMBER_FLAG_MAX_DEFINED;
                        }

                        buf.writeByte(numberFlags);
                        if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
                            buf.writeFloat(properties.getMin());
                        }

                        if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
                            buf.writeFloat(properties.getMax());
                        }
                    }
                    case INTEGER -> {
                        IntegerProperties properties = (IntegerProperties) node.getProperties();

                        int numberFlags = 0;
                        if (properties.getMin() != Integer.MIN_VALUE) {
                            numberFlags |= NUMBER_FLAG_MIN_DEFINED;
                        }

                        if (properties.getMax() != Integer.MAX_VALUE) {
                            numberFlags |= NUMBER_FLAG_MAX_DEFINED;
                        }

                        buf.writeByte(numberFlags);
                        if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
                            buf.writeInt(properties.getMin());
                        }

                        if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
                            buf.writeInt(properties.getMax());
                        }
                    }
                    case LONG -> {
                        LongProperties properties = (LongProperties) node.getProperties();

                        int numberFlags = 0;
                        if (properties.getMin() != Long.MIN_VALUE) {
                            numberFlags |= NUMBER_FLAG_MIN_DEFINED;
                        }

                        if (properties.getMax() != Long.MAX_VALUE) {
                            numberFlags |= NUMBER_FLAG_MAX_DEFINED;
                        }

                        buf.writeByte(numberFlags);
                        if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
                            buf.writeLong(properties.getMin());
                        }

                        if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
                            buf.writeLong(properties.getMax());
                        }
                    }
                    case STRING -> buf.writeVarInt(((StringProperties) node.getProperties()).ordinal());
                    case ENTITY -> {
                        EntityProperties properties = (EntityProperties) node.getProperties();
                        int entityFlags = 0;
                        if (properties.isSingleTarget()) {
                            entityFlags |= ENTITY_FLAG_SINGLE_TARGET;
                        }

                        if (properties.isPlayersOnly()) {
                            entityFlags |= ENTITY_FLAG_PLAYERS_ONLY;
                        }

                        buf.writeByte(entityFlags);
                    }
                    case SCORE_HOLDER -> buf.writeBoolean(((ScoreHolderProperties) node.getProperties()).isAllowMultiple());
                    case TIME -> buf.writeInt(((TimeProperties) node.getProperties()).getMin());
                    case RESOURCE_OR_TAG, RESOURCE_OR_TAG_KEY, RESOURCE, RESOURCE_KEY -> buf.writeString(((ResourceProperties) node.getProperties()).getRegistryKey());
                    default -> {
                    }
                }

                if (node.getSuggestionType() != null) {
                    buf.writeResourceLocation(node.getSuggestionType());
                }
            }
        }

        buf.writeVarInt(this.firstNodeIndex);
    }
}
