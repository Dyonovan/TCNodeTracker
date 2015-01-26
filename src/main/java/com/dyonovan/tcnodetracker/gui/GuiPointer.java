package com.dyonovan.tcnodetracker.gui;

import com.dyonovan.tcnodetracker.TCNodeTracker;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import org.lwjgl.opengl.GL11;


public class GuiPointer extends Gui {

    private static final ResourceLocation arrow = new ResourceLocation("tcnodetracker:textures/gui/arrow.png");
    private Minecraft mc;


    public GuiPointer(Minecraft mc) {
        super();

        this.mc = mc;
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {

        if (event.isCancelable() || event.type != ElementType.EXPERIENCE || !TCNodeTracker.doGui ||
                this.mc.thePlayer.inventory.armorItemInSlot(3) == null){
            return;
        }

        if (!this.mc.thePlayer.inventory.armorItemInSlot(3).getItem().getUnlocalizedName().equalsIgnoreCase("item.ItemGoggles")) {
            return;
        }

        double direction = (Math.toDegrees(Math.atan2(TCNodeTracker.xMarker - this.mc.thePlayer.posX,
                TCNodeTracker.zMarker - this.mc.thePlayer.posZ))) + this.mc.thePlayer.rotationYaw;

        Tessellator tl = Tessellator.instance;
        this.mc.getTextureManager().bindTexture(arrow);
        ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        double width = (scaledresolution.getScaledWidth() - 50) / 2;

        GL11.glPushMatrix();
        GL11.glTranslated(width + 25, 30, 0);
        GL11.glRotatef((float) -direction, 0, 0, 1);
        GL11.glTranslated(-25, -25, 0);

        tl.startDrawingQuads();
        tl.addVertexWithUV(0, 0, 0, 0, 0);
        tl.addVertexWithUV(0, 50, 0, 0, 1);
        tl.addVertexWithUV(50, 50, 0, 1, 1);
        tl.addVertexWithUV(50, 0, 0, 1, 0);

        tl.draw();

        GL11.glPopMatrix();
    }
}
