package io.github.mcpaimon.extension.player.tools;

import io.github.mcpaimon.api.model.AIAccount;
import io.github.mcpaimon.api.tools.AITool;
import io.github.mcpaimon.papermc.MCAIPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Tool to ban a player by name.
 * Logic: Requires OP permission.
 */
public class BanPlayerTool implements AITool {
    
    @Override
    public String getName() { 
        return "ban_player"; 
    }

    @Override
    public String getDescription() {
        return "Bans a player from the server by their name. Requires OP permission.";
    }

    @Override
    public String getParametersJsonSchema() {
        return "{"
                + "  \"type\": \"object\","
                + "  \"properties\": {"
                + "    \"targetName\": { \"type\": \"string\", \"description\": \"The name of the target player to ban\" },"
                + "    \"reason\": { \"type\": \"string\", \"description\": \"The reason for the ban (optional)\" }"
                + "  },"
                + "  \"required\": [\"targetName\"]"
                + "}";
    }

    @Override
    public List<String> getCategories() {
        return List.of("player", "admin");
    }

    @Override
    @SuppressWarnings("deprecation")
    public CompletableFuture<String> execute(Map<String, Object> arguments, AIAccount account) {
        Player sender = Bukkit.getPlayer(UUID.fromString(account.accountUuid()));
        if (sender == null) {
            return CompletableFuture.completedFuture("Error: Cannot find sender in game.");
        }

        // Permission Check
        if (!sender.isOp()) {
            return CompletableFuture.completedFuture("Error: Access Denied. You do not have OP permission to ban players.");
        }

        String targetName = (String) arguments.get("targetName");
        if (targetName == null || targetName.trim().isEmpty()) {
            return CompletableFuture.completedFuture("Error: 'targetName' parameter is required.");
        }

        String reason = arguments.containsKey("reason") ? (String) arguments.get("reason") : "Banned by an operator.";

        // Add ban to the server
        Bukkit.getBanList(org.bukkit.BanList.Type.NAME).addBan(targetName, reason, null, sender.getName());

        // Kick the player synchronously if they are currently online
        Player target = Bukkit.getPlayer(targetName);
        if (target != null) {
            Bukkit.getScheduler().runTask(JavaPlugin.getPlugin(MCAIPlugin.class), () -> target.kickPlayer(reason));
        }

        return CompletableFuture.completedFuture("Success. Player '" + targetName + "' has been banned. Reason: " + reason);
    }
}
