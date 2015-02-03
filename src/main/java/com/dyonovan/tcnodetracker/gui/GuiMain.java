package com.dyonovan.tcnodetracker.gui;

import com.dyonovan.tcnodetracker.TCNodeTracker;
import com.dyonovan.tcnodetracker.lib.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.DimensionManager;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

@SideOnly(Side.CLIENT)
public class GuiMain extends GuiScreen {

    private static final ResourceLocation nodes = new ResourceLocation("tcnodetracker:textures/gui/nodes.png");
    private static final ResourceLocation smallArrow = new ResourceLocation("tcnodetracker:textures/gui/small_arrows.png");

    public static ArrayList<AspectLoc> aspectList = new ArrayList<AspectLoc>();
    private int display, start, low, high;
    private int dimID = 0, dimIndex;
    private String dimName;

    public GuiMain() {
    }

    public void dimFunction() {
        //dimIndex = TCNodeTracker.dims.indexOf(dimID);
        for (int x = 0; x < TCNodeTracker.dims.size(); x++) {
            if (TCNodeTracker.dims.get(x).dimID == dimID) {
                dimName = TCNodeTracker.dims.get(x).DimName;
                dimIndex = x;
                sortNodes("ALL", dimID);
                return;
            }
        }
    }

    @Override
    public void initGui() {
        display = 425;
        start = (this.width - display) / 2;

        dimID = Minecraft.getMinecraft().theWorld.provider.dimensionId ;

        dimFunction();
        guiButtons();

    }

    protected void guiButtons() {
        int x = 67;
        this.buttonList.clear();
        for (int j = low; j < (high * 2); j += 2) {

            this.buttonList.add(new GuiButton(j, start + 410, x, 20, 10, "Del"));
            this.buttonList.add(new GuiButton(j + 1, start + 380, x, 30, 10, "Mark"));

            x += 14;
        }

        this.buttonList.add(new GuiButton((high * 2), start + 350, 11, 70, 16,"Clear Arrow"));
        this.updateScreen();
    }

    @Override
    protected void actionPerformed(GuiButton button) {

        if (button.id == this.buttonList.size() - 1) {
            TCNodeTracker.doGui = false;
            this.mc.displayGuiScreen(null);
            aspectList.clear();
        } else if (button.id % 2 == 0) {

            int i = button.id / 2;
            for (int k = 0; k < TCNodeTracker.nodelist.size(); k++) {
                for (int j = low; j < high; j++) {
                    if (TCNodeTracker.nodelist.get(k).x == aspectList.get(i).x &&
                            TCNodeTracker.nodelist.get(k).y == aspectList.get(i).y &&
                            TCNodeTracker.nodelist.get(k).z == aspectList.get(i).z) {
                        TCNodeTracker.nodelist.remove(k);
                        JsonUtils.writeJson();
                        aspectList.clear();
                        this.mc.displayGuiScreen(null);
                        return;
                    }
                }
            }

        } else if (button.id % 2 == 1) {

            int i = button.id / 2;
            this.mc.displayGuiScreen(null);
            TCNodeTracker.doGui = true;
            TCNodeTracker.xMarker = aspectList.get(i).x;
            TCNodeTracker.yMarker = aspectList.get(i).y;
            TCNodeTracker.zMarker = aspectList.get(i).z;
            aspectList.clear();
        }

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
        drawRect(start + 20, 210, start + 100, 225, -9408400);

    }

