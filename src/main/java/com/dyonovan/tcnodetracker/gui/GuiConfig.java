package com.dyonovan.tcnodetracker.gui;

import com.dyonovan.tcnodetracker.bindings.KeyBindings;
import com.dyonovan.tcnodetracker.handlers.ConfigHandler;
import com.dyonovan.tcnodetracker.lib.Constants;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Property;
import org.lwjgl.opengl.GL11;

public class GuiConfig extends GuiScreen {

    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    public static final int RESET = 4;
    public static final int ARROW_TYPE = 5;
    public static final int ARROW_SIZE_SMALL = 6;
    public static final int ARROW_SIZE_LARGE = 7;

    private static final ResourceLocation arrow = new ResourceLocation("tcnodetracker:textures/gui/arrow.png");
    private static final ResourceLocation altArrow = new ResourceLocation("tcnodetracker:textures/gui/arrow2.png");

    public GuiConfig() { }

/*    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
    }*/

    @SuppressWarnings("unchecked")
    private void drawButtons() {

        GuiButton arrowLarge = new GuiButton(ARROW_SIZE_LARGE, ((width / 4) * 3) + 70, height - 50, 15, 20, ">>");
        GuiButton arrowSmall = new GuiButton(ARROW_SIZE_SMALL, ((width / 4) * 3) - 10, height - 50, 15, 20, "<<");

        if (ConfigHandler.arrowSize >= 1)
            arrowLarge.enabled = false;
        else if (ConfigHandler.arrowSize <= 0.05)
            arrowSmall.enabled = false;

        buttonList.clear();

        buttonList.add(new GuiButton(UP, (width / 2) - 17, height - 75, 35, 20, "UP"));
        buttonList.add(new GuiButton(DOWN, (width / 2) - 17, height - 25, 35, 20, "DOWN"));
        buttonList.add(new GuiButton(LEFT, (width / 2) - 61, height - 50, 35, 20, "LEFT"));
        buttonList.add(new GuiButton(RIGHT, (width / 2) + 26, height - 50, 35, 20, "RIGHT"));
        buttonList.add(new GuiButton(RESET, (width / 2) - 17, height - 50, 35, 20, "RESET"));
        buttonList.add(new GuiButton(ARROW_TYPE, (width / 4) - 70, height - 50, 70, 20, "Arrow Type"));
        buttonList.add(arrowSmall);
        buttonList.add(arrowLarge);

        updateScreen();
    }

    @Override
    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case UP:
                ConfigHandler.arrowY -= 1;
                break;
            case DOWN:
                ConfigHandler.arrowY += 1;
                break;
            case LEFT:
                ConfigHandler.arrowX -= 1;
                break;
            case RIGHT:
                ConfigHandler.arrowX += 1;
                break;
            case RESET:
                ConfigHandler.arrowX = 0;
                ConfigHandler.arrowY = 0;
                break;
            case ARROW_TYPE:
                if (ConfigHandler.altArrow)
                    ConfigHandler.altArrow = false;
                else if (!ConfigHandler.altArrow)
                    ConfigHandler.altArrow = true;
                break;
            case ARROW_SIZE_SMALL:
                ConfigHandler.arrowSize -= 0.05;
                break;
            case ARROW_SIZE_LARGE:
                ConfigHandler.arrowSize += 0.05;
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float f) {

        drawDefaultBackground();

        drawButtons();

        FontRenderer fr = this.mc.fontRenderer;

        GL11.glPushMatrix();
        GL11.glScalef((float) ConfigHandler.arrowSize, (float) ConfigHandler.arrowSize, 1F);
        GL11.glTranslatef((float) ConfigHandler.arrowSize * 1, (float) ConfigHandler.arrowSize * 1, 1F);
        fr.drawString("# Blocks", (width - fr.getStringWidth("# Blocks")) / 2 + ConfigHandler.arrowX,
                60 + ConfigHandler.arrowY, Constants.WHITE);
        fr.drawString("Below", (width - fr.getStringWidth("Below")) / 2 + ConfigHandler.arrowX,
                70 + ConfigHandler.arrowY, Constants.WHITE);
        GL11.glPopMatrix();

        drawString(fr, "Size: " + Math.round(ConfigHandler.arrowSize * 100) + "%", ((width / 4) * 3) + 12,
                height - 43, Constants.WHITE);

        super.drawScreen(mouseX, mouseY, f);
    }

    @Override
    public void drawDefaultBackground() {
        super.drawDefaultBackground();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (!ConfigHandler.altArrow)
            this.mc.getTextureManager().bindTexture(arrow);
        else
            this.mc.getTextureManager().bindTexture(altArrow);

        GL11.glPushMatrix();
        GL11.glTranslated(((width / 2) - 25) + ConfigHandler.arrowX, 5 + ConfigHandler.arrowY, 0);
        //GL11.glTranslated(-25, -25, 0);
        GL11.glScalef((float)ConfigHandler.arrowSize, (float)ConfigHandler.arrowSize, 1F);
        Tessellator tl = Tessellator.instance;
        tl.startDrawingQuads();
        tl.addVertexWithUV(0, 0, 0, 0, 0);
        tl.addVertexWithUV(0, 50, 0, 0, 1);
        tl.addVertexWithUV(50, 50, 0, 1, 1);
        tl.addVertexWithUV(50, 0, 0, 1, 0);
        tl.draw();

        GL11.glPopMatrix();
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
    public void onGuiClosed() {

        Property arrowX = ConfigHandler.config.get("Arrow Location", "XCoord", 0);
        Property arrowY = ConfigHandler.config.get("Arrow Location", "YCoord", 0);
        Property arrowType = ConfigHandler.config.get("Arrow Type", "Use Alt Arrow Texture", false);
        Property arrowSize = ConfigHandler.config.get("Arrow Size", "Arrow Size 0-1", 1);

        arrowX.set(ConfigHandler.arrowX);
        arrowY.set(ConfigHandler.arrowY);
        arrowType.set(ConfigHandler.altArrow);
        arrowSize.set(ConfigHandler.arrowSize);

        ConfigHandler.config.save();

        super.onGuiClosed();
    }
}
