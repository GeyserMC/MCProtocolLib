package com.github.steveice10.mc.protocol.packet.ingame.clientbound;

import com.github.steveice10.mc.protocol.data.game.command.CommandNode;
import com.github.steveice10.mc.protocol.data.game.command.CommandParser;
import com.github.steveice10.mc.protocol.data.game.command.CommandType;
import com.github.steveice10.mc.protocol.data.game.command.SuggestionType;
import com.github.steveice10.mc.protocol.data.game.command.properties.DoubleProperties;
import com.github.steveice10.mc.protocol.data.game.command.properties.StringProperties;
import com.github.steveice10.mc.protocol.packet.PacketTest;
import org.junit.jupiter.api.BeforeEach;

import java.util.OptionalInt;

public class ClientboundCommandsPacketTest extends PacketTest {
    @BeforeEach
    public void setup() {
        this.setPackets(new ClientboundCommandsPacket(
                new CommandNode[]{
                        new CommandNode(
                                CommandType.ROOT,
                                true,
                                new int[]{1, 2},
                                OptionalInt.empty(),
                                null,
                                null,
                                null,
                                null
                        ),
                        new CommandNode(
                                CommandType.LITERAL,
                                false,
                                new int[]{3, 4},
                                OptionalInt.empty(),
                                "Literal",
                                null,
                                null,
                                null
                        ),
                        new CommandNode(
                                CommandType.ARGUMENT,
                                false,
                                new int[0],
                                OptionalInt.of(3),
                                "Argument1",
                                CommandParser.DOUBLE,
                                new DoubleProperties(),
                                null
                        ),
                        new CommandNode(
                                CommandType.ARGUMENT,
                                false,
                                new int[0],
                                OptionalInt.empty(),
                                "Argument2",
                                CommandParser.DOUBLE,
                                new DoubleProperties(0, 100),
                                null
                        ),
                        new CommandNode(
                                CommandType.ARGUMENT,
                                false,
                                new int[0],
                                OptionalInt.empty(),
                                "Argument3",
                                CommandParser.STRING,
                                StringProperties.SINGLE_WORD,
                                null
                        ),
                        new CommandNode(
                                CommandType.ARGUMENT,
                                false,
                                new int[0],
                                OptionalInt.empty(),
                                "Argument4",
                                CommandParser.STRING,
                                StringProperties.SINGLE_WORD,
                                SuggestionType.ALL_RECIPES.getResourceLocation()
                        )
                },
                0
        ));
    }
}
