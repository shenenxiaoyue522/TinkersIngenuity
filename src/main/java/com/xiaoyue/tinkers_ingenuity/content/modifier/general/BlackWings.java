package com.xiaoyue.tinkers_ingenuity.content.modifier.general;

import com.xiaoyue.tinkers_ingenuity.content.generic.SimpleModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class BlackWings extends SimpleModifier implements InventoryTickModifierHook {

    @Override
    public void addHooks(ModuleHookMap.Builder builder) {
        builder.addHook(this, ModifierHooks.INVENTORY_TICK);
    }

    @Override
    public boolean isSingleLevel() {
        return true;
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity entity, int index, boolean select, boolean current, ItemStack stack) {
        if (entity instanceof Player player && current) {
            Abilities abilities = player.getAbilities();
            if (abilities.mayfly) {
                return;
            }
            abilities.mayfly = true;
            if (player.tickCount % 100 == 0 && abilities.flying && chance(0.5f)) {
                player.causeFoodExhaustion(0.1f);
            }
        }
    }
}
