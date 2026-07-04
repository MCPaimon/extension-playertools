package io.github.mcpaimon.extension.player.tools;

import io.github.mcpaimon.api.model.AIAccount;
import io.github.mcpaimon.api.tools.AITool;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Tool to check if specific players have OP status.
 * Logic: Requires OP permission to use.
 */
public class CheckPlayersOpTool implements AITool {

    @Override
    public String getName() {
        return "check_players_op";
    }

    @Override
    public String getDescription() {
        return "Checks if specific players have operator (OP) status. Supports checking multiple players at once. Requires OP permission.";
    }

    @Override
    public String getParametersJsonSchema() {
        return "{"
                + "  \"type\": \"object\","
                + "  \"properties\": {"
                + "    \"player_names\": {"
                + "      \"type\": \"array\","
                + "      \"items\": { \"type\": \"string\" },"
                + "      \"description\": \"A list of player names to check for OP status\""
                + "    }"
                + "  },"
                + "  \"required\": [\"player_names\"]"
                + "}";
    }

    @Override
    public List<String> getCategories() {
        return List.of("player", "admin");
    }

    @Override
    @SuppressWarnings("unchecked")
    public CompletableFuture<String> execute(Map<String, Object> arguments, AIAccount account) {
        Player sender = Bukkit.getPlayer(UUID.fromString(account.accountUuid()));
        if (sender == null) {
            return CompletableFuture.completedFuture("Error: Cannot find sender in game.");
        }

        // Permission Check
        if (!sender.isOp()) {
            return CompletableFuture.completedFuture(sender.getName() + " is not op so he have no access to this command");
        }

        List<String> playerNames = (List<String>) arguments.get("player_names");
        if (playerNames == null || playerNames.isEmpty()) {
            return CompletableFuture.completedFuture("Error: 'player_names' parameter is required and cannot be empty.");
        }

        StringBuilder result = new StringBuilder("OP Status Check Results:\n");
        for (String rawName : playerNames) {
            if (rawName == null || rawName.trim().isEmpty()) continue;
            
            // Trim whitespace to prevent lookup failures
            String name = rawName.trim();
            boolean isOp;

            // Prioritize checking the online player first
            Player onlineTarget = Bukkit.getPlayer(name);
            if (onlineTarget != null) {
                isOp = onlineTarget.isOp();
            } else {
                // Fallback to offline player lookup
                @SuppressWarnings("deprecation")
                OfflinePlayer target = Bukkit.getOfflinePlayer(name);
                isOp = target.isOp();
            }
            
            result.append("- ").append(name).append(": ").append(isOp ? "OP" : "Not OP").append("\n");
        }

        return CompletableFuture.completedFuture(result.toString().trim());
    }
}
