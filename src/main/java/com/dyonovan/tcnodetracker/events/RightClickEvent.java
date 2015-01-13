package com.dyonovan.tcnodetracker.events;

import com.dyonovan.tcnodetracker.lib.NodeList;
import com.google.gson.Gson;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.INode;

import java.util.HashMap;
import java.util.Map;


public class RightClickEvent {


    @SubscribeEvent
    public void playerRightClick(PlayerInteractEvent event) {

        //if (event.isCanceled() || !event.world.isRemote ||
                //event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
        if (event.isCanceled() || !event.world.isRemote ||
                event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack heldItem = event.entityPlayer.inventory.getCurrentItem();
        if (!heldItem.getUnlocalizedName().equalsIgnoreCase("item.ItemThaumometer")) {
            return;
        }

        TileEntity i$ = event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z);

        if (i$ instanceof INode) {

            AspectList aspectList = ((INode) i$).getAspects();
            HashMap hm = new HashMap();

            for (Map.Entry<Aspect, Integer> entry : aspectList.aspects.entrySet()) {
                hm.put(entry.getKey().getTag(), entry.getValue());
            }


            //TODO add node type (Normal, Bright, etc)
            NodeList nodeList = new NodeList(hm, null, event.x, event.y, event.z);

            Gson gson = new Gson();
            String json = gson.toJson(nodeList);
            event.entityPlayer.addChatComponentMessage(new ChatComponentText(json));
        }
    }
}
