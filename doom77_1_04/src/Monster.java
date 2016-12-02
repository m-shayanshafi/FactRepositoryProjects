public class Monster {
  public double x;
  public double y;
  public double radius;
  public double angle;
  public int life;
  public double lastShot;

  protected double lastDmgTime;

  private double shotInterval;
  private double speed;
  private double turnSpeed;
  private double gahanShoesAngle;
  private Sprite sprite;

  public Monster(Sprite sprite, int x, int y, double radius, int life,
      double angle, double speed, double turnSpeed, double shotInterval) {
    this.sprite = sprite;
    this.x = (x+0.5)*Engine.size;
    this.y = (y+0.5)*Engine.size;
    this.radius = radius;
    this.life = life;
    this.angle = angle;
    this.speed = speed;;
    this.turnSpeed = turnSpeed;
    this.shotInterval = shotInterval;
    lastShot = 0;
    gahanShoesAngle = 0;
    lastDmgTime = 0;
  }

  protected static void initSprites(String name) {
    if (Gfx.sprites.get("hearted"+name)==null) {
      Sprite sp = new Sprite("hearted"+name, Gfx.sprites.get(name));
      new B(sp).invertedBut(B.transp);
    }
    if (Gfx.sprites.get("shooting"+name)==null) {
      Sprite sp = new Sprite("shooting"+name, Gfx.sprites.get(name));
      new B(sp).whitened(0.8);
    }
  }

  public Sprite sp() {
    return sprite;
  }

  public void act() {
    if (!afterDmg()) {
      turnTo(Player.player.x, Player.player.y);
      up();
      shoot();
    }
  }

  public void dmg() {
    if (!afterDmg()) {
      life--;
      lastDmgTime = Doom77.time;
    }
  }

  public boolean afterDmg() {
    return Adventure.time(lastDmgTime, 1200);
  }

  public boolean afterShoot() {
    return Adventure.time(lastShot, shotInterval);
  }

  public void shoot() {
    if (!afterShoot() && !afterDmg()) {
      lastShot = Doom77.time;
      initEngine();
      Engine.shoot();
    }
  }

  protected void gahanShoes() {
    gahanShoesAngle = Mat.angle(turnSpeed*2.5*Doom77.step + gahanShoesAngle);
    x+=Math.sin(gahanShoesAngle)*Doom77.step*speed/1.5;
    y+=Math.cos(gahanShoesAngle)*Doom77.step*speed/1.5;
  }

  protected void turn(double k) {
    angle = Mat.angle(turnSpeed*Doom77.step*k+angle);
  }

  protected void strafe(int k) {
    if (k==-1) {
      x+=Math.sin(angle-Mat.hpi)*Doom77.step*speed;
      y+=Math.cos(angle-Mat.hpi)*Doom77.step*speed;
    } else {
      x+=Math.sin(angle+Mat.hpi)*Doom77.step*speed;
      y+=Math.cos(angle+Mat.hpi)*Doom77.step*speed;
    }
    initEngine();
    Engine.checkCollision();
    copyFromEngine();
  }

  protected void up() {
    x+=Math.sin(angle)*Doom77.step*speed;
    y+=Math.cos(angle)*Doom77.step*speed;
    initEngine();
    Engine.checkCollision();
    copyFromEngine();
  }


  protected void down() {
    x -= Math.sin(angle)*speed*Doom77.step;
    y -= Math.cos(angle)*speed*Doom77.step;
    initEngine();
    Engine.checkCollision();
    copyFromEngine();
  }

  protected void initEngine() {
    Engine.init(x, y, angle, radius);
    Engine.mon = this;
  }

  private void copyFromEngine() {
    x = Engine.x;
    y = Engine.y;
    angle = Engine.angle;
    radius = Engine.radius;
  }

  private void turnTo(double xt, double yt) {
    double a = Mat.angle(xt-x, yt-y);
    double closest = Mat.closest(angle, a);
    turn(closest/Math.abs(closest));
  }
}
