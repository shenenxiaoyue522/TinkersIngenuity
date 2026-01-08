package com.xiaoyue.tinkers_ingenuity.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.xiaoyue.tinkers_ingenuity.content.items.ModifiableCurio;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.specail.TinkersCurioModifierHook;
import com.xiaoyue.tinkers_ingenuity.register.TIHooks;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import slimeknights.tconstruct.tools.logic.ToolEvents;

@Mixin(value = ToolEvents.class, remap = false)
public class ToolEventsMixin {

    @WrapOperation(at = @At(value = "INVOKE", target = "Lslimeknights/tconstruct/library/tools/helper/ArmorUtil;getDamageForEvent(FFFFFF)F"), method = "livingHurt")
    private static float tinkers_ingenuity$modifyProtection(float originalDamage, float armor, float toughness, float vanillaModifiers, float finalModifiers, float modifierCap, Operation<Float> original, @Local(argsOnly = true) LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        float newModifierValue = ModifiableCurio.postAction(entity, (c, e) -> {
            TinkersCurioModifierHook hook = e.getHook(TIHooks.TINKERS_CURIO);
            return hook.getProtection(c, entity, event.getSource(), finalModifiers);
        }, finalModifiers);
        return original.call(originalDamage, armor, toughness, vanillaModifiers, newModifierValue, modifierCap);
    }
}
