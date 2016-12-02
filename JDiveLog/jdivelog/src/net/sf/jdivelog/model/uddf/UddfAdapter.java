package net.sf.jdivelog.model.uddf;

import java.util.Iterator;
import java.util.List;

import net.sf.jdivelog.model.JDive;
import net.sf.jdivelog.model.udcf.Alarm;
import net.sf.jdivelog.model.udcf.Depth;
import net.sf.jdivelog.model.udcf.Dive;
import net.sf.jdivelog.model.udcf.Gas;
import net.sf.jdivelog.model.udcf.PPO2;
import net.sf.jdivelog.model.udcf.Switch;
import net.sf.jdivelog.model.udcf.Temperature;
import net.sf.jdivelog.model.udcf.Time;
import net.sf.jdivelog.model.uddf.file.UddfFileDive;
import net.sf.jdivelog.model.uddf.file.UddfFileGasDefinitions;
import net.sf.jdivelog.model.uddf.file.UddfFileMix;
import net.sf.jdivelog.model.uddf.file.UddfFileTankdata;
import net.sf.jdivelog.model.uddf.file.UddfFileWayPoint;

/**
 * This class knows how to build a JDive from an UDDF Dive
 * 
 * @author Levtraru
 * 
 */
public class UddfAdapter {

    private UddfAdapter() {

    }

    private static UddfAdapter instance;

    /**
     * Treat this class as a Singleton
     * 
     * @return
     */
    public static UddfAdapter getInstance() {
        if (instance == null) {
            instance = new UddfAdapter();
        }
        return instance;
    }

    /**
     * builds a JDive from an UDDF Dive
     * 
     * @param uddfDive
     * @param gasMixs
     * @return
     * @throws UddfException
     */
    public JDive buildJDive(UddfFileDive uddfDive, UddfFileGasDefinitions gasDefinitions) throws UddfException {
        Dive udcfDive = this.buildUdcfDive(uddfDive, gasDefinitions);
        JDive jDive = new JDive(UddfDigesterConfigurator.DEFAULT_UNITS, udcfDive);
        return jDive;
    }

    /**
     * Builds the underlying UDCF Dive
     * 
     * @param uddfDive
     * @param gasMixs
     * @return
     * @throws UddfException
     */
    private Dive buildUdcfDive(UddfFileDive uddfDive, UddfFileGasDefinitions gasDefinitions) throws UddfException {
        Dive dive = new Dive();
        dive.setDate(uddfDive.getDate());
        dive.setSurfaceinterval(uddfDive.getSurfaceinterval().getPassedtime());
        dive.setTemperature(UddfUtilsConverter.getInstance().convertToDouble(uddfDive.getLowesttemperature()));
        dive.setSurfaceTemperature(UddfUtilsConverter.getInstance().convertToDouble(uddfDive.getAirtemperature()));
        dive.setDensity(UddfUtilsConverter.getInstance().convertToDouble(uddfDive.getDensity()));
        dive.setAltitude(UddfUtilsConverter.getInstance().convertToDouble(uddfDive.getAltitude()));
        dive.setTimeDepthMode();

        addSamples(dive, uddfDive.getSamples(), gasDefinitions);
        return dive;

    }

    /**
     * Adds the different samples. Assumes there will always be a Divetime and a
     * Depth in a Way Point
     * 
     * @param dive
     * @param waypoints
     * @param gasMixs
     * @throws UddfException
     */
    private void addSamples(Dive dive, List<UddfFileWayPoint> waypoints, UddfFileGasDefinitions gasDefinitions) throws UddfException {
        Iterator<UddfFileWayPoint> it = waypoints.iterator();
        while (it.hasNext()) {
            UddfFileWayPoint waypoint = it.next();

            Time time = buildTime(waypoint);
            dive.addSample(time);

            Depth depth = buildDepth(waypoint);
            dive.addSample(depth);

            if (waypoint.hasTemperature()) {
                Temperature temperature = buildTemperature(waypoint);
                dive.addSample(temperature);
            }

            if (waypoint.hasSwitchmix()) {
                Switch switchMix = buildSwitch(waypoint, gasDefinitions);
                dive.addSample(switchMix);
                dive.addGas(buildGas(gasDefinitions, switchMix));
            }

            if (waypoint.hasPo2()) {
                PPO2 ppo2 = buildPo2(waypoint);
                dive.addSample(ppo2);
            }

            if (waypoint.hasAlarm()) {
                Alarm alarm = buildAlarm(waypoint);
                dive.addSample(alarm);
            }

        }
    }

