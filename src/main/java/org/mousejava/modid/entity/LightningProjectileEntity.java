package org.mousejava.modid.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import org.mousejava.modid.init.EntitiesMI;

public class LightningProjectileEntity extends Projectile {
    public LightningProjectileEntity(EntityType<? extends LightningProjectileEntity> type, Level level) {
        super(type, level);
    }

    public LightningProjectileEntity(Level level, LivingEntity shooter) {
        super(EntitiesMI.LIGHTNING_PROJECTILE, level);
        this.setOwner(shooter);

        Vec3 look = shooter.getLookAngle();
        this.setPos(shooter.getX() + look.x, shooter.getEyeY(), shooter.getZ() + look.z);

        this.setDeltaMovement(look.scale(1.5));
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    public void tick() {
        super.tick();

        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, this.getX(), this.getY(), this.getZ(),
                    2, 0.1, 0.1, 0.1, 0);
        }

        Vec3 deltaMovement = this.getDeltaMovement();
        this.move(MoverType.SELF, deltaMovement);

        HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);

        if (hitResult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, hitResult)) {
            this.onHit(hitResult);
        }

        if (this.tickCount > 200 || this.isInWater()) {
            this.discard();
        }
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return !(entity instanceof LightningProjectileEntity);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide) {
            Level level = this.level();

            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
            if (lightningBolt != null) {
                lightningBolt.moveTo(position());
                level.addFreshEntity(lightningBolt);
            }

            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            Level level = this.level();
            Entity entity = result.getEntity();
            entity.hurt(level.damageSources().lightningBolt(), 5.0F);

            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
            if (lightningBolt != null) {
                lightningBolt.moveTo(entity.position());
                level.addFreshEntity(lightningBolt);
            }

            this.discard();
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
