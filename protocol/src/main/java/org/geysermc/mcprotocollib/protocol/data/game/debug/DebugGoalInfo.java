package org.geysermc.mcprotocollib.protocol.data.game.debug;

import java.util.List;

public record DebugGoalInfo(List<DebugGoal> goals) implements DebugInfo {
    @Override
    public DebugSubscriptions getType() {
        return DebugSubscriptions.GOAL_SELECTORS;
    }

    public record DebugGoal(int priority, boolean isRunning, String name) {}
}
