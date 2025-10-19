package com.xiaoyue.tinkers_ingenuity.data.material;

import com.xiaoyue.tinkers_ingenuity.content.shared.material.MaterialStatsData;
import com.xiaoyue.tinkers_ingenuity.content.shared.stats.CurioMainMaterialStat;
import com.xiaoyue.tinkers_ingenuity.content.shared.stats.TIExtraMaterialStat;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Tier;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialStatsDataProvider;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import slimeknights.tconstruct.tools.stats.*;

public class TIMaterialStatGen extends AbstractMaterialStatsDataProvider {
    public TIMaterialStatGen(PackOutput packOutput, AbstractMaterialDataProvider materials) {
        super(packOutput, materials);
    }

    private void addVanillaCurioMaterialStats() {
        this.addAllCurioPart(MaterialIds.wood, 0.01f, 0.0f, 0.02f, 0.03f, 0.03f);
        this.addAllCurioPart(MaterialIds.bone, 0.03f, 2.0f, 0.05f, 0.08f, 0.06f);
        this.addAllCurioPart(MaterialIds.flint, 0.0f, 0.0f, 0.04f, 0.05f, 0.04f);
        this.addAllCurioPart(MaterialIds.copper, 0.04f, 4.0f, 0.08f, 0.07f, 0.05f);
        this.addAllCurioPart(MaterialIds.necroticBone, 0.02f, 7.0f, 0.08f, 0.09f, 0.08f);
        this.addAllCurioPart(MaterialIds.amethystBronze, 0.06f, 8.0f, 0.12f, 0.12f, 0.1f);
        this.addAllCurioPart(MaterialIds.obsidian, -0.02f, 8.0f, 0.15f, 0.13f, 0.12f);
        this.addAllCurioPart(MaterialIds.cobalt, 0.1f, 10.0f, 0.18f, 0.13f, 0.11f);
        this.addAllCurioPart(MaterialIds.manyullyn, 0.05f, 12.0f, 0.2f, 0.18f, 0.16f);
    }

    @Override
    protected void addMaterialStats() {
        for(TIMaterials mate : TIMaterials.values()) {
            MaterialStatsData stats = mate.holder.stats();
            if (stats != null) {
                this.addMaterialStats(mate.asMate(), stats.stats());
                if (stats.armor() != null && stats.shield()) {
                    this.addArmorShieldStats(mate.asMate(), stats.armor());
                } else if (stats.armor() != null) {
                    this.addArmorStats(mate.asMate(), stats.armor());
                }
            }
        }
        this.addVanillaCurioMaterialStats();
    }

    public void addAllCurioPart(MaterialId mat, float speed, float hp, float armor, float melee, float proj) {
        CurioMainMaterialStat main = new CurioMainMaterialStat(speed, hp, armor, melee, proj);
        this.addMaterialStats(mat, main, TIExtraMaterialStat.CURIO_EXTRA);
    }

    public static HeadMaterialStats head(int dur, float min, Tier tier, float attack) {
        return new HeadMaterialStats(dur, min, tier, attack);
    }

    public static HandleMaterialStats.Builder handle() {
        return HandleMaterialStats.multipliers();
    }

    public static LimbMaterialStats limb(int dur, float draw, float vel, float acc) {
        return new LimbMaterialStats(dur, draw, vel, acc);
    }

    public static GripMaterialStats grip(float dur, float melee, float acc) {
        return new GripMaterialStats(dur, acc, melee);
    }

    public static PlatingMaterialStats.Builder plat(float dur) {
        return PlatingMaterialStats.builder().durabilityFactor(dur);
    }

    @Override
    public String getName() {
        return "Tinkers Ingenuity material stats providers";
    }
}
