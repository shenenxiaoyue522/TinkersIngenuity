package com.xiaoyue.tinkers_ingenuity.data.tools;

import com.xiaoyue.tinkers_ingenuity.register.TIItems;
import net.minecraft.data.PackOutput;
import slimeknights.tconstruct.library.data.tinkering.AbstractToolDefinitionDataProvider;
import slimeknights.tconstruct.library.materials.RandomMaterial;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.definition.module.build.MultiplyStatsModule;
import slimeknights.tconstruct.library.tools.definition.module.build.SetStatsModule;
import slimeknights.tconstruct.library.tools.definition.module.build.ToolSlotsModule;
import slimeknights.tconstruct.library.tools.definition.module.build.ToolTraitsModule;
import slimeknights.tconstruct.library.tools.definition.module.material.DefaultMaterialsModule;
import slimeknights.tconstruct.library.tools.definition.module.material.PartStatsModule;
import slimeknights.tconstruct.library.tools.nbt.MultiplierNBT;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.TinkerToolParts;
import slimeknights.tconstruct.tools.data.ModifierIds;

public class TIToolDefinitionGen extends AbstractToolDefinitionDataProvider {
    public TIToolDefinitionGen(PackOutput packOutput) {
        super(packOutput, "tinkers_ingenuity");
    }

    public static final ToolDefinition METEOR_SPEAR = ToolDefinition.create(TIItems.METEOR_SPEAR.getId());
    public static final ToolDefinition TINKERS_MEDAL = ToolDefinition.create(TIItems.TINKERS_MEDAL.getId());
    public static final ToolDefinition BLOWPIPE = ToolDefinition.create(TIItems.BLOWPIPE.getId());

    @Override
    protected void addToolDefinitions() {
        RandomMaterial randomMaterial = RandomMaterial.random().tier(1).build();
        this.define(METEOR_SPEAR).module(PartStatsModule.parts()
                        .part(TinkerToolParts.smallBlade, 0.8f)
                        .part(TinkerToolParts.toughHandle, 0.8f)
                        .part(TinkerToolParts.largePlate)
                        .part(TinkerToolParts.largePlate)
                        .part(TinkerToolParts.toughHandle, 0.8f).build())
                .module(DefaultMaterialsModule.builder()
                        .material(randomMaterial, randomMaterial, randomMaterial, randomMaterial, randomMaterial).build())
                .module(ToolTraitsModule.builder()
                        .trait(ModifierIds.throwing).build())
                .module(new SetStatsModule(StatsNBT.builder()
                        .set(ToolStats.BLOCK_AMOUNT, 10f)
                        .set(ToolStats.ATTACK_DAMAGE, 2f)
                        .set(ToolStats.ATTACK_SPEED, 1.3f).build()))
                .module(new MultiplyStatsModule(MultiplierNBT.builder()
                        .set(ToolStats.MINING_SPEED, 0.65f)
                        .set(ToolStats.ATTACK_DAMAGE, 1.2f).build()))
                .module(ToolSlotsModule.builder()
                        .slots(SlotType.UPGRADE, 1)
                        .slots(SlotType.ABILITY, 1).build());
        this.define(TINKERS_MEDAL).module(PartStatsModule.parts()
                        .part(TIItems.MEDAL_RIBBONS)
                        .part(TIItems.MEDAL_BODY).build())
                .module(DefaultMaterialsModule.builder()
                        .material(new RandomMaterial[]{randomMaterial, randomMaterial}).build())
                .smallToolStartingSlots().build();
        this.define(BLOWPIPE).module(new SetStatsModule(StatsNBT.builder()
                        .set(ToolStats.ACCURACY, 0.85f).build()))
                .module(new MultiplyStatsModule(MultiplierNBT.builder()
                        .set(ToolStats.DURABILITY, 0.9f).set(ToolStats.VELOCITY, 0.9f).build()))
                .module(PartStatsModule.parts()
                        .part(TIItems.BLOWPIPE_TUBE)
                        .part(TIItems.BLOWPIPE_MOUTH).build())
                .module(DefaultMaterialsModule.builder()
                        .material(new RandomMaterial[]{randomMaterial, randomMaterial}).build())
                .smallToolStartingSlots().build();
    }

    @Override
    public String getName() {
        return "Tinkers Ingenuity tool definition data providers";
    }
}
