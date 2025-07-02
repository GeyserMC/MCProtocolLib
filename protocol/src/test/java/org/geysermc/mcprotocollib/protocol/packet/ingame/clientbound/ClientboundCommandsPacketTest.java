package org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound;

import org.geysermc.mcprotocollib.protocol.data.game.command.CommandNode;
import org.geysermc.mcprotocollib.protocol.data.game.command.CommandParser;
import org.geysermc.mcprotocollib.protocol.data.game.command.CommandType;
import org.geysermc.mcprotocollib.protocol.data.game.command.SuggestionType;
import org.geysermc.mcprotocollib.protocol.data.game.command.properties.DoubleProperties;
import org.geysermc.mcprotocollib.protocol.data.game.command.properties.StringProperties;
import org.geysermc.mcprotocollib.protocol.packet.PacketTest;
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
                                true,
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
                                true,
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
                                false,
                                new int[0],
                                OptionalInt.empty(),
                                "Argument4",
                                CommandParser.STRING,
                                StringProperties.SINGLE_WORD,
                                SuggestionType.AVAILABLE_SOUNDS.getResourceLocation()
                        )
                },
                0
        ));
    }
}
