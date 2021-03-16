package github.pitbox46.oddpower.entities;

import github.pitbox46.oddpower.OddPower;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class DummyRenderer extends BipedRenderer<DummyEntity, DummyModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(OddPower.MOD_ID, "textures/entity/dummy_generator.png");

    public DummyRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new DummyModel(1.0f, 0f, 64, 64), 0.5f);
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(DummyEntity entity) {
        return TEXTURE;
    }
}
