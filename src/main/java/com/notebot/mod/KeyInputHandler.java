package com.notebot.mod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Victor on 7/14/2015.
 * Ported by humboldt123 on 6/27/2020
 */
@SideOnly(Side.CLIENT)
public class KeyInputHandler
{
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event)
    {
        EntityPlayerSP ply = Minecraft.getMinecraft().player;

        if(NoteBot.opengui.isPressed())
        {

            ply.openGui(NoteBot.instance, 1, Minecraft.getMinecraft().world, 0, 0, 0);
        }
/*
        if(NoteBot.play.isPressed())
        {
            NoteBot.PlayingMusic = new Music("a.json");
            NoteBot.Started = false;
            NoteBot.Playing = true;
        }
*/


    }
}
