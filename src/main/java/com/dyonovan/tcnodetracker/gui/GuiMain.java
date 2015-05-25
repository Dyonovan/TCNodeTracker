package com.dyonovan.tcnodetracker.gui;

import com.dyonovan.tcnodetracker.TCNodeTracker;
import com.dyonovan.tcnodetracker.bindings.KeyBindings;
import com.dyonovan.tcnodetracker.lib.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;
import org.lwjgl.opengl.GL11;

import java.text.SimpleDateFormat;
import java.util.*;

@SideOnly(Side.CLIENT)
public class GuiMain extends GuiScreen {

    private static final ResourceLocation nodes = new ResourceLocation("tcnodetracker:textures/gui/nodes.png");
    private static final ResourceLocation smallArrow = new ResourceLocation("tcnodetracker:textures/gui/small_arrows.png");

    public static ArrayList<AspectLoc> aspectList = new ArrayList<>();
    private int display, start, low, high;
    private int dimID = 0, dimIndex;
    private String dimName, currentAspect, lastSort;

    public GuiMain() {
    }

    public void dimFunction() {
        for (int x = 0; x < TCNodeTracker.dims.size(); x++) {
            if (TCNodeTracker.dims.get(x).dimID == dimID) {
                dimName = TCNodeTracker.dims.get(x).DimName;
                dimIndex = x;
                currentAspect = "ALL";
                sortNodes(Constants.DISTANCE);
                return;
            }
        }
    }

    @Override
    public void initGui() {
        display = 425;
        start = (this.width - display) / 2;
        lastSort = Constants.DISTANCE;

        TCNodeTracker.dims.clear();
        for (int i : DimensionManager.getStaticDimensionIDs()) {
            //TCNodeTracker.dims.add(new DimList(i, DimensionManager.createProviderFor(i).getDimensionName()));
            if (DimensionManager.getWorld(i) != null) {
                try {
                    WorldProvider provider = DimensionManager.getProvider(i);
                    TCNodeTracker.dims.add(new DimList(i, provider.getDimensionName()));
                } catch (Throwable t) {
                    TCNodeTracker.dims.add(new DimList(i, Integer.toString(i)));
                }
            } else {
                try {
                    TCNodeTracker.dims.add(new DimList(i, DimensionManager.createProviderFor(i).getDimensionName()));
                } catch (Throwable t) {
                    TCNodeTracker.dims.add(new DimList(i, Integer.toString(i)));
                }
            }
        }
        Collections.sort(TCNodeTracker.dims, new Comparator<DimList>() {
            @Override
            public int compare(DimList o1, DimList o2) {
                return o1.dimID - o2.dimID;
            }
        });

        dimID = Minecraft.getMinecraft().theWorld.provider.dimensionId ;

        dimFunction();
        guiButtons();

    }

    @SuppressWarnings("unchecked")
    protected void guiButtons() {
        int x = 67;
        this.buttonList.clear();
        for (int j = low; j < (high * 2); j += 2) {

            this.buttonList.add(new GuiButton(j, start + 400, x, 20, 10, "Del"));
            this.buttonList.add(new GuiButton(j + 1, start + 370, x, 30, 10, "Mark"));

            x += 14;
        }

        this.buttonList.add(new GuiButton(buttonList.size(), start + 350, 5, 70, 20, "Clear Arrow"));
        this.buttonList.add(new GuiButton(buttonList.size(), start + 350, 27, 70, 20, "Config"));
        this.updateScreen();
    }

