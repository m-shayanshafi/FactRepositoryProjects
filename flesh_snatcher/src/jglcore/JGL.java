//	Copyright 2008 Nicolas Devere
//
//	This file is part of JavaGL.
//
//	JavaGL is free software; you can redistribute it and/or modify
//	it under the terms of the GNU General Public License as published by
//	the Free Software Foundation; either version 2 of the License, or
//	(at your option) any later version.
//
//	JavaGL is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//
//	You should have received a copy of the GNU General Public License
//	along with JavaGL; if not, write to the Free Software
//	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

package jglcore;

import java.awt.Component;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Color;
import java.util.HashMap;
import java.util.Stack;


/**
 * 3D display system. Based on the OpenGL model, it provides static methods to : <br><br>
 * 
 * - Manage Java AWT/Swing Components display (multi-screening, double-buffer)<br>
 * - Manage render parameters (perspective, clipping planes, cull-facing)<br>
 * - Manipulate spatial transformations<br>
 * - Manage materials parameters (color, shininess, full/wire frame faces)<br>
 * - Manage lighting<br>
 * - Display 3D primitives (triangles and lines)<br><br>
 * 
 * WARNING : To keep maximum performances, the system doesn't support multi-threading. 
 * Operations must be sequentially called, including display Components selections.
 * 
 * @author Nicolas Devere
 *
 */
public final class JGL {
	
	
	//*****************************************************//
	//****************    STATE OBJECTS    ****************//
	//*****************************************************//

	
	/**
	 * Display objects
	 */
	private static Image image;
	private static Graphics graphics;
	private static Component component;
	private static Graphics componentGraphics;
	private static HashMap componentsList;
	private static Color bkgColor;
	
	/**
	 * Represents a double-buffered display component.
	 * @author Nicolas Devere
	 */
	private static class Display {
		
		/** The component's graphics */
		public Graphics c_graphics;
		
		/** The component's second display buffer */
		public Image c_image;
		
		/** The second display buffer's graphics */
		public Graphics c_imageGraphics;
		
		/**
		 * Constructs a double-buffered display component.
		 * @param arg : the AWT/Swing component
		 */
		public Display(Component arg) {
			c_graphics = arg.getGraphics();
			c_image = arg.createImage(arg.getWidth(), arg.getHeight());
			c_imageGraphics = c_image.getGraphics();
		}
	}
	
	
	
	/**
	 * Matrix manipulation objects
	 */
	private static JGL_3DMatrix matrix;
	private static JGL_3DMatrix matrixSave;
	private static JGL_3DMatrix matrixCopy;
	private static int STOCKED_MATRIX = 8;
	private static Stack matrixStock;
	private static Stack matrixStack;
	

	
	/**
	 * Screen parameters
	 */
	private static int width;
	private static int height;
	private static int centerX;
	private static int centerY;
	
	
	
	/**
	 * 3D management parameters 
	 */
	private static float focal;
	private static boolean is_cullFace;
	private static float cullFaceOrder;
	private static float screenClip;
	
	private static float[] xVertex = new float[9];
	private static float[] yVertex = new float[9];
	private static float[] zVertex = new float[9];
	private static float[] xClip = new float[9];
	private static float[] yClip = new float[9];
	private static float[] zClip = new float[9];
	private static int nbVertex;
	
	private static float[] aPlane = new float[6];
	private static float[] bPlane = new float[6];
	private static float[] cPlane = new float[6];
	private static float[] dPlane = new float[6];
	private static float[] xPlane = new float[6];
	private static float[] yPlane = new float[6];
	private static float[] zPlane = new float[6];
	private static int nbPlanes;
	
	
	
	/**
	 * Materials parameters
	 */
	private static boolean is_filled;
	private static boolean is_light;
	private static float xLight;
	private static float yLight;
	private static float zLight;
	private static float shine;
	
	
	
	/**
	 * 2D managementt parameters
	 */
	private static int[] x2D = new int[13];
	private static int[] y2D = new int[13];
	private static int[] x2DClip = new int[13];
	private static int[] y2DClip = new int[13];
	private static int nbApex;
	
	
	
