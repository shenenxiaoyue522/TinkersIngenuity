package com.xiaoyue.tinkers_ingenuity.mixin;

import dev.xkmc.l2damagetracker.contents.attack.AttackEventHandler;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.mantle.util.CombatHelper;

@Mixin(value = CombatHelper.class, remap = false)
public class CombatRuleMixin {

    @Inject(at = @At("RETURN"), method = "damageSource(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/damagesource/DamageSource;", cancellable = true)
    private static void tinkers_ingenuity$modify_source(ResourceKey<DamageType> key, Entity direct, Entity causing, CallbackInfoReturnable<DamageSource> cir) {
        if (causing instanceof LivingEntity attacker) {
            Registry<DamageType> registry = direct.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
            CreateSourceEvent event = new CreateSourceEvent(registry, key, attacker, direct);
            cir.setReturnValue(AttackEventHandler.onDamageSourceCreate(event));
        }
    }
}