    @Override
    protected void actionPerformed(GuiButton button) {

        if (button.id == this.buttonList.size() - 2) {
            TCNodeTracker.doGui = false;
            TCNodeTracker.yMarker = -1;
            this.mc.displayGuiScreen(null);
            aspectList.clear();
        } else if (button.id == this.buttonList.size() - 1) {
            this.mc.displayGuiScreen(null);
            Minecraft.getMinecraft().displayGuiScreen(new GuiConfig());
        } else if (button.id % 2 == 0) {

            int i = button.id / 2;
            for (int k = 0; k < TCNodeTracker.nodelist.size(); k++) {
                for (int j = low; j < high; j++) {
                    if (TCNodeTracker.nodelist.get(k).x == aspectList.get(low + i).x &&
                            TCNodeTracker.nodelist.get(k).y == aspectList.get(low + i).y &&
                            TCNodeTracker.nodelist.get(k).z == aspectList.get(low + i).z) {
                        if (TCNodeTracker.doGui && TCNodeTracker.xMarker == aspectList.get(low + i).x &&
                                TCNodeTracker.yMarker == aspectList.get(low + i).y &&
                                TCNodeTracker.zMarker == aspectList.get(low + i).z) {
                            TCNodeTracker.doGui = false;
                            TCNodeTracker.yMarker = -1;
                        }
                        TCNodeTracker.nodelist.remove(k);
                        JsonUtils.writeJson();
                        sortNodes(lastSort);
                        return;
                    }
                }
            }

        } else if (button.id % 2 == 1) {

            int i = button.id / 2;
            this.mc.displayGuiScreen(null);
            TCNodeTracker.doGui = true;
            TCNodeTracker.xMarker = aspectList.get(low + i).x;
            TCNodeTracker.yMarker = aspectList.get(low + i).y;
            TCNodeTracker.zMarker = aspectList.get(low + i).z;
            aspectList.clear();
        }

    }

