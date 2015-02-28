package com.dyonovan.tcnodetracker.events;

import com.dyonovan.tcnodetracker.TCNodeTracker;
import com.dyonovan.tcnodetracker.lib.DimList;
import com.dyonovan.tcnodetracker.lib.JsonUtils;
import com.dyonovan.tcnodetracker.lib.Utils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraftforge.common.DimensionManager;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Comparator;

public class ClientConnectionEvent {

    @SubscribeEvent
    public void onConnected(FMLNetworkEvent.ClientConnectedToServerEvent event) {

        String hostname;

        /*for (int i : DimensionManager.getStaticDimensionIDs())
            TCNodeTracker.dims.add(new DimList(i , DimensionManager.createProviderFor(i).getDimensionName()));

        Collections.sort(TCNodeTracker.dims, new Comparator<DimList>() {
            @Override
            public int compare(DimList o1, DimList o2) {
                return o1.dimID - o2.dimID;
            }
        });*/
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
        TCNodeTracker.nodelist.clear();

        JsonUtils.readJson();

    }

}
