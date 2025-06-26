package com.xiaoyue.tinkers_ingenuity.data;

import com.tterrag.registrate.providers.loot.RegistrateLootTableProvider;
import com.xiaoyue.celestial_invoker.library.generator.RegistrateLootModifierProvider;
import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.loot.AddLootTableModifier;
import com.xiaoyue.tinkers_ingenuity.register.TIItems;
import dev.xkmc.l2library.util.data.LootTableTemplate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class TILootTableGen {

    public static final ResourceLocation MINESHAFT = TinkersIngenuity.loc("abandoned_mineshaft");

    public static void onLootModifier(RegistrateLootModifierProvider pvd) {
        pvd.add("chests/abandoned_mineshaft", new AddLootTableModifier(TILootTableGen.MINESHAFT,
                new LootTableIdCondition.Builder(BuiltInLootTables.ABANDONED_MINESHAFT).build()));
    }

    public static void onLootGen(RegistrateLootTableProvider pvd) {
        pvd.addLootAction(LootContextParamSets.EMPTY, cons -> {
            cons.accept(MINESHAFT, LootTable.lootTable().withPool(LootPool.lootPool()
                    .add(LootTableTemplate.getItem(TIItems.MITHRIL.ingot().asItem(), 1).setWeight(1))
                    .add(LootTableTemplate.getItem(Items.AIR, 0).setWeight(2))
            ));
        });
    }

}
