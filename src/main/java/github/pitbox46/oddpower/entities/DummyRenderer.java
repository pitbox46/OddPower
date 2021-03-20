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

    private static final ResourceLocation TEXTURE = new ResourceLocation(OddPower.MOD_ID, "textures/entity/dummy.png");
    //Todo figure out why the head is a little lower than it should be and fix it
    public DummyRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new DummyModel(0.5f, 0f, 64, 32), 0.5f);
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(DummyEntity entity) {
        return TEXTURE;
    }
}
