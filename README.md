# MCPaimon Player Extension

This module is an extension for the MCPaimon AI Plugin, designed to provide AI agents with the ability to query specific attributes and real-time statistics of Minecraft players.

## Features & AI Tools

This extension registers the following tools to the AI agent:

* **`get_player_name`**: Retrieves the in-game name of the player currently conversing with the AI.
* **`get_player_uuid`**: Retrieves the unique identifier (UUID) of the current player.
* **`get_player_health`**: Gets the current and maximum health of a player. Players can check themselves, but OP permission is required to check others.
* **`get_player_food`**: Gets the food level (hunger) and saturation of a player. OP permission is required to check others.
* **`get_player_info`**: Retrieves the UUID of any specific player by providing their target name. OP permission is required to check others.

## Requirements
* Java 25
* PaperMC 1.21+ (Paper API 26.1.2)
* MCPaimon AI Plugin Core

## Build Instructions
To build the extension, use the included Gradle wrapper:
```bash
./gradlew build
```

The compiled artifact will be located in `build/libs/`.
