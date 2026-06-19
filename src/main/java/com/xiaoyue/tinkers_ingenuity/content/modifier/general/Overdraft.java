package com.xiaoyue.tinkers_ingenuity.content.modifier.general;

import com.xiaoyue.tinkers_ingenuity.content.generic.SimpleModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.capacity.OverslimeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class Overdraft extends SimpleModifier implements InventoryTickModifierHook {

    @Override
    public void addHooks(ModuleHookMap.Builder builder) {
        builder.addHook(this, ModifierHooks.INVENTORY_TICK);
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity entity, int index, boolean select, boolean current, ItemStack stack) {
        if (tool.getStats().getContainedStats().contains(OverslimeModule.OVERSLIME_STAT)) {
            OverslimeModule module = OverslimeModule.INSTANCE;
            if (module.getAmount(tool) <= 0 && entity instanceof Player player) {
                Inventory inv = player.getInventory();
                for (int i = 0; i < inv.getContainerSize(); i++) {
                    ItemStack item = inv.getItem(i);
                    if (!item.is(Tags.Items.SLIMEBALLS)) continue;
                    item.shrink(1);
                    inv.setItem(i, item.copy());
                    module.addAmount(tool, modifier.getLevel() * 50);
                    return;
                }
            }
        }
    }
}
