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
 * Tool to ban a player's IP address.
 * Logic: Requires OP permission.
 */
public class BanPlayerIpTool implements AITool {
    
    @Override
    public String getName() { 
        return "ban_player_ip"; 
    }

    @Override
    public String getDescription() {
        return "Bans a player's IP address. You can provide either the player's name (must be online) or a direct IP address. Requires OP permission.";
    }

    @Override
    public String getParametersJsonSchema() {
        return "{"
                + "  \"type\": \"object\","
                + "  \"properties\": {"
                + "    \"targetNameOrIp\": { \"type\": \"string\", \"description\": \"The player's name or direct IP address to ban\" },"
                + "    \"reason\": { \"type\": \"string\", \"description\": \"The reason for the IP ban (optional)\" }"
                + "  },"
                + "  \"required\": [\"targetNameOrIp\"]"
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
            return CompletableFuture.completedFuture("Error: Access Denied. You do not have OP permission to ban IP addresses.");
        }

        String targetParam = (String) arguments.get("targetNameOrIp");
        if (targetParam == null || targetParam.trim().isEmpty()) {
            return CompletableFuture.completedFuture("Error: 'targetNameOrIp' parameter is required.");
        }

        String reason = arguments.containsKey("reason") ? (String) arguments.get("reason") : "IP Banned by an operator.";
        String targetIp;

        // Determine if the input is an IP address or a player name
        if (targetParam.contains(".")) {
            targetIp = targetParam;
        } else {
            Player target = Bukkit.getPlayer(targetParam);
            if (target == null || target.getAddress() == null) {
                return CompletableFuture.completedFuture("Error: Player '" + targetParam + "' is offline or cannot be found to resolve their IP.");
            }
            targetIp = target.getAddress().getAddress().getHostAddress();

            // Kick the online player synchronously
            Bukkit.getScheduler().runTask(JavaPlugin.getPlugin(MCAIPlugin.class), () -> target.kickPlayer(reason));
        }

        // Add IP ban to the server
        Bukkit.getBanList(org.bukkit.BanList.Type.IP).addBan(targetIp, reason, null, sender.getName());

        return CompletableFuture.completedFuture("Success. The IP address '" + targetIp + "' has been banned. Reason: " + reason);
    }
}
