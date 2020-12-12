package com.notebot.mod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiSlider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

/**
 * Created by Victor on 7/19/2015.
 * Ported by humboldt123 on 6/27/2020
 */
@SideOnly(Side.CLIENT)
public class GuiNotebot extends GuiScreen
{
    private static boolean Opened = false;

    public static boolean HasConverted = false;
    public static boolean AlreadyConverted = false;

    private static int MenuWidth = 320;
    private static int MenuHeight = 140;
    private static int MenuX = 0;
    private static int MenuY = 0;

    public static int Category = 1;//config, converter, editor, chooseeditorfile, player
    private static File ChosenFile;

    private static GuiFileList midifiles;
    private static GuiFileList jsonfiles;
    public static GuiEditorList editorchannels;

    private static GuiSlider discoverspeed;
    private static GuiSlider tunespeed;

    public GuiNotebot()
    {}

    @Override
    public void initGui()
    {
        this.buttonList.clear();

        this.buttonList.add(new GuiButton(0, MenuX - 31, MenuY - 50, 70, 20, "Instruments"));
        this.buttonList.add(new GuiButton(1, MenuX - 31 + 74, MenuY - 50, 100, 20, "MIDI Converter"));
        this.buttonList.add(new GuiButton(2, MenuX - 31 + 178, MenuY - 50, 50, 20, "Editor"));
        this.buttonList.add(new GuiButton(4, MenuX - 31 + 232, MenuY - 50, 50, 20, "Player"));
        this.buttonList.add(new GuiButton(5, MenuX - 31 + 286, MenuY - 50, 50, 20, "Other"));

        if(Category == 1)
        {
            this.buttonList.add(new GuiButton(100, MenuX+100, MenuY, 50, 20, "Map"));
            this.buttonList.add(new GuiButton(101, MenuX+100, MenuY + 23, 50, 20, "Map"));
            this.buttonList.add(new GuiButton(102, MenuX+100, MenuY + 46, 50, 20, "Map"));
            this.buttonList.add(new GuiButton(103, MenuX+100, MenuY + 69, 50, 20, "Map"));
            this.buttonList.add(new GuiButton(104, MenuX+100, MenuY + 92, 50, 20, "Map"));

            this.buttonList.add(new GuiButton(105, MenuX + 160, MenuY, 50, 20, "Discover"));
            this.buttonList.add(new GuiButton(106, MenuX + 160, MenuY + 23, 50, 20, "Discover"));
            this.buttonList.add(new GuiButton(107, MenuX + 160, MenuY + 46, 50, 20, "Discover"));
            this.buttonList.add(new GuiButton(108, MenuX + 160, MenuY + 69, 50, 20, "Discover"));
            this.buttonList.add(new GuiButton(109, MenuX + 160, MenuY + 92, 50, 20, "Discover"));

            this.buttonList.add(new GuiButton(110, MenuX + 220, MenuY, 50, 20, "Tune"));
            this.buttonList.add(new GuiButton(111, MenuX + 220, MenuY + 23, 50, 20, "Tune"));
            this.buttonList.add(new GuiButton(112, MenuX + 220, MenuY + 46, 50, 20, "Tune"));
            this.buttonList.add(new GuiButton(113, MenuX + 220, MenuY + 69, 50, 20, "Tune"));
            this.buttonList.add(new GuiButton(114, MenuX + 220, MenuY + 92, 50, 20, "Tune"));

            this.buttonList.add(new GuiButton(115, MenuX + 280, MenuY, 50, 20, "Override"));
            this.buttonList.add(new GuiButton(116, MenuX + 280, MenuY + 23, 50, 20, "Override"));
            this.buttonList.add(new GuiButton(117, MenuX + 280, MenuY + 46, 50, 20, "Override"));
            this.buttonList.add(new GuiButton(118, MenuX + 280, MenuY + 69, 50, 20, "Override"));
            this.buttonList.add(new GuiButton(119, MenuX + 280, MenuY + 92, 50, 20, "Override"));

            this.buttonList.add(new GuiButton(120, MenuX + MenuWidth / 2-60-120, MenuY + 120, 100, 20, "Center Player"));
            this.buttonList.add(new GuiButton(121, MenuX + MenuWidth / 2-60 , MenuY + 120, 100, 20, "Unmap Everything"));
            this.buttonList.add(new GuiButton(123, MenuX + MenuWidth / 2-60+120, MenuY + 120, 120, 20, "Cancel Tuning/Discover"));
        }
        else if(Category == 2)
        {
            File dir = new File("NoteBot/ToConvert");
            
            if (!dir.exists())
                dir.mkdirs();

            FileFilter filter = new WildcardFileFilter("*.mid");
            midifiles = new GuiFileList(dir.listFiles(filter), MenuX, MenuY, MenuWidth, MenuHeight);
            this.buttonList.add(new GuiButton(200, MenuX + MenuWidth / 2 - 50, MenuY + 120 + 24, 100, 20, "Convert"));
        }
        else if(Category == 3)
        {
            this.buttonList.add(new GuiButton(3, MenuX-26, MenuY-26, 100, 20, "Choose File"));
            editorchannels = new GuiEditorList(MenuX-26, MenuY, MenuWidth+52, MenuHeight);
            this.buttonList.add(new GuiButton(300, MenuX-26, MenuY+MenuHeight+4, 100, 20, "Preview Original"));
            this.buttonList.add(new GuiButton(301, MenuX-26+100+4, MenuY+MenuHeight+4, 100, 20, "Preview Instrument"));
            this.buttonList.add(new GuiButton(302, MenuX+MenuWidth-100+26, MenuY+MenuHeight+4, 100, 20, "Change Instrument"));

            this.buttonList.add(new GuiButton(303, MenuX+MenuWidth-100+26, MenuY-26, 100, 20, "Save"));

            if(ChosenFile != null)
                editorchannels.LoadChannels(ChosenFile);
        }
        else if(Category == 4)
        {
            File dir = new File("NoteBot/Converted");
            
            if (!dir.exists())
                dir.mkdirs();

            FileFilter filter = new WildcardFileFilter("*.json");

            jsonfiles = new GuiFileList(dir.listFiles(filter), MenuX-26, MenuY, MenuWidth+52, MenuHeight);
            this.buttonList.add(new GuiButton(400, MenuX+MenuWidth/2-50, MenuY+MenuHeight, 100, 20, "Choose"));
        }
        else if(Category == 5)
        {
            File dir = new File("NoteBot/Converted");
            
            if (!dir.exists())
                dir.mkdirs();
                

            FileFilter filter = new WildcardFileFilter("*.json");

            jsonfiles = new GuiFileList(dir.listFiles(filter), MenuX, MenuY, MenuWidth, MenuHeight);
            this.buttonList.add(new GuiButton(500, MenuX + MenuWidth / 2 - 50-54, MenuY + 120 + 24, 100, 20, "Play"));
            this.buttonList.add(new GuiButton(501, MenuX + MenuWidth / 2 - 50+54, MenuY + 120 + 24, 100, 20, "Stop"));
        }
        else if(Category == 6)
        {
            this.buttonList.add(new GuiButton(600, MenuX, MenuY, 100, 20, "Draw Notes: " + (NoteBot.DrawNotes ? "ON" : "OFF")));
            this.buttonList.add(new GuiButton(601, MenuX, MenuY + 30, 100, 20,  "Draw Blocks: " + (NoteBot.DrawBlocks ? "ON" : "OFF")));

            this.buttonList.add(new GuiButton(602, MenuX+110, MenuY, 100, 20, "Color: " + (NoteBot.NotesColorStyle ? "Note" : "Instrument")));
            this.buttonList.add(new GuiButton(603, MenuX+110, MenuY + 30, 100, 20, "Color: " + (NoteBot.BlocksColorStyle ? "Note" : "Instrument")));

            this.buttonList.add(new GuiButton(604, MenuX+220, MenuY, 100, 20, "Real Notes: " + (NoteBot.RealNotes ? "ON" : "OFF")));

            discoverspeed = new GuiSlider(605, MenuX, MenuY+60, MenuWidth, 20, "Discovery Delay: ", "", 10, 500, NoteBot.DiscoverSpeed, false, true);
            tunespeed = new GuiSlider(606, MenuX, MenuY+90, MenuWidth, 20, "Tuning Delay: ", "", 10, 500, NoteBot.TuneSpeed, false, true);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        drawDefaultBackground();

        MenuX = this.width/2 - (MenuWidth / 2);
        MenuY = this.height/2 - (MenuHeight / 2);

        drawGradientRect(MenuX - 30, MenuY - 30, MenuX + MenuWidth + 30, MenuY + MenuHeight + 30, 0xFFAAAAAA, 0xFFDDDDDD);

        if(Category == 1)
        {
            int offset = fontRenderer.FONT_HEIGHT/2+2;

            drawString(fontRenderer, "Piano", MenuX, MenuY + offset, 0xFFFFFFFF);
            drawHorizontalLine(MenuX, MenuX + MenuWidth + 10, MenuY + 20 + 1, 0xFFFFFFFF);

            drawString(fontRenderer, "Bass Guitar", MenuX, MenuY + 22 + offset, 0xFFFFFFFF);
            drawHorizontalLine(MenuX, MenuX + MenuWidth + 10, MenuY + 23 + 20 + 1, 0xFFFFFFFF);

            drawString(fontRenderer, "Snare Drum", MenuX, MenuY + 44 + offset, 0xFFFFFFFF);
            drawHorizontalLine(MenuX, MenuX + MenuWidth + 10, MenuY + 46 + 20 + 1, 0xFFFFFFFF);

            drawString(fontRenderer, "Sticks", MenuX, MenuY + 66 + offset, 0xFFFFFFFF);
            drawHorizontalLine(MenuX, MenuX + MenuWidth + 10, MenuY + 69 + 20 + 1, 0xFFFFFFFF);

            drawString(fontRenderer, "Bass Drum", MenuX, MenuY + 88 + offset, 0xFFFFFFFF);

        }
        else if(Category == 2)
        {
            midifiles.drawScreen(mouseX, mouseY, partialTicks);
            drawRect(MenuX, MenuY - 26, MenuX + MenuWidth, MenuY, 0xFFAAAAAA);
            drawRect(MenuX, MenuY + MenuHeight, MenuX+MenuWidth, MenuY+MenuHeight+26, 0xFFDDDDDD);

            if(AlreadyConverted)
                drawString(fontRenderer, "Already converted", MenuX + MenuWidth / 2 + 54, MenuY + MenuHeight + 10, 0xFFFFFF00);
            else
            {
                if (!HasConverted)
                    drawString(fontRenderer, "Waiting to convert", MenuX + MenuWidth / 2 + 54, MenuY + MenuHeight + 10, 0xFFFFFFFF);
                else
                    drawString(fontRenderer, "Converted", MenuX + MenuWidth / 2 + 54, MenuY + MenuHeight + 10, 0xFFAAFFAA);
            }
        }
        else if(Category == 3)
        {
            editorchannels.drawScreen(mouseX, mouseY, partialTicks);
            drawRect(MenuX-26, MenuY - 26, MenuX + MenuWidth+26, MenuY, 0xFFAAAAAA);
            drawRect(MenuX-26, MenuY + MenuHeight, MenuX + MenuWidth+26, MenuY + MenuHeight + 26, 0xFFDDDDDD);

            if(ChosenFile == null)
                drawString(fontRenderer, "No file chosen", MenuX-26+100+4, MenuY-20, 0xFFFFFFFF);
            else
                drawString(fontRenderer, ChosenFile.getName(), MenuX-26+100+4, MenuY-20, 0xFFFFFFFF);
        }
        else if(Category == 4)
        {
            jsonfiles.drawScreen(mouseX, mouseY, partialTicks);
            drawRect(MenuX-26, MenuY - 26, MenuX + MenuWidth+26, MenuY, 0xFFAAAAAA);
            drawRect(MenuX-26, MenuY + MenuHeight, MenuX + MenuWidth+26, MenuY + MenuHeight + 26, 0xFFDDDDDD);
        }
        else if(Category == 5)
        {
            jsonfiles.drawScreen(mouseX, mouseY, partialTicks);
            drawRect(MenuX, MenuY - 26, MenuX + MenuWidth, MenuY, 0xFFAAAAAA);
            drawRect(MenuX, MenuY + MenuHeight, MenuX + MenuWidth, MenuY + MenuHeight + 26, 0xFFDDDDDD);
        }
        else if(Category == 6)
        {
            discoverspeed.drawButtonForegroundLayer(mouseX, mouseY);
            discoverspeed.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, 1);
            tunespeed.drawButtonForegroundLayer(mouseX, mouseY);
            tunespeed.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, 1);
        }

