package github.pitbox46.oddpower.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ColorHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class GeneratorScreen<T extends AbstractGeneratorContainer> extends ContainerScreen<T> {
    private final ResourceLocation GUI;

    public GeneratorScreen(T container, PlayerInventory inv, ITextComponent name, ResourceLocation texture) {
        super(container, inv, name);
        GUI = texture;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        drawEnergyBar(matrixStack, 53, 13, 69, 65);

        int guiX = x - getGuiLeft(), guiY = y - getGuiTop();
        if(53 <= guiX && guiX <= 69 && 13 <= guiY && guiY <= 65) {// Tooltip to display specific amount of power when hovering over bar
            renderTooltip(matrixStack, new StringTextComponent(container.getEnergy() + " FE"), guiX, guiY);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(matrixStack, relX, relY, 0, 0, this.xSize, this.ySize);
    }

    /**
     * Draws a two color bar designed for displaying energy
     */
    private void drawEnergyBar(MatrixStack matrixStack, int minX, int minY, int maxX, int maxY) {
        int energyHeight = Math.floorDiv((maxY - minY) * container.getEnergy(), container.getMaxEnergy());

        int color1Amount = 3;
        int color2Amount = 2;
        int color1 = ColorHelper.PackedColor.packColor(255, 220, 0, 0);
        int color2 = ColorHelper.PackedColor.packColor(255, 150, 0, 0);

        int i = 0;
        boolean primaryColor = true;
        while(i < energyHeight) {
            if(i + (primaryColor ? color1Amount : color2Amount) > energyHeight) {
                fill(matrixStack, minX, maxY - energyHeight, maxX, maxY - i, primaryColor ? color1 : color2);
                break;
            }
            fill(matrixStack, minX, maxY - (i + (primaryColor ? color1Amount : color2Amount)), maxX, maxY - i, primaryColor ? color1 : color2);
            i += primaryColor ? color1Amount : color2Amount;
            primaryColor = !primaryColor;
        }
    }
}
