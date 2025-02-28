package org.mousejava.modid.init;

import net.minecraft.world.item.Item;
import org.mousejava.modid.item.WandOfLightningItem;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.mousejava.modid.ModId;

@SimplyRegister
public interface ItemsMI {
	@RegistryName("wand_of_lightning")
	Item WAND_OF_LIGHTNING = ModId.MOD_TAB.add(new WandOfLightningItem(new Item.Properties().durability(16)));
}
