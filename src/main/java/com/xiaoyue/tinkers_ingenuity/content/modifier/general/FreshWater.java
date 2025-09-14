package com.xiaoyue.tinkers_ingenuity.content.modifier.general;

import com.xiaoyue.tinkers_ingenuity.content.generic.SimpleModifier;
import com.xiaoyue.tinkers_ingenuity.content.shared.hooks.specail.MenuSlotClickModifierHook;
import com.xiaoyue.tinkers_ingenuity.mixin.ToolStackInvoker;
import com.xiaoyue.tinkers_ingenuity.register.TIHooks;
import com.xiaoyue.tinkers_ingenuity.utils.TinkerUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
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

import java.util.List;

public class FreshWater extends SimpleModifier implements MenuSlotClickModifierHook, RawDataModifierHook {

    private final String TAG_COPY_STATS = "freshWater_copyStats";
    private final String TAG_COPY_TRAITS = "freshWater_copyTraits";

    @Override
    public boolean isSingleLevel() {
        return true;
    }

    @Override
    protected void addHooks(ModuleHookMap.Builder builder) {
        builder.addHook(this, TIHooks.MENU_SLOT_CLICK, ModifierHooks.RAW_DATA);
    }

    private void updateTraits(ToolStack tool, ItemStack part) {
        ItemStack stack = tool.createStack();
        CompoundTag tag = stack.getOrCreateTag();
        List<ModifierEntry> partTraits = TinkerUtils.getPartTraits(part);
        if (tag.contains(TAG_COPY_TRAITS)) {
            ModifierNBT.readFromNBT(tag.getList(TAG_COPY_TRAITS, Tag.TAG_COMPOUND)).forEach(e -> {
                tool.removeModifier(e.getId(), e.getLevel());
            });
        }
        ModifierNBT copyTraits = new ModifierNBT(partTraits);
        tag.put(TAG_COPY_TRAITS, copyTraits.serializeToNBT());
        copyTraits.forEach(e -> {
            tool.addModifier(e.getId(), e.getLevel());
        });
    }

    public void updateStats(ToolStack tool, ToolStack target) {
        ItemStack stack = tool.createStack();
        CompoundTag tag = stack.getOrCreateTag();
        if (tool.getDefinition().equals(target.getDefinition()) && target.getUpgrades().isEmpty()) {
            tag.put(TAG_COPY_STATS, target.getStats().serializeToNBT());
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
        ItemStack stack = ((ToolStack) tool).createStack();
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains(TAG_COPY_TRAITS)) {
            ModifierNBT.readFromNBT(tag.getList(TAG_COPY_TRAITS, Tag.TAG_COMPOUND)).forEach(e -> {
                ((ToolStack) tool).removeModifier(e.getId(), e.getLevel());
            });
        }
        return null;
    }

    @Override
    public void addRawData(IToolStackView tool, ModifierEntry modifier, RestrictedCompoundTag tag) {
        ItemStack stack = ((ToolStack) tool).createStack();
        CompoundTag nbt = stack.getOrCreateTag();
        if (nbt.contains(TAG_COPY_STATS)) {
            ((ToolStackInvoker) tool).callSetStats(StatsNBT.readFromNBT(nbt.getCompound(TAG_COPY_STATS)));
        }
    }

    @Override
    public void removeRawData(IToolStackView tool, Modifier modifier, RestrictedCompoundTag tag) {
    }
}
