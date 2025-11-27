package com.xiaoyue.tinkers_ingenuity.content.modifier.general;

import com.xiaoyue.celestial_invoker.content.ancillary.entry.AttributeAdder;
import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.generic.ISimpleModule;
import com.xiaoyue.tinkers_ingenuity.content.generic.SerialLoader;
import com.xiaoyue.tinkers_ingenuity.utils.TinkerUtils;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.primitive.IntLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.registry.GenericLoaderRegistry;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.mining.BreakSpeedModifierHook;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;
import java.util.function.BiConsumer;

public record Alien(float bonus, int max, int bonusInterval)
        implements ISimpleModule, InventoryTickModifierHook, MeleeDamageModifierHook, ConditionalStatModifierHook, AttributesModifierHook, BreakSpeedModifierHook, TooltipModifierHook {

    public static final ResourceLocation TAG_BONUS_1 = TinkersIngenuity.loc("alien_stat_bonus1");
    public static final ResourceLocation TAG_BONUS_2 = TinkersIngenuity.loc("alien_stat_bonus2");
    public static final ResourceLocation TAG_BONUS_3 = TinkersIngenuity.loc("alien_stat_bonus3");
    public static final RecordLoadable<Alien> LOADER = RecordLoadable.create(
            FloatLoadable.ANY.requiredField("bonus", Alien::bonus),
            IntLoadable.ANY_FULL.requiredField("max", Alien::max),
            IntLoadable.ANY_FULL.requiredField("bonus_interval", Alien::bonusInterval),
            Alien::new
    );

    @SerialLoader
    public static void onInit() {
        TinkersIngenuity.REGISTRATE.module("alien_modifier", LOADER);
    }

    public static int getAllBonusValue(IToolStackView tool) {
        ModDataNBT data = tool.getPersistentData();
        return data.getInt(TAG_BONUS_1) + data.getInt(TAG_BONUS_2) + data.getInt(TAG_BONUS_3);
    }

    public boolean canUp(IToolStackView tool, int level) {
        return getAllBonusValue(tool) < max * level;
    }

    public static void addBonus(IToolStackView tool, ResourceLocation tag) {
        ModDataNBT data = tool.getPersistentData();
        data.putInt(tag, data.getInt(tag) + 1);
    }

    public float getBonus(IToolStackView tool, ResourceLocation tag) {
        return tool.getPersistentData().getInt(tag) * bonus;
    }

    @Override
    public float modifyStat(IToolStackView tool, ModifierEntry modifier, LivingEntity entity, FloatToolStat stat, float value, float multiplier) {
        if (tool.hasTag(TinkerTags.Items.RANGED)) {
            if (stat.equals(ToolStats.PROJECTILE_DAMAGE)) {
                return value * (1 + getBonus(tool, TAG_BONUS_1));
            }
            if (stat.equals(ToolStats.VELOCITY)) {
                return value * (1 + getBonus(tool, TAG_BONUS_2));
            }
            if (stat.equals(ToolStats.DRAW_SPEED)) {
                return value * (1 + getBonus(tool, TAG_BONUS_3));
            }
        }
        return value;
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        if (tool.hasTag(TinkerTags.Items.MELEE_WEAPON)) {
            return damage * (1 + getBonus(tool, TAG_BONUS_1));
        }
        return damage;
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity entity, int index, boolean select, boolean current, ItemStack stack) {
        if (entity.tickCount % bonusInterval == 0 && canUp(tool, modifier.getLevel())) {
            double chance = TConstruct.RANDOM.nextDouble();
            if (chance < 0.3) {
                addBonus(tool, TAG_BONUS_1);
            } else if (chance > 0.3 && chance < 0.6) {
                addBonus(tool, TAG_BONUS_2);
            } else if (chance > 0.6 && chance < 0.9) {
                addBonus(tool, TAG_BONUS_3);
            }
        }
    }

    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> cons) {
        if (slot.equals(LivingEntity.getEquipmentSlotForItem(((ToolStack) tool).createStack()))) {
            if (tool.hasTag(TinkerTags.Items.MELEE_PRIMARY)) {
                AttributeAdder.builder().attr(Attributes.ATTACK_SPEED).nameWithUUID(TAG_BONUS_2).value(getBonus(tool, TAG_BONUS_2))
                        .operation(1).toCons(cons);
            }
            if (tool.hasTag(TinkerTags.Items.ARMOR)) {
                AttributeAdder.builder().attr(Attributes.ARMOR).nameWithUUID(TAG_BONUS_1).value(getBonus(tool, TAG_BONUS_1))
                        .operation(1).toCons(cons);
                AttributeAdder.builder().attr(Attributes.ARMOR_TOUGHNESS).nameWithUUID(TAG_BONUS_2).value(getBonus(tool, TAG_BONUS_2))
                        .operation(1).toCons(cons);
                AttributeAdder.builder().attr(Attributes.KNOCKBACK_RESISTANCE).nameWithUUID(TAG_BONUS_3).value(getBonus(tool, TAG_BONUS_3))
                        .operation(1).toCons(cons);
            }
        }
    }

    @Override
    public void onBreakSpeed(IToolStackView tool, ModifierEntry modifier, PlayerEvent.BreakSpeed event, Direction sideHit, boolean effective, float mul) {
        if (tool.hasTag(TinkerTags.Items.HARVEST)) {
            event.setNewSpeed(event.getOriginalSpeed() * (1 + getBonus(tool, TAG_BONUS_3)));
        }
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        Modifier self = modifier.getModifier();
        if (tool.hasTag(TinkerTags.Items.RANGED)) {
            tooltip.add(TinkerUtils.getModifierBonusTifo(self, ToolStats.PROJECTILE_DAMAGE, getBonus(tool, TAG_BONUS_1), true));
            tooltip.add(TinkerUtils.getModifierBonusTifo(self, ToolStats.VELOCITY, getBonus(tool, TAG_BONUS_2), true));
            tooltip.add(TinkerUtils.getModifierBonusTifo(self, ToolStats.DRAW_SPEED, getBonus(tool, TAG_BONUS_3), true));
        }
        if (tool.hasTag(TinkerTags.Items.MELEE_PRIMARY)) {
            tooltip.add(TinkerUtils.getModifierBonusTifo(self, ToolStats.ATTACK_DAMAGE, getBonus(tool, TAG_BONUS_1), true));
        }
        if (tool.hasTag(TinkerTags.Items.HARVEST)) {
            tooltip.add(TinkerUtils.getModifierBonusTifo(self, ToolStats.MINING_SPEED, getBonus(tool, TAG_BONUS_3), true));
        }
    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return HookProvider.defaultHooks(ModifierHooks.INVENTORY_TICK, ModifierHooks.ATTRIBUTES,
                ModifierHooks.MELEE_DAMAGE, ModifierHooks.CONDITIONAL_STAT, ModifierHooks.BREAK_SPEED, ModifierHooks.TOOLTIP);
    }

    @Override
    public RecordLoadable<? extends GenericLoaderRegistry.IHaveLoader> getLoader() {
        return LOADER;
    }

    public static Alien getIns() {
        return new Alien(0.01f, 50, 400);
    }
}
