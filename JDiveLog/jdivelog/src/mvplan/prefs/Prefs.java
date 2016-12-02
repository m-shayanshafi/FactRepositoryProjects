/*
 * Prefs.java
 *
 *  Maintains application preferences. Is serialised to XML for persistance.
 *
 *  This is a Singleton object in that only one should be instatiated. However this is not
 *  enforced via the Singleton Pattern.
 *
 *   @author Guy Wittig
 *   @version 28-Jun-2006
 *
 *   This program is part of MV-Plan
 *   Copywrite 2006 Guy Wittig
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   The GNU General Public License can be read at http://www.gnu.org/licenses/licenses.html
 */
package mvplan.prefs;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import mvplan.gas.Gas;
import mvplan.main.Mvplan;
import mvplan.segments.SegmentAbstract;
import mvplan.segments.SegmentDive;
import mvplan.util.PressureConverter;

public class Prefs implements Serializable
{
    private static final long serialVersionUID = 7542165197110047917L;
    public final static int METRIC=0;
    public final static int IMPERIAL=1;
    public final static double ALTITUDE_MAX = 3000;
    public final static double METERS_TO_FEET = 3.3;

    private int units;                  // 0 = metric or 1 = imperial    
    private boolean disableModUpdate;       // Automatically update MODS on units switch
    private double lastStopDepth;        //msw
    private double stopDepthIncrement;   //msw
    private double stopDepthMin;
    private double stopDepthMax;
    private double stopTimeIncrement;    //minutes
    private double ascentRate;           //msw per min
    private double ascentRateMin;       
    private double ascentRateMax;
    private double descentRate;          // "
    private double descentRateMax;
    private double descentRateMin;
    private boolean extendedLimits;      // Enables extended limits
    private double gfHigh;
    private double gfLow;
    private double gfMax;    
    private double gfMin;
    private double decoRMV;              // RMV for gas calculations
    private double diveRMV;
    private boolean ocDeco;              // Flag for OC deco
    private boolean forceAllStops;       // Flag to force a stop at all deco levels
    private boolean runtimeFlag;         // Flag for segtime == runtime (first non-zero segment)
    private int outputStyle;             // Defines output style, as per below    
    private boolean printColour;         // Print coloured dive tables
    private Color backgroundColour;          // Colour to use
    private boolean showSeconds;         // Show seconds in dive table 
    private boolean showStopTime;       // Show stop time in dive Table
    private boolean showRunTime;        // Show Run Time in dive table
    private boolean showGasFirst;       // Ordering of gas table
    private int frameSizeX;              // Remember screen size
    private int frameSizeY;
    private int frameSplit;
    private boolean gfMultilevelMode;    // On multilevel mode the gf does not get set until final ascent.
    private ArrayList<Gas> prefGases;         // Stores known gas list
    private ArrayList<SegmentAbstract> prefSegments;      // Stores dive segments
    private String lastModelFile;          // Stores last model filename used    
    private boolean agreedToTerms;    
    public static final int BRIEF=0;
    public static final int EXTENDED=1;
    private double pAmb;                // Ambient pressure in units of pressure (msw or fsw)
    private double pH2O;                // Partial pressure of H2O in breath (msw or fsw)
    private double pConversion;         // Pressure to unit/depth conversion. 10. for metric, 33. for imperial
    private double altitude;            // Stores current altitude    
    private double maxDepth;
    private double maxSegmentTime;
    private double maxSetpoint;
    private double maxMOD;
    private double maxPO2;              // Maximum ppO2 permitted before O2 warning is given
    private String printFontName;       // Used to store font details used in the dive plan printout 
    private int printFontBodySize;
    private int printFontHeaderSize;
    private String homeURL;             // Target for MVPLAN home     
    private String depthShortString;    // Displays m or f depending on units
    private String volumeShortString;   // Dislays l or cuft     
    private int[] modifiers;            // Multi-profile time modifiers   
     
