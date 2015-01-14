package com.dyonovan.tcnodetracker.events;

import com.dyonovan.tcnodetracker.lib.JsonUtils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class ClientTick {
    public static Long pastTime;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {

        if (pastTime == null) {
            pastTime = System.currentTimeMillis();
        }
        Long test = System.currentTimeMillis();
        if (test >= (pastTime + 900*1000)) {
            JsonUtils.writeJson();
            pastTime = System.currentTimeMillis();
        }

    }
}
