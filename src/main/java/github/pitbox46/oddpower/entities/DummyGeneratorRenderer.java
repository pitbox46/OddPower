package github.pitbox46.oddpower.entities;

import github.pitbox46.oddpower.OddPower;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DummyGeneratorRenderer extends LivingRenderer<DummyGeneratorEntity, DummyGeneratorModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(OddPower.MOD_ID, "textures/entity/dummy_generator.png");

    public DummyGeneratorRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new DummyGeneratorModel(1.0f), 1.0f);
    }

    @Override
    public ResourceLocation getEntityTexture(DummyGeneratorEntity entity) {
        return TEXTURE;
    }
}
