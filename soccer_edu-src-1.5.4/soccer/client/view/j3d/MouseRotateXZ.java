/*
 *	@(#)MouseRotate.java 1.32 01/01/11 07:22:23
 *  @ Modified Fergus Murray 17/08/01 so that it rotates about x and z axes, 
 *  and the camera stays aligned so that there is no roll:
 *  that is, a line out of its left and right sides is always parallel to the ground.
 * Copyright (c) 1996-2001 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */

package soccer.client.view.j3d;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.behaviors.mouse.*;

/**
 * MouseRotateXZ is a Java3D behavior object that lets users control the 
 * rotation of an object via a mouse.
 * <p>
 * To use this utility, first create a transform group that this 
 * rotate behavior will operate on. Then,
 *<blockquote><pre>
 * 
 *   MouseRotateXZ behavior = new MouseRotateXZ();
 *   behavior.setTransformGroup(objTrans);
 *   objTrans.addChild(behavior);
 *   behavior.setSchedulingBounds(bounds);
 *
 *</pre></blockquote>
 * The above code will add the rotate behavior to the transform
 * group. The user can rotate any object attached to the objTrans.
 */

public class MouseRotateXZ extends MouseBehavior {
	double x_angle, z_angle, xa, za;
	double x_factor = .03;
	double z_factor = .03;
	Vector3d angleVector = new Vector3d();
	private MouseBehaviorCallback callback = null;

	/**
	 * Creates a rotate behavior given the transform group.
	 * @param transformGroup The transformGroup to operate on.
	 */
	public MouseRotateXZ(TransformGroup transformGroup) {
		super(transformGroup);
	}

	/**
	 * Creates a default mouse rotate behavior.
	 **/
	public MouseRotateXZ() {
		super(0);
	}

	/**
	 * Creates a rotate behavior.
	 * Note that this behavior still needs a transform
	 * group to work on (use setTransformGroup(tg)) and
	 * the transform group must add this behavior.
	 * @param flags interesting flags (wakeup conditions).
	 */
	public MouseRotateXZ(int flags) {
		super(flags);
	}

	/**
	 * Creates a rotate behavior that uses AWT listeners and behavior
	 * posts rather than WakeupOnAWTEvent.  The behavior is added to the
	 * specified Component. A null component can be passed to specify
	 * the behavior should use listeners.  Components can then be added
	 * to the behavior with the addListener(Component c) method.
	 * @param c The Component to add the MouseListener
	 * and MouseMotionListener to.
	 * @since Java 3D 1.2.1
	 */
	public MouseRotateXZ(Component c) {
		super(c, 0);
	}

	/**
	 * Creates a rotate behavior that uses AWT listeners and behavior
	 * posts rather than WakeupOnAWTEvent.  The behaviors is added to
	 * the specified Component and works on the given TransformGroup.
	 * A null component can be passed to specify the behavior should use
	 * listeners.  Components can then be added to the behavior with the
	 * addListener(Component c) method.
	 * @param c The Component to add the MouseListener and
	 * MouseMotionListener to.
	 * @param transformGroup The TransformGroup to operate on.
	 * @since Java 3D 1.2.1
	 */
	public MouseRotateXZ(Component c, TransformGroup transformGroup) {
		super(c, transformGroup);
	}

	/**
	 * Creates a rotate behavior that uses AWT listeners and behavior
	 * posts rather than WakeupOnAWTEvent.  The behavior is added to the
	 * specified Component.  A null component can be passed to specify
	 * the behavior should use listeners.  Components can then be added to
	 * the behavior with the addListener(Component c) method.
	 * Note that this behavior still needs a transform
	 * group to work on (use setTransformGroup(tg)) and the transform
	 * group must add this behavior.
	 * @param flags interesting flags (wakeup conditions).
	 * @since Java 3D 1.2.1
	 */
	public MouseRotateXZ(Component c, int flags) {
		super(c, flags);
	}

	public void initialize() {
		super.initialize();
		x_angle = 0;
		z_angle = 0;
		if ((flags & INVERT_INPUT) == INVERT_INPUT) {
			invert = true;
			x_factor *= -1;
			z_factor *= -1;
		}
	}

	/**
	 * Return the x-axis movement multipler.
	 **/
	public double getXFactor() {
		return x_factor;
	}

