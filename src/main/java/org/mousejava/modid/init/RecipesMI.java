package org.mousejava.modid.init;

import net.minecraft.world.item.Items;
import org.zeith.hammerlib.annotations.ProvideRecipes;
import org.zeith.hammerlib.api.IRecipeProvider;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;

@ProvideRecipes
public class RecipesMI implements IRecipeProvider {
    @Override
    public void provideRecipes(RegisterRecipesEvent event) {
        event.shaped()
                .result(ItemsMI.WAND_OF_LIGHTNING)
                .shape("  a", " g ", "s  ")
                .map('a', Items.AMETHYST_SHARD)
                .map('g', Items.GOLD_NUGGET)
                .map('s', Items.STICK)
                .register();
    }
}
