package com.notebot.mod;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Victor on 7/15/2015.
 */
@SideOnly(Side.CLIENT)
public class Note
{
    private int knownpitch;
    private int pitch;
    private boolean validated;
    private BlockPos pos;

    public Note(int pitch, BlockPos pos)
    {
        this.knownpitch = -1;
        this.pitch = pitch;
        this.pos = pos;
    }

    public void SetKnownPitch(int pitch)
    {
        this.knownpitch = pitch;
    }

    public int GetKnownPitch()
    {
        return knownpitch;
    }

    public int GetPitch()
    {
        return pitch;
    }

    public BlockPos GetPosition()
    {
        return pos;
    }

    public boolean IsValidated()
    {
        return validated;
    }

    public void SetValidated(boolean validated)
    {
        this.validated = validated;
    }
}
