package com.ferrett.viralmods;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mod.EventBusSubscriber
public class GameListener {
    public static Boolean isGameStarted = false;
    public static String game = "";

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
                        .then(Commands.argument("player1", EntityArgument.player())
                                .executes(context -> {
                                    List<ServerPlayer> players = new ArrayList<>();
                                    players.add(EntityArgument.getPlayer(context, "player1"));
                                    return startPillars(context, players);
                                })
                                .then(Commands.argument("player2", EntityArgument.player())
                                        .executes(context -> {
                                            List<ServerPlayer> players = new ArrayList<>();
                                            players.add(EntityArgument.getPlayer(context, "player1"));
                                            players.add(EntityArgument.getPlayer(context, "player2"));
                                            return startPillars(context, players);
                                        })
                                        .then(Commands.argument("player3", EntityArgument.player())
                                                .executes(context -> {
                                                    List<ServerPlayer> players = new ArrayList<>();
                                                    players.add(EntityArgument.getPlayer(context, "player1"));
                                                    players.add(EntityArgument.getPlayer(context, "player2"));
                                                    players.add(EntityArgument.getPlayer(context, "player3"));
                                                    return startPillars(context, players);
                                                })
                                                .then(Commands.argument("player4", EntityArgument.player())
                                                        .executes(context -> {
                                                            List<ServerPlayer> players = new ArrayList<>();
                                                            players.add(EntityArgument.getPlayer(context, "player1"));
                                                            players.add(EntityArgument.getPlayer(context, "player2"));
                                                            players.add(EntityArgument.getPlayer(context, "player3"));
                                                            players.add(EntityArgument.getPlayer(context, "player4"));
                                                            return startPillars(context, players);
                                                        })
                                                        .then(Commands.argument("player5", EntityArgument.player())
                                                                .executes(context -> {
                                                                    List<ServerPlayer> players = new ArrayList<>();
                                                                    players.add(EntityArgument.getPlayer(context, "player1"));
                                                                    players.add(EntityArgument.getPlayer(context, "player2"));
                                                                    players.add(EntityArgument.getPlayer(context, "player3"));
                                                                    players.add(EntityArgument.getPlayer(context, "player4"));
                                                                    players.add(EntityArgument.getPlayer(context, "player5"));
                                                                    return startPillars(context, players);
                                                                })
                                                                .then(Commands.argument("player6", EntityArgument.player())
                                                                        .executes(context -> {
                                                                            List<ServerPlayer> players = new ArrayList<>();
                                                                            players.add(EntityArgument.getPlayer(context, "player1"));
                                                                            players.add(EntityArgument.getPlayer(context, "player2"));
                                                                            players.add(EntityArgument.getPlayer(context, "player3"));
                                                                            players.add(EntityArgument.getPlayer(context, "player4"));
                                                                            players.add(EntityArgument.getPlayer(context, "player5"));
                                                                            players.add(EntityArgument.getPlayer(context, "player6"));
                                                                            return startPillars(context, players);
                                                                        })
                                                                        .then(Commands.argument("player7", EntityArgument.player())
                                                                                .executes(context -> {
                                                                                    List<ServerPlayer> players = new ArrayList<>();
                                                                                    players.add(EntityArgument.getPlayer(context, "player1"));
                                                                                    players.add(EntityArgument.getPlayer(context, "player2"));
                                                                                    players.add(EntityArgument.getPlayer(context, "player3"));
                                                                                    players.add(EntityArgument.getPlayer(context, "player4"));
                                                                                    players.add(EntityArgument.getPlayer(context, "player5"));
                                                                                    players.add(EntityArgument.getPlayer(context, "player6"));
                                                                                    players.add(EntityArgument.getPlayer(context, "player7"));
                                                                                    return startPillars(context, players);
                                                                                })
                                                                                .then(Commands.argument("player8", EntityArgument.player())
                                                                                        .executes(context -> {
                                                                                            List<ServerPlayer> players = new ArrayList<>();
                                                                                            players.add(EntityArgument.getPlayer(context, "player1"));
                                                                                            players.add(EntityArgument.getPlayer(context, "player2"));
                                                                                            players.add(EntityArgument.getPlayer(context, "player3"));
                                                                                            players.add(EntityArgument.getPlayer(context, "player4"));
                                                                                            players.add(EntityArgument.getPlayer(context, "player5"));
                                                                                            players.add(EntityArgument.getPlayer(context, "player6"));
                                                                                            players.add(EntityArgument.getPlayer(context, "player7"));
                                                                                            players.add(EntityArgument.getPlayer(context, "player8"));
                                                                                            return startPillars(context, players);
                                                                                        })
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
        );

    }

    private static int startPillars(CommandContext<CommandSourceStack> context, List<ServerPlayer> players) throws CommandSyntaxException {
        ServerPlayer executor = context.getSource().getPlayerOrException();
        ServerLevel level = context.getSource().getLevel();

        if (GameListener.isGameStarted) {
            executor.displayClientMessage(Component.literal("A game is already running!"), false);
            return 0;
        }

        PillarsOfFortune.startGame(players, executor, level);
        executor.displayClientMessage(
                Component.literal("Pillars of Fortune started for " + players.size() + " players."),
                false
        );
        return players.size();
    }

    @SubscribeEvent
    public static void onPlayerChat(ServerChatEvent event) {
        if (event.getMessage().getString().equalsIgnoreCase("stop")) {
            isGameStarted = false;
            game = "";
            event.getPlayer().sendSystemMessage(Component.literal("Game stopped."));
        }
    }
}