	//***************************************************//
	//**************    STATE CONSTANTS    **************//
	//***************************************************//
	
	/**
	 * Specifies that a triangle's front side is the side 
	 * where points are in the counter-clockwise order (default order).
	 */
	public static final int CCW = 0;
	
	/**
	 * Specifies that a triangle's front side is the side 
	 * where points are in the clockwise order.
	 */
	public static final int CW = 1;
	
	/** Axis order for rotations : X, then Y, then Z. */
	public static final int XYZ = 10;
	
	/** Axis order for rotations : X, then Z, then Y. */
	public static final int XZY = 11;
	
	/** Axis order for rotations : Y, then X, then Z. */
	public static final int YXZ = 12;
	
	/** Axis order for rotations : Y, then Z, then X. */
	public static final int YZX = 13;
	
	/** Axis order for rotations : Z, then X, then Y. */
	public static final int ZXY = 14;
	
	/** Axis order for rotations : Z, then Y, then X. */
	public static final int ZYX = 15;
	
	
	
	
	
	//**************************************************//
	//**********    DEFAULT INITIALIZATION    **********//
	//**************************************************//
	
	static {
		componentsList = new HashMap();
		bkgColor = new Color(0, 0, 0);
		matrix = new JGL_3DMatrix();
		matrixCopy = new JGL_3DMatrix();
		matrixStack = new Stack();
		matrixStock = new Stack();
		for(int i=0; i<STOCKED_MATRIX; i++)
			matrixStock.push(new JGL_3DMatrix());
		
		setPerspective(70f);
		setScreenClip(-1f);
		nbPlanes = 0;
		
		setCullFacing(false);
		setFaceOrder(CCW);
		setSolidFace(true);
		setLighting(false);
		xLight = yLight = zLight = 0f;
		setShininess(50f);
	}
	
	
	
	
	
	//*************************************************//
	//*************    STATIC METHODS    **************//
	//*************************************************//
	
	
	/**
	 * Sets the specified Java AWT/Swing Component as current display target of the system.
	 * 
	 * @param arg : the AWT/Swing Component
	 */
	public static final void setDisplayTarget(Component arg) {
		
		if(!componentsList.containsKey(arg))
			componentsList.put(arg, new Display(arg));
		
		Display display = (Display)componentsList.get(arg);
		if(display == null)
			return;
		
		component = arg;
		componentGraphics = display.c_graphics;
		image = display.c_image;
		graphics = display.c_imageGraphics;
		
		width = component.getWidth();
		height = component.getHeight();
		centerX = width / 2;
		centerY = height / 2;
	}
	
	
	/**
	 * Returns the current AWT/Swing component display target.
	 * 
	 * @return the current AWT/Swing component display target
	 */
	public static final Component getDisplayTarget() {
		return component;
	}
	
	
	
	/**
	 * Clears the current display buffer.
	 */
	public static final void clearBuffer() {
		graphics.setColor(bkgColor);
		graphics.fillRect(0, 0, width, height);
	}
	
	
	/**
	 * Flushes display buffer on the current target.
	 */
	public static final void swapBuffers() {
		componentGraphics.drawImage(image, 0, 0, component);
	}
	
	
	/**
	 * Returns the current display buffer, or null if no display component is set.
	 * @return the current display buffer, or null
	 */
	public static final Graphics getBuffer() {
		return graphics;
	}
	
	
	
	/**
	 * Returns the current display target's width (in pixels).
	 * 
	 * @return the current display target's width
	 */
	public static final int getWidth() {
		return width;
	}
	
	/**
	 * Returns the current display target's height (in pixels).
	 * 
	 * @return the current display target's height
	 */
	public static final int getHeight() {
		return height;
	}
	
	
	
	/**
	 * Sets the perspective with the specified vision angle. 
	 * The angle (in degrees) is the one between the observer's eye, the top-left screen corner 
	 * and the bottom-right screen corner. It must be between 1 and 179.
	 * 
	 * @param angle : the vision angle
	 */
	public static final void setPerspective(float angle) {
		if(angle<1f)
			angle = 1f;
		if(angle>179f)
			angle = 179f;
		
		float alpha = (1f + JGL_Math.cos(angle)) / (1f - JGL_Math.cos(angle));
		focal = (float)Math.sqrt( ((centerX * centerX) + (centerY * centerY)) * alpha );
	}
	
	
	
