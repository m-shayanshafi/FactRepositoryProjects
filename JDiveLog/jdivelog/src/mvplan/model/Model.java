/**
 * Model.java <br/>
 *
 * Represents a Buhlmann model.<br/>
 * Composed of a tissue array of Compartment[]<br/>
 * Has an OxTox and Gradient object <br/>
 * Can throw a ModelStateException propagated from a Compartment if pressures or time is out of bounds.<br/>
 * 
 * Model can be initialised from scratch or may be rebuilt from a saved Model via the ModelDAO class. <br/>
 * Models are initialised by initModel() if they are new models, or<br/>
 * validated by validateModel() if they are rebuild from a saved model.<br/> 
 * 
 * The model is capable of ascending or descending via ascDec() using the ascDec() method of Compartment,<br/>
 * or accounting for a constant depth using the constDepth() method of Compartment.<br/>
 *
 *   @author Guy Wittig
 *   @version 18-Jun-2006
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

package mvplan.model;

import mvplan.main.Mvplan;
import java.io.*;


public class Model implements Serializable
{
	private static final long serialVersionUID = 128863968000341210L;

	private Compartment[] tissues;          // Buhlmann tissues == array of compartments 
    private Gradient gradient;              // Gradient factor object
    private OxTox oxTox;                    // Oxygen toxicity model
    private String metaData;                // Stores information about where the model was created
    private static int COMPS=16;            // Number of compartments   
    private int units;                      // Metric or imperial units
    
    /** Return value - Model validated correctly */
    public static int MODEL_VALIDATION_SUCCESS=0;   
    /** Return value - Model failed to validate correctly */
    public static int MODEL_VALIDATION_FAILED=-1;   
    /** Indicates metric units used for model - msw */
    public static int METRIC=0;    
    /** Indicates imperial units used for model - fsw */
    public static int IMPERIAL=1;                   
    
    /**
     * Constructor for objects of class Model
     */
    public void initModel()
    {
        int c;  // counter 
        units = Mvplan.prefs.getUnits();           
        tissues = new Compartment[COMPS];         
        initGradient();         // Initialise gradient factor object               
        initOxTox();

        //pAmb = surface pressure msw absolute
        for (c=0; c<COMPS; c++) {
            // create and initialise compartments
            tissues[c] = new Compartment();
            tissues[c].setPpHe(0.0);                // Set initial ppH2 = 0.0           
            tissues[c].setPpN2(0.79*(Mvplan.prefs.getPAmb()-Mvplan.prefs.getPH2O()));     // Set ppN2 = Ambient - ppH2O
            if(Mvplan.DEBUG>1) System.out.println("Initialising compartment "+c+" ppN2="+tissues[c].getPpN2());
        }      
        setTimeConstants(); // Set all the Buhlmann timeconstants
        metaData="(none)";
    }
    /**
     * Initialise time constants in Buhlmann tissue array
     */
    private void setTimeConstants(){

        if(units == METRIC) {
            // public Compartment(double hHe,double hN2,double aHe,double bHe,double aN2,double bN2)
            // This is for Buhlmann ZHL-16B with the 1b halftimes
            // a = intercept at zero ambient pressure
            // b = reciprocal of slope of m-value line
            // public Compartment                  (hHe,    hN2,    aHe,    bHe,    aN2,    bN2
            tissues[0].setCompartmentTimeConstants(1.88,    5.0,    16.189, 0.4770, 11.696, 0.5578);         
            tissues[1].setCompartmentTimeConstants(3.02,    8.0,    13.83,  0.5747, 10.0,   0.6514);
            tissues[2].setCompartmentTimeConstants(4.72,    12.5,   11.919, 0.6527, 8.618,  0.7222);
            tissues[3].setCompartmentTimeConstants(6.99,    18.5,   10.458, 0.7223, 7.562,  0.7825);
            tissues[4].setCompartmentTimeConstants(10.21,   27.0,   9.220,  0.7582, 6.667,  0.8126);
            tissues[5].setCompartmentTimeConstants(14.48,   38.3,   8.205,  0.7957, 5.60,   0.8434);
            tissues[6].setCompartmentTimeConstants(20.53,   54.3,   7.305,  0.8279, 4.947,  0.8693);
            tissues[7].setCompartmentTimeConstants(29.11,   77.0,   6.502,  0.8553, 4.5,    0.8910);
            tissues[8].setCompartmentTimeConstants(41.20,   109.0,  5.950,  0.8757, 4.187,  0.9092);
            tissues[9].setCompartmentTimeConstants(55.19,   146.0,  5.545,  0.8903, 3.798,  0.9222);
            tissues[10].setCompartmentTimeConstants(70.69,  187.0,  5.333,  0.8997, 3.497,  0.9319);
            tissues[11].setCompartmentTimeConstants(90.34,  239.0,  5.189,  0.9073, 3.223,  0.9403);
            tissues[12].setCompartmentTimeConstants(115.29, 305.0,  5.181,  0.9122, 2.850,  0.9477);
            tissues[13].setCompartmentTimeConstants(147.42, 390.0,  5.176,  0.9171, 2.737,  0.9544);
            tissues[14].setCompartmentTimeConstants(188.24, 498.0,  5.172,  0.9217, 2.523,  0.9602);
            tissues[15].setCompartmentTimeConstants(240.03, 635.0,  5.119,  0.9267, 2.327,  0.9653);
        } else if(units == IMPERIAL) {            
            // public Compartment                  (hHe,    hN2,    aHe,    bHe,    aN2,    bN2
            tissues[0].setCompartmentTimeConstants(1.88,    5.0,    52.73,  0.4770, 38.09,  0.5578); 
            tissues[1].setCompartmentTimeConstants(3.02,    8.0,    45.04,  0.5747, 32.57,  0.6514);
            tissues[2].setCompartmentTimeConstants(4.72,    12.5,   38.82,  0.6527, 28.07,  0.7222);
            tissues[3].setCompartmentTimeConstants(6.99,    18.5,   34.06,  0.7223, 24.63,  0.7825);            
            tissues[4].setCompartmentTimeConstants(10.21,   27.0,   30.03,  0.7582, 21.71,  0.8126);
            tissues[5].setCompartmentTimeConstants(14.48,   38.3,   26.72,  0.7957, 18.24,  0.8434);
            tissues[6].setCompartmentTimeConstants(20.53,   54.3,   23.79,  0.8279, 16.11,  0.8693);
            tissues[7].setCompartmentTimeConstants(29.11,   77.0,   21.18,  0.8553, 14.66,  0.8910);
            tissues[8].setCompartmentTimeConstants(41.20,   109.0,  19.38,  0.8757, 13.64,  0.9092);
            tissues[9].setCompartmentTimeConstants(55.19,   146.0,  18.06,  0.8903, 12.37,  0.9222);
            tissues[10].setCompartmentTimeConstants(70.69,  187.0,  17.37,  0.8997, 11.39,  0.9319);
            tissues[11].setCompartmentTimeConstants(90.34,  239.0,  16.90,  0.9073, 10.50,  0.9403);
            tissues[12].setCompartmentTimeConstants(115.29, 305.0,  16.87,  0.9122, 9.280,  0.9477);
            tissues[13].setCompartmentTimeConstants(147.42, 390.0,  16.86,  0.9171, 8.910,  0.9544);
            tissues[14].setCompartmentTimeConstants(188.24, 498.0,  16.84,  0.9217, 8.220,  0.9602);
            tissues[15].setCompartmentTimeConstants(240.03, 635.0,  16.67,  0.9267, 7.580,  0.9653);            
        }
    }
    
    /**
     * Validate model - checks over the model and looks for corruption. 
     * This is needed to chack a model that has been loaded from XML.
     * Resets time constants
     * @return Validation result: MODEL_VALIDATION_SUCCESS or MODEL_VALIDATION_FAILED
     */
    public int validateModel(){        
        boolean fail=false;
        boolean timeConstantZero=false;
        int c;  // Counter
        
        for (c=0;c<COMPS;c++){          
            if(tissues[c].getPpN2()<=0.0)   fail=true;      // N2 must always be +ve
            if(tissues[c].getPpHe()< 0.0)   fail=true;      // He may be zero
            // TODO - do we need this or remove all accessors / mutators for timeconstants and always set them ?
            if(tissues[c].getKHe()==0.0)    timeConstantZero=true;
            if(tissues[c].getKN2()==0.0)    timeConstantZero=true;  
            if(tissues[c].getAHe()==0.0)    timeConstantZero=true;
            if(tissues[c].getBHe()==0.0)    timeConstantZero=true;
            if(tissues[c].getAN2()==0.0)    timeConstantZero=true;
            if(tissues[c].getBN2()==0.0)    timeConstantZero=true;
       }       
       if(timeConstantZero)   setTimeConstants();
       if(!fail)
           return MODEL_VALIDATION_SUCCESS;
       else
           return MODEL_VALIDATION_FAILED;             
    }
    
    /** 
     * Initialises the model's gradient factor object
     */
    public void initGradient() {
        gradient = new Gradient(Mvplan.prefs.getGfLow(),Mvplan.prefs.getGfHigh());        // Default Gradient factors         
    }
    /**
     * Initialise OxTox model
     */
    public void initOxTox() {
        oxTox = new OxTox();
        oxTox.initOxTox();
    }
    
    /**
     * Determine the controlling compartment at ceiling (1-16)
     * @return Controlling compartment (1-16)
     */
    public int controlCompartment()
    {
        int c;
        int control=0;
        double depth=0.0;
        double p=0.0;

        for (c=0;c<COMPS;c++) {
            p=tissues[c].getMaxAmb(gradient.getGradientFactor())-Mvplan.prefs.getPAmb();      // Get compartment max pressure
            if(p>depth) {
                control=c;
                depth=p;
            }
        }
        return control+1;
    }


    /**
      * Determine the current ceiling depth
     *  @return Ceiling depth msw (fsw)
     */
    public double ceiling()
    {
        int c;
        double depth=0.0;       // depth in msw
        double p=0.0;           // compartment pressure in msw

        for (c=0;c<COMPS;c++) {     // For all compartments ...
            // Get compartment tolerated ambient pressure and convert from absolute pressure to depth
            p=tissues[c].getMaxAmb(gradient.getGradientFactor()) - Mvplan.prefs.getPAmb();       
            // Save max depth
            if(p>depth)
                depth=p;
        }
        return depth;
    }

    /**
     * Determine the maximum M-Value for a given depth
     * @return Maximum M-Value
     * @param depth Depth in msw (fsw)
     */
    public double mValue(double depth)
    {
        int c;
        double pAbsolute = depth+Mvplan.prefs.getPAmb();             // derive ambient pressure for the given depth
        double compartmentMV=0.0;
        double maxMV=0.0;       
    
        for (c=0;c<COMPS;c++) {
            compartmentMV=tissues[c].getMV(pAbsolute);
            if(compartmentMV>maxMV)
                maxMV=compartmentMV;
        }
        return maxMV;       
    }


    /**
     * Constant depth profile. Calls Compartment.constDepth for each compartment to update the model.
     * @param depth Depth of segment in metres
     * @param segTime Time of segment in minutes
     * @param fHe Fraction of inert gas Helium in inspired gas mix
     * @param fN2 Fraction of inert gas Nitrogen in inspired gas mix
     * @param pO2 For CCR mode, partial pressure of oxygen in bar. If == 0.0, then open circuit
     * @throws mvplan.model.ModelStateException Propagates ModelStateException
     */ 
    public void constDepth(double depth, double segTime, double fHe, double fN2, double pO2) throws ModelStateException
    {
        double ppHeInspired;        // inspired gas pp
        double ppN2Inspired;
        double ppO2Inspired;
        double pInert;              // Total inert gas pressure (msw)
        double pAmb = depth+Mvplan.prefs.getPAmb();     // Total ambient pressure  (msw)
        int c;

        // Set inspired gas fractions.
        if(pO2 > 0.0) {                     
            // Rebreather mode
            // Determine pInert by subtracting absolute oxygen pressure (msw) and pH20 (msw)
            // Note that if fHe and fN2 == 0.0 then need to force pp's to zero
            if ((fHe+fN2)>0.0)
                pInert = pAmb - pO2*Mvplan.prefs.getPConversion()-Mvplan.prefs.getPH2O();
            else
                pInert=0.0;
            
            // Verify that pInert is positive. If the setpoint is close to or less than the depth
            // then there is no inert gas.
            if ( pInert>0.0) { 
                ppHeInspired = (pInert * fHe)/(fHe+fN2);
                ppN2Inspired = (pInert * fN2)/(fHe+fN2); 
            } else {
                ppHeInspired =0.0;
                ppN2Inspired =0.0;
            }
            // Update OxTox model - pO2 in atm NOT msw
            ppO2Inspired = pO2*Mvplan.prefs.getPConversion();  // Determine ppO2Inspired in msw
            // Check that ppO2Inspired is not greater than the depth. This occurs in shallow deco when the
            // setpoint specified is > depth in msw.
            if( (ppO2Inspired <= depth+Mvplan.prefs.getPAmb()) && (pInert > 0.0) )
                // pO2 is as per the setpoint
                oxTox.addO2(segTime,pO2);             
            else 
                // pO2 is equal to the depth in atm. Also true if there is no inert gas in the gas
                oxTox.addO2(segTime, (depth+Mvplan.prefs.getPAmb()-Mvplan.prefs.getPH2O())/Mvplan.prefs.getPConversion() );                                
            
        } else { 
            // Open circuit mode
            ppHeInspired = (pAmb-Mvplan.prefs.getPH2O())*fHe;
            ppN2Inspired = (pAmb-Mvplan.prefs.getPH2O())*fN2;
            // Update OxTox model - pO2 in atm NOT msw
            if( depth==0.0)  // Surface
                oxTox.removeO2(segTime);    
            else
                oxTox.addO2(segTime, ((pAmb-Mvplan.prefs.getPH2O()) * (1.0-fHe-fN2) )/Mvplan.prefs.getPConversion() );
        }

        // public void constDepth(double ppHeInspired, double ppN2Inspired, double segTime)     
        if(segTime>0) {
            for (c=0;c<COMPS;c++) {
                tissues[c].constDepth(ppHeInspired,ppN2Inspired,segTime);
            }
        }
        if((Mvplan.DEBUG > 1)) 
            System.out.println("-->CONST: "+depth+"m, t:"+segTime+" fHe:"+fHe+" fN2:"+fN2+" ppO2:"+pO2+" ppHei:"+ppHeInspired+" ppN2i:"+ppN2Inspired);

    }   

    /**
     * Ascend/Descend in profile. Calls model.ascDec to update compartments
     * @param start - Start depth of segment in metres
     * @param finish - Finish depth of segment in metres
     * @param rate - Rate of ascent (-ve) or descent (+ve) in m/min
     * @param fHe Fraction of inert gas Helium in inspired gas mix
     * @param fN2 Fraction of inert gas Nitrogen in inspired gas mix
     * @param pO2 For CCR mode, partial pressure of oxygen in bar. If == 0.0, then open circuit
     * @throws mvplan.model.ModelStateException Propogates ModelStateException
     */
    public void ascDec(double start, double finish, double rate, double fHe, double fN2,double pO2 ) throws ModelStateException
    {
        int c;
        double ppHeInspired;                        // Initial inspired gas pp
        double ppN2Inspired;
        double ppO2Inspired;
        double pO2InspiredAverage;                  // For oxtox calculations        
        double segTime = (finish-start)/rate;       // derive segment time (mins)
        double rateHe;                              // Rate of change for each inert gas (msw/min)
        double rateN2;  

        double pAmbStart = start+Mvplan.prefs.getPAmb();                // Starting ambient pressure (msw)
        double pAmbFinish = finish+Mvplan.prefs.getPAmb();
        double pInertStart,pInertFinish;            // 

        // Set inspired gas fractions.
        if(pO2 > 0.0) {                     
            // Rebreather mode
            // Calculate inert gas partial pressure (msw) == pAmb - pO2 - pH2O 
            pInertStart = pAmbStart - pO2*Mvplan.prefs.getPConversion()-Mvplan.prefs.getPH2O();
            pInertFinish = pAmbFinish -pO2*Mvplan.prefs.getPConversion()-Mvplan.prefs.getPH2O();
            // Check that it doesn't go less than zero. Could be due to shallow deco or starting on high setpoint
            if (pInertStart<0.0) pInertStart=0.0;       
            if (pInertFinish<0.0) pInertFinish=0.0;
            // Separate into He and N2 components, checking that we are not on pure O2 (or we get an arithmetic error)
            if( (fHe+fN2) > 0.0 ) { 
                ppHeInspired = (pInertStart * fHe)/(fHe+fN2);
                ppN2Inspired = (pInertStart * fN2)/(fHe+fN2);
                // Calculate rate of change of each inert gas
                rateHe = ((pInertFinish * fHe)/(fHe+fN2) - ppHeInspired)/segTime;
                rateN2 = ((pInertFinish * fN2)/(fHe+fN2) - ppN2Inspired)/segTime;
            } else {
                ppHeInspired=0.0;
                ppN2Inspired=0.0;
                rateHe=0.0;
                rateN2=0.0;
            }
            
            // Update OxTox model, constant ppO2
            // TODO - what if depth is less than pO2 in msw ?
            oxTox.addO2(segTime,pO2);           
    
        } else {                            
            // Open circuit mode
            // Calculate He and N2 components 
            ppHeInspired = (pAmbStart-Mvplan.prefs.getPH2O())*fHe;
            ppN2Inspired = (pAmbStart-Mvplan.prefs.getPH2O())*fN2;
            // Calculate rate of change of each inert gas
            rateHe=rate * fHe;
            rateN2=rate * fN2;
            // Update OxTox model, use average ppO2
            pO2InspiredAverage=( (pAmbStart-pAmbFinish)/2 + pAmbFinish -Mvplan.prefs.getPH2O())*(1.0-fHe-fN2)/Mvplan.prefs.getPConversion();       
            oxTox.addO2(segTime,pO2InspiredAverage);
        }

        for (c=0;c<COMPS;c++) {
            tissues[c].ascDec(ppHeInspired,ppN2Inspired,rateHe,rateN2,segTime);
        }
        if((Mvplan.DEBUG > 1)) 
            System.out.println("--> ASC (to): "+finish+"m, fHe:"+fHe+" fN2:"+fN2+" ppO2:"+pO2);   

    }

    /**
     * Prints the model for debugging purposes
     */     
    public void printModel()
    {
        int c;  // counter        
        //System.out.println("Current depth: "+currentDepth);
        System.out.println("Compartment pressures (msw)");
        // System.out.println("Maximum tolerated pressures ...");
        for (c=0; c<COMPS; c++) {
            System.out.println("C:"+c+" He:"+tissues[c].getPpHe()+" N2:"+tissues[c].getPpN2()+" Ceiling:"+
                     (tissues[c].getMaxAmb(gradient.getGradientFactor())-10.0)+" MV: "+tissues[c].getMV(10.0));
        }
        System.out.println("Ceiling: "+ceiling());
        System.out.println("Max surface M-Value: "+mValue(0.0));
        System.out.println("OTUs accumulated:"+oxTox.getOtu());
    }
    
    /****************** Accessor and mutator bean methods ****************/
    /**
     * Gets Gradient object for this model
     * @return return Gradient object
     */
    public Gradient getGradient()           { return gradient; }
    /**
     * Sets Gradient object for this model
     * @param g Gradient object
     */
    public void setGradient(Gradient g)     { gradient = g; }
    /**
     * Gets OxTox object for this model
     * @return OxTox object
     */
    public OxTox getOxTox()                 { return oxTox; }
    /**
     * Sets OxTox object for this model
     * @param o OxTox Object
     */
    public void setOxTox(OxTox o)           { oxTox = o; }
    /**
     * Gets tissues as Array of Compartment[] for this model
     * @return tissues - Array of Compartment[]
     */
    public Compartment[] getTissues()       { return tissues; }
    /**
     * Sets tissues array of Compartment[]
     * @param t Tissue array of Compartment
     */
    public void setTissues(Compartment[] t) { tissues=t; }
    /**
     * Gets metadata string for this model
     * @return Metadata String
     */
    public String getMetaData()             { return metaData; }
    /**
     * Sets metadata String for this model
     * @param s Metadata String
     */
    public void setMetaData(String s)       { metaData=s;}    

    /**
     * Gets units for this model: METRIC or IMPERIAL
     * @return units: METRIC or IMPERIAL
     */
    public int getUnits() {
        return units;
    }

    /**
     * Sets units for this model: METRIC or IMPERIAL
     * @param units Sets units: METRIC or IMPERIAL
     */
    public void setUnits(int units) {
        this.units = units;
    }
}
