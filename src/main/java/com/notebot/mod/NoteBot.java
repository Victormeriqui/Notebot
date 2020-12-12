package com.notebot.mod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.vecmath.Vector3d;
import java.awt.*;

@SideOnly(Side.CLIENT)
@Mod(modid = NoteBot.MODID, version = NoteBot.VERSION, name = NoteBot.NAME)
public class NoteBot
{
    public static final String MODID = "hypernotebot";
    public static final String NAME = "Hyper Notebot";
    public static final String VERSION = "1.0";

    @Mod.Instance(MODID)
    public static NoteBot instance;

    public static boolean DrawNotes = true;
    public static boolean NotesColorStyle;//false = inst, true = pitch
    public static boolean RealNotes;

    public static boolean DrawBlocks;
    public static boolean BlocksColorStyle;//false = inst, true = pitch

    public static Player Player;
    public static Thread PlayerThread;

    public static int DiscoverSpeed = 50;
    public static int TuneSpeed = 50;

    private static String lastnote = "";
    private static Color lastnotecolor = Color.black;

    @SubscribeEvent
    public void OnHUD(RenderGameOverlayEvent e)
    {
       if(e.getType() != RenderGameOverlayEvent.ElementType.TEXT)
            return;

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP ply = mc.player;

        //dunno why the resolution is doubled lol
        int scrw = mc.displayWidth/2;
        int scrh = mc.displayHeight/2;
        int fontheight = mc.fontRenderer.FONT_HEIGHT;

        if(Player.Playing)
        {
            String playingstr = "Playing: " + Player.PlayingMusic.GetName();
            int playingstrwidth = mc.fontRenderer.getStringWidth(playingstr);

            mc.fontRenderer.drawString(playingstr, scrw / 2 - playingstrwidth / 2, 10, 0xFFFFFFFF);
            mc.fontRenderer.drawString(lastnote, scrw / 2, 20, lastnotecolor.getRGB() | 0xFF000000);


            GuiIngame.drawRect(scrw / 2 - 200, 30, scrw / 2 + 200, 40, 0xFFAAAAAA);

            float headx = Util.Interpolate(Player.PlayingMusic.GetProgress(), 0.0f, 1.0f, scrw/2-200, scrw/2+200);

            GuiIngame.drawRect(scrw/2-200, 30, scrw/2+200, 40, 0xFFAAAAAA);
            GuiIngame.drawRect((int) headx - 5, 30, (int) headx + 5, 40, 0xFFFFFFFF);


            double mins = Player.PlayingMusic.GetLength() / 60000.0;
            double secs = 60.0 * (mins - (int)mins);
            int mini = (int)mins;
            int seci = (int)secs;

            String endstr = (mini < 10 ? "0" + mini : ""+mini) + ":" + (seci < 10 ? "0" + seci : ""+seci);
            int startwidth = mc.fontRenderer.getStringWidth("00:00");

            mc.fontRenderer.drawString("00:00", scrw/2-200-startwidth-5, 31, 0xFFFFFFFF);
            mc.fontRenderer.drawString(endstr, scrw/2+200+5, 31, 0xFFFFFFFF);

            float current = Util.Interpolate(Player.PlayingMusic.GetProgress(), 0.0f, 1.0f, 0.0f, Player.PlayingMusic.GetLength());
            double cmins =  current / 60000.0;
            double csecs = 60.0 * (cmins - (int)cmins);
            int cmini = (int)cmins;
            int cseci = (int)csecs;

            String currentstr = (cmini < 10 ? "0" + cmini : ""+cmini) + ":" + (cseci < 10 ? "0" + cseci : ""+cseci);
            int currentstrwidth = mc.fontRenderer.getStringWidth(currentstr);

            mc.fontRenderer.drawString(currentstr, scrw/2-currentstrwidth/2, 50, 0xFFFFFFFF);
        }

    }

