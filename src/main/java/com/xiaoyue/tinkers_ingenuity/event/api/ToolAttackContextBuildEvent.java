package com.xiaoyue.tinkers_ingenuity.event.api;

import net.minecraftforge.eventbus.api.Event;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class ToolAttackContextBuildEvent extends Event {

    private final IToolStackView tool;
    private final ToolAttackContext context;

    public ToolAttackContextBuildEvent(IToolStackView tool, ToolAttackContext context) {
        this.tool = tool;
        this.context = context;
    }

    public IToolStackView getTool() {
        return this.tool;
    }

    public ToolAttackContext getContext() {
        return context;
    }
}
