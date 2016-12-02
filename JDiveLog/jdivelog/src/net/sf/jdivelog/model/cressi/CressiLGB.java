package net.sf.jdivelog.model.cressi;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import net.sf.jdivelog.gui.MainWindow;
import net.sf.jdivelog.gui.commands.CommandAddDives;
import net.sf.jdivelog.gui.commands.CommandManager;
import net.sf.jdivelog.model.DiveSite;
import net.sf.jdivelog.model.Equipment;
import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.model.JDiveLog;
import net.sf.jdivelog.model.Tank;
import net.sf.jdivelog.model.udcf.Delta;
import net.sf.jdivelog.model.udcf.Dive;
import net.sf.jdivelog.model.udcf.Gas;

/**
 * Loader of native Cressi LGB files
 *
 * Cress LGB files loader. Tested with PCLogBook version 6.0
 *
 * Special thanks for algorithms of pressure calculation to
 * Rainer Mohr (http://www.divelogs.de/) and Michal Zuberek.
 *
 * Based on the PHP code of Rainer Mohr.
 *
 * @author Uri Kogan urkheh@gmail.com
 *
 */
public class CressiLGB {

    /** file being loaded */
    private File loadedFile = null;

    /** dive information signature */
    private final static byte[] SIGN_DIVE_INFO =
        new byte[] { 'C', 'M', 0 };

    /** dive start signature */
    private final static byte[] SIGN_DIVE_START =
        new byte[] { 'D', 'D', 0, 0x18 };

    /** dives counter field signature */
    private final static byte[] SIGN_DIVE_COUNT =
        new byte[] { 'D', 'D', 'B', '\b' };

    /** start of dive samples signature */
    private final static byte[] SIGN_DIVE_DETAILS =
        new byte[] { 'L', '\1' };

    /** start of first tank information signature */
    private final static byte[] SIGN_TANK0_DETAILS =
        new byte[] { 'T', '\0' };

    /** start of second tank information signature */
    private final static byte[] SIGN_TANK1_DETAILS =
        new byte[] { 'T', '\1' };

    public CressiLGB(File inputFile)
    {
        /* remember the file to load */
        this.loadedFile = inputFile;
    }

    private boolean SkipString(RandomAccessFile rd, byte[] arrMark)
    throws IOException
    {
        /* single character read from file */
        int ch = 0;

        /* current cursor position in input string */
        int pos = 0;

        while (pos < arrMark.length && ch >= 0)
        {
            ch = rd.readUnsignedByte() & 0xff;
            if (ch == arrMark[pos])
            {
                pos++;
            }
            else
            {
                pos = 0;
            }
        }
        if (ch >= 0)
        {
            return true;
        }

        return false;
    }

