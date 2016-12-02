package GameCore;

import GameElements.DynamicObject;
import GameElements.GameObject;

import java.util.ArrayList;
/**
 * Created by mertuygur on 23/12/14.
 */
public class CollisionController {




    public boolean collision(DynamicObject o1, ArrayList<DynamicObject> o2) {
        for (int i = 0; i < o2.size(); i++) {
            if (o1.getRectangle().intersects(o2.get(i).getRectangle())) {
                o2.remove(i);
                return true;
            }
        }
        return false;
    }
    public boolean staticCollision(DynamicObject o1, GameObject o2){
        if (o1.getRectangle().intersects(o2.getRectangle())) {
            return true;
        }
        return false;
    }
}
