package com.github.steveice10.mc.protocol.data.game.command;

import com.github.steveice10.mc.protocol.data.game.command.properties.CommandProperties;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

/**
 * @param type           Type of command.
 * @param executable     Whether the node is executable.
 * @param childIndices   Child node indices.
 * @param redirectIndex  Redirect index, or empty if none is set.
 * @param name           Name of the node, if type is LITERAL or ARGUMENT.
 * @param parser         Node parser, if type is ARGUMENT.
 * @param properties     Command properties, if type is ARGUMENT and the parser has properties.
 * @param suggestionType Suggestions type, if present.
 *                       See {@link SuggestionType} for vanilla defaults.
 */
public record CommandNode(@NonNull CommandType type, boolean executable, int @NonNull [] childIndices,
                          @Nullable Integer redirectIndex, String name, CommandParser parser,
                          CommandProperties properties,
                          String suggestionType) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommandNode that)) return false;
        return executable == that.executable && type == that.type && Arrays.equals(childIndices, that.childIndices) && Objects.equals(redirectIndex, that.redirectIndex) && Objects.equals(name, that.name) && parser == that.parser && Objects.equals(properties, that.properties) && Objects.equals(suggestionType, that.suggestionType);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(type, executable, redirectIndex, name, parser, properties, suggestionType);
        result = 31 * result + Arrays.hashCode(childIndices);
        return result;
    }
}
