package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.codec.MinecraftCodecHelper;
import com.github.steveice10.mc.protocol.codec.MinecraftPacket;
import com.github.steveice10.mc.protocol.data.game.command.CommandNode;
import com.github.steveice10.mc.protocol.data.game.command.CommandParser;
import com.github.steveice10.mc.protocol.data.game.command.CommandType;
import com.github.steveice10.mc.protocol.data.game.command.properties.*;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.jetbrains.annotations.NotNull;

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

    private final @NotNull CommandNode[] nodes;
    private final int firstNodeIndex;

    public ClientboundCommandsPacket(ByteBuf in, MinecraftCodecHelper helper) {
        this.nodes = new CommandNode[helper.readVarInt(in)];
        for (int i = 0; i < this.nodes.length; i++) {
            byte flags = in.readByte();
            CommandType type = CommandType.from(flags & FLAG_TYPE_MASK);
            boolean executable = (flags & FLAG_EXECUTABLE) != 0;

            int[] children = new int[helper.readVarInt(in)];
            for (int j = 0; j < children.length; j++) {
                children[j] = helper.readVarInt(in);
            }

            Integer redirectIndex;
            if ((flags & FLAG_REDIRECT) != 0) {
                redirectIndex = helper.readVarInt(in);
            } else {
                redirectIndex = null;
            }

            String name = null;
            if (type == CommandType.LITERAL || type == CommandType.ARGUMENT) {
                name = helper.readString(in);
            }

            CommandParser parser = null;
            CommandProperties properties = null;
            String suggestionType = null;
            if (type == CommandType.ARGUMENT) {
                parser = CommandParser.from(helper.readVarInt(in));
                switch (parser) {
                    case DOUBLE -> {
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
                    }
                    case FLOAT -> {
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
                    }
                    case INTEGER -> {
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
                    }
                    case LONG -> {
                        byte numberFlags = in.readByte();
                        long min = Long.MIN_VALUE;
                        long max = Long.MAX_VALUE;
                        if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
                            min = in.readLong();
                        }

                        if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
                            max = in.readLong();
                        }

                        properties = new LongProperties(min, max);
                    }
                    case STRING -> properties = StringProperties.from(helper.readVarInt(in));
                    case ENTITY -> {
                        byte entityFlags = in.readByte();
                        properties = new EntityProperties((entityFlags & ENTITY_FLAG_SINGLE_TARGET) != 0,
                            (entityFlags & ENTITY_FLAG_PLAYERS_ONLY) != 0);
                    }
                    case SCORE_HOLDER -> properties = new ScoreHolderProperties(in.readBoolean());
                    case TIME -> properties = new TimeProperties(in.readInt());
                    case RESOURCE_OR_TAG, RESOURCE_OR_TAG_KEY, RESOURCE, RESOURCE_KEY -> properties = new ResourceProperties(helper.readString(in));
                    default -> {
                    }
                }

                if ((flags & FLAG_SUGGESTION_TYPE) != 0) {
                    suggestionType = helper.readResourceLocation(in);
                }
            }

            this.nodes[i] = new CommandNode(type, executable, children, redirectIndex, name, parser, properties, suggestionType);
        }

        this.firstNodeIndex = helper.readVarInt(in);
    }

    @Override
    public void serialize(ByteBuf out, MinecraftCodecHelper helper) {
        helper.writeVarInt(out, this.nodes.length);
        for (CommandNode node : this.nodes) {
            int flags = node.type().ordinal() & FLAG_TYPE_MASK;
            if (node.executable()) {
                flags |= FLAG_EXECUTABLE;
            }

            if (node.redirectIndex() != null) {
                flags |= FLAG_REDIRECT;
            }

            if (node.suggestionType() != null) {
                flags |= FLAG_SUGGESTION_TYPE;
            }

            out.writeByte(flags);

            helper.writeVarInt(out, node.childIndices().length);
            for (int childIndex : node.childIndices()) {
                helper.writeVarInt(out, childIndex);
            }

            if (node.redirectIndex() != null) {
                helper.writeVarInt(out, node.redirectIndex());
            }

            if (node.type() == CommandType.LITERAL || node.type() == CommandType.ARGUMENT) {
                helper.writeString(out, node.name());
            }

            if (node.type() == CommandType.ARGUMENT) {
                helper.writeVarInt(out, node.parser().ordinal());
                switch (node.parser()) {
                    case DOUBLE -> {
                        DoubleProperties properties = (DoubleProperties) node.properties();

                        int numberFlags = 0;
                        if (properties.min() != -Double.MAX_VALUE) {
                            numberFlags |= NUMBER_FLAG_MIN_DEFINED;
                        }

                        if (properties.max() != Double.MAX_VALUE) {
                            numberFlags |= NUMBER_FLAG_MAX_DEFINED;
                        }

                        out.writeByte(numberFlags);
                        if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
                            out.writeDouble(properties.min());
                        }

                        if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
                            out.writeDouble(properties.max());
                        }

                    }
                    case FLOAT -> {
                        FloatProperties properties = (FloatProperties) node.properties();

                        int numberFlags = 0;
                        if (properties.min() != -Float.MAX_VALUE) {
                            numberFlags |= NUMBER_FLAG_MIN_DEFINED;
                        }

                        if (properties.max() != Float.MAX_VALUE) {
                            numberFlags |= NUMBER_FLAG_MAX_DEFINED;
                        }

                        out.writeByte(numberFlags);
                        if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
                            out.writeFloat(properties.min());
                        }

                        if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
                            out.writeFloat(properties.max());
                        }

                    }
                    case INTEGER -> {
                        IntegerProperties properties = (IntegerProperties) node.properties();

                        int numberFlags = 0;
                        if (properties.min() != Integer.MIN_VALUE) {
                            numberFlags |= NUMBER_FLAG_MIN_DEFINED;
                        }

                        if (properties.max() != Integer.MAX_VALUE) {
                            numberFlags |= NUMBER_FLAG_MAX_DEFINED;
                        }

                        out.writeByte(numberFlags);
                        if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
                            out.writeInt(properties.min());
                        }

                        if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
                            out.writeInt(properties.max());
                        }

                    }
                    case LONG -> {
                        LongProperties properties = (LongProperties) node.properties();

                        int numberFlags = 0;
                        if (properties.min() != Long.MIN_VALUE) {
                            numberFlags |= NUMBER_FLAG_MIN_DEFINED;
                        }

                        if (properties.max() != Long.MAX_VALUE) {
                            numberFlags |= NUMBER_FLAG_MAX_DEFINED;
                        }

                        out.writeByte(numberFlags);
                        if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
                            out.writeLong(properties.min());
                        }

                        if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
                            out.writeLong(properties.max());
                        }

                    }
                    case STRING -> helper.writeVarInt(out, ((StringProperties) node.properties()).ordinal());
                    case ENTITY -> {
                        EntityProperties properties = (EntityProperties) node.properties();
                        int entityFlags = 0;
                        if (properties.singleTarget()) {
                            entityFlags |= ENTITY_FLAG_SINGLE_TARGET;
                        }

                        if (properties.playersOnly()) {
                            entityFlags |= ENTITY_FLAG_PLAYERS_ONLY;
                        }

                        out.writeByte(entityFlags);
                    }
                    case SCORE_HOLDER -> out.writeBoolean(((ScoreHolderProperties) node.properties()).allowMultiple());
                    case TIME -> out.writeInt(((TimeProperties) node.properties()).min());
                    case RESOURCE_OR_TAG, RESOURCE_OR_TAG_KEY, RESOURCE, RESOURCE_KEY -> helper.writeString(out, ((ResourceProperties) node.properties()).registryKey());
                    default -> {
                    }
                }

                if (node.suggestionType() != null) {
                    helper.writeResourceLocation(out, node.suggestionType());
                }
            }
        }

        helper.writeVarInt(out, this.firstNodeIndex);
    }
}