	/**
	 * Returns the current 3D->2D focal parameter.
	 * 
	 * @return the current focal
	 */
	public static final float getFocal() {
		return focal;
	}
	

	/**
	 * Sets the abstract screen plane depth (-1 by default). 
	 * It must be a negative value.
	 * 
	 * @param arg : the screen plane depth
	 */
	public static final void setScreenClip(float arg) {
		if(arg > -0.001f)
			arg = -0.001f;
		screenClip = arg;
	}
	
	
	/**
	 * Returns the current screen plane depth.
	 * 
	 * @return the screen plane depth
	 */
	public static final float getScreenClip() {
		return screenClip;
	}
	
	
	
	
	
	/**
	 * Adds a 3D clipping plane. A plane is defined by 3 points.<br>
	 * The front side of the plane is the one where 
	 * the points are disposed in the counter clockwise order.<br>
	 * 6 planes max can be added.<br><br>
	 * 
	 * @param x1 : first point's X value
	 * @param y1 : first point's Y value
	 * @param z1 : first point's Z value
	 * 
	 * @param x2 : second point's X value
	 * @param y2 : second point's Y value
	 * @param z2 : second point's Z value
	 * 
	 * @param x3 : third point's X value
	 * @param y3 : third point's Y value
	 * @param z3 : third point's Z value
	 */
	public static final void addClipPlane(	float x1, float y1, float z1,
											float x2, float y2, float z2,
											float x3, float y3, float z3 ) {
		
		if(nbPlanes>6) return;
		
		float xv1 = x2 - x1; float yv1 = y2 - y1; float zv1 = z2 - z1;
		float xv2 = x3 - x1; float yv2 = y3 - y1; float zv2 = z3 - z1;
		
		float xn = (yv1 * zv2) - (zv1 * yv2);
		float yn = (zv1 * xv2) - (xv1 * zv2);
		float zn = (xv1 * yv2) - (yv1 * xv2);
		
		float norm = 1f / (float)Math.sqrt( (xn*xn) + (yn*yn) + (zn*zn) );
		xn *= norm;
		yn *= norm;
		zn *= norm;
		
		aPlane[nbPlanes] = xn;
		bPlane[nbPlanes] = yn;
		cPlane[nbPlanes] = zn;
		dPlane[nbPlanes] = - ( (xn*x1) + (yn*y1) + (zn*z1) );
		
		xPlane[nbPlanes] = x1;
		yPlane[nbPlanes] = y1;
		zPlane[nbPlanes] = z1;
		
		nbPlanes++;
	}
	
	
	
	
	/**
	 * Removes all the clipping planes.
	 */
	public static final void clearClipPlanes() {
		nbPlanes = 0;
	}
	
	
	
	
	/**
	 * Resets the current matrix to identity.
	 */
	public static final void identity() {
		matrix.identity();
	}
	
	/**
	 * Applies a translation to the current matrix.
	 * 
	 * @param x : the X axis translation value
	 * @param y : the Y axis translation value
	 * @param z : the Z axis translation value
	 */
	public static final void translate(float x, float y, float z) {
		matrix.translate(x, y, z);
	}
	
	/**
	 * Applies a rotation (in degrees) to the current matrix 
	 * around the specified axis.
	 * 
	 * @param angle : the rotation angle (in degrees)
	 * @param x : the rotation axis's X value
	 * @param y : the rotation axis's Y value
	 * @param z : the rotation axis's Z value
	 * @param normalizedAxis : sets if the axis is normalized (witch accelerates compute), or not
	 */
	public static final void rotate(float angle, float x, float y, float z, boolean normalizedAxis) {
		matrix.rotate(angle, x, y, z, normalizedAxis);
	}
	
