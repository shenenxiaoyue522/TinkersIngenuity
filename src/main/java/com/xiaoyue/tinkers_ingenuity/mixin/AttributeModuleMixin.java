package com.xiaoyue.tinkers_ingenuity.mixin;

import com.xiaoyue.tinkers_ingenuity.data.TITagGen;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.function.BiConsumer;

@Mixin(value = AttributeModule.class, remap = false)
public abstract class AttributeModuleMixin {

    @Inject(at = @At("HEAD"), method = "addAttributes", cancellable = true)
    public void tinkers_ingenuity$checkTool(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer, CallbackInfo ci) {
        if (tool.hasTag(TITagGen.MODIFIABLE_CURIO)) {
            ci.cancel();
        }
    }
}
