package github.pitbox46.oddpower.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class UpgradeItem extends Item {
    public UpgradeItem(Properties properties) {
        super(properties.maxStackSize(1));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new StringTextComponent("").appendSibling(getDescription()).mergeStyle(TextFormatting.GRAY));
    }

    @OnlyIn(Dist.CLIENT)
    public ITextComponent getDescription(){
        return new TranslationTextComponent(this.getTranslationKey() + ".desc");
    }
}
