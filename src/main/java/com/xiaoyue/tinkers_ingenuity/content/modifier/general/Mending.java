package com.xiaoyue.tinkers_ingenuity.content.modifier.general;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.generic.ISimpleModule;
import com.xiaoyue.tinkers_ingenuity.content.generic.SerialLoader;
import com.xiaoyue.tinkers_ingenuity.data.modifier.TIModifierData;
import com.xiaoyue.tinkers_ingenuity.utils.TinkerUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import slimeknights.mantle.data.loadable.primitive.IntLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public record Mending(int repair) implements ISimpleModule {

    public static final RecordLoadable<Mending> LOADER = RecordLoadable.create(
            IntLoadable.FROM_ONE.requiredField("repair", Mending::repair),
            Mending::new);

    @SerialLoader
    public static void onInit() {
        TinkersIngenuity.REGISTRATE.module("mending", LOADER);
    }

    public static void onHandler(LivingEntity entity) {
        ItemStack stack = entity.getMainHandItem();
        if (TinkerUtils.checkTool(stack)) {
            ToolStack tool = ToolStack.from(stack);
            int lv = tool.getModifierLevel(TIModifierData.MENDING.getId());
            ToolDamageUtil.repair(tool, lv);
        }
    }

    @Override
    public RecordLoadable<? extends ModifierModule> getLoader() {
        return LOADER;
    }
}
