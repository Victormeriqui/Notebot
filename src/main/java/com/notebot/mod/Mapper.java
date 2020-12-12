package com.notebot.mod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.vecmath.Vector3d;
import java.awt.*;

/**
 * Created by Victor on 7/14/2015.
 * Ported by humboldt123 on 6/27/2020
 */

/**
 * "A Note"
 * Though the instruments are assigned wrong; I could care less.
 * The end result is something that is audible and not a pain to listen to.
 * It works. I am keeping the code like this until I can learn how to fix it.
 */
@SideOnly(Side.CLIENT)
public class Mapper
{
    private static Instrument inst1, inst2, inst3, inst4, inst5;

    public static void Unmap()
    {
        inst1 = null;
        inst2 = null;
        inst3 = null;
        inst4 = null;
        inst5 = null;
    }

    public static Instrument GetInstrument(int inst)
    {
        switch(inst)
        {
            case 1:
                return inst1;
            case 2:
                return inst2;
            case 3:
                return inst3;
            case 4:
                return inst4;
            case 5:
                return inst5;
        }

        return null;
    }


    public static Color GetInstrumentColor(int inst)
    {
        switch(inst)
        {
            case 1:
                return new Color(188, 152, 98);
            case 2:
                return new Color(127, 127, 127);
            case 3:
                return new Color(255, 230, 150);
            case 4:
                return new Color(255, 255, 255);
            case 5:
                return new Color(108, 164, 93);
        }

        return null;
    }

    private static BlockPos GetPosition(BlockPos center, int forward, int left)
    {
        double cx = center.getX();
        double cy = center.getY();
        double cz = center.getZ();

        EnumFacing facing = Minecraft.getMinecraft().player.getHorizontalFacing();

        Vector3d forwardv = new Vector3d(0, 0 ,0);
        Vector3d leftv = new Vector3d(0, 0 ,0);

        switch(facing)
        {
            case NORTH:
                forwardv.z = -1;
                leftv.x = -1;
                break;
            case EAST:
                forwardv.x = 1;
                leftv.z = -1;
                break;
            case SOUTH:
                forwardv.z = 1;
                leftv.x = 1;
                break;
            case WEST:
                forwardv.x = -1;
                leftv.z = 1;
                break;
        }

        double forwardx = forwardv.x*forward;
        double forwardz = forwardv.z*forward;
        double leftx = leftv.x*left;
        double leftz = leftv.z*left;

        return new BlockPos(cx + forwardx + leftx, cy, cz + forwardz + leftz);
    }

    public static void MapInstrument1()
    {
        EntityPlayerSP ply = Minecraft.getMinecraft().player;
        BlockPos center = new BlockPos(ply.posX, (int)ply.posY, ply.posZ);

        inst1 = new Instrument(1);
        int curpitch = 0;

        inst1.AddNote(curpitch++, GetPosition(center, 2, 2).add(0, 4, 0));
        inst1.AddNote(curpitch++, GetPosition(center, 1, 2).add(0, 4, 0));
        inst1.AddNote(curpitch++, GetPosition(center, 0, 2).add(0, 4, 0));
        inst1.AddNote(curpitch++, GetPosition(center, -1, 2).add(0, 4, 0));
        inst1.AddNote(curpitch++, GetPosition(center, -2, 2).add(0, 4, 0));

        inst1.AddNote(curpitch++, GetPosition(center, 2, 1).add(0, 4, 0));
        inst1.AddNote(curpitch++, GetPosition(center, 1, 1).add(0, 4, 0));
        inst1.AddNote(curpitch++, GetPosition(center, 0, 1).add(0, 4, 0));
        inst1.AddNote(curpitch++, GetPosition(center, -1, 1).add(0, 4, 0));
        inst1.AddNote(curpitch++, GetPosition(center, -2, 1).add(0, 4, 0));

        inst1.AddNote(curpitch++, GetPosition(center, 2, 0).add(0, 4, 0));
        inst1.AddNote(curpitch++, GetPosition(center, 1, 0).add(0, 4, 0));
        inst1.AddNote(curpitch++, GetPosition(center, 0, 0).add(0, 4, 0));
        inst1.AddNote(curpitch++, GetPosition(center, -1, 0).add(0, 4, 0));
        inst1.AddNote(curpitch++, GetPosition(center, -2, 0).add(0, 4, 0));

        inst1.AddNote(curpitch++, GetPosition(center, 2, -1).add(0, 4, 0));
        inst1.AddNote(curpitch++, GetPosition(center, 1, -1).add(0, 4, 0));
        inst1.AddNote(curpitch++, GetPosition(center, 0, -1).add(0, 4, 0));
        inst1.AddNote(curpitch++, GetPosition(center, -1, -1).add(0, 4, 0));
        inst1.AddNote(curpitch++, GetPosition(center, -2, -1).add(0, 4, 0));

        inst1.AddNote(curpitch++, GetPosition(center, 2, -2).add(0, 4, 0));
        inst1.AddNote(curpitch++, GetPosition(center, 1, -2).add(0, 4, 0));
        inst1.AddNote(curpitch++, GetPosition(center, 0, -2).add(0, 4, 0));
        inst1.AddNote(curpitch++, GetPosition(center, -1, -2).add(0, 4, 0));
        inst1.AddNote(curpitch++, GetPosition(center, -2, -2).add(0, 4, 0));
    }

