package com.xiaoyue.tinkers_ingenuity.register;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;

public class TIToolStats {

    public static final FloatToolStat CURIO_MOVEMENT_SPEED, CURIO_MELEE_ATTACK, CURIO_PROJECTILE_ATTACK, CURIO_MAX_HEALTH,
            CURIO_ARMOR;

    static {
        CURIO_MOVEMENT_SPEED = TinkersIngenuity.REGISTRATE.floatToolStat("curio_movement_speed",
                "Movement Speed:",  "How much more can I increase your movement speed.",
                -8871731, 0.0F, 0.0F, 2048.0F);
        CURIO_MELEE_ATTACK = TinkersIngenuity.REGISTRATE.floatToolStat("curio_melee_attack",
                "Melee Attack Damage:", "How much armor can I improve for you.",
                -2661276, 0.0F, 0.0F, 4096.0F);
        CURIO_PROJECTILE_ATTACK = TinkersIngenuity.REGISTRATE.floatToolStat("curio_projectile_attack",
                "Projectile Attack Damage:", "How much projectile attack damage can be increased for you.",
                -242829, 0.0F, 0.0F, 2048.0F);
        CURIO_MAX_HEALTH = TinkersIngenuity.REGISTRATE.floatToolStat("curio_max_health",
                "Max Health:", "How much max life can be boosted for you.",
                -242829, 0.0F, 0.0F, 2048.0F);
        CURIO_ARMOR = TinkersIngenuity.REGISTRATE.floatToolStat("curio_armor",
                "Armor:", "How much projectile attack damage can be increased for you.",
                -8042548, 0.0F, 0.0F, 2048.0F);
    }

    public static void register() {
    }
}
