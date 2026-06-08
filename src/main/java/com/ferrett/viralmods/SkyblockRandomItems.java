package com.ferrett.viralmods;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;


@Mod.EventBusSubscriber
public class SkyblockRandomItems {
    public static boolean randItems = false;
    private static int tick = 0;
    public static ServerPlayer Splayer = null;
    private static ServerBossEvent bossBar = null;
    public static void startGame(ServerPlayer player) {
        bossBar = new ServerBossEvent(
                Component.literal("Next Item"),
                BossEvent.BossBarColor.GREEN,
                BossEvent.BossBarOverlay.PROGRESS
        );
        bossBar.addPlayer(player);
        bossBar.setProgress(1.0f);
        // maxHeightWorld = 320
        tick = 200;
        Splayer = player;
        ServerLevel level = player.level();
        BlockPos startPos = new BlockPos(player.getBlockX(), 300, player.getBlockZ());
        for (int i = -1; i <= 1; i++) {
            for (int z = -1; z <= 1; z++) {
                level.setBlock(new BlockPos(player.getBlockX() + i, 299, player.getBlockZ() + z), Blocks.BEDROCK.defaultBlockState(), 3);
            }
        }
        player.teleportTo(startPos.getX(), startPos.getY(), startPos.getZ());
        randItems = true;
    }
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent.Post event) {
        if (!randItems) return;
        if (Splayer.blockPosition().getY() < 260) {
            Splayer.kill((ServerLevel) Splayer.level());
            Splayer.displayClientMessage(Component.literal("You went below the allowed height, game over!"), false);
            bossBar.removePlayer(Splayer);
            bossBar = null;
            Splayer = null;
            randItems = false;
            return;

        }
        tick++;
        bossBar.setProgress(1.0f - (tick / 300.0f)); // counts down from full to empty
        bossBar.setName(Component.literal("Next Item: " + (300 - tick) / 20 + "s"));

        if (tick < 300) return;
        tick = 0;
        bossBar.setProgress(1.0f); // reset bar
        List<Item> itemPool = net.minecraftforge.registries.ForgeRegistries.ITEMS.getValues().stream().toList();
        Item chosen = itemPool.get(new java.util.Random().nextInt(itemPool.size()));
        Splayer.getInventory().add(new ItemStack(chosen, 1));
        Splayer.displayClientMessage(Component.literal("New item: " + chosen.getName(new ItemStack(chosen)).getString()), false);
    }
}