    public static void MapInstrument2()
    {
        EntityPlayerSP ply = Minecraft.getMinecraft().player;
        BlockPos center = new BlockPos(ply.posX, (int)ply.posY, ply.posZ);

        inst2 = new Instrument(2);
        int curpitch = 0;

        // this idiot skips middle row; source in crystalpvp.cc the middle row is the same note wtf!?!


        inst2.AddNote(curpitch++, GetPosition(center, 2, 2).add(0, -1, 0));
        inst2.AddNote(curpitch++, GetPosition(center, 1, 2).add(0, -1, 0));
        inst2.AddNote(curpitch++, GetPosition(center, -1, 2).add(0, -1, 0));
        inst2.AddNote(curpitch++, GetPosition(center, -2, 2).add(0, -1, 0));

        inst2.AddNote(curpitch++, GetPosition(center, 2, 1).add(0, -1, 0));
        inst2.AddNote(curpitch++, GetPosition(center, 1, 1).add(0, -1, 0));
        inst2.AddNote(curpitch++, GetPosition(center, -1, 1).add(0, -1, 0));
        inst2.AddNote(curpitch++, GetPosition(center, -2, 1).add(0, -1, 0));

        inst2.AddNote(curpitch++, GetPosition(center, 2, 0).add(0, -1, 0));
        inst2.AddNote(curpitch++, GetPosition(center, 1, 0).add(0, -1, 0));
        inst2.AddNote(curpitch++, GetPosition(center, -1, 0).add(0, -1, 0));
        inst2.AddNote(curpitch++, GetPosition(center, -2, 0).add(0, -1, 0));

        inst2.AddNote(curpitch++, GetPosition(center, 2, -1).add(0, -1, 0));
        inst2.AddNote(curpitch++, GetPosition(center, 1, -1).add(0, -1, 0));
        inst2.AddNote(curpitch++, GetPosition(center, -1, -1).add(0, -1, 0));
        inst2.AddNote(curpitch++, GetPosition(center, -2, -1).add(0, -1, 0));

        inst2.AddNote(curpitch++, GetPosition(center, 2, -2).add(0, -1, 0));
        inst2.AddNote(curpitch++, GetPosition(center, 1, -2).add(0, -1, 0));
        inst2.AddNote(curpitch++, GetPosition(center, -1, -2).add(0, -1, 0));
        inst2.AddNote(curpitch++, GetPosition(center, -2, -2).add(0, -1, 0));
    }

    public static void MapInstrument3()
    {
        EntityPlayerSP ply = Minecraft.getMinecraft().player;
        BlockPos center = new BlockPos(ply.posX, (int)ply.posY, ply.posZ);

        inst3 = new Instrument(3);
        int curpitch = 0;

        inst3.AddNote(curpitch++, GetPosition(center, 2, 4).add(0, -1, 0));
        inst3.AddNote(curpitch++, GetPosition(center, 1, 4).add(0, -1, 0));
        // i hate crystalpvp.cc FREE SPACE
        inst3.AddNote(curpitch++, GetPosition(center, -1, 4).add(0, -1, 0));
        inst3.AddNote(curpitch++, GetPosition(center, -2, 4).add(0, -1, 0));
        inst3.AddNote(curpitch++, GetPosition(center, -3, 4));
        inst3.AddNote(curpitch++, GetPosition(center, -4, 3));

        inst3.AddNote(curpitch++, GetPosition(center, 4, 2).add(0, -1, 0));
        inst3.AddNote(curpitch++, GetPosition(center, -4, 2).add(0, -1, 0));
        inst3.AddNote(curpitch++, GetPosition(center, 4, 1).add(0, -1, 0));
        inst3.AddNote(curpitch++, GetPosition(center, -4, 1).add(0, -1, 0));
        inst3.AddNote(curpitch++, GetPosition(center, 4, 0).add(0, -1, 0));
        inst3.AddNote(curpitch++, GetPosition(center, -4, 0).add(0, -1, 0)); //11

        inst3.AddNote(curpitch++, GetPosition(center, -4, -1).add(0, -1, 0)); //13 wtf!??!
        inst3.AddNote(curpitch++, GetPosition(center, 4, -2).add(0, -1, 0)); //14
        inst3.AddNote(curpitch++, GetPosition(center, -4, -2).add(0, -1, 0)); //15

        inst3.AddNote(curpitch++, GetPosition(center, 4, -3)); //16
        inst3.AddNote(curpitch++, GetPosition(center, -4, -3)); //17
        inst3.AddNote(curpitch++, GetPosition(center, 3, -4)); //18

        inst3.AddNote(curpitch++, GetPosition(center, 2, -4).add(0, -1, 0)); //19
        inst3.AddNote(curpitch++, GetPosition(center, 1, -4).add(0, -1, 0)); //20
        //FREE SPACE
        inst3.AddNote(curpitch++, GetPosition(center, -1, -4).add(0, -1, 0)); //22
        inst3.AddNote(curpitch++, GetPosition(center, -2, -4).add(0, -1, 0)); //23
        inst3.AddNote(curpitch++, GetPosition(center, -3, -4)); //24





    }

