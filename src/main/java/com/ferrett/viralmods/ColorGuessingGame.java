package com.ferrett.viralmods;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber
public class ColorGuessingGame {
    private static int amountOfBlocks = 0;
    private static List<Block> useBlocks = new ArrayList<>();
    private static List<BlockPos> blocksToSetToAirOnWin = new ArrayList<>();
    private static int guesses = 0;
    private static BlockPos buttonPos = null;
    private static List<BlockPos> blockPosOfPlacedBlocks = new ArrayList<>();
    private static final Block[] usableBlocks = {
            Blocks.STONE,
            Blocks.GRASS_BLOCK,
            Blocks.SAND,
            Blocks.GRAVEL,
            Blocks.GOLD_ORE,
            Blocks.DIAMOND_ORE,
            Blocks.OBSIDIAN,
            Blocks.NETHERRACK,
            Blocks.SOUL_SAND,
            Blocks.MAGMA_BLOCK,
            Blocks.END_STONE,
            Blocks.PURPUR_BLOCK,
            Blocks.PRISMARINE,
            Blocks.SEA_LANTERN,
            Blocks.TERRACOTTA,
            Blocks.SANDSTONE,
            Blocks.BRICKS,
            Blocks.BOOKSHELF,
            Blocks.SNOW_BLOCK,
            Blocks.ICE,
            Blocks.GLOWSTONE,
            Blocks.PUMPKIN,
            Blocks.HAY_BLOCK,
            Blocks.HONEYCOMB_BLOCK,
            Blocks.DRIED_KELP_BLOCK,
            Blocks.SPONGE,
            Blocks.TNT,
            Blocks.CRAFTING_TABLE,
            Blocks.FURNACE,
            Blocks.BARREL,
            Blocks.BEEHIVE,
            Blocks.MYCELIUM,
            Blocks.NETHER_WART_BLOCK,
            Blocks.SHROOMLIGHT,
            Blocks.CRYING_OBSIDIAN,
            Blocks.BLACKSTONE,
            Blocks.BASALT,
            Blocks.AMETHYST_BLOCK,
            Blocks.CALCITE,
            Blocks.TUFF,
            Blocks.DRIPSTONE_BLOCK,
            Blocks.MUD,
            Blocks.MANGROVE_ROOTS,
            Blocks.SCULK,
            Blocks.ANCIENT_DEBRIS,
            Blocks.LODESTONE,
            Blocks.RESPAWN_ANCHOR
    };
    public static void startGame(ServerPlayer player, ServerLevel level, int blocks) {
        if (GameListener.isGameStarted) {
            return;
        }
        // Restart vars
        blockPosOfPlacedBlocks.clear();
        buttonPos = null;
        guesses = 0;
        useBlocks.clear();
        amountOfBlocks = blocks;

        GameListener.isGameStarted = true;

        for (int i = 0; i < blocks; i++) {
            Block randomBlock = usableBlocks[new Random().nextInt(usableBlocks.length)];
            useBlocks.add(randomBlock);
            if (i == blocks - 1) break;
            int x = new Random().nextInt(2);
            if (x == 0 && blocks > 2) {
                useBlocks.add(randomBlock);
                i++;
            }
        }

        BlockPos startFloor = new BlockPos(player.getBlockX() - 1, player.getBlockY(), player.getBlockZ() - 1);
        BlockPos endFloor = new BlockPos(player.getBlockX() - 1 + blocks + 1, player.getBlockY(), player.getBlockZ() + 10);
        BlockPos middle = new BlockPos(player.getBlockX() - 2 + Math.round((float) (blocks + 2) / 2), player.getBlockY() + 1, player.getBlockZ() + 2);
        player.teleportTo(middle.getX(), middle.getY(), middle.getZ());
        clearArea(level, startFloor, endFloor, 15);

        for (int x = startFloor.getX(); x <= endFloor.getX(); x++) {
            for (int z = startFloor.getZ(); z <= endFloor.getZ(); z++) {
                if (z == startFloor.getZ() + 4) {
                    level.setBlock(new BlockPos(x, startFloor.getY() + 1, z), Blocks.OAK_PLANKS.defaultBlockState(), 3);
                    blocksToSetToAirOnWin.add(new BlockPos(x, startFloor.getY() + 1, z));
                }
                if (z == startFloor.getZ() + 6) {
                    level.setBlock(new BlockPos(x, startFloor.getY() + 1, z), Blocks.OAK_PLANKS.defaultBlockState(), 3);
                    if (x == startFloor.getX() || x == endFloor.getX()) {
                        level.setBlock(new BlockPos(x, startFloor.getY() + 3, z), Blocks.OAK_PLANKS.defaultBlockState(), 3);
                        blocksToSetToAirOnWin.add(new BlockPos(x, startFloor.getY() + 3, z));
                    } else {
                        BlockPos newPos = new BlockPos(x, startFloor.getY() + 3, z);
                        if (!blockPosOfPlacedBlocks.contains(newPos)) {
                            blockPosOfPlacedBlocks.add(newPos); // Block Positions for the blocks the player has to place
                            blocksToSetToAirOnWin.add(newPos);
                        }
                    }
                    level.setBlock(new BlockPos(x, startFloor.getY() + 2, z), Blocks.OAK_PLANKS.defaultBlockState(), 3);
                    level.setBlock(new BlockPos(x, startFloor.getY() + 4, z), Blocks.OAK_PLANKS.defaultBlockState(), 3);
                    blocksToSetToAirOnWin.add(new BlockPos(x, startFloor.getY() + 2, z));
                    blocksToSetToAirOnWin.add(new BlockPos(x, startFloor.getY() + 4, z));
                    if (x == middle.getX()) {
                        level.setBlock(new BlockPos(x, startFloor.getY() + 2, z - 1), Blocks.STONE_BUTTON.defaultBlockState()
                                .setValue(ButtonBlock.FACING, Direction.NORTH)
                                .setValue(ButtonBlock.FACE, AttachFace.WALL), 3);
                        buttonPos = new BlockPos(x, startFloor.getY() + 2, z - 1);
                        blocksToSetToAirOnWin.add(buttonPos);
                    }
                }
                level.setBlock(new BlockPos(x, startFloor.getY(), z), Blocks.OAK_PLANKS.defaultBlockState(), 3);
            }
        }
        int wallHeight = 15;
        int minX = startFloor.getX() - 1;
        int maxX = endFloor.getX() + 1;
        int minZ = startFloor.getZ() - 1;
        int maxZ = endFloor.getZ() + 1;
        int y = startFloor.getY();

        for (int h = 0; h < wallHeight; h++) {
            // North and south walls
            for (int x = minX; x <= maxX; x++) {
                level.setBlock(new BlockPos(x, y + h, minZ), Blocks.OAK_PLANKS.defaultBlockState(), 3);
                level.setBlock(new BlockPos(x, y + h, maxZ), Blocks.OAK_PLANKS.defaultBlockState(), 3);
            }
            // East and west walls
            for (int z = minZ; z <= maxZ; z++) {
                level.setBlock(new BlockPos(minX, y + h, z), Blocks.OAK_PLANKS.defaultBlockState(), 3);
                level.setBlock(new BlockPos(maxX, y + h, z), Blocks.OAK_PLANKS.defaultBlockState(), 3);
            }
        }
        // Ceiling with lights
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                level.setBlock(new BlockPos(x, y + wallHeight, z), Blocks.OAK_PLANKS.defaultBlockState(), 3);
            }
        }

        // Corner lights
        level.setBlock(new BlockPos(minX + 1, y + wallHeight - 1, minZ + 1), Blocks.SEA_LANTERN.defaultBlockState(), 3);
        level.setBlock(new BlockPos(maxX - 1, y + wallHeight - 1, minZ + 1), Blocks.SEA_LANTERN.defaultBlockState(), 3);
        level.setBlock(new BlockPos(minX + 1, y + wallHeight - 1, maxZ - 1), Blocks.SEA_LANTERN.defaultBlockState(), 3);
        level.setBlock(new BlockPos(maxX - 1, y + wallHeight - 1, maxZ - 1), Blocks.SEA_LANTERN.defaultBlockState(), 3);
        level.setBlock(new BlockPos(minX + 1, y + 1, minZ + 1), Blocks.SEA_LANTERN.defaultBlockState(), 3);
        level.setBlock(new BlockPos(maxX - 1, y + 1, minZ + 1), Blocks.SEA_LANTERN.defaultBlockState(), 3);
        level.setBlock(new BlockPos(minX + 1, y + 1, maxZ - 1), Blocks.SEA_LANTERN.defaultBlockState(), 3);
        level.setBlock(new BlockPos(maxX - 1, y + 1, maxZ - 1), Blocks.SEA_LANTERN.defaultBlockState(), 3);

        // Now the building is done
        player.getInventory().clearContent();
        Collections.shuffle(useBlocks); // THE BLOCKS USED (in order)

        int x = 0;
        for (int i = startFloor.getX() + 1; i < endFloor.getX() + 1; i++) {
            if (x >= useBlocks.size()) break;
            level.setBlock(new BlockPos(i, startFloor.getY() + 1, startFloor.getZ() + 7), useBlocks.get(x).defaultBlockState(), 3);
            x++;
        }

        List<Block> playerBlocksGiven = new ArrayList<>(useBlocks);
        Collections.shuffle(playerBlocksGiven);
        for (Block block : playerBlocksGiven) {
            player.getInventory().add(new ItemStack(block.asItem()));
        }




        player.gameMode.changeGameModeForPlayer(GameType.CREATIVE);
    }
    @SubscribeEvent
    public static void onBlockInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide()) return;
        if (buttonPos == null) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!GameListener.isGameStarted) {
            return;
        }

        BlockPos pos = event.getPos();
        BlockState state = event.getLevel().getBlockState(pos);
        Level level = event.getLevel();

        if (state.getBlock() instanceof ButtonBlock && pos.equals(buttonPos)) {
            List<Block> playerGuess = new ArrayList<>();
            for (BlockPos blockPos : blockPosOfPlacedBlocks) {
                if (level.getBlockState(blockPos).getBlock() == Blocks.AIR) {
                    player.displayClientMessage(Component.literal("Must have all the spaces filled."), false);
                    return;
                }
                playerGuess.add(level.getBlockState(blockPos).getBlock());
            }
            guesses++;
            int correct = 0;
            for (int i = 0; i < playerGuess.toArray().length; i++) {
                if (playerGuess.get(i) == useBlocks.get(i)) {
                    correct++;
                }
            }
            if (correct == amountOfBlocks) {
                player.connection.send(new ClientboundSetTitleTextPacket(Component.literal("Congrats! You got it in " + guesses + " guesses.")));
                player.connection.send(new ClientboundSetTitlesAnimationPacket(10, 70, 20));
                // Delay the cleanup by 1 tick so the button press finishes first
                runDelayed(level, () -> {
                    for (BlockPos blockPos : blocksToSetToAirOnWin) {
                        level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
                    }

                    if (buttonPos != null) {
                        level.removeBlock(buttonPos, false);
                        buttonPos = null;
                    }

                    GameListener.isGameStarted = false;
                    removeNearbyStoneButtons(level, player, 10); // 10-block radius


                }, 10);


            } else {
                player.connection.send(new ClientboundSetTitleTextPacket(Component.literal("You have " + correct + " correct out of " + amountOfBlocks)));
                player.connection.send(new ClientboundSetTitlesAnimationPacket(10, 70, 20));
            }

        }
    }
    public static void runDelayed(Level level, Runnable task, int ticks) {
        MinecraftServer server = level.getServer();
        int targetTick = server.getTickCount() + ticks;

        server.execute(() -> {
            server.execute(() -> {
                if (server.getTickCount() >= targetTick) {
                    task.run();
                } else {
                    runDelayed(level, task, ticks - 1);
                }
            });
        });
    }

    public static void removeNearbyStoneButtons(Level level, Player player, double radius) {
        AABB box = new AABB(
                player.getX() - radius, player.getY() - radius, player.getZ() - radius,
                player.getX() + radius, player.getY() + radius, player.getZ() + radius
        );

        level.getEntitiesOfClass(ItemEntity.class, box, item ->
                item.getItem().is(Items.STONE_BUTTON)
        ).forEach(Entity::discard);
    }

    public static void clearArea(ServerLevel level, BlockPos start, BlockPos end, int height) {
        int minX = Math.min(start.getX(), end.getX());
        int maxX = Math.max(start.getX(), end.getX());
        int minY = start.getY();
        int maxY = start.getY() + height;
        int minZ = Math.min(start.getZ(), end.getZ());
        int maxZ = Math.max(start.getZ(), end.getZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    level.setBlock(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
                }
            }
        }
    }




}













