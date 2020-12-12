package com.notebot.mod;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Victor on 7/15/2015.
 * Ported by humboldt123 on 6/27/2020
 */

@SideOnly(Side.CLIENT)
class MusicNote
{
    int note;
    int time;

    public MusicNote(int note, int time)
    {
        this.note = note;
        this.time = time;
    }

    public int GetNote()
    {
        return note;
    }

    public int GetTime()
    {
        return time;
    }
}

@SideOnly(Side.CLIENT)
class MusicChannel
{
    int id;
    int instrument;
    int originalinstrument;
    ArrayList<MusicNote> notes;

    public MusicChannel(int id, int instrument, int originalinstrument)
    {
        this.id = id;
        this.instrument = instrument;
        this.originalinstrument = originalinstrument;

        notes = new ArrayList<MusicNote>();
    }

    public int GetInstrument()
    {
        return instrument;
    }

    public void AddNote(MusicNote note)
    {
        notes.add(note);
    }

    public void RemoveNextNote()
    {
        if(notes.size() > 0)
            notes.remove(0);
    }

    public boolean IsDone()
    {
        return notes.size() == 0;
    }

    public ArrayList<MusicNote> GetNextNotes()
    {
        ArrayList<MusicNote> ret = new ArrayList<MusicNote>();
        if(notes.size() > 0)
        {
            int time = -1;
            int i = 0;

            MusicNote note = notes.get(0);
            int notes_c = notes.size();
            do
            {
                ret.add(note);
                time = note.GetTime();

                i++;
                if(i >= notes.size())
                    break;

                note =  notes.get(i);
            }
            while(note.GetTime() == time);
        }

        return ret;
    }

    public ArrayList<MusicNote> GetNotes()
    {
        return notes;
    }
}

@SideOnly(Side.CLIENT)
class Music
{
    String name;
    long length;
    ArrayList<MusicChannel> channels;
    float progress;

    public Music(String filename)
    {
        channels = new ArrayList<MusicChannel>();
        progress = 0;

        String content = "";

        try
        {
            content = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        JsonParser jsonparser = new JsonParser();
        JsonObject mainjson = (JsonObject)jsonparser.parse(content);

        name = mainjson.get("Name").getAsString();
        length = mainjson.get("Length").getAsLong();

        for(Map.Entry<String, JsonElement> channelel : mainjson.getAsJsonObject("Data").entrySet())
        {
            JsonObject channeljson = channelel.getValue().getAsJsonObject();

            int instrument = channeljson.get("Instrument").getAsInt();
            int originalinstrument = channeljson.get("OriginalInstrument").getAsInt();

            MusicChannel channel = new MusicChannel(Integer.parseInt(channelel.getKey()), instrument, originalinstrument);

            JsonArray notesjson = channeljson.getAsJsonArray("Notes");

            for(int i = 0; i < notesjson.size(); i++)
            {
                JsonElement noteel = notesjson.get(i);

                int note = noteel.getAsJsonObject().get("Note").getAsInt();
                int time = noteel.getAsJsonObject().get("Tick").getAsInt();

                channel.AddNote(new MusicNote(note, time));
            }

            channels.add(channel);
        }
    }

    public float GetProgress()
    {
        return progress;
    }

    public void CalculateProgress(long starttime, long currenttime)
    {
        progress = Util.LongInterpolate(currenttime, starttime, starttime + length, 0.0f, 1.0f);
    }

    public ArrayList<MusicChannel> GetChannels()
    {
        return channels;
    }

    public String GetName()
    {
        return name;
    }

    public long GetLength()
    {
        return length;
    }
}

/*
class MusicNote
{
    private int note;
    private int tick;
    private int instrument;

    public MusicNote(int note, int tick, int instrument)
    {
        this.note = note;
        this.tick = tick;
        this.instrument = instrument;
    }

    public int GetNote()
    {
        return note;
    }

    public int GetInstrument()
    {
        return instrument;
    }

    public int GetTick()
    {
        return tick;
    }
}

class MusicTrack
{
    private int instrument;
    private ArrayList<MusicNote> notes;

    public MusicTrack(int instrument)
    {
        this.instrument = instrument;
        notes = new ArrayList<MusicNote>();
    }

    public int GetInstrument()
    {
        return instrument;
    }

    public void AddNote(MusicNote note)
    {
        notes.add(note);
    }

    public void RemoveNextNote()
    {
        if(notes.size() > 0)
            notes.remove(0);
    }

    public boolean IsDone()
    {
        return notes.size() == 0;
    }

    public ArrayList<MusicNote> GetNextNotes()
    {
        ArrayList<MusicNote> ret = new ArrayList<MusicNote>();
        if(notes.size() > 0)
        {
            int tick = -1;
            int i = 0;

            MusicNote note = notes.get(0);
            int notes_c = notes.size();
            do
            {

                ret.add(note);
                tick = note.GetTick();

                i++;
                if(i >= notes.size())
                    break;

                note =  notes.get(i);
            }
            while(note.GetTick() == tick);
        }

        return ret;
    }

    public ArrayList<MusicNote> GetNotes()
    {
        return notes;
    }
}

public class Music
{
    private String name;
    private int tracks_c;

    private ArrayList<MusicTrack> tracks;

    public Music(String filename)
    {
        tracks = new ArrayList<MusicTrack>();
        String content = "";

        try
        {
            content = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        JsonParser parser = new JsonParser();
        JsonObject obj = (JsonObject)parser.parse(content);

        name = obj.get("Name").getAsString();

        for(Map.Entry<String, JsonElement> track : obj.getAsJsonObject("Data").entrySet())
        {
            JsonObject trackobj = track.getValue().getAsJsonObject();
            int instrument = trackobj.get("Instrument").getAsInt();

            MusicTrack mtrack = new MusicTrack(instrument);
            JsonArray tracknotes = trackobj.getAsJsonArray("Notes");

            for(int i = 0; i < tracknotes.size(); i++)
            {
                JsonElement noteel = tracknotes.get(i);

                int notepitch = noteel.getAsJsonObject().get("Note").getAsInt();
                int notetick = noteel.getAsJsonObject().get("Tick").getAsInt();
                mtrack.AddNote(new MusicNote(notepitch, notetick, instrument));
                System.out.println(notepitch + " " + notetick);
            }

            tracks.add(mtrack);
        }
        System.out.println("Done");
    }

    public ArrayList<MusicTrack> GetTracks()
    {
        return tracks;
    }

    public String GetName()
    {
        return name;
    }

}
*/