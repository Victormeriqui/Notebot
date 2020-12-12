package com.notebot.mod;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

/**
 * Created by Victor on 7/14/2015.
 * Ported by humboldt123 on 6/27/2020
 */


@SideOnly(Side.CLIENT)
public class Instrument
{
    public static boolean Tunning = false;
    public static ArrayList<Note> ToTune;
    public static boolean CurrentTuneComplete = false;

    public static boolean Discovering = false;
    public static ArrayList<Note> ToDiscover;
    public static boolean CurrentDiscoveryComplete = false;

    public static int CurrentInstrument = -1;

    private ArrayList<Note> notes;
    private int id;

    public Instrument(int id)
    {
        notes = new ArrayList<Note>();
        this.id = id;
    }

    public int GetID()
    {
        return id;
    }

    public void TuneInstrument()
    {
        ToTune = new ArrayList<Note>();

        for(Note note : notes)
        {
            if(note.GetKnownPitch() != note.GetPitch())
                ToTune.add(note);
        }

        Tunning = true;
        CurrentInstrument = id;
    }

    public void DiscoverInstrument()
    {
        ToDiscover = new ArrayList<Note>();
        for(Note note : notes)
        {
            note.SetValidated(false);
            ToDiscover.add(note);
        }

        Discovering = true;
        CurrentInstrument = id;
    }

    public void AddNote(int pitch, BlockPos pos)
    {
        notes.add(new Note(pitch, pos));
    }

    public BlockPos GetNotePosition(int pitch)
    {
        for(Note note : notes)
        {
            if(note.GetPitch() == pitch)
                return note.GetPosition();
        }

        return null;
    }

    public void SetNoteKnownPitch(int pitch, int knownpitch)
    {
        for(Note note : notes)
        {
            if(note.GetPitch() == pitch)
                note.SetKnownPitch(knownpitch);
        }
    }

    public void SetNoteValidation(int pitch, boolean val)
    {
        for(Note note : notes)
        {
            if(note.GetPitch() == pitch)
                note.SetValidated(val);
        }
    }

    public boolean IsNoteValidated(int pitch)
    {
        for(Note note : notes)
        {
            if(note.GetPitch() == pitch)
                return note.IsValidated();
        }

        return false;
    }

    public boolean IsNoteDiscovered(int pitch)
    {
        for(Note note : notes)
        {
            if(note.GetPitch() == pitch)
                return note.GetKnownPitch() != -1;
        }

        return false;
    }

    public void OverrideValidation()
    {
        for(Note note : notes)
        {
            note.SetKnownPitch(note.GetPitch());
            note.SetValidated(true);
        }
    }

    public void PlayNote(int pitch)
    {

    }

}
