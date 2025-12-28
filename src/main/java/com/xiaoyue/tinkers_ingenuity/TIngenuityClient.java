package com.xiaoyue.tinkers_ingenuity;

import com.xiaoyue.tinkers_ingenuity.register.TIItems;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import slimeknights.tconstruct.library.client.model.TinkerItemProperties;

import static com.xiaoyue.tinkers_ingenuity.TinkersIngenuity.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class TIngenuityClient {

    @SubscribeEvent
    public static void onClientStep(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            TinkerItemProperties.registerToolProperties(TIItems.BLOWPIPE.get());
            TinkerItemProperties.registerToolProperties(TIItems.METEOR_SPEAR.get());
        });
    }

}