    public void setDefaultPrefs()
    {
        // Set defaults
        units = METRIC;
        disableModUpdate=false;
        lastStopDepth=3;
        stopDepthIncrement =3;
        stopTimeIncrement = 1.0;
        extendedLimits=false;                // Default to standard limits         
        stopDepthMax=10.0;
        stopDepthMin=1.0;
        ascentRate = -10;
        ascentRateMin=-1.0;
        ascentRateMax=-10.0;
        descentRate= 20;
        descentRateMin=5.0;
        descentRateMax=50.0;
        gfHigh=0.85;
        gfLow=0.1;
        gfMin=0.0;
        gfMax=0.95;
        diveRMV=17.0;
        decoRMV=12.0;
        pAmb=10.0;          // Sea level in msw
        pConversion=10.0;   // Metric 1bar == 10msw
        altitude=0.0;
        pH2O=0.627;
        ocDeco=true;
        forceAllStops=true;
        runtimeFlag=true;
        outputStyle=BRIEF;
        showSeconds=false;
        printColour=true;
        backgroundColour = new  Color(228,255,255);
        showStopTime=true;
        showRunTime=true;
        showGasFirst=false;        
        frameSizeX=800;     // Display state
        frameSizeY=600;
        frameSplit=320;
        gfMultilevelMode=false;        
        prefGases = new ArrayList<Gas>();
        prefSegments = new ArrayList<SegmentAbstract>();
        prefGases.add(new Gas (0.0,0.21,66.0));
        prefSegments.add(new SegmentDive(30.0,20.0,prefGases.get(0),0));        
        maxDepth=100.0;         // Default maximums
        maxSegmentTime=100.0;
        maxSetpoint=1.6;
        maxMOD=1.607;
        maxPO2=1.6;        
        printFontName="Arial"; // Defaults for printing laminated dive plans
        printFontBodySize=9;
        printFontHeaderSize=8;
        agreedToTerms=false;        
        homeURL="http://wittig.net.au/diving/mvplan.html";        
        modifiers = new int[] {0,2,4,6,8};    // Default modifiers for multi-dive plans        
        // Default units strings are managed here as we know the units
        depthShortString = Mvplan.getResource("mvplan.meters.shortText");
        volumeShortString = Mvplan.getResource("mvplan.litres.shortText");  
    }
    
    private void setLimits(boolean limits){
        if (limits) {    // Set for expedition mode
            gfMax=1.0;
            maxSegmentTime=10000.0;
            maxSetpoint=2.0; 
            maxMOD=1.607;               
        }   else {
            maxSegmentTime=100.0;
            maxSetpoint=1.6;
            maxMOD=1.607;
            gfMax=0.95;                        
        }
         if(units==METRIC) 
            maxDepth = limits ? 200.0 : 100.0;
         else
             maxDepth= limits ? 660.0 : 330.0;         
    }
    
    /*
     * Checks that prefs are valid. This is necessary due to the possibility of missing XML tags.
     *
     */
    public void validatePrefs() {
               
        setLimits (extendedLimits);
        
        maxPO2=1.6; 
        gfMin=0.0;
        
        if(units==METRIC) {
            // METRIC UNITS
            if ( !(lastStopDepth>1.0 && lastStopDepth<=10.0) ) lastStopDepth=3.0;
            if ( !(stopDepthIncrement>1.0 && stopDepthIncrement<=10.0) ) stopDepthIncrement=3.0;        
            if ( !(stopTimeIncrement>0.0 && stopTimeIncrement<5.0)) stopTimeIncrement=1.0;
            if ( !(ascentRate>= -10.0 && ascentRate <= -1.0)) ascentRate=-10.0;
            if ( !(descentRate>=5.0 && descentRate<= 50.0)) descentRate=20.0;
            if (!(pAmb>5.0 && pAmb<10.5)) pAmb=10.0;
            if (altitude <0.0 || altitude > 5000.0) {
                altitude = 0.0;
                pAmb=10.0;
            }
            pConversion=10.0;
            if (!(pH2O>=0.0 && pH2O <0.7)) pH2O=0.627;            
            stopDepthMax=10.0;
            descentRateMax=50.0;
            descentRateMin=5.0;
            ascentRateMax=-10.0;
            ascentRateMin=-1.0;
            depthShortString = Mvplan.getResource("mvplan.meters.shortText");
            volumeShortString = Mvplan.getResource("mvplan.litres.shortText");
        } else {
            // IMPERIAL UNITS
            if ( !(lastStopDepth>3.0 && lastStopDepth<=30.0) ) lastStopDepth=10.0;
            if ( !(stopDepthIncrement>3.0 && stopDepthIncrement<=30.0) ) stopDepthIncrement=10.0;        
            if ( !(ascentRate>= -33.0 && ascentRate <= -3.0)) ascentRate=-33.0;
            if ( !(descentRate>=15.0 && descentRate<= 150.0)) descentRate=60.0;
            if (!(pAmb>15.0 && pAmb<36.0)) pAmb=33.0;
            if (altitude < 0.0 || altitude > 16500.0) {
                altitude = 0.0;
                pAmb=33.0;
            }
            pConversion=33.0;
            if (!(pH2O>=0.0 && pH2O <2.5)) pH2O=2.041;             
            stopDepthMax=40.0;  
            descentRateMax=150;
            descentRateMin=15.0;
            ascentRateMax=-33.0;
            ascentRateMin=-3.0;
            depthShortString = Mvplan.getResource("mvplan.feet.shortText");  
            volumeShortString = Mvplan.getResource("mvplan.cuft.shortText");
        }
        // Non unit dependent
        if ( !(stopTimeIncrement>0.0 && stopTimeIncrement<5.0)) stopTimeIncrement=1.0;            
        if ( !(gfHigh>=gfMin && gfHigh<=gfMax)) gfHigh=0.8;
        if ( !(gfLow>=gfMin && gfLow<=gfMax )) gfLow=0.3;
        if (!(diveRMV>0.0 && diveRMV<100.0)) diveRMV=17.0;
        if (!(decoRMV>0.0 && decoRMV<100.0)) decoRMV=12.0;  
        
        if (prefGases.isEmpty()) prefGases.add(new Gas (0.0,0.21,66.0));
        if (prefSegments.isEmpty()) prefSegments.add(new SegmentDive(30.0,20.0,prefGases.get(0),1.3));
        
        if (printFontName==null || printFontName.contains("Dialog") )    printFontName="Arial";
        if (printFontBodySize<8 || printFontBodySize>18) printFontBodySize=9;
        if (printFontHeaderSize<6 || printFontHeaderSize>18) printFontHeaderSize=8;
        homeURL="http://wittig.net.au/diving/mvplan.html";
        if (modifiers == null) modifiers = new int [] {0,2,4,6,8};
        if (!showStopTime && !showRunTime) showRunTime=true;   
        if (backgroundColour == null) backgroundColour = new  Color(228,255,255);
                  
        // Check ArrayLists for consistency
        Iterator<SegmentAbstract> it = prefSegments.iterator();
        SegmentAbstract seg;
        while(it.hasNext()) {
            seg = it.next();
            if( seg.getGas() == null ) {
                seg.setGas(prefGases.get(0));  
                if(Mvplan.DEBUG >0) System.out.println("Prefs: fixed missing gas.");
            }
        }        
    }
        /*
     * Used to change the units. Needs to make some changes to some parameters
     */
    public void setUnitsTo(int units) {
        if (this.units == units)    return;
        if(Mvplan.DEBUG>0) System.out.println("Prefs: setting units to "+units);
        setUnits(units);
        if(units==METRIC) {
            //ascentRate=-10.0;                
            pAmb=10.0;
            pConversion = 10.0;
            pH2O=0.627;                  
        } else {
            //ascentRate=-33.0;
            pAmb=33.0;
            pConversion = 33.0;
            pH2O=2.041;             
        }
        validatePrefs();    // Just check that everything is still ok.
    }

