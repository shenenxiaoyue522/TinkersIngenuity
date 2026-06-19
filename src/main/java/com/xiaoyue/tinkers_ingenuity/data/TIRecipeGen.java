package com.xiaoyue.tinkers_ingenuity.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.xiaoyue.celestial_invoker.content.common.Bindings;
import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.json.condition.TIMaterialCondition;
import com.xiaoyue.tinkers_ingenuity.content.shared.holder.MaterialBuildHolder;
import com.xiaoyue.tinkers_ingenuity.content.shared.material.MaterialRecipeData;
import com.xiaoyue.tinkers_ingenuity.data.material.TIMaterials;
import com.xiaoyue.tinkers_ingenuity.data.modifier.TIModifierData;
import com.xiaoyue.tinkers_ingenuity.register.TIFluids;
import com.xiaoyue.tinkers_ingenuity.register.TIItems;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.CompoundIngredient;
import slimeknights.mantle.recipe.data.ConsumerWrapperBuilder;
import slimeknights.mantle.recipe.ingredient.SizedIngredient;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.common.registration.GeodeItemObject;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.ISmelteryRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.IToolRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.SmelteryRecipeBuilder;
import slimeknights.tconstruct.library.json.predicate.material.MaterialPredicate;
import slimeknights.tconstruct.library.json.predicate.material.MaterialStatTypePredicate;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.recipe.alloying.AlloyRecipeBuilder;
import slimeknights.tconstruct.library.recipe.casting.ItemCastingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.fuel.MeltingFuelBuilder;
import slimeknights.tconstruct.library.recipe.ingredient.MaterialValueIngredient;
import slimeknights.tconstruct.library.recipe.melting.MeltingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.modifiers.adding.ModifierRecipeBuilder;
import slimeknights.tconstruct.library.recipe.partbuilder.PartRecipeBuilder;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.shared.TinkerMaterials;
import slimeknights.tconstruct.shared.block.SlimeType;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.tables.TinkerTables;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.TinkerToolParts;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.data.ModifierIds;
import slimeknights.tconstruct.tools.stats.PlatingMaterialStats;
import slimeknights.tconstruct.world.TinkerWorld;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.tterrag.registrate.providers.RegistrateRecipeProvider.has;
import static com.xiaoyue.celestial_invoker.content.common.Bindings.unlock;

public class TIRecipeGen implements ISmelteryRecipeHelper, IMaterialRecipeHelper, IToolRecipeHelper {

    public static void acceptRecipe(RegistrateRecipeProvider pvd) {
        new TIRecipeGen().onRecipeGen(pvd);
    }