    public void Load(MainWindow wnd, JDiveLog wholeLog)
    throws IOException
    {
        /* is current block found successfully? */
        boolean IsFound = false;

        /* number of dives in the file */
        int recordCount = 0;

        /* size of single dive record */
        int diveBlockSize = 0;

        /* number of samples for current dive */
        int samplesCount = 0;

        /* current dive site */
        DiveSite diveSite = null;

        /* offset in the file modulo 8 */
        long offsetMod = 0;

        /* offset of currently analyzed dive in the file */
        long lDiveOffset = 0;

        /* single depth read from the file */
        int nDepthSample = 0;

        /* list of added dives */
        Collection<JDive> dives = null;

        /* single created dive */
        JDive dive = null;

        /* file being read */
        RandomAccessFile fle = new RandomAccessFile(loadedFile, "r");

        /* sampling delta value */
        Delta delta = null;

        /* date of the dive */
        Calendar dte = null;

        /* name of the dive set used */
        String diveSetName = null;

        /* contents of the dive set */
        String diveSetContents = null;

        /* buddy read from LGB file */
        String StrBuddy = null;

        /* weather read from LGB file */
        String StrWeather = null;

        /* visibility read from LGB file */
        String StrVisibility = null;

        /* condition read from LGB file */
        String StrCondition = null;

        /* guide for this dive as read from LGB */
        String StrGuide = null;

        /* locate and read number of dives in this file */
        IsFound = SkipString(fle, SIGN_DIVE_COUNT);
        if (!IsFound)
        {
            throw new IllegalArgumentException("Cannot find 'DDB' signature");
        }
        recordCount = fle.readUnsignedShort();

        /* locate the dive */
        IsFound = SkipString(fle, SIGN_DIVE_START);
        if (!IsFound)
        {
            throw new IllegalArgumentException("Cannot find 'DD' signature");
        }

        /* return to the beginning of the first dive */
        fle.seek(fle.getFilePointer() - SIGN_DIVE_START.length);

        /* create array of dives */
        dives = new ArrayList<JDive>(recordCount);

        /* read data of all the dives */
        try
        {
            for (int i = 0; i < recordCount; i++)
            {
                /* remember the offset of the beginning of the dive */
                lDiveOffset = fle.getFilePointer();

                /* create new dive */
                dive = new JDive();
                dive.setDive(new Dive());

                fle.skipBytes(SIGN_DIVE_START.length);
                diveBlockSize = fle.readUnsignedShort();
                fle.skipBytes(10);
                samplesCount = fle.readUnsignedShort();

                /* try to find 'T\0' at offsets that are modulo of 8 */
                offsetMod = fle.getFilePointer() % 8;
                if (offsetMod != 0)
                {
                    fle.seek(fle.getFilePointer() + (8 - offsetMod));
                }

                /* save first tank information */
                IsFound = SkipString(fle, SIGN_TANK0_DETAILS);
                if (!IsFound)
                {
                    throw new IllegalArgumentException(
                        "Unable to find tank 1 data");
                }
                addTank(fle, dive);

                /* save second tank information */
                IsFound = SkipString(fle, SIGN_TANK1_DETAILS);
                if (!IsFound)
                {
                    throw new IllegalArgumentException(
                        "Unable to find tank 2 data");
                }
                addTank(fle, dive);

                /* locate the dive information */
                IsFound = SkipString(fle, SIGN_DIVE_DETAILS);
                if (!IsFound)
                {
                    throw new IllegalArgumentException(
                        "Cannot find samples signature");
                }

                /* date of the dive */
                dte = Calendar.getInstance();
                dte.set(fle.readUnsignedShort(),
                    fle.readByte() - 1,
                    fle.readByte(),
                    fle.readUnsignedByte(),
                    fle.readUnsignedByte());
                dive.getDive().setDate(dte.getTime());
                dive.setDate(dte.getTime());

                /* time of the dive: start */
                dive.getDive().setTime(
                    Integer.toString(dte.get(Calendar.HOUR)),
                    Integer.toString(dte.get(Calendar.MINUTE)));

                /* skip exit time */
                fle.readUnsignedByte();
                fle.readUnsignedByte();

                /* set dive duration */
                dive.setDuration(new Double(fle.readUnsignedShort()));

                /* skip 2 unknown bytes */
                fle.readUnsignedByte();
                fle.readUnsignedByte();

                /* read safety factor */
                dive.setComment("Safety factor: " + fle.readUnsignedByte());

                /* read OLI */
                dive.setComment(dive.getComment() + "\r\nOLI: " +
                    fle.readUnsignedByte());

                /* read PGT */
                dive.setComment(dive.getComment() + "\r\nPGT: " +
                    fle.readUnsignedByte());

                /* read sampling period */
                delta = new Delta();
                delta.setValue(((double)fle.readUnsignedByte()) / 60);
                dive.getDive().addSample(delta);
                dive.setComment(dive.getComment() + "\r\nSampling period: " +
                    delta.getValue());

                /* read maximal depth (is this the same as AMV?) */
                dive.setDepth(((double)fle.readUnsignedShort()) / 10);

                /* read average depth */
                dive.setAverageDepth(
                    ((double)fle.readUnsignedShort()) / 10);

                /* dive temperature */
                dive.setTemperature(
                    ((double)fle.readUnsignedShort()) / 10);

                /* skip 8 unknown bytes */
                fle.readUnsignedByte();
                fle.readUnsignedByte();
                fle.readUnsignedByte();
                fle.readUnsignedByte();
                fle.readUnsignedByte();
                fle.readUnsignedByte();
                fle.readUnsignedByte();
                fle.readUnsignedByte();

                /* save all the samples */
                for (int scnt = 0; scnt < samplesCount; scnt++)
                {
                    nDepthSample = fle.readInt();
                    nDepthSample >>= 16;
                    dive.getDive().addDepth(
                        Double.toString(((double)nDepthSample) / 10));
                }

                /* read dive information which starts with "CM\0" string */
                IsFound = SkipString(fle, SIGN_DIVE_INFO);
                if (!IsFound)
                {
                    throw new IllegalArgumentException(
                        "Cannot find dive information start signature");
                }
                /* skip the length of string block and rest of the bytes */
                fle.skipBytes(8 - SIGN_DIVE_INFO.length);

                diveSite = getSite(wholeLog, readNextString(fle),
                        readNextString(fle));
                dive.setDiveSiteId(diveSite.getPrivateId());

                /* read and save buddy and guide */
                StrBuddy = readNextString(fle);
                StrGuide = readNextString(fle);
                dive.setBuddy(StrBuddy);
                if (!StrGuide.equals(""))
                {
                    if (!StrBuddy.equals(""))
                    {
                        dive.setBuddy(dive.getBuddy() + ", ");
                    }
                    dive.setBuddy(dive.getBuddy() + "Guide: " + StrGuide);
                }

                /* save weather conditions */
                StrCondition = readNextString(fle);
                StrWeather = readNextString(fle);
                StrVisibility = readNextString(fle);
                dive.setVisibility(StrVisibility);
                if (!StrCondition.equals(""))
                {
                    if (!dive.getVisibility().equals(""))
                    {
                        dive.setVisibility(dive.getVisibility() + ", ");
                    }
                    dive.setVisibility(dive.getVisibility() + "Condition: " +
                        StrCondition);
                }
                if (!StrWeather.equals(""))
                {
                    if (!dive.getVisibility().equals(""))
                    {
                        dive.setVisibility(dive.getVisibility() + ", ");
                    }
                    dive.setVisibility(dive.getVisibility() + "Weather: " +
                        StrWeather);
                }

                diveSetName = readNextString(fle);
                /* skip one byte after gear set name */
                fle.readUnsignedByte();
                diveSetContents = readNextString(fle);
                /* save dive set name and contents if at least one of them  */
                /* is not empty                                             */
                if (!diveSetName.equals("") || !diveSetContents.equals(""))
                {
                    dive.getEquipment().setComment(
                        diveSetName + "\r\n" + diveSetContents);
                }

                dives.add(dive);

                /* jump to the next dive */
                fle.seek(lDiveOffset + diveBlockSize);
            }
        }
        catch (EOFException e)
        {
            /* do nothing on end of file */
        }
        finally
        {
            try { fle.close(); }
            catch (Exception e) { }
        }

        /* add all the created dives */
        CommandAddDives cmd = new CommandAddDives(wnd, dives);
        CommandManager.getInstance().execute(cmd);

    }

