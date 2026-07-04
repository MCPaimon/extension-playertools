# MCPaimon PlayerTools Extension

This module is an extension for the MCAgents plugin, designed to provide AI agents with the ability to query specific attributes and real-time statistics of Minecraft players, and to run administrative player actions. It mirrors the structure of the [Core extension](https://github.com/MCPaimon/extension-core) and supports all three platforms: PaperMC, SpigotMC, and FoliaMC.

> **Note:** This extension only adds tools to the MCAgents plugin. It does not need its own route on the central API server ([`MCEngine/server-expressjs`](https://github.com/MCEngine/server-expressjs)); all persistence goes through the plugin's `/api/mcagents` route.

## Project Structure

* **`tools/`**: Platform-neutral AI tools shared by every platform module.
* **`platforms/papermc/`**: PaperMC entry point (`PlayerTools`).
* **`platforms/spigotmc/`**: SpigotMC entry point (`PlayerTools`).
* **`platforms/foliamc/`**: FoliaMC entry point (`PlayerTools`).
* **`src/main/resources/`**: The global `extension.yml` descriptor with the per platform main classes.

## Features & AI Tools

This extension registers the following tools to the AI agent:

* **`get_player_name`**: Retrieves the in-game name of the player currently conversing with the AI.
* **`get_player_uuid`**: Retrieves the unique identifier (UUID) of the current player.
* **`get_player_health`**: Gets the current and maximum health of a player. Players can check themselves, but OP permission is required to check others.
* **`get_player_food`**: Gets the food level (hunger) and saturation of a player. OP permission is required to check others.
* **`get_player_info`**: Retrieves the UUID of any specific player by providing their target name. OP permission is required to check others.
* **`ban_player`**: Bans one or multiple players by name. *(Requires OP permission; OP targets cannot be banned)*
* **`ban_player_ip`**: Bans one or multiple players by name or IP address. *(Requires OP permission; OP targets cannot be banned)*
* **`check_player_ip`**: Retrieves the IP address of an online player. *(Requires OP permission)*
* **`check_players_op`**: Checks the OP status of specific players. *(Requires OP permission)*
* **`check_all_player_op`**: Lists all operators on the server. *(Requires OP permission)*

Player-affecting actions (such as kicking during a ban) run through a platform specific scheduler: the main thread scheduler on PaperMC and SpigotMC, and the entity scheduler on FoliaMC.

## Requirements

* Java 25
* PaperMC, SpigotMC, or FoliaMC (API 26.1.2)
* MCAgents Plugin (`io.github.mcpaimon` artifacts, version 2026.0.7-8)

## Build Instructions

To build the extension and create a merged JAR containing all platform implementations:

```bash
./gradlew build
```

The compiled artifact (Shadow JAR) will be located in `build/libs/`.
