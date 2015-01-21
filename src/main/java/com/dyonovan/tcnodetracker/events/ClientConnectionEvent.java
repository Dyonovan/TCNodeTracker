package com.dyonovan.tcnodetracker.events;

import com.dyonovan.tcnodetracker.TCNodeTracker;
import com.dyonovan.tcnodetracker.lib.JsonUtils;
import com.dyonovan.tcnodetracker.lib.Utils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.server.integrated.IntegratedServer;

import java.io.File;
import java.net.InetSocketAddress;

public class ClientConnectionEvent {

    @SubscribeEvent
    public void onConnected(FMLNetworkEvent.ClientConnectedToServerEvent event) {

        String hostname;

        if (!event.isLocal) {

            InetSocketAddress address = (InetSocketAddress) event.manager.getSocketAddress();
            hostname = address.getHostName() + "_" + address.getPort();

        } else {

            IntegratedServer server = Minecraft.getMinecraft().getIntegratedServer();
            hostname = (server != null) ? server.getFolderName() : "sp_world";
        }

        hostname = Utils.invalidChars(hostname);
        hostname = "TCNodeTracker/" + hostname;

        File fileJson = new File(hostname);
        if (!fileJson.exists()) {
            fileJson.mkdirs();
        }

        TCNodeTracker.hostName = hostname;

        JsonUtils.readJson();

    }

}
