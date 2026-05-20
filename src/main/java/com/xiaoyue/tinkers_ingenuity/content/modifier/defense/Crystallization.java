package com.xiaoyue.tinkers_ingenuity.content.modifier.defense;


import com.xiaoyue.celestial_invoker.content.common.entry.AttributeAdder;
import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.generic.ISimpleModule;
import com.xiaoyue.tinkers_ingenuity.content.generic.SerialLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.data.loadable.primitive.DoubleLoadable;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.primitive.IntLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import java.util.List;
import java.util.function.BiConsumer;

public record Crystallization(int time, double chance, int max, float bonus)
        implements ISimpleModule, InventoryTickModifierHook, AttributesModifierHook, ModifierRemovalHook {

    public static final ResourceLocation KEY = TinkersIngenuity.loc("crystallization");
    public static final RecordLoadable<Crystallization> LOADER = RecordLoadable.create(
            IntLoadable.ANY_FULL.requiredField("time", Crystallization::time),
            DoubleLoadable.ANY.requiredField("chance", Crystallization::chance),
            IntLoadable.ANY_FULL.requiredField("max", Crystallization::max),
            FloatLoadable.ANY.requiredField("bonus", Crystallization::bonus),
            Crystallization::new
    );

    @SerialLoader
    public static void onInit() {
        TinkersIngenuity.REGISTRATE.module("crystallization_modifier", LOADER);
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity entity, int index, boolean select, boolean current, ItemStack stack) {
        ModDataNBT data = tool.getPersistentData();
        if (entity.tickCount % this.time == 0 && this.chance(this.chance)) {
            data.putInt(KEY, Math.min(data.getInt(KEY) + 1, this.max));
        }
    }

    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> cons) {
        ModDataNBT data = tool.getPersistentData();
        if (slot.isArmor()) {
            float bonus = this.bonus * (float) data.getInt(KEY) * modifier.getLevel();
            AttributeAdder.builder()
                    .attr(Attributes.ARMOR_TOUGHNESS).nameWithUUID(KEY).value(bonus).operation(2).toCons(cons)
                    .attr(Attributes.MOVEMENT_SPEED).nameWithUUID(KEY).value(bonus / 2).operation(2).toCons(cons);
        }
    }

    @Override
    public @Nullable Component onRemoved(IToolStackView tool, Modifier modifier) {
        tool.getPersistentData().remove(KEY);
        return null;
    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return HookProvider.defaultHooks(ModifierHooks.INVENTORY_TICK, ModifierHooks.ATTRIBUTES, ModifierHooks.REMOVE);
    }

    @Override
    public RecordLoadable<? extends ModifierModule> getLoader() {
        return LOADER;
    }

    public static Crystallization getIns() {
        return new Crystallization(900, 0.45, 10, 0.02F);
    }
}
