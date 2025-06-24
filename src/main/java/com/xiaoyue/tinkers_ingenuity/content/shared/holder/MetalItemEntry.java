package com.xiaoyue.tinkers_ingenuity.content.shared.holder;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public record MetalItemEntry(ItemEntry<Item> ingot, ItemEntry<Item> nugget, BlockEntry<Block> block) {
}
