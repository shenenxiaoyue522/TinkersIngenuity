package com.xiaoyue.tinkers_ingenuity.register;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.xiaoyue.celestial_invoker.content.generic.SimpleEffect;
import com.xiaoyue.celestial_invoker.content.generic.builder.SimpleEffectBuilder;
import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class TIEffects {

    public static final RegistryEntry<SimpleEffect> DEADLY_PLAGUE;

    static {
        DEADLY_PLAGUE = effect("deadly_plague", () -> new SimpleEffect(MobEffectCategory.HARMFUL, -12619444,
                SimpleEffectBuilder.impl().effectTick((e, l) -> {
                    if (!e.isDeadOrDying()) {
                        e.hurt(e.damageSources().magic(), e.getHealth() * 0.01f * l);
                    }
                }).isEffective((time, l) -> time % 20 == 0)),
                "Plague damage is continuously taken based on current health");
    }

    private static <T extends MobEffect> RegistryEntry<T> effect(String name, NonNullSupplier<T> sup, String desc) {
        return TinkersIngenuity.REGISTRATE.simpleEffect(name, sup, desc).register();
    }

    public static void register() {
    }
}
