package com.xiaoshi2022.kamen_rider_weapon_craft.Item.client.musouhinawadj;

import com.xiaoshi2022.kamen_rider_weapon_craft.Item.custom.musouhinawadj;
import com.xiaoshi2022.kamen_rider_weapon_craft.kamen_rider_weapon_craft;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class musouhinawadjRenderer extends GeoItemRenderer<musouhinawadj> {
    public musouhinawadjRenderer() {
        super(new musouhinawadjModel<>(new ResourceLocation(kamen_rider_weapon_craft.MOD_ID,"musouhinawadj")));
    }
}