    public void mouseMovedOrUp(int mouseX, int mouseY, int button) {
        int w = (this.width - 204) / 2;

        if (button >= 0) {
            if (mouseX >= w + 2 && mouseX <= w + 32 && mouseY >= 3 && mouseY <= 35) {
                sortNodes(Constants.AIR, dimID);
            } else if (mouseX >= w + 35 && mouseX <= w + 66 && mouseY >= 3 && mouseY <= 35) {
                sortNodes(Constants.WATER, dimID);
            } else if (mouseX >= w + 70 && mouseX <= w + 101 && mouseY >= 3 && mouseY <= 35) {
                sortNodes(Constants.FIRE, dimID);
            } else if (mouseX >= w + 104 && mouseX <= w + 135 && mouseY >= 3 && mouseY <= 35) {
                sortNodes(Constants.ORDER, dimID);
            } else if (mouseX >= w + 139 && mouseX <= w + 170 && mouseY >= 3 && mouseY <= 35) {
                sortNodes(Constants.ENTROPY, dimID);
            } else if (mouseX >= w + 172 && mouseX <= w + 203 && mouseY >= 3 && mouseY <= 35) {
                sortNodes(Constants.EARTH, dimID);
            } else if (mouseX >= (this.width - 50) / 2 && mouseX <= ((this.width - 50) / 2) + 15 &&
                    mouseY >= 210 && mouseY <= 227 && low > 0) {
                low -= 1;
                high -= 1;
            } else if (mouseX >= (this.width + 32) / 2 && mouseX <= ((this.width + 32) / 2) + 17 &&
                    mouseY >= 210 && mouseY <= 227 && high != aspectList.size()) {
                low += 1;
                high += 1;
            } else if (mouseX >= start  && mouseX <= start + 17 && mouseY >= 209 && mouseY <= 226) {
                if (dimIndex == 0){
                    dimIndex = TCNodeTracker.dims.size() - 1;
                } else {
                    dimIndex -= 1;
                }
                dimID = TCNodeTracker.dims.get(dimIndex).dimID;
                dimFunction();
            } else if (mouseX >= start + 102  && mouseX <= start + 119 && mouseY >= 209 && mouseY <= 226) {
                if (dimIndex < TCNodeTracker.dims.size() - 1){
                    dimIndex += 1;
                } else {
                    dimIndex = 0;
                }
                dimID = TCNodeTracker.dims.get(dimIndex).dimID;
                dimFunction();
            }
        }
    }

