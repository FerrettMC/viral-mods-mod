package com.ferrett.viralmods;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class MobHideAndSeek {
    public static boolean mobHideAndSeek = false;
    public static List<ServerPlayer> players = null;
    public static ServerLevel level = null;
    public static void startHideAndSeek(List<ServerPlayer> ps, ServerLevel l) {
        players = ps;
        mobHideAndSeek = true;
        level = l;
    }
}
