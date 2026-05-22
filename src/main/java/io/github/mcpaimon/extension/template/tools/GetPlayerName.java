package io.github.mcpaimon.extension.template.tools;

import io.github.mcpaimon.api.model.AIAccount;
import io.github.mcpaimon.api.tools.AITool;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class GetPlayerName implements AITool {

    @Override
    public String getName() {
        return "get_player_name";
    }

    @Override
    public String getDescription() {
        return "Returns the in-game name of the player interacting with the AI.";
    }

    @Override
    public String getParametersJsonSchema() {
        // No parameters required from the AI to execute this tool
        return "{ \"type\": \"object\", \"properties\": {} }";
    }

    @Override
    public CompletableFuture<String> execute(Map<String, Object> arguments, AIAccount account) {
        CompletableFuture<String> future = new CompletableFuture<>();
        
        try {
            // Retrieve the UUID from the account object
            UUID playerUuid = UUID.fromString(account.accountUuid());
            Player player = Bukkit.getPlayer(playerUuid);
            
            if (player != null) {
                // Complete the future with the player's name
                future.complete(player.getName());
            } else {
                future.complete("Error: Player is not online or cannot be found.");
            }
        } catch (Exception e) {
            future.complete("Error: Failed to retrieve player name. " + e.getMessage());
        }
        
        return future;
    }
}