    private void sortNodes(String aspect, int dimID) {

        aspectList.clear();

        for (NodeList n : TCNodeTracker.nodelist) {
            if (aspect.equals("ALL") && n.dim == dimID) {

                aspectList.add(new AspectLoc(n.x, n.y, n.z, n.dim, (int) Math.round(mc.thePlayer.getDistance(n.x, n.y, n.z)),
                        n.type,
                        n.aspect.containsKey(Constants.AIR) ? n.aspect.get(Constants.AIR) : 0,
                        n.aspect.containsKey(Constants.WATER) ? n.aspect.get(Constants.WATER) : 0,
                        n.aspect.containsKey(Constants.FIRE) ? n.aspect.get(Constants.FIRE) : 0,
                        n.aspect.containsKey(Constants.ORDER) ? n.aspect.get(Constants.ORDER) : 0,
                        n.aspect.containsKey(Constants.ENTROPY) ? n.aspect.get(Constants.ENTROPY) : 0,
                        n.aspect.containsKey(Constants.EARTH) ? n.aspect.get(Constants.EARTH) : 0));

            } else if (n.aspect.containsKey(aspect) && n.dim == dimID) {

                aspectList.add(new AspectLoc(n.x, n.y, n.z, n.dim, (int) Math.round(mc.thePlayer.getDistance(n.x, n.y, n.z)),
                        n.type,
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
        low = 0;
        high = (aspectList.size() > 10) ? 10 : aspectList.size();
        guiButtons();
    }


    public void drawScreen(int x, int y, float f) {

        int l = 70;
        drawDefaultBackground();

        this.fontRendererObj.drawString(dimName, start + 20 +(80 - this.fontRendererObj.getStringWidth(dimName)) / 2, 214, Constants.WHITE);

        String s1 = "Click aspect to get node list";
        this.fontRendererObj.drawString(s1, this.width / 2 - this.fontRendererObj.getStringWidth(s1) / 2, 40, Constants.WHITE);
        s1 = "(N)ormal";
        this.fontRendererObj.drawString(s1, start, 1, Constants.WHITE);
        s1 = "(U)nstable";
        this.fontRendererObj.drawString(s1, start, 9, Constants.WHITE);
        s1 = "(D)ark";
        this.fontRendererObj.drawString(s1, start, 17, Constants.WHITE);
        s1 = "(T)ainted";
        this.fontRendererObj.drawString(s1, start, 25, Constants.WHITE);
        s1 = "(H)ungry";
        this.fontRendererObj.drawString(s1, start, 33, Constants.WHITE);
        s1 = "(P)ure";
        this.fontRendererObj.drawString(s1, start, 41, Constants.WHITE);

        drawRect(start, 50, start + display, 52, -9408400);
        drawRect(start, 64, start + display, 66, -9408400);

        this.fontRendererObj.drawString("Dist", start + 2, 55, Constants.WHITE);
        this.fontRendererObj.drawString("X", start + 50, 55, Constants.WHITE);
        this.fontRendererObj.drawString("Y", start + 80, 55, Constants.WHITE);
        this.fontRendererObj.drawString("Z", start + 110, 55, Constants.WHITE);
        this.fontRendererObj.drawString("Type", start + 140, 55, Constants.WHITE);
        s1 = "Aer  Aqua  Ignis  Ordo  Perd  Terra";
        this.fontRendererObj.drawString(s1, start + 188, 55, Constants.WHITE);

        for (AspectLoc a : aspectList.subList(low, high)) {
            String s2 = Integer.toString(a.distance);
            this.fontRendererObj.drawString(s2, start + (11 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, Constants.WHITE);

            s2 = Integer.toString(a.x);
            this.fontRendererObj.drawString(s2, start + (52 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, Constants.WHITE);

            s2 = Integer.toString(a.y);
            this.fontRendererObj.drawString(s2, start + (83 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, Constants.WHITE);

            s2 = Integer.toString(a.z);
            this.fontRendererObj.drawString(s2, start + (112 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, Constants.WHITE);

            if (a.type == null) s2 = "";
            else {
                String[] type;
                    type = a.type.split("-");
                s2 = "";
                for (String aType : type)
                        s2 += (s2.equals("")) ? aType.charAt(0) : "/" + aType.trim().charAt(0);

            }
            this.fontRendererObj.drawString(s2, start + (152 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, Constants.WHITE);

            s2 = a.hasAer > 0 ? Integer.toString(a.hasAer) : "";
            this.fontRendererObj.drawString(s2, start + (195 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, Constants.WHITE);

            s2 = a.hasAqua > 0 ? Integer.toString(a.hasAqua) : "";
            this.fontRendererObj.drawString(s2, start + (226 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, Constants.WHITE);

            s2 = a.hasIgnis > 0 ? Integer.toString(a.hasIgnis) : "";
            this.fontRendererObj.drawString(s2, start + (258 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, Constants.WHITE);

            s2 = a.hasOrdo > 0 ? Integer.toString(a.hasOrdo) : "";
            this.fontRendererObj.drawString(s2, start + (290 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, Constants.WHITE);

            s2 = a.hasPerdito > 0 ? Integer.toString(a.hasPerdito) : "";
            this.fontRendererObj.drawString(s2, start + (322 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, Constants.WHITE);

            s2 = a.hasTerra > 0 ? Integer.toString(a.hasTerra) : "";
            this.fontRendererObj.drawString(s2, start + (358 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, Constants.WHITE);

            drawRect(start, l + 9, start + display, l + 10, -9408400);

            l += 14;
        }

        this.mc.getTextureManager().bindTexture(smallArrow);
        if (low > 0)
            this.drawTexturedModalRect((this.width - 50) / 2, 210, 1, 1, 15, 17);
        if (high != aspectList.size())
        this.drawTexturedModalRect((this.width + 32) / 2, 211, 17, 1, 32, 17);
        this.drawTexturedModalRect(start, 209, 91, 41, 17, 17);
        this.drawTexturedModalRect(start + 102, 209, 91, 25, 17, 17);

        super.drawScreen(x, y, f);
    }
}
