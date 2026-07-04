# Repository Index

This file is the entry point for any agent working in this repository. Read it first, then [`AGENTS.md`](AGENTS.md) and [`README.md`](README.md).

`extension-playertools` is an extension for the MCAgents plugin that gives AI agents tools for querying player attributes and managing players (health, food, info, bans, OP checks). Keep this index accurate whenever files or directories are added, removed, or restructured.

## Root Files

| Path | Purpose |
|---|---|
| AGENTS.md | Primary agent instruction set; routes to `agents/`. |
| README.md | Project overview, registered tools, and build instructions. |
| LICENSE | License. |
| settings.gradle | Gradle project definition. |
| build.gradle | Build configuration, repositories, and dependencies. |
| gradle.properties | Project identity and versioning. |
| gradlew / gradlew.bat / gradle/ | Gradle wrapper and version catalog. |

## Source

| Path | Purpose |
|---|---|
| src/main/java/io/github/mcpaimon/extension/player/PlayerExtension.java | Extension entry point: creates the `player` category and registers all tools. |
| src/main/java/io/github/mcpaimon/extension/player/tools/GetPlayerNameTool.java | `get_player_name` — name of the calling player. |
| src/main/java/io/github/mcpaimon/extension/player/tools/GetPlayerUuidTool.java | `get_player_uuid` — UUID of the calling player. |
| src/main/java/io/github/mcpaimon/extension/player/tools/GetPlayerHealthTool.java | `get_player_health` — health of self or others (OP for others). |
| src/main/java/io/github/mcpaimon/extension/player/tools/GetPlayerFoodTool.java | `get_player_food` — food and saturation (OP for others). |
| src/main/java/io/github/mcpaimon/extension/player/tools/GetPlayerInfoTool.java | `get_player_info` — lookup player info by name (OP for others). |
| src/main/java/io/github/mcpaimon/extension/player/tools/BanPlayerTool.java | `ban_player` — ban players (OP only). |
| src/main/java/io/github/mcpaimon/extension/player/tools/BanPlayerIpTool.java | `ban_player_ip` — ban player IPs (OP only). |
| src/main/java/io/github/mcpaimon/extension/player/tools/CheckPlayerIpTool.java | `check_player_ip` — inspect a player's IP (OP only). |
| src/main/java/io/github/mcpaimon/extension/player/tools/CheckPlayersOpTool.java | `check_players_op` — check OP status of specific players (OP only). |
| src/main/java/io/github/mcpaimon/extension/player/tools/CheckAllPlayerOpTool.java | `check_all_player_op` — list all OP players (OP only). |
| src/main/resources/extension.yml | Extension descriptor: name, version, and main class. |

## Other Directories

| Path | Purpose |
|---|---|
| agents/ | Specialized agent instructions (credentials, git, gradle, rules). See `agents/README.md`. |
| .devcontainer/ | Development container definition. |
