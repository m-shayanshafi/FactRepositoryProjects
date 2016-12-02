/*
 * Copyright (c) 2002-2004 LWJGL Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.lwjgl.opengl;

import java.awt.Canvas;
import java.nio.ByteBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;

/**
 * $Id: AWTSurfaceLock.java,v 1.4 2005/05/04 20:59:34 cix_foo Exp $
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision: 1.4 $
 */
final class AWTSurfaceLock {
	private final static int WAIT_DELAY_MILLIS = 100;
	
	private final ByteBuffer lock_buffer;

	public AWTSurfaceLock() {
		lock_buffer = createHandle();
	}
	private static native ByteBuffer createHandle();

	public ByteBuffer lockAndGetHandle(Canvas canvas) throws LWJGLException {
		while (!lockAndInitHandle(lock_buffer, canvas)) {
			LWJGLUtil.log("Could not get drawing surface info, retrying...");
			try {
				Thread.sleep(WAIT_DELAY_MILLIS);
			} catch (InterruptedException e) {
				LWJGLUtil.log("Interrupted while retrying: " + e);
			}
		}
		return lock_buffer;
	}
	private static native boolean lockAndInitHandle(ByteBuffer lock_buffer, Canvas canvas) throws LWJGLException;

	protected void unlock() throws LWJGLException {
		nUnlock(lock_buffer);
	}
	private static native void nUnlock(ByteBuffer lock_buffer) throws LWJGLException;
}