	/**
	 * Return the z-axis movement multipler.
	 **/
	public double getZFactor() {
		return z_factor;
	}

	/**
	 * Set the x-axis amd z-axis movement multipler with factor.
	 **/
	public void setFactor(double factor) {
		x_factor = z_factor = factor;
	}

	/**
	 * Set the x-axis amd z-axis movement multipler with xFactor and zFactor
	 * respectively.
	 **/
	public void setFactor(double xFactor, double zFactor) {
		x_factor = xFactor;
		z_factor = zFactor;
	}

	public void processStimulus(Enumeration criteria) {
		WakeupCriterion wakeup;
		AWTEvent[] events;
		MouseEvent evt;
		// 	int id;
		// 	int dx, dz;

		while (criteria.hasMoreElements()) {
			wakeup = (WakeupCriterion) criteria.nextElement();
			if (wakeup instanceof WakeupOnAWTEvent) {
				events = ((WakeupOnAWTEvent) wakeup).getAWTEvent();
				if (events.length > 0) {
					evt = (MouseEvent) events[events.length - 1];
					doProcess(evt);
				}
			} else if (wakeup instanceof WakeupOnBehaviorPost) {
				while (true) {
					// access to the queue must be synchronized
					synchronized (mouseq) {
						if (mouseq.isEmpty())
							break;
						evt = (MouseEvent) mouseq.remove(0);
						// consolidate MOUSE_DRAG events
						while ((evt.getID() == MouseEvent.MOUSE_DRAGGED)
							&& !mouseq.isEmpty()
							&& (((MouseEvent) mouseq.get(0)).getID()
								== MouseEvent.MOUSE_DRAGGED)) {
							evt = (MouseEvent) mouseq.remove(0);
						}
					}
					doProcess(evt);
				}
			}

		}
		wakeupOn(mouseCriterion);
	}

	void doProcess(MouseEvent evt) {
		int id;
		int dx, dy;
		processMouseEvent(evt);
		if (((buttonPress) && ((flags & MANUAL_WAKEUP) == 0))
			|| ((wakeUp) && ((flags & MANUAL_WAKEUP) != 0))) {
			id = evt.getID();
			if ((id == MouseEvent.MOUSE_DRAGGED)
				&& !evt.isMetaDown()
				&& !evt.isAltDown()) {
				x = evt.getX();
				y = evt.getY();

				dx = x - x_last;
				dy = y - y_last;

				if (!reset) {
					x_angle = dy * z_factor;
					z_angle = dx * x_factor;

					xa += x_angle;
					za += z_angle;

					angleVector.set(xa, 0.0, za);
					transformX.rotX(x_angle);
					transformY.rotZ(z_angle);

					transformGroup.getTransform(currXform);

					Matrix4d mat = new Matrix4d();
					// Remember old matrix
					currXform.get(mat);

					// Translate to origin
					/*currXform.setTranslation(new Vector3d(0.0,0.0,0.0));
					if (invert) {
					currXform.mul(currXform, transformX);
					currXform.mul(currXform, transformY);
					} else {
					currXform.mul(transformX, currXform);
					currXform.mul(transformY, currXform);
					}*/
					currXform.setEuler(angleVector);
					// Set old translation back
					/*Vector3d translation = new 
					Vector3d(mat.m03, mat.m13, mat.m23);
					currXform.setTranslation(translation);
					*/
					// Update xform
					transformGroup.setTransform(currXform);

					transformChanged(currXform);

					if (callback != null)
						callback.transformChanged(
							MouseBehaviorCallback.ROTATE,
							currXform);

				} else {
					reset = false;
				}

				x_last = x;
				y_last = y;
			} else if (id == MouseEvent.MOUSE_PRESSED) {
				x_last = evt.getX();
				y_last = evt.getY();
			}
		}
	}

	/**
	 * Users can overload this method  which is called every time
	 * the Behavior updates the transform
	 *
	 * Default implementation does nothing
	 */
	public void transformChanged(Transform3D transform) {
	}

	/**
	 * The transformChanged method in the callback class will
	 * be called every time the transform is updated
	 */
	public void setupCallback(MouseBehaviorCallback callback) {
		this.callback = callback;
	}
}
