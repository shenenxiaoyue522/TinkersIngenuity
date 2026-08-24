package com.xiaoyue.tinkers_ingenuity.event;

import com.xiaoyue.tinkers_ingenuity.content.items.ModifiableCurio;
import com.xiaoyue.tinkers_ingenuity.content.modifier.general.Mending;
import com.xiaoyue.tinkers_ingenuity.content.shared.holder.CurioStackView;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.defense.LivingEventModifierHook;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.specail.MenuSlotClickModifierHook;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.specail.TinkersCurioModifierHook;
import com.xiaoyue.tinkers_ingenuity.data.modifier.TIModifierData;
import com.xiaoyue.tinkers_ingenuity.mixin.ToolAttackContextAccessor;
import com.xiaoyue.tinkers_ingenuity.register.TIHooks;
import com.xiaoyue.tinkers_ingenuity.utils.TinkerUtils;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemStackedOnOtherEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent.ImpactResult;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.definition.module.mining.IsEffectiveToolHook;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.data.ModifierIds;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;
import java.util.Map;

import static com.xiaoyue.tinkers_ingenuity.TinkersIngenuity.MODID;

@EventBusSubscriber(modid = MODID, bus = Bus.FORGE)
public class TIGeneralEventHandler {

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        ModifiableCurio.postAction(entity, (c, e) -> {
            TinkersCurioModifierHook hook = e.getHook(TIHooks.TINKERS_CURIO);
            event.setAmount(hook.onLivingHeal(c, e.getLevel(), entity, event.getAmount()));
        });
    }

    @SubscribeEvent
    public static void onProjHit(ProjectileImpactEvent event) {
        Projectile proj = event.getProjectile();
        if (proj.getOwner() instanceof LivingEntity entity) {
            AbstractArrow arrow = proj instanceof AbstractArrow it ? it : null;
            ModifiableCurio.postAction(entity, (c, e) -> {
                TinkersCurioModifierHook hook = e.getHook(TIHooks.TINKERS_CURIO);
                if (hook.onProjectileHit(c, e.getLevel(), entity, proj, arrow, event.getRayTraceResult(), PersistentDataCapability.getOrWarn(proj))) {
                    event.setImpactResult(ImpactResult.STOP_AT_CURRENT);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onBreakBlock(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        ModifiableCurio.postAction(player, (c, e) -> {
            TinkersCurioModifierHook hook = e.getHook(TIHooks.TINKERS_CURIO);
            hook.onBreakBlock(c, e.getLevel(), player, event.getState(), event.getPos());
        });
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        ItemStack stack = player.getMainHandItem();
        ModifiableCurio.postAction(player, (c, e) -> {
            TinkersCurioModifierHook hook = e.getHook(TIHooks.TINKERS_CURIO);
            boolean effective = TinkerUtils.checkTool(stack) && IsEffectiveToolHook.isEffective(ToolStack.from(stack), event.getState());
            hook.getBreakSpeed(c, e.getLevel(), player, event, effective);
            if (c.hasModifier(ModifierIds.haste)) {
                event.setNewSpeed(event.getNewSpeed() + c.getModifierLevel(ModifierIds.haste) * 4);
            }
        });
    }

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Projectile proj) {
            if (proj.getOwner() instanceof LivingEntity shooter) {
                ModifiableCurio.calculateProjDamage(shooter, proj);
                AbstractArrow arrow = proj instanceof AbstractArrow it ? it : null;
                ModifiableCurio.postAction(shooter, (c, e) -> {
                    TinkersCurioModifierHook hook = e.getHook(TIHooks.TINKERS_CURIO);
                    hook.onShootProjectile(c, e.getLevel(), shooter, proj, arrow, PersistentDataCapability.getOrWarn(proj));
                });
            }
        }
    }

    @SubscribeEvent
    public static void onDamage(LivingDamageEvent event) {
        LivingEntity target = event.getEntity();
        Entity source = event.getSource().getEntity();
        if (source instanceof LivingEntity attacker) {
            ModifiableCurio.postAction(attacker, (c, e) -> {
                TinkersCurioModifierHook hook = e.getHook(TIHooks.TINKERS_CURIO);
                hook.onDamageTargetPost(c, e.getLevel(), attacker, target, event);
            });
        }
        ModifiableCurio.postAction(target, (c, e) -> {
            TinkersCurioModifierHook hook = e.getHook(TIHooks.TINKERS_CURIO);
            hook.onTakenDamagePost(c, e.getLevel(), target, event.getSource(), event);
        });
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        Entity source = event.getSource().getEntity();
        if (source instanceof LivingEntity attacker) {
            ModifiableCurio.postAction(attacker, ModifierIds.strength, (C, l) -> {
                event.setAmount(event.getAmount() * (1 + l * 0.1f));
            });
            ModifiableCurio.postAction(attacker, (c, e) -> {
                TinkersCurioModifierHook hook = e.getHook(TIHooks.TINKERS_CURIO);
                hook.onDamageTargetPre(c, e.getLevel(), attacker, target, event);
            });
        }
        ModifiableCurio.postAction(target, (c, e) -> {
            TinkersCurioModifierHook hook = e.getHook(TIHooks.TINKERS_CURIO);
            hook.onTakenDamagePre(c, e.getLevel(), target, event.getSource(), event);
        });
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onHurtLowest(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        List<SlotResult> allCurios = ModifiableCurio.findAll(entity);
        if (!allCurios.isEmpty()) {
            float factor = 1f;
            for (SlotResult result : allCurios) {
                CurioStackView curio = CurioStackView.of(result);
                if (curio.hasModifier(ModifierIds.protection)) {
                    factor -= curio.getModifierLevel(ModifierIds.protection) * 0.05f;
                }
                for (Map.Entry<TagKey<DamageType>, ModifierId> entry : PROTECTION_MAP.entrySet()) {
                    if (curio.hasModifier(entry.getValue()) && event.getSource().is(entry.getKey())) {
                        factor -= curio.getModifierLevel(entry.getValue()) * 0.08f;
                    }
                }
            }
            event.setAmount(event.getAmount() * factor);
        }
    }

    private static final Map<TagKey<DamageType>, ModifierId> PROTECTION_MAP = Map.of(
            TinkerTags.DamageTypes.PROJECTILE_PROTECTION, ModifierIds.projectileProtection,
            TinkerTags.DamageTypes.FIRE_PROTECTION, ModifierIds.fireProtection,
            TinkerTags.DamageTypes.MELEE_PROTECTION, ModifierIds.meleeProtection,
            TinkerTags.DamageTypes.BLAST_PROTECTION, ModifierIds.blastProtection,
            TinkerTags.DamageTypes.MAGIC_PROTECTION, ModifierIds.magicProtection
    );

    @SubscribeEvent
    public static void onAttack(LivingAttackEvent event) {
        LivingEntity target = event.getEntity();
        ModifiableCurio.postAction(target, (c, e) -> {
            TinkersCurioModifierHook hook = e.getHook(TIHooks.TINKERS_CURIO);
            if (hook.canImmuneAttack(c, e.getLevel(), target, event.getSource(), event.getAmount())) {
                event.setCanceled(true);
            }
        });
    }

    @SubscribeEvent
    public static void onMenuSlotClick(ItemStackedOnOtherEvent event) {
        ItemStack stack = event.getCarriedItem();
        if (TinkerUtils.checkTool(stack)) {
            MenuSlotClickModifierHook.post(ToolStack.from(stack), event);
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        TinkerUtils.postArmorAction(event.getEntity(), holder ->
                LivingEventModifierHook.postDeath(holder.getTool(), holder.context(), event, holder.slot()));
    }

    @SubscribeEvent
    public static void onPickupExp(PlayerXpEvent.PickupXp event) {
        Player entity = event.getEntity();
        ModifiableCurio.postAction(entity, (c, e) -> {
            TinkersCurioModifierHook hook = e.getHook(TIHooks.TINKERS_CURIO);
            hook.onPickupExp(c, e.getLevel(), entity, event.getOrb());
        });
        Mending.onHandler(entity);
    }

    public static void onToolMeleeStart(IToolStackView tool, ToolAttackContext context) {
        if (tool.getModifierLevel(TIModifierData.BE_IMPOLITE.getId()) > 0) {
            ((ToolAttackContextAccessor) context).setCriticalModifier(1.5f);
        }
    }
}