	/**
	 * Applies a rotation to the current matrix with the specified Euler angles, 
	 * in the chosen order (the angles order is given by constants from 
	 * the <code>JGL</code> class. 
	 * Ex : <code>JGL.ZXY</code> means that the matrix is rotated around Z, then X, then Y).
	 * 
	 * @param angleX : the rotation angle around the X axis
	 * @param angleY : the rotation angle around the Y axis
	 * @param angleZ : the rotation angle around the Z axis
	 * @param order : the 3 axis rotations order
	 */
	public static final void rotate(float angleX, float angleY, float angleZ, int order) {
		matrix.rotate(angleX, angleY, angleZ, order);
	}
	
	/**
	 * Applies a scale transformation on 3 axis to the current matrix.
	 * 
	 * @param x : the X axis scale value
	 * @param y : the Y axis scale value
	 * @param z : the Z axis scale value
	 */
	public static final void scale(float x, float y, float z) {
		matrix.scale(x, y, z);
	}
	
	
	/**
	 * Right-multiplies the current matrix by the specified matrix.
	 * 
	 * @param m : the matrix to multiply
	 */
	public static final void multiplyRight(JGL_3DMatrix m) {
		matrix.multiplyRight(m);
	}
	
	
	/**
	 * Left-multiplies the current matrix by the specified matrix.
	 * 
	 * @param m : the matrix to multiply
	 */
	public static final void multiplyLeft(JGL_3DMatrix m) {
		matrix.multiplyLeft(m);
	}
	
	
	/**
	 * Pushes the current matrix in the matrix stack.
	 */
	public static final void pushMatrix() {
		if(!matrixStock.isEmpty()) {
			matrixSave = (JGL_3DMatrix)matrixStock.pop();
			matrixSave.assign(matrix);
		}
		else
			matrixSave = new JGL_3DMatrix(matrix);
		
		matrixStack.push(matrixSave);
	}
	
	/**
	 * Restores the last pushed matrix as the current matrix.
	 */
	public static final void popMatrix() {
		if(!matrixStack.isEmpty()) {
			matrixStock.push(matrix);
			matrix = (JGL_3DMatrix)matrixStack.pop();
		}
	}
	
	
	/**
	 * Returns a copy of the current matrix.
	 * 
	 * @return the current transformation matrix (copy instance)
	 */
	public static final JGL_3DMatrix getMatrix() {
		matrixCopy.assign(matrix);
		return matrixCopy;
	}
	
	
	/**
	 * Sets the cull-facing on / off.
	 * 
	 * @param arg : the cull-facing state
	 */
	public static final void setCullFacing(boolean arg) {
		is_cullFace = arg;
	}
	
	
	
	/**
	 * Returns if the cull-facing is on or off.
	 * 
	 * @return the cull-facing state
	 */
	public static final boolean getCullFacing() {
		return is_cullFace;
	}
	
	
	/**
	 * Sets the points order of a 3D triangle.<br>
	 * <code>JGL.CCW</code> : The front side of the face is the side where 
	 * its points are in the counter-clockwise order (by default order).<br>
	 * <code>JGL.CW</code> : The front side of the face is the side where 
	 * its points are in the clockwise order.
	 * 
	 * @param order : the points order : <code>JGL.CCW</code> or <code>JGL.CW</code>
	 */
	public static final void setFaceOrder(int order) {
		if(order == CCW)
			cullFaceOrder = 1f;
		if(order == CW)
			cullFaceOrder = -1f;
	}
	
	
	/**
	 * Sets triangles's display mode : solid or wire frame.
	 * 
	 * @param arg : display mode (true : solid display, false : wire frame display)
	 */
	public static final void setSolidFace(boolean arg) {
		is_filled = arg;
	}
	
	
	/**
	 * Returns the current triangles's display mode : solid or wire frame.
	 * 
	 * @return boolean (true : solid display, false : wire frame display)
	 */
	public static final boolean getSolidFace() {
		return is_filled;
	}
	
	
	
