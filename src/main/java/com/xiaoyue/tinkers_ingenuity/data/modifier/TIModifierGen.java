package com.xiaoyue.tinkers_ingenuity.data.modifier;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.json.action.LivingEntityAction;
import com.xiaoyue.tinkers_ingenuity.content.json.action.MultiBonusHelper;
import com.xiaoyue.tinkers_ingenuity.content.json.action.ProjectileDataAction;
import com.xiaoyue.tinkers_ingenuity.content.json.condition.TIEntityCondition;
import com.xiaoyue.tinkers_ingenuity.content.json.variable.LevelingFormula;
import com.xiaoyue.tinkers_ingenuity.content.json.variable.StatOperation;
import com.xiaoyue.tinkers_ingenuity.content.modifier.defense.Crystallization;
import com.xiaoyue.tinkers_ingenuity.content.modifier.defense.KnightBloodline;
import com.xiaoyue.tinkers_ingenuity.content.modifier.defense.VulnerabilityInsurance;
import com.xiaoyue.tinkers_ingenuity.content.modifier.general.Alien;
import com.xiaoyue.tinkers_ingenuity.content.modifier.general.Chivalry;
import com.xiaoyue.tinkers_ingenuity.content.modifier.module.building.AllFloatStatCopyModule;
import com.xiaoyue.tinkers_ingenuity.content.modifier.module.building.StatWithTraitCountModule;
import com.xiaoyue.tinkers_ingenuity.content.modifier.module.combat.CreateSourceModule;
import com.xiaoyue.tinkers_ingenuity.content.modifier.module.combat.ExtraMagicAttackModule;
import com.xiaoyue.tinkers_ingenuity.content.modifier.module.combat.ForceAddEffectModule;
import com.xiaoyue.tinkers_ingenuity.content.modifier.module.curios.*;
import com.xiaoyue.tinkers_ingenuity.content.modifier.module.defense.SimpleProtectionModule;
import com.xiaoyue.tinkers_ingenuity.content.modifier.module.defense.SourceOrEntityProtectionModule;
import com.xiaoyue.tinkers_ingenuity.content.modifier.module.mixed.MixedModificationModule;
import com.xiaoyue.tinkers_ingenuity.data.TIDamageState;
import com.xiaoyue.tinkers_ingenuity.register.TIEffects;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import slimeknights.mantle.data.predicate.damage.DamageSourcePredicate;
import slimeknights.mantle.data.predicate.entity.LivingEntityPredicate;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.data.tinkering.AbstractModifierProvider;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.modifiers.modules.build.ModifierSlotModule;
import slimeknights.tconstruct.library.modifiers.modules.build.StatBoostModule;
import slimeknights.tconstruct.library.modifiers.modules.capacity.OverslimeModule;
import slimeknights.tconstruct.library.modifiers.util.ModifierLevelDisplay;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.TinkerModifiers;

