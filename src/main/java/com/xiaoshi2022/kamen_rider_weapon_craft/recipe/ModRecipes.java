package com.xiaoshi2022.kamen_rider_weapon_craft.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import com.xiaoshi2022.kamen_rider_weapon_craft.kamen_rider_weapon_craft;

/**
 * ModRecipes 负责注册自定义配方的 RecipeType 和 RecipeSerializer。
 */
public class ModRecipes {
    public static final ResourceLocation RIDER_FUSION_RECIPE_ID = new ResourceLocation(kamen_rider_weapon_craft.MOD_ID, "rider_fusion_recipe");

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, kamen_rider_weapon_craft.MOD_ID);

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, kamen_rider_weapon_craft.MOD_ID);

    public static final RegistryObject<RecipeType<RiderFusionRecipe>> RIDER_FUSION_RECIPE =
            RECIPE_TYPES.register("rider_fusion_recipe", () -> new RecipeType<RiderFusionRecipe>() {});

    public static final RegistryObject<RecipeSerializer<RiderFusionRecipe>> RIDER_FUSION_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("rider_fusion_recipe", RiderFusionRecipe.Serializer::new);

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
    }
}