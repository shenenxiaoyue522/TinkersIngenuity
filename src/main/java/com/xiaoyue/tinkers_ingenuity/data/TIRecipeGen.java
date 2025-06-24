package com.xiaoyue.tinkers_ingenuity.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.xiaoyue.tinkers_ingenuity.content.shared.holder.MetalItemEntry;
import com.xiaoyue.tinkers_ingenuity.content.shared.material.MaterialRecipeData;
import com.xiaoyue.tinkers_ingenuity.data.material.TIMaterials;
import com.xiaoyue.tinkers_ingenuity.data.modifier.TIModifierData;
import com.xiaoyue.tinkers_ingenuity.register.TIFluids;
import com.xiaoyue.tinkers_ingenuity.register.TIItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.ISmelteryRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.IToolRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.SmelteryRecipeBuilder;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.recipe.alloying.AlloyRecipeBuilder;
import slimeknights.tconstruct.library.recipe.casting.ItemCastingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.fuel.MeltingFuelBuilder;
import slimeknights.tconstruct.library.recipe.melting.MeltingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.modifiers.adding.ModifierRecipeBuilder;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.shared.TinkerMaterials;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class TIRecipeGen implements ISmelteryRecipeHelper, IMaterialRecipeHelper, IToolRecipeHelper {
    public static void acceptRecipe(RegistrateRecipeProvider pvd) {
        (new TIRecipeGen()).onRecipeGen(pvd);
    }

    private void vanillaRecipes(RegistrateRecipeProvider pvd) {
        String material = "craft/material/";
        this.unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TIItems.ELFS_CRYSTAL)::unlockedBy, Items.HEART_OF_THE_SEA)
        .requires(Items.HEART_OF_THE_SEA).requires(Items.DIAMOND).requires(Items.QUARTZ).requires(TinkerMaterials.cobalt.getIngot())
                .save(pvd, this.prefix(TIItems.ELFS_CRYSTAL.getId(), material));
        this.unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TIItems.PLAGUE_BONE)::unlockedBy, TinkerMaterials.venombone.get())
                .requires(TinkerMaterials.venombone).requires(Items.SOUL_SAND).requires(Items.NETHER_WART)
                .save(pvd, this.prefix(TIItems.PLAGUE_BONE.getId(), material));
        this.metalCraft(pvd, TIItems.BLACK_GOLD);
        this.metalCraft(pvd, TIItems.FLAME_STEEL);
        this.metalCraft(pvd, TIItems.BLACK_FLASH_ALLOY);
        this.metalCraft(pvd, TIItems.COLORFUL_SLIME);
        this.metalCraft(pvd, TIItems.KNIGHT_CRYSTAL);
    }

    public void metalCraft(RegistrateRecipeProvider pvd, MetalItemEntry entry) {
        this.unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, entry.block())::unlockedBy, entry.ingot().get())
                .pattern("XXX").pattern("XXX").pattern("XXX")
                .define('X', entry.ingot())
                .save(pvd, this.prefix(entry.block().getId(), "block_from_ingot/"));
        this.unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, entry.ingot())::unlockedBy, entry.nugget().get())
                .pattern("XXX").pattern("XXX").pattern("XXX").define('X', entry.nugget())
                .save(pvd, this.prefix(entry.block().getId(), "ingot_from_nugget/"));
        this.unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, entry.ingot())::unlockedBy, entry.block().asItem())
                .requires(entry.block()).save(pvd, this.prefix(entry.ingot().getId(), "ingot_from_block/"));
        this.unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, entry.nugget())::unlockedBy, entry.ingot().get())
                .requires(entry.ingot()).save(pvd, this.prefix(entry.ingot().getId(), "nugget_from_ingot/"));
    }

    private <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
        return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCritereon(pvd));
    }

    protected void modifierRecipe(Consumer<FinishedRecipe> cons) {
        String ability = "tools/modifier/ability/";
        ModifierRecipeBuilder.modifier(TIModifierData.RAPID_FIRE.getId()).setTools(TinkerTags.Items.RANGED).setSlots(SlotType.ABILITY, 1)
                .addInput(Items.AMETHYST_BLOCK).addInput(Items.REDSTONE).addInput(Items.STRING)
                .save(cons, this.prefix(TIModifierData.RAPID_FIRE.getId(), ability));
    }

    protected void materialBuildRecipe(Consumer<FinishedRecipe> cons) {
        for(TIMaterials material : TIMaterials.values()) {
            MaterialRecipeData recipe = material.holder.recipe();
            if (recipe != null) {
                if (recipe.craftItem() != null) {
                    this.addMaterial(cons, material.holder.asMate(), recipe.craftItem());
                }
                if (recipe.meltingFluid() != null) {
                    this.addMeltingMaterial(cons, material.holder.asMate(), recipe.meltingFluid());
                }
            }
        }
    }

    private void addMaterial(Consumer<FinishedRecipe> cons, MaterialId id, ItemLike input) {
        this.materialRecipe(cons, id, Ingredient.of(input), 1, 1, "tools/materials/" + id.getPath());
    }

    private void addMeltingMaterial(Consumer<FinishedRecipe> cons, MaterialId id, FluidObject<?> fluid) {
        this.metalMaterialRecipe(cons, id, "tools/materials/", id.getPath(), false);
        this.materialMeltingCasting(cons, id, fluid, "tools/materials/");
    }

    private void partToolRecipes(Consumer<FinishedRecipe> cons) {
        String building = "tools/building/";
        String part = "tools/parts/";
        String cast = "smeltery/casts/";
        this.toolBuilding(cons, TIItems.TINKERS_MEDAL.get(), building);
        this.partRecipes(cons, TIItems.MEDAL_RIBBONS.get(), this.cast("medal_ribbons", TIItems.MEDAL_RIBBONS_CAST), 4, part, cast);
        this.partRecipes(cons, TIItems.MEDAL_BODY.get(), this.cast("medal_body", TIItems.MEDAL_BODY_CAST), 4, part, cast);
        this.toolBuilding(cons, TIItems.BLOWPIPE.get(), building);
        this.partRecipes(cons, TIItems.BLOWPIPE_TUBE.get(), this.cast("blowpipe_tube", TIItems.BLOWPIPE_TUBE_CAST), 4, part, cast);
        this.partRecipes(cons, TIItems.BLOWPIPE_MOUTH.get(), this.cast("blowpipe_mouth", TIItems.BLOWPIPE_MOUTH_CAST), 2, part, cast);
    }

    private CastItemObject cast(String id, List<ItemEntry<Item>> casts) {
        return new CastItemObject(this.location(id), casts.get(0).get(), casts.get(1).get(), casts.get(2).get());
    }

    private void smelteryRecipes(Consumer<FinishedRecipe> cons) {
        String alloys = "smeltery/alloys/";
        String special = "smeltery/special/";
        String casting = "smeltery/casting/";
        String melting = "smeltery/melting/";
        String fuel = "smeltery/fuel/";
        this.metal(cons, TIFluids.MOLTEN_BLACK_GOLD).metal();
        this.metal(cons, TIFluids.MOLTEN_FLAME_STEEL).metal();
        this.metal(cons, TIFluids.MOLTEN_BLACK_FLASH_ALLOY).metal();
        this.metal(cons, TIFluids.MOLTEN_COLORFUL_SLIME).metal();
        this.metal(cons, TIFluids.MOLTEN_KNIGHT_CRYSTAL).metal();

        AlloyRecipeBuilder.alloy(TIFluids.MOLTEN_BLACK_GOLD, 90)
                .addInput(TinkerFluids.moltenGold.ingredient(180))
                .addInput(TinkerFluids.moltenObsidian.ingredient(100))
                .save(cons, this.prefix(TIFluids.MOLTEN_BLACK_GOLD, alloys));
        AlloyRecipeBuilder.alloy(TIFluids.MOLTEN_FLAME_STEEL, 90)
                .addInput(TinkerFluids.blazingBlood.ingredient(500))
                .addInput(TinkerFluids.moltenDebris.ingredient(200))
                .addInput(TinkerFluids.moltenSteel.ingredient(90))
                .save(cons, this.prefix(TIFluids.MOLTEN_FLAME_STEEL, alloys));
        AlloyRecipeBuilder.alloy(TIFluids.MOLTEN_BLACK_FLASH_ALLOY, 90)
                .addInput(TinkerFluids.moltenDiamond.ingredient(200))
                .addInput(TinkerFluids.moltenManyullyn.ingredient(270))
                .addInput(TIFluids.SCULK_GENE.ingredient(270))
                .save(cons, this.prefix(TIFluids.MOLTEN_BLACK_FLASH_ALLOY, alloys));
        AlloyRecipeBuilder.alloy(TIFluids.MOLTEN_COLORFUL_SLIME, 90)
                .addInput(TinkerFluids.skySlime.ingredient(750))
                .addInput(TinkerFluids.magma.ingredient(750))
                .addInput(TinkerFluids.enderSlime.ingredient(750))
                .addInput(TinkerFluids.moltenHepatizon.ingredient(90))
                .save(cons, this.prefix(TIFluids.MOLTEN_COLORFUL_SLIME, alloys));
        AlloyRecipeBuilder.alloy(TIFluids.ENDER_COMPOUND, 100)
                .addInput(TinkerFluids.enderSlime.ingredient(1000))
                .addInput(TinkerFluids.moltenEnder.ingredient(500))
                .addInput(TIFluids.DRAGON_BREATH.ingredient(500))
                .save(cons, this.prefix(TIFluids.ENDER_COMPOUND, special));
        AlloyRecipeBuilder.alloy(TIFluids.MOLTEN_KNIGHT_CRYSTAL, 90)
                .addInput(TinkerFluids.moltenDebris.ingredient(180))
                .addInput(TinkerFluids.moltenDiamond.ingredient(150))
                .addInput(TinkerFluids.moltenAmethyst.ingredient(300))
                .save(cons, this.prefix(TIFluids.MOLTEN_KNIGHT_CRYSTAL, alloys));

        ItemCastingRecipeBuilder.tableRecipe(TIItems.FINAL_SHELL).setCast(Items.SHULKER_SHELL, true)
                .setFluid(TIFluids.ENDER_COMPOUND.ingredient(100))
                .setCoolingTime(45)
                .save(cons, this.prefix(TIItems.FINAL_SHELL.getId(), casting));
        ItemCastingRecipeBuilder.tableRecipe(TIItems.COLOURED_GLAZE_STAR).setCast(Items.NETHER_STAR, true)
                .setFluid(TinkerFluids.moltenGlass.ingredient(2000))
                .setCoolingTime(80)
                .save(cons, this.prefix(TIItems.COLOURED_GLAZE_STAR.getId(), casting));

        MeltingRecipeBuilder.melting(Ingredient.of(Items.DRAGON_BREATH),
                TIFluids.DRAGON_BREATH.result(250), 1200, 30)
                .save(cons, this.prefix(TIFluids.DRAGON_BREATH.getId(), melting));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.ECHO_SHARD),
                TIFluids.SCULK_GENE.result(45), 1200, 40)
                .save(cons, this.prefix(TIFluids.SCULK_GENE.getId(), melting));

        MeltingFuelBuilder.fuel(TIFluids.DRAGON_BREATH.ingredient(50), 120, 2200)
                .save(cons, this.location(fuel));
    }

    public SmelteryRecipeBuilder metal(Consumer<FinishedRecipe> consumer, FluidObject<?> fluid) {
        return this.molten(consumer, fluid).castingFolder("smeltery/casting/metal").meltingFolder("smeltery/melting/metal");
    }

    public void onRecipeGen(RegistrateRecipeProvider pvd) {
        this.vanillaRecipes(pvd);
        this.modifierRecipe(pvd);
        this.materialBuildRecipe(pvd);
        this.partToolRecipes(pvd);
        this.smelteryRecipes(pvd);
    }

    @Override
    public String getModId() {
        return "tinkers_ingenuity";
    }
}
