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
    public static ArrayList<AspectLoc> aspectList = new ArrayList<AspectLoc>();

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

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(nodes);
        this.drawTexturedModalRect((this.width - 204) / 2, 1, 0, 0, 204, 35);

    }

    public void mouseMovedOrUp(int mouseX, int mouseY, int button) {
        int w = (this.width - 204) / 2;

        if (button >= 0) {
            if (mouseX >= w + 2 && mouseX <= w + 32 && mouseY >= 3 && mouseY <= 35) {
                sortNodes(Constants.AIR);
            } else if (mouseX >= w + 35 && mouseX <= w + 66 && mouseY >= 3 && mouseY <= 35) {
                sortNodes(Constants.WATER);
            } else if (mouseX >= w + 70 && mouseX <= w + 101 && mouseY >= 3 && mouseY <= 35) {
                sortNodes(Constants.FIRE);
            } else if (mouseX >= w + 104 && mouseX <= w + 135 && mouseY >= 3 && mouseY <= 35) {
                sortNodes(Constants.ORDER);
            } else if (mouseX >= w + 139 && mouseX <= w + 170 && mouseY >= 3 && mouseY <= 35) {
                sortNodes(Constants.ENTROPY);
            } else if (mouseX >= w + 172 && mouseX <= w + 203 && mouseY >= 3 && mouseY <= 35) {
                sortNodes(Constants.EARTH);
            }
        }


    }

    private void sortNodes(String aspect) {

        aspectList.clear();

        for (NodeList n : TCNodeTracker.nodelist) {
            if (n.aspect.containsKey(aspect)) {

                aspectList.add(new AspectLoc(n.x, n.y, n.z, (int) Math.round(mc.thePlayer.getDistance(n.x, n.y, n.z)),
                        n.aspect.containsKey(Constants.AIR) ? n.aspect.get(Constants.AIR) : 0,
                        n.aspect.containsKey(Constants.WATER) ? n.aspect.get(Constants.WATER) : 0,
                        n.aspect.containsKey(Constants.FIRE) ? n.aspect.get(Constants.FIRE) : 0,
                        n.aspect.containsKey(Constants.ORDER) ? n.aspect.get(Constants.ORDER) : 0,
                        n.aspect.containsKey(Constants.ENTROPY) ? n.aspect.get(Constants.ENTROPY) : 0,
                        n.aspect.containsKey(Constants.EARTH) ? n.aspect.get(Constants.EARTH) : 0));
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

        int k = 25;
        int l = 86;


        drawDefaultBackground();

        String s1 = "Click aspect to get node list";

        this.fontRendererObj.drawString(s1, this.width / 2 - this.fontRendererObj.getStringWidth(s1) / 2, 40, Constants.WHITE);
        drawRect(0, 50, this.width, 52, -9408400);
        drawRect(0, 64, this.width, 66, -9408400);
        GL11.glScalef(.8F, .8F, .8F);
        this.fontRendererObj.drawString("Distance", 5, 69, Constants.WHITE);
        this.fontRendererObj.drawString("X", 62, 69, Constants.WHITE);
        this.fontRendererObj.drawString("Y", 102, 69, Constants.WHITE);
        this.fontRendererObj.drawString("Z", 142, 69, Constants.WHITE);
        s1 = "Aer  Aqua  Ignis  Ordo  Perditio  Terra";
        this.fontRendererObj.drawString(s1, 173, 69, Constants.WHITE);


        for (AspectLoc a : aspectList) {
            String s2 = Integer.toString(a.distance);
            this.fontRendererObj.drawString(s2, k - (this.fontRendererObj.getStringWidth(s2) / 2), l, Constants.WHITE);
            s2 = Integer.toString(a.x);
            this.fontRendererObj.drawString(s2, k + 40 - (this.fontRendererObj.getStringWidth(s2) / 2), l, Constants.WHITE);
            s2 = Integer.toString(a.y);
            this.fontRendererObj.drawString(s2, k + 80 - (this.fontRendererObj.getStringWidth(s2) / 2), l, Constants.WHITE);
            s2 = Integer.toString(a.z);
            this.fontRendererObj.drawString(s2, k + 120 - (this.fontRendererObj.getStringWidth(s2) / 2), l, Constants.WHITE);
            s2 = a.hasAer > 0 ? Integer.toString(a.hasAer) : "";
            this.fontRendererObj.drawString(s2, k + 156 - (this.fontRendererObj.getStringWidth(s2) / 2), l, Constants.WHITE);
            s2 = a.hasAqua > 0 ? Integer.toString(a.hasAqua) : "";
            this.fontRendererObj.drawString(s2, k + 187 - (this.fontRendererObj.getStringWidth(s2) / 2), l, Constants.WHITE);
            s2 = a.hasIgnis > 0 ? Integer.toString(a.hasIgnis) : "";
            this.fontRendererObj.drawString(s2, k + 217 - (this.fontRendererObj.getStringWidth(s2) / 2), l, Constants.WHITE);
            s2 = a.hasOrdo > 0 ? Integer.toString(a.hasOrdo) : "";
            this.fontRendererObj.drawString(s2, k + 250 - (this.fontRendererObj.getStringWidth(s2) / 2), l, Constants.WHITE);
            s2 = a.hasPerdito > 0 ? Integer.toString(a.hasPerdito) : "";
            this.fontRendererObj.drawString(s2, k + 288 - (this.fontRendererObj.getStringWidth(s2) / 2), l, Constants.WHITE);
            s2 = a.hasTerra > 0 ? Integer.toString(a.hasTerra) : "";
            this.fontRendererObj.drawString(s2, k + 331 - (this.fontRendererObj.getStringWidth(s2) / 2), l, Constants.WHITE);
            s2 = "DELETE";
            this.fontRendererObj.drawString(s2, k + 375 - (this.fontRendererObj.getStringWidth(s2) / 2), l, Constants.RED);
            s2 = "MARK";
            this.fontRendererObj.drawString(s2, k + 425 - (this.fontRendererObj.getStringWidth(s2) / 2), l, Constants.GREEN);
            l += 12;
        }

        super.drawScreen(x, y, f);

    }


}
