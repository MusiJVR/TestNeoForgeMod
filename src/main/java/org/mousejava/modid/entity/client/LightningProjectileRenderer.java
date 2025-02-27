package org.mousejava.modid.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.mousejava.modid.entity.LightningProjectileEntity;

@OnlyIn(Dist.CLIENT)
public class LightningProjectileRenderer extends EntityRenderer<LightningProjectileEntity> {
    public LightningProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(LightningProjectileEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

    }

    @Override
    public ResourceLocation getTextureLocation(LightningProjectileEntity entity) {
        return null;
    }
}
