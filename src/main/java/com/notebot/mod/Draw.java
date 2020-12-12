package com.notebot.mod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector3d;
import java.awt.*;

/**
 * Created by Victor on 7/14/2015.
 * Ported by humboldt123 on 6/27/2020
 */
@SideOnly(Side.CLIENT)
public class Draw
{

    public static void Line(double sx, double sy, double sz, double ex, double ey, double ez, Vector3d plypos, Color color, float width)
    {
        float r = Util.Interpolate(color.getRed(), 0, 255, 0, 1);
        float g = Util.Interpolate(color.getGreen(), 0, 255, 0, 1);
        float b = Util.Interpolate(color.getBlue(), 0, 255, 0, 1);

        double rx = ex-sx;
        double ry = ey-sy;
        double rz = ez-sz;

        sx = -plypos.getX()+sx;
        sy = -plypos.getY()+sy;
        sz = -plypos.getZ()+sz;

        GL11.glPushMatrix();

        GL11.glTranslated(sx, sy, sz);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

        GL11.glLineWidth(width);

        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glColor4f(r, g, b, 1f);

        GL11.glBegin(GL11.GL_LINES);

        GL11.glVertex3d(0, 0, 0);
        GL11.glVertex3d(rx, ry, rz);

        GL11.glEnd();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ZERO);

        GL11.glPopMatrix();
    }

    public static void Prism(double x, double y, double z, double width, double height, double depth, Vector3d plypos, Color color)
    {
        float r = Util.Interpolate(color.getRed(), 0, 255, 0, 1);
        float g = Util.Interpolate(color.getGreen(), 0, 255, 0, 1);
        float b = Util.Interpolate(color.getBlue(), 0, 255, 0, 1);

        double wh = width / 2;
        double hh = height / 2;
        double dh = depth / 2;

        x = -plypos.getX()+x;
        y = -plypos.getY()+y;
        z = -plypos.getZ()+z;

        GL11.glPushMatrix();

        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glTranslated(x, y, z);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glColor4f(r, g, b, 0.25f);

        GL11.glBegin(GL11.GL_QUADS);

        GL11.glVertex3d(wh, hh, -dh); GL11.glVertex3d(wh, -hh, -dh); GL11.glVertex3d(-wh, -hh, -dh); GL11.glVertex3d(-wh, hh, -dh);
        GL11.glVertex3d(wh, hh, dh); GL11.glVertex3d(wh, -hh, dh); GL11.glVertex3d(wh, -hh, -dh); GL11.glVertex3d(wh, hh, -dh);
        GL11.glVertex3d(-wh, hh, dh); GL11.glVertex3d(-wh, -hh, dh); GL11.glVertex3d(wh, -hh, dh); GL11.glVertex3d(wh, hh, dh);
        GL11.glVertex3d(-wh, hh, -dh); GL11.glVertex3d(-wh, -hh, -dh); GL11.glVertex3d(-wh, -hh, dh); GL11.glVertex3d(-wh, hh, dh);
        GL11.glVertex3d(wh, -hh, -dh); GL11.glVertex3d(wh, -hh, dh); GL11.glVertex3d(-wh, -hh, dh); GL11.glVertex3d(-wh, -hh, -dh);
        GL11.glVertex3d(-wh, hh, dh); GL11.glVertex3d(wh, hh, dh); GL11.glVertex3d(wh, hh, -dh); GL11.glVertex3d(-wh, hh, -dh);

        GL11.glEnd();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ZERO);

        GL11.glPopMatrix();
    }

    public static void OutlinedPrism(double x, double y, double z, double width, double height, double depth, Vector3d plypos, Color color, float lwidth)
    {
        float r = Util.Interpolate(color.getRed(), 0, 255, 0, 1);
        float g = Util.Interpolate(color.getGreen(), 0, 255, 0, 1);
        float b = Util.Interpolate(color.getBlue(), 0, 255, 0, 1);

        double wh = width / 2;
        double hh = height / 2;
        double dh = depth / 2;

         x = -plypos.getX()+x;
         y = -plypos.getY()+y;
         z = -plypos.getZ()+z;

        GL11.glPushMatrix();

        GL11.glTranslated(x, y, z);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glLineWidth(lwidth);

        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glColor4f(r, g, b, 1f);

        GL11.glBegin(GL11.GL_LINE_STRIP);

        GL11.glVertex3d(-wh, -hh, -dh); GL11.glVertex3d(-wh, hh, -dh); GL11.glVertex3d(wh, hh, -dh); GL11.glVertex3d(wh, -hh, -dh);
        GL11.glVertex3d(wh, -hh, dh); GL11.glVertex3d(wh, hh, dh); GL11.glVertex3d(-wh, hh, dh); GL11.glVertex3d(-wh, -hh, dh);
        GL11.glVertex3d(-wh, -hh, -dh); GL11.glVertex3d(wh, -hh, -dh); GL11.glVertex3d(wh, hh, -dh); GL11.glVertex3d(wh, hh, dh);
        GL11.glVertex3d(wh, -hh, dh); GL11.glVertex3d(-wh, -hh, dh); GL11.glVertex3d(-wh, hh, dh); GL11.glVertex3d(-wh, hh, -dh);

        GL11.glEnd();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ZERO);

        GL11.glPopMatrix();
    }

    public static void Text(double x, double y, double z, String text, Vector3d plypos, Color color)
    {
        float r = Util.Interpolate(color.getRed(), 0, 255, 0, 1);
        float g = Util.Interpolate(color.getGreen(), 0, 255, 0, 1);
        float b = Util.Interpolate(color.getBlue(), 0, 255, 0, 1);

        RenderManager rm = Minecraft.getMinecraft().getRenderManager();

        x = -plypos.getX()+x;
        y = -plypos.getY()+y;
        z = -plypos.getZ()+z;

        GL11.glPushMatrix();

        GL11.glTranslated(x, y, z);

        GL11.glRotatef(-rm.playerViewY, 0f, 1f, 0f);
        GL11.glRotatef(rm.playerViewX, 1f, 0f, 0f);
        GL11.glScalef(-0.05f, -0.05f, 0.05f);

      //  GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glColor4f(r, g, b, 1f);

        Minecraft.getMinecraft().fontRenderer.drawString(text, -Minecraft.getMinecraft().fontRenderer.getStringWidth(text) / 2, -Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT / 2, color.getRGB());

        //GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ZERO);

        GL11.glPopMatrix();
    }

}
