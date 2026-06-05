package com.ferrett.viralmods;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber
public class MobHideAndSeek {
    public static boolean mobHideAndSeek = false;
    public static List<ServerPlayer> players = null;
    public static ServerLevel level = null;
    public static void startHideAndSeek(List<ServerPlayer> ps, ServerLevel l) {
        players = ps;
        mobHideAndSeek = true;
        level = l;
    }
    @SubscribeEvent
    public static void onPlayerChat(ServerChatEvent event) {
        if (!mobHideAndSeek || players.stream().noneMatch(p -> p.getUUID().equals(event.getPlayer().getUUID()))) {
            return;
        }
        if (event.getMessage().getString().equalsIgnoreCase("animals")) {
            event.getPlayer().displayClientMessage(Component.literal("(Type self to reset) Allowed animals: Sheep, Cow, Zombie, Creeper, Bat, Pig, Villager, Witch"), false);
            event.setMessage(Component.literal(""));
        }
        if (event.getMessage().getString().equalsIgnoreCase("self")) {
            event.getPlayer().getAbilities().flying = false;
            ServerPlayer player = event.getPlayer();
            playerMobs.removeIf(pm -> {
                if (pm.uuid().equals(player.getUUID())) {
                    pm.mob().discard();
                    return true;
                }
                return false;
            });
            player.setInvisible(false);
            player.getAttribute(Attributes.SCALE).setBaseValue(1);


        }
        String msg = event.getMessage().getString();

        switch (msg.toLowerCase()) {
            case "sheep" -> {
                spawnMob(event.getPlayer(), new Sheep(EntityType.SHEEP, event.getPlayer().level()) {
                    @Override public boolean isPushable() { return false; }
                    @Override public void push(Entity entity) {}
                }, 0.8);
                event.getPlayer().getAbilities().flying = false;
            }
            case "cow" -> {
                spawnMob(event.getPlayer(), new net.minecraft.world.entity.animal.cow.Cow(EntityType.COW, event.getPlayer().level()) {
                    @Override public boolean isPushable() { return false; }
                    @Override public void push(Entity entity) {}
                }, 0.8);
                event.getPlayer().getAbilities().flying = false;
            }
            case "pig" -> {
                spawnMob(event.getPlayer(), new net.minecraft.world.entity.animal.pig.Pig(EntityType.PIG, event.getPlayer().level()) {
                    @Override public boolean isPushable() { return false; }
                    @Override public void push(Entity entity) {}
                }, 0.6);
                event.getPlayer().getAbilities().flying = false;
            }
            case "bat" -> {
                spawnMob(event.getPlayer(), new net.minecraft.world.entity.ambient.Bat(EntityType.BAT, event.getPlayer().level()) {
                    @Override public boolean isPushable() { return false; }
                    @Override public void push(Entity entity) {}
                }, 0.2);
                event.getPlayer().getAbilities().flying = true;
            }
            case "zombie" -> {
                spawnMob(event.getPlayer(), new net.minecraft.world.entity.monster.zombie.Zombie(EntityType.ZOMBIE, event.getPlayer().level()) {
                    @Override public boolean isPushable() { return false; }
                    @Override public void push(Entity entity) {}
                }, 1.0);
                event.getPlayer().getAbilities().flying = false;
            }
            case "creeper" -> {
                spawnMob(event.getPlayer(), new net.minecraft.world.entity.monster.Creeper(EntityType.CREEPER, event.getPlayer().level()) {
                    @Override public boolean isPushable() { return false; }
                    @Override public void push(Entity entity) {}
                }, 0.8);
                event.getPlayer().getAbilities().flying = false;
            }
            case "villager" -> {
                spawnMob(event.getPlayer(), new net.minecraft.world.entity.npc.villager.Villager(EntityType.VILLAGER, event.getPlayer().level()) {
                    @Override public boolean isPushable() { return false; }
                    @Override public void push(Entity entity) {}
                }, 1);
                event.getPlayer().getAbilities().flying = false;
            }
            case "witch" -> {
                spawnMob(event.getPlayer(), new net.minecraft.world.entity.monster.Witch(EntityType.WITCH, event.getPlayer().level()) {
                    @Override public boolean isPushable() { return false; }
                    @Override public void push(Entity entity) {}
                }, 1);
                event.getPlayer().getAbilities().flying = false;
            }
        }
    }

    private static void spawnMob(ServerPlayer player, Entity mob, double scale) {
        playerMobs.removeIf(pm -> {
            if (pm.uuid().equals(player.getUUID())) {
                pm.mob().discard();
                return true;
            }
            return false;
        });
        player.setInvisible(true);
        player.getAttribute(Attributes.SCALE).setBaseValue(scale);
        mob.setInvulnerable(true);
        mob.noPhysics = true;
        mob.teleportTo(player.getX(), player.getY(), player.getZ());
        player.level().addFreshEntity(mob);
        playerMobs.add(new PlayerMob(player.getUUID(), mob));
    }
    record PlayerMob(UUID uuid, Entity mob) {}
    public static List<PlayerMob> playerMobs = new ArrayList<>();

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent.Post event) {
        if (!mobHideAndSeek) return;
        for (PlayerMob pm : playerMobs) {
            for (ServerPlayer player : players) {
                if (player.getUUID().equals(pm.uuid())) {
                    pm.mob().noPhysics = true;
                    pm.mob().teleportTo(player.getX() + 0.7, player.getY(), player.getZ());
                    pm.mob().setYRot(player.getYRot());
                    pm.mob().setXRot(player.getXRot());
                    pm.mob().yRotO = player.getYRot();
                    pm.mob().xRotO = player.getXRot();
                    if (pm.mob() instanceof LivingEntity living) {
                        living.yHeadRot = player.getYRot();
                        living.yBodyRot = player.getYRot();
                    }
                }
            }
        }
    }




}
