package io.github.mcpaimon.extension.player.tools;

import io.github.mcpaimon.api.model.AIAccount;
import io.github.mcpaimon.api.tools.AITool;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Tool to retrieve a list of all players who have OP status.
 * Logic: Requires OP permission to use.
 */
public class CheckAllPlayerOpTool implements AITool {

    @Override
    public String getName() {
        return "check_all_player_op";
    }

    @Override
    public String getDescription() {
        return "Returns a list of all offline and online players who currently have operator (OP) status. Requires OP permission.";
    }

    @Override
    public String getParametersJsonSchema() {
        return "{ \"type\": \"object\", \"properties\": {} }";
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
            return CompletableFuture.completedFuture(sender.getName() + " is not op so he have no access to this command");
        }

        Set<OfflinePlayer> operators = Bukkit.getOperators();
        
        if (operators.isEmpty()) {
            return CompletableFuture.completedFuture("There are currently no operators (OPs) on this server.");
        }

        String opList = operators.stream()
                .map(OfflinePlayer::getName)
                .filter(name -> name != null)
                .collect(Collectors.joining(", "));

        return CompletableFuture.completedFuture("Total Operators (" + operators.size() + "): " + opList);
    }
}
