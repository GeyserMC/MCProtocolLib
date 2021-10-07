package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.game.command.CommandNode;
import com.github.steveice10.mc.protocol.data.game.command.CommandParser;
import com.github.steveice10.mc.protocol.data.game.command.CommandType;
import com.github.steveice10.mc.protocol.data.game.command.SuggestionType;
import com.github.steveice10.mc.protocol.data.game.command.properties.DoubleProperties;
import com.github.steveice10.mc.protocol.data.game.command.properties.StringProperties;
import com.github.steveice10.mc.protocol.packet.PacketTest;
import org.junit.Before;

public class ServerDeclareCommandsPacketTest extends PacketTest {
    @Before
    public void setup() {
        this.setPackets(new ServerDeclareCommandsPacket(
                new CommandNode[]{
                        new CommandNode(
                                CommandType.ROOT,
                                true,
                                new int[]{1, 2},
                                -1,
                                null,
                                null,
                                null,
                                null
                        ),
                        new CommandNode(
                                CommandType.LITERAL,
                                false,
                                new int[]{3, 4},
                                -1,
                                "Literal",
                                null,
                                null,
                                null
                        ),
                        new CommandNode(
                                CommandType.ARGUMENT,
                                false,
                                new int[0],
                                3,
                                "Argument1",
                                CommandParser.DOUBLE,
                                new DoubleProperties(),
                                null
                        ),
                        new CommandNode(
                                CommandType.ARGUMENT,
                                false,
                                new int[0],
                                -1,
                                "Argument2",
                                CommandParser.DOUBLE,
                                new DoubleProperties(0, 100),
                                null
                        ),
                        new CommandNode(
                                CommandType.ARGUMENT,
                                false,
                                new int[0],
                                -1,
                                "Argument3",
                                CommandParser.STRING,
                                StringProperties.SINGLE_WORD,
                                null
                        ),
                        new CommandNode(
                                CommandType.ARGUMENT,
                                false,
                                new int[0],
                                -1,
                                "Argument4",
                                CommandParser.STRING,
                                StringProperties.SINGLE_WORD,
                                SuggestionType.ALL_RECIPES
                        )
                },
                0
        ));
    }
}
