package io.github.mcpaimon.extension.player.spigotmc;

import io.github.mcpaimon.extension.player.tools.*;
import io.github.mcpaimon.mcextension.api.IMCExtension;
import io.github.mcpaimon.spigotmc.MCAgentsPlugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executor;

/**
 * The PlayerTools extension class for the MCAgents system on SpigotMC.
 * This class acts as the entry point for registering the player information
 * and management tools and their categories into the runtime environment.
 */
public class PlayerTools implements IMCExtension {

    /**
     * Invoked when the extension is loaded by the extension manager.
     * @param plugin   The host plugin instance executing this extension.
     * @param executor The executor thread provided for async operations if needed.
     */
    @Override
    public void onLoad(Object plugin, Executor executor) {
        if (plugin instanceof MCAgentsPlugin mcagentsPlugin) {

            // Runs player affecting tasks on the main server thread
            PlayerTaskScheduler scheduler =
                    (target, task) -> Bukkit.getScheduler().runTask(mcagentsPlugin, task);

            // Create the tool categories
            mcagentsPlugin.getProvider().createCategory("player", "Tools for player information and management");
            mcagentsPlugin.getProvider().createCategory("admin", "Administrative tools for player management");

            // Register the player information tools
            mcagentsPlugin.getProvider().registerTool(new GetPlayerNameTool());
            mcagentsPlugin.getProvider().registerTool(new GetPlayerUuidTool());
            mcagentsPlugin.getProvider().registerTool(new GetPlayerHealthTool());
            mcagentsPlugin.getProvider().registerTool(new GetPlayerFoodTool());
            mcagentsPlugin.getProvider().registerTool(new GetPlayerInfoTool());

            // Register the admin tools
            mcagentsPlugin.getProvider().registerTool(new BanPlayerTool(scheduler));
            mcagentsPlugin.getProvider().registerTool(new BanPlayerIpTool(scheduler));
            mcagentsPlugin.getProvider().registerTool(new CheckAllPlayerOpTool());
            mcagentsPlugin.getProvider().registerTool(new CheckPlayerIpTool());
            mcagentsPlugin.getProvider().registerTool(new CheckPlayersOpTool());

            mcagentsPlugin.getLogger().info("PlayerTools extension loaded successfully on SpigotMC. Tools registered.");
        } else if (plugin instanceof JavaPlugin javaPlugin) {
            javaPlugin.getLogger().severe("Failed to load PlayerTools extension: Host plugin is not MCAgentsPlugin.");
        }
    }

    /**
     * Invoked when the extension is disabled.
     * @param plugin   The host plugin instance executing this extension.
     * @param executor The executor thread provided for async operations if needed.
     */
    @Override
    public void onDisable(Object plugin, Executor executor) {
        if (plugin instanceof JavaPlugin javaPlugin) {
            javaPlugin.getLogger().info("PlayerTools extension has been disabled on SpigotMC.");
        }
    }
}
