package com.ferrett.viralmods;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.block.state.BlockState;
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

        dispatcher.register(
                Commands.literal("mob-powers")
                        .executes(context -> {
                            if (MobPowers.mobPowers) {
                                MobPowers.powers.clear();
                                GameListener.isGameStarted = false;
                                ServerPlayer player = context.getSource().getPlayerOrException();
                                List<ServerPlayer> allPlayers = ((ServerLevel) player.level()).getServer().getPlayerList().getPlayers();
                                for (ServerPlayer player1 : allPlayers) {
                                    player1.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20);
                                    player1.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.1);
                                    player1.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(2);
                                    player1.getAbilities().mayfly = false;
                                    player1.getAbilities().flying = false;
                                    player1.onUpdateAbilities();
                                }


                                MobPowers.mobPowers = false;

                                player.displayClientMessage(Component.literal("Mob Powers turned off"), false);
                            } else {

                                MobPowers.powers.clear();
                                MobPowers.mobPowers = true;
                                ServerPlayer player = context.getSource().getPlayerOrException();
                                player.displayClientMessage(Component.literal("Mob Powers turned on"), false);
                            }
                            return 1;
                        })
        );

        dispatcher.register(
                Commands.literal("deathswap")
                        .then(Commands.argument("player1", EntityArgument.player())
                                .executes(context -> {
                                    List<ServerPlayer> players = new ArrayList<>();
                                    players.add(EntityArgument.getPlayer(context, "player1"));
                                    return startDeathSwap(context, players);
                                })
                                .then(Commands.argument("player2", EntityArgument.player())
                                        .executes(context -> {
                                            List<ServerPlayer> players = new ArrayList<>();
                                            players.add(EntityArgument.getPlayer(context, "player1"));
                                            players.add(EntityArgument.getPlayer(context, "player2"));
                                            return startDeathSwap(context, players);
                                        })
                                )
                        )
        );

        dispatcher.register(
                Commands.literal("mob-hide-and-seek")
                        .executes(context -> {
                            // No argument — just show usage message
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            player.displayClientMessage(Component.literal("Usage: /mob-hide-and-seek <players> (from 2-5)"), false);
                            return 1;
                        })
                        .then(Commands.argument("player1", EntityArgument.player())
                                .executes(context -> {
                                    List<ServerPlayer> players = new ArrayList<>();
                                    players.add(EntityArgument.getPlayer(context, "player1"));
                                    return startModHAS(context, players);
                                })
                                .then(Commands.argument("player2", EntityArgument.player())
                                        .executes(context -> {
                                            List<ServerPlayer> players = new ArrayList<>();
                                            players.add(EntityArgument.getPlayer(context, "player1"));
                                            players.add(EntityArgument.getPlayer(context, "player2"));
                                            return startModHAS(context, players);
                                        })
                                )
                                .then(Commands.argument("player3", EntityArgument.player())
                                        .executes(context -> {
                                            List<ServerPlayer> players = new ArrayList<>();
                                            players.add(EntityArgument.getPlayer(context, "player1"));
                                            players.add(EntityArgument.getPlayer(context, "player2"));
                                            players.add(EntityArgument.getPlayer(context, "player3"));
                                            return startModHAS(context, players);
                                        })
                                        .then(Commands.argument("player4", EntityArgument.player())
                                                .executes(context -> {
                                                    List<ServerPlayer> players = new ArrayList<>();
                                                    players.add(EntityArgument.getPlayer(context, "player1"));
                                                    players.add(EntityArgument.getPlayer(context, "player2"));
                                                    players.add(EntityArgument.getPlayer(context, "player3"));
                                                    players.add(EntityArgument.getPlayer(context, "player4"));
                                                    return startModHAS(context, players);
                                                })
                                                .then(Commands.argument("player5", EntityArgument.player())
                                                        .executes(context -> {
                                                            List<ServerPlayer> players = new ArrayList<>();
                                                            players.add(EntityArgument.getPlayer(context, "player1"));
                                                            players.add(EntityArgument.getPlayer(context, "player2"));
                                                            players.add(EntityArgument.getPlayer(context, "player3"));
                                                            players.add(EntityArgument.getPlayer(context, "player4"));
                                                            players.add(EntityArgument.getPlayer(context, "player5"));
                                                            return startModHAS(context, players);
                                                        })
                                                )
                                        )
                                )
                        )

        );

        dispatcher.register(
                Commands.literal("one-block")
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            return 1;
                        })
                        .then(Commands.argument("block",
                                                BlockStateArgument.block(event.getBuildContext())
                                        )
                                        .executes(context -> {
                                            ServerPlayer player = context.getSource().getPlayerOrException();
                                            BlockState block = BlockStateArgument.getBlock(context, "block").getState();
                                            player.displayClientMessage(Component.literal("You can now only touch the block " + block.getBlock().getName().getString() + ". Type stop to stop."), false);
                                            OneBlock.startOneBlock(player, player.level(), block.getBlock());
                                            return 1;
                                        })
                        )
        );
        dispatcher.register(
                Commands.literal("skyblock-random-items")
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            SkyblockRandomItems.startGame(player);
                            return 1;
                        })
        );




    }


    private static int startModHAS(CommandContext<CommandSourceStack> context, List<ServerPlayer> players) throws CommandSyntaxException {
        ServerPlayer executor = context.getSource().getPlayerOrException();
        ServerLevel level = context.getSource().getLevel();
        if (players.size() < 2 || players.size() > 5) {
            executor.displayClientMessage(Component.literal("Not correct amount of players (2-5)!"), false);
            return 0;
        }
        if (MobHideAndSeek.mobHideAndSeek) {
            executor.displayClientMessage(Component.literal("Game already running!"), false);
            return 0;
        }

        for (ServerPlayer player : players) {
            player.displayClientMessage(Component.literal("Mob Hide and Seek Started"), false);
        }
        MobHideAndSeek.startHideAndSeek(players, level);
        return 1;
    }

    private static int startDeathSwap(CommandContext<CommandSourceStack> context, List<ServerPlayer> players) throws CommandSyntaxException {
        ServerPlayer executor = context.getSource().getPlayerOrException();
        ServerLevel level = context.getSource().getLevel();
        if (players.size() != 2) {
            executor.displayClientMessage(Component.literal("Not correct amount of players (2)!"), false);
            return 0;
        }
        if (DeathSwap.swap) {
            executor.displayClientMessage(Component.literal("Game already running!"), false);
            return 0;
        }
        if (players.get(0) == players.get(1)) {
            executor.displayClientMessage(Component.literal("Cannot use same player"), false);
            return 0;
        }
        for (ServerPlayer player : players) {
            player.displayClientMessage(Component.literal("DeathSwap Started"), false);
        }
        DeathSwap.startDeathSwap(players, level);
        return 1;
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
            OneBlock.oneBlock = false;
            SkyblockRandomItems.randItems = false;
            event.getPlayer().getAbilities().flying = false;
            game = "";
            if (DeathSwap.swap) {
                DeathSwap.swap = false;
            }
            if (MobHideAndSeek.mobHideAndSeek) {
                MobHideAndSeek.mobHideAndSeek = false;
            }
            event.getPlayer().sendSystemMessage(Component.literal("Games stopped."));
        }
    }
}