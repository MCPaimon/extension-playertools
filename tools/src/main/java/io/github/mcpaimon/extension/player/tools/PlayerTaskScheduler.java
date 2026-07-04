package io.github.mcpaimon.extension.player.tools;

import org.bukkit.entity.Player;

/**
 * Runs a task on the thread that owns the given player.
 * Platform modules provide the implementation: the main thread scheduler on
 * PaperMC and SpigotMC, and the entity scheduler on FoliaMC.
 */
@FunctionalInterface
public interface PlayerTaskScheduler {

    /**
     * Schedules the task on the thread that is allowed to modify the player.
     *
     * @param player The player the task operates on.
     * @param task   The task to run.
     */
    void run(Player player, Runnable task);
}
