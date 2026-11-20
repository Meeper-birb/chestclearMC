ClearRenderChests - Minecraft Forge 1.16.5

Build instructions (Windows)
-------------------------
1. Install a Java 8 JDK (Temurin 8 recommended):
   https://adoptium.net/temurin/releases/?version=8
2. Extract this folder somewhere writable (e.g. Desktop).
3. Double-click BUILD.bat.
4. On first run, the script downloads Gradle 6.9.1 and ForgeGradle/Minecraft dependencies.
   This requires an internet connection and can take several minutes.
5. The compiled mod JAR will be in build\libs.

In-game command
---------------
/clearnearbychests

Requires OP permission level 2. Clears inventories of chest block entities in loaded
chunks within the server view-distance chunk radius. Chests remain in place. It does
not force-load chunks, and it does not affect barrels or ender chests.


Compatibility note
------------------
This project targets Minecraft 1.16.5 Forge 36.2.39 and Java 8. The mod has not
been compiled/tested in this environment. If the build reports a compile error,
send the complete error output for troubleshooting.
