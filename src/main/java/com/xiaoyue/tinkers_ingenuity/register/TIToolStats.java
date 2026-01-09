package com.xiaoyue.tinkers_ingenuity.register;

import com.xiaoyue.tinkers_ingenuity.TinkersIngenuity;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;

public class TIToolStats {

    public static final FloatToolStat CURIO_MOVEMENT_SPEED, CURIO_MELEE_ATTACK, CURIO_PROJECTILE_ATTACK, CURIO_MAX_HEALTH,
            CURIO_ARMOR;

    static {
        CURIO_MOVEMENT_SPEED = TinkersIngenuity.REGISTRATE.floatToolStat("curio_movement_speed",
                "Movement Speed:",  "How much more can I increase your movement speed.",
                -8871731, 0f, -1f, 2048f);
        CURIO_MELEE_ATTACK = TinkersIngenuity.REGISTRATE.floatToolStat("curio_melee_attack",
                "Melee Attack Damage:", "How much armor can I improve for you.",
                -2661276, 0f, -4096f, 4096f);
        CURIO_PROJECTILE_ATTACK = TinkersIngenuity.REGISTRATE.floatToolStat("curio_projectile_attack",
                "Projectile Attack Damage:", "How much projectile attack damage can be increased for you.",
                -242829, 0f, -4096f, 4096f);
        CURIO_MAX_HEALTH = TinkersIngenuity.REGISTRATE.floatToolStat("curio_max_health",
                "Max Health:", "How much max life can be boosted for you.",
                -242829, 0f, -2048f, 2048f);
        CURIO_ARMOR = TinkersIngenuity.REGISTRATE.floatToolStat("curio_armor",
                "Armor:", "How much projectile attack damage can be increased for you.",
                -8042548, 0f, -2048f, 2048f);
    }

    public static void register() {
    }
}
