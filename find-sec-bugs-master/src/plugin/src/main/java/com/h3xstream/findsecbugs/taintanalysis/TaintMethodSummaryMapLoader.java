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
package com.h3xstream.findsecbugs.taintanalysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TaintMethodSummaryMapLoader {

    public void load(InputStream input, TaintMethodSummaryReceiver receiver) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
        for (;;) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            putFromLine(line, receiver);
        }
    }

    private void putFromLine(String line, TaintMethodSummaryReceiver receiver) throws IOException {
        if (line.startsWith("-")) {
            // for comments or removing summary temporarily
            return;
        }
        String[] tuple = line.split("\\:");
        if (tuple.length != 2) {
            throw new IOException("Line format is not 'method name:summary info'");
        }
        receiver.receiveTaintMethodSummary(tuple[0].trim(), TaintMethodSummary.load(tuple[1]));
    }

    public interface TaintMethodSummaryReceiver {
        void receiveTaintMethodSummary(String fullMethodName, TaintMethodSummary taintMethodSummary);
    }
}
