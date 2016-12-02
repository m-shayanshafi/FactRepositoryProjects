/**
 * Find Security Bugs
 * Copyright (c) Philippe Arteau, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */
package com.h3xstream.findsecbugs.crypto;

import com.h3xstream.findsecbugs.common.ByteCode;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.*;
import edu.umd.cs.findbugs.ba.constant.ConstantDataflow;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.*;

import java.util.Iterator;

/**
 * Similar to the blowfish key size detector
 */
public class InsufficientKeySizeRsaDetector implements Detector {

    private static final boolean DEBUG = false;
    private static final String RSA_KEY_SIZE_TYPE = "RSA_KEY_SIZE";

    private BugReporter bugReporter;

    public InsufficientKeySizeRsaDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        JavaClass javaClass = classContext.getJavaClass();

        Method[] methodList = javaClass.getMethods();

        for (Method m : methodList) {

            try {
                analyzeMethod(m, classContext);
            } catch (CFGBuilderException e) {
            } catch (DataflowAnalysisException e) {
            }
        }
    }

    private void analyzeMethod(Method m, ClassContext classContext) throws CFGBuilderException, DataflowAnalysisException {

        //Conditions that needs to fill to identify the vulnerability
        boolean createRsaKeyGen = false;
        boolean initializeWeakKeyLength = false;
        Location locationWeakness = null;

        ConstantPoolGen cpg = classContext.getConstantPoolGen();
        CFG cfg = classContext.getCFG(m);

        for (Iterator<Location> i = cfg.locationIterator(); i.hasNext(); ) {
            Location location = i.next();

            Instruction inst = location.getHandle().getInstruction();
//            ByteCode.printOpCode(inst, cpg);

            if (inst instanceof INVOKESTATIC) { //KeyPairGenerator.getInstance is called
                INVOKESTATIC invoke = (INVOKESTATIC) inst;
                if ("java.security.KeyPairGenerator".equals(invoke.getClassName(cpg)) && "getInstance".equals(invoke.getMethodName(cpg))) {
                    String value = ByteCode.getConstantLDC(location.getHandle().getPrev(), cpg, String.class);
                    if (value != null && value.toUpperCase().startsWith("RSA")) {
                        createRsaKeyGen = true;
                    }
                }
            } else if (inst instanceof INVOKEVIRTUAL) { //KeyPairGenerator.initialize
                INVOKEVIRTUAL invoke = (INVOKEVIRTUAL) inst;
                if ("java.security.KeyPairGenerator".equals(invoke.getClassName(cpg)) && "initialize".equals(invoke.getMethodName(cpg))) {
                    Number n = null;
                    //init() with one parameter
                    if ("(I)V".equals(invoke.getSignature(cpg))) {
                        n = ByteCode.getPushNumber(location.getHandle().getPrev());
                    }
                    //init() with a second parameter an instance of SecureRandom
                    else if ("(ILjava/security/SecureRandom;)V".equals(invoke.getSignature(cpg))) {

                        SIPUSH push = ByteCode.getPrevInstruction(location.getHandle(), SIPUSH.class);
                        if (push != null) {
                            n = push.getValue();
                        }
                    }

                    if (n != null && n.intValue() < 1024) {
                        initializeWeakKeyLength = true;
                        locationWeakness = location;
                    }
                }
            } else if (inst instanceof INVOKESPECIAL) { // new RSAKeyGenParameterSpec() is called
                INVOKESPECIAL invoke = (INVOKESPECIAL) inst;
                if ("java.security.spec.RSAKeyGenParameterSpec".equals(invoke.getClassName(cpg)) && "<init>".equals(invoke.getMethodName(cpg))) {
                    Number n = null;
                    //init() with one parameter
                    if ("(ILjava/math/BigInteger;)V".equals(invoke.getSignature(cpg))) {
                        SIPUSH push = ByteCode.getPrevInstruction(location.getHandle(), SIPUSH.class);
                        if (push != null) {
                            n = push.getValue();
                        }
                    }

                    if (n != null && n.intValue() < 1024) {
                        initializeWeakKeyLength = true;
                        locationWeakness = location;
                    }
                }
            }
        }

        //Both condition have been found in the same method
        if (createRsaKeyGen && initializeWeakKeyLength) {
            JavaClass clz = classContext.getJavaClass();
            bugReporter.reportBug(new BugInstance(this, RSA_KEY_SIZE_TYPE, Priorities.NORMAL_PRIORITY) //
                    .addClass(clz)
                    .addMethod(clz, m)
                    .addSourceLine(classContext, m, locationWeakness));
        }
    }

    @Override
    public void report() {
    }
}
