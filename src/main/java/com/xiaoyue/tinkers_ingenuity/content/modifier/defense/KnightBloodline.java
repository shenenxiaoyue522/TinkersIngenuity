package com.xiaoyue.tinkers_ingenuity.content.modifier.defense;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.generic.ISimpleModule;
import com.xiaoyue.tinkers_ingenuity.content.generic.SerialLoader;
import dev.xkmc.l2library.init.events.GeneralEventHandler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.primitive.IntLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.registry.GenericLoaderRegistry;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public record KnightBloodline(float protect, int maxMultiplier)
        implements ISimpleModule, OnAttackedModifierHook, ModifyDamageModifierHook {

    public static final RecordLoadable<KnightBloodline> LOADER = RecordLoadable.create(
            FloatLoadable.ANY.requiredField("protect", KnightBloodline::protect),
            IntLoadable.ANY_FULL.requiredField("max_multiplier", KnightBloodline::maxMultiplier),
            KnightBloodline::new
    );

    @SerialLoader
    public static void onInit() {
        TinkersIngenuity.REGISTRATE.module("knight_bloodline_modifier", LOADER);
    }

    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slot, DamageSource source, float amount, boolean direct) {
        if (context.getEntity() instanceof Player player) {
            if (player.getAbsorptionAmount() > 0.0f) {
                return Math.min(player.getMaxHealth() * this.protect, amount);
            }
        }

        return amount;
    }

    @Override
    public void onAttacked(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slot, DamageSource source, float amount, boolean direct) {
        if (context.getEntity() instanceof Player player) {
            if (player.getAbsorptionAmount() < player.getMaxHealth() * (float) this.maxMultiplier) {
                float maxAbs = Math.min(player.getMaxHealth() * (float) this.maxMultiplier, player.getAbsorptionAmount() + (float) modifier.getLevel());
                GeneralEventHandler.schedule(() -> player.setAbsorptionAmount(maxAbs));
            }
        }
    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return HookProvider.defaultHooks(ModifierHooks.ON_ATTACKED, ModifierHooks.MODIFY_DAMAGE);
    }

    @Override
    public RecordLoadable<? extends GenericLoaderRegistry.IHaveLoader> getLoader() {
        return LOADER;
    }

    public static KnightBloodline getIns() {
        return new KnightBloodline(0.45F, 2);
    }
}
