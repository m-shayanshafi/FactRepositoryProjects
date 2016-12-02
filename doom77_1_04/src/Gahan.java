public class Gahan extends Monster {
  private double startTime;

  public Gahan(int x, int y) {
    super(mainsp(), x, y, Engine.hsize*mainsp().h/mainsp().w, 1,
      Mat.rnd(Mat.dpi), Player.speed*0.5, Player.turnSpeed, 1500);
    Console.set("now I'm not looking for absolution", 1500);
    initSprites("gahan");
  }

  public Sprite sp() {
    if (afterDmg()) {
      return Gfx.sprites.get("heartedgahan");
    } else {
      return mainsp();
    }
  }

  public void act() {
    gahanShoes();
    super.act();
  }

  public void dmg() {
    super.dmg();
    Console.set("but before you come\nto any conclusion\ntry walking in my shoes", 2500);
    Player.player.gahanShoesTime = Doom77.time;
  }

  private static Sprite mainsp() {
    return Gfx.sprites.get("gahan");
  }
}
