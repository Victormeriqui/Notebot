package com.notebot.mod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;

/**
 * Created by Victor on 7/27/2015.
 * Ported by humboldt123 on 6/27/2020
 */
@SideOnly(Side.CLIENT)
public class GuiFileList extends GuiScrollingList
{
    private File[] list;
    private File selected;
    private int width;

    public GuiFileList(File[] list, int x, int y, int width, int height)
    {
        super(Minecraft.getMinecraft(), width, height, y, y+height, x, 20);

        this.width = width;
        this.list = list;
    }

    public File GetSelectedFile()
    {
        return selected;
    }

    @Override
    protected int getSize()
    {
        return list.length;
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick)
    {
        selectedIndex = index;
        selected = list[index];
        GuiNotebot.HasConverted = false;
        GuiNotebot.AlreadyConverted = false;
    }

    @Override
    protected boolean isSelected(int index)
    {
        if(selected == null)
            return false;

        return list[index] == selected;
    }

    @Override
    protected void drawBackground()
    {

    }

    @Override
    protected int getContentHeight()
    {
        return list.length*20 + 1;
    }

    @Override
    protected void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5)
    {
        File file = list[var1];
        String name = file.getName();
        int namewidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(name);

        Minecraft.getMinecraft().fontRenderer.drawString(file.getName(), var2 -width + 20, var3, 0xFFFFFFFF);
    }
}
