package com.dyonovan.tcnodetracker.gui;

import com.dyonovan.tcnodetracker.TCNodeTracker;
import com.dyonovan.tcnodetracker.lib.AspectLoc;
import com.dyonovan.tcnodetracker.lib.Constants;
import com.dyonovan.tcnodetracker.lib.JsonUtils;
import com.dyonovan.tcnodetracker.lib.NodeList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
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
    private int display, start;
    private String sort;

    public GuiMain() {
    }

    @Override
    public void initGui() {
        display = 425;
        start = (this.width - display) / 2;
        int x = 67;
        this.sortNodes("ALL");

        for (int j = 0; j < (aspectList.size() * 2); j += 2) {

            this.buttonList.add(new GuiButton(j, start + 410, x, 20, 10, "Del"));
            this.buttonList.add(new GuiButton(j + 1, start + 380, x, 30, 10, "Mark"));

            x += 14;
        }

        this.buttonList.add(new GuiButton((aspectList.size() * 2), start + 350, 11, 70, 16,"Clear Arrow"));
    }

    @Override
    protected void actionPerformed(GuiButton button) {

        if (button.id == this.buttonList.size() - 1) {
            TCNodeTracker.doGui = false;
            this.mc.displayGuiScreen(null);
            aspectList.clear();
            return;
        } else if (button.id % 2 == 0) {

            int i = button.id / 2;
            for (int j = 0; j < TCNodeTracker.nodelist.size(); j++) {
                if (TCNodeTracker.nodelist.get(j).x == aspectList.get(i).x &&
                        TCNodeTracker.nodelist.get(j).y == aspectList.get(i).y &&
                        TCNodeTracker.nodelist.get(j).z == aspectList.get(i).z) {
                    TCNodeTracker.nodelist.remove(j);
                    JsonUtils.writeJson();
                    aspectList.clear();
                    this.mc.displayGuiScreen(null);
                    return;
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
            } /*else if (mouseX >= 358 && mouseX <= 398 && mouseY >= 54 && mouseY <= 63) {
                TCNodeTracker.doGui = false;
                this.mc.displayGuiScreen(null);
                aspectList.clear();
                return;
            }*/

            /*int l = 68;

            for (int i = 0; i < aspectList.size(); i++) {

                if (mouseX >= 320 && mouseX <= 340 && mouseY >= l + 2 && mouseY <= l + 9) {

                    for (int j = 0; j < TCNodeTracker.nodelist.size(); j++) {
                        if (TCNodeTracker.nodelist.get(j).x == aspectList.get(i).x &&
                                TCNodeTracker.nodelist.get(j).y == aspectList.get(i).y &&
                                TCNodeTracker.nodelist.get(j).z == aspectList.get(i).z) {
                            TCNodeTracker.nodelist.remove(j);
                            JsonUtils.writeJson();
                            aspectList.clear();
                            this.mc.displayGuiScreen(null);
                            return;
                        }
                    }
                } else if (mouseX >= 360 && mouseX <= 380 && mouseY >= l + 2 && mouseY <= l + 9) {

                    this.mc.displayGuiScreen(null);
                    TCNodeTracker.doGui = true;
                    TCNodeTracker.xMarker = aspectList.get(i).x;
                    TCNodeTracker.yMarker = aspectList.get(i).y;
                    TCNodeTracker.zMarker = aspectList.get(i).z;
                    aspectList.clear();
                }

                l += 11;
            }*/
        }


    }

    private void sortNodes(String aspect) {

        this.sort = aspect;
        aspectList.clear();

        for (NodeList n : TCNodeTracker.nodelist) {
            if (aspect.equals("ALL")) {

                aspectList.add(new AspectLoc(n.x, n.y, n.z, (int) Math.round(mc.thePlayer.getDistance(n.x, n.y, n.z)),
                        n.type,
                        n.aspect.containsKey(Constants.AIR) ? n.aspect.get(Constants.AIR) : 0,
                        n.aspect.containsKey(Constants.WATER) ? n.aspect.get(Constants.WATER) : 0,
                        n.aspect.containsKey(Constants.FIRE) ? n.aspect.get(Constants.FIRE) : 0,
                        n.aspect.containsKey(Constants.ORDER) ? n.aspect.get(Constants.ORDER) : 0,
                        n.aspect.containsKey(Constants.ENTROPY) ? n.aspect.get(Constants.ENTROPY) : 0,
                        n.aspect.containsKey(Constants.EARTH) ? n.aspect.get(Constants.EARTH) : 0));

            } else if (n.aspect.containsKey(aspect)) {

                aspectList.add(new AspectLoc(n.x, n.y, n.z, (int) Math.round(mc.thePlayer.getDistance(n.x, n.y, n.z)),
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
    }


    public void drawScreen(int x, int y, float f) {

        int l = 70;
        drawDefaultBackground();

        String s1 = "Click aspect to get node list";

        this.fontRendererObj.drawString(s1, this.width / 2 - this.fontRendererObj.getStringWidth(s1) / 2, 40, Constants.WHITE);
        drawRect(start, 50, start + display, 52, -9408400);
        drawRect(start, 64, start + display, 66, -9408400);

        this.fontRendererObj.drawString("Dist", start + 2, 55, Constants.WHITE);
        this.fontRendererObj.drawString("X", start + 50, 55, Constants.WHITE);
        this.fontRendererObj.drawString("Y", start + 80, 55, Constants.WHITE);
        this.fontRendererObj.drawString("Z", start + 110, 55, Constants.WHITE);
        this.fontRendererObj.drawString("Type", start + 140, 55, Constants.WHITE);
        s1 = "Aer  Aqua  Ignis  Ordo  Perd  Terra";
        this.fontRendererObj.drawString(s1, start + 188, 55, Constants.WHITE);
        /*FontHelper.drawString("Distance", start + 5, 54, TCNodeTracker.stringFont, 1f, 1f);
        FontHelper.drawString("X", start + 50, 54, TCNodeTracker.stringFont, 1f, 1f);
        FontHelper.drawString("Y", start + 80, 54, TCNodeTracker.stringFont, 1f, 1f);
        FontHelper.drawString("Z", start + 110, 54, TCNodeTracker.stringFont, 1f, 1f);
        FontHelper.drawString("Type", start + 140, 54, TCNodeTracker.stringFont, 1f, 1f);
        s1 = "Aer      Aqua      Ignis      Ordo      Perditio      Terra";
        FontHelper.drawString(s1, start + 188, 54, TCNodeTracker.stringFont, 1f, 1f);
        FontHelper.drawString("CLEAR", start + 358, 54, TCNodeTracker.stringFont, 1f, 1f);*/

        //TODO Add Dim to List and Select Dim
        for (AspectLoc a : aspectList) {
            String s2 = Integer.toString(a.distance);
            this.fontRendererObj.drawString(s2, start + (11 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, Constants.WHITE);

            s2 = Integer.toString(a.x);
            //FontHelper.drawString(s2, start + (50 - (s2.length() / 2)), l, TCNodeTracker.stringFont, 1f, 1f);
            this.fontRendererObj.drawString(s2, start + (52 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, Constants.WHITE);

            s2 = Integer.toString(a.y);
            //FontHelper.drawString(s2, start + (80 - (s2.length() / 2)), l, TCNodeTracker.stringFont, 1f, 1f);
            this.fontRendererObj.drawString(s2, start + (83 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, Constants.WHITE);

            s2 = Integer.toString(a.z);
            //FontHelper.drawString(s2, start + (110 - (s2.length() / 2)), l, TCNodeTracker.stringFont, 1f, 1f);
            this.fontRendererObj.drawString(s2, start + (112 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, Constants.WHITE);

            if (a.type == null) s2 = "";
            else {
                String[] type = new String[2];
                if (a.type.contains("-")) {
                    type = a.type.split("-");
                } else {
                    type[0] = a.type;
                }
                s2 = "";
                for (int j = 0; j < type.length; j++) {
                    if (type[j] != null)
                    s2 += (s2.equals("")) ? type[j].charAt(0) : "/" + type[j].trim().charAt(0);
                }
            }
            //s2 = a.type == null ? "" : a.type;
            //FontHelper.drawString(s2, start + (135 - (s2.length() / 2)), l, TCNodeTracker.stringFont, 1f, 1f);
            this.fontRendererObj.drawString(s2, start + (152 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, Constants.WHITE);

            s2 = a.hasAer > 0 ? Integer.toString(a.hasAer) : "";
            //FontHelper.drawString(s2, start + (190 - (s2.length() / 2)), l, TCNodeTracker.stringFont, 1f, 1f);
            this.fontRendererObj.drawString(s2, start + (195 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, Constants.WHITE);

            s2 = a.hasAqua > 0 ? Integer.toString(a.hasAqua) : "";
            //FontHelper.drawString(s2, start + (206 - (s2.length() / 2)), l, TCNodeTracker.stringFont, 1f, 1f);
            this.fontRendererObj.drawString(s2, start + (226 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, Constants.WHITE);

            s2 = a.hasIgnis > 0 ? Integer.toString(a.hasIgnis) : "";
            //FontHelper.drawString(s2, start + (223 - (s2.length() / 2)), l, TCNodeTracker.stringFont, 1f, 1f);
            this.fontRendererObj.drawString(s2, start + (258 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, Constants.WHITE);

            s2 = a.hasOrdo > 0 ? Integer.toString(a.hasOrdo) : "";
            //FontHelper.drawString(s2, start + (242 - (s2.length() / 2)), l, TCNodeTracker.stringFont, 1f, 1f);
            this.fontRendererObj.drawString(s2, start + (290 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, Constants.WHITE);

            s2 = a.hasPerdito > 0 ? Integer.toString(a.hasPerdito) : "";
            //FontHelper.drawString(s2, start + (260 - (s2.length() / 2)), l, TCNodeTracker.stringFont, 1f, 1f);
            this.fontRendererObj.drawString(s2, start + (322 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, Constants.WHITE);

            s2 = a.hasTerra > 0 ? Integer.toString(a.hasTerra) : "";
            //FontHelper.drawString(s2, start + (282 - (s2.length() / 2)), l, TCNodeTracker.stringFont, 1f, 1f);
            this.fontRendererObj.drawString(s2, start + (358 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, Constants.WHITE);

            /*s2 = "DELETE";
            FontHelper.drawString(s2, start + 320, l, TCNodeTracker.stringFont, 1f, 1f, new float[]{0.941F, 0.188F, 0.102F, 1F});

            s2 = "MARK";
            FontHelper.drawString(s2, start + 360, l, TCNodeTracker.stringFont, 1f, 1f, new float[]{0.063F, 0.769F, 0.322F, 1F});
*/
            drawRect(start, l + 9, start + display, l + 10, -9408400);

            l += 14;
        }

        super.drawScreen(x, y, f);

    }

}