    /* 
     * Sets altitude and corresponding ambient pressure pAmb 
     */
    public void setAltitude( double alt) {
        if(units==METRIC){
            // Bounds check
            if(alt < 0.0 || alt > ALTITUDE_MAX ) alt = 0.0;
            pAmb=PressureConverter.altitudeToPressure(alt);
            altitude = alt;                        
        } else {
            // Bounds check
            if(alt < 0.0 || alt > ALTITUDE_MAX * METERS_TO_FEET) alt = 0.0;
            pAmb=PressureConverter.altitudeToPressure(alt/METERS_TO_FEET)*METERS_TO_FEET; // This class is metric so convert for feet
            altitude = alt;             
        }     
        if(Mvplan.DEBUG > 0) System.out.println("setAltitude: "+altitude+" "+ pAmb);
    }
    

    // Accessor methods
    public boolean isDisableModUpdate()     { return disableModUpdate; }
    public double getLastStopDepth()        { return lastStopDepth; }
    public double getStopDepthIncrement()   { return stopDepthIncrement; }
    public double getStopTimeIncrement ()   { return stopTimeIncrement; }
    public double getStopDepthMax()         { return stopDepthMax; }
    public double getStopDepthMin()         { return stopDepthMin; }
    public double getAscentRate()           { return ascentRate; }
    public double getAscentRateMax()        { return ascentRateMax; }
    public double getAscentRateMin()        { return ascentRateMin; }
    public double getDescentRate()          { return descentRate; }
    public double getDescentRateMax()       { return descentRateMax; }
    public double getDescentRateMin()       { return descentRateMin; }
    public boolean getExtendedLimits()      { return extendedLimits; }
    public double getGfHigh()               { return  gfHigh; }   
    public double getGfLow()                { return gfLow; }
    public double getGfMax()                { return gfMax; }
    public double getGfMin()                {return gfMin; }
    public double getDecoRMV()              { return decoRMV; }
    public double getDiveRMV()              { return diveRMV; }
    public boolean getOcDeco()              { return ocDeco; }   
    public boolean getForceAllStops()       { return forceAllStops; }
    public boolean getRuntimeFlag()         { return runtimeFlag; }
    public int getOutputStyle()             { return outputStyle; }
    public boolean isPrintColour()         { return printColour; }
    public boolean isShowSeconds()         { return showSeconds;}
    public int getFrameSizeX()              { return frameSizeX; }
    public int getFrameSizeY()              { return frameSizeY; }
    public int getFrameSplit()              { return frameSplit; }
    public boolean getGfMultilevelMode()    { return gfMultilevelMode; }
    public ArrayList<Gas> getPrefGases()         { return prefGases; }
    public ArrayList<SegmentAbstract> getPrefSegments()      { return prefSegments; }
    public String getLastModelFile()          { return lastModelFile; }
    public double getPAmb()                 { return pAmb; }
    public double getPConversion()          { return pConversion; }
    public double getPH2O()                 { return pH2O; }
    public double getAltitude()             { return altitude; }
    public boolean getAgreedToTerms()       { return agreedToTerms; }   
    public double getMaxDepth()             { return maxDepth; }
    public double getMaxSegmentTime()       { return maxSegmentTime; }
    public double getMaxSetpoint()          { return maxSetpoint; }
    public double getMaxMOD()               { return maxMOD; }
    public double getMaxPO2()               { return maxPO2; }
    public String getPrintFontName()        { return printFontName; }
    public int getPrintFontBodySize()       { return printFontBodySize; }
    public int getPrintFontHeaderSize()     { return printFontHeaderSize; }
    public int[] getModifiers()             { return modifiers; }
    public String getDepthShortString()     { return depthShortString; }
    public String getVolumeShortString()    { return volumeShortString; }

    
    // Mutator methods - TODO no bounds checking here
    public void setDisableModUpdate(boolean b)      { disableModUpdate = b;}
    public void setLastStopDepth(double d)          { lastStopDepth=d; }
    public void setStopDepthIncrement(double d)     { stopDepthIncrement=d; }
    public void setStopTimeIncrement (double t)     { stopTimeIncrement=t; }
    public void setAscentRate(double r)             { ascentRate=r; }
    public void setDescentRate(double r)            { descentRate=r; }
    public void setGfHigh(double gf)                { gfHigh=gf; }   
    public void setGfLow(double gf)                 { gfLow=gf; }
    public void setDecoRMV(double rmv)              { decoRMV=rmv; }
    public void setDiveRMV(double rmv)              { diveRMV=rmv; }
    public void setOcDeco(boolean b)                { ocDeco=b; }
    public void setForceAllStops(boolean b)         { forceAllStops=b; }
    public void setRuntimeFlag(boolean b)           { runtimeFlag=b; }
    public void setOutputStyle(int i)               { outputStyle=i; }
    public void setPrintColour(boolean b)           { printColour=b; }
    public void setShowSeconds(boolean b)           { showSeconds=b;}
    public void setFrameSizeX(int i)                { frameSizeX=i; }
    public void setFrameSizeY(int i)                { frameSizeY=i; }
    public void setFrameSplit(int i)                { frameSplit=i; }
    public void setGfMultilevelMode(boolean b)      { gfMultilevelMode=b; }
    public void setPrefGases(ArrayList<Gas> a)           { prefGases=a; }
    public void setPrefSegments(ArrayList<SegmentAbstract> a)        { prefSegments=a; }
    public void setLastModelFile(String f)          { lastModelFile=f; }
    public void setPAmb(double d)                   { pAmb=d; }
    public void setPH2O(double d)                   { pH2O=d; }
    public void setAgreedToTerms(boolean b)         { agreedToTerms=b; }
    public void setPrintFontName(String s)          { printFontName=s;} 
    public void setPrintFontBodySize(int i)         { if (i>=8 && i<=18) printFontBodySize=i; }
    public void setPrintFontHeaderSize(int i)         { if (i>=6 && i<=18) printFontHeaderSize=i; } 
    public void setModifiers(int [] ia)             { modifiers = ia; }
    
    /* Flags are used to alter program operation level
     * Currently:
     *  8 = expedition mode with extended limits.
     *
     */
    public void setExtendedLimits(boolean b) { 
        extendedLimits=b;
        // Update limits
        setLimits(extendedLimits);
    }
    
    
    // These are accessors created by Netbeans ...
    
    public boolean isShowStopTime() {
        return showStopTime;
    }

    public void setShowStopTime(boolean showStopTime) {
        this.showStopTime = showStopTime;
    }

    public boolean isShowRunTime() {
        return showRunTime;
    }

    public void setShowRunTime(boolean showRunTime) {
        this.showRunTime = showRunTime;
    }

    public boolean isShowGasFirst() {
        return showGasFirst;
    }

    public void setShowGasFirst(boolean showGasFirst) {
        this.showGasFirst = showGasFirst;
    }

    public Color getBackgroundColour() {
        return backgroundColour;
    }

    public void setBackgroundColour(Color backgroundColour) {
        this.backgroundColour = backgroundColour;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }
    public String getHomeURL() {
        return homeURL;
    }

    public void setHomeURL(String homeURL) {
        this.homeURL = homeURL;
    }

}

