package com.ferrett.viralmods;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class DeathSwap {
    public static Boolean swap = false;
    private static int tickCounter = 0;
    private static List<ServerPlayer> thePlayers = new ArrayList<>();

    public static void startDeathSwap(List<ServerPlayer> players, Level level) {
        thePlayers = players;
        swap = true;
        tickCounter = 0;
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent.Post event) {
        if (!swap) return;

        tickCounter++;

        if (tickCounter == 1200) {
            for (ServerPlayer player : thePlayers) {
                player.displayClientMessage(Component.literal("§cSwap in 4 minutes"), false);
            }
        }
        if (tickCounter == 2400) {
            for (ServerPlayer player : thePlayers) {
                player.displayClientMessage(Component.literal("§cSwap in 3 minutes"), false);
            }
        }
        if (tickCounter == 3600) {
            for (ServerPlayer player : thePlayers) {
                player.displayClientMessage(Component.literal("§cSwap in 2 minutes"), false);
            }
        }
        if (tickCounter == 4800) {
            for (ServerPlayer player : thePlayers) {
                player.displayClientMessage(Component.literal("§cSwap in 1 minute"), false);
            }
        }


        if (tickCounter >= 5800 && tickCounter <= 5980 && (tickCounter - 5800) % 20 == 0) {
            int countdown = 10 - (tickCounter - 5800) / 20;
            for (ServerPlayer player : thePlayers) {
                player.displayClientMessage(Component.literal("§cSwap in " + String.valueOf(countdown)), false);
            }
        }
        if (tickCounter > 6000) {
            tickCounter = 0;
            ServerPlayer player1 = thePlayers.getFirst();
            ServerPlayer player2 = thePlayers.getLast();
            BlockPos player1pos = player1.blockPosition();
            BlockPos player2pos = player2.blockPosition();
            player1.teleportTo(player2pos.getX() + 0.5, player2pos.getY(), player2pos.getZ() + 0.5);
            player2.teleportTo(player1pos.getX() + 0.5, player1pos.getY(), player1pos.getZ() + 0.5);

        }
    }
    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!swap) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        ServerPlayer player1 = thePlayers.getFirst();
        ServerPlayer player2 = thePlayers.getLast();
        if (player.getUUID().equals(player1.getUUID())) {

            swap = false;
            for (ServerPlayer aplayer : thePlayers) {
                aplayer.displayClientMessage(Component.literal("§6" + player1.getName().getString() + " has died, " + player2.getName().getString() + " wins!"), false);
            }
            thePlayers.clear();
        }
        else if (player.getUUID().equals(player2.getUUID())) {
            swap = false;
            for (ServerPlayer aplayer : thePlayers) {
                aplayer.displayClientMessage(Component.literal("§6" + player2.getName().getString() + " has died, " + player1.getName().getString() + " wins!"), false);
            }
            thePlayers.clear();
        }
    }
}
