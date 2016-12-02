public class Kolja extends Monster {
  public Kolja(int x, int y) {
    super(mainsp(), x, y, Engine.hsize*mainsp().h/mainsp().w, 4,
      Mat.rnd(Mat.dpi), Player.speed*0.4, Player.turnSpeed/3, 1500);
    Console.set("come on\ngonna put you on the\nground, ground, ground ", 1500);
    initSprites("kolja");
  }

  public Sprite sp() {
    if (afterDmg()) {
      return Gfx.sprites.get("heartedkolja");
    } else if (Adventure.time(lastShot, Adventure.shotAnimation)) {
      return Gfx.sprites.get("shootingkolja");
    } else {
      return mainsp();
    }
  }

  public void dmg() {
    super.dmg();
    if (life==0) Console.set("all of my hate cannot be bound", 2500);
  }

  private static Sprite mainsp() {
    return Gfx.sprites.get("kolja");
  }
}
