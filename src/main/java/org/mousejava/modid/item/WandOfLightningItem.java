package org.mousejava.modid.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.mousejava.modid.entity.LightningProjectileEntity;

public class WandOfLightningItem extends Item {
    public WandOfLightningItem(Properties properties) {
        super(properties);
    }

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
}
