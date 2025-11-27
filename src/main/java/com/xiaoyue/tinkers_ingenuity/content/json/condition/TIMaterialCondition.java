package com.xiaoyue.tinkers_ingenuity.content.json.condition;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import com.xiaoyue.tinkers_ingenuity.content.generic.SerialLoader;
import com.xiaoyue.tinkers_ingenuity.content.shared.holder.MaterialBuildHolder;
import com.xiaoyue.tinkers_ingenuity.data.material.TIMaterials;
import slimeknights.tconstruct.library.json.predicate.material.MaterialPredicate;

public class TIMaterialCondition {

    public static final MaterialPredicate NO_MOLTEN_INGENUITY_MATERIAL = MaterialPredicate.simple(material -> {
        for (TIMaterials mate : TIMaterials.values()) {
            if (!mate.asMate().equals(material.getId())) continue;
            MaterialBuildHolder holder = mate.holder;
            if (holder.definition() != null && holder.definition().craftable() && holder.stats() != null && holder.stats().armor() != null) {
                return true;
            }
        }
        return false;
    });

    @SerialLoader
    public static void registerPredicate() {
        MaterialPredicate.LOADER.register(TinkersIngenuity.loc("no_molten_ingenuity_material"), NO_MOLTEN_INGENUITY_MATERIAL.getLoader());
    }
}
