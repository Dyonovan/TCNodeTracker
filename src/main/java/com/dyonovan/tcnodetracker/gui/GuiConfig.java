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

    boolean moving;
    int startX;
    int startY;
    int startArrowX;
    int startArrowY;

    private static final ResourceLocation arrow = new ResourceLocation("tcnodetracker:textures/gui/arrow.png");
    private static final ResourceLocation altArrow = new ResourceLocation("tcnodetracker:textures/gui/arrow2.png");

    public GuiConfig() { }

    @Override
    public void initGui() {
        moving = false;
        startX = 0;
        startY = 0;
        startArrowX = 0;
        startArrowY = 0;
    }

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

        final int arrowWidth = 64;
        final int arrowHeight= 64;

        drawDefaultBackground();
        drawButtons();

        if (!ConfigHandler.altArrow)
            this.mc.getTextureManager().bindTexture(arrow);
        else
            this.mc.getTextureManager().bindTexture(altArrow);

        GL11.glPushMatrix();
        GL11.glTranslated(width / 2 + ConfigHandler.arrowX, arrowHeight/2 + 5 + ConfigHandler.arrowY, 0);
        GL11.glScaled(ConfigHandler.arrowSize, ConfigHandler.arrowSize, 1F);
        GL11.glTranslatef(-arrowWidth / 2, -arrowHeight / 2, 0);
        Tessellator tl = Tessellator.instance;
        tl.startDrawingQuads();
        tl.addVertexWithUV(0, 0, 0, 0, 0);
        tl.addVertexWithUV(0, arrowHeight, 0, 0, 1);
        tl.addVertexWithUV(arrowWidth, arrowHeight, 0, 1, 1);
        tl.addVertexWithUV(arrowWidth, 0, 0, 1, 0);
        tl.draw();

        GL11.glPopMatrix();

        FontRenderer fr = this.mc.fontRenderer;

        GL11.glPushMatrix();
        GL11.glTranslated(width / 2 + ConfigHandler.arrowX, arrowHeight + (5 * ConfigHandler.arrowSize) + ConfigHandler.arrowY, 0);
        GL11.glScaled(ConfigHandler.arrowSize, ConfigHandler.arrowSize, 1F);
        GL11.glTranslatef(-fr.getStringWidth("# Blocks - Below"), 0, 0);
        fr.drawString("# Blocks - Below", fr.getStringWidth("# Blocks - Below") / 2,
                0, Constants.WHITE);
        GL11.glPopMatrix();

        fr.drawString("Size: " + Math.round(ConfigHandler.arrowSize * 100) + "%", ((width / 4) * 3) + 12,
                height - 43, Constants.WHITE);

        super.drawScreen(mouseX, mouseY, f);
    }

    @Override
    public void drawDefaultBackground() {
        super.drawDefaultBackground();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
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

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton != 0) {
            return;
        }

        //final int arrowWidth = 64;
        final int arrowHeight = 64;

        int baseX = width / 2;
        int baseY = arrowHeight / 2 + 5;

        final int clickRadius = 32;

        if (mouseX >= baseX + ConfigHandler.arrowX - clickRadius && mouseX <= baseX + ConfigHandler.arrowX + clickRadius && mouseY >= baseY + ConfigHandler.arrowY - clickRadius && mouseY <= baseY + ConfigHandler.arrowY + clickRadius
                ) {
            moving = true;
            startX = mouseX;
            startY = mouseY;
            startArrowX = ConfigHandler.arrowX;
            startArrowY = ConfigHandler.arrowY;
        } else {
            moving = false;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int mouseButton, long p_146273_4_) {
        if (!moving || mouseButton != 0) {
            return;
        }

        ConfigHandler.arrowX = startArrowX + mouseX - startX;
        ConfigHandler.arrowY = startArrowY + mouseY - startY;
        super.mouseClickMove(mouseX, mouseY, mouseButton, p_146273_4_);
    }

    @Override
    protected void mouseMovedOrUp(int mouseX, int mouseY, int mouseButton) {
        moving = false;
        super.mouseMovedOrUp(mouseX, mouseY, mouseButton);
    }
}
