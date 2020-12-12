package com.notebot.mod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Victor on 7/24/2015.
 * Ported by humboldt123 on 6/27/2020
*/

@SideOnly(Side.CLIENT)
public class Converter
{
    static final int NOTE_ON = 0x90;
    static int SET_TEMPO = 0x51;
    static int TIME_SIGNATURE = 0x58;
    static int PATCH_CHANGE = 0xC0;

    static Sequence seq;
    static int[] channelprograms;

    public static void Convert(String filename) throws InvalidMidiDataException, IOException
    {
        File file = new File(filename);

        seq = MidiSystem.getSequence(file);
        channelprograms = new int[30];

        JsonObject main = new JsonObject();
        main.addProperty("Name", file.getName().substring(0, file.getName().length() - 4));

        JsonObject data = new JsonObject();
        main.addProperty("Length", 0);
        main.add("Data", data);

        HashMap<Integer, JsonObject> jsonchannels = new HashMap<Integer, JsonObject>();

        long maxstamp = 0;

        float minuteinms = 60000000;

        int timesignum = 4;
        int timesigden = 4;
        int bpm = 120;
        int mspqn = 500000;

        int res = seq.getResolution();

        int trackn = 0;
        for(Track track : seq.getTracks())
        {
            for(int eventidx = 0; eventidx < track.size(); eventidx++)
            {
                MidiEvent event = track.get(eventidx);

                if(IsTimeSignature(event))
                {
                    MetaMessage mmsg = (MetaMessage)event.getMessage();

                    byte[] timesig = mmsg.getMessage();

                    timesignum = timesig[2];
                    timesigden = timesig[3];

                    // bpm = ( minuteinms / newMicrosecondsPerQuarterNote ) * ( denominator / 4.0f );

                    System.out.println("TIME_SIGNATURE Track: " + trackn +" msg: num: " + timesignum + " den: " + timesigden  + " Tick: " + event.getTick());
                }
                else if(IsTempoChange(event))
                {
                    MetaMessage mmsg = (MetaMessage)event.getMessage();

                    //500000
                    mspqn = new BigInteger(mmsg.getData()).intValue();

                    System.out.println("SET_TEMPO Track: " + trackn +" msg: " + mspqn  + " Tick: " + event.getTick());
                }
                else if(IsNoteOn(event))
                {
                    ShortMessage smsg = (ShortMessage)event.getMessage();

                    int note = smsg.getData1();
                    long tick = event.getTick();
                    int channel = smsg.getChannel();

                    long time = (long)(((tick * (mspqn/res)) / 1000.0) + 0.5);

                    note = note % 24;

                    if(time >= maxstamp)
                        maxstamp = time;

                    if(jsonchannels.get(channel) == null)
                    {
                        JsonObject jsonchannel = new JsonObject();
                        jsonchannels.put(channel, jsonchannel);

                        jsonchannel.addProperty("OriginalInstrument", channelprograms[channel]);
                        jsonchannel.addProperty("Instrument", "0");

                        JsonArray jsonnotes = new JsonArray();
                        jsonchannel.add("Notes", jsonnotes);
                    }

                    JsonObject element = new JsonObject();

                    element.addProperty("Note", "" + note);
                    element.addProperty("Tick", "" + time);

                    jsonchannels.get(channel).getAsJsonArray("Notes").add(element);

                    System.out.println("NOTE_ON Track: " + trackn + " Note: " + note + " Instrument: " + channelprograms[channel] + " Channel: " + channel + " Tick: " + tick + " Time: " + time);
                }
                else if(IsPatchChange(event))
                {
                    ShortMessage smsg = (ShortMessage)event.getMessage();
                    long tick = event.getTick();
                    int channel = smsg.getChannel();
                    int program = smsg.getData1();

                    channelprograms[channel] = program+1;

                    System.out.println("PATCH_CHANGE Track: " + trackn + " Program: " + program + " Channel: " + channel + " Tick: " + tick);
                }
            }

            trackn++;
        }

        int jsonchanneln = 1;
        for(Map.Entry<Integer, JsonObject> entryset : jsonchannels.entrySet())
        {
            data.add("" + jsonchanneln, entryset.getValue());

            jsonchanneln++;
        }

        main.addProperty("Length", maxstamp);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String finaljson = gson.toJson(main);

        FileUtils.writeStringToFile(new File("NoteBot/Converted/"  + file.getName().substring(0, file.getName().length() - 4) + ".json"), finaljson, "UTF-8");

    }

    private static boolean IsPatchChange(MidiEvent event)
    {
        if(!(event.getMessage() instanceof ShortMessage))
            return false;

        ShortMessage smsg = (ShortMessage)event.getMessage();

        if(smsg.getStatus() >= 0xC0 && smsg.getStatus() <= 0xCF)
            return true;

        return false;
    }

    private static boolean IsTempoChange(MidiEvent event)
    {
        if(!(event.getMessage() instanceof MetaMessage))
            return false;

        MetaMessage mmsg = (MetaMessage)event.getMessage();

        if(mmsg.getType() == SET_TEMPO)
            return true;

        return false;
    }

    private static boolean IsTimeSignature(MidiEvent event)
    {
        if(!(event.getMessage() instanceof MetaMessage))
            return false;

        MetaMessage mmsg = (MetaMessage)event.getMessage();

        if(mmsg.getType() == TIME_SIGNATURE)
            return true;

        return false;
    }

    private static boolean IsNoteOn(MidiEvent event)
    {
        if(!(event.getMessage() instanceof ShortMessage))
            return false;

        ShortMessage smsg = (ShortMessage)event.getMessage();

        if(smsg.getCommand() == NOTE_ON)
            return true;

        return false;
    }
}