	/**
	 * Sets the lighting on / off.
	 * 
	 * @param arg : the lighting state
	 */
	public static final void setLighting(boolean arg) {
		is_light = arg;
	}
	
	
	/**
	 * Returns if the lighting is on or off.
	 * 
	 * @return the lighting state
	 */
	public static final boolean getLighting() {
		return is_light;
	}
	
	
	/**
	 * Sets the light position (defined in the current matrix).
	 * 
	 * @param x : light X position
	 * @param y : light Y position
	 * @param z : light Z position
	 */
	public static final void setLightPosition(float x, float y, float z) {
		xLight = (matrix.m11*x) + (matrix.m12*y) + (matrix.m13*z) + (matrix.m14);
		yLight = (matrix.m21*x) + (matrix.m22*y) + (matrix.m23*z) + (matrix.m24);
		zLight = (matrix.m31*x) + (matrix.m32*y) + (matrix.m33*z) + (matrix.m34);
	}
	
	
	
	/**
	 * Sets the current display color.
	 * 
	 * @param arg : the color to set
	 */
	public static final void setColor(Color arg) {
		graphics.setColor(arg);
	}
	
	
	/**
	 * Returns the current display color.
	 * 
	 * @return the current color
	 */
	public static final Color getColor() {
		return graphics.getColor();
	}
	
	
	/**
	 * Sets the color witch fills the display buffer 
	 * at the <code>JGL.clearBuffer()</code> method call.
	 * 
	 * @param background : the background color
	 */
	public static final void setBackground(Color background) {
		bkgColor = background;
	}
	
	
	/**
	 * Returns the current background color.
	 * 
	 * @return the current background color
	 */
	public static final Color getBackground() {
		return bkgColor;
	}
	
	
	
	/**
	 * Sets the current shininess percentage (between 0 and 100). 
	 * Shininess effect is available only when the lighting is on (see <code>JGL.setLighting()</code> ).
	 * 
	 * @param percentage : the shininess percentage
	 */
	public static final void setShininess(float percentage) {
		if(percentage < 0f)
			percentage = 0f;
		
		if(percentage >100f)
			percentage = 100f;
		
		shine = percentage * 0.01f;
	}
	
	
	
	/**
	 * Returns the current shininess percentage.
	 * 
	 * @return the shininess percentage
	 */
	public static final float getShininess() {
		return shine * 100f;
	}
	
	
	
	
	
	
	/**
	 * Displays a 3D line with its 2 points coordinates.
	 * 
	 * @param x1 : first point's X value
	 * @param y1 : first point's Y value
	 * @param z1 : first point's Z value
	 * 
	 * @param x2 : second point's X value
	 * @param y2 : second point's Y value
	 * @param z2 : second point's Z value
	 */
	public static final void displayLine(	float x1, float y1, float z1,
											float x2, float y2, float z2) {
		
		//*******************************************
		//*	VERTEX TRANSFORMATION BY CURRENT MATRIX *
		//*******************************************
		
		// first point
		xVertex[0] = (matrix.m11*x1) + (matrix.m12*y1) + (matrix.m13*z1) + (matrix.m14);
		yVertex[0] = (matrix.m21*x1) + (matrix.m22*y1) + (matrix.m23*z1) + (matrix.m24);
		zVertex[0] = (matrix.m31*x1) + (matrix.m32*y1) + (matrix.m33*z1) + (matrix.m34);
				
		// second point
		xVertex[1] = (matrix.m11*x2) + (matrix.m12*y2) + (matrix.m13*z2) + (matrix.m14);
		yVertex[1] = (matrix.m21*x2) + (matrix.m22*y2) + (matrix.m23*z2) + (matrix.m24);
		zVertex[1] = (matrix.m31*x2) + (matrix.m32*y2) + (matrix.m33*z2) + (matrix.m34);
		
		nbVertex = 2;
		
		display();
		
	}
	
	
	
