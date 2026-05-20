package com.xiaoyue.tinkers_ingenuity.mixin;

import com.xiaoyue.tinkers_ingenuity.content.generic.MeleeCacheCapability;
import com.xiaoyue.tinkers_ingenuity.event.TIGeneralEventHandler;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

@Mixin(value = ToolAttackUtil.class, remap = false)
public abstract class ToolAttackUtilMixin {

    @Inject(at = @At("HEAD"), method = "performAttack")
    private static void tinkers_ingenuity$attackEntity$putCache(IToolStackView tool, ToolAttackContext context, CallbackInfoReturnable<Boolean> cir) {
        if (context.getAttacker() instanceof Player player) {
            MeleeCacheCapability.saveTool(player, context.getHand());
        }
        TIGeneralEventHandler.onToolMeleeStart(tool, context);
    }

    @Inject(at = @At("RETURN"), method = "performAttack")
    private static void tinkers_ingenuity$attackEntity$removeCache(IToolStackView tool, ToolAttackContext context, CallbackInfoReturnable<Boolean> cir) {
        if (context.getAttacker() instanceof Player player) {
            MeleeCacheCapability.removeCache(player);
        }
    }
}
