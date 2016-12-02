public class Manson extends Monster {
  public Manson(int x, int y) {
    super(mainsp(), x, y, Engine.hsize*mainsp().h/mainsp().w, 3,
      Mat.rnd(Mat.dpi), Player.speed*0.7, Player.turnSpeed, 1300);
    Console.set("don't forget Manson\nto include everyone\ndon't forget\ndon't forget", 1500);
    initSprites("manson");
  }

  public void dmg() {
    super.dmg();
    if (life==0) Console.set("sleep with one eye open\ngripping your pillow tight", 2500);
  }

  public Sprite sp() {
    if (afterDmg()) {
      return Gfx.sprites.get("heartedManson");
    } else if (Adventure.time(lastShot, Adventure.shotAnimation)) {
      return Gfx.sprites.get("shootingmanson");
    } else {
      return mainsp();
    }
  }

  private static Sprite mainsp() {
    return Gfx.sprites.get("manson");
  }
}
