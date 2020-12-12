package com.notebot.mod;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Victor on 8/1/2015.
 * Ported by humboldt123 on 6/27/2020
 */
@SideOnly(Side.CLIENT)
public class Player implements Runnable
{
    public static volatile boolean Running;

    public static volatile boolean Playing;
    public static volatile boolean Started;
    public static volatile Music PlayingMusic;

    public static volatile String LastNote;
    public static volatile Color LastNoteColor;

    private static long starttime;

    @Override
    public void run()
    {
        while(Running)
        {
            if(Playing)
            {
                if(PlayingMusic == null)
                    continue;

                if(!Started)
                {
                    starttime = System.currentTimeMillis();
                    Started = true;
                }

                PlayingMusic.CalculateProgress(starttime, System.currentTimeMillis());

                boolean finished = true;
                for(MusicChannel channel : PlayingMusic.GetChannels())
                {
                    if (channel.IsDone())
                        continue;

                    finished = false;
                    ArrayList<MusicNote> notes = channel.GetNextNotes();

                    long time = System.currentTimeMillis() - starttime;

                    if (time >= notes.get(0).GetTime())
                    {
                        int notes_c = notes.size();
                        for (int i = 0; i < notes_c; i++)
                        {
                            MusicNote note = notes.get(i);

                            int inst = channel.GetInstrument();

                            if (inst == 0)
                            {
                                channel.RemoveNextNote();
                                continue;
                            }

                            Instrument instrument = Mapper.GetInstrument(inst);

                            if (instrument == null)
                            {
                                channel.RemoveNextNote();
                                continue;
                            }

                            int pitch = note.GetNote();

                            BlockPos pos = instrument.GetNotePosition(pitch);

                            if(pos != null)
                                Util.LeftClick(pos);

                            channel.RemoveNextNote();
                            LastNote = Util.GetRealNote(pitch);
                            LastNoteColor = Mapper.GetInstrumentColor(inst);
                        }
                    }
                }

                if(finished)
                {
                    Started = false;
                    Playing = false;
                }

            }

        }
    }

}
