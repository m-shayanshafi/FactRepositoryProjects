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
package com.h3xstream.findsecbugs.xss;

import com.h3xstream.findsecbugs.common.InterfaceUtils;
import com.h3xstream.findsecbugs.injection.BasicInjectionDetector;
import com.h3xstream.findsecbugs.taintanalysis.Taint;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.ClassContext;

public class XssServletDetector extends BasicInjectionDetector {

    private static final String XSS_SERVLET_TYPE = "XSS_SERVLET";

    public XssServletDetector(BugReporter bugReporter) {
        super(bugReporter);
        loadConfiguredSinks("xss-servlet.txt", XSS_SERVLET_TYPE);
    }

     @Override
    protected int getPriority(Taint taint) {
        if (!taint.isSafe() && taint.hasTag(Taint.Tag.XSS_SAFE)) {
            return Priorities.LOW_PRIORITY;
        } else if (!taint.isSafe()
                && (taint.hasTag(Taint.Tag.QUOTE_ENCODED) || taint.hasTag(Taint.Tag.APOSTROPHE_ENCODED))
                && taint.hasTag(Taint.Tag.LT_ENCODED)) {
            return Priorities.LOW_PRIORITY;
        } else {
            return super.getPriority(taint);
        }
    }
    
    @Override
    public boolean shouldAnalyzeClass(ClassContext classContext) {
        String className = classContext.getClassDescriptor().getDottedClassName();
        return InterfaceUtils.isSubtype(className, "javax.servlet.http.HttpServlet")
                && !InterfaceUtils.isSubtype(className, XssJspDetector.JSP_PARENT_CLASSES);
    }
}
