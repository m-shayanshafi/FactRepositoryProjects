/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: CommUtil.java
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 * 
 * This file is part of JDiveLog.
 * JDiveLog is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * JDiveLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with JDiveLog; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sf.jdivelog.comm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility Class to support different type of Comm APIs, e.g. Java Comm API, RXTX-2.1
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class CommUtil {
    
    private static final Logger LOGGER = Logger.getLogger(CommUtil.class.getName());
    private List<CommPortIdentifier> identifiers;
    private HashSet<CommApi> apis;
    private StringBuffer initProtocol;
    
    
    private CommUtil() {
        initialize();
    }
    
    private void initialize() {
        initProtocol = new StringBuffer();
        identifiers = new ArrayList<CommPortIdentifier>();
        apis = new HashSet<CommApi>();
        
        // try to find java comm api
        writeProtocol(ProtocolClassification.INFO, "Initializing JavaCommAPI Comm Ports");
        try {
            Class<?> javaxCommCommPortIdentifierClass = Class.forName("javax.comm.CommPortIdentifier");
            Class.forName("javax.comm.SerialPort");
            
            // try to find ports supported by comm api
            try {
                Method getPortIdentifiersMethod = javaxCommCommPortIdentifierClass.getDeclaredMethod("getPortIdentifiers", new Class[0]);
                Enumeration <?>javaxCommPorts = (Enumeration<?>)getPortIdentifiersMethod.invoke(null, new Object[0]);
                if (javaxCommPorts != null) {
                    apis.add(CommApi.JAVA_COMM_API);
                    // iterate over javax comm ports
                    while(javaxCommPorts.hasMoreElements()) {
                        Object portIdentifier = javaxCommPorts.nextElement();
                        Method getNameMethod = javaxCommCommPortIdentifierClass.getDeclaredMethod("getName", new Class[0]);
                        String nativeName = (String)getNameMethod.invoke(portIdentifier, new Object[0]);
                        writeProtocol(ProtocolClassification.INFO, "javax.comm Port found: "+nativeName);
                        identifiers.add(new CommPortIdentifier(CommApi.JAVA_COMM_API, nativeName));
                    }
                } else {
                    writeProtocol(ProtocolClassification.WARNING, "javax.comm getPortIdentifiers returned null: No Ports available!");
                }
            } catch (SecurityException e) {
                // could not access necessary methods on comm api
                writeProtocol(ProtocolClassification.WARNING, "javax.comm-Classes not working! Wrong version? JavaCommAPI support disabled!");
            } catch (NoSuchMethodException e) {
                // could not access necessary methods on comm api
                writeProtocol(ProtocolClassification.WARNING, "javax.comm-Classes not working! Wrong version? JavaCommAPI support disabled!");
            } catch (IllegalArgumentException e) {
                // could not access necessary methods on comm api
                writeProtocol(ProtocolClassification.WARNING, "javax.comm-Classes not working! Wrong version? JavaCommAPI support disabled!");
            } catch (IllegalAccessException e) {
                // could not access necessary methods on comm api
                writeProtocol(ProtocolClassification.WARNING, "javax.comm-Classes not working! Wrong version? JavaCommAPI support disabled!");
            } catch (InvocationTargetException e) {
                // could not access necessary methods on comm api
                writeProtocol(ProtocolClassification.WARNING, "javax.comm-Classes not working! Wrong version? JavaCommAPI support disabled!");
            }
        } catch (ClassNotFoundException e) {
            // not all necessary classes of Java Comm API found -> do not use it
            writeProtocol(ProtocolClassification.WARNING, "javax.comm-Classes not found! JavaCommAPI support disabled!");
        } catch (ClassCastException e) {
            // misconfigured system: either rxtx-2.1 or java comm api should be installed
            writeProtocol(ProtocolClassification.ERROR, "javax.comm-Classes could not be loaded! Probably the system is misconfigured! Either install RXTX-2.1 or Java Comm API, but not both!");
        } catch (Throwable e) {
            LOGGER.log(Level.SEVERE, "error getting javax.comm classes", e);
            writeProtocol(ProtocolClassification.ERROR, "javax.comm-Classes could not be loaded! Probably the system is misconfigure! Either install RXTX-2.1 or Java Comm API, but not both! Exception: "+e.toString());
        }
        
        
        // try to find rxtx 2.1
        writeProtocol(ProtocolClassification.INFO, "Initializing RXTX-2.1 Comm Ports");
        try {
            Class<?> gnuIoCommPortIdentifierClass = Class.forName("gnu.io.CommPortIdentifier");
            Class.forName("gnu.io.SerialPort");
            
            // try to find ports supported by rxtx
            try {
                Method getPortIdentifiersMethod = gnuIoCommPortIdentifierClass.getDeclaredMethod("getPortIdentifiers", new Class[0]);
                Enumeration<?> gnuIoCommPorts = (Enumeration<?>)getPortIdentifiersMethod.invoke(null, new Object[0]);
                if (gnuIoCommPorts != null) {
                    apis.add(CommApi.RXTX_2_1);
                    // iterate over rxtx comm ports
                    while(gnuIoCommPorts.hasMoreElements()) {
                        Object portIdentifier = gnuIoCommPorts.nextElement();
                        Method getNameMethod = gnuIoCommPortIdentifierClass.getDeclaredMethod("getName", new Class[0]);
                        String nativeName = (String)getNameMethod.invoke(portIdentifier, new Object[0]);
                        writeProtocol(ProtocolClassification.INFO, "RXTX Port found: "+nativeName);
                        identifiers.add(new CommPortIdentifier(CommApi.RXTX_2_1, nativeName));
                    }
                } else {
                    writeProtocol(ProtocolClassification.WARNING, "RXTX getPortIdentifiers returned null: No Ports available!");
                }
            } catch (SecurityException e) {
                // could not access necessary methods on comm api
                writeProtocol(ProtocolClassification.WARNING, "RXTX-Classes not working! Wrong version? RXTX support disabled!");
            } catch (NoSuchMethodException e) {
                // could not access necessary methods on comm api
                writeProtocol(ProtocolClassification.WARNING, "RXTX-Classes not working! Wrong version? RXTX support disabled!");
            } catch (IllegalArgumentException e) {
                // could not access necessary methods on comm api
                writeProtocol(ProtocolClassification.WARNING, "RXTX-Classes not working! Wrong version? RXTX support disabled!");
            } catch (IllegalAccessException e) {
                // could not access necessary methods on comm api
                writeProtocol(ProtocolClassification.WARNING, "RXTX-Classes not working! Wrong version? RXTX support disabled!");
            } catch (InvocationTargetException e) {
                // could not access necessary methods on comm api
                writeProtocol(ProtocolClassification.WARNING, "RXTX-Classes not working! Wrong version? RXTX support disabled!");
            }
        } catch (ClassNotFoundException e) {
            // not all necessary classes of Java Comm API found -> do not use it
            writeProtocol(ProtocolClassification.WARNING, "RXTX-Classes not found! RXTX support disabled!");
        } catch (ClassCastException e) {
            // misconfigured system: either rxtx-2.1 or java comm api should be installed
            writeProtocol(ProtocolClassification.ERROR, "RXTX-Classes could not be loaded! Probably the system is misconfigured! Either install RXTX-2.1 or Java Comm API, but not both!");
        } catch (Throwable e) {
            LOGGER.log(Level.SEVERE, "error getting RXTX-classes", e);
            writeProtocol(ProtocolClassification.ERROR, "RXTX-Classes could not be loaded! Probably the system is misconfigured! Either install RXTX-2.1 or Java Comm API, but not both! Exception: "+e.toString());
        }
        writeProtocol(ProtocolClassification.INFO, "CommUtil initialization finished.");
        
        // write out log.
        LOGGER.info(initProtocol.toString());
    }
    
    public SerialPort open(CommPortIdentifier identifier) throws PortNotFoundException, PortInUseException {
        if (identifier.getApi() == CommApi.JAVA_COMM_API) {
            return JavaCommSerialPort.open(identifier, "JDiveLog", 100);
        } else if (identifier.getApi() == CommApi.RXTX_2_1) {
            return RxTx21SerialPort.open(identifier, "JDiveLog", 100);
        }
        throw new RuntimeException("Invalid Port Type "+identifier.getApi());
    }
    
    public boolean isSupported(CommApi api) {
        return apis.contains(api);
    }
    
    public Iterator<CommPortIdentifier> getPortIdentifiers() {
        return identifiers.iterator();
    }
    
    public String getInitProtocol() {
        return initProtocol.toString();
    }
    
    private void writeProtocol(ProtocolClassification classification, String message) {
        if (classification == ProtocolClassification.ERROR) {
            initProtocol.append("  ERROR: ");
        } else if (classification == ProtocolClassification.WARNING) {
            initProtocol.append("WARNING: ");
        } else if (classification == ProtocolClassification.INFO) {
            initProtocol.append("   INFO: ");
        }
        initProtocol.append(message);
        initProtocol.append("\n");
    }
    
    //
    // static methods
    //

    private static CommUtil instance = null;

    public static CommUtil getInstance() {
        if (instance == null) {
            instance = new CommUtil();
        }
        return instance;
    }
    
    //
    // inner classes and types
    //

    public static enum CommApi {
        JAVA_COMM_API,
        RXTX_2_1;
    }
    
    private static enum ProtocolClassification {
        ERROR, WARNING, INFO; 
    }

}
