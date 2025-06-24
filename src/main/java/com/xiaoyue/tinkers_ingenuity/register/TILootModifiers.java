package com.xiaoyue.tinkers_ingenuity.register;

import com.mojang.serialization.Codec;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.loot.AddLootTableModifier;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.ForgeRegistries.Keys;

public class TILootModifiers {

    public static final RegistryEntry<Codec<AddLootTableModifier>> ADD_LOOT_TABLE;

    static {
        ADD_LOOT_TABLE = reg("add_loot_table", () -> AddLootTableModifier.CODEC);
    }

    private static <T extends IGlobalLootModifier> RegistryEntry<Codec<T>> reg(String str, NonNullSupplier<Codec<T>> codec) {
        return TinkersIngenuity.REGISTRATE.simple(str, Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, codec);
    }

    public static void register() {
    }
}