	/**
	 * Displays a 3D triangle with its 3 points coordinates.
	 * 
	 * @param x1 : first point's X value
	 * @param y1 : first point's Y value
	 * @param z1 : first point's Z value
	 * 
	 * @param x2 : second point's X value
	 * @param y2 : second point's Y value
	 * @param z2 : second point's Z value
	 * 
	 * @param x3 : third point's X value
	 * @param y3 : third point's Y value
	 * @param z3 : third point's Z value
	 */
	public static final void displayTriangle(	float x1, float y1, float z1,
												float x2, float y2, float z2,
												float x3, float y3, float z3) {
		
		//*******************************************
		//*	VERTEX TRANSFORMATION BY CURRENT MATRIX *
		//*******************************************
		
		// first point
		xVertex[0] = (matrix.m11*x1) + (matrix.m12*y1) + (matrix.m13*z1) + (matrix.m14);
		yVertex[0] = (matrix.m21*x1) + (matrix.m22*y1) + (matrix.m23*z1) + (matrix.m24);
		zVertex[0] = (matrix.m31*x1) + (matrix.m32*y1) + (matrix.m33*z1) + (matrix.m34);
				
		// second point
		xVertex[1] = (matrix.m11*x2) + (matrix.m12*y2) + (matrix.m13*z2) + (matrix.m14);
		yVertex[1] = (matrix.m21*x2) + (matrix.m22*y2) + (matrix.m23*z2) + (matrix.m24);
		zVertex[1] = (matrix.m31*x2) + (matrix.m32*y2) + (matrix.m33*z2) + (matrix.m34);
		
		// third point
		xVertex[2] = (matrix.m11*x3) + (matrix.m12*y3) + (matrix.m13*z3) + (matrix.m14);
		yVertex[2] = (matrix.m21*x3) + (matrix.m22*y3) + (matrix.m23*z3) + (matrix.m24);
		zVertex[2] = (matrix.m31*x3) + (matrix.m32*y3) + (matrix.m33*z3) + (matrix.m34);
		
		nbVertex = 3;

		
		//**************************************************
		//********** TESTS CULL-FACING AND LIGHT ***********
		//**************************************************
		
		if(is_cullFace || is_light) {
			
			//	Computes the triangle normal.
			float xu = xVertex[1] - xVertex[0];
			float yu = yVertex[1] - yVertex[0];
			float zu = zVertex[1] - zVertex[0];
			float xv = xVertex[2] - xVertex[0];
			float yv = yVertex[2] - yVertex[0];
			float zv = zVertex[2] - zVertex[0];
			
			float a = (yu*zv) - (yv*zu);
			float b = (zu*xv) - (zv*xu);
			float c = (xu*yv) - (xv*yu);
			
			//	Tests if the triangle shows its right side.
			if(is_cullFace)
				if( ((a*xVertex[0]) + (b*yVertex[0]) + (c*zVertex[0])) * cullFaceOrder >= 0) return;
			
			//	Computes the triangle color according to the light.
			if(is_light) {
				
				float norm = 1f / (float)Math.sqrt(((a*a) + (b*b) + (c*c)));
				a = a*norm;
				b = b*norm;
				c = c*norm;
				
				float xl = (3f * xLight) - xVertex[0] - xVertex[1] - xVertex[2];
				float yl = (3f * yLight) - yVertex[0] - yVertex[1] - yVertex[2];
				float zl = (3f * zLight) - zVertex[0] - zVertex[1] - zVertex[2];
				
				norm = 1f / (float)Math.sqrt(((xl*xl) + (yl*yl) + (zl*zl)));
				xl = xl*norm;
				yl = yl*norm;
				zl = zl*norm;
				
				float scal = (xl*a) + (yl*b) + (zl*c);
				if(scal<0f)
					scal = 0f;
				
				//	Color compute :
				//	color * [shininess + ( (1-shininess) * scal ) ]
				float ratio = (1f - shine) + (shine * scal);
				graphics.setColor(new Color((int)(graphics.getColor().getRed() * ratio), 
											(int)(graphics.getColor().getGreen() * ratio), 
											(int)(graphics.getColor().getBlue() * ratio)));
			}
		}
		
		display();
	}
	
		
	/**
	 * 3D shape display method.
	 */
	private static final void display() {
		
		//**********************************************************
		//***** COMPUTES THE POLYGON SPLIT BY THE SCREEN PLANE *****
		//**********************************************************
		
		int h; int i; int j; int nbClip;
		float xvi;
		float yvi;
		float zvi;
		
		float xvj;
		float yvj;
		float zvj;
		
		float t;
		
		i=0; j=1; nbClip=0;
		while(i < nbVertex) {
			
			if(i == nbVertex-1) j = 0;
		
			xvi = xVertex[i]; yvi = yVertex[i]; zvi = zVertex[i];
			xvj = xVertex[j]; yvj = yVertex[j]; zvj = zVertex[j];
			
			if(zvi < screenClip) {
				xClip[nbClip] = xvi;
				yClip[nbClip] = yvi;
				zClip[nbClip] = zvi;
				nbClip++;
			}
			
			if( (zvi>=screenClip && zvj<screenClip) || (zvi<screenClip && zvj>=screenClip) ) {
			
				float ratio = (screenClip - zvi) / (zvj - zvi);
			
				xClip[nbClip] = xvi + ((xvj - xvi) * ratio);
				yClip[nbClip] = yvi + ((yvj - yvi) * ratio);
				zClip[nbClip] = zvi + ((zvj - zvi) * ratio);
				
				nbClip++;
			}
		
			i++; j++;
		}
		if(nbClip == 0) return;
		
		for(i=0; i<nbClip; i++) {
			xVertex[i] = xClip[i];
			yVertex[i] = yClip[i];
			zVertex[i] = zClip[i];
		}
		nbVertex = nbClip;
		
		
		//*************************************************************
		//***** COMPUTES THE POLYGON SPLIT BY THE CLIPPING PLANES *****
		//*************************************************************
		
		for(h=0; h<nbPlanes; h++) {
			
			float ap = aPlane[h];
			float bp = bPlane[h];
			float cp = cPlane[h];
			float dp = dPlane[h];
		
			i = 0; j = 1; nbClip = 0;
			while(i < nbVertex) {
				
				if(i == nbVertex-1) j = 0;
				
				xvi = xVertex[i]; yvi = yVertex[i]; zvi = zVertex[i];
				xvj = xVertex[j]; yvj = yVertex[j]; zvj = zVertex[j];
				
				float dVi = (ap*xvi) + (bp*yvi) + (cp*zvi) + dp;
				float dVj = (ap*xvj) + (bp*yvj) + (cp*zvj) + dp;
				
				if(dVi > 0f) {
					xClip[nbClip] = xvi;
					yClip[nbClip] = yvi;
					zClip[nbClip] = zvi;
					nbClip++;
				}
				
				if( (dVi <= 0f && dVj > 0f) || (dVi > 0f && dVj <= 0f) ) {
					
					float xv = xvj - xvi;
					float yv = yvj - yvi;
					float zv = zvj - zvi;
					
					float xw = xvi - xPlane[h];
					float yw = yvi - yPlane[h];
					float zw = zvi - zPlane[h];
					
					float ratio = -( (ap*xw) + (bp*yw) + (cp*zw) ) / ( (ap*xv) + (bp*yv) + (cp*zv) );
					
					xClip[nbClip] = xvi + (xv * ratio);
					yClip[nbClip] = yvi + (yv * ratio);
					zClip[nbClip] = zvi + (zv * ratio);
					
					nbClip++;
				}
			
				i++; j++;
			}
			if(nbClip == 0) return;
			
			for(i=0; i<nbClip; i++) {
				xVertex[i] = xClip[i];
				yVertex[i] = yClip[i];
				zVertex[i] = zClip[i];
			}
			nbVertex = nbClip;
		}
			
		
		//*********************************************************
		//***** COMPUTES THE 3D->2D PROJECTION OF THE POLYGON *****
		//*********************************************************
		
		float quot;
		for(i=0; i<nbVertex; i++) {
			quot = - (focal / zVertex[i]);
			x2D[i] = centerX + (int)(xVertex[i] * quot);
			y2D[i] = centerY - (int)(yVertex[i] * quot);
		}
		nbApex = nbVertex;
		
		
		
		//*************************************************************
		//***** COMPUTES THE 2D POLYGON SPLIT BY THE SCREEN EDGES *****
		//*************************************************************
		
		//	Left
		i=0; j=1; nbClip=0;
		while(i < nbApex) {
			if(i == nbApex-1) j = 0;
			
			xvi = x2D[i]; yvi = y2D[i];
			xvj = x2D[j]; yvj = y2D[j];
			
			if(xvi>0f) {
				x2DClip[nbClip] = (int)xvi;
				y2DClip[nbClip] = (int)yvi;
				nbClip++;
			}
			if( (xvi<=0 && xvj>0) || (xvi>0 && xvj<=0) ){
				t = (0f - xvi) / (xvj - xvi);
				x2DClip[nbClip] = 0;
				y2DClip[nbClip] = (int)(yvi + (t * (yvj - yvi)));
				nbClip++;
			}
			i++; j++; 
		}
		if(nbClip == 0) return;
		for(i=0; i<nbClip; i++) {
			x2D[i] = x2DClip[i];
			y2D[i] = y2DClip[i];
		}
		nbApex = nbClip;
		
		
		
		//	Top
		i=0; j=1; nbClip=0;
		while(i < nbApex) {
			if(i == nbApex-1) j = 0;
			
			xvi = x2D[i]; yvi = y2D[i];
			xvj = x2D[j]; yvj = y2D[j];
			
			if(yvi>0f) {
				x2DClip[nbClip] = (int)xvi;
				y2DClip[nbClip] = (int)yvi;
				nbClip++;
			}
			if( (yvi<=0 && yvj>0) || (yvi>0 && yvj<=0) ){
				t = (0f - yvi) / (yvj - yvi);
				x2DClip[nbClip] = (int)(xvi + (t * (xvj - xvi)));
				y2DClip[nbClip] = 0;
				nbClip++;
			}
			i++; j++; 
		}
		if(nbClip == 0) return;
		for(i=0; i<nbClip; i++) {
			x2D[i] = x2DClip[i];
			y2D[i] = y2DClip[i];
		}
		nbApex = nbClip;
		
		
		
		
		//	Right
		i=0; j=1; nbClip=0;
		while(i < nbApex) {
			if(i == nbApex-1) j = 0;
			
			xvi = x2D[i]; yvi = y2D[i];
			xvj = x2D[j]; yvj = y2D[j];
			
			if(xvi<width) {
				x2DClip[nbClip] = (int)xvi;
				y2DClip[nbClip] = (int)yvi;
				nbClip++;
			}
			if( (xvi<width && xvj>=width) || (xvi>=width && xvj<width) ){
				t = (width - xvi) / (xvj - xvi);
				x2DClip[nbClip] = width;
				y2DClip[nbClip] = (int)(yvi + (t * (yvj - yvi)));
				nbClip++;
			}
			i++; j++; 
		}
		if(nbClip == 0) return;
		for(i=0; i<nbClip; i++) {
			x2D[i] = x2DClip[i];
			y2D[i] = y2DClip[i];
		}
		nbApex = nbClip;
		
		
		
		//	Bottom
		i=0; j=1; nbClip=0;
		while(i < nbApex) {
			if(i == nbApex-1) j = 0;
			
			xvi = x2D[i]; yvi = y2D[i];
			xvj = x2D[j]; yvj = y2D[j];
			
			if(yvi<height) {
				x2DClip[nbClip] = (int)xvi;
				y2DClip[nbClip] = (int)yvi;
				nbClip++;
			}
			if( (yvi<height && yvj>=height) || (yvi>=height && yvj<height) ){
				t = (height - yvi) / (yvj - yvi);
				x2DClip[nbClip] = (int)(xvi + (t * (xvj - xvi)));
				y2DClip[nbClip] = height;
				nbClip++;
			}
			i++; j++; 
		}
		if(nbClip == 0) return;
		for(i=0; i<nbClip; i++) {
			x2D[i] = x2DClip[i];
			y2D[i] = y2DClip[i];
		}
		nbApex = nbClip;
		
		
		
		//****************************************************************
		//********************* DISPLAYS THE POLYGON *********************
		//****************************************************************
		
		if(is_filled)
			graphics.fillPolygon(x2D, y2D, nbApex);
		else
			graphics.drawPolygon(x2D, y2D, nbApex);
	}
	
}