    private void vanillaRecipes(RegistrateRecipeProvider pvd) {
        String material = "craft/material/";
        unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TIItems.ELFS_CRYSTAL)::unlockedBy, Items.HEART_OF_THE_SEA)
                .requires(Items.HEART_OF_THE_SEA).requires(Items.DIAMOND).requires(Items.QUARTZ).requires(TinkerMaterials.cobalt.getIngot())
                .save(pvd, this.prefix(TIItems.ELFS_CRYSTAL.getId(), material));
        unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TIItems.PLAGUE_BONE)::unlockedBy, TinkerMaterials.venombone.get())
                .requires(TinkerMaterials.venombone).requires(Items.SOUL_SAND).requires(Items.NETHER_WART)
                .save(pvd, this.prefix(TIItems.PLAGUE_BONE.getId(), material));
        unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TIItems.SEA_SNAIL)::unlockedBy, Items.NAUTILUS_SHELL)
                .requires(Items.NAUTILUS_SHELL).requires(Items.GOLD_INGOT).requires(Items.DRIED_KELP_BLOCK).requires(Items.COPPER_INGOT)
                .save(pvd, this.prefix(TIItems.SEA_SNAIL.getId(), material));
        Bindings.metalCraft(pvd, "craft/", TIItems.BLACK_GOLD);
        Bindings.metalCraft(pvd, "craft/", TIItems.FLAME_STEEL);
        Bindings.metalCraft(pvd, "craft/", TIItems.BLACK_FLASH_ALLOY);
        Bindings.metalCraft(pvd, "craft/", TIItems.COLORFUL_SLIME);
        Bindings.metalCraft(pvd, "craft/", TIItems.KNIGHT_CRYSTAL);
        Bindings.metalCraft(pvd, "craft/", TIItems.MITHRIL);
    }

    protected void modifierRecipe(Consumer<FinishedRecipe> cons) {
        String ability = "tools/modifier/ability/";
        String abilitySalvage = "tools/modifier/ability/salvage/";
        String upgrade = "tools/modifier/upgrade/";
        String upgradeSalvage = "tools/modifier/upgrade/salvage/";
        String curioAbility = "tools/modifier/curio/ability/";
        String curioAbilitySalvage = "tools/modifier/curio/ability/salvage/";
        String curioUpgrade = "tools/modifier/curio/upgrade/";
        String curioUpgradeSalvage = "tools/modifier/curio/upgrade/salvage/";
        ModifierRecipeBuilder.modifier(TIModifierData.RAPID_FIRE.getId()).setTools(TinkerTags.Items.RANGED).setSlots(SlotType.ABILITY, 1)
                .addInput(Items.AMETHYST_BLOCK).addInput(Items.REDSTONE).addInput(Items.STRING)
                .saveSalvage(cons, prefix(TIModifierData.RAPID_FIRE.getId(), abilitySalvage))
                .setMaxLevel(1).save(cons, this.prefix(TIModifierData.RAPID_FIRE.getId(), ability));
        ModifierRecipeBuilder.modifier(TIModifierData.SCHOLAR.getId()).setTools(TITagGen.MODIFIABLE_CURIO).setSlots(SlotType.UPGRADE, 1)
                .addInput(Ingredient.of(Items.BOOK)).addInput(Items.EMERALD).setMaxLevel(5)
                .saveSalvage(cons, this.prefix(TIModifierData.SCHOLAR.getId(), curioUpgradeSalvage))
                .save(cons, this.prefix(TIModifierData.SCHOLAR.getId(), curioUpgrade));
        ModifierRecipeBuilder.modifier(TIModifierData.BLOT_OUT.getId()).setTools(TITagGen.MODIFIABLE_CURIO).setSlots(SlotType.ABILITY, 1)
                .addInput(Ingredient.of(ItemTags.WOOL)).addInput(Items.ENDER_PEARL)
                .saveSalvage(cons, this.prefix(TIModifierData.BLOT_OUT.getId(), curioAbilitySalvage))
                .setMaxLevel(1).save(cons, this.prefix(TIModifierData.BLOT_OUT.getId(), curioAbility));
        ModifierRecipeBuilder.modifier(TIModifierData.WALK_SNOW.getId()).setTools(TITagGen.MODIFIABLE_CURIO).setSlots(SlotType.UPGRADE, 1)
                .addInput(Items.LEATHER_BOOTS).setMaxLevel(1)
                .saveSalvage(cons, this.prefix(TIModifierData.WALK_SNOW.getId(), curioUpgradeSalvage))
                .save(cons, this.prefix(TIModifierData.WALK_SNOW.getId(), curioUpgrade));
        ModifierRecipeBuilder.modifier(TIModifierData.GOLDEN.getId()).setTools(TITagGen.MODIFIABLE_CURIO).setSlots(SlotType.UPGRADE, 1)
                .addInput(Items.GOLD_INGOT).addInput(Items.BLACKSTONE).setMaxLevel(1)
                .saveSalvage(cons, this.prefix(TIModifierData.GOLDEN.getId(), curioUpgradeSalvage))
                .save(cons, this.prefix(TIModifierData.GOLDEN.getId(), curioUpgrade));
        ModifierRecipeBuilder.modifier(TIModifierData.FLAME_HEART.getId()).setTools(TITagGen.MODIFIABLE_CURIO).setSlots(SlotType.ABILITY, 1)
                .addInput(Ingredient.of(Items.NETHERITE_INGOT)).addInput(Items.OBSIDIAN).addInput(Items.MAGMA_CREAM)
                .saveSalvage(cons, this.prefix(TIModifierData.FLAME_HEART.getId(), curioAbilitySalvage))
                .setMaxLevel(1).save(cons, this.prefix(TIModifierData.FLAME_HEART.getId(), curioAbility));
        ModifierRecipeBuilder.modifier(ModifierIds.reach).setTools(TITagGen.MODIFIABLE_CURIO).setSlots(SlotType.ABILITY, 1)
                .addInput(Items.PISTON).addInput(Items.PISTON).addInput(TinkerMaterials.queensSlime.getIngotTag())
                .addInput(Items.SLIME_BALL).addInput(Items.SLIME_BALL)
                .saveSalvage(cons, this.prefix(ModifierIds.reach, curioAbilitySalvage))
                .setMaxLevel(3).save(cons, this.prefix(ModifierIds.reach, curioAbility));
        ModifierRecipeBuilder.modifier(ModifierIds.strength).setTools(TITagGen.MODIFIABLE_CURIO).setSlots(SlotType.UPGRADE, 1)
                .addInput(SizedIngredient.of(Ingredient.of(Items.IRON_INGOT), 16))
                .addInput(Items.BLAZE_POWDER).setMaxLevel(5)
                .saveSalvage(cons, this.prefix(ModifierIds.strength, curioUpgradeSalvage))
                .save(cons, this.prefix(ModifierIds.strength, curioUpgrade));
        ModifierRecipeBuilder.modifier(ModifierIds.stepUp).setTools(TITagGen.MODIFIABLE_CURIO).setSlots(SlotType.ABILITY, 1)
                .addInput(Items.LEATHER).addInput(Items.LEATHER).addInput(Items.SCAFFOLDING).addInput(Items.SCAFFOLDING)
                .saveSalvage(cons, this.prefix(ModifierIds.stepUp, curioAbilitySalvage))
                .setMaxLevel(5).save(cons, this.prefix(ModifierIds.stepUp, curioAbility));
        ModifierRecipeBuilder.modifier(ModifierIds.haste).setTools(TITagGen.MODIFIABLE_CURIO).setSlots(SlotType.UPGRADE, 1)
                .addInput(SizedIngredient.of(Ingredient.of(Items.REDSTONE_BLOCK), 5))
                .saveSalvage(cons, this.prefix(ModifierIds.haste, curioUpgradeSalvage))
                .setMaxLevel(4).save(cons, this.prefix(ModifierIds.haste, curioUpgrade));
        ModifierRecipeBuilder.modifier(ModifierIds.swiftstrike).setTools(TITagGen.MODIFIABLE_CURIO).setSlots(SlotType.UPGRADE, 1)
                .addInput(SizedIngredient.of(Ingredient.of(Items.AMETHYST_BLOCK), 18))
                .saveSalvage(cons, this.prefix(ModifierIds.swiftstrike, curioUpgradeSalvage))
                .setMaxLevel(3).save(cons, this.prefix(ModifierIds.swiftstrike, curioUpgrade));
        ModifierRecipeBuilder.modifier(ModifierIds.protection).setTools(TITagGen.MODIFIABLE_CURIO).setSlots(SlotType.ABILITY, 1)
                .addInput(Ingredient.of(TinkerModifiers.goldReinforcement)).addInput(Ingredient.of(TinkerModifiers.obsidianReinforcement))
                .addInput(Ingredient.of(TinkerModifiers.ironReinforcement)).addInput(Ingredient.of(TinkerModifiers.searedReinforcement))
                .addInput(Ingredient.of(TinkerModifiers.cobaltReinforcement))
                .saveSalvage(cons, this.prefix(ModifierIds.swiftstrike, curioAbilitySalvage))
                .setMaxLevel(2).save(cons, this.prefix(ModifierIds.swiftstrike, curioAbility));
        ModifierRecipeBuilder.modifier(ModifierIds.projectileProtection).setTools(TITagGen.MODIFIABLE_CURIO).setSlots(SlotType.UPGRADE, 1)
                .addInput(SizedIngredient.of(Ingredient.of(TinkerModifiers.ironReinforcement), 6))
                .saveSalvage(cons, this.prefix(ModifierIds.projectileProtection, curioUpgradeSalvage))
                .setMaxLevel(3).save(cons, this.prefix(ModifierIds.projectileProtection, curioUpgrade));
        ModifierRecipeBuilder.modifier(ModifierIds.fireProtection).setTools(TITagGen.MODIFIABLE_CURIO).setSlots(SlotType.UPGRADE, 1)
                .addInput(SizedIngredient.of(Ingredient.of(TinkerModifiers.searedReinforcement), 6))
                .saveSalvage(cons, this.prefix(ModifierIds.fireProtection, curioUpgradeSalvage))
                .setMaxLevel(3).save(cons, this.prefix(ModifierIds.fireProtection, curioUpgrade));
        ModifierRecipeBuilder.modifier(ModifierIds.meleeProtection).setTools(TITagGen.MODIFIABLE_CURIO).setSlots(SlotType.UPGRADE, 1)
                .addInput(SizedIngredient.of(Ingredient.of(TinkerModifiers.cobaltReinforcement), 6))
                .saveSalvage(cons, this.prefix(ModifierIds.meleeProtection, curioUpgradeSalvage))
                .setMaxLevel(3).save(cons, this.prefix(ModifierIds.meleeProtection, curioUpgrade));
         ModifierRecipeBuilder.modifier(ModifierIds.blastProtection).setTools(TITagGen.MODIFIABLE_CURIO).setSlots(SlotType.UPGRADE, 1)
                .addInput(SizedIngredient.of(Ingredient.of(TinkerModifiers.emeraldReinforcement), 6))
                .saveSalvage(cons, this.prefix(ModifierIds.blastProtection, curioUpgradeSalvage))
                .setMaxLevel(3).save(cons, this.prefix(ModifierIds.blastProtection, curioUpgrade));
        ModifierRecipeBuilder.modifier(ModifierIds.magicProtection).setTools(TITagGen.MODIFIABLE_CURIO).setSlots(SlotType.UPGRADE, 1)
                .addInput(SizedIngredient.of(Ingredient.of(TinkerModifiers.goldReinforcement), 6))
                .saveSalvage(cons, this.prefix(ModifierIds.magicProtection, curioUpgradeSalvage))
                .setMaxLevel(3).save(cons, this.prefix(ModifierIds.magicProtection, curioUpgrade));
        ModifierRecipeBuilder.modifier(TinkerModifiers.magnetic).setTools(TITagGen.MODIFIABLE_CURIO).setSlots(SlotType.UPGRADE, 1)
                .addInput(Ingredient.of(Items.COMPASS)).setMaxLevel(3)
                .saveSalvage(cons, this.prefix(TinkerModifiers.magnetic, curioUpgradeSalvage))
                .save(cons, this.prefix(TinkerModifiers.magnetic, curioUpgrade));
        ModifierRecipeBuilder.modifier(ModifierIds.luck).setTools(TITagGen.MODIFIABLE_CURIO).setSlots(SlotType.ABILITY, 1)
                .addInput(Ingredient.of(Items.LAPIS_BLOCK)).addInput(Ingredient.of(Items.LAPIS_BLOCK))
                .addInput(Ingredient.of(Items.GOLD_BLOCK)).addInput(Ingredient.of(ItemTags.FLOWERS)).setMaxLevel(1)
                .saveSalvage(cons, this.prefix(ModifierIds.luck, curioAbilitySalvage))
                .save(cons, this.prefix(ModifierIds.luck, curioAbility));
        ModifierRecipeBuilder.modifier(ModifierIds.speedy).setTools(TITagGen.MODIFIABLE_CURIO).setSlots(SlotType.UPGRADE, 1)
                .addInput(Ingredient.of(Items.REDSTONE_BLOCK)).addInput(Ingredient.of(Items.REDSTONE_BLOCK))
                .addInput(Ingredient.of(Items.FEATHER)).addInput(Ingredient.of(Items.FEATHER))
                .saveSalvage(cons, this.prefix(ModifierIds.speedy, curioUpgradeSalvage))
                .setMaxLevel(2).save(cons, this.prefix(ModifierIds.speedy, curioUpgrade));
        ModifierRecipeBuilder.modifier(TIModifierData.OVERDRAFT.getId()).setTools(TinkerTags.Items.DURABILITY).setSlots(SlotType.UPGRADE, 1)
                .addInput(Ingredient.of(Items.SLIME_BALL)).addInput(Ingredient.of(Items.BUCKET))
                .saveSalvage(cons, this.prefix(TIModifierData.OVERDRAFT.getId(), upgradeSalvage))
                .setMaxLevel(5).save(cons, this.prefix(TIModifierData.OVERDRAFT.getId(), upgrade));
        ModifierRecipeBuilder.modifier(TIModifierData.SUPER_SHARP.getId()).setTools(TITagGen.METEOR_SPEAR).setSlots(SlotType.UPGRADE, 1)
                .addInput(Ingredient.of(Items.IRON_SWORD)).addInput(Ingredient.of(Items.ARROW))
                .addInput(Ingredient.of(TinkerMaterials.roseGold.getIngotTag()))
                .saveSalvage(cons, this.prefix(TIModifierData.SUPER_SHARP.getId(), upgradeSalvage))
                .setMaxLevel(5).save(cons, this.prefix(TIModifierData.SUPER_SHARP.getId(), upgrade));
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
        this.toolBuilding(cons, TIItems.METEOR_SPEAR.get(), building);
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
        this.metal(cons, TIFluids.MOLTEN_MITHRIL).metal();

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
        AlloyRecipeBuilder.alloy(TIFluids.MOLTEN_MITHRIL, 90)
                .addInput(TIFluids.TERRESTRIAL_SOLUTION.ingredient(180))
                .addInput(TinkerFluids.moltenClay.ingredient(250))
                .addInput(TinkerFluids.moltenEmerald.ingredient(200))
                .save(cons, this.prefix(TIFluids.MOLTEN_MITHRIL, alloys));

        ItemCastingRecipeBuilder.tableRecipe(TIItems.FINAL_SHELL).setCast(Items.SHULKER_SHELL, true)
                .setFluid(TIFluids.ENDER_COMPOUND.ingredient(100))
                .setCoolingTime(45)
                .save(cons, this.prefix(TIItems.FINAL_SHELL.getId(), casting));
        ItemCastingRecipeBuilder.tableRecipe(TIItems.COLOURED_GLAZE_STAR).setCast(Items.NETHER_STAR, true)
                .setFluid(TinkerFluids.moltenGlass.ingredient(2000))
                .setCoolingTime(80)
                .save(cons, this.prefix(TIItems.COLOURED_GLAZE_STAR.getId(), casting));
        ItemCastingRecipeBuilder.tableRecipe(TIItems.BLACK_DRAGON_SUBSTANCE).setCast(Items.DRAGON_HEAD, true)
                .setFluid(TIFluids.BLACK_DRAGON_GENE.ingredient(300))
                .setCoolingTime(30)
                .save(cons, this.prefix(TIItems.BLACK_DRAGON_SUBSTANCE.getId(), casting));
        ItemCastingRecipeBuilder.tableRecipe(TIItems.MITHRIL_REINFORCEMENT).setCast(TIItems.MITHRIL.ingot(), true)
                .setFluid(TinkerFluids.moltenGold.ingredient(720))
                .setCoolingTime(40)
                .save(cons, this.prefix(TIItems.MITHRIL_REINFORCEMENT.getId(), casting));

        MeltingRecipeBuilder.melting(Ingredient.of(TinkerModifiers.dragonScale),
                        TIFluids.BLACK_DRAGON_GENE.result(75), 1800, 20)
                .save(cons, this.prefix(TIFluids.BLACK_DRAGON_GENE.getId(), melting));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.DRAGON_BREATH),
                        TIFluids.DRAGON_BREATH.result(250), 1200, 30)
                .save(cons, this.prefix(TIFluids.DRAGON_BREATH.getId(), melting));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.ECHO_SHARD),
                        TIFluids.SCULK_GENE.result(45), 1200, 40)
                .save(cons, this.prefix(TIFluids.SCULK_GENE.getId(), melting));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerWorld.earthGeode.getBud(GeodeItemObject.BudSize.CLUSTER)),
                        TinkerFluids.slime.get(SlimeType.EARTH), 1000, 1)
                .addByproduct(TIFluids.TERRESTRIAL_SOLUTION.result(20))
                .save(cons, TConstruct.getResource("smeltery/melting/slime/earth/bud_cluster"));

        MeltingFuelBuilder.fuel(TIFluids.DRAGON_BREATH.ingredient(50), 120, 2200)
                .save(cons, this.location(fuel));
    }

    public SmelteryRecipeBuilder metal(Consumer<FinishedRecipe> consumer, FluidObject<?> fluid) {
        return this.molten(consumer, fluid).castingFolder("smeltery/casting/metal").meltingFolder("smeltery/melting/metal");
    }

    public void addMiscArmorRecipe(Consumer<FinishedRecipe> cons) {
        String parts = "misc_armor/parts/";
        for (TIMaterials value : TIMaterials.values()) {
            String prefix = parts + value.asMate().getPath() + "/";
            MaterialBuildHolder holder = value.holder;
            if (holder.definition() != null && holder.definition().craftable() && holder.stats() != null) {
                if (holder.stats().armor() != null) {
                    PartRecipeBuilder.partRecipe(TinkerToolParts.maille.get())
                            .setCost(2).setPattern(TinkerToolParts.maille.getId())
                            .setPatternItem(CompoundIngredient.of(Ingredient.of(TinkerTags.Items.DEFAULT_PATTERNS),
                                    Ingredient.of(TinkerSmeltery.mailleCast.get())))
                            .save(cons, prefix(TinkerToolParts.maille.getId(), prefix));
                    PartRecipeBuilder.partRecipe(TinkerToolParts.plating.get(ArmorItem.Type.HELMET))
                            .setCost(3).setPattern(TConstruct.getResource("helmet_plating"))
                            .setPatternItem(CompoundIngredient.of(Ingredient.of(TinkerTags.Items.DEFAULT_PATTERNS),
                                    Ingredient.of(TinkerSmeltery.helmetPlatingCast.get())))
                            .save(cons, prefix(TinkerToolParts.plating.get(ArmorItem.Type.HELMET).getStatType(), prefix));
                    PartRecipeBuilder.partRecipe(TinkerToolParts.plating.get(ArmorItem.Type.CHESTPLATE))
                            .setCost(6).setPattern(TConstruct.getResource("chestplate_plating"))
                            .setPatternItem(CompoundIngredient.of(Ingredient.of(TinkerTags.Items.DEFAULT_PATTERNS),
                                    Ingredient.of(TinkerSmeltery.chestplatePlatingCast.get())))
                            .save(cons, prefix(TinkerToolParts.plating.get(ArmorItem.Type.CHESTPLATE).getStatType(), prefix));
                    PartRecipeBuilder.partRecipe(TinkerToolParts.plating.get(ArmorItem.Type.LEGGINGS))
                            .setCost(5).setPattern(TConstruct.getResource("leggings_plating"))
                            .setPatternItem(CompoundIngredient.of(Ingredient.of(TinkerTags.Items.DEFAULT_PATTERNS),
                                    Ingredient.of(TinkerSmeltery.leggingsPlatingCast.get())))
                            .save(cons, prefix(TinkerToolParts.plating.get(ArmorItem.Type.LEGGINGS).getStatType(), prefix));
                    PartRecipeBuilder.partRecipe(TinkerToolParts.plating.get(ArmorItem.Type.BOOTS))
                            .setCost(3).setPattern(TConstruct.getResource("boots_plating"))
                            .setPatternItem(CompoundIngredient.of(Ingredient.of(TinkerTags.Items.DEFAULT_PATTERNS),
                                    Ingredient.of(TinkerSmeltery.bootsPlatingCast.get())))
                            .save(cons, prefix(TinkerToolParts.plating.get(ArmorItem.Type.BOOTS).getStatType(), prefix));
                }
            }
        }
        // travelers
        Consumer<FinishedRecipe> shapedMaterial = ConsumerWrapperBuilder.wrap(TinkerTables.shapedMaterialRecipeSerializer.get()).build(cons);
        Function<MaterialStatsId, Ingredient> materialsCosting = type -> MaterialValueIngredient.of(MaterialPredicate.and(TIMaterialCondition.NO_MOLTEN_INGENUITY_MATERIAL,
                new MaterialStatTypePredicate(type)), 1);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TinkerTools.travelersGear.get(ArmorItem.Type.HELMET))
                .pattern("l l")
                .pattern("glg")
                .pattern("c c")
                .define('c', materialsCosting.apply(PlatingMaterialStats.HELMET.getId()))
                .define('l', Tags.Items.LEATHER)
                .define('g', Tags.Items.GLASS_PANES_COLORLESS)
                .unlockedBy("has_item", has(Tags.Items.LEATHER))
                .save(shapedMaterial, TinkersIngenuity.loc("misc_armor/ingenuity_travelers_goggles"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TinkerTools.travelersGear.get(ArmorItem.Type.CHESTPLATE))
                .pattern("l l")
                .pattern("lcl")
                .pattern("lcl")
                .define('c', materialsCosting.apply(PlatingMaterialStats.CHESTPLATE.getId()))
                .define('l', Tags.Items.LEATHER)
                .unlockedBy("has_item", has(Tags.Items.LEATHER))
                .save(shapedMaterial, TinkersIngenuity.loc("misc_armor/ingenuity_travelers_chestplate"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TinkerTools.travelersGear.get(ArmorItem.Type.LEGGINGS))
                .pattern("lll")
                .pattern("c c")
                .pattern("l l")
                .define('c', materialsCosting.apply(PlatingMaterialStats.LEGGINGS.getId()))
                .define('l', Tags.Items.LEATHER)
                .unlockedBy("has_item", has(Tags.Items.LEATHER))
                .save(shapedMaterial, TinkersIngenuity.loc("misc_armor/ingenuity_travelers_pants"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TinkerTools.travelersGear.get(ArmorItem.Type.BOOTS))
                .pattern("c c")
                .pattern("l l")
                .define('c', materialsCosting.apply(PlatingMaterialStats.BOOTS.getId()))
                .define('l', Tags.Items.LEATHER)
                .unlockedBy("has_item", has(Tags.Items.LEATHER))
                .save(shapedMaterial, TinkersIngenuity.loc("misc_armor/ingenuity_travelers_boots"));
    }

    public void onRecipeGen(RegistrateRecipeProvider pvd) {
        this.vanillaRecipes(pvd);
        this.modifierRecipe(pvd);
        this.materialBuildRecipe(pvd);
        this.partToolRecipes(pvd);
        this.smelteryRecipes(pvd);
        this.addMiscArmorRecipe(pvd);
    }

    @Override
    public String getModId() {
        return "tinkers_ingenuity";
    }
}
