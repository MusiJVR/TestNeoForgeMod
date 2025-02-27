package org.mousejava.modid.init;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.mousejava.modid.ModId;
import org.mousejava.modid.entity.LightningProjectileEntity;

@SimplyRegister
public interface ItemsMI {
	@RegistryName("wand_of_lightning")
	Item WAND_OF_LIGHTNING = ModId.MOD_TAB.add(new Item(new Item.Properties().durability(16)) {
		@Override
		public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
			ItemStack itemStack = player.getItemInHand(hand);
			if (!level.isClientSide) {
				LightningProjectileEntity projectile = new LightningProjectileEntity(level, player);
				level.addFreshEntity(projectile);
				if (!player.isCreative()) {
					itemStack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
				}
			}
			return InteractionResultHolder.success(player.getItemInHand(hand));
		}
	});
}