    /**
     * Creates a Gas object from the UDDF <gasdefinitions> tag. Looks for the
     * referenced <mix> and <tankdata> tags according to switchMix param
     * 
     * @param gasDefinitions
     * @param switchMix
     * @return
     * @throws UddfException
     */
    private Gas buildGas(UddfFileGasDefinitions gasDefinitions, Switch switchMix) throws UddfException {
        UddfFileMix uddfMix = getUddfMixByName(gasDefinitions.getMixs(), switchMix.getValue());

        Gas gas = new Gas();
        gas.setHelium(UddfUtilsConverter.getInstance().convertToDouble(uddfMix.getHe()));
        gas.setName(uddfMix.getName());
        gas.setNitrogen(UddfUtilsConverter.getInstance().convertToDouble(uddfMix.getN2()));
        gas.setOxygen(UddfUtilsConverter.getInstance().convertToDouble(uddfMix.getO2()));

        UddfFileTankdata tankdata = getTankContainingMix(gasDefinitions.getTanksdata(), uddfMix.getId());
        if (tankdata != null) {
            gas.setTankvolume(UddfUtilsConverter.getInstance().convertToDouble(tankdata.getTankvolume()));
            gas.setPstart(UddfUtilsConverter.getInstance().convertToDouble(tankdata.getTankpressurebegin()));
            gas.setPend(UddfUtilsConverter.getInstance().convertToDouble(tankdata.getTankpressureend()));
        }
        return gas;
    }

    /**
     * Returns the Tank data matching mixref=mixId
     * 
     * @param tanksdata
     * @param mixId
     * @return
     * @throws UddfException
     */
    private UddfFileTankdata getTankContainingMix(List<UddfFileTankdata> tanksdata, String mixId) throws UddfException {
        Iterator<UddfFileTankdata> it = tanksdata.iterator();
        while (it.hasNext()) {
            UddfFileTankdata tankdata = it.next();
            if (tankdata.getMixref().equals(mixId)) {
                return tankdata;
            }
        }
        return null;
    }

    /**
     * Returns the Mix matching mixId
     * 
     * @param gasMixs
     * @param mixId
     * @return
     * @throws UddfException
     */
    private UddfFileMix getUddfMix(List<UddfFileMix> gasMixs, String mixId) throws UddfException {
        Iterator<UddfFileMix> it = gasMixs.iterator();
        while (it.hasNext()) {
            UddfFileMix uddfFileMix = it.next();
            if (uddfFileMix.getId().equals(mixId)) {
                return uddfFileMix;
            }
        }
        throw new UddfException("Inexistent Mix: " + mixId + ". It should be declared on <gasdefinitions> section.");
    }

    /**
     * Returns the Mix matching mixId
     * 
     * @param gasMixs
     * @param mixName
     * @return
     * @throws UddfException
     */
    private UddfFileMix getUddfMixByName(List<UddfFileMix> gasMixs, String mixName) throws UddfException {
        Iterator<UddfFileMix> it = gasMixs.iterator();
        while (it.hasNext()) {
            UddfFileMix uddfFileMix = it.next();
            if (uddfFileMix.getName().equals(mixName)) {
                return uddfFileMix;
            }
        }
        throw new UddfException("Inexistent Mix name: " + mixName + ". It should be declared on <gasdefinitions> section.");
    }

    private Alarm buildAlarm(UddfFileWayPoint waypoint) {
        Alarm alarm = new Alarm();
        alarm.setValue(waypoint.getAlarm());
        return alarm;
    }

    private PPO2 buildPo2(UddfFileWayPoint waypoint) {
        PPO2 ppo2 = new PPO2();
        ppo2.setValue(UddfUtilsConverter.getInstance().convertToDouble(waypoint.getSetpo2()));
        ppo2.setSensor("Mix");
        return ppo2;
    }

    private Switch buildSwitch(UddfFileWayPoint waypoint, UddfFileGasDefinitions gasDefinitions) throws UddfException {
        Switch switchMix = new Switch();
        UddfFileMix uddfMix = getUddfMix(gasDefinitions.getMixs(), waypoint.getSwitchmix());
        switchMix.setValue(uddfMix.getName());
        return switchMix;
    }

    private Temperature buildTemperature(UddfFileWayPoint waypoint) {
        Temperature temperature = new Temperature();
        temperature.setValue(UddfUtilsConverter.getInstance().convertToDouble(waypoint.getTemperature()));
        return temperature;
    }

    private Depth buildDepth(UddfFileWayPoint waypoint) {
        Depth depth = new Depth();
        depth.setValue(UddfUtilsConverter.getInstance().convertToDouble(waypoint.getDepth()));
        return depth;
    }

    private Time buildTime(UddfFileWayPoint waypoint) {
        Time time = new Time();
        time.setValue(UddfUtilsConverter.getInstance().convertToDouble(waypoint.getDivetime()));
        return time;
    }

}
