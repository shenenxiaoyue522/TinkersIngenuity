package com.xiaoyue.tinkers_ingenuity.data.tools;

import com.xiaoyue.tinkers_ingenuity.register.TIItems;
import net.minecraft.data.PackOutput;
import slimeknights.tconstruct.library.data.tinkering.AbstractStationSlotLayoutProvider;
import slimeknights.tconstruct.tools.TinkerToolParts;

public class TIStationLayoutGen extends AbstractStationSlotLayoutProvider {
    public TIStationLayoutGen(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void addLayouts() {
        this.defineModifiable(TIItems.METEOR_SPEAR)
                .sortIndex(2)
                .addInputItem(TinkerToolParts.smallBlade, 45, 26)
                .addInputItem(TinkerToolParts.toughHandle, 25, 46)
                .addInputItem(TinkerToolParts.largePlate, 45, 46)
                .addInputItem(TinkerToolParts.largePlate, 25, 26)
                .addInputItem(TinkerToolParts.toughHandle, 7, 62)
                .build();
        this.defineModifiable(TIItems.TINKERS_MEDAL)
                .sortIndex(2)
                .addInputItem(TIItems.MEDAL_RIBBONS, 39, 35)
                .addInputItem(TIItems.MEDAL_BODY, 21, 53)
                .build();
        this.defineModifiable(TIItems.BLOWPIPE)
                .sortIndex(2)
                .addInputItem(TIItems.BLOWPIPE_TUBE, 39, 35)
                .addInputItem(TIItems.BLOWPIPE_MOUTH, 21, 53)
                .build();
    }

    @Override
    public String getName() {
        return "Tinkers Ingenuity station layout providers";
    }
}