        if(!Opened)
        {
            initGui();
            Opened = true;
        }


        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseButton == 0 && Category == 6)
        {
            if(discoverspeed == null || tunespeed == null)
                return;

            discoverspeed.mousePressed(this.mc, mouseX, mouseY);
            tunespeed.mousePressed(this.mc, mouseX, mouseY);

            NoteBot.DiscoverSpeed = discoverspeed.getValueInt();
            NoteBot.TuneSpeed = tunespeed.getValueInt();
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        super.mouseReleased(mouseX, mouseY, state);

        if (state == 0 && Category == 6)
        {
            if(discoverspeed == null || tunespeed == null)
                return;

            discoverspeed.mouseReleased(mouseX, mouseY);
            tunespeed.mouseReleased(mouseX, mouseY);

            NoteBot.DiscoverSpeed = discoverspeed.getValueInt();
            NoteBot.TuneSpeed = tunespeed.getValueInt();
        }
    }

    public void actionPerformed(GuiButton button)
    {
        switch(button.id)
        {

        case 100:
            Mapper.MapInstrument5();
            break;
        case 101:
            Mapper.MapInstrument1();
            break;
        case 102:
            Mapper.MapInstrument3();
            break;
        case 103:
            Mapper.MapInstrument4();
            break;
        case 104:
            Mapper.MapInstrument2();
            break;
        case 105:
            assert Mapper.GetInstrument(5) != null;
            Mapper.GetInstrument(5).DiscoverInstrument();
            break;
        case 106:
            assert Mapper.GetInstrument(1) != null;
            Mapper.GetInstrument(1).DiscoverInstrument();
            break;
        case 107:
            assert Mapper.GetInstrument(3) != null;
            Mapper.GetInstrument(3).DiscoverInstrument();
            break;
        case 108:
            assert Mapper.GetInstrument(4) != null;
            Mapper.GetInstrument(4).DiscoverInstrument();
            break;
        case 109:
            assert Mapper.GetInstrument(2) != null;
            Mapper.GetInstrument(2).DiscoverInstrument();
            break;
        case 110:
            assert Mapper.GetInstrument(5) != null;
            Mapper.GetInstrument(5).TuneInstrument();
            break;
        case 111:
            assert Mapper.GetInstrument(1) != null;
            Mapper.GetInstrument(1).TuneInstrument();
            break;
        case 112:
            assert Mapper.GetInstrument(3) != null;
            Mapper.GetInstrument(3).TuneInstrument();
            break;
        case 113:
            assert Mapper.GetInstrument(4) != null;
            Mapper.GetInstrument(4).TuneInstrument();
            break;
        case 114:
            assert Mapper.GetInstrument(2) != null;
            Mapper.GetInstrument(2).TuneInstrument();
            break;
        case 115:
            assert Mapper.GetInstrument(5) != null;
            Mapper.GetInstrument(5).OverrideValidation();
            break;
        case 116:
            assert Mapper.GetInstrument(1) != null;
            Mapper.GetInstrument(1).OverrideValidation();
            break;
        case 117:
            assert Mapper.GetInstrument(3) != null;
            Mapper.GetInstrument(3).OverrideValidation();
            break;
        case 118:
            assert Mapper.GetInstrument(4) != null;
            Mapper.GetInstrument(4).OverrideValidation();
            break;
        case 119:
            assert Mapper.GetInstrument(2) != null;
            Mapper.GetInstrument(2).OverrideValidation();
            break;
        case 120:
            EntityPlayerSP ply = Minecraft.getMinecraft().player;
            float modx = ply.posX > 0f ? 0.5f : -0.5f;
            float modz = ply.posZ > 0f ? 0.5f : -0.5f;
            ply.setPosition((int) ply.posX + modx, (int) ply.posY + 0.3f, (int) ply.posZ + modz);

            break;
        case 121:
            Mapper.Unmap();
            break;
        case 122:

            if(Instrument.ToTune != null)
                Instrument.ToTune.clear();

            Instrument.Tunning = false;

            if(Instrument.ToDiscover != null)
                Instrument.ToDiscover.clear();

            Instrument.Discovering = false;
            Instrument.CurrentInstrument = -1;
        case 0:
            Category = 1;
            initGui();

            break;
        case 1:
            Category = 2;
            initGui();

            break;
        case 2:
            Category = 3;
            initGui();

            break;
        case 3:
            Category = 4;
            initGui();
            break;
        case 4:
            Category = 5;
            initGui();

            break;
        case 5:
            Category = 6;
            initGui();

            break;
        case 200:
            File file = midifiles.GetSelectedFile();

            if (file == null || IsAlreadyConverted(file.getName()))
            {
                AlreadyConverted = true;
                break;
            }

            try
            {
                Converter.Convert(file.getAbsolutePath());
                HasConverted = true;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            break;

        case 300:

            if(editorchannels.GetSelectedChannel() == null)
                break;

            try
            {
                Util.PlayNote(60, editorchannels.GetSelectedChannel().originalinsturment);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            break;

        case 301:

            if(editorchannels.GetSelectedChannel() == null)
                break;

           // String res = Util.InstrumentIntToResource(editorchannels.GetSelectedChannel().instrument);
          //TODO I FUCKING STG THIS SHIT IS RETARDED  Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.createAccessor(new ResourceLocation(res)));
            break;

        case 302:

            Channel channel = editorchannels.GetSelectedChannel();

            if(channel == null)
                break;

            channel.instrument = (channel.instrument + 1) % 6;
            break;

        case 303:
            editorchannels.Save();
            break;

        case 400:
            ChosenFile = jsonfiles.GetSelectedFile();
            Category = 3;
            initGui();
            editorchannels.LoadChannels(ChosenFile);
            break;

        case 500:
            if(jsonfiles.GetSelectedFile() == null)
                break;

            Player.PlayingMusic = new Music(jsonfiles.GetSelectedFile().getAbsolutePath());
            Player.Started = false;
            Player.Playing = true;
            break;

        case 501:
            Player.PlayingMusic = null;
            Player.Started = false;
            Player.Playing = false;
            break;

        case 600:
            NoteBot.DrawNotes = !NoteBot.DrawNotes;
            initGui();
            break;

        case 601:
            NoteBot.DrawBlocks = !NoteBot.DrawBlocks;
            initGui();
            break;

        case 602:
            NoteBot.NotesColorStyle = !NoteBot.NotesColorStyle;
            initGui();
            break;

        case 603:
            NoteBot.BlocksColorStyle = !NoteBot.BlocksColorStyle;
            initGui();
            break;

        case 604:
            NoteBot.RealNotes = !NoteBot.RealNotes;
            initGui();
            break;
        }
    }

    private boolean IsAlreadyConverted(String filename)
    {
        filename = filename.substring(0, filename.length()-4);

        File dir = new File("NoteBot/Converted");

        File[] files = dir.listFiles();
        assert files != null;
        for(File file : files)
        {
            if(file.getName().substring(0, file.getName().length()-5).equals(filename))
                return true;
        }

        return false;
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
    }
}