    /**
     * add tank to the dive
     *
     * @param fle file with tank data, file pointer should be set at the byte
     * right after the "T\x" signature
     * @param dive dive to add this tank to
     * @throws IOException on reading errors
     */
    private void addTank(RandomAccessFile fle, JDive dive) throws IOException
    {
        /* volume of this tank */
        double volume = 0;

        /* oxygen fraction in the mix */
        double oxygen = 0;

        /* nitrogen fraction in the mix */
        double nitrogen = 0;

        /* state of this tank: enabled or disabled */
        int state = 0;

        /* buffer with pressures read from file */
        byte[] bufPres = new byte[4];

        /* pressure reading from the file */
        int[] pressure = new int[4];

        /* calculated starting pressure */
        int startPressure = 0;

        /* calculated end pressure */
        int endPressure = 0;

        /* exponent of the pressure */
        int exponent = 0;

        fle.skipBytes(2);

        volume = fle.readUnsignedShort();
        volume /= 10;

        oxygen = fle.readUnsignedByte();

        nitrogen = fle.readUnsignedByte();

        state = fle.readUnsignedByte();

        if (state != 0x80)
        {
            return;
        }

        fle.skipBytes(11);
        fle.read(bufPres);
        if (bufPres[0] == 0 &&
            bufPres[1] == 0 &&
            bufPres[2] == 0 &&
            bufPres[3] == 0)
        {
            startPressure = 0;
        }
        else
        {
            for (int i = 0; i < bufPres.length; i++)
            {
                pressure[i] = bufPres[i];
                pressure[i] &= 0xff;
            }
            exponent = pressure[2] >> 4;
            startPressure = pressure[2] & 0x0f;
            startPressure <<= 8;
            startPressure += pressure[1];
            startPressure >>= (12 - (exponent + 1));
            startPressure += Math.pow(2.0, exponent + 1);
        }

        fle.skipBytes(20);
        fle.read(bufPres);
        if (bufPres[0] == 0 &&
            bufPres[1] == 0 &&
            bufPres[2] == 0 &&
            bufPres[3] == 0)
        {
            endPressure = 0;
        }
        else
        {
            for (int i = 0; i < bufPres.length; i++)
            {
                pressure[i] = bufPres[i];
                pressure[i] &= 0xff;
            }
            exponent = pressure[2] >> 4;
            endPressure = pressure[2] & 0x0f;
            endPressure <<= 8;
            endPressure += pressure[1];
            endPressure >>= (12 - (exponent + 1));
            endPressure += Math.pow(2.0, exponent + 1);
        }

        addMix(dive, oxygen, nitrogen, volume, startPressure, endPressure);
    }

