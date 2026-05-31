package com.ferrett.viralmods;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

record Duplet(int x, int z) {}

@Mod.EventBusSubscriber
public class PillarsOfFortune {


    private static int tickCounter = 0;
    private static List<ServerPlayer> activePlayers = new ArrayList<>();

    private static final List<Duplet> positions = new ArrayList<>();
    static { // <-- must be static
        positions.add(new Duplet(16, 0));
        positions.add(new Duplet(8, 8));
        positions.add(new Duplet(8, -8));
        positions.add(new Duplet(4, 4));
        positions.add(new Duplet(4, -4));
        positions.add(new Duplet(12, 4));
        positions.add(new Duplet(12, -4));
    }

    public static void startGame(Collection<ServerPlayer> players, Player executor, ServerLevel level) {

        if (GameListener.isGameStarted) return;
        activePlayers = new ArrayList<>(players);
        GameListener.game = "pillars_of_fortune";
        GameListener.isGameStarted = true;

        int startY = executor.getBlockY();
        int maxHeight = 290;

        // Executor pillar
        for (int y = startY; y < maxHeight; y++) {
            level.setBlock(new BlockPos(executor.getBlockX(), y, executor.getBlockZ()), Blocks.BEDROCK.defaultBlockState(), 3);
        }
        executor.teleportTo(executor.getBlockX(), maxHeight + 1, executor.getBlockZ());

        int i = 0;
        for (ServerPlayer player : players) {
            player.getInventory().clearContent();
            if (player.getUUID().equals(executor.getUUID())) continue;
            if (i >= positions.size()) break;

            int px = executor.getBlockX() + positions.get(i).x();
            int pz = executor.getBlockZ() + positions.get(i).z();

            for (int y = startY; y < maxHeight; y++) {
                level.setBlock(new BlockPos(px, y, pz), Blocks.BEDROCK.defaultBlockState(), 3);
            }
            player.teleportTo(px, maxHeight + 1, pz);
            giveRandomItems();
            i++;
        }
    }




    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent.Post event) {
        if (!GameListener.isGameStarted || !Objects.equals(GameListener.game, "pillars_of_fortune")) {
            tickCounter = 0;
            return;
        }

        tickCounter++;
        if (tickCounter >= 100) {
            tickCounter = 0;
            giveRandomItems();
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!GameListener.isGameStarted || !Objects.equals(GameListener.game, "pillars_of_fortune")) return;
        if (!(event.getEntity() instanceof ServerPlayer dead)) return;

        activePlayers.remove(dead);
        dead.sendSystemMessage(Component.literal("You have been eliminated!"));

        // Broadcast to remaining players
        for (ServerPlayer player : activePlayers) {
            player.sendSystemMessage(Component.literal(dead.getName().getString() + " has been eliminated! " + activePlayers.size() + " players remaining."));
        }

        if (activePlayers.size() == 1) {
            ServerPlayer winner = activePlayers.iterator().next();
            winner.sendSystemMessage(Component.literal("You win! Congratulations!"));
            for (ServerPlayer player : activePlayers) {
                player.sendSystemMessage(Component.literal(winner.getName().getString() + " wins!"));
            }
            GameListener.isGameStarted = false;
            GameListener.game = "";
            activePlayers.clear();
        }
    }

    private static void giveRandomItems() {
        List<Item> itemPool = net.minecraftforge.registries.ForgeRegistries.ITEMS.getValues()
                .stream()
                .toList();

        Random random = new Random();
        for (ServerPlayer player : activePlayers) {
            Item chosen = itemPool.get(random.nextInt(itemPool.size()));
            player.getInventory().add(new ItemStack(chosen, 1));
        }
    }
}
