// Forest.java
// copyright 2003 Mike Prosser
// this code is licensed under the terms of the GNU General Public License
// http://www.gnu.org/copyleft/gpl.html


package arabian;

import javax.media.j3d.Appearance;

/** The forest class is a collection of trees, which are scenery. */
public class Forest
{
  // the appearance of the trees
  private static Appearance treeApp = PhysicsEngine.createAppearance(GameFrame.STRING_TREE);

  /** This creates a group of a certain number of randomly placed trees
   *  centered around x,z and extending out by a
   *  specified radius.
   */
  public Forest(float x, float z, float radius, int trees)
  {
    for (int i=0; i<trees; i++)
    {
      float dist = (float)(Math.random()*radius);
      float angle = (float)(Math.random()*2*Math.PI);
      float tx = (float)(dist*Math.sin(angle));
      float tz = (float)(dist*Math.cos(angle));
      GameFrame.physics.addParticle(new Tree(tx, tz));
    }
  }

  /** The tree is just a particle with the tree appearance. */
  public static class Tree extends Particle
  {
    public Tree(float x, float z)
    {
      setType(TYPE_ROLLING);
      setAppearance(treeApp);
      setPosition(x,0,z);
    }
  }
}