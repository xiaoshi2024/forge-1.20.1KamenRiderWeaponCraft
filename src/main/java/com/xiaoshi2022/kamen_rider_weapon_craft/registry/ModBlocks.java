package com.xiaoshi2022.kamen_rider_weapon_craft.registry;

import com.xiaoshi2022.kamen_rider_weapon_craft.blocks.portals.RiderFusionMachineBlock;
import com.xiaoshi2022.kamen_rider_weapon_craft.blocks.portals.RiderforgingalloymineralBlock;
import com.xiaoshi2022.kamen_rider_weapon_craft.blocks.portals.Time_traveler_studio_block;
import com.xiaoshi2022.kamen_rider_weapon_craft.blocks.portals.helheim_crack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import com.xiaoshi2022.kamen_rider_weapon_craft.blocks.portals.plant.HelheimVine;

public class ModBlocks {
    // 创建一个 DeferredRegister 实例用于注册方块
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "kamen_rider_weapon_craft");

    // 注册一个方块
    public static final RegistryObject<helheim_crack> HELHEIM_CRACK_BLOCK = BLOCKS.register("helheim_crack_block",
            () -> new helheim_crack(Block.Properties.of().strength(1.5f, 6.0f).noCollission()));

    public static final RegistryObject<Time_traveler_studio_block> TIME_TRAVELER_STUDIO_BLOCK = BLOCKS.register("time_traveler_studio_block",
            () -> new Time_traveler_studio_block(Block.Properties.of().strength(1.5f, 6.0f).noCollission()));

    public static final RegistryObject<Block> RIDER_FUSION_MACHINE_BLOCK = BLOCKS.register(
            "rider_fusion_machine_block", RiderFusionMachineBlock::new);

    public static final RegistryObject<Block> RIDERFORGINGALLOYMINERAL = BLOCKS.register("riderforgingalloymineral", RiderforgingalloymineralBlock::new);

    public static final RegistryObject<HelheimVine> HELHEIMVINE = BLOCKS.register("helheimvine",
            () -> new HelheimVine(Block.Properties.of().noCollission().randomTicks().instabreak()));

}