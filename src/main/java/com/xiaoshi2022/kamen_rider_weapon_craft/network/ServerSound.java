package com.xiaoshi2022.kamen_rider_weapon_craft.network;

import com.xiaoshi2022.kamen_rider_weapon_craft.Item.custom.sonicarrow;
import com.xiaoshi2022.kamen_rider_weapon_craft.procedures.PullSounds;
import com.xiaoshi2022.kamen_rider_weapon_craft.procedures.SonicarrowBoot;
import com.xiaoshi2022.kamen_rider_weapon_craft.registry.ModSounds;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class ServerSound {

    public enum SoundType {
        START_STANDBY,
        STOP_STANDBY,
        BOOT
    }

    private static boolean isPlayingStandbySound = false; // 标识符，控制 pull_standby 音效播放

    private final SoundType soundType;

    public ServerSound(SoundType soundType) {
        this.soundType = soundType;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeEnum(soundType);
    }

    public static ServerSound decode(FriendlyByteBuf buffer) {
        return new ServerSound(buffer.readEnum(SoundType.class));
    }

    public static void handle(ServerSound message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                switch (message.soundType) {
                    case START_STANDBY:
                        isPlayingStandbySound = true; // 设置标识符为 true
                        break;
                    case STOP_STANDBY:
                        isPlayingStandbySound = false; // 设置标识符为 false
                        break;
                    case BOOT:
                        if (isHoldingSonicArrow(player)) {
                            // 调用 SonicarrowBoot 类中的方法播放 sonicarrow_boot 音效
                            SonicarrowBoot.playSonicarrowBootSound(player);
                        }
                        break;
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    private static boolean isHoldingSonicArrow(Player player) {
        return player.getMainHandItem().getItem() instanceof sonicarrow ||
                player.getOffhandItem().getItem() instanceof sonicarrow;
    }

    public static boolean isPlayingStandbySound() {
        return isPlayingStandbySound;
    }

    public static void sendToServer(ServerSound message) {
        NetworkHandler.INSTANCE.sendToServer(message);
    }
}