    @Override
    protected void keyTyped(char key, int i) {
        if (i == KeyBindings.aspectMenu.getKeyCode() || i == 1) {
            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

    }

    @Override
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
                currentAspect = Constants.AIR;
                sortNodes(Constants.DISTANCE);
            } else if (mouseX >= w + 35 && mouseX <= w + 66 && mouseY >= 3 && mouseY <= 35) {
                currentAspect = Constants.WATER;
                sortNodes(Constants.DISTANCE);
            } else if (mouseX >= w + 70 && mouseX <= w + 101 && mouseY >= 3 && mouseY <= 35) {
                currentAspect = Constants.FIRE;
                sortNodes(Constants.DISTANCE);
            } else if (mouseX >= w + 104 && mouseX <= w + 135 && mouseY >= 3 && mouseY <= 35) {
                currentAspect = Constants.ORDER;
                sortNodes(Constants.DISTANCE);
            } else if (mouseX >= w + 139 && mouseX <= w + 170 && mouseY >= 3 && mouseY <= 35) {
                currentAspect = Constants.ENTROPY;
                sortNodes(Constants.DISTANCE);
            } else if (mouseX >= w + 172 && mouseX <= w + 203 && mouseY >= 3 && mouseY <= 35) {
                currentAspect = Constants.EARTH;
                sortNodes(Constants.DISTANCE);
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
            } else if (isInBounds(mouseX, mouseY, start + 2, 55, start + 20, 65)) {
                sortNodes(Constants.DISTANCE);
            } else if (isInBounds(mouseX, mouseY, start + 188, 55, start + 208, 65)) {
                sortNodes(Constants.AIR);
            } else if (isInBounds(mouseX, mouseY, start + 214, 55, start + 238, 65)) {
                sortNodes(Constants.WATER);
            } else if (isInBounds(mouseX, mouseY, start + 246, 55, start + 270, 65)) {
                sortNodes(Constants.FIRE);
            } else if (isInBounds(mouseX, mouseY, start + 278, 55, start + 302, 65)) {
                sortNodes(Constants.ORDER);
            } else if (isInBounds(mouseX, mouseY, start + 310, 55, start + 334, 65)) {
                sortNodes(Constants.ENTROPY);
            } else if (isInBounds(mouseX, mouseY, start + 342, 55, start + 372, 65)) {
                sortNodes(Constants.EARTH);
            }
        }
    }

    private void sortNodes(final String sortBy) {
        aspectList.clear();
        lastSort = sortBy;
        for (NodeList n : TCNodeTracker.nodelist) {
            if (currentAspect.equals("ALL") && n.dim == dimID) {
                getNodes(n);
            } else if (n.aspect.containsKey(currentAspect) && n.dim == dimID) {
                getNodes(n);
            }
        }
        Comparator<AspectLoc> comparator;
        switch (sortBy) {
            case Constants.DISTANCE:
                comparator = AspectLoc.getDistComparator();
                break;
            case Constants.AIR:
                comparator = AspectLoc.getAerComparator();
                break;
            case Constants.WATER:
                comparator = AspectLoc.getAquaComparator();
                break;
            case Constants.FIRE:
                comparator = AspectLoc.getIgnisComparator();
                break;
            case Constants.ORDER:
                comparator = AspectLoc.getOrdoComparator();
                break;
            case Constants.ENTROPY:
                comparator = AspectLoc.getPerdComparator();
                break;
            case Constants.EARTH:
                comparator = AspectLoc.getTerraComparator();
                break;
            default:
                comparator = AspectLoc.getDistComparator();
        }
        Collections.sort(aspectList, comparator);
        if (!sortBy.equals(Constants.DISTANCE))
            Collections.reverse(aspectList);
        low = 0;
        high = (aspectList.size() > 10) ? 10 : aspectList.size();
        guiButtons();
    }

    private void getNodes(NodeList nodes) {
        int air = 0;
        int water = 0;
        int fire = 0;
        int order = 0;
        int entropy = 0;
        int earth = 0;
        HashMap<String, Integer> compound = new HashMap<>();

        for (Map.Entry<String, Integer> node : nodes.aspect.entrySet()) {
            String aspect = node.getKey();
            int amount = node.getValue();

            if (aspect.equalsIgnoreCase(Constants.AIR))
                air += amount;
            else if (aspect.equalsIgnoreCase(Constants.WATER))
                water += amount;
            else if (aspect.equalsIgnoreCase(Constants.FIRE))
                fire += amount;
            else if (aspect.equalsIgnoreCase(Constants.ORDER))
                order += amount;
            else if (aspect.equalsIgnoreCase(Constants.ENTROPY))
                entropy += amount;
            else if (aspect.equalsIgnoreCase(Constants.EARTH))
                earth += amount;
            else compound.put(aspect, amount);
        }

        aspectList.add(new AspectLoc(nodes.x, nodes.y, nodes.z, nodes.dim, nodes.date,
                (int) Math.round(mc.thePlayer.getDistance(nodes.x, mc.thePlayer.posY, nodes.z)),
                nodes.type, nodes.mod, air, water, fire, order, entropy, earth, compound));
    }

    public void drawScreen(int x, int y, float f) {

        int l = 70;
        drawDefaultBackground();

        this.fontRendererObj.drawString(dimName, start + 20 +(80 - this.fontRendererObj.getStringWidth(dimName)) / 2, 214, Constants.WHITE);

        String s1 = "Click aspect to get node list";
        this.fontRendererObj.drawString(s1, this.width / 2 - this.fontRendererObj.getStringWidth(s1) / 2, 40, Constants.WHITE);

        drawRect(start, 50, start + display, 52, -9408400);
        drawRect(start, 64, start + display, 66, -9408400);

        this.fontRendererObj.drawString("Dist", start + 2, 55, Constants.WHITE);
        this.fontRendererObj.drawString("X", start + 50, 55, Constants.WHITE);
        this.fontRendererObj.drawString("Y", start + 80, 55, Constants.WHITE);
        this.fontRendererObj.drawString("Z", start + 110, 55, Constants.WHITE);
        this.fontRendererObj.drawString("Type", start + 130, 55, Constants.WHITE);
        s1 = "Aer  Aqua  Ignis  Ordo  Perd  Terra";
        this.fontRendererObj.drawString(s1, start + 178, 55, Constants.WHITE);

        for (AspectLoc a : aspectList.subList(low, high)) {
            int color;
            if (TCNodeTracker.xMarker == a.x && TCNodeTracker.yMarker == a.y && TCNodeTracker.zMarker == a.z) {
                color = Constants.GREEN;
            } else color = Constants.WHITE;
            String s2 = Integer.toString(a.distance);
            this.fontRendererObj.drawString(s2, start + (11 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, color);

            s2 = Integer.toString(a.x);
            this.fontRendererObj.drawString(s2, start + (52 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, color);

            s2 = Integer.toString(a.y);
            this.fontRendererObj.drawString(s2, start + (83 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, color);

            s2 = Integer.toString(a.z);
            this.fontRendererObj.drawString(s2, start + (112 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, color);

            /*if (a.type == null) s2 = "";
            else {
                String[] type;
                    type = a.type.split("-");
                s2 = "";
                for (String aType : type)
                        s2 += (s2.equals("")) ? aType.charAt(0) : "/" + aType.trim().charAt(0);

            }*/
            s2 = a.type.substring(0, 1);
            this.fontRendererObj.drawString(s2, start + (142 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, color);

            s2 = a.hasAer > 0 ? Integer.toString(a.hasAer) : "";
            this.fontRendererObj.drawString(s2, start + (185 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, color);

            s2 = a.hasAqua > 0 ? Integer.toString(a.hasAqua) : "";
            this.fontRendererObj.drawString(s2, start + (216 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, color);

            s2 = a.hasIgnis > 0 ? Integer.toString(a.hasIgnis) : "";
            this.fontRendererObj.drawString(s2, start + (248 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, color);

            s2 = a.hasOrdo > 0 ? Integer.toString(a.hasOrdo) : "";
            this.fontRendererObj.drawString(s2, start + (280 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, color);

            s2 = a.hasPerdito > 0 ? Integer.toString(a.hasPerdito) : "";
            this.fontRendererObj.drawString(s2, start + (312 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, color);

            s2 = a.hasTerra > 0 ? Integer.toString(a.hasTerra) : "";
            this.fontRendererObj.drawString(s2, start + (348 - (this.fontRendererObj.getStringWidth(s2) / 2)), l, color);



            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_LIGHTING);
            //GL11.glColor3f(1, 1, 1);

            drawRect(start, l + 9, start + display, l + 10, -9408400);

            GL11.glPopMatrix();

            l += 14;
        }

        l = 70;
        for (AspectLoc a : aspectList.subList(low, high)) {

            if (isInBounds(x, y, start + 130, l - 5, start + 156, l + 8)) {
                List<String> toolTip = new ArrayList<>();
                toolTip.add("\u00a7" + Integer.toHexString(2) + "Compound Aspects");
                if (a.compound.size() >0) {
                    for (Map.Entry<String, Integer> node : a.compound.entrySet())
                        toolTip.add(node.getKey().toUpperCase() + ": " + node.getValue());
                } else {
                    toolTip.add("None");
                }
                toolTip.add("\u00a7" + Integer.toHexString(2) + "Node Type");
                String type;
                if  (a.mod == null || a.mod.equals("BLANK"))
                    type = a.type;
                else
                    type = a.type + " - State: " + a.mod;
                toolTip.add(type);
                drawHoveringText(toolTip, x, y, fontRendererObj);
            } else if (isInBounds(x, y, start + 2, l - 5, start + 40, l + 8)) {
                List<String> toolTip = new ArrayList<>();
                toolTip.add("\u00a7" + Integer.toHexString(2) + "Last Scanned");
                if (a.date != null)
                    toolTip.add(new SimpleDateFormat("HH:mm:ss yyyy.MM.dd").format(a.date));
                else toolTip.add("Unknown");
                drawHoveringText(toolTip, x, y, fontRendererObj);
            }

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

    public static boolean isInBounds(int x, int y, int a, int b, int c, int d)
    {
        return (x >= a && x <= c && y >= b && y <=d);
    }
}
