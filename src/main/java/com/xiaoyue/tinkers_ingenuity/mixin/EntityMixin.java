package com.xiaoyue.tinkers_ingenuity.mixin;

import com.xiaoyue.tinkers_ingenuity.content.items.ModifiableCurio;
import com.xiaoyue.tinkers_ingenuity.data.modifier.TIModifierData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(at = @At("HEAD"), method = "isOnFire", cancellable = true)
    public void tinkers_ingenuity$setOnFire(CallbackInfoReturnable<Boolean> cir) {
        Entity self = (Entity) (Object) this;
        if (self instanceof LivingEntity entity) {
            ModifiableCurio.postAction(entity, TIModifierData.FLAME_HEART.getId(), (curio, entry) -> cir.setReturnValue(true));
        }
    }
}
