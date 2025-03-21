package com.xiaoshi2022.kamen_rider_weapon_craft.Item.prop.client.entity;

import com.xiaoshi2022.kamen_rider_weapon_craft.particle.ModParticles;
import com.xiaoshi2022.kamen_rider_weapon_craft.registry.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Random;

public class LaserBeamEntity extends Projectile {
    private static final EntityDataAccessor<ParticleOptions> DATA_PARTICLE_TYPE = SynchedEntityData.defineId(LaserBeamEntity.class, EntityDataSerializers.PARTICLE);
    private static final int MAX_LIFETIME = 20 * 20; // 激光束最大存活时间，这里设置为 20 秒
    private int lifetime = 0;
    public double damage; // 设置基础伤害
    private boolean isCritical = false; // 是否为暴击箭
    private float chargeTime; // 蓄力时间
    private final ItemStack weaponStack;

    public LaserBeamEntity(EntityType<? extends LaserBeamEntity> entityType, Level level, ItemStack weaponStack) {
        super(entityType, level);
        if (weaponStack == null) {
            throw new IllegalArgumentException("Weapon stack cannot be null");
        }
        this.weaponStack = weaponStack;
        this.damage = 9.0D;
        this.chargeTime = 0.0F;
        // 调整碰撞箱大小
        this.setBoundingBox(new AABB(-1.0, -1.0, -1.0, 1.5, 1.5, 1.5)); // 增大碰撞箱
    }

    public LaserBeamEntity(Level level, LivingEntity shooter, ParticleOptions particleType, float chargeTime, ItemStack weaponStack) {
        super(ModEntityTypes.LASER_BEAM.get(), level);
        if (weaponStack == null) {
            throw new IllegalArgumentException("Weapon stack cannot be null");
        }
        this.weaponStack = weaponStack;
        this.damage = 9.0D;
        this.chargeTime = chargeTime;
        this.entityData.set(DATA_PARTICLE_TYPE, particleType);
        this.setPos(shooter.getX(), shooter.getY() + shooter.getEyeHeight(), shooter.getZ());
        this.setOwner(shooter); // 确保设置 owner 属性
        // 调整碰撞箱大小
        this.setBoundingBox(new AABB(-1.0, -1.0, -1.0, 1.5, 1.5, 1.5)); // 增大碰撞箱
        this.adjustDamageBasedOnCharge();
        this.setCriticalChance(chargeTime);
    }
    public LaserBeamEntity(EntityType<? extends LaserBeamEntity> entityType, Level level) {
        super(entityType, level);
        this.weaponStack = ItemStack.EMPTY; // 默认值，确保不会为 null
    }

    public LaserBeamEntity(Level level, Player player) {
        this(ModEntityTypes.LASER_BEAM.get(), level, player.getMainHandItem()); // 使用主手物品作为武器堆栈
    }

    private void adjustDamageBasedOnCharge() {
        // 根据蓄力时间调整伤害
        this.damage += this.chargeTime * 0.6D; // 每蓄力1秒，伤害增加0.6
    }

