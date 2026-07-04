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
 * Tool to check a player's IP address.
 * Logic: Requires OP permission. Target player must be online.
 */
public class CheckPlayerIpTool implements AITool {
    
    @Override
    public String getName() { 
        return "check_player_ip"; 
    }

    @Override
    public String getDescription() {
        return "Gets the IP address of a currently online player. Requires OP permission.";
    }

    @Override
    public String getParametersJsonSchema() {
        return "{"
                + "  \"type\": \"object\","
                + "  \"properties\": {"
                + "    \"targetName\": { \"type\": \"string\", \"description\": \"The name of the target player\" }"
                + "  },"
                + "  \"required\": [\"targetName\"]"
                + "}";
    }

    @Override
    public List<String> getCategories() {
        return List.of("player", "admin");
    }

    @Override
    public CompletableFuture<String> execute(Map<String, Object> arguments, AIAccount account) {
        Player sender = Bukkit.getPlayer(UUID.fromString(account.accountUuid()));
        if (sender == null) {
            return CompletableFuture.completedFuture("Error: Cannot find sender in game.");
        }

        // Permission Check
        if (!sender.isOp()) {
            return CompletableFuture.completedFuture("Error: Access Denied. You do not have OP permission to view IP addresses.");
        }

        String targetName = (String) arguments.get("targetName");
        if (targetName == null || targetName.trim().isEmpty()) {
            return CompletableFuture.completedFuture("Error: 'targetName' parameter is required.");
        }

        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            return CompletableFuture.completedFuture("Error: Target player is offline or does not exist.");
        }

        String ipAddress = target.getAddress() != null ? target.getAddress().getAddress().getHostAddress() : "Unknown";
        return CompletableFuture.completedFuture("Success. Player " + target.getName() + "'s IP address is: " + ipAddress);
    }
}
