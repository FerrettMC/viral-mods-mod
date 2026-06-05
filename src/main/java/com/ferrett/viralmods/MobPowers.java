package com.ferrett.viralmods;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber
public class MobPowers {
    static Set<UUID> wasSneaking = new HashSet<>();
    static int tick = 0;
    static int explosionCooldown = 0;
    record PlayerPower(UUID uuid, ServerPlayer player, String power) {}
    public static Boolean mobPowers = false;
    public static List<PlayerPower> powers = new ArrayList<>();
    @SubscribeEvent
    public static void onMobKill(LivingDeathEvent event) {
        if (!mobPowers) return;
        if (event.getEntity() instanceof ServerPlayer) return; // ignore player deaths
        GameListener.isGameStarted = true;

        Player killer = event.getSource().getEntity() instanceof Player p ? p : null;
        if (killer == null) return;
        LivingEntity mob = event.getEntity();
        String mobName = mob.getType().toShortString();

        switch (mobName) {
            case "enderman" -> {
                killer.displayClientMessage(Component.literal("You have gotten the enderman power!"), false);
                powers.removeIf(pp -> pp.player().equals(killer));
                powers.add(new PlayerPower(killer.getUUID(), (ServerPlayer) killer, "enderman"));
            }
            case "iron_golem" -> {
                killer.displayClientMessage(Component.literal("You have gotten the iron golem power!"), false);
                powers.removeIf(pp -> pp.player().equals(killer));
                powers.add(new PlayerPower(killer.getUUID(), (ServerPlayer) killer, "iron_golem"));
            }
            case "bat", "vex" -> {
                killer.displayClientMessage(Component.literal("You have gotten the flying power!"), false);
                powers.removeIf(pp -> pp.player().equals(killer));
                powers.add(new PlayerPower(killer.getUUID(), (ServerPlayer) killer, "fly"));
            }
            case "creeper" -> {
                killer.displayClientMessage(Component.literal("You have gotten the creeper power!"), false);
                powers.removeIf(pp -> pp.player().equals(killer));
                powers.add(new PlayerPower(killer.getUUID(), (ServerPlayer) killer, "creeper"));
            }
        }


    }
    @SubscribeEvent
    public static void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        if (!event.getEntity().getMainHandItem().isEmpty()) return;
        doTeleport(event.getEntity());
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getEntity().getMainHandItem().isEmpty()) return;
        doTeleport(event.getEntity());
    }

    private static void doTeleport(Player player) {
        var start = player.getEyePosition();
        var end = start.add(player.getLookAngle().scale(50));

        PlayerPower found = powers.stream()
        .filter(pp -> pp.uuid().equals(player.getUUID()))
        .findFirst()
        .orElse(null);
        if (found == null) return;
        if (!Objects.equals(found.power, "enderman")) return;

        ServerPlayer serverPlayer = found.player();

        HitResult hit = serverPlayer.level().clip(new ClipContext(
                start, end,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                serverPlayer
        ));

        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) hit;
            BlockPos pos = blockHit.getBlockPos();
            BlockPos above = pos.above();
            boolean isAir = serverPlayer.level().getBlockState(above).isAir();
            if (isAir) {
                serverPlayer.teleportTo(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            } else {
                int dx = start.x > pos.getX() ? 1 : -1;
                int dz = start.z > pos.getZ() ? 1 : -1;
                serverPlayer.teleportTo(pos.getX() + 0.5 + dx, pos.getY() + 1, pos.getZ() + 0.5 + dz);
            }
        } else {
            serverPlayer.teleportTo(end.x, end.y, end.z);
        }
    }
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent.Post event) {
        if (explosionCooldown > 0) explosionCooldown--;

        // in tick event, replace the creeper block:
        for (PlayerPower pp : powers) {
            if (!pp.power().equals("creeper")) continue;
            ServerPlayer player = pp.player();
            boolean sneakingNow = player.isCrouching();
            boolean wasSneakingBefore = wasSneaking.contains(player.getUUID());

            if (sneakingNow && !wasSneakingBefore && explosionCooldown == 0) {
                player.level().explode(null, player.getX(), player.getY(), player.getZ(),
                        10f, false, Level.ExplosionInteraction.TNT);
                explosionCooldown = 100;
            }

            if (sneakingNow) {
                wasSneaking.add(player.getUUID());
            } else {
                wasSneaking.remove(player.getUUID());
            }
        }
        tick++;
        if (tick % 2 != 0) return;
        tick = 0;
        for (PlayerPower player : powers) {
            if (player.power.equals("iron_golem")) {
                player.player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(80);
                player.player.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(6);
                player.player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.03);
                player.player.getAbilities().mayfly = false;
                player.player.getAbilities().flying = false;
                player.player.onUpdateAbilities();
            } else {

                player.player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20);
                player.player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.1);
                player.player.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(2);
                if (player.power.equals("fly")) {
                    player.player.getAbilities().mayfly = true;
                    player.player.getAbilities().flying = true;
                    player.player.onUpdateAbilities(); // sync to client
                } else {
                    player.player.getAbilities().mayfly = false;
                    player.player.getAbilities().flying = false;
                    player.player.onUpdateAbilities();
                }
            }
        }

    }

}
