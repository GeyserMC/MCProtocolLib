package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.Identifier;
import com.github.steveice10.mc.protocol.data.game.command.CommandNode;
import com.github.steveice10.mc.protocol.data.game.command.CommandParser;
import com.github.steveice10.mc.protocol.data.game.command.CommandType;
import com.github.steveice10.mc.protocol.data.game.command.SuggestionType;
import com.github.steveice10.mc.protocol.data.game.command.properties.CommandProperties;
import com.github.steveice10.mc.protocol.data.game.command.properties.DoubleProperties;
import com.github.steveice10.mc.protocol.data.game.command.properties.EntityProperties;
import com.github.steveice10.mc.protocol.data.game.command.properties.FloatProperties;
import com.github.steveice10.mc.protocol.data.game.command.properties.IntegerProperties;
import com.github.steveice10.mc.protocol.data.game.command.properties.LongProperties;
import com.github.steveice10.mc.protocol.data.game.command.properties.RangeProperties;
import com.github.steveice10.mc.protocol.data.game.command.properties.ScoreHolderProperties;
import com.github.steveice10.mc.protocol.data.game.command.properties.StringProperties;
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

import java.io.IOException;

@Data
@With
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ServerDeclareCommandsPacket implements Packet {
    private static final int FLAG_TYPE_MASK = 0x03;
    private static final int FLAG_EXECUTABLE = 0x04;
    private static final int FLAG_REDIRECT = 0x08;
    private static final int FLAG_SUGGESTION_TYPE = 0x10;

    private static final int NUMBER_FLAG_MIN_DEFINED = 0x01;
    private static final int NUMBER_FLAG_MAX_DEFINED = 0x02;

    private static final int ENTITY_FLAG_SINGLE_TARGET = 0x01;
    private static final int ENTITY_FLAG_PLAYERS_ONLY = 0x02;

    private @NonNull CommandNode[] nodes;
    private int firstNodeIndex;

    @Override
    public void read(NetInput in) throws IOException {
        this.nodes = new CommandNode[in.readVarInt()];
        for (int i = 0; i < this.nodes.length; i++) {
            byte flags = in.readByte();
            CommandType type = MagicValues.key(CommandType.class, flags & FLAG_TYPE_MASK);
            boolean executable = (flags & FLAG_EXECUTABLE) != 0;

            int[] children = new int[in.readVarInt()];
            for (int j = 0; j < children.length; j++) {
                children[j] = in.readVarInt();
            }

            int redirectIndex = -1;
            if ((flags & FLAG_REDIRECT) != 0) {
                redirectIndex = in.readVarInt();
            }

            String name = null;
            if (type == CommandType.LITERAL || type == CommandType.ARGUMENT) {
                name = in.readString();
            }

            CommandParser parser = null;
            CommandProperties properties = null;
            if (type == CommandType.ARGUMENT) {
                String identifier = Identifier.formalize(in.readString());
                if (identifier.equals("minecraft:")) continue;
                parser = MagicValues.key(CommandParser.class, identifier);
                switch (parser) {
                    case DOUBLE: {
                        byte numberFlags = in.readByte();
                        double min = -Double.MAX_VALUE;
                        double max = Double.MAX_VALUE;
                        if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
                            min = in.readDouble();
                        }

                        if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
                            max = in.readDouble();
                        }

                        properties = new DoubleProperties(min, max);
                        break;
                    }
                    case FLOAT: {
                        byte numberFlags = in.readByte();
                        float min = -Float.MAX_VALUE;
                        float max = Float.MAX_VALUE;
                        if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
                            min = in.readFloat();
                        }

                        if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
                            max = in.readFloat();
                        }

                        properties = new FloatProperties(min, max);
                        break;
                    }
                    case INTEGER: {
                        byte numberFlags = in.readByte();
                        int min = Integer.MIN_VALUE;
                        int max = Integer.MAX_VALUE;
                        if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
                            min = in.readInt();
                        }

                        if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
                            max = in.readInt();
                        }

                        properties = new IntegerProperties(min, max);
                        break;
                    }
                    case LONG: {
                        byte numberFlags = in.readByte();
                        long min;
                        long max;
                        if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
                            min = in.readLong();
                        } else {
                            min = Long.MIN_VALUE;
                        }

                        if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
                            max = in.readLong();
                        } else {
                            max = Long.MAX_VALUE;
                        }

                        properties = new LongProperties(min, max);
                        break;
                    }
                    case STRING:
                        properties = MagicValues.key(StringProperties.class, in.readVarInt());
                        break;
                    case ENTITY: {
                        byte entityFlags = in.readByte();
                        properties = new EntityProperties((entityFlags & ENTITY_FLAG_SINGLE_TARGET) != 0,
                                (entityFlags & ENTITY_FLAG_PLAYERS_ONLY) != 0);
                        break;
                    }
                    case SCORE_HOLDER:
                        properties = new ScoreHolderProperties(in.readBoolean());
                        break;
                    case RANGE:
                        properties = new RangeProperties(in.readBoolean());
                        break;
                    default:
                        break;
                }
            }

            SuggestionType suggestionType = null;
            if ((flags & FLAG_SUGGESTION_TYPE) != 0) {
                suggestionType = MagicValues.key(SuggestionType.class, Identifier.formalize(in.readString()));
            }

            this.nodes[i] = new CommandNode(type, executable, children, redirectIndex, name, parser, properties, suggestionType);
        }

        this.firstNodeIndex = in.readVarInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.nodes.length);
        for (CommandNode node : this.nodes) {
            int flags = MagicValues.value(Integer.class, node.getType()) & FLAG_TYPE_MASK;
            if (node.isExecutable()) {
                flags |= FLAG_EXECUTABLE;
            }

            if (node.getRedirectIndex() != -1) {
                flags |= FLAG_REDIRECT;
            }

            if (node.getSuggestionType() != null) {
                flags |= FLAG_SUGGESTION_TYPE;
            }

            out.writeByte(flags);

            out.writeVarInt(node.getChildIndices().length);
            for (int childIndex : node.getChildIndices()) {
                out.writeVarInt(childIndex);
            }

            if (node.getRedirectIndex() != -1) {
                out.writeVarInt(node.getRedirectIndex());
            }

            if (node.getType() == CommandType.LITERAL || node.getType() == CommandType.ARGUMENT) {
                out.writeString(node.getName());
            }

            if (node.getType() == CommandType.ARGUMENT) {
                out.writeString(MagicValues.value(String.class, node.getParser()));
                switch (node.getParser()) {
                    case DOUBLE: {
                        DoubleProperties properties = (DoubleProperties) node.getProperties();

                        int numberFlags = 0;
                        if (properties.getMin() != -Double.MAX_VALUE) {
                            numberFlags |= NUMBER_FLAG_MIN_DEFINED;
                        }

                        if (properties.getMax() != Double.MAX_VALUE) {
                            numberFlags |= NUMBER_FLAG_MAX_DEFINED;
                        }

                        out.writeByte(numberFlags);
                        if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
                            out.writeDouble(properties.getMin());
                        }

                        if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
                            out.writeDouble(properties.getMax());
                        }

                        break;
                    }
                    case FLOAT: {
                        FloatProperties properties = (FloatProperties) node.getProperties();

                        int numberFlags = 0;
                        if (properties.getMin() != -Float.MAX_VALUE) {
                            numberFlags |= NUMBER_FLAG_MIN_DEFINED;
                        }

                        if (properties.getMax() != Float.MAX_VALUE) {
                            numberFlags |= NUMBER_FLAG_MAX_DEFINED;
                        }

                        out.writeByte(numberFlags);
                        if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
                            out.writeFloat(properties.getMin());
                        }

                        if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
                            out.writeFloat(properties.getMax());
                        }

                        break;
                    }
                    case INTEGER: {
                        IntegerProperties properties = (IntegerProperties) node.getProperties();

                        int numberFlags = 0;
                        if (properties.getMin() != Integer.MIN_VALUE) {
                            numberFlags |= NUMBER_FLAG_MIN_DEFINED;
                        }

                        if (properties.getMax() != Integer.MAX_VALUE) {
                            numberFlags |= NUMBER_FLAG_MAX_DEFINED;
                        }

                        out.writeByte(numberFlags);
                        if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
                            out.writeInt(properties.getMin());
                        }

                        if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
                            out.writeInt(properties.getMax());
                        }

                        break;
                    }
                    case LONG: {
                        LongProperties properties = (LongProperties) node.getProperties();

                        int numberFlags = 0;
                        if (properties.getMin() != Long.MIN_VALUE) {
                            numberFlags |= NUMBER_FLAG_MIN_DEFINED;
                        }

                        if (properties.getMax() != Long.MAX_VALUE) {
                            numberFlags |= NUMBER_FLAG_MAX_DEFINED;
                        }

                        out.writeByte(numberFlags);
                        if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
                            out.writeLong(properties.getMin());
                        }

                        if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
                            out.writeLong(properties.getMax());
                        }

                        break;
                    }
                    case STRING:
                        out.writeVarInt(MagicValues.value(Integer.class, node.getProperties()));
                        break;
                    case ENTITY: {
                        EntityProperties properties = (EntityProperties) node.getProperties();
                        int entityFlags = 0;
                        if (properties.isSingleTarget()) {
                            entityFlags |= ENTITY_FLAG_SINGLE_TARGET;
                        }

                        if (properties.isPlayersOnly()) {
                            entityFlags |= ENTITY_FLAG_PLAYERS_ONLY;
                        }

                        out.writeByte(entityFlags);
                        break;
                    }
                    case SCORE_HOLDER:
                        out.writeBoolean(((ScoreHolderProperties) node.getProperties()).isAllowMultiple());
                        break;
                    case RANGE:
                        out.writeBoolean(((RangeProperties) node.getProperties()).isAllowDecimals());
                        break;
                    default:
                        break;
                }
            }

            if (node.getSuggestionType() != null) {
                out.writeString(MagicValues.value(String.class, node.getSuggestionType()));
            }
        }

        out.writeVarInt(this.firstNodeIndex);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}
