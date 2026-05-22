package io.github.mcpaimon.extension.template;

import io.github.mcpaimon.extension.template.tools.GetPlayerName;
import io.github.mcpaimon.extension.template.tools.GetPlayerUuid;
import io.github.mcpaimon.mcextension.api.IMCExtension;
import io.github.mcpaimon.papermc.MCAIPlugin;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executor;

public class Template implements IMCExtension {

    @Override
    public void onLoad(JavaPlugin plugin, Executor executor) {
        if (plugin instanceof MCAIPlugin mcaiPlugin) {
            
            // Register your custom tools here
            mcaiPlugin.getManager().registerTool(new GetPlayerName());
            mcaiPlugin.getManager().registerTool(new GetPlayerUuid());
            
            plugin.getLogger().info("Template extension loaded successfully. Tools registered.");
        } else {
            plugin.getLogger().severe("Failed to load Template extension: Host plugin is not MCAIPlugin.");
        }
    }

    @Override
    public void onDisable(JavaPlugin plugin, Executor executor) {
        plugin.getLogger().info("Template extension has been disabled.");
    }
}
