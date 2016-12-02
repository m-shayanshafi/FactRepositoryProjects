/* Robot.java
  
   Copyright (C) 2001  Fergus Crawshay Murray
   Modified 254

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the 
   Free Software Foundation, Inc., 
   59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

/* Modified Player model, using spheres and cylinders instead of boxes
 * Yu Zhang, Nov 29 2004.
 */

package soccer.client.view.j3d;
import java.net.URL;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.TextureLoader;

public class Robot extends TransformGroup implements TOSModel 
{
	public Robot() {
		this(0);
	}

	
	public Robot(int ID) 
	{
		if(ID<11)
		{
			colour = new Color3f(1f, 1f, 0f);
			side = "l";
			id = ID + 1;
		}
		else
		{
			colour = new Color3f(1f, 0f, 0f);
			side = "r";
			id = ID - 10;
		}
		this.appearance = createAppearance(colour);
		this.skin = createAppearance(new Color3f(0.77f, 0.48f, 0.3f));
		Transform3D partTransform3D;
		// Build a humanoid robot out of boxes.

		Transform3D lt = new Transform3D();
		lt.setTranslation(new Vector3f(0.0f, 0.0f, 1.6f));
		TransformGroup legs = new TransformGroup(lt);
		double lltm[] =
			{
				1.0,
				0.0,
				0.0,
				-0.5,
				0.0,
				1.0,
				0.0,
				0.0,
				0.0,
				0.0,
				1.0,
				-0.4,
				0.0,
				0.0,
				0.0,
				1.0 };
		llt = new Transform3D(lltm);
		leftLeg = new TransformGroup(llt);
		leftLeg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		partTransform3D = new Transform3D();
		partTransform3D.setScale(new Vector3d(1, 2.0f, 1));
		partTransform3D.setRotation(new AxisAngle4f(1.0f, 0f, 0f, 1.57f));
		partTransform3D.setTranslation(new Vector3d(0.15, 0.0, 0.0));
		TransformGroup t_leftLeg = new TransformGroup(partTransform3D);
		t_leftLeg.addChild(new Cylinder(0.27f, -0.8f, appearance));		
		leftLeg.addChild(t_leftLeg);
		legs.addChild(leftLeg);

		double lstm[] =
			{
				1.0,
				0.0,
				0.0,
				0.0,
				0.0,
				1.0,
				0.0,
				0.0,
				0.0,
				0.0,
				1.0,
				-1.2,
				0.0,
				0.0,
				0.0,
				1.0 };
		lst = new Transform3D(lstm);
		leftShin = new TransformGroup(lst);
		leftShin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		partTransform3D = new Transform3D();
		partTransform3D.setScale(new Vector3d(1, 2.0f, 1));
		partTransform3D.setRotation(new AxisAngle4f(1.0f, 0f, 0f, 1.57f));
		partTransform3D.setTranslation(new Vector3d(0.15, 0.0, 0.0));
		TransformGroup t_leftShin = new TransformGroup(partTransform3D);
		t_leftShin.addChild(new Cylinder(0.2f, 0.8f, skin));		
		leftShin.addChild(t_leftShin);
		leftLeg.addChild(leftShin);

		double rltm[] =
			{
				1.0,
				0.0,
				0.0,
				0.5,
				0.0,
				1.0,
				0.0,
				0.0,
				0.0,
				0.0,
				1.0,
				-0.4,
				0.0,
				0.0,
				0.0,
				1.0 };
		rlt = new Transform3D(rltm);
		rightLeg = new TransformGroup(rlt);
		rightLeg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		partTransform3D = new Transform3D();
		partTransform3D.setScale(new Vector3d(1, 2.0f, 1));
		partTransform3D.setRotation(new AxisAngle4f(1.0f, 0f, 0f, 1.57f));
		partTransform3D.setTranslation(new Vector3d(-0.15, 0.0, 0.0));
		TransformGroup t_rightLeg = new TransformGroup(partTransform3D);
		t_rightLeg.addChild(new Cylinder(0.27f, 0.8f, appearance));		
		rightLeg.addChild(t_rightLeg);
		legs.addChild(rightLeg);
		addChild(legs);

		double rstm[] =
			{
				1.0,
				0.0,
				0.0,
				0.0,
				0.0,
				1.0,
				0.0,
				0.0,
				0.0,
				0.0,
				1.0,
				-1.2,
				0.0,
				0.0,
				0.0,
				1.0 };
		rst = new Transform3D(rstm);
		rightShin = new TransformGroup(rst);
		rightShin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		partTransform3D = new Transform3D();
		partTransform3D.setScale(new Vector3d(1, 2.0f, 1));
		partTransform3D.setRotation(new AxisAngle4f(1.0f, 0f, 0f, 1.57f));
		partTransform3D.setTranslation(new Vector3d(-0.15, 0.0, 0.0));
		TransformGroup t_rightShin = new TransformGroup(partTransform3D);
		t_rightShin.addChild(new Cylinder(0.2f, 0.8f, skin));			
		rightShin.addChild(t_rightShin);
		rightLeg.addChild(rightShin);

		double latm[] =
			{
				1.0,
				0.0,
				0.0,
				-1.0,
				0.0,
				1.0,
				0.0,
				0.0,
				0.0,
				0.0,
				1.0,
				3.0,
				0.0,
				0.0,
				0.0,
				1.0 };
		lat = new Transform3D(latm);
		leftArm = new TransformGroup(lat);
		leftArm.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		partTransform3D = new Transform3D();
		partTransform3D.setScale(new Vector3d(1, 2.0f, 1));
		partTransform3D.setRotation(new AxisAngle4f(1.0f, 0f, 0f, 1.57f));
		partTransform3D.setTranslation(new Vector3d(0.2, 0.0, 0.0));
		TransformGroup t_leftArm = new TransformGroup(partTransform3D);
		t_leftArm.addChild(new Cylinder(0.2f, 0.6f, appearance));		
		leftArm.addChild(t_leftArm);
		addChild(leftArm);

		double lftm[] =
			{
				1.0,
				0.0,
				0.0,
				0.0,
				0.0,
				1.0,
				0.0,
				0.0,
				0.0,
				0.0,
				1.0,
				-0.8,
				0.0,
				0.0,
				0.0,
				1.0 };
		lft = new Transform3D(lftm);
		leftForearm = new TransformGroup(lft);
		leftForearm.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		partTransform3D = new Transform3D();
		partTransform3D.setScale(new Vector3d(1, 2.0f, 1));
		partTransform3D.setRotation(new AxisAngle4f(1.0f, 0f, 0f, 1.57f));
		partTransform3D.setTranslation(new Vector3d(0.2, 0.0, 0.0));
		TransformGroup t_leftForeArm = new TransformGroup(partTransform3D);
		t_leftForeArm.addChild(new Cylinder(0.15f, 0.5f, skin));		
		leftForearm.addChild(t_leftForeArm);		
		leftArm.addChild(leftForearm);

		double ratm[] =
			{
				1.0,
				0.0,
				0.0,
				1.0,
				0.0,
				1.0,
				0.0,
				0.0,
				0.0,
				0.0,
				1.0,
				3.0,
				0.0,
				0.0,
				0.0,
				1.0 };
		rat = new Transform3D(ratm);
		rightArm = new TransformGroup(rat);
		rightArm.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		partTransform3D = new Transform3D();
		partTransform3D.setScale(new Vector3d(1, 2.0f, 1));
		partTransform3D.setRotation(new AxisAngle4f(1.0f, 0f, 0f, 1.57f));
		partTransform3D.setTranslation(new Vector3d(-0.2, 0.0, 0.0));
		TransformGroup t_rightArm = new TransformGroup(partTransform3D);
		t_rightArm.addChild(new Cylinder(0.2f, 0.6f, appearance));			
		rightArm.addChild(t_rightArm);
		addChild(rightArm);

		double rftm[] =
			{
				1.0,
				0.0,
				0.0,
				0.0,
				0.0,
				1.0,
				0.0,
				0.0,
				0.0,
				0.0,
				1.0,
				-0.8,
				0.0,
				0.0,
				0.0,
				1.0 };
		rft = new Transform3D(rftm);
		rightForearm = new TransformGroup(rft);
		rightForearm.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		partTransform3D = new Transform3D();
		partTransform3D.setScale(new Vector3d(1, 2.0f, 1));
		partTransform3D.setRotation(new AxisAngle4f(1.0f, 0f, 0f, 1.57f));
		partTransform3D.setTranslation(new Vector3d(-0.2, 0.0, 0.0));
		TransformGroup t_rightForeArm = new TransformGroup(partTransform3D);
		t_rightForeArm.addChild(new Cylinder(0.15f, 0.5f, skin));			
		rightForearm.addChild(t_rightForeArm);
		rightArm.addChild(rightForearm);

		double ttm[] =
			{
				1.0,
				0.0,
				0.0,
				0.0,
				0.0,
				1.0,
				0.0,
				0.0,
				0.0,
				0.0,
				1.0,
				2.7,
				0.0,
				0.0,
				0.0,
				1.0 };
		tt = new Transform3D(ttm);
		torso = new TransformGroup(tt);
		partTransform3D = new Transform3D();
		partTransform3D.setScale(new Vector3d(1, 1.1, 0.6f));
		//partTransform3D.setRotation(new AxisAngle4f(1.0f, 0f, 0f, 1.57f));

		// with this orientation, player number images must be upside down;
		// in this case they are placed on the back side of the robot 
		partTransform3D.setRotation(new AxisAngle4f(
									1.0f, 0f, 0f, -(float)Math.PI/2f));
				
		TransformGroup t_torso = new TransformGroup(partTransform3D);

		URL imgURL = getClass().getResource("/imag/num" + side + id + ".png");
		//URL imgURL = getClass().getResource("/imag/numr1.png");
		TextureLoader loader = new TextureLoader(imgURL, null);
		ImageComponent2D image = loader.getImage();

		if (image == null) {
			System.out.println("load failed for texture, player " + id + "-" + side );
		}		
	      // can't use parameterless constuctor
	    Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
	                                      image.getWidth(), image.getHeight());
	    texture.setImage(0, image);
	    TextureAttributes textureAttrib = new TextureAttributes();
	    textureAttrib.setTextureMode(TextureAttributes.DECAL);
	    Appearance playNum = new Appearance();
	    playNum.setTexture(texture);
	    playNum.setTextureAttributes(textureAttrib);
		t_torso.addChild(new Cylinder(0.6f, 1.5f, Primitive.GENERATE_TEXTURE_COORDS, playNum));		
		