    @SubscribeEvent
    public void OnRender(RenderWorldLastEvent e)
    {
        EntityPlayerSP ply = Minecraft.getMinecraft().player;

        double x = ply.lastTickPosX + (ply.posX - ply.lastTickPosX) * e.getPartialTicks();
        double y = ply.lastTickPosY + (ply.posY - ply.lastTickPosY) * e.getPartialTicks();
        double z = ply.lastTickPosZ + (ply.posZ - ply.lastTickPosZ) * e.getPartialTicks();

        Vector3d plypos = new Vector3d(x, y, z);

        for(int inst = 1; inst < 6; inst++)
        {
            Instrument instrument = Mapper.GetInstrument(inst);

            if(instrument == null)
                continue;

            for(int pitch = 0; pitch <= 24; pitch++)
            {
                BlockPos pos = instrument.GetNotePosition(pitch);

                if(pos == null)
                    continue;

                if(DrawBlocks)
                {
                    Color col = BlocksColorStyle ? Util.InterpolateColor(pitch, 0, 24, Color.red, Color.green) : Mapper.GetInstrumentColor(inst);


                    if(instrument.IsNoteValidated(pitch))
                    {
                        Draw.Prism(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, 1f, 1f, 1f, plypos, col);
                    }
                    else
                    {
                        if (instrument.IsNoteDiscovered(pitch))
                            Draw.Prism(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, 1f, 1f, 1f, plypos, Color.red);
                        else
                            Draw.Prism(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, 1f, 1f, 1f, plypos, Color.white);
                    }
                }

                if(DrawNotes)
                {
                    Color col = NotesColorStyle ? Util.InterpolateColor(pitch, 0, 24, Color.red, Color.green) : Mapper.GetInstrumentColor(inst);

                    if(instrument.IsNoteValidated(pitch))
                    {
                        Draw.Text(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, "" + (RealNotes ? Util.GetRealNote(pitch) : pitch), plypos, col);
                    }
                    else
                    {
                        if (instrument.IsNoteDiscovered(pitch))
                            Draw.Text(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, "X", plypos, Color.red);
                        else
                            Draw.Text(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, "?", plypos, Color.white);
                    }
                }
            }
        }


        //Draw.Line(71, 6, 1539, 71, 6, 1550, plypos, Color.BLUE, 10f);
        // Draw.OutlinedPrism(71, 6, 1539, 2, 2, 2, plypos, Color.BLUE, 2f);
        //Draw.Text(71, 6, 1539, "hello", plypos, Color.BLUE);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void OnNotePlayed(NoteBlockEvent event)
    {
        if(Instrument.Tunning)
        {
            if(Instrument.ToTune.size() == 0)
            {
                Instrument.CurrentTuneComplete = true;
                return;
            }

            if(event.getVanillaNoteId() == Instrument.ToTune.get(0).GetPitch())
                Instrument.CurrentTuneComplete = true;
        }
        if(Instrument.Discovering)
        {
            Note note = Instrument.ToDiscover.get(0);

            note.SetKnownPitch(event.getVanillaNoteId());

            if(event.getVanillaNoteId() == note.GetPitch())
                note.SetValidated(true);

            Instrument.CurrentDiscoveryComplete = true;
        }

    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        FMLCommonHandler.instance().bus().register(new KeyInputHandler());
    }

    public static KeyBinding opengui;

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);

        opengui = new KeyBinding("Open GUI", Keyboard.KEY_G, "NoteBot");

        Player = new Player();
        PlayerThread = new Thread(Player);

        Player.Running = true;
        PlayerThread.start();

        ClientRegistry.registerKeyBinding(opengui);
    }



    long lsttime = 0;
    long starttime = 0;
    @SubscribeEvent
    public void OnTick(TickEvent.ClientTickEvent event)
    {
        if(Instrument.Tunning)
        {
            if(Instrument.ToTune.size() == 0)
            {
                Instrument.Tunning = false;
                Instrument.CurrentInstrument = -1;
                return;
            }

            Note note = Instrument.ToTune.get(0);

            if(!Instrument.CurrentTuneComplete)
            {
                if(System.currentTimeMillis()-lsttime >= TuneSpeed)
                {
                    Util.RightClick(note.GetPosition());
                    lsttime = System.currentTimeMillis();
                }
            }
            else
            {
                Instrument.ToTune.remove(0);
                Instrument.CurrentTuneComplete = false;
            }
        }

        if(Instrument.Discovering)
        {
            if(Instrument.ToDiscover.size() == 0)
            {
                Instrument.Discovering = false;
                Instrument.CurrentInstrument = -1;
                return;
            }

            Note note = Instrument.ToDiscover.get(0);

            if(!Instrument.CurrentDiscoveryComplete)
            {
                if(System.currentTimeMillis()-lsttime >= DiscoverSpeed)
                {
                    Util.LeftClick(note.GetPosition());
                    lsttime = System.currentTimeMillis();
                }
            }
            else
            {
                Instrument.ToDiscover.remove(0);
                Instrument.CurrentDiscoveryComplete = false;
            }
        }

    }
}
