package github.pitbox46.oddpower.blocks;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import github.pitbox46.oddpower.OddPower;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ColorHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class GenericGeneratorScreen extends ContainerScreen<GenericGeneratorContainer> {
    private ResourceLocation GUI = new ResourceLocation(OddPower.MOD_ID, "textures/gui/generator_gui.png");

    public GenericGeneratorScreen(GenericGeneratorContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        int barHeight = Math.floorDiv(container.getEnergy() * 52, container.getMaxEnergy());
        fill(matrixStack, 53, 65-barHeight, 69, 65, ColorHelper.PackedColor.packColor(255, 232, 72, 72));
        int guiX = x - getGuiLeft(), guiY = y - getGuiTop();
        if(53 <= guiX && guiX <= 69 && 13 <= guiY && guiY <= 65) {
            renderTooltip(matrixStack, new StringTextComponent(Integer.toString(container.getEnergy()) + " FE"), guiX, guiY);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void renderHoveredTooltip(MatrixStack matrixStack, int x, int y) {
        super.renderHoveredTooltip(matrixStack, x, y);
    }
}
