package com.xiaoshi2022.kamen_rider_weapon_craft.Item.client.musousaberd;

import com.xiaoshi2022.kamen_rider_weapon_craft.Item.custom.musousaberd;
import com.xiaoshi2022.kamen_rider_weapon_craft.kamen_rider_weapon_craft;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class musousaberdRenderer extends GeoItemRenderer<musousaberd> {
    public musousaberdRenderer() {
        super(new musousaberdModel<>(new ResourceLocation(kamen_rider_weapon_craft.MOD_ID,"musousaberd")));
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}

