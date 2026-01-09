package com.xiaoyue.tinkers_ingenuity.mixin;

import com.xiaoyue.tinkers_ingenuity.content.items.ModifiableCurio;
import com.xiaoyue.tinkers_ingenuity.data.modifier.TIModifierData;
import com.xiaoyue.tinkers_ingenuity.utils.TinkerUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(at = @At("HEAD"), method = "isOnFire", cancellable = true)
    public void tinkers_ingenuity$setOnFire(CallbackInfoReturnable<Boolean> cir) {
        Entity self = (Entity) (Object) this;
        if (self instanceof LivingEntity entity) {
            ModifiableCurio.postAction(entity, TIModifierData.FLAME_HEART.getId(), (c, l) -> cir.setReturnValue(true));
        }
    }

    @Inject(at = @At("HEAD"), method = "isInWater", cancellable = true)
    public void tinkers_ingenuity$setOnWater(CallbackInfoReturnable<Boolean> cir) {
        Entity self = (Entity) (Object) this;
        if (self instanceof LivingEntity entity) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack stack = entity.getItemBySlot(slot);
                if (TinkerUtils.checkTool(stack)) {
                    ToolStack tool = ToolStack.from(stack);
                    if (tool.getModifierLevel(TIModifierData.MOIST.getId()) > 0) {
                        cir.setReturnValue(true);
                        break;
                    }
                }
            }
        }
    }
}
