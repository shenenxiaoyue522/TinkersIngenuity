package com.xiaoyue.tinkers_ingenuity.register;

import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.items.ModifiableBlowpipe;
import com.xiaoyue.tinkers_ingenuity.content.items.ModifiableCurio;
import com.xiaoyue.tinkers_ingenuity.content.shared.holder.MetalItemEntry;
import com.xiaoyue.tinkers_ingenuity.content.shared.stats.CurioMainMaterialStat;
import com.xiaoyue.tinkers_ingenuity.content.shared.stats.TIExtraMaterialStat;
import com.xiaoyue.tinkers_ingenuity.data.tools.TIToolDefinitionGen;
import com.xiaoyue.tinkers_ingenuity.utils.TinkerUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tools.stats.GripMaterialStats;
import slimeknights.tconstruct.tools.stats.LimbMaterialStats;

import java.util.List;

public class TIItems {

    public static final ItemEntry<Item> FINAL_SHELL;
    public static final MetalItemEntry BLACK_GOLD;
    public static final MetalItemEntry FLAME_STEEL;
    public static final MetalItemEntry BLACK_FLASH_ALLOY;
    public static final ItemEntry<Item> COLOURED_GLAZE_STAR;
    public static final ItemEntry<Item> ELFS_CRYSTAL;
    public static final ItemEntry<Item> PLAGUE_BONE;
    public static final MetalItemEntry COLORFUL_SLIME;
    public static final MetalItemEntry KNIGHT_CRYSTAL;
    public static final List<ItemEntry<Item>> MEDAL_RIBBONS_CAST;
    public static final List<ItemEntry<Item>> MEDAL_BODY_CAST;
    public static final List<ItemEntry<Item>> BLOWPIPE_TUBE_CAST;
    public static final List<ItemEntry<Item>> BLOWPIPE_MOUTH_CAST;
    public static final ItemEntry<ToolPartItem> MEDAL_RIBBONS;
    public static final ItemEntry<ToolPartItem> MEDAL_BODY;
    public static final ItemEntry<ToolPartItem> BLOWPIPE_TUBE;
    public static final ItemEntry<ToolPartItem> BLOWPIPE_MOUTH;
    public static final RegistryEntry<ModifiableCurio> TINKERS_MEDAL;
    public static final RegistryEntry<ModifiableBlowpipe> BLOWPIPE;

    static {
        registrate().defaultCreativeTab(TinkersIngenuity.ITEMS.getKey());
        FINAL_SHELL = item("final_shell", "material", Item::new);
        BLACK_GOLD = registrate().metal("black_gold");
        FLAME_STEEL = registrate().metal("flame_steel");
        BLACK_FLASH_ALLOY = registrate().metal("black_flash_alloy");
        COLOURED_GLAZE_STAR = item("coloured_glaze_star", "material", Item::new);
        ELFS_CRYSTAL = item("elfs_crystal", "material", Item::new);
        PLAGUE_BONE = item("plague_bone", "material", Item::new);
        COLORFUL_SLIME = registrate().metal("colorful_slime");
        KNIGHT_CRYSTAL = registrate().metal("knight_crystal");
        MEDAL_RIBBONS_CAST = registrate().castItem("medal_ribbons");
        MEDAL_BODY_CAST = registrate().castItem("medal_body");
        BLOWPIPE_TUBE_CAST = registrate().castItem("blowpipe_tube");
        BLOWPIPE_MOUTH_CAST = registrate().castItem("blowpipe_mouth");
        registrate().defaultCreativeTab(TinkersIngenuity.TOOLS.getKey());
        MEDAL_RIBBONS = part("medal_ribbons", "tinkers_medal", p -> new ToolPartItem(p, TIExtraMaterialStat.getCurioExtra()), 0, 3);
        MEDAL_BODY = part("medal_body", "tinkers_medal", p -> new ToolPartItem(p, CurioMainMaterialStat.ID), 0, -3);
        BLOWPIPE_TUBE = part("blowpipe_tube", "blowpipe", p -> new ToolPartItem(p, LimbMaterialStats.ID), 1, 1);
        BLOWPIPE_MOUTH = part("blowpipe_mouth", "blowpipe", p -> new ToolPartItem(p, GripMaterialStats.ID), -2, -2);
        TINKERS_MEDAL = registrate().simpleItem("tinkers_medal", () -> new ModifiableCurio(TIToolDefinitionGen.TINKERS_MEDAL));
        BLOWPIPE = registrate().simpleItem("blowpipe", () -> new ModifiableBlowpipe(TIToolDefinitionGen.BLOWPIPE));
    }

    public static void addItemsToTab(CreativeModeTab.ItemDisplayParameters ignored, CreativeModeTab.Output o) {
        TinkerUtils.addPartToTab(o, MEDAL_RIBBONS.get());
        TinkerUtils.addPartToTab(o, MEDAL_BODY.get());
        TinkerUtils.addPartToTab(o, BLOWPIPE_TUBE.get());
        TinkerUtils.addPartToTab(o, BLOWPIPE_MOUTH.get());
        TinkerUtils.addTooToTab(o, TINKERS_MEDAL.get());
        TinkerUtils.addTooToTab(o, BLOWPIPE.get());
    }

    public static <T extends Item> ItemEntry<T> part(String id, String tool, NonNullFunction<Item.Properties, T> factory, int x, int y) {
        return registrate().partItem(id, tool, factory, x, y).removeTab(TinkersIngenuity.TOOLS.getKey()).register();
    }

    public static <T extends Item> ItemEntry<T> item(String id, String path, NonNullFunction<Item.Properties, T> factory) {
        return registrate().item(id, factory).model((ctx, pvd) -> pvd.generated(ctx, new ResourceLocation[]{pvd.modLoc("item/" + path + "/" + ctx.getName())})).register();
    }

    public static TIRegistrate registrate() {
        return TinkersIngenuity.REGISTRATE;
    }

    public static void register() {
    }


}
