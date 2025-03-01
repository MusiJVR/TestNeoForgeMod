package org.mousejava.modid.init;

import org.mousejava.modid.ModId;
import org.mousejava.modid.block.LightningGeneratorBlock;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister
public interface BlocksMI {
    @RegistryName("lightning_generator")
    LightningGeneratorBlock LIGHTNING_GENERATOR = ModId.MOD_TAB.add(new LightningGeneratorBlock());
}
