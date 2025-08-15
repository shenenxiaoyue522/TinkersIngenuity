package com.xiaoyue.tinkers_ingenuity.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.xiaoyue.tinkers_ingenuity.content.shared.material.MaterialDescData;
import com.xiaoyue.tinkers_ingenuity.data.material.TIMaterials;
import com.xiaoyue.tinkers_ingenuity.data.modifier.TIModifierData;
import com.xiaoyue.tinkers_ingenuity.register.TIFluids;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import slimeknights.mantle.registration.object.FluidObject;

public enum TILang {
    ;

    private final String key;
    private final String text;
    private String desc;

    TILang(String key, String text) {
        this.key = key;
        this.text = text;
    }

    TILang(String key, String text, String desc) {
        this.key = key;
        this.text = text;
        this.desc = desc;
    }

    public MutableComponent get(boolean isDesc) {
        return isDesc && this.desc != null ? Component.translatable(this.key + ".description") : Component.translatable(this.key);
    }

    public static void addLang(RegistrateLangProvider pvd) {
        for(TILang lang : values()) {
            pvd.add(lang.key, lang.text);
            if (lang.desc != null) {
                pvd.add(lang.key + ".description", lang.desc);
            }
        }

        for(FluidObject<ForgeFlowingFluid> fluid : TIFluids.FLUID_OBJECTS) {
            String fluid_name = fluid.getId().getPath();
            pvd.add(fluid.getType().getDescriptionId(), usName(fluid_name));
            pvd.add(fluid.getBucket().getDescriptionId(), usName(fluid_name + "_bucket"));
        }

        for(TIModifierData holder : TIModifierData.values()) {
            pvd.add(holder.langKey(), holder.asName());
            pvd.add(holder.flavorKey(), holder.flavor);
            pvd.add(holder.descKey(), holder.desc);
        }

        for(TIMaterials mate : TIMaterials.values()) {
            MaterialDescData id = mate.holder.desc();
            pvd.add(id.langKey(), id.name());
            id.caseAddLangData(pvd);
        }

    }

    private static String usName(String id) {
        return RegistrateLangProvider.toEnglishName(id);
    }
}
