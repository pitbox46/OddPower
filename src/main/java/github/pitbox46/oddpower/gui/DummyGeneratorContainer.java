package github.pitbox46.oddpower.gui;

import github.pitbox46.oddpower.items.UpgradeItem;
import github.pitbox46.oddpower.setup.Registration;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public class DummyGeneratorContainer extends AbstractGeneratorContainer{
    /**
     * @param type            Registration object for container
     * @param id              Window ID
     * @param blockPos        BlockPos
     * @param playerInventory PlayerInventory
     * @param block           Registration object for associated block
     */
    public DummyGeneratorContainer(@Nullable ContainerType<?> type, int id, BlockPos blockPos, PlayerInventory playerInventory, Block block) {
        super(type, id, blockPos, playerInventory, block);
        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 107, 13));
                addSlot(new SlotItemHandler(h, 1, 107, 13+18));
                addSlot(new SlotItemHandler(h, 2, 107, 13+36));
                addSlot(new SlotItemHandler(h, 3, 80, 31));
            });
        }
        layoutPlayerInventorySlots(8, 81);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemstack = stack.copy();
            if (index <= 3) {
                if (!this.mergeItemStack(stack, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(stack, itemstack);
            } else {
                if (stack.getItem() instanceof UpgradeItem) {
                    if (!this.mergeItemStack(stack, 0, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (stack.getItem() == Registration.DUMMY_ITEM.get()) {
                    if (!this.mergeItemStack(stack, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 31) {
                    if (!this.mergeItemStack(stack, 31, 40, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 40 && !this.mergeItemStack(stack, 4, 31, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, stack);
        }

        return itemstack;
    }
}
