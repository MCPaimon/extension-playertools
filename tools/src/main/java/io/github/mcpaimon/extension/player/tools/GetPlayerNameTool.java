package io.github.mcpaimon.extension.player.tools;

import io.github.mcpaimon.api.model.AIAccount;
import io.github.mcpaimon.api.tools.AITool;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Tool to get the name of the player currently talking to the AI.
 */
public class GetPlayerNameTool implements AITool {
    @Override
    public String getName() { return "get_player_name"; }

    @Override
    public String getDescription() { return "Gets the in-game name of the player who is currently talking to you."; }

    @Override
    public String getParametersJsonSchema() {
        return "{ \"type\": \"object\", \"properties\": {} }";
    }

    @Override
    public List<String> getCategories() {
        return List.of("player");
    }

    @Override
    public CompletableFuture<String> execute(Map<String, Object> arguments, AIAccount account) {
        // Retrieve player directly using Bukkit API instead of PlayerTools
        Player sender = Bukkit.getPlayer(UUID.fromString(account.accountUuid()));
        if (sender == null) return CompletableFuture.completedFuture("Error: Cannot find sender in game.");

        return CompletableFuture.completedFuture("The player currently talking to you is: " + sender.getName());
    }
}
