package org.mousejava.modid.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import org.mousejava.modid.block.entity.LightningGeneratorTile;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.hammerlib.api.forge.BlockAPI;

@SimplyRegister
public interface TilesMI {
    @RegistryName("lightning_generator")
    BlockEntityType<LightningGeneratorTile> LIGHTNING_GENERATOR_TILE = BlockAPI.createBlockEntityType(LightningGeneratorTile::new, BlocksMI.LIGHTNING_GENERATOR);
}
