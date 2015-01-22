package com.dyonovan.tcnodetracker.gui;

import com.dyonovan.tcnodetracker.TCNodeTracker;
import com.dyonovan.tcnodetracker.lib.AspectLoc;
import com.dyonovan.tcnodetracker.lib.Constants;
import com.dyonovan.tcnodetracker.lib.NodeList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@SideOnly(Side.CLIENT)
public class GuiMain extends GuiScreen {

    private static final ResourceLocation nodes = new ResourceLocation("tcnodetracker:textures/gui/nodes.png");
    private static final ResourceLocation nodesSmall = new ResourceLocation("tcnodetracker:textures/gui/nodes_small.png");
    private static final ResourceLocation list = new ResourceLocation("tcnodetracker:textures/gui/list.png");
    public static ArrayList<AspectLoc> aspectList = new ArrayList<>();

    private final int WHITE = 0xFFFFFF;

    public GuiMain() {
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void updateScreen() {
        super.updateScreen();
    }

    public void drawDefaultBackground() {
        super.drawDefaultBackground();

        int i = 50;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(nodes);
        this.drawTexturedModalRect((this.width - 204) / 2, 1, 0, 0, 204, 35);
        this.mc.getTextureManager().bindTexture(list);
        //Header Sides
        this.drawTexturedModalRect(0, i, 0, 0, 10, 10);
        this.drawTexturedModalRect(0, i + 10, 0, 167, 10, 10);
        this.drawTexturedModalRect(this.width - 10, i, 238, 0, 10, 10);
        this.drawTexturedModalRect(this.width - 10, i + 10, 238, 167, 10, 10);
        //Header middle
        for (int x = 0; x < (this.width - 20); x++) {
            this.drawTexturedModalRect(x + 10, i, 10, 0, 1, 10);
            this.drawTexturedModalRect(x + 10, i + 10, 10, 167, 1, 10);
        }
        this.mc.getTextureManager().bindTexture(nodesSmall);
        this.drawTexturedModalRect(165, 52, 0, 0, 82, 16);
    }

    public void mouseMovedOrUp(int mouseX, int mouseY, int button) {
        int w = (this.width - 204) / 2;

        if (button >= 0) {
            if (mouseX >= w + 2 && mouseX <= w + 32 && mouseY >= 3 && mouseY <= 35) {
            sortNodes("aer");
    }
}



}

    private void sortNodes(String aspect) {

        aspectList.clear();

        for (NodeList n : TCNodeTracker.nodelist) {
            if (n.aspect.containsKey(aspect)) {

                aspectList.add(new AspectLoc(n.x, n.y, n.z, (int) Math.round(mc.thePlayer.getDistance(n.x, n.y, n.z)),
                        n.aspect.containsKey(Constants.AIR) ? (double) n.aspect.get(Constants.AIR) : 0,
                        n.aspect.containsKey(Constants.WATER) ? (double) n.aspect.get(Constants.WATER) : 0,
                        n.aspect.containsKey(Constants.FIRE) ? (double) n.aspect.get(Constants.FIRE) : 0,
                        n.aspect.containsKey(Constants.ORDER) ? (double) n.aspect.get(Constants.ORDER) : 0,
                        n.aspect.containsKey(Constants.ENTROPY) ? (double) n.aspect.get(Constants.ENTROPY) : 0,
                        n.aspect.containsKey(Constants.EARTH) ? (double) n.aspect.get(Constants.EARTH) : 0));
            }
        }
        Collections.sort(aspectList, new Comparator<AspectLoc>() {
            @Override
            public int compare(AspectLoc o1, AspectLoc o2) {
                return o1.distance - o2.distance;
            }
        });
    }


    public void drawScreen(int x, int y, float f) {

        int k = 20;
        int l = 80;


        drawDefaultBackground();

        String s1 = "Click aspect to get node list";

        this.fontRendererObj.drawString(s1, this.width / 2 - this.fontRendererObj.getStringWidth(s1) / 2, 40, WHITE);
        this.fontRendererObj.drawString("Distance", 5, 57, WHITE);
        this.fontRendererObj.drawString("X", k + 43, 57, WHITE);
        this.fontRendererObj.drawString("Y", k + 83, 57, WHITE);
        this.fontRendererObj.drawString("Z", k + 123, 57, WHITE);

        for (AspectLoc a : this.aspectList) {
            this.fontRendererObj.drawString(Integer.toString(a.distance), k, l, WHITE );
            this.fontRendererObj.drawString(Integer.toString(a.x), k + 40, l, WHITE );
            this.fontRendererObj.drawString(Integer.toString(a.y), k + 80, l, WHITE );
            this.fontRendererObj.drawString(Integer.toString(a.z), k + 120, l, WHITE );
            String s2 = Integer.toString((int) Math.round(a.hasAer));
            this.fontRendererObj.drawString(s2, k + 156 - (this.fontRendererObj.getStringWidth(s2) / 2), l, WHITE);
            s2 = Integer.toString((int) Math.round(a.hasAqua));
            this.fontRendererObj.drawString(s2, k + 176 - (this.fontRendererObj.getStringWidth(s2) / 2), l, WHITE);
            s2 = Integer.toString((int) Math.round(a.hasIgnis));
            this.fontRendererObj.drawString(s2, k + 196 - (this.fontRendererObj.getStringWidth(s2) / 2), l, WHITE);
            s2 = Integer.toString((int) Math.round(a.hasOrdo));
            this.fontRendererObj.drawString(s2, k + 216 - (this.fontRendererObj.getStringWidth(s2) / 2), l, WHITE);
            s2 = Integer.toString((int) Math.round(a.hasPerdito));
            this.fontRendererObj.drawString(s2, k + 236 - (this.fontRendererObj.getStringWidth(s2) / 2), l, WHITE);
            s2 = Integer.toString((int) Math.round(a.hasTerra));
            this.fontRendererObj.drawString(s2, k + 256 - (this.fontRendererObj.getStringWidth(s2) / 2), l, WHITE);
            l += 8;
        }

        super.drawScreen(x, y, f);

    }



}