    public static void MapInstrument4()
    {
        EntityPlayerSP ply = Minecraft.getMinecraft().player;
        BlockPos center = new BlockPos(ply.posX, (int)ply.posY, ply.posZ);

        inst4 = new Instrument(4);
        int curpitch = 0;

        inst4.AddNote(curpitch++, GetPosition(center, 2, 4).add(0, 3, 0));
        inst4.AddNote(curpitch++, GetPosition(center, 1, 4).add(0, 3, 0));
        inst4.AddNote(curpitch++, GetPosition(center, 0, 4).add(0, 3, 0));
        inst4.AddNote(curpitch++, GetPosition(center, -1, 4).add(0, 3, 0));
        inst4.AddNote(curpitch++, GetPosition(center, -4, 4).add(0, 3, 0));

        // i hate myself

        inst4.AddNote(curpitch++, GetPosition(center, 4, 3).add(0, 3, 0)); //5
        inst4.AddNote(curpitch++, GetPosition(center, -4, 3).add(0, 3, 0)); //6

        inst4.AddNote(curpitch++, GetPosition(center, 4, 2).add(0, 3, 0));
        inst4.AddNote(curpitch++, GetPosition(center, -4, 2).add(0, 3, 0));

        inst4.AddNote(curpitch++, GetPosition(center, 4, 1).add(0, 3, 0));
        inst4.AddNote(curpitch++, GetPosition(center, -4, 1).add(0, 3, 0));

        inst4.AddNote(curpitch++, GetPosition(center, 4, 0).add(0, 3, 0));
        inst4.AddNote(curpitch++, GetPosition(center, -4, 0).add(0, 3, 0));

        // 13: does not apply | Instead its stupid | inst1.AddNote(curpitch++, GetPosition(center, 4, -1).add(0, 3, 0));
        inst4.AddNote(curpitch++, GetPosition(center, -4, -1).add(0, 3, 0)); // 14

        inst4.AddNote(curpitch++, GetPosition(center, 2, -4).add(0, 3, 0)); // 15
        inst4.AddNote(curpitch++, GetPosition(center, 1, -4).add(0, 3, 0));
        inst4.AddNote(curpitch++, GetPosition(center, 0, -4).add(0, 3, 0));
        inst4.AddNote(curpitch++, GetPosition(center, -1, -4).add(0, 3, 0));
        inst4.AddNote(curpitch++, GetPosition(center, -2, -4).add(0, 3, 0));

    }


    public static void MapInstrument5()
    {
        EntityPlayerSP ply = Minecraft.getMinecraft().player;
        BlockPos center = new BlockPos(ply.posX, (int)ply.posY, ply.posZ);

        inst5 = new Instrument(5);
        int curpitch = 0;

        inst5.AddNote(curpitch++, GetPosition(center, 3, 3)); //1
        inst5.AddNote(curpitch++, GetPosition(center, 2, 3));
        inst5.AddNote(curpitch++, GetPosition(center, 1, 3));
        // fuck this (4)
        inst5.AddNote(curpitch++, GetPosition(center, -1, 3));
        inst5.AddNote(curpitch++, GetPosition(center, -2, 3));
        inst5.AddNote(curpitch++, GetPosition(center, -3, 3)); // 7

        // Why can't this be consistent. The notes should go in a circle or a line, not whatever this is!

        inst5.AddNote(curpitch++, GetPosition(center, 3, 2)); // 8
        inst5.AddNote(curpitch++, GetPosition(center, -3, 2)); //9
        // Whats wrong with 10?
        inst5.AddNote(curpitch++, GetPosition(center, 3, 1)); // 11
        inst5.AddNote(curpitch++, GetPosition(center, 3, 0)); // 12
        inst5.AddNote(curpitch++, GetPosition(center, -3, 0)); // 13
        inst5.AddNote(curpitch++, GetPosition(center, 3, -1)); // 14
        inst5.AddNote(curpitch++, GetPosition(center, -3, -1)); // 15
        // Whats wrong with 16?
        inst5.AddNote(curpitch++, GetPosition(center, 3, -2)); // 17
        inst5.AddNote(curpitch++, GetPosition(center, 3, -3)); // 18 to the side
        inst5.AddNote(curpitch++, GetPosition(center, 2, -3)); // 19 now back
        inst5.AddNote(curpitch++, GetPosition(center, 1, -3));
        //21 gets skipped by 0 cha-cha real smooth
        inst5.AddNote(curpitch++, GetPosition(center, -1, -3)); //22
        inst5.AddNote(curpitch++, GetPosition(center, -2, -3));
        inst5.AddNote(curpitch++, GetPosition(center, -3, -3));



    }
}
