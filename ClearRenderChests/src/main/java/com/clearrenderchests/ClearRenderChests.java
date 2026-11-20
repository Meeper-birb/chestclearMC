package com.clearrenderchests;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod(ClearRenderChests.MOD_ID)
public class ClearRenderChests {
    public static final String MOD_ID = "clearrenderchests";

    public ClearRenderChests() {}

    @Mod.EventBusSubscriber(modid = MOD_ID)
    public static class CommandsHandler {
        @SubscribeEvent
        public static void onRegisterCommands(RegisterCommandsEvent event) {
            event.getDispatcher().register(Commands.literal("clearnearbychests")
                .requires(source -> source.hasPermission(2))
                .executes(ctx -> clearAroundPlayer(ctx.getSource())));
        }

        private static int clearAroundPlayer(CommandSource source) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
            ServerPlayerEntity player = source.getPlayerOrException();
            ServerWorld world = player.getLevel();
            MinecraftServer server = source.getServer();
            int viewDistance = Math.max(2, server.getPlayerList().getViewDistance());
            ChunkPos center = new ChunkPos(player.blockPosition());
            int cleared = 0;
            int scannedChunks = 0;

            for (int cx = center.x - viewDistance; cx <= center.x + viewDistance; cx++) {
                for (int cz = center.z - viewDistance; cz <= center.z + viewDistance; cz++) {
                    // Only inspect chunks that are already loaded; never force-load terrain.
                    Chunk chunk = world.getChunkSource().getChunkNow(cx, cz);
                    if (chunk == null) continue;
                    scannedChunks++;
                    for (TileEntity tile : new ArrayList<>(chunk.getBlockEntities().values())) {
                        if (tile instanceof ChestTileEntity) {
                            ChestTileEntity chest = (ChestTileEntity) tile;
                            if (!chest.isEmpty()) {
                                chest.clearContent();
                                chest.setChanged();
                                cleared++;
                            }
                        }
                    }
                }
            }
            final int clearedF = cleared;
            final int scannedF = scannedChunks;
            source.sendSuccess(new StringTextComponent("Cleared " + clearedF + " chest(s) in " + scannedF + " loaded chunk(s) within server view-distance range."), true);
            return cleared;
        }
    }
}
