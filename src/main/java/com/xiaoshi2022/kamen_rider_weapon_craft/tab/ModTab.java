package com.xiaoshi2022.kamen_rider_weapon_craft.tab;

import com.xiaoshi2022.kamen_rider_weapon_craft.Item.custom.prop.arrowx.AonicxEntity;
import com.xiaoshi2022.kamen_rider_weapon_craft.Item.prop.items.AonicxItem;
import com.xiaoshi2022.kamen_rider_weapon_craft.kamen_rider_weapon_craft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.xiaoshi2022.kamen_rider_weapon_craft.registry.ModItems.*;

public class ModTab {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, kamen_rider_weapon_craft.MOD_ID);

    public static final RegistryObject<CreativeModeTab> TEST_TAB = TABS.register("kamen_rider_weapon_craft_tab", () -> CreativeModeTab.builder()
            .icon(() -> SONICARROW.get().getDefaultInstance())
            .title(Component.translatable("item group.kamen_rider_weapon_craft_tab"))
            .displayItems(((parameters, output) -> {
                output.accept(SONICARROW.get());
                output.accept(DAIDAIMARU.get());
                output.accept(MUSOUSABERD.get());
                output.accept(GANGUNSABER.get());
                output.accept(RIDEBOOKER.get());
            })).build());
    public static final RegistryObject<CreativeModeTab> T_TAB = TABS.register("kamen_rider_weapon_craft_prop_tab", () -> CreativeModeTab.builder()
            .icon(() -> MELON.get().getDefaultInstance())
            .title(Component.translatable("item group.kamen_rider_weapon_craft_prop_tab"))
            .displayItems(((parameters, output) -> {
                output.accept(MELON.get());
            })).build());
}