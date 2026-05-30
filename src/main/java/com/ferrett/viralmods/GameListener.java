package com.ferrett.viralmods;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;

@Mod.EventBusSubscriber
public class GameListener {
    public static Boolean isGameStarted = false;

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(
                Commands.literal("block_guessing_game")
                        .executes(context -> {
                            // No argument — just show usage message
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            player.displayClientMessage(Component.literal("Usage: /block_guessing_game <blocks> (from 2-10)"), false);
                            return 1;
                        })
                        .then(Commands.argument("blocks", IntegerArgumentType.integer(1, 10))
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    ServerLevel level = context.getSource().getLevel();
                                    int blocks = IntegerArgumentType.getInteger(context, "blocks");
                                    if (blocks < 2 || blocks > 10) {
                                        return 0;
                                    }
                                    ColorGuessingGame.startGame(player, level, blocks);
                                    return 1;
                                })
                        )


        );

        dispatcher.register(
                Commands.literal("pillars_of_fortune")
                        .executes(context -> {
                            // No argument — just show usage message
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            player.displayClientMessage(Component.literal("Usage: /pillars_of_fortune <player> (for each player)"), false);
                            return 1;
                        })
                        .then(Commands.argument("players", EntityArgument.players())
                                .executes(context -> {
                                    Collection<ServerPlayer> players =
                                            EntityArgument.getPlayers(context, "players");

                                    ServerPlayer executor = context.getSource().getPlayerOrException();
                                    ServerLevel level = context.getSource().getLevel();

                                    // Start your game logic here
                                    // PillarsOfFortune.startGame(players, executor, level);

                                    executor.displayClientMessage(
                                            Component.literal("Pillars of Fortune started for " + players.size() + " players."),
                                            false
                                    );

                                    return players.size();
                                })
                        )
        );

    }

    @SubscribeEvent
    public static void onPlayerChat(ServerChatEvent event) {
        if (event.getMessage().getString().equalsIgnoreCase("stop")) {
            isGameStarted = false;
            event.getPlayer().sendSystemMessage(Component.literal("Game stopped."));
        }
    }
}