package com.xiaoyue.tinkers_ingenuity.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class LivingEntityMixin {

    @Inject(at = @At("HEAD"), method = "isOnFire")
    public void tinkers_ingenuity$setOnFire(CallbackInfoReturnable<Boolean> cir) {

    }
}
