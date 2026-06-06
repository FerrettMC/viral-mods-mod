package com.ferrett.viralmods;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class OneBlock {
    public static boolean oneBlock = false;
    private static Block block = null;
    private static ServerPlayer player = null;
    private static ServerLevel level = null;
    public static void startOneBlock(ServerPlayer p, ServerLevel l, Block b) {
        block = b;
        level = l;
        player = p;
        oneBlock = true;
    }
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent.Post event) {
        if (!oneBlock) return;
        if (!player.onGround()) return;
        BlockPos playerFeetPos = player.blockPosition();
        BlockPos playerHeadPos = player.blockPosition().above();
        BlockPos below = player.blockPosition().below();
        // block.getBlock().getName()
        BlockState belowBlock = player.level().getBlockState(below);
        BlockState feetBlock = player.level().getBlockState(playerFeetPos);
        BlockState headBlock = player.level().getBlockState(playerHeadPos);

        if (!headBlock.is(block) || !feetBlock.is(block)) {
            if (!headBlock.is(Blocks.AIR) || !feetBlock.is(Blocks.AIR)) {
                player.kill((ServerLevel) player.level());
                oneBlock = false;
                player.displayClientMessage(Component.literal("YOU LOSE!!"), false);
            }
        }


        if (!belowBlock.is(block)) {
            if (belowBlock.is(Blocks.AIR)) return;
            player.kill((ServerLevel) player.level());
            oneBlock = false;
            player.displayClientMessage(Component.literal("YOU LOSE!!"), false);
        }
    }
}
