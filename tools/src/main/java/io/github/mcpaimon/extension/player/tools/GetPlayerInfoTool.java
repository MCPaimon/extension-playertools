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
 * Tool to get a player's UUID based on their name.
 * Logic: Player can check themselves. OP can check anyone.
 */
public class GetPlayerInfoTool implements AITool {
    @Override
    public String getName() { return "get_player_info"; }

    @Override
    public String getDescription() { 
        return "Gets the UUID of a player. IMPORTANT: To get the UUID of the player currently talking to you, omit the 'targetName' parameter entirely. You do not need to know their name first."; 
    }

    @Override
    public String getParametersJsonSchema() {
        return "{ \"type\": \"object\", \"properties\": { \"targetName\": { \"type\": \"string\" } } }";
    }

    @Override
    public List<String> getCategories() {
        return List.of("player");
    }

    @Override
    public CompletableFuture<String> execute(Map<String, Object> arguments, AIAccount account) {
        Player sender = Bukkit.getPlayer(UUID.fromString(account.accountUuid()));
        if (sender == null) return CompletableFuture.completedFuture("Error: Cannot find sender in game.");

        String targetName = arguments.containsKey("targetName") ? (String) arguments.get("targetName") : sender.getName();
        Player target = Bukkit.getPlayer(targetName);
        
        if (target == null) return CompletableFuture.completedFuture("Error: Target player is offline or does not exist.");

        // Permission Check
        if (!sender.getName().equalsIgnoreCase(targetName) && !sender.isOp()) {
            return CompletableFuture.completedFuture("Error: Access Denied. You do not have OP permission to view other players' UUIDs.");
        }

        return CompletableFuture.completedFuture("Success. Name: " + target.getName() + " | UUID: " + target.getUniqueId().toString());
    }
}
