package com.xiaoshi2022.kamen_rider_weapon_craft.Item.client.progrise_hopper_blade;

import com.xiaoshi2022.kamen_rider_weapon_craft.Item.client.progrise_hopper_blade.progrise_hopper_bladeModel;
import com.xiaoshi2022.kamen_rider_weapon_craft.kamen_rider_weapon_craft;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.DefaultedGeoModel;

public class progrise_hopper_bladeModel <T extends GeoAnimatable> extends DefaultedGeoModel<T> {
    /**
     * Create a new instance of this model class.<br>
     * The asset path should be the truncated relative path from the base folder.<br>
     * E.G.
     * <pre>{@code
     * 	new ResourceLocation("myMod", "armor/obsidian")
     * }</pre>
     */
    public progrise_hopper_bladeModel(ResourceLocation assetSubpath) {
        super(assetSubpath);
    }

    @Override
    protected String subtype() {
        return "item";
    }

    /**
     * Changes the constructor-defined model path for this model to an alternate.<br>
     * This is useful if your animatable shares a model path with another animatable that differs in path to the texture and animations for this model
     */
    @Override
    public progrise_hopper_bladeModel<T> withAltModel(ResourceLocation altPath) {
        return (progrise_hopper_bladeModel<T>)super.withAltModel(new ResourceLocation(kamen_rider_weapon_craft.MOD_ID,"geo/item/progrise_hopper_blade.geo.json"));
    }

    /**
     * Changes the constructor-defined animations path for this model to an alternate.<br>
     * This is useful if your animatable shares an animations path with another animatable that differs in path to the model and texture for this model
     */
    @Override
    public progrise_hopper_bladeModel<T> withAltAnimations(ResourceLocation altPath) {
        return (progrise_hopper_bladeModel<T>)super.withAltAnimations(new ResourceLocation(kamen_rider_weapon_craft.MOD_ID,"animations/item/progrise_hopper_blade.animation.json"));
    }

    /**
     * Changes the constructor-defined texture path for this model to an alternate.<br>
     * This is useful if your animatable shares a texture path with another animatable that differs in path to the model and animations for this model
     */
    @Override
    public progrise_hopper_bladeModel<T> withAltTexture(ResourceLocation altPath) {
        return (progrise_hopper_bladeModel<T>)super.withAltTexture(new ResourceLocation(kamen_rider_weapon_craft.MOD_ID,"textures/item/progrise_hopper_blade.png"));
    }
}
