package com.xiaoyue.tinkers_ingenuity.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;

@Mixin(value = ToolAttackContext.class, remap = false)
public interface ToolAttackContextInvoker {

    @Accessor
    void setCriticalModifier(float modifier);

}
