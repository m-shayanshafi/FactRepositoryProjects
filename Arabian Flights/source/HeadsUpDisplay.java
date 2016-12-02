// HeadsUpDisplay.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.net.URL;

/** The heads up display is a branchgroup that is added to the root of the
 * scene. It contains all of the visual elements that appear sepearately from the
 * gameworld.
 */
public class HeadsUpDisplay extends BranchGroup
    implements GeometryUpdater
{
  // H is equivalent to the width of the screen.
  private static float H = GameFrame.HUD_DISTANCE;

  // the health object
  private TextElement healthText;

  // the mana object
  private TextElement manaText;

  // the gold object
  private TextElement goldText;

  // the message object
  private TextElement messageText;
  private long messageSetTime = 0;

  // the crosshair object
  private TextElement crosshairText;

  // the spells list object
  private TextElement spelllistText;

  // the current spell object
  private TextElement currentspellText;

  // the castle upgrade indicator
  private TextElement upgradeText;

  // the fps counter objects
  private TextElement fpsText;
  private long lasttime;

  // the kill counter
  private TextElement killcounterText;

  // the remaining monsters counter
  private TextElement remainderText;

  // the bar objects (health and mana)
  private Shape3D redBarShape = new Shape3D();
  private Shape3D blueBarShape = new Shape3D();
  private TransformGroup redBarTransGroup = new TransformGroup();
  private TransformGroup blueBarTransGroup = new TransformGroup();
  private Transform3D redBarTransform = new Transform3D();
  private Transform3D blueBarTransform = new Transform3D();

  // the minimap objects
  private final static float s2 = 1.0f/(float)Math.sqrt(2.0);
  GeometryInfo minimapGeometry = new GeometryInfo(GeometryInfo.QUAD_ARRAY);
  Point3f[] minimapCoordinates = {
    // this is four quads forming an octagon
    new Point3f(  0,  1,-H*.01f) , // note, the texture needs to be 1% farther
    new Point3f(-s2, s2,-H*.01f) , // away from the camera than the dots, thus
    new Point3f( -1,  0,-H*.01f) , // the z-coord is set to 1% of H
    new Point3f(-s2,-s2,-H*.01f) ,

    new Point3f(-s2,-s2,-H*.01f) ,
    new Point3f(  0, -1,-H*.01f) ,
    new Point3f( s2, s2,-H*.01f) ,
    new Point3f(  0,  1,-H*.01f) ,

    new Point3f(  0, -1,-H*.01f) ,
    new Point3f( s2,-s2,-H*.01f) ,
    new Point3f(  1,  0,-H*.01f) ,
    new Point3f( s2, s2,-H*.01f) };

  private Shape3D minimapShape = new Shape3D();
  private TransformGroup minimapTransGroup = new TransformGroup();
  private Transform3D minimapTransform = new Transform3D();
  private Vector3f minimapTransformVector = new Vector3f(-.8f*H,.55f*H,-H);

  // for the big minimap
  private boolean bigMinimap = false;
  private Transform3D bigMinimapTransform = new Transform3D();
  private Vector3f bigMinimapTransformVector = new Vector3f(0,0,-H);


  // a point array for the constant dots
  private PointArray points;
  private float[] coords;
  private float[] colors;
  private Shape3D pointShape;

  /** The constructor sets up the elements of the HUD. */
  public HeadsUpDisplay()
  {
    // allows the text elements to be replaced
    setCapability(Group.ALLOW_CHILDREN_EXTEND);
    setCapability(Group.ALLOW_CHILDREN_WRITE);

    setupText();
    setupBar();
    setupMinimap();
  }

  private void setupText()
  {
    // set the transforms for the text elements
    healthText       = new TextElement(new Vector3f(-.92f*H, -.61f*H, -H),   H/2.5f, "Arial",   36, Font.BOLD, new Color3f(Color.red));
    goldText         = new TextElement(new Vector3f(-.92f*H, -.75f*H, -H),   H/2.5f, "Arial",   36, Font.BOLD, new Color3f(Color.yellow));
    manaText         = new TextElement(new Vector3f(-.92f*H, -.68f*H, -H),   H/2.5f, "Arial",   36, Font.BOLD, new Color3f(Color.blue));
    messageText      = new TextElement(new Vector3f(-.60f*H, -.75f*H, -H),   H/2.5f, "Arial",   36, Font.BOLD, new Color3f(Color.white));
    crosshairText    = new TextElement(new Vector3f(-.016f*H, -.035f*H, -H), H/2.5f, "Courier", 36, Font.BOLD, new Color3f(Color.white));
    spelllistText    = new TextElement(new Vector3f(-.6f*H, .68f*H, -H),     H/2.5f, "Courier", 36, Font.BOLD, new Color3f(Color.white));
    currentspellText = new TextElement(new Vector3f(-.6f*H, .58f*H, -H),     H/2.5f, "Arial",   36, Font.BOLD, new Color3f(Color.white));
    upgradeText      = new TextElement(new Vector3f( -1f*H, -.54f*H, -H),    H/2.5f, "Courier", 36, Font.BOLD, new Color3f(Color.orange));
    fpsText          = new TextElement(new Vector3f(.72f*H, -.75f*H, -H),    H/2.5f, "Courier", 36, Font.BOLD, new Color3f(Color.gray));
    killcounterText  = new TextElement(new Vector3f(.75f*H, .68f*H, -H),     H/2.5f, "Courier", 36, Font.BOLD, new Color3f(Color.red));
    remainderText    = new TextElement(new Vector3f(.75f*H, .61f*H, -H),     H/2.5f, "Courier", 36, Font.BOLD, new Color3f(Color.red));
  }

  private void setupBar()
  {
    // set up the geometry for the bars
    GeometryInfo barGeometry = new GeometryInfo(GeometryInfo.QUAD_ARRAY);
    Point3f[] coordinates = {
      new Point3f(0f, 0f, 0f) ,
      new Point3f(1f, 0f, 0f) ,
      new Point3f(1f, 1f, 0f) ,
      new Point3f(0f, 1f, 0f)  };
    barGeometry.setCoordinates(coordinates);
    redBarShape.setGeometry(barGeometry.getGeometryArray());
    blueBarShape.setGeometry(barGeometry.getGeometryArray());

    // set up the Appearance for the red bar
    Appearance redAppearance = new Appearance();
    redAppearance.setColoringAttributes(new ColoringAttributes(new Color3f(Color.red),0));
    redBarShape.setAppearance(redAppearance);

    // set up the Appearance for the blue bar
    Appearance blueAppearance = new Appearance();
    blueAppearance.setColoringAttributes(new ColoringAttributes(new Color3f(Color.blue),0));
    blueBarShape.setAppearance(blueAppearance);

    // set up the red heirarchy
    redBarTransform.set(new Vector3f(-.995f*H, -.745f*H, -H));
    redBarTransform.setScale(new Vector3d(.03*H,.2*H,1));
    redBarTransGroup.addChild(redBarShape);
    redBarTransGroup.setTransform(redBarTransform);
    redBarTransGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    this.addChild(redBarTransGroup);

    // set up the blue heirarchy
    blueBarTransform.set(new Vector3f(-.96f*H, -.745f*H, -H));
    blueBarTransform.setScale(new Vector3d(.03*H,.2*H,1));
    blueBarTransGroup.addChild(blueBarShape);
    blueBarTransGroup.setTransform(blueBarTransform);
    blueBarTransGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    this.addChild(blueBarTransGroup);
  }

  private void setupMinimap()
  {
    // set up the shape
    int[] stripCounts = { 4 };
    minimapGeometry.setCoordinates(minimapCoordinates);

    TexCoord2f[] texcoords = {
      new TexCoord2f(0.0f, 0.0f),
      new TexCoord2f(1.0f, 0.0f),
      new TexCoord2f(1.0f, 1.0f),
      new TexCoord2f(0.0f, 1.0f) };

    minimapGeometry.setTextureCoordinateParams(1, 2);
    minimapGeometry.setTextureCoordinates(0, texcoords);

    minimapShape.setGeometry(minimapGeometry.getGeometryArray());

    minimapShape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
    minimapShape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
    minimapShape.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
    minimapShape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);

    // set up the Appearance
    minimapShape.setAppearance(createAppearance(GameFrame.URL_MAP_TEXTURE));

    // set up the transform
    minimapTransform.set(minimapTransformVector);
    minimapTransform.setScale(GameFrame.HUD_MINIMAP_SIZE);
    bigMinimapTransform.set(bigMinimapTransformVector);
    bigMinimapTransform.setScale(GameFrame.HUD_BIG_MINIMAP_SIZE);
    minimapTransGroup.setTransform(minimapTransform);
    minimapTransGroup.addChild(minimapShape);
    this.addChild(minimapTransGroup);

    minimapTransGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

    // set up point array for the dots on the minimap
    points = new PointArray(GameFrame.HUD_MINIMAP_DOTS, PointArray.COORDINATES | PointArray.COLOR_3 | PointArray.BY_REFERENCE);

    coords = new float[GameFrame.HUD_MINIMAP_DOTS*3];

    colors = new float[GameFrame.HUD_MINIMAP_DOTS*3];

    Appearance pointAppearance = new Appearance();
    pointAppearance.setPointAttributes(new PointAttributes(5,false));
    points.setCoordRefFloat(coords);
    points.setColorRefFloat(colors);
    points.setCapability(GeometryArray.ALLOW_COORDINATE_READ);
    points.setCapability(GeometryArray.ALLOW_COORDINATE_WRITE);
    points.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
    points.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);

    pointShape = new Shape3D(points, pointAppearance);

    minimapTransGroup.addChild(pointShape);
  }

  // used to make the appearance for the minimap
  private Appearance createAppearance(URL filename)
  {
    Appearance appearance = new Appearance();
    TextureUnitState texUnits[] = new TextureUnitState[1];

    // load the texture
    System.out.print("loading texture:" + filename);
    TextureLoader loader = new TextureLoader(filename, GameFrame.menuFrame);
    ImageComponent2D image = loader.getImage();
    if(image == null)
    {
      System.out.println("load failed for texture " + filename);
      System.exit(-1);
    }
    else
    {
      System.out.println("..");
      Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGB, image.getWidth(), image.getHeight());
      texture.setImage(0, image);
      texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
      texture.setBoundaryModeS(Texture.WRAP);
      texture.setBoundaryModeT(Texture.WRAP);
      appearance.setTexture(texture);
    }

    // set up transparency
    appearance.setTransparencyAttributes(
        new TransparencyAttributes(TransparencyAttributes.FASTEST, .25f));

    // set up back culling
    PolygonAttributes pa = new PolygonAttributes();
    pa.setCullFace(PolygonAttributes.CULL_BACK);
    appearance.setPolygonAttributes(pa);

    return appearance;
  }

  /** Sets the health displayed in the HUD. Normally called by a PlayerData object. */
  public synchronized void setHealth(int health)
  {
    // set the bar
    redBarTransform.setScale(new Vector3d(.03*H,.2*H*(health/100f),1));
    redBarTransGroup.setTransform(redBarTransform);

    // set the text
    healthText.setText("%"+health);
  }

  /** Sets the mana displayed in the HUD. Normally called by a PlayerData object. */
  public synchronized void setMana(int mana, int maxmana)
  {
    // set the bar
    blueBarTransform.setScale(new Vector3d(.03*H,.2*H*((float)mana/(float)maxmana),1));
    blueBarTransGroup.setTransform(blueBarTransform);

    // set the text
    manaText.setText(mana + "/" + maxmana);
  }

  /** Sets the gold displayed in the HUD. Normally called by a PlayerData object. */
  public synchronized void setGold(int gold)
  {
    StringBuffer goldString = new StringBuffer("$");
    if (gold > 999)
    {
      int rem = gold%1000;
      goldString.append(gold/1000);
      goldString.append(',');
      if (rem < 100) goldString.append('0');
      if (rem < 10) goldString.append('0');
      goldString.append(rem);
    }
    else if (gold > 999999) goldString.append("999,999+");
    else goldString.append(gold);

    goldText.setText(goldString.toString());
  }

  /** This keeps the minimap centered on the camera, and it is called every
   * so often by the PhysicsEngine. */
  public void updateMinimap()
  {
    float camx;
    float camz;
    float size;

    bigMinimap = GameFrame.listener.keyDown(KeyEvent.VK_TAB);

    if (!bigMinimap)
    {
      camx = GameFrame.renderer.getCamera().getCamX()*GameFrame.HUD_MINIMAP_SCALE/1000;
      camz = GameFrame.renderer.getCamera().getCamZ()*GameFrame.HUD_MINIMAP_SCALE/1000;
      size = GameFrame.HUD_MINIMAP_RANGE;
    }
    else
    {
      camx = GameFrame.renderer.getCamera().getCamX()*GameFrame.HUD_MINIMAP_SCALE/1000;
      camz = GameFrame.renderer.getCamera().getCamZ()*GameFrame.HUD_MINIMAP_SCALE/1000;
      size = GameFrame.HUD_MINIMAP_BIG_RANGE;
    }

    // this is four quads forming an octagon
    TexCoord2f[] texcoords = {
      new TexCoord2f(camx          , -camz+size) ,
      new TexCoord2f(camx-s2*size  , -camz+s2*size) ,
      new TexCoord2f(camx-size     , -camz) ,
      new TexCoord2f(camx-s2*size  , -camz-s2*size) ,

      new TexCoord2f(camx-s2*size  , -camz-s2*size) ,
      new TexCoord2f(camx          , -camz-size) ,
      new TexCoord2f(camx+s2*size  , -camz+s2*size) ,
      new TexCoord2f(camx          , -camz+size) ,

      new TexCoord2f(camx          , -camz-size) ,
      new TexCoord2f(camx+s2*size  , -camz-s2*size) ,
      new TexCoord2f(camx+size     , -camz) ,
      new TexCoord2f(camx+s2*size  , -camz+s2*size) };


    minimapGeometry.setTextureCoordinates(0, texcoords);

    minimapShape.setGeometry(minimapGeometry.getGeometryArray());

    // change the transform
    float rot = GameFrame.renderer.getCamera().getCamRot();

    if (bigMinimap)
    {
      bigMinimapTransform.setRotation(new AxisAngle4f(0,0,1f,-rot));
      minimapTransGroup.setTransform(bigMinimapTransform);
    }
    else
    {
      minimapTransform.setRotation(new AxisAngle4f(0,0,1f,-rot));
      minimapTransGroup.setTransform(minimapTransform);
    }

    // update the points on the minimap
    points.updateData(this);

    // check to see if the message should be cleared
    if (messageSetTime < GameFrame.CURRENT_TIME - 10000)
    {
      setMessage("");
    }
  }

  /** This method is used to update the PointsArray holding the points on the minimap. */
  public void updateData(Geometry geometry)
  {
    if (bigMinimap)
    {
      GameFrame.physics.getDotCoords(coords, GameFrame.HUD_MINIMAP_BIG_DOT_SCALE);
      GameFrame.physics.getDotColors(colors, GameFrame.HUD_MINIMAP_BIG_DOT_SCALE);
    }
    else
    {
      GameFrame.physics.getDotCoords(coords, GameFrame.HUD_MINIMAP_DOT_SCALE);
      GameFrame.physics.getDotColors(colors, GameFrame.HUD_MINIMAP_DOT_SCALE);
    }
  }

  /** Sets the message displayed at the bottom of the screen. */
  public void setMessage(String message)
  {
    messageText.setText(message);
    messageSetTime = GameFrame.CURRENT_TIME;
  }

  /** Sets the crosshair character displayed in the middle of the screen. */
  public void setCrosshair(char crosshair)
  {
    crosshairText.setText(""+crosshair);
  }

  /** Sets the spells available at th top of the screen. */
  public void setSpells(String spells)
  {
    spelllistText.setText(spells);
  }

  /** Sets the current spell name */
  public void setCurrentSpell(String spell)
  {
    currentspellText.setText(spell);
  }

  /** Sets the fps counter. */
  public void setFps(float fps)
  {
    fpsText.setText("FPS: "+(int)fps);
  }

  /** Sets the fps counter. */
  public void setKills(int kills)
  {
    StringBuffer str = new StringBuffer("+");
    if (kills < 100) str.append("0");
    if (kills < 10) str.append("0");
    str.append(kills);
    killcounterText.setText(str.toString());
  }

  /** Sets the fps counter. */
  public void setRemainder(int remainder)
  {
    remainderText.setText("Left:"+remainder);
  }

  /** hides the message text HUD element. */
  public void hideRemainder()
  {
    remainderText.hide();
  }

  /** hides the message text HUD element. */
  public void hideMessage()
  {
    messageText.hide();
  }

  /** hides the crosshair HUD element. */
  public void hideCrosshair()
  {
    crosshairText.hide();
  }

  /** Shows the upgrade indicator   */
  public void showUpgradeIndicator()
  {
    upgradeText.setText("! -^- !");
  }

  /** Shows the upgrade indicator   */
  public void hideUpgradeIndicator()
  {
    upgradeText.setText("  ");
  }

  public class TextElement
  {
    // the text objects
    private Text2D text;
    private TransformGroup transGroup;
    private Transform3D transform = new Transform3D();
    private BranchGroup group;

    private Color3f color;
    private String font;
    private int fontsize;
    private int fonttype;

    private String string;

    /** Creates the text element at a specified position, scale, font, size, color, and font type */
    public TextElement(Vector3f translation, float scale, String font, int fontsize, int fonttype, Color3f color)
    {
      transform.setTranslation(translation);
      transform.setScale(scale);

      this.color = color;
      this.font = font;
      this.fontsize = fontsize;
      this.fonttype = fonttype;
    }

    /** sets and shows the text HUD element. */
    public void setText(String string)
    {
      // only set the text if it is different
      if (this.string != null && string != null && this.string.equals(string)) return;

      this.string = string;
      BranchGroup oldGroup = group;

      text = new Text2D(string, color, font, fontsize, fonttype);
      transGroup = new TransformGroup();
      transGroup.addChild(text);
      transGroup.setTransform(transform);
      group = new BranchGroup();
      group.setCapability(BranchGroup.ALLOW_DETACH);
      group.addChild(transGroup);

      addChild(group);
      if (oldGroup != null) removeChild(oldGroup);
    }

    /** used to check what text is currently displayed */
    public String getText()
    {
      return string;
    }

    /** hides the text HUD element. */
    public void hide()
    {
      if (group != null) removeChild(group);
      group = null;
    }

  }
}