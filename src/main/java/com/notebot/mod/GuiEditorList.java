package com.notebot.mod;

import com.google.gson.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import javax.sound.midi.MidiUnavailableException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Victor on 7/27/2015.
 * Ported by humboldt123 on 6/27/2020
 */
@SideOnly(Side.CLIENT)
class Channel
{
    int channelnumber;
    int originalinsturment;
    int instrument;

    public Channel(int channelnumber, int originalinsturment, int instrument)
    {
        this.channelnumber = channelnumber;
        this.originalinsturment = originalinsturment;
        this.instrument = instrument;
    }

}

@SideOnly(Side.CLIENT)
public class GuiEditorList extends GuiScrollingList
{

    private File musicfile;
    private ArrayList<Channel> list;
    private Channel selected;
    private int width;

    public GuiEditorList(int x, int y, int width, int height)
    {
        super(Minecraft.getMinecraft(), width, height, y, y + height, x, 20);

        this.width = width;

        list = new ArrayList<Channel>();
    }

    public void LoadChannels(File file)
    {
        list.clear();

        musicfile = file;

        JsonParser parse = new JsonParser();
        JsonElement main = null;
        try
        {
            main = parse.parse(new String(Files.readAllBytes(file.toPath())));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        JsonObject data = main.getAsJsonObject().getAsJsonObject("Data");

        for(Map.Entry<String, JsonElement> entry : data.entrySet())
        {
            JsonObject channelobj = entry.getValue().getAsJsonObject();

            int channeln = Integer.parseInt(entry.getKey());
            int instrument = channelobj.get("Instrument").getAsInt();
            int origiinstrument = channelobj.get("OriginalInstrument").getAsInt();

            Channel channel = new Channel(channeln, origiinstrument, instrument);

            list.add(channel);
        }
    }

    public void Save()
    {
        JsonParser parse = new JsonParser();
        JsonElement main = null;
        try
        {
            main = parse.parse(new String(Files.readAllBytes(musicfile.toPath())));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        JsonObject data = main.getAsJsonObject().getAsJsonObject("Data");

        for(Map.Entry<String, JsonElement> entry : data.entrySet())
        {
            JsonObject channelobj = entry.getValue().getAsJsonObject();
            int channeln = Integer.parseInt(entry.getKey());

            int newinst = channelobj.get("Instrument").getAsInt();
            for(Channel channel : list)
            {
                if(channel.channelnumber == channeln)
                {
                    newinst = channel.instrument;
                    break;
                }
            }

            channelobj.addProperty("Instrument", newinst);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String finaljson = gson.toJson(main);

        try
        {
            FileUtils.writeStringToFile(new File("NoteBot/Converted/" + musicfile.getName()), finaljson, "UTF-8");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Channel GetSelectedChannel()
    {
        return selected;
    }

    @Override
    protected int getSize()
    {
        return list.size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick)
    {
        Channel channel = list.get(index);

        selected = channel;
    }

    @Override
    protected boolean isSelected(int index)
    {
        if(selected == null)
            return false;

        return list.get(index) == selected;
    }

    @Override
    protected void drawBackground()
    {

    }

    @Override
    protected void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5)
    {
        FontRenderer font = Minecraft.getMinecraft().fontRenderer;
        Channel channel = list.get(var1);

        String channelnumber = "" + channel.channelnumber;
        String instrument = Util.InstrumentIntToString(channel.instrument);
        String originalinstrument = "Unknown";

        try
        {
            originalinstrument = Util.GetInstrumentName(channel.originalinsturment);
        }
        catch (MidiUnavailableException e)
        {
            e.printStackTrace();
        }

        int channelnumberwidth = font.getStringWidth(channelnumber);
        int instrumentwidth = font.getStringWidth(instrument);
        int originalinstrumentwidth = font.getStringWidth(originalinstrument);


       font.drawString(channelnumber, var2-width+10, var3, 0xFFFFFFFF);
       font.drawString(originalinstrument, var2 - width/2 - 20, var3, 0xFFFFFFFF);
       font.drawString(instrument, var2-instrumentwidth, var3, 0xFFFFFFFF);
    }
}
