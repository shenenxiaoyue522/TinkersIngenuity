package com.xiaoyue.tinkers_ingenuity.content.modifier.general;

import com.xiaoyue.tinkers_ingenuity.content.generic.SimpleModifier;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.specail.MenuSlotClickModifierHook;
import com.xiaoyue.tinkers_ingenuity.mixin.ToolStackInvoker;
import com.xiaoyue.tinkers_ingenuity.register.TIHooks;
import com.xiaoyue.tinkers_ingenuity.utils.TinkerUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemStackedOnOtherEvent;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.RawDataModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.part.IToolPart;
import slimeknights.tconstruct.library.utils.RestrictedCompoundTag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreshWater extends SimpleModifier implements MenuSlotClickModifierHook, RawDataModifierHook {

    public final Map<String, ModifierNBT> TRAIT_MAP = new HashMap<>();
    public final Map<String, StatsNBT> STATS_MAP = new HashMap<>();

    @Override
    public boolean isSingleLevel() {
        return true;
    }

    @Override
    protected void addHooks(ModuleHookMap.Builder builder) {
        builder.addHook(this, TIHooks.MENU_SLOT_CLICK, ModifierHooks.RAW_DATA);
    }

    private void updateTraits(ToolStack tool, ItemStack part) {
        List<ModifierEntry> partTraits = TinkerUtils.getPartTraits(part);
        if (this.TRAIT_MAP.containsKey("partTraits")) {
            for (ModifierEntry entry : this.TRAIT_MAP.get("partTraits")) {
                tool.removeModifier(entry.getId(), entry.getLevel());
            }
        }
        this.TRAIT_MAP.put("partTraits", new ModifierNBT(partTraits));
        for (ModifierEntry trait : partTraits) {
            tool.addModifier(trait.getId(), trait.getLevel());
        }
    }

    public void updateStats(ToolStack tool, ToolStack target) {
        if (!STATS_MAP.containsKey("default_stats")) {
            STATS_MAP.put("default_stats", tool.getStats());
        }
        if (tool.getDefinition().equals(target.getDefinition()) && target.getUpgrades().isEmpty()) {
            STATS_MAP.put("copy_stats", target.getStats());
            ((ToolStackInvoker) tool).callSetStats(target.getStats());
        }
    }

    @Override
    public void onClickTool(ToolStack tool, ModifierEntry modifier, ItemStackedOnOtherEvent event, ItemStack onItem, ClickAction action) {
        if (action.equals(ClickAction.SECONDARY)) {
            if (onItem.getItem() instanceof IToolPart part) {
                if (TinkerUtils.matchesPart(tool, part)) {
                    this.updateTraits(tool, onItem);
                    onItem.shrink(1);
                    event.setCanceled(true);
                }
            }
            if (TinkerUtils.checkTool(onItem)) {
                this.updateStats(tool, ToolStack.from(onItem));
                event.setCanceled(true);
            }
        }
    }

    @Override
    public @Nullable Component onRemoved(IToolStackView tool, Modifier modifier) {
        if (this.TRAIT_MAP.containsKey("partTraits")) {
            for (ModifierEntry entry : this.TRAIT_MAP.get("partTraits")) {
                ((ToolStack) tool).removeModifier(entry.getId(), entry.getLevel());
            }
        }
        if (STATS_MAP.containsKey("default_stats")) {
            ((ToolStackInvoker) tool).callSetStats(STATS_MAP.get("default_stats"));
        }
        return null;
    }

    @Override
    public void addRawData(IToolStackView tool, ModifierEntry modifier, RestrictedCompoundTag tag) {
        if (STATS_MAP.containsKey("copy_stats")) {
            ((ToolStackInvoker) tool).callSetStats(STATS_MAP.get("copy_stats"));
        }
    }

    @Override
    public void removeRawData(IToolStackView tool, Modifier modifier, RestrictedCompoundTag tag) {
    }
}