		torso.addChild(t_torso);
		addChild(torso);

		double htm[] =
			{
				1.0,
				0.0,
				0.0,
				0.0,
				0.0,
				1.0,
				0.0,
				0.0,
				0.0,
				0.0,
				1.0,
				4.0,
				0.0,
				0.0,
				0.0,
				1.0 };
		ht = new Transform3D(htm);
		head = new TransformGroup(ht);		
		partTransform3D = new Transform3D();
		partTransform3D.setScale(new Vector3d(1, 1, 1.3f));
		TransformGroup t_head = new TransformGroup(partTransform3D);
		t_head.addChild(new Sphere(0.4f, skin));		
		head.addChild(t_head);
		addChild(head);

		Transform3D rt = new Transform3D();
		rt.setRotation(new AxisAngle4f(0f, 0f, 1f, (float) (Math.PI * 0.5)));
		this.setTransform(rt);
	}

	Color3f colour;
	String side;
	int id;
	Appearance appearance;
	Appearance skin;
	float phase = 0;
	int N = 16;
	double a;
	int v;
	TransformGroup leftLeg,
		leftShin,
		rightLeg,
		rightShin,
		leftArm,
		leftForearm,
		rightArm,
		rightForearm,
		torso,
		head;
	Transform3D llt, lst, rlt, rst, lat, lft, rat, rft, tt, ht;

	public void step(float stepSize) {

		//stepSize /= 2;
		phase += stepSize;
		//System.out.println(phase);
		if (phase > Math.PI * 2)
			phase -= Math.PI * 2;
		AxisAngle4f angt = new AxisAngle4f();

		// Crude trigonometric approximation of walking motion

		angt.set(1.0f, 0f, 0f, -0.25f + 0.5f * (float) Math.sin(phase));
		llt.setRotation(angt);
		leftLeg.setTransform(llt);

		angt.set(1.0f, 0f, 0f, -0.25f + 0.5f * (float) Math.sin(-phase));
		rlt.setRotation(angt);
		rightLeg.setTransform(rlt);

		// Shins and forearms are a little bit behind thighs and upper arms...
		angt.set(1.0f, 0f, 0f, 0.25f + 0.25f * (float) Math.sin(-phase + 0.4));
		rst.setRotation(angt);
		rightShin.setTransform(rst);

		angt.set(
			1.0f,
			0.3f,
			0f,
			-0.25f + 0.25f * (float) Math.sin(-phase + 0.4));
		rat.setRotation(angt);
		rightArm.setTransform(rat);
		rft.setRotation(angt);
		rightForearm.setTransform(rft);

		angt.set(1.0f, 0f, 0f, 0.25f + 0.25f * (float) Math.sin(phase - 0.4));
		lst.setRotation(angt);
		leftShin.setTransform(lst);

		angt.set(
			1.0f,
			0.3f,
			0f,
			-0.25f + 0.25f * (float) Math.sin(phase - 0.4));
		lat.setRotation(angt);
		leftArm.setTransform(lat);
		lft.setRotation(angt);
		leftForearm.setTransform(lft);

	}

	Appearance createAppearance(Color3f colour) {

		//Material(Color3f ambientColor, Color3f emissiveColor, Color3f diffuseColor, Color3f specularColor, float shininess) 
		Appearance appearance = new Appearance();
		appearance.setMaterial(
			new Material(
				new Color3f(0.1f, 0.0f, 0.1f),
				new Color3f(0.2f, 0.0f, 0.0f),
				colour,
				colour,
				120f));
		PolygonAttributes polyAttrib = new PolygonAttributes();
		polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
		appearance.setPolygonAttributes(polyAttrib);

		return appearance;
	}

}
