package io.github.mcpaimon.extension.template.tools;

import io.github.mcpaimon.api.model.AIAccount;
import io.github.mcpaimon.api.tools.AITool;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class GetPlayerUuid implements AITool {

    @Override
    public String getName() {
        return "get_player_uuid";
    }

    @Override
    public String getDescription() {
        return "Returns the unique identifier (UUID) of the player interacting with the AI.";
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
            // The account UUID is already available directly in the AIAccount object
            future.complete(account.accountUuid());
        } catch (Exception e) {
            future.complete("Error: Failed to retrieve player UUID. " + e.getMessage());
        }
        
        return future;
    }
}
