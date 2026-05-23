package io.github.mcpaimon.extension.player;

import io.github.mcpaimon.extension.player.tools.*;
import io.github.mcpaimon.mcextension.api.IMCExtension;
import io.github.mcpaimon.papermc.MCAIPlugin;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executor;

public class PlayerExtension implements IMCExtension {

    @Override
    public void onLoad(JavaPlugin plugin, Executor executor) {
        if (plugin instanceof MCAIPlugin mcaiPlugin) {
            
            // Create the player category
            mcaiPlugin.getManager().createCategory("player", "Tools for player information and management");
            
            // Register all custom player-related tools here
            mcaiPlugin.getManager().registerTool(new GetPlayerNameTool());
            mcaiPlugin.getManager().registerTool(new GetPlayerUuidTool());
            mcaiPlugin.getManager().registerTool(new GetPlayerHealthTool());
            mcaiPlugin.getManager().registerTool(new GetPlayerFoodTool());
            mcaiPlugin.getManager().registerTool(new GetPlayerInfoTool());
            
            // Register new admin tools
            mcaiPlugin.getManager().registerTool(new BanPlayerTool());
            mcaiPlugin.getManager().registerTool(new BanPlayerIpTool());
            mcaiPlugin.getManager().registerTool(new CheckPlayerIpTool());
            
            plugin.getLogger().info("Player extension loaded successfully. Tools registered.");
        } else {
            plugin.getLogger().severe("Failed to load Player extension: Host plugin is not MCAIPlugin.");
        }
    }

    @Override
    public void onDisable(JavaPlugin plugin, Executor executor) {
        plugin.getLogger().info("Player extension has been disabled.");
    }
}
