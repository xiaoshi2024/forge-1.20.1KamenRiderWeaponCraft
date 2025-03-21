package com.xiaoshi2022.kamen_rider_weapon_craft.procedures;

import com.xiaoshi2022.kamen_rider_weapon_craft.Item.custom.HinawaDaidai_DJ_Ju;
import com.xiaoshi2022.kamen_rider_weapon_craft.Item.custom.sonicarrow;
import com.xiaoshi2022.kamen_rider_weapon_craft.registry.ModSounds;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PullSounds {
    private static final int SOUND_INTERVAL = 20; // 1秒 = 20 ticks
    private static long lastPlayedTime = 0;

    public static void playPullStandbySound(ServerPlayer player) {
        Level level = player.level();
        long currentTime = level.getGameTime();
        if (currentTime - lastPlayedTime >= SOUND_INTERVAL) {
            lastPlayedTime = currentTime;

            if (player.isUsingItem() && player.getUseItem().getItem() instanceof sonicarrow) {
                // 在服务器端播放音效
                player.playSound(ModSounds.PULL_STANDBY.get(),  1.0F, 1.0F);
            }
        }
    }
    public static void playPullStandbyDJSound(ServerPlayer player) {
        Level level = player.level();
        long currentTime = level.getGameTime();
        if (currentTime - lastPlayedTime >= SOUND_INTERVAL) {
            lastPlayedTime = currentTime;

            if (player.isUsingItem() && player.getUseItem().getItem() instanceof HinawaDaidai_DJ_Ju) {
                // 在服务器端播放音效
                player.playSound(ModSounds.DJ_DISC.get(),  1.0F, 1.0F);
            }
        }
    }
}