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
package com.h3xstream.findsecbugs.injection;

import com.h3xstream.findsecbugs.taintanalysis.TaintLocation;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.Detector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.SourceLineAnnotation;
import edu.umd.cs.findbugs.StringAnnotation;
import edu.umd.cs.findbugs.ba.ClassContext;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.InstructionHandle;

/**
 * Used to represent location of a taint sink
 * 
 * @author David Formanek (Y Soft Corporation, a.s.)
 */
public class InjectionSink {
    
    private final Detector detector;
    private final String bugType;
    private final int originalPriority;
    private final ClassContext classContext;
    private final Method method;
    private final InstructionHandle instructionHandle;
    private final String sinkMethod;
    private static final int UNKNOWN_SINK_PRIORITY = Integer.MAX_VALUE;
    private int sinkPriority = UNKNOWN_SINK_PRIORITY;
    private final List<SourceLineAnnotation> lines = new LinkedList<SourceLineAnnotation>();

    public InjectionSink(Detector detector, String bugType, int originalPriority,
            ClassContext classContext, Method method, InstructionHandle instructionHandle,
            String sinkMethod) {
        Objects.requireNonNull(detector, "detector");
        Objects.requireNonNull(bugType, "bugType");
        Objects.requireNonNull(classContext, "classContext");
        Objects.requireNonNull(method, "method");
        Objects.requireNonNull(instructionHandle, "instructionHandle");
        this.detector = detector;
        this.bugType = bugType;
        this.originalPriority = originalPriority;
        this.classContext = classContext;
        this.method = method;
        this.instructionHandle = instructionHandle;
        this.sinkMethod = (sinkMethod == null) ? "unknown" : sinkMethod;
    }

    public boolean updateSinkPriority(int priority) {
        if (priority < sinkPriority) {
            sinkPriority = priority;
            return true;
        }
        return false;
    }
    
    public void addLine(SourceLineAnnotation line) {
        Objects.requireNonNull(line, "line");
        lines.add(line);
    }
    
    public void addLines(Collection<TaintLocation> locations) {
        Objects.requireNonNull(detector, "locations");
        for (TaintLocation location : locations) {
            lines.add(SourceLineAnnotation.fromVisitedInstruction(
                location.getMethodDescriptor(), location.getPosition()));
        }
    }
    
    public BugInstance generateBugInstance(boolean taintedInsideMethod) {
        BugInstance bug = new BugInstance(detector, bugType, originalPriority);
        bug.addClassAndMethod(classContext.getJavaClass(), method);
        bug.addSourceLine(SourceLineAnnotation.fromVisitedInstruction(classContext, method, instructionHandle));
        addMessage(bug, "Sink method", sinkMethod);
        if (sinkPriority != UNKNOWN_SINK_PRIORITY) {
            // higher priority is represented by lower integer
            if (sinkPriority < originalPriority) {
                bug.setPriority(sinkPriority);
                addMessage(bug, "Method usage", "with tainted arguments detected");
            } else if (sinkPriority > originalPriority) {
                bug.setPriority(Priorities.LOW_PRIORITY);
                addMessage(bug, "Method usage", "detected only with safe arguments");
            }
        } else if (!taintedInsideMethod) {
            addMessage(bug, "Method usage", "not detected");
        }
        Collections.sort(lines);
        SourceLineAnnotation annotation = null;
        for (Iterator<SourceLineAnnotation> it = lines.iterator(); it.hasNext();) {
            SourceLineAnnotation prev = annotation;
            annotation = it.next();
            if (prev != null && prev.getClassName().equals(annotation.getClassName())
                    && prev.getStartLine() == annotation.getStartLine()) {
                // keep only one annotation per line
                it.remove();
            }
        }
        for (SourceLineAnnotation sourceLine : lines) {
            bug.addSourceLine(sourceLine);
        }
        return bug;
    }
    
    private void addMessage(BugInstance bug, String role, String text) {
        StringAnnotation stringAnnotation = new StringAnnotation(text);
        stringAnnotation.setDescription(role);
        bug.add(stringAnnotation);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof InjectionSink)) {
            return false;
        }
        final InjectionSink other = (InjectionSink) obj;
        // include only attributes that cannot change after object construction
        return this.bugType.equals(other.bugType)
                && this.originalPriority == other.originalPriority
                && this.classContext.getClassDescriptor().equals(other.classContext.getClassDescriptor())
                && this.method.getName().equals(other.method.getName())
                && this.method.getSignature().equals(other.method.getSignature())
                && this.method.getReturnType().equals(other.method.getReturnType())
                && this.instructionHandle.getInstruction().getOpcode() == other.instructionHandle.getInstruction().getOpcode()
                && this.instructionHandle.getPosition() == other.instructionHandle.getPosition();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + bugType.hashCode();
        hash = 67 * hash + originalPriority;
        hash = 67 * hash + classContext.getClassDescriptor().hashCode();
        hash = 67 * hash + method.getName().hashCode();
        hash = 67 * hash + method.getSignature().hashCode();
        hash = 67 * hash + method.getReturnType().hashCode();
        hash = 67 * hash + instructionHandle.getInstruction().getOpcode();
        hash = 67 * hash + instructionHandle.getPosition();
        return hash;
    }
}
