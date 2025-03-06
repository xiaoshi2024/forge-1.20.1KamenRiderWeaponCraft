package com.xiaoshi2022.kamen_rider_weapon_craft.Item.custom.food;

import com.xiaoshi2022.kamen_rider_weapon_craft.Item.food.HelheimFruit.HelheimFruitRenderer;
import com.xiaoshi2022.kamen_rider_weapon_craft.registry.EffectInit;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class HelheimFruit extends Item implements GeoItem {
    private static final RawAnimation OPEN = RawAnimation.begin().thenPlay("open");
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public HelheimFruit(Properties properties) {
        super(properties.food(new FoodProperties.Builder()
                .nutrition(4) // 饱腹值
                .saturationMod(0.3f) // 饱和度
                .build()));
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            // 自定义渲染器
            private BlockEntityWithoutLevelRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new HelheimFruitRenderer();
                }
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
        controllers.add(new AnimationController<>(this, "open", 2, state -> PlayState.STOP)
                .triggerableAnim("open", OPEN)
                .setSoundKeyframeHandler(state -> {
                }));
    }

    private PlayState predicate(AnimationState<HelheimFruit> event) {
        // 设置默认动画逻辑
        event.getController().setAnimation(IDLE);
        return PlayState.CONTINUE;
    }

    // 在玩家食用时播放动画
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level instanceof ServerLevel serverLevel)
            triggerAnim(player, GeoItem.getOrAssignId(player.getItemInHand(hand), serverLevel), "open", "open");

        return super.use(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        // 确保只在服务器端执行逻辑
        if (!level.isClientSide && entity instanceof Player player) {
            // 30% 概率获得赫尔海姆之力（HELMHEIM_POWER）buff
            if (player.getRandom().nextInt(100) < 60) { // 60% 概率
                player.addEffect(new MobEffectInstance(EffectInit.HELMHEIM_POWER.get(), 30 * 20, 0, false, false));
            }
        }
        // 调用父类方法以确保物品的默认行为（例如消耗物品）
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}