    /**
     * gets diving site by parameters, if the site does not exist, it is created
     *
     * @param wholeLog
     *            whole log book
     * @param StrPlace
     *            site location
     * @param StrSite
     *            site name
     * @return diving site
     */
    private DiveSite getSite(JDiveLog wholeLog, String StrPlace, String StrSite)
    {
        /* returned site */
        DiveSite retSite = null;
        retSite = wholeLog.getMasterdata().getDiveSiteBySpotAndCity(StrSite,
                StrPlace);
        if (retSite == null)
        {
            retSite = new DiveSite();
            retSite.setCity(StrPlace);
            retSite.setSpot(StrSite);
            retSite.setPrivateId(Integer.toString(
                Math.abs(((StrPlace + StrSite).hashCode()))));
            wholeLog.getMasterdata().addDiveSite(retSite);
        }
        return retSite;
    }

    /**
     * finds next string in the file by skipping non ASCII characters
     *
     * @param fle
     *            file to read the data from
     * @return requested string
     * @throws IOException
     */
    private String readNextString(RandomAccessFile fle)
    throws IOException
    {
        /* length of this string */
        int lenStr = 0;

        /* string bytes from the file */
        byte bytes[] = null;

        /* find length of this string */
        lenStr = fle.readUnsignedByte();
        if (lenStr == 0)
        {
            return "";
        }
        if (lenStr < 0)
        {
            throw new IllegalArgumentException(
                "cannot find string at location" + fle.getFilePointer());
        }

        bytes = new byte[lenStr];
        fle.read(bytes);
        return new String(bytes);
    }

    /**
     * test the character for being an ASCII one: the whole range of
     * alphanumeric characters and punctuation is included in "ASCII" group
     *
     * @param chr
     *            character to test
     * @return <code>true</code> if the character is an ASCII one and
     *         <code>false</code> otherwise
     */
    private boolean IsAscii(int chr)
    {
        if (chr >= '!' && chr <= '~')
        {
            return true;
        }
        return false;
    }

    /**
     * Adds single mix to the dive
     *
     * @param dive dive to add the mix to
     * @param oxy oxygen percentage in range [0..100]
     * @param nitro nitrogen percentage in range [0..100]
     * @param tankVolume volume of added tank
     * @param startPressure initial pressure of the tank
     * @param endPressure pressure of the tank on exit
     */
    private void addMix(
        JDive dive,
        double oxy,
        double nitro,
        double tankVolume,
        double startPressure,
        double endPressure)
    {
        /* list of equipment of this dive */
        Equipment eq = dive.getEquipment();

        /* new tank with a mix */
        Tank tank = new Tank();

        /* gas of new tank */
        Gas gas = new Gas();

        if (oxy == 21)
        {
            gas.setName("Air");
        }
        else
        {
            gas.setName("EAN" + oxy);
        }
        gas.setOxygen(oxy / 100);
        gas.setHelium(0.0);
        gas.setNitrogen(nitro / 100);
        gas.setTankvolume(tankVolume / 1000);
        gas.setPstart(startPressure);
        gas.setPend(endPressure);
        tank.setGas(gas);

        dive.getDive().addGas(gas);
        dive.getDive().addSwitch(gas.getName());

        if (eq == null)
        {
            eq = new Equipment();
            dive.setEquipment(eq);
        }

        eq.addTank(tank);
    }
}
