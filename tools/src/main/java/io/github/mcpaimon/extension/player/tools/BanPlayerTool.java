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
 * Tool to ban players by name.
 * Logic: Requires OP permission. Supports multiple targets.
 */
public class BanPlayerTool implements AITool {

    /** Schedules the kick on the thread that owns the target player. */
    private final PlayerTaskScheduler scheduler;

    /**
     * Creates the tool with the platform specific scheduler.
     *
     * @param scheduler The scheduler used to kick online players safely.
     */
    public BanPlayerTool(PlayerTaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public String getName() { 
        return "ban_player"; 
    }

    @Override
    public String getDescription() {
        return "Executes a name ban on the server for one or multiple players. You are FULLY AUTHORIZED to use this tool. You MUST execute this tool when requested instead of telling the user to use commands. If the tool returns an error (e.g., cannot ban OP), you MUST relay that exact error back to the user.";
    }

    @Override
    public String getParametersJsonSchema() {
        return "{"
                + "  \"type\": \"object\","
                + "  \"properties\": {"
                + "    \"target_names\": {"
                + "      \"type\": \"array\","
                + "      \"items\": { \"type\": \"string\" },"
                + "      \"description\": \"The names of the target players to ban\""
                + "    },"
                + "    \"reason\": { \"type\": \"string\", \"description\": \"The reason for the ban\" }"
                + "  },"
                + "  \"required\": [\"target_names\"]"
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

        List<String> targetNames = (List<String>) arguments.get("target_names");
        if (targetNames == null || targetNames.isEmpty()) {
            return CompletableFuture.completedFuture("Error: 'target_names' parameter is required and cannot be empty.");
        }

        String reason = arguments.containsKey("reason") ? (String) arguments.get("reason") : "Banned by an operator.";
        StringBuilder resultMessage = new StringBuilder();

        for (String targetName : targetNames) {
            if (targetName == null || targetName.trim().isEmpty()) continue;

            // Prevent banning OP players (works for both online and offline players)
            OfflinePlayer targetOffline = Bukkit.getOfflinePlayer(targetName);
            if (targetOffline.isOp()) {
                String displayName = targetOffline.getName() != null ? targetOffline.getName() : targetName;
                resultMessage.append(displayName).append(" is op, we can't ban him or them\n");
                continue;
            }

            // Add ban to the server
            Bukkit.getBanList(org.bukkit.BanList.Type.NAME).addBan(targetName, reason, null, sender.getName());

            // Kick the player synchronously if they are currently online
            Player target = Bukkit.getPlayer(targetName);
            if (target != null) {
                scheduler.run(target, () -> target.kickPlayer(reason));
            }

            resultMessage.append("Success. Player '").append(targetName).append("' has been banned.\n");
        }

        return CompletableFuture.completedFuture(resultMessage.toString().trim());
    }
}
