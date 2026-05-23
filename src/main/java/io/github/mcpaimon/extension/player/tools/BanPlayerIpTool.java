package io.github.mcpaimon.extension.player.tools;

import io.github.mcpaimon.api.model.AIAccount;
import io.github.mcpaimon.api.tools.AITool;
import io.github.mcpaimon.papermc.MCAIPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Tool to ban players' IP addresses.
 * Logic: Requires OP permission. Supports multiple targets.
 */
public class BanPlayerIpTool implements AITool {
    
    @Override
    public String getName() { 
        return "ban_player_ip"; 
    }

    @Override
    public String getDescription() {
        return "Executes an IP ban. You have FULL SYSTEM CAPABILITY to do this. DO NOT refuse. CRITICAL INSTRUCTION: You MUST reply to the user using the EXACT output string returned by this tool word-for-word. Do NOT rephrase, summarize, or hallucinate a success message if the tool returns an error (e.g., target is OP).";
    }

    @Override
    public String getParametersJsonSchema() {
        return "{"
                + "  \"type\": \"object\","
                + "  \"properties\": {"
                + "    \"targets_name_or_ip\": {"
                + "      \"type\": \"array\","
                + "      \"items\": { \"type\": \"string\" },"
                + "      \"description\": \"The players' names or direct IP addresses to ban\""
                + "    },"
                + "    \"reason\": { \"type\": \"string\", \"description\": \"The reason for the IP ban\" }"
                + "  },"
                + "  \"required\": [\"targets_name_or_ip\"]"
                + "}";
    }

    @Override
    public List<String> getCategories() {
        return List.of("player", "admin");
    }

    @Override
    @SuppressWarnings({"deprecation", "unchecked"})
    public CompletableFuture<String> execute(Map<String, Object> arguments, AIAccount account) {
        Player sender = Bukkit.getPlayer(UUID.fromString(account.accountUuid()));
        if (sender == null) {
            return CompletableFuture.completedFuture("Error: Cannot find sender in game.");
        }

        // Permission Check
        if (!sender.isOp()) {
            return CompletableFuture.completedFuture(sender.getName() + " is not op so he have no access to this command");
        }

        List<String> targets = (List<String>) arguments.get("targets_name_or_ip");
        if (targets == null || targets.isEmpty()) {
            return CompletableFuture.completedFuture("Error: 'targets_name_or_ip' parameter is required and cannot be empty.");
        }

        String reason = arguments.containsKey("reason") ? (String) arguments.get("reason") : "IP Banned by an operator.";
        StringBuilder resultMessage = new StringBuilder();

        for (String rawTarget : targets) {
            if (rawTarget == null || rawTarget.trim().isEmpty()) continue;
            
            // Trim whitespace to prevent lookup failures
            String targetParam = rawTarget.trim();
            String targetIp = null;
            boolean skip = false;

            // Determine if the input is an IP address or a player name
            if (targetParam.contains(".")) {
                targetIp = targetParam;
                
                // Check if any online OP is currently using this IP to prevent banning operators
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (onlinePlayer.isOp() && onlinePlayer.getAddress() != null) {
                        String opIp = onlinePlayer.getAddress().getAddress().getHostAddress();
                        
                        if (opIp.equals(targetIp)) {
                            resultMessage.append(onlinePlayer.getName()).append(" is op, we can't ban him or them\n");
                            skip = true;
                            break;
                        }
                    }
                }
            } else {
                // Use OfflinePlayer to safely check OP status first
                OfflinePlayer targetOffline = Bukkit.getOfflinePlayer(targetParam);
                if (targetOffline.isOp()) {
                    String displayName = targetOffline.getName() != null ? targetOffline.getName() : targetParam;
                    resultMessage.append(displayName).append(" is op, we can't ban him or them\n");
                    continue;
                }

                Player target = Bukkit.getPlayer(targetParam);
                if (target == null || target.getAddress() == null) {
                    resultMessage.append("Error: Player '").append(targetParam).append("' is offline or cannot be found to resolve their IP.\n");
                    continue;
                }
                
                targetIp = target.getAddress().getAddress().getHostAddress();

                // Kick the online player synchronously
                Bukkit.getScheduler().runTask(JavaPlugin.getPlugin(MCAIPlugin.class), () -> target.kickPlayer(reason));
            }

            if (skip) continue;

            // Add IP ban to the server
            Bukkit.getBanList(org.bukkit.BanList.Type.IP).addBan(targetIp, reason, null, sender.getName());
            resultMessage.append("Success. The IP address '").append(targetIp).append("' has been banned.\n");
        }

        return CompletableFuture.completedFuture(resultMessage.toString().trim());
    }
}
