package com.xiaoyue.tinkers_ingenuity.data;

import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import com.xiaoyue.tinkers_ingenuity.register.TIFluids;
import com.xiaoyue.tinkers_ingenuity.register.TIItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.tconstruct.common.TinkerTags.Items;

public class TITagGen {
    public static final TagKey<Item> TINKERS_CURIO = ItemTags.create(new ResourceLocation("curios", "tinkers_curio"));

    public static void addItemTagGen(RegistrateItemTagsProvider pvd) {
        pvd.addTag(TINKERS_CURIO).add(TIItems.TINKERS_MEDAL.get());
        pvd.addTag(Items.MODIFIABLE).add(TIItems.TINKERS_MEDAL.get(), TIItems.BLOWPIPE.get());
        pvd.addTag(Items.MULTIPART_TOOL).add(TIItems.TINKERS_MEDAL.get(), TIItems.BLOWPIPE.get());
        pvd.addTag(Items.DURABILITY).add(TIItems.BLOWPIPE.get());
        pvd.addTag(Items.BOWS).add(TIItems.BLOWPIPE.get());
        pvd.addTag(Items.SMALL_TOOLS).add(TIItems.BLOWPIPE.get());
        pvd.addTag(Items.RANGED).add(TIItems.BLOWPIPE.get());
        pvd.addTag(Items.BONUS_SLOTS).add(TIItems.BLOWPIPE.get());
        pvd.addTag(Items.INTERACTABLE_LEFT).add(TIItems.BLOWPIPE.get());
        pvd.addTag(Items.MELEE).add(TIItems.BLOWPIPE.get());
    }

    public static void addFluidTagGen(RegistrateTagsProvider.IntrinsicImpl<Fluid> pvd) {
        for(FluidObject<ForgeFlowingFluid> fluid : TIFluids.FLUID_OBJECTS) {
            TagKey<Fluid> tag = fluid.getCommonTag();
            assert tag != null;
            pvd.addTag(tag).add(fluid.get());
        }

    }

    public static TagKey<Item> ingotTag(String id) {
        return ItemTags.create(new ResourceLocation("forge:ingots/" + id));
    }

    public static TagKey<Item> nuggetTag(String id) {
        return ItemTags.create(new ResourceLocation("forge:nuggets/" + id));
    }

    public static TagKey<Item> blockTag(String id) {
        return ItemTags.create(new ResourceLocation("forge:storage_blocks/" + id));
    }
}
