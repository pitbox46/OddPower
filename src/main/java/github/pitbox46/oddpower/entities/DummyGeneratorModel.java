package github.pitbox46.oddpower.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

import java.util.function.Function;

public class DummyGeneratorModel extends BipedModel<DummyGeneratorEntity> {
    public DummyGeneratorModel(float modelSize) {
        super(modelSize);
    }
}
