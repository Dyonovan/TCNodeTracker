package com.dyonovan.tcnodetracker.gui;

import com.dyonovan.tcnodetracker.TCNodeTracker;
import com.dyonovan.tcnodetracker.lib.AspectLoc;
import com.dyonovan.tcnodetracker.lib.NodeList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class GuiMain extends GuiScreen {

    private static final ResourceLocation background = new ResourceLocation("tcnodetracker:textures/gui/nodes.png");
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

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(background);
        this.drawTexturedModalRect((this.width - 204) / 2, 1, 0, 0, 204, 35);

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

        boolean hasAer = false, hasOrdo = false, hasTerra = false, hasPerdito = false, hasIgnis = false, hasAqua = false;
        aspectList.clear();

        for (NodeList n : TCNodeTracker.nodelist) {
            if (n.aspect.containsKey(aspect)) {
                Iterator it = n.aspect.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pairs = (Map.Entry)it.next();
                    switch (pairs.getKey().toString()) {
                        case "aer":
                            hasAer = true;
                            break;
                        case "terra":
                            hasTerra = true;
                            break;
                        case "ignis":
                            hasIgnis = true;
                            break;
                        case "aqua":
                            hasAqua = true;
                            break;
                        case "ordo":
                            hasOrdo = true;
                            break;
                        case "perditio":
                            hasPerdito = true;
                            break;
                    }
                }

                aspectList.add(new AspectLoc(n.x, n.y, n.z, hasAer, hasAqua, hasIgnis, hasOrdo, hasPerdito, hasTerra));
            }

        }
    }


    public void drawScreen(int x, int y, float f) {

        int k = 20;
        int l = 40;


        drawDefaultBackground();

        for (AspectLoc a : this.aspectList) {
            this.fontRendererObj.drawString(Integer.toString(a.x), k, l, WHITE );
            this.fontRendererObj.drawString(Integer.toString(a.y), k + 40, l, WHITE );
            this.fontRendererObj.drawString(Integer.toString(a.z), k + 80, l, WHITE );


            l += 8;
        }

        super.drawScreen(x, y, f);

    }



}
