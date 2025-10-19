package com.xiaoyue.tinkers_ingenuity.content.modifier.defense;

import com.xiaoyue.tinkers_ingenuity.content.generic.SimpleModifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.ArrayList;
import java.util.List;

public class Spotless extends SimpleModifier implements InventoryTickModifierHook {
    
    @Override
    public boolean isSingleLevel() {
        return true;
    }

    @Override
    public void addHooks(ModuleHookMap.Builder builder) {
        builder.addHook(this, ModifierHooks.INVENTORY_TICK);
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity entity, int index, boolean select, boolean current, ItemStack stack) {
        if (current) {
            List<MobEffect> list = new ArrayList<>();
            for (MobEffectInstance effect : entity.getActiveEffects()) {
                if (effect.getEffect().getCategory().equals(MobEffectCategory.HARMFUL)) {
                    list.add(effect.getEffect());
                }
            }
            list.forEach(entity::removeEffect);
        }
    }
}
