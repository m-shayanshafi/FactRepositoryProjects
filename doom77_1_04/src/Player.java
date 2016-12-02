public class Player extends Monster {
  public static final int maxLife = 5;

  public static Player player;
  public double deathTime;
  public double gahanShoesTime;

  protected static final double speed = Engine.size*0.001;
  protected static final double turnSpeed = Mat.dpi/1000/3;

  public Player() {
    super(null, 1, 1, 100, maxLife, 0.0001, speed, turnSpeed, 200);
    player = this;
    deathTime = 0;
    gahanShoesTime = 0;
    Console.init();
  }

  public void act() {
    if (life > 0) {
      if (Adventure.time(gahanShoesTime, 7000)) {
        gahanShoes();
      }
      if (Keyboard.shift()) {
        if (Keyboard.left()) strafe(-1);
        if (Keyboard.right()) strafe(1);
      } else {
        if (Keyboard.left()) turn(-1);
        if (Keyboard.right()) turn(1);
      }
      if (Keyboard.up()) up();
      if (Keyboard.down()) down();
      if (Keyboard.ctrl()) {
        shoot();
      }
    }
  }

  public void dmg() {
    super.dmg();
    if (life==0) deathTime = Doom77.time;
  }

  public void draw() {
    initEngine();
    Engine.draw();
  }
}