public class TIModifierGen extends AbstractModifierProvider {
    public TIModifierGen(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void addModifiers() {
        buildModifier(TIModifierData.PURGATORY.getId())
                .addModule(MixedModificationModule.get(LivingEntityPredicate.ON_FIRE, MultiBonusHelper.addAll(ToolStats.PROJECTILE_DAMAGE),
                        LevelingFormula.mulBase(0.2F)));
        buildModifier(TIModifierData.AFTERSHOCK.getId())
                .addModule(ExtraMagicAttackModule.any(LevelingFormula.mulTotal(0.1F)));
        buildModifier(TIModifierData.QUENCHED_BODY.getId())
                .addModule(SourceOrEntityProtectionModule.get(DamageSourcePredicate.tag(DamageTypeTags.IS_FIRE), LivingEntityPredicate.ON_FIRE,
                        LevelingFormula.add(5.0F), LevelingFormula.add(2.5F)));
        buildModifier(TIModifierData.FINAL_REINFORCEMENT.getId())
                .addModule(StatBoostModule.multiplyBase(ToolStats.ATTACK_DAMAGE).flat(0.2F))
                .addModule(StatBoostModule.multiplyBase(ToolStats.PROJECTILE_DAMAGE).flat(0.2F))
                .addModule(CreateSourceModule.any(TIDamageState.BYPASS_ARMOR));
        buildModifier(TIModifierData.BE_IMPOLITE.getId()).levelDisplay(ModifierLevelDisplay.NO_LEVELS);
        buildModifier(TIModifierData.PENETRATING_STAR.getId())
                .levelDisplay(ModifierLevelDisplay.NO_LEVELS)
                .addModule(CreateSourceModule.any(TIDamageState.BYPASS_ENTITY_INV))
                .addModule(CreateSourceModule.any(TIDamageState.BYPASS_COOLDOWN));
        buildModifier(TIModifierData.DELICATE.getId())
                .levelDisplay(ModifierLevelDisplay.NO_LEVELS)
                .addModule(StatBoostModule.multiplyBase(ToolStats.MINING_SPEED).flat(0.49F))
                .addModule(StatBoostModule.multiplyBase(ToolStats.ATTACK_DAMAGE).flat(0.49F))
                .addModule(StatBoostModule.multiplyBase(ToolStats.PROJECTILE_DAMAGE).flat(0.49F))
                .addModule(StatBoostModule.multiplyBase(ToolStats.DURABILITY).flat(-0.49F));
        buildModifier(TIModifierData.DEADLY_PLAGUE.getId())
                .addModule(ForceAddEffectModule.get(TIEffects.DEADLY_PLAGUE.get(), 1, 200));
        buildModifier(TIModifierData.COLORFUL_SLIME.getId())
                .addModule(AllFloatStatCopyModule.get(OverslimeModule.OVERSLIME_STAT,
                        StatOperation.multiplier_base, 5.0E-5F)).priority(22);
        buildModifier(TIModifierData.COOPERATION.getId()).levelDisplay(ModifierLevelDisplay.NO_LEVELS)
                .addModule(StatWithTraitCountModule.get(ToolStats.DURABILITY, StatOperation.multiplier_base, 0.15F,
                        true, TinkerModifiers.overslime.getId()))
                .addModule(StatWithTraitCountModule.get(OverslimeModule.OVERSLIME_STAT, StatOperation.multiplier_base, 0.15F,
                        true, TinkerModifiers.overslime.getId()));
        buildModifier(TIModifierData.CRYSTALLIZATION.getId())
                .addModule(Crystallization.getIns());
        buildModifier(TIModifierData.VULNERABILITY_INSURANCE.getId())
                .levelDisplay(ModifierLevelDisplay.NO_LEVELS)
                .addModule(VulnerabilityInsurance.getIns());
        buildModifier(TIModifierData.CHIVALRY.getId())
                .addModule(Chivalry.getIns());
        buildModifier(TIModifierData.KNIGHT_BLOODLINE.getId())
                .addModule(KnightBloodline.getIns());
        buildModifier(TIModifierData.RAPID_FIRE.getId())
                .levelDisplay(ModifierLevelDisplay.NO_LEVELS)
                .addModule(StatBoostModule.add(ToolStats.USE_ITEM_SPEED).flat(1.0F));
        buildModifier(TIModifierData.LITHE.getId())
                .addModule(CAttributeAdderModule.get(TinkersIngenuity.loc("lithe"), Attributes.ATTACK_SPEED, 1, 0.05))
                .addModule(CShootProjectileModule.any(ProjectileDataAction.scale_velocity, LevelingFormula.mulBase(0.05F)))
                .addModule(CBreakSpeedModule.any(LevelingFormula.mulBase(0.08F), false));
        buildModifier(TIModifierData.COLD_BLOODED.getId())
                .addModule(CModifyAttackModule.get(TIEntityCondition.FULL_HEALTH, LivingEntityPredicate.ANY,
                        LevelingFormula.mulBase(0.5F)));
        buildModifier(TIModifierData.CRUSH.getId())
                .addModule(CBreakSpeedModule.any(LevelingFormula.mulBase(0.25F), true));
        buildModifier(TIModifierData.DEVOURING_LIFE.getId())
                .addModule(CAfterAttackModule.get(TIEntityCondition.FULL_CHARGED, LivingEntityPredicate.ANY,
                        LivingEntityAction.heal_entity, 1.0F, true));
        buildModifier(TIModifierData.FORGE_FIRE.getId())
                .addModule(CAttackImmuneModule.get(DamageSourcePredicate.tag(DamageTypeTags.IS_FIRE), 0.1,
                        LivingEntityAction.heal_entity, 1.0f));
        buildModifier(TIModifierData.BLOT_OUT.getId()).levelDisplay(ModifierLevelDisplay.NO_LEVELS);
        buildModifier(TIModifierData.WALK_SNOW.getId()).levelDisplay(ModifierLevelDisplay.NO_LEVELS);
        buildModifier(TIModifierData.GOLDEN.getId()).levelDisplay(ModifierLevelDisplay.NO_LEVELS);
        buildModifier(TIModifierData.MYSTERIOUS.getId())
                .addModule(ModifierSlotModule.slot(SlotType.UPGRADE).eachLevel(2));
        buildModifier(TIModifierData.DEXTEROUS.getId())
                .addModule(MixedModificationModule.get(LivingEntityPredicate.ANY, new MultiBonusHelper(true,
                        false, ToolStats.VELOCITY), LevelingFormula.mulBase(0.12f)))
                .addModule(AttributeModule.builder(Attributes.ATTACK_SPEED, AttributeModifier.Operation.MULTIPLY_BASE).eachLevel(0.12f));
        buildModifier(TIModifierData.DEMONIC.getId())
                .addModule(SimpleProtectionModule.any(DamageSourcePredicate.tag(TinkerTags.DamageTypes.MAGIC_PROTECTION), LevelingFormula.add(11.25f)));
        buildModifier(TIModifierData.SCHOLAR.getId())
                .addModule(CPickupExpBonusModule.any(LevelingFormula.mulBase(0.15f)));
        buildModifier(TIModifierData.ALIEN.getId())
                .addModules(Alien.getIns());
        buildModifier(TIModifierData.FLAME_HEART.getId()).levelDisplay(ModifierLevelDisplay.NO_LEVELS);
        buildModifier(TIModifierData.MOIST.getId()).levelDisplay(ModifierLevelDisplay.NO_LEVELS);
        buildModifier(TIModifierData.AQUATIC.getId())
                .addModule(MixedModificationModule.get(TIEntityCondition.IN_WATER_OR_RAIN,
                        MultiBonusHelper.addAll(ToolStats.DRAW_SPEED), LevelingFormula.mulBase(0.18f)));
        buildModifier(TIModifierData.SUPER_SHARP.getId())
                .addModule(StatBoostModule.multiplyBase(ToolStats.ATTACK_DAMAGE).eachLevel(0.1f))
                .addModule(StatBoostModule.multiplyBase(ToolStats.PROJECTILE_DAMAGE).eachLevel(0.1f));
    }

    @Override
    public String getName() {
        return "Tinkers Ingenuity modifier providers";
    }
}
