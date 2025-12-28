package com.xiaoyue.tinkers_ingenuity.content.modifier.module.curios;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.generic.ISimpleModule;
import com.xiaoyue.tinkers_ingenuity.content.generic.SerialLoader;
import com.xiaoyue.tinkers_ingenuity.content.json.variable.LevelingFormula;
import com.xiaoyue.tinkers_ingenuity.content.shared.holder.CurioStackView;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.specail.TinkersCurioModifierHook;
import com.xiaoyue.tinkers_ingenuity.register.TIHooks;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.mantle.data.predicate.entity.LivingEntityPredicate;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;

import java.util.List;

public record CPickupExpBonusModule(IJsonPredicate<LivingEntity> entity, LevelingFormula bonus) implements ISimpleModule, TinkersCurioModifierHook {

    public static final RecordLoadable<CPickupExpBonusModule> LOADER = RecordLoadable.create(
            LivingEntityPredicate.LOADER.requiredField("entity", CPickupExpBonusModule::entity),
            LevelingFormula.LOADER.requiredField("bonus", CPickupExpBonusModule::bonus),
            CPickupExpBonusModule::new
    );

    @SerialLoader
    public static void onInit() {
        TinkersIngenuity.REGISTRATE.module("curio_pickup_exp_bonus", LOADER);
    }

    @Override
    public void onPickupExp(CurioStackView curio, int level, LivingEntity entity, ExperienceOrb orb) {
        if (test(entity, this.entity)) {
            orb.value = (int) bonus.apply(orb.value, level);
        }
    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return HookProvider.defaultHooks(TIHooks.TINKERS_CURIO);
    }

    @Override
    public RecordLoadable<? extends ModifierModule> getLoader() {
        return LOADER;
    }

    public static CPickupExpBonusModule any(LevelingFormula bonus) {
        return new CPickupExpBonusModule(LivingEntityPredicate.ANY, bonus);
    }
}
