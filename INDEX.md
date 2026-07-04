# Repository Index

This file is the entry point for any agent working in this repository. Read it first, then [`AGENTS.md`](AGENTS.md) and [`README.md`](README.md).

`extension-playertools` is a Gradle multi project build that produces the PlayerTools extension for the MCAgents plugin. It gives AI agents tools for querying player attributes and managing players (health, food, info, bans, OP checks) on PaperMC, SpigotMC, and FoliaMC. Keep this index accurate whenever files or directories are added, removed, or restructured.

## Root Files

| Path | Purpose |
|---|---|
| AGENTS.md | Primary agent instruction set; routes to `agents/`. |
| README.md | Project overview, registered tools, and build instructions. |
| LICENSE | License. |
| settings.gradle | Multi project definition and module includes. |
| build.gradle | Root build: shared repositories, publishing, and Shadow JAR merge. |
| gradle.properties | Project identity (`project-name`, `project-group`) and versioning. |
| gradlew / gradlew.bat / gradle/ | Gradle wrapper and version catalog. |

## Modules

| Path | Module | Purpose |
|---|---|---|
| tools/ | `tools` | Platform-neutral AI tools shared by every platform module. |
| platforms/papermc/ | `platforms:papermc` | PaperMC entry point (`PlayerTools`). |
| platforms/spigotmc/ | `platforms:spigotmc` | SpigotMC entry point (`PlayerTools`). |
| platforms/foliamc/ | `platforms:foliamc` | FoliaMC entry point (`PlayerTools`). |

## Tools

| Path | Tool | Purpose |
|---|---|---|
| tools/src/.../tools/GetPlayerNameTool.java | `get_player_name` | Name of the calling player. |
| tools/src/.../tools/GetPlayerUuidTool.java | `get_player_uuid` | UUID of the calling player. |
| tools/src/.../tools/GetPlayerHealthTool.java | `get_player_health` | Health of self or others (OP for others). |
| tools/src/.../tools/GetPlayerFoodTool.java | `get_player_food` | Food and saturation (OP for others). |
| tools/src/.../tools/GetPlayerInfoTool.java | `get_player_info` | Lookup player info by name (OP for others). |
| tools/src/.../tools/BanPlayerTool.java | `ban_player` | Ban players by name (OP only). |
| tools/src/.../tools/BanPlayerIpTool.java | `ban_player_ip` | Ban players by name or IP (OP only). |
| tools/src/.../tools/CheckPlayerIpTool.java | `check_player_ip` | Inspect a player's IP (OP only). |
| tools/src/.../tools/CheckPlayersOpTool.java | `check_players_op` | Check OP status of specific players (OP only). |
| tools/src/.../tools/CheckAllPlayerOpTool.java | `check_all_player_op` | List all OP players (OP only). |
| tools/src/.../tools/PlayerTaskScheduler.java | — | Platform scheduler abstraction used by the ban tools to kick players safely. |

## Resources

| Path | Purpose |
|---|---|
| src/main/resources/extension.yml | Extension descriptor: name, version, and per platform main classes. |

## Other Directories

| Path | Purpose |
|---|---|
| agents/ | Specialized agent instructions (credentials, git, gradle, rules). See `agents/README.md`. |
| .devcontainer/ | Development container definition. |
