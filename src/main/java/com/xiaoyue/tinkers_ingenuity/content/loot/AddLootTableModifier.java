package com.xiaoyue.tinkers_ingenuity.content.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.Objects;

public class AddLootTableModifier extends LootModifier {

    public static final Codec<AddLootTableModifier> CODEC = RecordCodecBuilder.create((
            inst) -> codecStart(inst).and(ResourceLocation.CODEC.fieldOf("lootTable")
            .forGetter((m) -> m.lootTable)).apply(inst, AddLootTableModifier::new));
    private final ResourceLocation lootTable;

    protected AddLootTableModifier(LootItemCondition[] conditionsIn, ResourceLocation lootTable) {
        super(conditionsIn);
        this.lootTable = lootTable;
    }

    public AddLootTableModifier(ResourceLocation lootTable, LootItemCondition... conditionsIn) {
        super(conditionsIn);
        this.lootTable = lootTable;
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        LootTable extraTable = context.getResolver().getLootTable(this.lootTable);
        ServerLevel level = context.getLevel();
        Objects.requireNonNull(generatedLoot);
        extraTable.getRandomItems(context, LootTable.createStackSplitter(level, generatedLoot::add));
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
