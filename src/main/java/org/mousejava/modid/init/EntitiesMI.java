package org.mousejava.modid.init;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.mousejava.modid.entity.LightningProjectileEntity;

@SimplyRegister
public interface EntitiesMI {
    @RegistryName("lightning_projectile")
    EntityType<LightningProjectileEntity> LIGHTNING_PROJECTILE = EntityType.Builder
            .<LightningProjectileEntity>of(LightningProjectileEntity::new, MobCategory.MISC)
            .sized(0.5F, 0.5F)
            .build("lightning_projectile");
}
