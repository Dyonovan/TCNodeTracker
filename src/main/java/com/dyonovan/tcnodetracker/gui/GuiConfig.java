package com.dyonovan.tcnodetracker.gui;

import com.dyonovan.tcnodetracker.bindings.KeyBindings;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiConfig extends GuiScreen {

    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;

    private static final ResourceLocation arrow = new ResourceLocation("tcnodetracker:textures/gui/arrow.png");

    public GuiConfig() { }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        buttonList.clear();

        buttonList.add(new GuiButton(UP, (width / 2) - 6, height - 75, 12, 20, "^"));
        buttonList.add(new GuiButton(DOWN, (width / 2) - 6, height - 25, 12, 20, "v"));
        buttonList.add(new GuiButton(LEFT, (width / 2) - 24, height - 50, 12, 20, "<"));
        buttonList.add(new GuiButton(RIGHT, (width / 2) + 12, height - 50, 12, 20, ">"));

        updateScreen();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float f) {

        drawDefaultBackground();

        super.drawScreen(mouseX, mouseY, f);
    }

    @Override
    public void drawDefaultBackground() {
        super.drawDefaultBackground();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(arrow);

        GL11.glPushMatrix();
        GL11.glTranslated((width / 2) + 25, 30, 0);
        GL11.glRotatef((float) 1, 0, 0, 1);
        GL11.glTranslated(-25, -25, 0);

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
}
