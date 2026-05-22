package io.github.mcpaimon.extension.player.tools;

import io.github.mcpaimon.api.model.AIAccount;
import io.github.mcpaimon.api.tools.AITool;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Tool to get the UUID of the player currently talking to the AI.
 */
public class GetPlayerUuidTool implements AITool {
    @Override
    public String getName() { return "get_player_uuid"; }

    @Override
    public String getDescription() { return "Gets the unique identifier (UUID) of the player who is currently talking to you."; }

    @Override
    public String getParametersJsonSchema() {
        return "{ \"type\": \"object\", \"properties\": {} }";
    }

    @Override
    public CompletableFuture<String> execute(Map<String, Object> arguments, AIAccount account) {
        // The UUID is directly available in the AIAccount object, no need for Bukkit lookups
        return CompletableFuture.completedFuture("The UUID of the player currently talking to you is: " + account.accountUuid());
    }
}
