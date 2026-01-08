package com.xiaoyue.tinkers_ingenuity.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.xiaoyue.tinkers_ingenuity.content.modifier.general.FreshWater;
import com.xiaoyue.tinkers_ingenuity.data.modifier.TIModifierData;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.tools.context.ToolRebuildContext;
import slimeknights.tconstruct.library.tools.nbt.MultiplierNBT;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.INumericToolStat;
import slimeknights.tconstruct.library.tools.stat.IToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.utils.RestrictedCompoundTag;

import java.util.List;

@Mixin(value = ToolStack.class, remap = false)
public abstract class ToolStackMixin {

    @Shadow private CompoundTag nbt;

    @Shadow protected abstract void setStats(StatsNBT stats);

    @Shadow protected abstract void setMultipliers(MultiplierNBT multipliers);

    @Shadow public abstract RestrictedCompoundTag getRestrictedNBT();

    @Inject(at = @At(value = "INVOKE", target = "Lslimeknights/tconstruct/library/tools/definition/module/build/ToolStatsHook;addToolStats(Lslimeknights/tconstruct/library/tools/nbt/IToolContext;Lslimeknights/tconstruct/library/tools/stat/ModifierStatsBuilder;)V"), method = "rebuildStats", cancellable = true)
    public void tinkers_ingenuity$onFreshWater(CallbackInfo ci, @Local ToolRebuildContext tool) {
        if (tool.getModifierLevel(TIModifierData.FRESH_WATER.getId()) > 0 && nbt.contains(FreshWater.TAG_COPY_STATS) && nbt.contains(FreshWater.TAG_COPY_MULTIPLIERS)) {
            ModifierStatsBuilder builder = ModifierStatsBuilder.builder();
            MultiplierNBT multiplierNBT = MultiplierNBT.readFromNBT(nbt.getCompound(FreshWater.TAG_COPY_MULTIPLIERS));
            StatsNBT statsNBT = StatsNBT.readFromNBT(nbt.getCompound(FreshWater.TAG_COPY_STATS));
            for (IToolStat<?> stat : statsNBT.getContainedStats()) {
                if (stat instanceof FloatToolStat floatStat) {
                    float newVal = statsNBT.get(floatStat) / multiplierNBT.get(floatStat);
                    floatStat.update(builder, newVal);
                } else {
                    stat.update(builder, Wrappers.cast(statsNBT.get(stat)));
                }
            }
            for (INumericToolStat<?> stat : multiplierNBT.getContainedStats()) {
                stat.multiplyAll(builder, multiplierNBT.get(stat));
            }
            List<ModifierEntry> list = tool.getModifierList();
            for(ModifierEntry entry : list) {
                entry.getHook(ModifierHooks.TOOL_STATS).addToolStats(tool, entry, builder);
            }
            setStats(builder.build());
            setMultipliers(builder.buildMultipliers());
            for(ModifierEntry entry : list) {
                entry.getHook(ModifierHooks.RAW_DATA).addRawData((ToolStack) (Object) this, entry, getRestrictedNBT());
            }
            ci.cancel();
        }
    }
}