    private void setCriticalChance(float chargeTime) {
        // 根据蓄力时间设置暴击几率
        Random random = new Random();
        float criticalChance = chargeTime * 0.1F; // 每蓄力1秒，暴击几率增加10%
        if (random.nextFloat() < criticalChance) {
            this.isCritical = true;
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity target = result.getEntity();

        if (target instanceof LivingEntity livingEntity) {
            // 检查是否有火矢附魔
            if (this.isOnFire()) {
                livingEntity.setSecondsOnFire(5); // 使生物着火5秒
            }

            // 检查是否有击退附魔
            if (this.weaponStack != null && this.weaponStack.isEnchanted() && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, this.weaponStack) > 0) {
                int punchLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, this.weaponStack);
                float knockbackStrength = 0.5F * punchLevel; // 每级击退附魔增加0.5的击退强度

                // 计算击退方向
                if (this.getOwner() != null) {
                    double dx = this.getOwner().getX() - livingEntity.getX();
                    double dz = this.getOwner().getZ() - livingEntity.getZ();

                    // 标准化击退方向向量
                    double length = Math.sqrt(dx * dx + dz * dz);
                    if (length > 0) {
                        dx /= length;
                        dz /= length;
                    }

                    // 应用击退效果
                    livingEntity.knockback(knockbackStrength, dx, dz);
                }
            }

            // 检查是否有引雷附魔
            if (this.weaponStack != null && this.weaponStack.isEnchanted()) {
                Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(this.weaponStack);
                if (((java.util.Map<?, ?>) enchantments).containsKey(Enchantments.CHANNELING) && enchantments.get(Enchantments.CHANNELING) > 0) {
                    // 检查是否处于雷暴天气
                    if (this.level().isThundering()) {
                        // 检查目标实体是否处于露天位置
                        if (this.level().canSeeSky(new BlockPos(livingEntity.blockPosition()))) {
                            // 在目标位置生成闪电
                            this.level().addParticle(ParticleTypes.LARGE_SMOKE, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 0, 0, 0);
                            LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, this.level());
                            lightningBolt.setPos(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                            this.level().addFreshEntity(lightningBolt);
                        }
                    }
                }
            }


            // 确定伤害来源
            DamageSource damageSource;
            if (this.getOwner() instanceof Player player) {
                // 如果激光束是由玩家发射的，使用玩家攻击伤害源
                damageSource = player.damageSources().playerAttack(player);
            } else {
                // 否则使用通用伤害源
                damageSource = damageSources().generic();
            }

            // 对目标实体造成基础伤害
            float damageAmount = (float) this.damage;
            if (this.isCritical) {
                damageAmount *= 1.5F; // 暴击箭增加50%伤害
            }

            // 对目标实体造成伤害
            livingEntity.hurt(damageSource, damageAmount);
        }

        this.discard(); // 击中实体后移除激光束
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);

        // 在击中方块时移除激光束
        this.discard();

        // 移除相关的粒子效果
        if (this.level().isClientSide) {
            ParticleOptions particleType = this.entityData.get(DATA_PARTICLE_TYPE);
            Vec3 hitPos = result.getLocation();
            this.level().addParticle(particleType, hitPos.x, hitPos.y, hitPos.z, 0, 0, 0);
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_PARTICLE_TYPE, ParticleTypes.FLAME);
    }

    @Override
    public void tick() {
        super.tick();
        lifetime++;
        if (lifetime >= MAX_LIFETIME) {
            this.discard(); // 达到最大存活时间后移除激光束
            return;
        }

        // 获取激光束的当前位置和运动方向
        Vec3 motion = this.getDeltaMovement();
        Vec3 nextPos = this.position().add(motion);

        // 检测激光束是否击中方块
        BlockHitResult blockHitResult = this.level().clip(new ClipContext(this.position(), nextPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            this.onHitBlock(blockHitResult); // 调用击中方块的处理逻辑
            return;
        }

        // 检测激光束是否击中实体
        AABB aabb = this.getBoundingBox().move(motion).inflate(1.0D, 1.0D, 1.0D);
        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(this.level(), this, this.position(), nextPos, aabb, entity -> !entity.isSpectator() && entity.isPickable());
        if (entityHitResult != null) {
            this.onHitEntity(entityHitResult); // 调用击中实体的处理逻辑
            return;
        }

        // 如果没有击中任何东西，继续移动
        this.setPos(nextPos.x, nextPos.y, nextPos.z);

        // 在客户端侧生成粒子效果
        if (this.level().isClientSide) {
            ParticleOptions particleType = this.entityData.get(DATA_PARTICLE_TYPE);
            for (int i = 0; i < 15; i++) { // 增加粒子数量
                double offsetX = motion.x * i * 0.1;
                double offsetY = motion.y * i * 0.1;
                double offsetZ = motion.z * i * 0.1;
                this.level().addParticle(particleType, this.getX() + offsetX, this.getY() + offsetY, this.getZ() + offsetZ, 0, 0, 0);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putDouble("baseDamage", this.damage);
        tag.putBoolean("isCritical", this.isCritical);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.damage = tag.getDouble("baseDamage");
        this.isCritical = tag.getBoolean("isCritical");
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return super.canHitEntity(entity) && entity != this.getOwner();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result); // 确保调用父类方法
    }
}