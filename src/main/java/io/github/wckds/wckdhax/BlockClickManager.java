package io.github.wckds.wckdhax;

import io.github.wckds.wckdhax.automationhax.AutoHotbar;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class BlockClickManager implements AttackBlockCallback {
    @Override
    public ActionResult interact(PlayerEntity playerEntity, World world, Hand hand, BlockPos blockPos, Direction direction) {
        PlayerInventory inventory = playerEntity.getInventory();
        BlockState block = world.getBlockState(blockPos);
        int slot = -1;
        float maxSpeed = Float.MIN_VALUE;
        for (int i = 0; i < 9; i++) {
            ItemStack tool = inventory.getStack(i);
            if (isUsingEffectiveTool(tool, block)) {
                float speed = getBlockBreakingSpeed(tool, block);
                if (speed > maxSpeed) {
                    if (inventory.getStack(i).getDamage() + 3 != inventory.getStack(i).getMaxDamage()) {
                        maxSpeed = speed;
                        slot = i;
                    }
                }
            }
        }

        if (slot == -1) {
            maxSpeed = Float.MIN_VALUE;
            for (int i = 0; i < 9; i++) {
                ItemStack tool = inventory.getStack(i);
                float speed = getBlockBreakingSpeed(tool, block);
                if (speed > maxSpeed) {
                    if (inventory.getStack(i).getDamage() + 3 != inventory.getStack(i).getMaxDamage()) {
                        maxSpeed = speed;
                        slot = i;
                    }
                }
            }
        }

        if (AutoHotbar.INSTANCE.enabled && getBlockBreakingSpeed(inventory.getMainHandStack(), block) < maxSpeed) {
            inventory.selectedSlot = slot;
            Utils.sendPacket(new UpdateSelectedSlotC2SPacket(slot));
        }

        return ActionResult.PASS;
    }

    public boolean isUsingEffectiveTool(ItemStack tool, BlockState block) { // adapted from net.minecraft.entity.player.PlayerEntity.class
        return !block.isToolRequired() || tool.isSuitableFor(block);
    }

    public float getBlockBreakingSpeed(ItemStack tool, BlockState block) { // adapted from net.minecraft.entity.player.PlayerEntity.class
        float f = tool.getMiningSpeedMultiplier(block);

        if (f > 1.0F) {
            int efficiency = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, tool);

            if (efficiency > 0 && !tool.isEmpty()) {
                f += (float) (efficiency * efficiency + 1);
            }
        }

        return f;
    }
}